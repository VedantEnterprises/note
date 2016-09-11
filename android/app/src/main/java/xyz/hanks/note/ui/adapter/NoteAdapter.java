package xyz.hanks.note.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanks.note.R;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.util.PrettyDateUtils;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder> {

    OnItemClickListener onItemClickListener;
    private List<NoteItem> data;

    private NoteAdapter(List<NoteItem> data) {
        this.data = data;
    }

    public static NoteAdapter newInstance(List<NoteItem> data) {
        return new NoteAdapter(data);
    }

    @Override
    public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteItemViewHolder holder, int position) {
        // holder.cbFavorite.setChecked(true);
        NoteItem item = data.get(position);
        holder.ivImage.setText(item.detail.length() > 0 ? item.detail.substring(0, 1).toUpperCase() : "无");
        holder.tvModifyTime.setText(PrettyDateUtils.format(item.createdAt));
        holder.tvTitle.setText(item.title);
        holder.tvNoteDetail.setText(item.detail.length() > 100 ? item.detail.substring(0, 100) : item.detail);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int adapterPosition);
    }

    public class NoteItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_modify_time)
        public TextView tvModifyTime;
        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_note_detail)
        public TextView tvNoteDetail;
        @BindView(R.id.cb_favorite)
        public View cbFavorite;
        @BindView(R.id.layout_item)
        public View layoutItem;
        @BindView(R.id.iv_img)
        public TextView ivImage;
        @BindView(R.id.iv_del)
        public ImageView ivDel;
        @BindView(R.id.iv_edit)
        public ImageView ivEdit;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, getAdapterPosition());
                    }
                }
            });
        }

    }
}
