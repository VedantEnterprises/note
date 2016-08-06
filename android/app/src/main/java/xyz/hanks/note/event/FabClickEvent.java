package xyz.hanks.note.event;

import android.support.design.widget.FloatingActionButton;

/**
 * Created by hanks on 16/8/7.
 */
public class FabClickEvent {
    public final FloatingActionButton fab;
    public FabClickEvent(FloatingActionButton floatingActionButton) {
        this.fab = floatingActionButton;
    }
}
