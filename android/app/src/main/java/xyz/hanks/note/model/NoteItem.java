package xyz.hanks.note.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * 单个笔记
 * Created by hanks on 16/6/22.
 */
@RealmClass
public class NoteItem implements RealmModel {

    public static final String UPDATED_AT = "updatedAt";
    public static final String OBJECT_ID = "objectId";

    @PrimaryKey
    public String objectId;
    public long createdAt;
    public long updatedAt;
    public boolean favorite;
    public boolean deleted;
    public boolean markdown;
    public String title;
    public String detail;
    public int positionInFolder;
    public int folderId;
}
