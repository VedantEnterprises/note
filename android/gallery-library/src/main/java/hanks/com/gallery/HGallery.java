package hanks.com.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import hanks.com.gallery.base.DefalutImageLoader;
import hanks.com.gallery.base.HImageLoader;

/**
 * weibo gallery
 * Created by hanks on 16/9/2.
 */
public class HGallery {

    public static final String EXTRA_PATH = "extra_path";
    private static HImageLoader sImageLoader;

    public static HImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            synchronized (HGallery.class) {
                if (sImageLoader == null) {
                    sImageLoader = new DefalutImageLoader();
                }
            }
        }
        return sImageLoader;
    }

    public static void init(Context context, HImageLoader imageLoader) {
        sImageLoader = imageLoader;
        init(context);
    }

    public static void init(Context context) {
    }

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, GridImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }

}
