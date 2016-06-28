package xyz.hanks.note.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

import xyz.hanks.note.NoteApp;

/**
 * Created by hanks on 16/6/28.
 */
public class FileUtils {

    public static Bitmap getBitmapFromFile(String name) {
        String filePath = getProjectImagePath()+"/"+name;
        return BitmapFactory.decodeFile(filePath);
    }

    public static String getProjectImagePath() {
        String path = getProjectPath() + "/images";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    public static String getProjectPath() {
        String filePath;
        if(sdCardAvaible()){
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/note";
        }else {
            filePath = NoteApp.app.getFilesDir().getAbsolutePath();
        }
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return filePath;
    }

    private static boolean sdCardAvaible() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }
}
