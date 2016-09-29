package com.cc.testrepo.overscroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.cc.testrepo.R;

/**
 *  Scroller.computeScrollOffset()中,会根据滑动状态做判断,Interpolator只会影响SCROLL_MODE,而不会作用于
 *  FLING_MODE。而fling似乎只是通过输入的边界和速度省略了一些判断和计算。
 *  View.overScrollBy(...)的场景感觉只体现在对OverScroll的范围限制上
 */
public class OverScrollView extends ViewGroup {

    private LinearLayout mContent;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mMaxFlingSpeed;
    private int mDownY;

    private int mOverScrollDistance;

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverScrollView, 0, 0);
        for (int i=0, size=a.length(); i<size; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.OverScrollView_overScrollDistance:
                    mOverScrollDistance = (int)a.getDimension(attr, 0);
                    break;
            }
        }
        a.recycle();

        init(context);
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context, new OvershootInterpolator());
        mVelocityTracker = VelocityTracker.obtain();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxFlingSpeed = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (LinearLayout)findViewById(R.id.ll);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mDownY = (int)ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                int y = (int)ev.getY();
                return Math.abs(y - mDownY) > mTouchSlop;
            default:
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int)event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                int y = (int)event.getY();
//                overScrollBy(0, mDownY - y, getScrollX(), getScrollY(), 0, getVerticalScrollRange()
//                        , 0, mOverScrollDistance, true);
                scrollBy(0, mDownY - y);
                mDownY = y;
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mVelocityTracker.clear();
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingSpeed);
                int v = (int)mVelocityTracker.getYVelocity();
                int scrollY = getScrollY();
                if (scrollY < 0) {
                    mScroller.startScroll(0, scrollY, 0, -scrollY, 1000);
                } else if (scrollY > getVerticalScrollRange()) {
                    mScroller.startScroll(0, scrollY, 0, getVerticalScrollRange() - scrollY, 1000);
                }

                mVelocityTracker.clear();
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
////        Log.d("chen", String.format("onOverScroller - scrollX: %d, scrollY: %d, clampedX: %b, calmpedY: %b",
////                scrollX, scrollY, clampedX, clampedY));
//        if (mScroller.isFinished()) {
//            scrollTo(scrollX, scrollY);
//        } else {
//            if (clampedY) {
//                if (mScroller.springBack(scrollX, scrollY, 0, 0, 0, getVerticalScrollRange())) {
//                    invalidate();
//                }
//            }
//        }
//    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private int getVerticalScrollRange() {
        return Math.max(0, mContent.getHeight() - getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        View child = null;
        if (getChildCount() > 0) {
            child = getChildAt(0);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childHeightSpec;
            if (lp.height >= 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(1 << 30 - 1, MeasureSpec.AT_MOST);
            }
            child.measure(widthMeasureSpec, childHeightSpec);
        }

        int width = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                if (child != null) {
                    width = Math.min(widthSize, child.getMeasuredWidth());
                }
                break;
        }

        setMeasuredDimension(width, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }
}
