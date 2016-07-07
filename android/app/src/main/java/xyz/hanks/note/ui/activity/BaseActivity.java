package xyz.hanks.note.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import xyz.hanks.note.R;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 16/7/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolbar();
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(getNavigationIcon());
            setSupportActionBar(toolbar);
        }
    }

    protected abstract int getLayoutId();

    protected Drawable getNavigationIcon() {
        return VectorDrawableUtils.getMenuDrawable(this);
    }
}
