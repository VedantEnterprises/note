package xyz.hanks.note.model;

/**
 * 单个笔记
 * Created by hanks on 16/6/22.
 */
public class NoteItem {
    int _id;
    long modify_time;
    String location;
    String weather;
    boolean favorite;
    int call_timestamp;
    int call_search_number;
    String call_search_name;
    int dirty;
    boolean deleted;
    int source_id;
    String title;
    String detail;
    String weibo_text;
    String thumb_pic;
    boolean  markdown;
    int preset_tip;
    String note_content_md5;
    String note_folder_id;
    int position_in_folder;
    int folder_id;
    int folder_type;
}
