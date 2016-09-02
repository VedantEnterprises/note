package xyz.hanks.note.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import xyz.hanks.note.ui.widget.gallery.HImageLoader;

/**
 * Created by hanks on 16/9/2.
 */
public class GlideImageLoader implements HImageLoader {
    @Override
    public void displayImage(ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayImageListener displayImageListener) {
        if (path == null) {
            path = "";
        }
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        Glide.with(imageView.getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void displayImage(ImageView imageView, String path) {
        if (path == null) {
            path = "";
        }
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        Glide.with(imageView.getContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public void downloadImage(Context context, String path, DownloadImageListener downloadImageListener) {
        if (path == null) {
            path = "";
        }
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://")) {
            path = "file://" + path;
        }

        final String finalPath = path;
        Glide.with(context).load(finalPath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
            }
        });
    }
}
