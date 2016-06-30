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

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder(app).build();
//        Realm.setDefaultConfiguration(realmConfig);

    }
}
