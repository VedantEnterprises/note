package xyz.hanks.note.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hanks on 16/8/8.
 */
public class NoteUtils {

    public static final String ATT_IMAGE_TAG = "<image w=%s h=%s describe=%s name=%s>";
    public static final String ATT_IMAGE_PATTERN_STRING = "<image w=.*? h=.*? describe=.*? name=.*?>";

    public static String getCoverImage(String noteContent){
        if (TextUtils.isEmpty(noteContent)) {
            return null;
        }
        Pattern pattern = Pattern.compile(ATT_IMAGE_PATTERN_STRING);
        String tmp = noteContent;
        Matcher localMatcher = pattern.matcher(tmp);
        boolean result = localMatcher.find();
        if (!result) {
            return null;
        }
        int m = localMatcher.start();
        int n = localMatcher.end();
        String imageString = tmp.substring(m, n);
        int nIndex = imageString.indexOf("name=");
        return imageString.substring(nIndex + 5, imageString.length() - 1);
    }
}
