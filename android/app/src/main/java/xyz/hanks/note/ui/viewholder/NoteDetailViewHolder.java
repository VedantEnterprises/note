package xyz.hanks.note.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.hanks.note.R;

/**
 * Created by hanks on 16/7/4.
 */
public class NoteDetailViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView tv_line;
    public View rootLayout;
    public View imgLayout;
    public View imgDrag;

    public NoteDetailViewHolder(View itemView) {
        super(itemView);
        rootLayout = itemView.findViewById(R.id.root_layout);
        imgLayout = itemView.findViewById(R.id.layout_img);
        imgDrag = itemView.findViewById(R.id.img_drag);
        tv_line = (TextView) itemView.findViewById(R.id.tv_line);
        imageView = (ImageView) itemView.findViewById(R.id.iv_img_item);

        imgDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                if (listener != null) {
                    return listener.onTouch(view, motionEvent);
                }
                return false;
            }
        });
    }

    public static NoteDetailViewHolder newInstance(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note_detail, parent, false);
        NoteDetailViewHolder holder = new NoteDetailViewHolder(itemView);
        return holder;
    }

    OnImageHandleTouchListener listener;

    public void setListener(OnImageHandleTouchListener listener) {
        this.listener = listener;
    }

    public interface OnImageHandleTouchListener{
        boolean onTouch(View view, MotionEvent motionEvent);
    }
}
