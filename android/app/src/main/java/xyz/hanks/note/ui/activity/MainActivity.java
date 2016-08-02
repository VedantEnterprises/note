package xyz.hanks.note.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.fragment.EditFragment;
import xyz.hanks.note.ui.fragment.MainFragment;
import xyz.hanks.note.ui.fragment.SettingFragment;
import xyz.hanks.note.util.VectorDrawableUtils;

public class MainActivity extends BaseActivity {

    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment.newInstance())
                .commit();
        setupUI();
    }

    private void addChildFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    private void setupUI() {
        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });
    }

    private void addNewNote() {
        addChildFragment(EditFragment.newInstance());
    }

    @Override
    protected Drawable getNavigationIcon() {
        return VectorDrawableUtils.getMenuDrawable(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_search).setIcon(VectorDrawableUtils.getSearchDrawable(this));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CommonActivity.start(this, SettingFragment.class.getName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToEditFragment(String objectId) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, EditFragment.newInstance(objectId))
                .addToBackStack(EditFragment.class.getName())
                .commit();
    }
}
