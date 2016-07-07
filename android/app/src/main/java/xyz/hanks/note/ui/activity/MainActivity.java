package xyz.hanks.note.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.adapter.NoteAdapter;
import xyz.hanks.note.util.VectorDrawableUtils;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setupUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NoteAdapter());
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_search).setIcon(VectorDrawableUtils.getSearchDrawable(this));
        return super.onCreateOptionsMenu(menu);
    }
}
