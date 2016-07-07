package xyz.hanks.note;

import android.app.Application;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import xyz.hanks.note.model.NoteFolder;
import xyz.hanks.note.model.NoteItem;

/**
 * Created by hanks on 16/6/22.
 */
public class NoteApp extends Application {
    public static NoteApp app;

    @Override public void onCreate() {
        super.onCreate();
        app = this;
        initRealm();
    }

    private void initRealm() {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(app).name(getString(R.string.app_name)).build();
        Realm.setDefaultConfiguration(realmConfig);


        initData();
    }

    private void initData() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                final String[] folderNames = new String[]{
                        "全部", "生活", "工作", "随笔", "回收站"
                };
                final String[] notes = new String[]{
                        "Tip: 基本操作介绍",
                        "Tip: 便签文件夹介绍",
                        "Tip: Markdown 介绍",
                        "Tip: 基本操作介绍",
                };
                for (int i = 0; i < folderNames.length; i++) {
                    NoteFolder noteFolder = new NoteFolder();
                    noteFolder.objectId = i;
                    noteFolder.createdAt = new Date().getTime();
                    noteFolder.updatedAt = new Date().getTime();
                    noteFolder.name = folderNames[i];
                    noteFolder.position = i;
                    noteFolder.type = i == folderNames.length - 1 ? 0 : 1;
                    realm.copyToRealmOrUpdate(noteFolder);
                }


                for (int i = 0; i < notes.length; i++) {
                    NoteItem noteItem = new NoteItem();
                    noteItem.objectId = i;
                    noteItem.createdAt = new Date().getTime();
                    noteItem.updatedAt = new Date().getTime();
                    noteItem.title = notes[i];
                    noteItem.detail = notes[i];
                    noteItem.deleted = false;
                    noteItem.markdown = false;
                    noteItem.favorite = false;
                    noteItem.folder_id = 0;
                    noteItem.positionInFolder = i;
                    realm.copyToRealmOrUpdate(noteItem);
                }

            }
        });
    }
}
