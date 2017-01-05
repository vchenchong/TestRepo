package com.cc.testrepo.recyclerview.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SimpleLinearDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight;
    private Drawable mDrawable;

    public SimpleLinearDecoration(int dividerHeight, Drawable drawable) {
        mDividerHeight = dividerHeight;
        mDrawable = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // 如果放在onDrawOver实现,分割线会在被拖拽的元素之后绘制
        if (mDrawable == null) {
            return;
        }

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i=0, size=parent.getChildCount(); i<size; ++i) {
            if (i == size - 1) {
                break;
            }

            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + lp.bottomMargin;
            mDrawable.setBounds(left, top, right, top + mDividerHeight);
            mDrawable.draw(c);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        if (mDrawable == null) {
//            return;
//        }
//
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();
//        for (int i=0, size=parent.getChildCount(); i<size; ++i) {
//            if (i == size - 1) {
//                break;
//            }
//
//            View child = parent.getChildAt(i);
//            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
//            int top = child.getBottom() + lp.bottomMargin;
//            mDrawable.setBounds(left, top, right, top + mDividerHeight);
//            mDrawable.draw(c);
//        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}
