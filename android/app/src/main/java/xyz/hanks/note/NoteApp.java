package xyz.hanks.note;

import android.app.Application;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteApp extends Application {
    public static NoteApp app;

    @Override public void onCreate() {
        super.onCreate();
        app = this;
    }
}
