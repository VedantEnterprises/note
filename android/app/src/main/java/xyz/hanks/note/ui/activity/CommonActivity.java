package xyz.hanks.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import xyz.hanks.note.R;

/**
 * Created by hanks on 16/7/14.
 */
public class CommonActivity extends BaseActivity {


    private static final String EXTRA_FRAGMENT_NAME = "fragment_name";
    private static final String EXTRA_BUNDLE = "bundle";
    private String fragmentName;
    private Bundle bundle;

    public static void start(Context context, String fragmentName) {
        start(context, fragmentName, null);
    }

    public static void start(Context context, String fragmentName, Bundle bundle) {
        Intent starter = new Intent(context, CommonActivity.class);
        starter.putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        starter.putExtra(EXTRA_BUNDLE, bundle);
        context.startActivity(starter);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentName = getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);
        bundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment.instantiate(this, fragmentName, bundle)).commit();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_common;
    }

}
