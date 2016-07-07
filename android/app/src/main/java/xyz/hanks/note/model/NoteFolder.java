package xyz.hanks.note.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 文件夹
 * Created by hanks on 16/6/22.
 */
@RealmClass
public class NoteFolder implements RealmModel {
    @PrimaryKey
    public int objectId;
    public long createdAt;
    public long updatedAt;
    public String name;
    public int position;
    public int type;
}
