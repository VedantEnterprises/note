package xyz.hanks.note.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.activity.EditActivity;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder> {
    @Override public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override public void onBindViewHolder(NoteItemViewHolder holder, int position) {
        // holder.cbFavorite.setChecked(true);
        holder.ivImage.setVisibility(View.VISIBLE);
        holder.tvModifyTime.setText("昨天 12:45");
        holder.tvNoteDetail.setText("点击进来看看还有什么惊喜");
    }

    @Override public int getItemCount() {
        return 10;
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
            cbFavorite =  itemView.findViewById(R.id.cb_favorite);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    EditActivity.start(v.getContext());
                }
            });
        }

    }
}
