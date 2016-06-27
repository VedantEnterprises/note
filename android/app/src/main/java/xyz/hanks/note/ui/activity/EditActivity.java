package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;

/**
 * Edit note Activity
 * Created by hanks on 16/6/26.
 */
public class EditActivity extends AppCompatActivity {

    private ListView listView;

    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "\n" +
            "便签内容现在支持分享至新浪长微博。";
    private MyAdapter adapter;


    public static final String ATT_IAMGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";

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


        calcText(noteContent);
    }

    private void calcText(String noteContent) {

        if (noteContent == null) {
            return;
        }

        Pattern pattern = Pattern.compile(ATT_IMAGE_PATTERN_STRING);

        Matcher localMatcher = pattern.matcher(noteContent);

        boolean result = localMatcher.find();
        while (result){
            int m = localMatcher.start();
            int n = localMatcher.end();
            String imageString = noteContent.substring(m, n);

        }
        for (;;)
        {
            bool = localMatcher.find();
            if (!bool) {
                break;
            }
            if (k == 0) {
                k = 1;
            }
            int m = localMatcher.start();
            int n = localMatcher.end();
            String str2 = paramString.substring(m, n);
            localObject = "";
            str1 = str1.replace(str2, (CharSequence)localObject);
        }
        if (k == 0) {
            break;
        }
        bool = TextUtils.isEmpty(str1);
        if (!bool) {
            break;
        }

        String[] texts = pattern.split(noteContent);



        Matcher matcher = pattern.matcher(noteContent);
        if (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(i+"matcher = " + matcher.group(i));
            }
        }


    }


    class MyAdapter extends BaseAdapter{

        @Override public int getCount() {
            return 0;
        }

        @Override public Object getItem(int position) {
            return null;
        }

        @Override public long getItemId(int position) {
            return 0;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }


}
