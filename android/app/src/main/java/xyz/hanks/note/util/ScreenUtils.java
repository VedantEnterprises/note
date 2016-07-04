package xyz.hanks.note.util;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import xyz.hanks.note.NoteApp;

/**
 * Created by hanks on 16/6/30.
 */
public class ScreenUtils {

    public static float dpToPx(float valueInDp) {
        DisplayMetrics metrics = NoteApp.app.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


}
