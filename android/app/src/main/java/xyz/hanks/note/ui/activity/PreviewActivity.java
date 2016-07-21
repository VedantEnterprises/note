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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hanks.note.R;
import xyz.hanks.note.util.FileUtils;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 16/7/5.
 */
public class PreviewActivity extends AppCompatActivity {

    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";
    private static final String TAG = "PreviewActivity";
    private static final String EXTRA_CONTENT = "content";
    List<NoteItem> data = new ArrayList<>();
    private String noteContent = "";
    private LinearLayout linearLayout;


    public static void start(Context context, String noteContent) {
        Intent starter = new Intent(context, PreviewActivity.class);
        starter.putExtra(EXTRA_CONTENT, noteContent);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        noteContent = getIntent().getStringExtra(EXTRA_CONTENT);

        linearLayout = (LinearLayout) findViewById(R.id.container_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("预览");
        toolbar.setNavigationIcon(VectorDrawableUtils.getBackDrawable(this));
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
                viewHolder.imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
                viewHolder.imageView.getLayoutParams().height = noteItem.height;
            }
            linearLayout.addView(viewHolder.rootLayout);
        }

    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                FileUtils.takeScreenShot(linearLayout, System.currentTimeMillis() + ".png");
                break;
        }
        return super.onOptionsItemSelected(item);
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
