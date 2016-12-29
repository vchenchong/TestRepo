package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

public class PullToRefreshRecyclerView extends WrapRecyclerView {

    private final static float HEADER_OFFSET_RADIO = 1.8f;
    private final static int HEADER_SCROLL_DURATION = 400;

    private LinearLayoutManager mLayoutManager;

    private AbsPullToRefreshRecyclerViewHeader mHeaderView;
    private int mHeaderViewIntrinsicHeight;

    private boolean mEnablePullToRefresh;
    private boolean mIsRefreshing;
    private float mDownY;
    private Scroller mScroller;

    private OnRefreshListener mOnRefreshListener;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (! (layout instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("Only support LinearLayoutManager");
        }
        mLayoutManager = (LinearLayoutManager) layout;
        super.setLayoutManager(layout);
    }

    public void setRefreshHeader(AbsPullToRefreshRecyclerViewHeader headerView) {
        if (mHeaderView != null) {
            removeHeaderView(mHeaderView);
            mHeaderViewIntrinsicHeight = 0;
        }
        mHeaderView = headerView;
        if (headerView != null) {
            headerView.setLayoutParams(new PullToRefreshRecyclerView.LayoutParams(
                    LayoutParams.MATCH_PARENT, 0));
            addHeaderView(headerView, 0);
            mHeaderViewIntrinsicHeight = headerView.getIntrinsicHeight();
        }
    }

    public void enablePullToRefresh(boolean enable) {
        mEnablePullToRefresh = enable;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void stopRefresh() {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            resetHeaderHeight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mDownY = e.getY();
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mEnablePullToRefresh || mHeaderView == null || mLayoutManager == null) {
                    break;
                }

                float y = e.getY();
                float deltaY = y - mDownY;
                mDownY = y;
                if (mLayoutManager.findFirstVisibleItemPosition() <= 1 && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / HEADER_OFFSET_RADIO);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mHeaderView != null && mHeaderView.getVisibleHeight() > 0) {
                    if (!mIsRefreshing && mHeaderView.getVisibleHeight() >= mHeaderViewIntrinsicHeight) {
                        mIsRefreshing = true;
                        mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_REFRESHING);
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    private void updateHeaderHeight(float delta) {
        if (mHeaderView == null || mLayoutManager == null) {
            return;
        }

        int newHeight = Math.max(mHeaderView.getVisibleHeight() + (int)delta, 0);
        mHeaderView.setVisibleHeight(newHeight);

        if (mEnablePullToRefresh && !mIsRefreshing) {
            int visibleHeight = mHeaderView.getVisibleHeight();
            if (visibleHeight == 0) {
                mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_DEFAULT);
            } else if (mHeaderView.getVisibleHeight() < mHeaderViewIntrinsicHeight) {
                mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_PULLING_DOWN);
            } else {
                mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_READY);
            }
        }
        mLayoutManager.scrollToPosition(0);
    }

    private void resetHeaderHeight() {
        if (mHeaderView == null) {
            return;
        }

        int height = mHeaderView.getVisibleHeight();
        int targetHeight = mIsRefreshing ? mHeaderViewIntrinsicHeight : 0;
        if (height == targetHeight) {
            return;
        }

        mScroller.startScroll(0, height, 0, targetHeight - height, HEADER_SCROLL_DURATION);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHeaderView == null) {
            return;
        }

        if (mScroller.computeScrollOffset()) {
            int y = mScroller.getCurrY();
            mHeaderView.setVisibleHeight(y);
            if (y == 0) {
                mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_DEFAULT);
                mScroller.forceFinished(true);
                return;
            }
            invalidate();
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
