package xyz.hanks.note.datamanager.db;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import xyz.hanks.note.model.NoteItem;

/**
 * dao for  NoteItem
 * Created by hanks on 16/7/7.
 */
public class NoteItemManager {
    public static List<NoteItem> getNoteItemList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NoteItem> results = realm
                .where(NoteItem.class)
                .notEqualTo(NoteItem.DELETED, true)
                .findAllSorted(NoteItem.UPDATED_AT, Sort.DESCENDING);
        List<NoteItem> noteItemList = new ArrayList<>();
        for (NoteItem item : results) {
            noteItemList.add(item);
        }
        return noteItemList;
    }

    public static NoteItem getNoteById(String noteId) {
        if (TextUtils.isEmpty(noteId)) {
            return null;
        }
        Realm realm = Realm.getDefaultInstance();
        NoteItem noteItem = realm.where(NoteItem.class).equalTo(NoteItem.OBJECT_ID, noteId).findFirst();
        NoteItem tmp = realm.copyFromRealm(noteItem);
        realm.close();
        return tmp;
    }

    public static void createOrUpdateNote(final NoteItem noteItem) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(noteItem);
            }
        });
        realm.close();
    }

    public static void deleteNote(final NoteItem noteItem) {
        NoteItem item = getNoteById(noteItem.objectId);
        if (item != null) {
            item.deleted = true;
            createOrUpdateNote(item);
        }
    }
}
