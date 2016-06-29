package xyz.hanks.note.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.hanks.note.R;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteDetailAdapter extends RecyclerView.Adapter<NoteDetailAdapter.NoteItemViewHolder> {

    @Override public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override public void onBindViewHolder(NoteItemViewHolder holder, int position) {
//        // holder.cbFavorite.setChecked(true);
//        NoteItem noteItem = data.get(position);
//        if (noteItem.type == 0) {
//            editText.setText(noteItem.content);
//            imageView.setVisibility(View.GONE);
//            editText.setVisibility(View.VISIBLE);
//            editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override public void onGlobalLayout() {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }else {
//                        editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    }
//                    int lineCount = editText.getLayout().getLineCount();
//                    for (int i = 0; i < lineCount; i++) {
//                        int lineStart = editText.getLayout().getLineStart(i);
//                        int lineEnd = editText.getLayout().getLineEnd(i);
//                        String lineText = editText.getText().subSequence(lineStart,lineEnd).toString();
//                        Log.e(TAG,lineStart+","+lineEnd+","+lineText+","+lineCount);
//                        backupData.add(lineText);
//                        backupAdapter.notifyDataSetChanged();
//                    }
//                }
//            });
//        } else if (noteItem.type == 1) {
//            editText.setVisibility(View.GONE);
//            imageView.setVisibility(View.VISIBLE);
//            imageView.setImageBitmap(FileUtils.getBitmapFromFile(noteItem.name));
//            imageView.getLayoutParams().height = noteItem.height;
//            int itemCount = noteItem.height % ITEM_HEIGHT == 0 ? noteItem.height/ITEM_HEIGHT : noteItem.height/ITEM_HEIGHT+1;
//            for (int i = 0; i < itemCount; i++) {
//                backupData.add("\n");
//            }
//            backupAdapter.notifyDataSetChanged();
//        }
    }

    @Override public int getItemCount() {
        return 10;
    }

    class NoteItemViewHolder extends RecyclerView.ViewHolder {

        public TextView etNote;
        public ImageView ivImage;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_img_item);
            etNote = (TextView) itemView.findViewById(R.id.et_note_item);
        }

    }
}
