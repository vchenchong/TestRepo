package com.cc.testrepo.nestedscroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.cc.testrepo.R;
import com.cc.testrepo.base.SimpleRecyclerViewAdapter;

public class NestedScrollTestView extends LinearLayout implements NestedScrollingParent {

    private View mHeaderView;
    private RecyclerView mRecyclerView;

    private int mHeaderHeight;
    private OverScroller mScroller;

    public NestedScrollTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.header);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new SimpleRecyclerViewAdapter());

        mScroller = new OverScroller(getContext());
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mHeaderHeight = mHeaderView.getHeight();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //  向上推RV,dy是正
        if (dy > 0) {
            if (getScrollY() < mHeaderHeight) {
                int scrollDistance = Math.min(dy, mHeaderHeight - getScrollY());
                scrollBy(0, scrollDistance);
                consumed[1] = scrollDistance;
            }
        } else if (dy < 0) {
            if (getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1)) {
                //  -1表示target不能向下滑
                int scrollDistance = Math.min(-dy, getScrollY());
                scrollBy(0, -scrollDistance);
                consumed[1] = scrollDistance;
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() < mHeaderHeight && velocityY > 0) {
            mScroller.fling(0, getScrollY(), 0, (int)velocityY, 0, 0, 0, mHeaderHeight);
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int rvHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        mRecyclerView.measure(widthMeasureSpec, rvHeightMeasureSpec);
    }
}
