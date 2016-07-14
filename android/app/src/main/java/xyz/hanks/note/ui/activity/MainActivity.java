package xyz.hanks.note.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import xyz.hanks.note.R;
import xyz.hanks.note.datamanager.db.NoteItemManager;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.adapter.NoteAdapter;
import xyz.hanks.note.util.VectorDrawableUtils;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<NoteItem> noteList = new ArrayList<>();
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        getData();
    }

    private void setupUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = NoteAdapter.newInstance(noteList);
        recyclerView.setAdapter(adapter);
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
}
