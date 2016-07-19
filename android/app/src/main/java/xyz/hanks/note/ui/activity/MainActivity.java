package xyz.hanks.note.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xyz.hanks.note.R;
import xyz.hanks.note.datamanager.db.NoteItemManager;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.adapter.NoteAdapter;
import xyz.hanks.note.ui.fragment.SettingFragment;
import xyz.hanks.note.util.VectorDrawableUtils;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<NoteItem> noteList = new ArrayList<>();
    private NoteAdapter adapter;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        getData();
    }

    private void setupUI() {
        addButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = NoteAdapter.newInstance(noteList);
        recyclerView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                addNewNote();
            }
        });
    }

    private void addNewNote() {
        EditActivity.start(this);
    }

    @Override protected Drawable getNavigationIcon() {
        return VectorDrawableUtils.getSettingDrawable(this);
    }

    private void getData() {
        noteList.addAll(NoteItemManager.getNoteItemList());
        adapter.notifyItemRangeChanged(0, noteList.size());
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_search).setIcon(VectorDrawableUtils.getSearchDrawable(this));
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CommonActivity.start(this, SettingFragment.class.getName());
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
