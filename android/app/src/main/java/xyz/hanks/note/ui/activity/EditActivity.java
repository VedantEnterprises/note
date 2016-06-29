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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.widget.MyFrameLayout;
import xyz.hanks.note.util.FileUtils;

/**
 * Edit note Activity
 * Created by hanks on 16/6/26.
 */
public class EditActivity extends AppCompatActivity {

    public static final String ATT_IAMGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "........";
    List<NoteItem> data = new ArrayList<>();
    List<String> backupData = new ArrayList<>();
    HashMap<Integer, Integer> itemHeightMap = new HashMap<>();
    private int ITEM_HEIGHT = 125;
    private ListView listView;
    private ListView backupListView;
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "\n" +
            "便签内容现在支持分享至新浪长微博。";
    private MyAdapter adapter;
    private MyBackAdapter backupAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, EditActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ITEM_HEIGHT = getResources().getDimensionPixelSize(R.dimen.note_detail_item_height);
        setupUI();
    }

    private void setupUI() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);


        MyFrameLayout frameLayout = (MyFrameLayout) findViewById(R.id.frameLayout);


        listView = frameLayout.listView;
//        backupListView = frameLayout.backListView;

//        backupListView = (ListView) findViewById(R.id.backListView);
//        backupAdapter = new MyBackAdapter();
//        backupListView.setAdapter(backupAdapter);


//        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new MyScrollListener(frameLayout.backListView));
//
//        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                backupListView.getLayoutParams().height = listView.getHeight();
//
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            }
//        });

        calcText(noteContent);
        calcBackupText();
    }

    private void calcBackupText() {

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
            return 100;
        }

        @Override public Object getItem(int position) {
            return null;
        }

        @Override public long getItemId(int position) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            Log.e(TAG,"MyBackAdapter.getView:"+position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail_back, parent, false);
//                convertView.setBackgroundDrawable(getDrawable(R.drawable.note_paper_middle));
            }
            final TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
             editText.setText(""+position);
            editText.setVisibility(View.VISIBLE);
            return convertView;
        }
    }

    private class MyScrollListener implements AbsListView.OnScrollListener {
        int offset;
        int oldVisibleItem = -1;
        int currentHeight;
        int prevHeight;
        private View mSyncedView;


        public MyScrollListener(View syncedView){
            if(syncedView == null){
                throw new IllegalArgumentException("syncedView is null");
            }
            mSyncedView = syncedView;
        }

        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            int[] location = new int[2];

            if(visibleItemCount == 0){
                return;
            }

            if(oldVisibleItem != firstVisibleItem){

                if(oldVisibleItem < firstVisibleItem){
                    prevHeight = currentHeight;
                    currentHeight = view.getChildAt(0).getHeight();

                    offset += prevHeight;

                }else{
                    currentHeight = view.getChildAt(0).getHeight();

                    View prevView;
                    if((prevView = view.getChildAt(firstVisibleItem - 1)) != null){
                        prevHeight = prevView.getHeight();
                    }else{
                        prevHeight = 0;
                    }

                    offset -= currentHeight;
                }

                oldVisibleItem = firstVisibleItem;
            }

            view.getLocationOnScreen(location);
            int listContainerPosition = location[1];

            view.getChildAt(0).getLocationOnScreen(location);
            int currentLocation = location[1];

            int blah = listContainerPosition - currentLocation + offset;

            mSyncedView.scrollTo(0, blah);

//            Hack.scrollListBy(mSyncedView,blah);
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

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

        @Override public View getView(int position, View convertView, ViewGroup parent) {
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
                            int lineCount = editText.getLayout().getLineCount();
                            for (int i = 0; i < lineCount; i++) {
                                int lineStart = editText.getLayout().getLineStart(i);
                                int lineEnd = editText.getLayout().getLineEnd(i);
                                String lineText = editText.getText().subSequence(lineStart, lineEnd).toString();
                                Log.e(TAG, lineStart + "," + lineEnd + "," + lineText + "," + lineCount);
                                //                                backupData.add(lineText);
                                //                                backupAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

            } else if (noteItem.type == 1) {

                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));

                if (itemHeightMap.containsKey(position)) {
                    int height = itemHeightMap.get(position);
                    imageView.getLayoutParams().height = height;
                    return convertView;
                }



                int totalHeight = 0;
                for (int i = 0; i < position; i++) {
                    View child = listView.getChildAt(i);
                    totalHeight += child.getMeasuredHeight();
                }

                int gap = 0;
                if (totalHeight % ITEM_HEIGHT != 0) {
                    gap = ITEM_HEIGHT - totalHeight % ITEM_HEIGHT;
                }

                int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height / ITEM_HEIGHT : noteItem.height / ITEM_HEIGHT + 1;


                Log.e(TAG, totalHeight + "??????????????????" + gap + "," + (itemCount * ITEM_HEIGHT));
                int finalHeight = itemCount * ITEM_HEIGHT + gap;
                imageView.getLayoutParams().height = finalHeight;
                itemHeightMap.put(position, finalHeight);
                if ((Boolean) convertView.getTag()) {
                    convertView.setTag(false);
                    //                    for (int i = 0; i < itemCount; i++) {
                    //                        backupData.add("");
                    //                    }
                    //                    backupAdapter.notifyDataSetChanged();
                }
            }
            //convertView.setBackgroundDrawable(getDrawable(R.drawable.listview_bg));
            return convertView;
        }
    }
}
