package xyz.hanks.note.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import xyz.hanks.note.R;
import xyz.hanks.note.constant.Constants;
import xyz.hanks.note.event.FabClickEvent;
import xyz.hanks.note.ui.fragment.EditFragment;
import xyz.hanks.note.ui.fragment.MainFragment;
import xyz.hanks.note.util.VectorDrawableUtils;

public class MainActivity extends BaseActivity {

    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .commit();
        setupUI();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addChildFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right_scale, R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    private void setupUI() {
        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setTag(Constants.FabTag.ADD);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new FabClickEvent(addButton));
            }
        });
    }

    @Subscribe
    public void onEvent(FabClickEvent event) {
        if (Constants.FabTag.ADD.equals(event.fab.getTag())) {
            addNewNote();
        }
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

    public void changeToEditFragment(String objectId) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right_scale, R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                .replace(R.id.fragment_container, EditFragment.newInstance(objectId))
                .addToBackStack(EditFragment.class.getName())
                .commit();
    }
}
