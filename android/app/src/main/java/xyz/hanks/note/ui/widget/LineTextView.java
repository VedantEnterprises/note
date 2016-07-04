package xyz.hanks.note.ui.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;

/**
 * 每一行的文字垂直居中
 * Created by hanks on 16/7/2.
 */
public class LineTextView extends EditText {

    private static final float ITEM_HEIGHT = 125;
    boolean reLayout = false;

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float add = ITEM_HEIGHT;
                float mul = 0;
                setLineSpacing(0f, 1f);
                setLineSpacing(add, mul);
                setIncludeFontPadding(false);
                setGravity(Gravity.CENTER_VERTICAL);
                setPadding(getPaddingLeft(), (int) ((ITEM_HEIGHT - getTextSize()) * 0.5f), getPaddingRight(), getPaddingBottom());
            }

            @Override public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!reLayout) {
            reLayout = true;
            float add = ITEM_HEIGHT;
            setIncludeFontPadding(false);
            setGravity(Gravity.CENTER_VERTICAL);
            Log.e("........","onDraw:"+add);
            setLineSpacing(add, 0);
            setPadding(getPaddingLeft(), (int) ((add - getTextSize()) * 0.5f), getPaddingRight(), getPaddingBottom());
            requestLayout();
            invalidate();
        }
    }
}
