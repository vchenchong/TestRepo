package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

public class PullToRefreshRecyclerView extends WrapRecyclerView {

    public static final int STATE_LOAD_MORE_COMPLETE = 0;
    public static final int STATE_LOAD_MORE_FAILED = 1;
    public static final int STATE_LOAD_MORE_NO_DATA = 2;

    private final static float HEADER_OFFSET_RADIO = 1.8f;
    private final static int HEADER_SCROLL_DURATION = 400;

    private LinearLayoutManager mLayoutManager;

    private AbsPullToRefreshRecyclerViewHeader mHeaderView;
    private int mHeaderViewIntrinsicHeight;

    private boolean mEnablePullToRefresh;
    private boolean mIsRefreshing;
    private float mDownY;
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
    private Scroller mScroller;

    private OnRefreshListener mOnRefreshListener;

    private AbsPullToRefreshRecyclerViewFooter mFooterView;
    private boolean mEnableLoadMore;
    private boolean mIsLoadingMore;

    private OnLoadMoreListener mOnLoadMoreListener;

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
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mEnableLoadMore || mFooterView == null || mLayoutManager == null || getAdapter() == null) {
                    return;
                }
                if (mIsLoadingMore) {
                    return;
                }
                if (mFooterView.getParent() == null) {
                    return;
                }

                int footerTop = mLayoutManager.getDecoratedTop(mFooterView);
                int footerBottom = mLayoutManager.getDecoratedBottom(mFooterView);
                int recyclerViewBottom = getBottom() - getPaddingBottom();
                if (footerTop <= recyclerViewBottom && footerBottom >= recyclerViewBottom) {
                    Log.d("chen", "reach End");
                    mFooterView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_LOADING);
                    mFooterView.show();
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                        mIsLoadingMore = true;
                    }
                }
            }
        });
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
            mHeaderView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_DEFAULT);
            removeHeaderView(mHeaderView);
            mHeaderViewIntrinsicHeight = 0;
        }
        mHeaderView = headerView;
        if (headerView != null) {
            headerView.setState(AbsPullToRefreshRecyclerViewHeader.STATE_DEFAULT);
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

    public void setLoadMoreFooter(AbsPullToRefreshRecyclerViewFooter footerView) {
        if (mFooterView != null) {
            mFooterView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_DEFAULT);
            removeFooterView(mFooterView);
        }
        mFooterView = footerView;
        if (footerView != null) {
            footerView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_DEFAULT);
            footerView.setLayoutParams(new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            footerView.hide();
            addFooterView(footerView);
        }
    }

    public void enableLoadMore(boolean enable) {
        mEnableLoadMore = enable;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public void stopLoadMore(@IntRange(from = 0, to = 2) int result) {
        if (mFooterView == null) {
            return;
        }

        switch (result) {
            case STATE_LOAD_MORE_COMPLETE:
                mFooterView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_DEFAULT);
                mFooterView.hide();
                break;
            case STATE_LOAD_MORE_FAILED:
                mFooterView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_LOAD_FAILED);
                break;
            case STATE_LOAD_MORE_NO_DATA:
                mFooterView.setState(AbsPullToRefreshRecyclerViewFooter.STATE_NO_DATA);
                break;
        }
        mIsLoadingMore = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = e.getPointerId(0);
                mDownY = e.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int index = e.getActionIndex();
                mActivePointerId = e.getPointerId(index);
                mDownY = e.getY();
                break;
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
                mActivePointerId = e.getPointerId(0);
                mDownY = e.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int index = e.getActionIndex();
                mActivePointerId = e.getPointerId(index);
                mDownY = e.getY(index);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mEnablePullToRefresh || mHeaderView == null || mLayoutManager == null) {
                    break;
                }
                index = e.findPointerIndex(mActivePointerId);
                float y = e.getY(index);
                float deltaY = y - mDownY;
                mDownY = y;
                // 对于mLayoutManager.findFirstCompletelyVisibleItemPosition()的返回值:
                //    -1: Header隐藏且没有数据,此时可以下拉刷新
                //     0: Header部分或全部显示(getVisibleHeight() > 0),可以下拉刷新
                //     1: HeaderView影藏(getVisibleHeight() == 0),此时HeaderView不算作VisibleItem
                if (mLayoutManager.findFirstCompletelyVisibleItemPosition() <= 1
                        && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / HEADER_OFFSET_RADIO);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                index = e.getActionIndex();
                int pointerId = e.getPointerId(index);
                if (pointerId == mActivePointerId) {
                    int newIndex = index == 0 ? 1 : 0;
                    mDownY = e.getY(newIndex);
                    mActivePointerId = e.getPointerId(newIndex);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
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

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
