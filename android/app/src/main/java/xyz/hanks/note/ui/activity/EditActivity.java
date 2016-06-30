package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;
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
    private ObservableListView listView;
    private ListView backupListView;
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "\n" +
            "便签内容现在支持分享至新浪长微博。";
    private MyAdapter adapter;
    private MyBackAdapter backupAdapter;
    private boolean draggable;

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

        listView = (ObservableListView) findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        backupListView = (ListView) findViewById(R.id.backListView);
        backupAdapter = new MyBackAdapter();
        backupListView.setAdapter(backupAdapter);

        listView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                Log.e(TAG, "onScrollChanged=" + scrollY);
                backupListView.setSelectionFromTop(0, -scrollY);
            }

            @Override public void onDownMotionEvent() {
            }

            @Override public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
        calcText(noteContent);
    }

    private void calcBackupText() {
        backupData.clear();

        for (Integer integer : lineTextMap.keySet()) {
            Log.e(TAG,integer+"lineTextMap");
        }
        for(int i=0;i<10000;i++){
            if (lineTextMap.containsKey(i)) {
                List<String> strings = lineTextMap.get(i);
                backupData.addAll(strings);
            }else {
                break;
            }
        }
        listView.setVisibility(View.GONE);
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

    private void calcText(String noteContent) {
        if (noteContent == null) {
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
        adapter.notifyDataSetChanged();
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

    class MyBackAdapter extends BaseAdapter {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail_back, parent, false);
            }
            final TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
            if (draggable && position < backupData.size()) {
                editText.setText(backupData.get(position));
            } else {
                editText.setText("" + position);
            }
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
                convertView.setTag(true);
            }
            final TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);
            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                editText.setText(noteItem.content);
                imageView.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                if ((Boolean) convertView.getTag()) {
                    convertView.setTag(false);
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
                        }
                    });
                }

            } else if (noteItem.type == 1) {

                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;

                int totalHeight = 0;
                for (int i = 0; i < position; i++) {
                    View child = listView.getChildAt(i);
                    totalHeight += child.getMeasuredHeight();
                }
                int gap = 0;
                if (totalHeight % ITEM_HEIGHT != 0) {
                    gap = ITEM_HEIGHT - totalHeight % ITEM_HEIGHT;
                }
                Log.e(TAG, totalHeight + "??????????????????" + gap + "," + (itemCount * ITEM_HEIGHT + gap));
                imageView.getLayoutParams().height = itemCount * ITEM_HEIGHT + gap;

                List<String> lineTextList = new ArrayList();
                for (int i = 0; i < itemCount; i++) {
                    lineTextList.add("");
                }
                lineTextMap.put(position, lineTextList);
            }
            //convertView.setBackgroundDrawable(getDrawable(R.drawable.listview_bg));
            return convertView;
        }
    }
}
