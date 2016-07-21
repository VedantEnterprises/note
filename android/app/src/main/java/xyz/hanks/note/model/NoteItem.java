package xyz.hanks.note.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * 单个笔记
 * Created by hanks on 16/6/22.
 */
@RealmClass
public class NoteItem implements RealmModel {

    public static final String UPDATED_AT = "updatedAt";
    public static final String OBJECT_ID = "objectId";

    @Required
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

    @Override public String toString() {
        return "NoteItem{" +
                "objectId='" + objectId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", favorite=" + favorite +
                ", deleted=" + deleted +
                ", markdown=" + markdown +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", positionInFolder=" + positionInFolder +
                ", folderId=" + folderId +
                '}';
    }
}
