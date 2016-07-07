package xyz.hanks.note.datamanager.db;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import xyz.hanks.note.model.NoteItem;

/**
 * Created by hanks on 16/7/7.
 */
public class NoteItemManager {
    public static List<NoteItem> getNoteItemList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NoteItem> results = realm.where(NoteItem.class).findAllSorted(NoteItem.UPDATED_AT, Sort.DESCENDING);
        List<NoteItem> noteItemList = new ArrayList<>();
        for (NoteItem item : results) {
            noteItemList.add(item);
        }
        return noteItemList;

    }
}
