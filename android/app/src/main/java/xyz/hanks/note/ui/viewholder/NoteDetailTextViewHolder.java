package xyz.hanks.note.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.hanks.note.R;
import xyz.hanks.note.ui.widget.LineTextView;

/**
 * Created by hanks on 16/7/4.
 */
public class NoteDetailTextViewHolder extends RecyclerView.ViewHolder {

    public LineTextView lineTextView;

    public NoteDetailTextViewHolder(View itemView) {
        super(itemView);
        lineTextView = (LineTextView) itemView.findViewById(R.id.et_note_item);
    }

    public static NoteDetailTextViewHolder newInstance(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail_text, parent, false);
        NoteDetailTextViewHolder holder = new NoteDetailTextViewHolder(itemView);
        return holder;
    }
}
