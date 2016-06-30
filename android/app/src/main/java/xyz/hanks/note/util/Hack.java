package xyz.hanks.note.util;

import android.os.Build;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Created by hanks on 16/6/30.
 */
public class Hack {

    private static final Method listViewTrackMotionScroll;

    static {
        Method trackMotionScroll = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            try {
                trackMotionScroll = AbsListView.class
                        .getDeclaredMethod("trackMotionScroll", int.class, int.class);
                trackMotionScroll.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        listViewTrackMotionScroll = trackMotionScroll;
    }

    public static void scrollListBy(ListView listView, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            listView.scrollListBy(y);
        } else {
            try {
                listViewTrackMotionScroll.invoke(listView, -y, -y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
