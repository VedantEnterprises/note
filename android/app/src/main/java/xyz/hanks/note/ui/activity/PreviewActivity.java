package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;

/**
 * Created by hanks on 16/7/5.
 */
public class PreviewActivity extends AppCompatActivity {

    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "PreviewActivity";
    List<NoteItem> data = new ArrayList<>();
    private String noteContent = "进来看看还有什么惊喜^_^\n" +
            "我们支持把便签的文字直接发送到新<image w=858 h=483 describe= name=Note_123.jpg>浪微博，\n\n" +
            "同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签会自动生成排版优雅、字体<image w=858 h=223 describe=no one name=Note_453.jpg>精美的图片长微博，希望我们的便签能够让你重新喜欢上不那么碎片的表达。试试点击右上角的小飞机，再点击随后出现的菜单中的 “以图片分享” 将图片分享至你的其他应用。\n" +
            "便签内容现在支持分享至新浪长微博同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签同<image w=858 h=383 describe= name=Note_123.jpg>时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签同时你再也不用忍受新浪的数字限制了，当文字超过 140 之后，便签。\n";


    public static void start(Context context) {
        Intent starter = new Intent(context, PreviewActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("预览");
        toolbar.setNavigationIcon(R.drawable.toolbar_back_white);
        setSupportActionBar(toolbar);
        calcText();
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


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container_layout);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < data.size(); i++) {
            PreviewHolder viewHolder = new PreviewHolder(inflater.inflate(R.layout.item_list_preview, null));
            NoteItem noteItem = data.get(i);
            if (noteItem.type == 0) {
                viewHolder.tv_line.setVisibility(View.VISIBLE);
                viewHolder.imgLayout.setVisibility(View.GONE);
                viewHolder.tv_line.setText(noteItem.content);
            } else {
                viewHolder.tv_line.setVisibility(View.GONE);
                viewHolder.imgLayout.setVisibility(View.VISIBLE);
                viewHolder.imageView.getLayoutParams().height = noteItem.height;
            }
            linearLayout.addView(viewHolder.rootLayout);
        }

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

    class PreviewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_line;
        public View rootLayout;
        public View imgLayout;

        public PreviewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.root_layout);
            imgLayout = itemView.findViewById(R.id.layout_img);
            tv_line = (TextView) itemView.findViewById(R.id.tv_line);
            imageView = (ImageView) itemView.findViewById(R.id.iv_img_item);
        }
    }


}
