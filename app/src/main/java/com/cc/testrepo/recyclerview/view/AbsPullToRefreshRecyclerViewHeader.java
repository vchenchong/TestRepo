package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class AbsPullToRefreshRecyclerViewHeader extends FrameLayout {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_PULLING_DOWN = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_REFRESHING = 3;

    protected int mState;

    public AbsPullToRefreshRecyclerViewHeader(Context context) {
        super(context);
    }

    public AbsPullToRefreshRecyclerViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract int getIntrinsicHeight();
    protected abstract void setVisibleHeight(int height);
    protected abstract int getVisibleHeight();
    protected void setState(@IntRange(from = 0,to = 3) int state) {}
}
