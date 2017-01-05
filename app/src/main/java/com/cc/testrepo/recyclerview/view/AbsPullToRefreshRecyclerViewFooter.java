package com.cc.testrepo.recyclerview.view;


import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public abstract class AbsPullToRefreshRecyclerViewFooter extends FrameLayout {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOAD_FAILED = 2;
    public static final int STATE_NO_DATA = 3;

    protected int mState;

    public AbsPullToRefreshRecyclerViewFooter(Context context) {
        super(context);
        init();
    }

    public AbsPullToRefreshRecyclerViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void setState(@IntRange(from = 0,to = 3) int state){}
}
