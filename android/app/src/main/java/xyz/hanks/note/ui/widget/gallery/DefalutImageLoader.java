package xyz.hanks.note.ui.widget.gallery;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import xyz.hanks.note.ui.widget.gallery.util.ImageLoader;


/**
 * Created by hanks on 16/9/2.
 */
public class DefalutImageLoader implements HImageLoader {
    @Override
    public void displayImage(ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayImageListener displayImageListener) {
        ImageLoader.getInstance().loadImage(imageView, path);
    }

    @Override
    public void displayImage(ImageView imageView, String path) {
        ImageLoader.getInstance().loadImage(imageView, path);
    }

    @Override
    public void downloadImage(Context context, String path, DownloadImageListener downloadImageListener) {
    }
}
