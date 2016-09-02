package xyz.hanks.note;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Application instance
 * Created by hanks on 16/6/22.
 */
public class NoteApp extends Application {
    public static NoteApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initRealm();
        initGalley();
    }

    private void initGalley() {

    }

    private void initRealm() {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(app)
                .name(getString(R.string.app_name) + ".realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
