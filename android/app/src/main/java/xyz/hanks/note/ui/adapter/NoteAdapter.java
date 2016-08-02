package xyz.hanks.note.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xyz.hanks.note.R;
import xyz.hanks.note.model.NoteItem;
import xyz.hanks.note.ui.activity.EditActivity;
import xyz.hanks.note.util.PrettyDateUtils;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder> {

    private List<NoteItem> data;

    private NoteAdapter(List<NoteItem> data) {
        this.data = data;
    }

    public static NoteAdapter newInstance(List<NoteItem> data) {
        return new NoteAdapter(data);
    }

    @Override public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override public void onBindViewHolder(NoteItemViewHolder holder, int position) {
        // holder.cbFavorite.setChecked(true);
        NoteItem item = data.get(position);
        holder.ivImage.setVisibility(View.GONE);
        holder.tvModifyTime.setText(PrettyDateUtils.format(item.createdAt));
        holder.tvNoteDetail.setText(item.title);
    }

    @Override public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class NoteItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvModifyTime;
        public TextView tvNoteDetail;
        public View cbFavorite;
        public ImageView ivImage;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            tvModifyTime = (TextView) itemView.findViewById(R.id.tv_modify_time);
            tvNoteDetail = (TextView) itemView.findViewById(R.id.tv_note_detail);
            cbFavorite = itemView.findViewById(R.id.cb_favorite);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v,getAdapterPosition());
                    }
                }
            });
        }

    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View v, int adapterPosition);
    }
}
