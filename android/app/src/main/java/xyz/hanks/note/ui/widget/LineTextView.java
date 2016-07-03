package xyz.hanks.note.ui.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 每一行的文字垂直居中
 * Created by hanks on 16/7/2.
 */
public class LineTextView extends TextView {

    boolean reLayout = false;

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        reLayout = false;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!reLayout) {
            reLayout = true;
            int lineCount = getLineCount();
            int height = getMeasuredHeight();
            float add = height * 1.0f / lineCount;
            setIncludeFontPadding(false);
            setLineSpacing(add, 0);
            setPadding(getPaddingLeft(), (int) ((add - getTextSize()) * 0.5f), getPaddingRight(), getPaddingBottom());
            requestLayout();
            invalidate();
        }


    }
}
