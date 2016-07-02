package xyz.hanks.note.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.widget.LineTextView;
import xyz.hanks.note.ui.widget.draglist.DragSortListView;
import xyz.hanks.note.util.FileUtils;

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
    private ObservableRecyclerView listView;
    private DragSortListView backupListView;
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "便签内容现在支持分享至新浪长微博。\n";
    // private MyAdapter adapter;
    private NoteDetailAdapter noteDetailAdapter;
    private MyBackAdapter backupAdapter;
    private BackgroundAdapter backgroundAdapter;
    private boolean draggable;
    private ListView backgroundListView;
    //    private DragSortListView.DragListener onDrag =
    //            new DragSortListView.DragListener() {
    //                @Override public void drag(int from, int to) {
    //                    if("[图]".equals(backupData.get(from))){
    //                        backupListView.setDragEnabled(true);
    //                    }else {
    //                        backupListView.setDragEnabled(false);
    //                    }
    //                }
    //            };
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {

                    String fromItem = backupData.get(from);
                    String toItem = backupData.get(to);
                    backupData.set(from, toItem);
                    backupData.set(to, fromItem);
                    backupAdapter.notifyDataSetChanged();

                    StringBuilder sb = new StringBuilder();
                    for (String s : backupData) {
                        sb.append(s);
                    }
                    noteContent = sb.toString();

                    calcText();

                }
            };

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
        // adapter = new MyAdapter();
        // listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        noteDetailAdapter = new NoteDetailAdapter();
        listView.setAdapter(noteDetailAdapter);

        backgroundListView = (ListView) findViewById(R.id.backgroundListView);
        backgroundAdapter = new BackgroundAdapter();
        backgroundListView.setAdapter(backgroundAdapter);

        backupListView = (DragSortListView) findViewById(R.id.backListView);
        backupAdapter = new MyBackAdapter();
        backupListView.setAdapter(backupAdapter);
        //        backupListView.setDragListener(onDrag);
        backupListView.setDropListener(onDrop);


        //        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                if (data.get(position).type == 1) {// image
        //                    draggable = true;
        //                    calcBackupText();
        //                }
        //            }
        //        });

        listView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                Log.e(TAG, "onScrollChanged=" + scrollY);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    backgroundListView.setSelectionFromTop(0, -scrollY);
                }
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

        for (Integer integer : lineTextMap.keySet()) {
            Log.e(TAG, integer + "lineTextMap");
        }
        for (int i = 0; i < data.size(); i++) {

            List<String> strings = lineTextMap.get(i);
            if (data.get(i).type == 0) {// 文字
                backupData.addAll(strings);
            } else {
                backupData.add(strings.get(0));
            }
        }
        listView.setVisibility(View.GONE);
        backupListView.setVisibility(View.VISIBLE);
        backupAdapter.notifyDataSetChanged();
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
            return;
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
        listView.setVisibility(View.VISIBLE);
        backupListView.setVisibility(View.GONE);
        // adapter.notifyDataSetChanged();
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

    class MyBackAdapter extends BaseAdapter {
        @Override public int getCount() {
            return backupData.size();
        }

        @Override public Object getItem(int i) {
            return null;
        }

        @Override public long getItemId(int i) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail_back, parent, false);
            }
            final TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
            //            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);
            //            String item = backupData.get(position);
            //            if (!isImage(item)) {
            if (draggable && position < backupData.size()) {
                editText.setText(backupData.get(position));
            } else {
                editText.setText("" + position);
            }
            editText.getLayoutParams().height = ITEM_HEIGHT;
            //                imageView.setVisibility(View.GONE);
            //                editText.setVisibility(View.VISIBLE);
            //            } else {
            //
            //
            //                NoteItem noteItem = new NoteItem();
            //
            //                int wIndex = item.indexOf("w=");
            //                int hIndex = item.indexOf("h=");
            //                int dIndex = item.indexOf("describe=");
            //                int nIndex = item.indexOf("name=");
            //
            //                noteItem.type = 1;
            //                noteItem.width = Integer.parseInt(item.substring(wIndex + 2, hIndex - 1));
            //                noteItem.height = Integer.parseInt(item.substring(hIndex + 2, dIndex - 1));
            //                noteItem.describe = item.substring(dIndex + 9, nIndex - 1);
            //                noteItem.name = item.substring(nIndex + 5, item.length() - 1);
            //
            //                editText.setVisibility(View.GONE);
            //                imageView.setVisibility(View.VISIBLE);
            //                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
            //                final int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
            //                int totalHeight = 0;
            //                for (int i = 0; i < position; i++) {
            //                    View child = backupListView.getChildAt(i);
            //                    totalHeight += child.getMeasuredHeight();
            //                }
            //                int gap = 0;
            //                if (totalHeight % ITEM_HEIGHT != 0) {
            //                    gap = ITEM_HEIGHT - totalHeight % ITEM_HEIGHT;
            //                }
            //                final int finalHeight = itemCount * ITEM_HEIGHT + gap;
            //                Log.e(TAG, totalHeight + "??????????????????" + gap + "," + finalHeight);
            //                imageView.getLayoutParams().height = finalHeight;
            //
            //
            //
            //            }
            return convertView;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override public int getCount() {
            return data.size();
        }

        @Override public Object getItem(int position) {
            return null;
        }

        @Override public long getItemId(int position) {
            return 0;
        }

        @Override public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail, parent, false);
            }
            final View rootLayout = convertView.findViewById(R.id.root_layout);
            final LineTextView editText = (LineTextView) rootLayout.findViewById(R.id.et_note_item);
            final ImageView imageView = (ImageView) rootLayout.findViewById(R.id.iv_img_item);
            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                editText.setText(noteItem.content);
                imageView.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        List<String> lineTextList = new ArrayList();
                        int lineCount = editText.getLayout().getLineCount();
                        for (int i = 0; i < lineCount; i++) {
                            int lineStart = editText.getLayout().getLineStart(i);
                            int lineEnd = editText.getLayout().getLineEnd(i);
                            String lineText = editText.getText().subSequence(lineStart, lineEnd).toString();
                            Log.e(TAG, lineStart + "," + lineEnd + "," + lineText + "," + lineCount);
                            lineTextList.add(lineText);
                        }
                        lineTextMap.put(position, lineTextList);
                        int finalHeight = ITEM_HEIGHT * lineCount;
                        Log.e(TAG, "rootLayout" + finalHeight);
                        rootLayout.getLayoutParams().height = finalHeight;
                        rootLayout.requestLayout();
                    }
                });
            } else if (noteItem.type == 1) {

                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;

                //                int totalHeight = 0;
                //                for (int i = 0; i < position; i++) {
                //                    View child = listView.getChildAt(i);
                //                    totalHeight += child.getMeasuredHeight();
                //                }
                //                int gap = 0;
                //                if (totalHeight % ITEM_HEIGHT != 0) {
                //                    gap = ITEM_HEIGHT - totalHeight % ITEM_HEIGHT;
                //                }
                final int finalHeight = itemCount * ITEM_HEIGHT;
                Log.e(TAG, "ImageView" + finalHeight);
                //                Log.e(TAG, totalHeight + "??????????????????" + gap + "," + finalHeight);
                rootLayout.getLayoutParams().height = finalHeight;
                rootLayout.requestLayout();

                List<String> lineTextList = new ArrayList();
                lineTextList.add("<image w=" + noteItem.width + " h=" + noteItem.height + " describe=" + noteItem.describe + " name=" + noteItem.name + ">");
                lineTextMap.put(position, lineTextList);

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override public boolean onTouch(View v, MotionEvent event) {

                        ValueAnimator valueAnimator = ValueAnimator.ofInt(finalHeight, ITEM_HEIGHT).setDuration(300);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                Log.e(TAG, "animation:" + value);
                                imageView.getLayoutParams().height = value;
                                imageView.requestLayout();
                            }
                        });
                        valueAnimator.addListener(new Animator.AnimatorListener() {
                            @Override public void onAnimationStart(Animator animation) {

                            }

                            @Override public void onAnimationEnd(Animator animation) {
                                draggable = true;
                                calcBackupText();
                            }

                            @Override public void onAnimationCancel(Animator animation) {

                            }

                            @Override public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        valueAnimator.start();
                        return true;
                    }
                });
            }
            //convertView.setBackgroundDrawable(getDrawable(R.drawable.listview_bg));
            return convertView;
        }
    }

    class NoteDetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public LineTextView editText;
        public View rootLayout;

        public NoteDetailViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.root_layout);
            editText = (LineTextView) itemView.findViewById(R.id.et_note_item);
            imageView = (ImageView) itemView.findViewById(R.id.iv_img_item);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View v, MotionEvent event) {
                    NoteItem noteItem = data.get(getAdapterPosition());
                    int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;
                    final int finalHeight = itemCount * ITEM_HEIGHT;
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(finalHeight, ITEM_HEIGHT).setDuration(300);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animation.getAnimatedValue();
                            rootLayout.getLayoutParams().height = value;
                            rootLayout.requestLayout();
                        }
                    });
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        @Override public void onAnimationStart(Animator animation) {

                        }

                        @Override public void onAnimationEnd(Animator animation) {
                            //draggable = true;
                            //calcBackupText();
                        }

                        @Override public void onAnimationCancel(Animator animation) {

                        }

                        @Override public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    valueAnimator.start();
                    return true;
                }
            });
        }
    }

    class NoteDetailAdapter extends RecyclerView.Adapter<NoteDetailViewHolder> {

        @Override
        public NoteDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail, parent, false);
            return new NoteDetailViewHolder(view);
        }

        @Override public void onBindViewHolder(final NoteDetailViewHolder holder, final int position) {
            holder.rootLayout.getLayoutParams().height = ITEM_HEIGHT;
            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                holder.editText.setText(noteItem.content);
                holder.imageView.setVisibility(View.GONE);
                holder.editText.setVisibility(View.VISIBLE);
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
            } else if (noteItem.type == 1) {

                holder.editText.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));

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
            return data.size();
        }
    }
}
