package xyz.hanks.note.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import xyz.hanks.note.NoteApp;

/**
 * Created by hanks on 16/6/28.
 */
public class FileUtils {

    public static String saveImage(String imagePath) {
        File file = new File(imagePath);
        if (!file.exists()) {
            return null;
        }
        try {
            String fileName = System.currentTimeMillis() + ".png";
            String outputPath = getProjectImagePath();
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            InputStream in = new FileInputStream(imagePath);
            OutputStream out = new FileOutputStream(outputPath + "/" + fileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImagePath(String name) {
        return getProjectImagePath() + "/" + name;
    }

    public static Bitmap getBitmapFromFile(String name) {
        String filePath = getProjectImagePath() + "/" + name;
        return BitmapFactory.decodeFile(filePath);
    }

    public static String getProjectImagePath() {
        String path = getProjectPath() + "/images";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getProjectPath() {
        String filePath;
        if (sdCardAvaible()) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/note";
        } else {
            filePath = NoteApp.app.getFilesDir().getAbsolutePath();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    private static boolean sdCardAvaible() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }

    public static void takeScreenShot(View view, @NonNull String fileName) {
        Bitmap b = getBitmapFromView(view, view.getHeight(), view.getWidth());
        //Save bitmap
        String extr = Environment.getExternalStorageDirectory() + "/";
        File myPath = new File(extr, fileName);
        if (!myPath.exists()) {
            try {
                myPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), b, "Screen", "screen");
            Toast.makeText(view.getContext(), "save success:" + myPath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
