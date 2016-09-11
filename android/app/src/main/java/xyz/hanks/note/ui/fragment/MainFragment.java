package xyz.hanks.note.ui.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // 不处理拖拽事件回调
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // 处理滑动事件回调
            final int pos = viewHolder.getAdapterPosition();
            final NoteItem noteItem = noteList.get(pos);
            noteList.remove(pos);
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            String text;
            // 判断方向，进行不同的操作
            if (direction == ItemTouchHelper.RIGHT) {
                text = "删除一项";
            } else {
                text = "延迟一项";
            }
            Snackbar.make(viewHolder.itemView, text, Snackbar.LENGTH_LONG)
                    .setAction("撤销", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noteList.add(pos, noteItem);
                            adapter.notifyItemInserted(pos);
                        }
                    }).show();
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 释放View时回调，清除背景颜色，隐藏图标
            // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
            getDefaultUIUtil().clearView(((NoteAdapter.NoteItemViewHolder) viewHolder).layoutItem);
            ((NoteAdapter.NoteItemViewHolder) viewHolder).ivDel.setVisibility(View.GONE);
            ((NoteAdapter.NoteItemViewHolder) viewHolder).ivDel.setVisibility(View.GONE);
            ((NoteAdapter.NoteItemViewHolder) viewHolder).ivEdit.setVisibility(View.GONE);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            // 当viewHolder的滑动或拖拽状态改变时回调
            if (viewHolder != null) {
                // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
                getDefaultUIUtil().onSelected(((NoteAdapter.NoteItemViewHolder) viewHolder).layoutItem);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // ItemTouchHelper的onDraw方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的下方
            // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
            getDefaultUIUtil().onDraw(c, recyclerView, ((NoteAdapter.NoteItemViewHolder) viewHolder).layoutItem, dX, dY, actionState, isCurrentlyActive);
            if (dX > 0) { // 向左滑动是的提示
                ((NoteAdapter.NoteItemViewHolder) viewHolder).ivDel.setVisibility(View.VISIBLE);
                ((NoteAdapter.NoteItemViewHolder) viewHolder).ivEdit.setVisibility(View.GONE);
                ((NoteAdapter.NoteItemViewHolder) viewHolder).itemView.setBackgroundResource(R.color.del_red);
            }
            if (dX < 0) { // 向右滑动时的提示
                ((NoteAdapter.NoteItemViewHolder) viewHolder).ivDel.setVisibility(View.GONE);
                ((NoteAdapter.NoteItemViewHolder) viewHolder).ivEdit.setVisibility(View.VISIBLE);
                ((NoteAdapter.NoteItemViewHolder) viewHolder).itemView.setBackgroundResource(R.color.del_green);
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // ItemTouchHelper的onDrawOver方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的上方
            // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
            getDefaultUIUtil().onDrawOver(c, recyclerView, ((NoteAdapter.NoteItemViewHolder) viewHolder).layoutItem, dX, dY, actionState, isCurrentlyActive);
        }
    });
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
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        return true;
    }

}
