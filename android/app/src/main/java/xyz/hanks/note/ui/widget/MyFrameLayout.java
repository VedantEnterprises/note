package xyz.hanks.note.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import xyz.hanks.note.R;

/**
 * Created by hanks on 16/6/30.
 */
public class MyFrameLayout extends FrameLayout {

    public ListView listView;
    public View backListView;
    private int mListViewHeight;
    private boolean isBottom;

    public MyFrameLayout(Context context) {
        this(context,null);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_framelayout,this);

        listView = (ListView) findViewById(R.id.listView);
        backListView =  findViewById(R.id.backListView);

//        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mListViewHeight = listView.getHeight();
//
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                isBottom = false;
//                if (firstVisibleItem == 0) {
//                    View firstVisibleItemView = listView.getChildAt(0);
//                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        Log.d("ListView", "<----滚动到顶部----->");
//                    }
//                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
//                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListViewHeight) {
//                        Log.d("ListView", "#####滚动到底部######");
//                        isBottom = true;
//                    }
//                }
//            }
//        });

    }

//    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(!isBottom){
//            backListView.dispatchTouchEvent(ev);
//        }
//        listView.dispatchTouchEvent(ev);
//        return true;
//    }
}
