package com.cc.testrepo.multitouch;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class MultiTouchView extends LinearLayout {

    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
    private int mLastDownY;
    private int mVerticalScrollRange;

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mLastDownY = (int)event.getY();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                //  以最晚按下的手指为基准
                int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                mLastDownY = (int)event.getY(index);
                return true;
            case MotionEvent.ACTION_MOVE:
                index = event.findPointerIndex(mActivePointerId);
                int y = (int)event.getY(index);
                overScrollBy(0, mLastDownY - y, getScrollX(), getScrollY(), 0, mVerticalScrollRange, 0, 0, true);
                mLastDownY = y;
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                index = event.getActionIndex();
                int pointerId = event.getPointerId(index);
                if (pointerId == mActivePointerId) {
                    //  index的有效值为0到getPointerCount()-1,所以以下方法可行
                    int newIndex = index == 0 ? 1 : 0;
                    mLastDownY = (int)event.getY(newIndex);
                    mActivePointerId = event.getPointerId(newIndex);
//                    以下写法有问题:
//                        当三指触摸时,先抬起id为0的手指,再抬起mActivePointerId对应的手指,此时mActivePointerId
//                        会被赋值为0,但对应的手指由于被抬起,而导致index为-1,最终导致getY(int)时抛出异常
//                    mActivePointerId = (mActivePointerId == 0) ? 1 : 0;
//                    index = event.findPointerIndex(mActivePointerId);
//                    mLastDownY = (int)event.getY(index);
                }
                return true;
            case MotionEvent.ACTION_UP:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        scrollTo(scrollX, scrollY);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mVerticalScrollRange = 0;
        for (int i=0, count=getChildCount(); i!=count; ++i) {
            mVerticalScrollRange += getChildAt(i).getHeight();
        }
        mVerticalScrollRange = Math.max(0, mVerticalScrollRange - getHeight());
    }
}
