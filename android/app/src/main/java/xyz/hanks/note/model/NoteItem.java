package xyz.hanks.note.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 单个笔记
 * Created by hanks on 16/6/22.
 */
public class NoteItem extends RealmObject {
    @PrimaryKey
    public int _id;
    public long modify_time;
    public String location;
    public String weather;
    public boolean favorite;
    public int call_timestamp;
    public int call_search_number;
    public String call_search_name;
    public int dirty;
    public boolean deleted;
    public int source_id;
    public String title;
    public String detail;
    public String weibo_text;
    public String thumb_pic;
    public boolean  markdown;
    public int preset_tip;
    public String note_content_md5;
    public String note_folder_id;
    public int position_in_folder;
    public int folder_id;
    public int folder_type;
}
