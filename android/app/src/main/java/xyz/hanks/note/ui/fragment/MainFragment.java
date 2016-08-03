package xyz.hanks.note.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.hanks.note.R;
import xyz.hanks.note.datamanager.db.NoteItemManager;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.activity.CommonActivity;
import xyz.hanks.note.ui.activity.MainActivity;
import xyz.hanks.note.ui.adapter.NoteAdapter;
import xyz.hanks.note.util.VectorDrawableUtils;

/**
 * Created by hanks on 2016/8/2.
 */
public class MainFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<NoteItem> noteList = new ArrayList<>();
    private NoteAdapter adapter;
    private FloatingActionButton addButton;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        getData();
    }


    private void setupUI() {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(VectorDrawableUtils.getMenuDrawable(getContext()));

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = NoteAdapter.newInstance(noteList);
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int adapterPosition) {
                String objectId = noteList.get(adapterPosition).objectId;
                ((MainActivity) getActivity()).changeToEditFragment(objectId);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    private void getData() {
        List<NoteItem> noteItemList = NoteItemManager.getNoteItemList();
        if (noteItemList != null && noteItemList.size() > 0) {
            noteList.clear();
            noteList.addAll(noteItemList);
        }
        adapter.notifyItemRangeChanged(0, noteList.size());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_search).setIcon(VectorDrawableUtils.getSearchDrawable(getContext()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CommonActivity.start(getContext(), SettingFragment.class.getName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
