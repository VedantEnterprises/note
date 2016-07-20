package xyz.hanks.note.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;

import xyz.hanks.note.R;

/**
 * Created by hanks on 2016/7/6.
 */
public class VectorDrawableUtils {
    public static Drawable getMenuDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_menu_white_24dp, context.getTheme());
    }
    public static Drawable getBackDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_arrow_back_white_24dp, context.getTheme());
    }
    public static Drawable getDeleteDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_delete_white_24dp, context.getTheme());
    }
    public static Drawable getPreviewDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_compare_arrows_white_24dp, context.getTheme());
    }
    public static Drawable getDownArrowDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_arrow_drop_down_white_24dp, context.getTheme());
    }
    public static Drawable getSearchDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_search_white_24dp, context.getTheme());
    }
    public static Drawable getSettingDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_settings_white_24dp, context.getTheme());
    }
    public static Drawable getImageDrawable(Context context){
        return VectorDrawableCompat.create(context.getResources(), R.drawable.ic_image_white_24dp, context.getTheme());
    }
}
