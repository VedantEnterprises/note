package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import xyz.hanks.note.ui.widget.LineTextView;

/**
 * Edit note Activity
 * Created by hanks on 16/6/26.
 */
public class EditActivity extends AppCompatActivity {

    public static final String ATT_IAMGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "........";
    private final int ITEM_HEIGHT = 125;
    List<NoteItem> data = new ArrayList<>();
    List<String> backupData = new ArrayList<>();
    Map<Integer, List<String>> lineTextMap = new HashMap<>();
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "便签内容现在支持分享至新浪长微博。\n";
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
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
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
                Log.e(TAG, "onScrollChanged=" + scrollY);
                backgroundListView.setSelectionFromTop(0, -scrollY);
                lastScrollY = -scrollY;
            }

            @Override public void onDownMotionEvent() {
            }

            @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
        calcText();
    }

    private boolean isImage(String str) {
        Pattern pattern = Pattern.compile(ATT_IMAGE_PATTERN_STRING);
        return pattern.matcher(str).find();
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
        noteDetailAdapter.notifyItemRangeChanged(0,backupData.size());
        //listView.scrollVerticallyTo(lastScrollY);
        layoutManager.scrollToPositionWithOffset(0,lastScrollY);
        //        listView.setVisibility(View.INVISIBLE);
        //backupListView.setVisibility(View.VISIBLE);
        //backupAdapter.notifyDataSetChanged();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                draggable = !draggable;
                calcBackupText();
                break;
        }

        return true;

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
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
        draggable = false;
        noteDetailAdapter.notifyDataSetChanged();
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
            return convertView;
        }
    }

    class NoteDetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public LineTextView editText;
        public TextView tv_line;
        public View rootLayout;
        public View imgLayout;

        public NoteDetailViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.root_layout);
            imgLayout = itemView.findViewById(R.id.layout_img);
            editText = (LineTextView) itemView.findViewById(R.id.et_note_item);
            tv_line = (TextView) itemView.findViewById(R.id.tv_line);
            imageView = (ImageView) itemView.findViewById(R.id.iv_img_item);
        }
    }

    class NoteDetailAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NoteDetailViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_note_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
            final NoteDetailViewHolder holder = (NoteDetailViewHolder) viewHolder;
            holder.rootLayout.getLayoutParams().height = ITEM_HEIGHT;
            if (draggable) {
                String str = backupData.get(position).trim();
                holder.editText.setVisibility(View.INVISIBLE);
                if (isImage(str)) {
                    holder.tv_line.setVisibility(View.INVISIBLE);
                    holder.imageView.setVisibility(View.VISIBLE);
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
                    //holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                    int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
                    final int finalHeight = itemCount * ITEM_HEIGHT;
                    Log.e(TAG, "ImageView" + finalHeight);
                    holder.rootLayout.getLayoutParams().height = finalHeight;
                    holder.rootLayout.requestLayout();
                } else {
                    holder.imageView.setVisibility(View.INVISIBLE);
                    holder.tv_line.setVisibility(View.VISIBLE);
                    holder.tv_line.setText(str);
                }
                return;
            }

            holder.tv_line.setVisibility(View.INVISIBLE);
            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                holder.imageView.setVisibility(View.INVISIBLE);
                holder.editText.setVisibility(View.VISIBLE);
                if (draggable) {
                } else {
                    holder.editText.setText(noteItem.content);
                    holder.editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                holder.editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                holder.editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            List<String> lineTextList = new ArrayList();
                            int lineCount = holder.editText.getLayout().getLineCount();
                            for (int i = 0; i < lineCount; i++) {
                                int lineStart = holder.editText.getLayout().getLineStart(i);
                                int lineEnd = holder.editText.getLayout().getLineEnd(i);
                                String lineText = holder.editText.getText().subSequence(lineStart, lineEnd).toString();
                                Log.e(TAG, lineStart + "," + lineEnd + "," + lineText + "," + lineCount);
                                lineTextList.add(lineText);
                            }
                            lineTextMap.put(position, lineTextList);
                            int finalHeight = ITEM_HEIGHT * lineCount;
                            Log.e(TAG, "rootLayout" + finalHeight);
                            holder.rootLayout.getLayoutParams().height = finalHeight;
                            holder.rootLayout.requestLayout();
                        }

                    });
                }
            } else if (noteItem.type == 1) {
                holder.editText.setVisibility(View.INVISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                //holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                List<String> lineTextList = new ArrayList();
                lineTextList.add("<image w=" + noteItem.width + " h=" + noteItem.height + " describe=" + noteItem.describe + " name=" + noteItem.name + ">");
                lineTextMap.put(position, lineTextList);
                int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
                final int finalHeight = itemCount * ITEM_HEIGHT;
                Log.e(TAG, "ImageView" + finalHeight);
                holder.rootLayout.getLayoutParams().height = finalHeight;
            }
        }

        @Override public int getItemCount() {
            return draggable ? backupData.size() : data.size();
        }
    }

    class NoteItemTouchHelper extends ItemTouchHelper.Callback {

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
            return true;
        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }
}
