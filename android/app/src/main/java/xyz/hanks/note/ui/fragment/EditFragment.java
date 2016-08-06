package xyz.hanks.note.ui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.realm.Realm;
import io.realm.RealmResults;
import xyz.hanks.note.R;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.activity.PreviewActivity;
import xyz.hanks.note.ui.viewholder.NoteDetailTextViewHolder;
import xyz.hanks.note.ui.viewholder.NoteDetailViewHolder;
import xyz.hanks.note.ui.widget.LineTextView;
import xyz.hanks.note.util.FileUtils;
import xyz.hanks.note.util.ScreenUtils;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 2016/8/2.
 */
public class EditFragment extends BaseFragment {

    public static final String ATT_IMAGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "........";
    private static final String EXTRA_ID = "note_id";
    List<NoteItemView> data = new ArrayList<>();
    List<String> backupData = new ArrayList<>();
    private int ITEM_HEIGHT = 125;
    private String noteContent = "\n\n";
    private NoteDetailAdapter noteDetailAdapter;
    private BackgroundAdapter backgroundAdapter;

    private ObservableRecyclerView listView;
    private ListView backgroundListView;

    private boolean draggable = false;
    private int lastScrollY = 0;
    private LinearLayoutManager layoutManager;
    private String noteId = "";
    private FloatingActionButton floatingActionButton;
    private View rootView;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            rootView.getWindowVisibleDisplayFrame(r);

            int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
            if (heightDiff > ScreenUtils.dpToPx(100)) { // if more than 100 pixels, its probably a keyboard...
                floatingActionButton.setImageDrawable(VectorDrawableUtils.getSaveDrawable(getContext()));
            } else {
                floatingActionButton.setImageDrawable(VectorDrawableUtils.getPreviewDrawable(getContext()));
            }
        }
    };

    public static EditFragment newInstance(String noteId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, noteId);
        EditFragment fragment = new EditFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditFragment newInstance() {
        return newInstance("");
    }

    public void measureText() {
        backupData.clear();
        View view = View.inflate(getContext(), R.layout.item_list_note_detail_text, null);
        TextView textView = (TextView) view.findViewById(R.id.et_note_item);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ScreenUtils.getDeviceWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        for (NoteItemView noteItemView : data) {
            if (noteItemView.type == 0) {
                textView.setText(noteItemView.content);
                textView.measure(widthMeasureSpec, heightMeasureSpec);
                Layout layout = textView.getLayout();
                int lineCount = layout.getLineCount();
                for (int i = 0; i < lineCount; i++) {
                    int lineStart = layout.getLineStart(i);
                    int lineEnd = layout.getLineEnd(i);
                    String lineText = textView.getText().subSequence(lineStart, lineEnd).toString();
                    backupData.add(lineText);
                }
            } else {
                backupData.add(noteItemView.content);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteId = getArguments().getString(EXTRA_ID, "");
        if (!TextUtils.isEmpty(noteId)) {
            Realm realm = Realm.getDefaultInstance();
            NoteItem noteItem = realm.where(NoteItem.class).equalTo(NoteItem.OBJECT_ID, noteId).findFirst();
            if (noteItem != null) {
                noteContent = noteItem.detail;
            }
            realm.close();
        }
        setupUI();
        ITEM_HEIGHT = (int) getResources().getDimension(R.dimen.note_detail_item_height);
    }

    private void setupUI() {
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("编辑");
        toolbar.setNavigationIcon(VectorDrawableUtils.getBackDrawable(getContext()));

        listView = (ObservableRecyclerView) getView().findViewById(R.id.listView);
        layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(null);


        noteDetailAdapter = new NoteDetailAdapter();
        listView.setAdapter(noteDetailAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NoteItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(listView);

        backgroundListView = (ListView) getView().findViewById(R.id.backgroundListView);
        backgroundAdapter = new BackgroundAdapter();
        backgroundListView.setAdapter(backgroundAdapter);

        listView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                backgroundListView.setSelectionFromTop(0, -scrollY);
                lastScrollY = -scrollY;
            }

            @Override public void onDownMotionEvent() {
            }

            @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
        calcText();

        rootView = getView().findViewById(R.id.edit_root_layout);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (floatingActionButton != null) {
            floatingActionButton.setImageDrawable(VectorDrawableUtils.getAddDrawable(getContext()));
        }
        if (rootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
            } else {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(layoutListener);
            }
        }
    }

    private boolean isImage(String str) {
        Pattern pattern = Pattern.compile(ATT_IMAGE_PATTERN_STRING);
        return pattern.matcher(str).find();
    }

    private void calcText() {
        if (TextUtils.isEmpty(noteContent)) {
            noteContent = "";
        }

        Pattern pattern = Pattern.compile(ATT_IMAGE_PATTERN_STRING);
        data.clear();
        String tmp = noteContent;
        while (true) {
            Matcher localMatcher = pattern.matcher(tmp);
            boolean result = localMatcher.find();
            if (!result) {
                break;
            }
            int m = localMatcher.start();
            int n = localMatcher.end();
            String imageString = tmp.substring(m, n);

            if (m > 0) {
                NoteItemView noteItemView = new NoteItemView();
                noteItemView.type = 0;
                noteItemView.content = tmp.substring(0, m);
                data.add(noteItemView);
            }

            NoteItemView noteItemView = new NoteItemView();
            noteItemView.content = imageString;
            int wIndex = imageString.indexOf("w=");
            int hIndex = imageString.indexOf("h=");
            int dIndex = imageString.indexOf("describe=");
            int nIndex = imageString.indexOf("name=");

            noteItemView.type = 1;
            noteItemView.width = Integer.parseInt(imageString.substring(wIndex + 2, hIndex - 1));
            noteItemView.height = Integer.parseInt(imageString.substring(hIndex + 2, dIndex - 1));
            noteItemView.describe = imageString.substring(dIndex + 9, nIndex - 1);
            noteItemView.name = imageString.substring(nIndex + 5, imageString.length() - 1);
            noteItemView.start = m;
            noteItemView.end = n;
            data.add(noteItemView);

            Log.e(TAG, imageString + "," + m + "," + n);
            tmp = tmp.substring(n);
        }
        if (!TextUtils.isEmpty(tmp)) {
            NoteItemView noteItemView = new NoteItemView();
            noteItemView.type = 0;
            noteItemView.content = tmp;
            data.add(noteItemView);
        }
        noteDetailAdapter.notifyItemRangeChanged(0, data.size());
        layoutManager.scrollToPositionWithOffset(0, lastScrollY);
    }

    private void calcBackupText() {
        measureText();
        noteDetailAdapter.notifyItemRangeChanged(0, backupData.size());
        layoutManager.scrollToPositionWithOffset(0, lastScrollY);
    }

    private void updateNote(final NoteItem tmp) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tmp);
            }
        });
        realm.close();
    }

    private void saveNote() {
        measureText();
        if (backupData.size() <= 0) {
            return;
        }
        NoteItem tmp;
        if (TextUtils.isEmpty(noteId)) {
            tmp = new NoteItem();
            tmp.objectId = UUID.randomUUID().toString();
            tmp.createdAt = System.currentTimeMillis();
        } else {
            Realm realm = Realm.getDefaultInstance();
            NoteItem noteItem = realm.where(NoteItem.class).equalTo(NoteItem.OBJECT_ID, noteId).findFirst();
            tmp = realm.copyFromRealm(noteItem);
            realm.close();
        }
        tmp.updatedAt = System.currentTimeMillis();
        tmp.detail = noteContent;
        tmp.title = "no title";
        for (String s : backupData) {
            if (!TextUtils.isEmpty(s) && !isImage(s)) {
                tmp.title = s;
                break;
            }
        }
        updateNote(tmp);
        Toast.makeText(getContext(), "save success!", Toast.LENGTH_SHORT).show();
    }

    private void deleteNote() {
        if (!TextUtils.isEmpty(noteId)) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    RealmResults<NoteItem> noteItems = realm.where(NoteItem.class).equalTo(NoteItem.OBJECT_ID, noteId).findAll();
                    noteItems.deleteAllFromRealm();
                }
            });
            realm.close();
            getFragmentManager().popBackStack();
        }
    }

    private void insertImage() {
        int cursorPosition = getCursorPosition();
        GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
            @Override public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList.size() > 0) {
                    String photoPath = resultList.get(0).getPhotoPath();
                    String fileName = FileUtils.saveImage(photoPath);
                    if (!TextUtils.isEmpty(fileName)) {
                        String imageTag = String.format(ATT_IMAGE_TAG, 300, 400, fileName, fileName);
                        noteContent = noteContent + imageTag;
                        calcText();
                    }
                }
            }

            @Override public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
        /*new AlertDialog.Builder(this)
                .setTitle("选择照片")
                .setItems(new String[]{"相机","图库"}, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();*/
    }

    private int getCursorPosition() {
        return 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.menu_del).setIcon(VectorDrawableUtils.getDeleteDrawable(getContext()));
        menu.findItem(R.id.menu_preview).setIcon(VectorDrawableUtils.getPreviewDrawable(getContext()));
        menu.findItem(R.id.menu_img).setIcon(VectorDrawableUtils.getImageDrawable(getContext()));
        menu.findItem(R.id.menu_save).setIcon(VectorDrawableUtils.getSaveDrawable(getContext()));
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
            case R.id.menu_del:
                deleteNote();
                break;
            case R.id.menu_img:
                insertImage();
                break;
            case R.id.menu_save:
                saveNote();
                break;
            case R.id.menu_preview:
                PreviewActivity.start(getContext(), noteContent);
                break;
        }
        return true;
    }

    private void changeToDragMode() {
        draggable = true;
        calcBackupText();
    }

    private void changeToNormalMode() {
        draggable = false;
        calcText();
    }

    @NonNull
    private NoteItemView getNoteItem(String str) {
        int wIndex = str.indexOf("w=");
        int hIndex = str.indexOf("h=");
        int dIndex = str.indexOf("describe=");
        int nIndex = str.indexOf("name=");
        NoteItemView noteItemView = new NoteItemView();
        noteItemView.type = 1;
        noteItemView.width = Integer.parseInt(str.substring(wIndex + 2, hIndex - 1));
        noteItemView.height = Integer.parseInt(str.substring(hIndex + 2, dIndex - 1));
        noteItemView.describe = str.substring(dIndex + 9, nIndex - 1);
        noteItemView.name = str.substring(nIndex + 5, str.length() - 1);
        return noteItemView;
    }

    private void calcContentString() {
        StringBuilder sb = new StringBuilder();
        for (NoteItemView noteItemView : data) {
            if (noteItemView.type == 0) {
                sb.append(noteItemView.content);
            } else {
                sb.append(String.format(ATT_IMAGE_TAG, noteItemView.width, noteItemView.height, noteItemView.describe, noteItemView.name));
            }
        }
        noteContent = sb.toString();
    }

    class NoteItemView {
        int type; // 0 文字 1 图片
        String content; // 如果是文字的内容
        String name;
        String describe;
        int width;
        int height;
        int start;
        int end;
    }

    // 背景
    class BackgroundAdapter extends BaseAdapter {
        @Override public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override public Object getItem(int i) {
            return null;
        }

        @Override public long getItemId(int i) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail_background, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.et_note_item);
            // tv.setText("" + position);
            return convertView;
        }
    }

    class NoteDetailAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) { // drag
                return NoteDetailViewHolder.newInstance(parent);
            } else if (viewType == 1) { // lineTextView
                final NoteDetailTextViewHolder detailTextViewHolder = NoteDetailTextViewHolder.newInstance(parent);
                detailTextViewHolder.lineTextView.addTextWatcher(new LineTextView.TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                    }

                    @Override
                    public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                        int adapterPosition = detailTextViewHolder.getAdapterPosition();
                        NoteItemView noteItemView = data.get(adapterPosition);
                        noteItemView.content = var1.toString();
                        calcContentString();

                    }

                    @Override public void afterTextChanged(Editable var1) {

                    }
                });
                return detailTextViewHolder;
            } else {
                NoteDetailViewHolder noteDetailViewHolder = NoteDetailViewHolder.newInstance(parent);
                noteDetailViewHolder.setListener(new NoteDetailViewHolder.OnImageHandleTouchListener() {
                    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            changeToDragMode();
                        }
                        return false;
                    }
                });
                return noteDetailViewHolder;
            }
        }

        @Override public int getItemViewType(int position) {
            if (draggable) {
                return 0;
            }
            if (data.get(position).type == 0) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

            if (draggable) {
                final NoteDetailViewHolder holder = (NoteDetailViewHolder) viewHolder;
                holder.rootLayout.getLayoutParams().height = ITEM_HEIGHT;
                String str = backupData.get(position).trim();
                if (isImage(str)) {
                    holder.tv_line.setVisibility(View.INVISIBLE);
                    holder.imgLayout.setVisibility(View.VISIBLE);
                    NoteItemView noteItemView = getNoteItem(str);
                    holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItemView.name));
                    int itemCount = noteItemView.height % ITEM_HEIGHT == 0 ? noteItemView.height / ITEM_HEIGHT : noteItemView.height / ITEM_HEIGHT + 1;
                    final int finalHeight = itemCount * ITEM_HEIGHT;
                    Log.e(TAG, "ImageView" + finalHeight);
                    holder.rootLayout.getLayoutParams().height = finalHeight;
                } else {
                    holder.imgLayout.setVisibility(View.INVISIBLE);
                    holder.tv_line.setVisibility(View.VISIBLE);
                    holder.tv_line.setText(str);
                }

            } else {
                NoteItemView noteItemView = data.get(position);
                if (viewHolder instanceof NoteDetailTextViewHolder) {// 文字
                    final NoteDetailTextViewHolder textHolder = (NoteDetailTextViewHolder) viewHolder;
                    textHolder.lineTextView.setText(noteItemView.content);
                } else {
                    final NoteDetailViewHolder imageHolder = (NoteDetailViewHolder) viewHolder;
                    imageHolder.tv_line.setVisibility(View.INVISIBLE);
                    imageHolder.imgLayout.setVisibility(View.VISIBLE);
                    imageHolder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItemView.name));
                    int itemCount = noteItemView.height % ITEM_HEIGHT == 0 ? noteItemView.height / ITEM_HEIGHT : noteItemView.height / ITEM_HEIGHT + 1;
                    final int finalHeight = itemCount * ITEM_HEIGHT;
                    Log.e(TAG, "ImageView" + finalHeight);
                    imageHolder.rootLayout.getLayoutParams().height = finalHeight;
                }
            }
        }

        @Override public int getItemCount() {
            return draggable ? backupData.size() : data.size();
        }

        public void onDrop(Integer from, Integer to) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < backupData.size(); i++) {
                String s = backupData.get(i);
                sb.append(s);

                //                if (!isImage(s) && i + 1 < backupData.size() && !isImage(backupData.get(i + 1))) {
                //                    sb.append('\n');
                //                }
            }
            noteContent = sb.toString();
            changeToNormalMode();
        }
    }

    class NoteItemTouchHelper extends ItemTouchHelper.Callback {

        private Integer mFrom = null;
        private Integer mTo = null;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeFlag = 0;
            int dragFlag = draggable ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
            return makeMovementFlags(dragFlag, swipeFlag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(backupData, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(backupData, i, i - 1);
                }
            }
            noteDetailAdapter.notifyItemMoved(fromPosition, toPosition);

            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }

            // remember FIRST from position
            if (mFrom == null)
                mFrom = viewHolder.getAdapterPosition();
            mTo = target.getAdapterPosition();

            return true;

        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setAlpha(0.6f);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1f);
            if (mFrom != null && mTo != null)
                noteDetailAdapter.onDrop(mFrom, mTo);
            // clear saved positions
            mFrom = mTo = null;
        }
    }
}
