package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
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
    List<NoteItem> data = new ArrayList<>();
    List<NoteItem> backupData = new ArrayList<>();
    private ListView listView;
    private ListView backupListView;
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "\n" +
            "便签内容现在支持分享至新浪长微博。";
    private MyAdapter adapter;
    private MyBackAdapter backupAdapter;

    private final int ITEM_HEIGHT = 100;

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

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        backupListView = (ListView) findViewById(R.id.backListView);
        backupAdapter = new MyBackAdapter();
        backupListView.setAdapter(backupAdapter);

        calcText(noteContent);
        calcBackupText();
    }

    private void calcBackupText() {
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {

            }
        });
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
            return data.size();
        }

        @Override public Object getItem(int position) {
            return null;
        }

        @Override public long getItemId(int position) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            Log.e(TAG,position+"....................");
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail, parent, false);
            }
            final TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);

            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                editText.setText(noteItem.content);
                imageView.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                Log.e(TAG,position+"....................");
                editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }else {
                            editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        int lineCount = editText.getLayout().getLineCount();
                        for (int i = 0; i < lineCount; i++) {
                            int lineStart = editText.getLayout().getLineStart(i);
                            int lineEnd = editText.getLayout().getLineEnd(i);
                            String lineText = editText.getText().subSequence(lineStart,lineEnd).toString();
                            Log.e(TAG,lineStart+","+lineEnd+","+lineText+","+lineCount);
                        }
                    }
                });
            } else if (noteItem.type == 1) {
                Log.e(TAG,position+"..................noteItem.."+noteItem.height);
                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                imageView.getLayoutParams().height = noteItem.height;

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

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail, parent, false);
            }


            TextView editText = (TextView) convertView.findViewById(R.id.et_note_item);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);
//            if(position==data.size()){
//                editText.setText("     ");
//                editText.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.GONE);
//                convertView.setBackgroundDrawable(getDrawable(R.drawable.listview_bg));
//                return convertView;
//            }
            NoteItem noteItem = data.get(position);
            if (noteItem.type == 0) {
                editText.setText(noteItem.content);
                imageView.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);

            } else if (noteItem.type == 1) {
                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                imageView.getLayoutParams().height = noteItem.height;
            }
            //convertView.setBackgroundDrawable(getDrawable(R.drawable.listview_bg));
            return convertView;
        }
    }
    /**
     * http://stackoverflow.com/questions/18966943/how-does-a-android-textview-breaks-the-text-into-lines
     * Returns the original text if it fits in the specified width (<code>avail</code>) with
     * the properties of the specified TextAppearanceSpan (<code>textAppearance</code>), or,
     * if it does not fit, a truncated copy with ellipsis character added at the end
     * (<code>TextUtils.TruncateAt.END</code>).
     *
     * @param paint
     *           the TextPaint from the TextView
     * @param text
     *           the text to truncated
     * @param textAppearance
     *           text typeface, size, and style
     * @param avail
     *           the available width
     * @param maxLines
     *           maximum number of displayed lines
     * @return the original text or a truncated copy
     * @see TextView#getPaint()
     * @see TextAppearanceSpan
     * @see TextUtils.TruncateAt#END
     */
    public static String ellipsize(final TextPaint paint,
                                   final String text,
                                   final TextAppearanceSpan textAppearance,
                                   final int avail,
                                   final int maxLines) {

        if (TextUtils.isEmpty(text)) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();

        paint.setTextSize(textAppearance.getTextSize());
        paint.setTypeface(Typeface.create(textAppearance.getFamily(), textAppearance.getTextStyle()));

        final StaticLayout layout = new StaticLayout(text, paint, avail, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        final int lineCount = layout.getLineCount();

        final int lines = lineCount < maxLines ? lineCount : maxLines;

        for (int i = 0; i < lines; i++) {

            final int lineStart = layout.getLineStart(i);
            final int lineEnd = layout.getLineEnd(i);
            String substring = text.substring(lineStart, lineEnd);

            if ((i == (maxLines - 1)) && (lineCount > maxLines)) {

                final String lastLine = substring.concat("\u2026");
                substring = TextUtils.ellipsize(lastLine, paint, avail, TextUtils.TruncateAt.END, true, null).toString();
            }

            builder.append(substring);
        }

        return builder.toString();
    }
}
