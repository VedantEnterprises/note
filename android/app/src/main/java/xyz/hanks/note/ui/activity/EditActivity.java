package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.viewholder.NoteDetailTextViewHolder;
import xyz.hanks.note.ui.viewholder.NoteDetailViewHolder;
import xyz.hanks.note.ui.widget.LineTextView;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Edit note Activity
 * Created by hanks on 16/6/26.
 */
public class EditActivity extends AppCompatActivity {

    public static final String ATT_IMAGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "........";
    private final int ITEM_HEIGHT = 125;
    List<NoteItem> data = new ArrayList<>();
    List<String> backupData = new ArrayList<>();
    Map<Integer, List<String>> lineTextMap = new HashMap<>();
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "便签内容现在支持分享至新浪长微博同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签同<image w=858 h=383 describe= name=Note_123.jpg>时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签。\n";
    private NoteDetailAdapter noteDetailAdapter;
    private BackgroundAdapter backgroundAdapter;

    private ObservableRecyclerView listView;
    private ListView backgroundListView;

    private boolean draggable = false;
    private int lastScrollY = 0;
    private LinearLayoutManager layoutManager;


    public static void start(Context context) {
        Intent starter = new Intent(context, EditActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupUI();
    }

    private void setupUI() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑");
        toolbar.setNavigationIcon(VectorDrawableUtils.getBackDrawable(this));
        setSupportActionBar(toolbar);

        listView = (ObservableRecyclerView) findViewById(R.id.listView);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(null);


        noteDetailAdapter = new NoteDetailAdapter();
        listView.setAdapter(noteDetailAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NoteItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(listView);

        backgroundListView = (ListView) findViewById(R.id.backgroundListView);
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

        //        final View activityRootView = findViewById(R.id.activityRoot);
        //        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //            @Override
        //            public void onGlobalLayout() {
        //                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
        //                if (heightDiff > ScreenUtils.dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...
        //                    // hide
        //
        //                }
        //            }
        //        });
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
                NoteItem noteItem = new NoteItem();
                noteItem.type = 0;
                noteItem.content = tmp.substring(0, m);
                data.add(noteItem);
            }

            NoteItem noteItem = new NoteItem();

            int wIndex = imageString.indexOf("w=");
            int hIndex = imageString.indexOf("h=");
            int dIndex = imageString.indexOf("describe=");
            int nIndex = imageString.indexOf("name=");

            noteItem.type = 1;
            noteItem.width = Integer.parseInt(imageString.substring(wIndex + 2, hIndex - 1));
            noteItem.height = Integer.parseInt(imageString.substring(hIndex + 2, dIndex - 1));
            noteItem.describe = imageString.substring(dIndex + 9, nIndex - 1);
            noteItem.name = imageString.substring(nIndex + 5, imageString.length() - 1);
            noteItem.start = m;
            noteItem.end = n;
            data.add(noteItem);
            Log.e(TAG, imageString + "," + m + "," + n);
            tmp = tmp.substring(n);
        }
        if (!TextUtils.isEmpty(tmp)) {
            NoteItem noteItem = new NoteItem();
            noteItem.type = 0;
            noteItem.content = tmp;
            data.add(noteItem);
        }
        noteDetailAdapter.notifyItemRangeChanged(0, data.size());
        layoutManager.scrollToPositionWithOffset(0, lastScrollY);
    }

    private void calcBackupText() {

        backupData.clear();
        for (int i = 0; i < data.size(); i++) {
            List<String> strings = lineTextMap.get(i);
            if (data.get(i).type == 0) {// 文字
                backupData.addAll(strings);
            } else {
                backupData.add(strings.get(0));
            }
        }
        noteDetailAdapter.notifyItemRangeChanged(0, backupData.size());
        layoutManager.scrollToPositionWithOffset(0, lastScrollY);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_preview:
                PreviewActivity.start(this,noteContent);
                break;
        }

        return true;

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.menu_del).setIcon(VectorDrawableUtils.getDeleteDrawable(this));
        menu.findItem(R.id.menu_preview).setIcon(VectorDrawableUtils.getPreviewDrawable(this));
        return super.onCreateOptionsMenu(menu);
    }


    private void changeToDragMode() {
        draggable = true;
        calcBackupText();
    }

    private void changeToNormalMode() {
        draggable = false;
        calcText();
    }

    @NonNull private NoteItem getNoteItem(String str) {
        int wIndex = str.indexOf("w=");
        int hIndex = str.indexOf("h=");
        int dIndex = str.indexOf("describe=");
        int nIndex = str.indexOf("name=");
        NoteItem noteItem = new NoteItem();
        noteItem.type = 1;
        noteItem.width = Integer.parseInt(str.substring(wIndex + 2, hIndex - 1));
        noteItem.height = Integer.parseInt(str.substring(hIndex + 2, dIndex - 1));
        noteItem.describe = str.substring(dIndex + 9, nIndex - 1);
        noteItem.name = str.substring(nIndex + 5, str.length() - 1);
        return noteItem;
    }

    private void calcContentString() {
        StringBuilder sb = new StringBuilder();
        for (NoteItem noteItem : data) {
            if (noteItem.type == 0) {
                sb.append(noteItem.content);
            } else {
                sb.append(String.format(ATT_IMAGE_TAG, noteItem.width, noteItem.height, noteItem.describe, noteItem.name));
            }
        }
        noteContent = sb.toString();
    }

    class NoteItem {
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
                        NoteItem noteItem = data.get(adapterPosition);
                        noteItem.content = var1.toString();
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
                    NoteItem noteItem = getNoteItem(str);
                    //holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                    int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
                    final int finalHeight = itemCount * ITEM_HEIGHT;
                    Log.e(TAG, "ImageView" + finalHeight);
                    holder.rootLayout.getLayoutParams().height = finalHeight;
                } else {
                    holder.imgLayout.setVisibility(View.INVISIBLE);
                    holder.tv_line.setVisibility(View.VISIBLE);
                    holder.tv_line.setText(str);
                }

            } else {
                NoteItem noteItem = data.get(position);
                if (viewHolder instanceof NoteDetailTextViewHolder) {// 文字
                    final NoteDetailTextViewHolder textHolder = (NoteDetailTextViewHolder) viewHolder;
                    textHolder.lineTextView.setText(noteItem.content);
                    textHolder.lineTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                textHolder.lineTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                textHolder.lineTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            List<String> lineTextList = new ArrayList();
                            int lineCount = textHolder.lineTextView.getLayout().getLineCount();
                            for (int i = 0; i < lineCount; i++) {
                                int lineStart = textHolder.lineTextView.getLayout().getLineStart(i);
                                int lineEnd = textHolder.lineTextView.getLayout().getLineEnd(i);
                                String lineText = textHolder.lineTextView.getText().subSequence(lineStart, lineEnd).toString();
                                Log.e(TAG, lineStart + "," + lineEnd + "," + lineText + "," + lineCount);
                                lineTextList.add(lineText);
                            }
                            lineTextMap.put(position, lineTextList);
                            int finalHeight = ITEM_HEIGHT * lineCount;
                            Log.e(TAG, "rootLayout" + finalHeight);
                        }

                    });
                } else {
                    final NoteDetailViewHolder imageHolder = (NoteDetailViewHolder) viewHolder;
                    imageHolder.tv_line.setVisibility(View.INVISIBLE);
                    imageHolder.imgLayout.setVisibility(View.VISIBLE);
                    //holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                    List<String> lineTextList = new ArrayList();
                    lineTextList.add("<image w=" + noteItem.width + " h=" + noteItem.height + " describe=" + noteItem.describe + " name=" + noteItem.name + ">");
                    lineTextMap.put(position, lineTextList);
                    int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
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
                sb.append(s.trim());

                if (!isImage(s) && i + 1 < backupData.size() && !isImage(backupData.get(i + 1))) {
                    sb.append('\n');
                }
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
