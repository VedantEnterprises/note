package xyz.hanks.note.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

/**
 * Created by hanks on 16/7/5.
 */
public class NoteRecyclerView extends ObservableRecyclerView {
    public NoteRecyclerView(Context context) {
        super(context);
    }

    public NoteRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }
}
