package xyz.hanks.note.ui.widget.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startFromFragment(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), GridImageActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

}
