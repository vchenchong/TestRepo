package com.cc.testrepo.expandgridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;

import java.util.ArrayList;
import java.util.List;

public class FixedSpaceGridLayout extends ViewGroup {

    private static final int INVALID_SELECTED_ITEM = -1;

    private int mHorizontalSpace;
    private int mVerticalSpace;
    private int mColumnCount;

    private List<Integer> mLineHeightList;

    private int mSelectedItem = INVALID_SELECTED_ITEM;
    private OnGridItemClickListener mOnGridItemClickListener;
    private OnClickListener mCommonOnClickListener;

    public FixedSpaceGridLayout(Context context) {
        this(context, null);
    }

    public FixedSpaceGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FixedSpaceGridLayout, 0, 0);
        mHorizontalSpace = (int)a.getDimension(R.styleable.FixedSpaceGridLayout_horizontal_space, 0);
        if (mHorizontalSpace < 0) {
            mHorizontalSpace = 0;
        }
        mVerticalSpace = (int)a.getDimension(R.styleable.FixedSpaceGridLayout_vertical_space, 0);
        if (mVerticalSpace < 0) {
            mVerticalSpace = 0;
        }
        mColumnCount = a.getInt(R.styleable.FixedSpaceGridLayout_column_count, 4);
        if (mColumnCount < 1) {
            mColumnCount = 1;
        }
        a.recycle();

        init();
    }

    private void init() {
        mLineHeightList = new ArrayList<>();

        mCommonOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSelectedItem = indexOfChild(v);
                if (newSelectedItem == mSelectedItem) {
                    return;
                }

                View lastSelectedView = getChildAt(mSelectedItem);
                if (lastSelectedView != null) {
                    lastSelectedView.setSelected(false);
                }
                v.setSelected(true);
                mSelectedItem = newSelectedItem;

                if (mOnGridItemClickListener != null) {
                    mOnGridItemClickListener.onGridItemClick(v, newSelectedItem);
                }
            }
        };
    }

    public void setOnGridItemClickListener(OnGridItemClickListener listener) {
        mOnGridItemClickListener = listener;
    }

    public void selectItem(int position) {
        if (position < 0 || position >= getChildCount()) {
            return;
        }

        if (position == mSelectedItem) {
            return;
        }

        View newSelectedView = getChildAt(position);
        if (newSelectedView == null) {
            return;
        }

        View lastSelectedItemView = getChildAt(mSelectedItem);
        if (lastSelectedItemView != null) {
            lastSelectedItemView.setSelected(false);
        }
        newSelectedView.setSelected(true);
        mSelectedItem = position;

        if (mOnGridItemClickListener != null) {
            mOnGridItemClickListener.onGridItemClick(newSelectedView, position);
        }
    }

    public void reset() {
        removeAllViews();
        mSelectedItem = INVALID_SELECTED_ITEM;
        mOnGridItemClickListener = null;
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        child.setOnClickListener(mCommonOnClickListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        mLineHeightList.clear();

        int width, height;
        width = widthSize;
        int childExactWidth = (width - getPaddingLeft() - getPaddingRight() - mHorizontalSpace * (mColumnCount - 1)) / mColumnCount;
        if (childExactWidth < 0) {
            childExactWidth = 0;
        }
        int childWidthSpec = MeasureSpec.makeMeasureSpec(childExactWidth, MeasureSpec.EXACTLY);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        for (int i=0, size=getChildCount(); i < size; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int childrenTotalHeight = getPaddingTop() + getPaddingBottom();
            int visibleChildIndex = 0;
            int lineHeight = 0;
            for (int i=0, size=getChildCount(); i < size; ++i) {
                View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                if (visibleChildIndex % mColumnCount == 0) {
                    if (visibleChildIndex != 0) {
                        childrenTotalHeight += lineHeight + mVerticalSpace;
                        mLineHeightList.add(lineHeight);
                    }
                    lineHeight = 0;
                }
                if (child.getMeasuredHeight() > lineHeight) {
                    lineHeight = child.getMeasuredHeight();
                }
                ++visibleChildIndex;
            }
            if (lineHeight > 0) {
                childrenTotalHeight += lineHeight + mVerticalSpace;
                mLineHeightList.add(lineHeight);
            }
            height = childrenTotalHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }


        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int offsetX = 0;
        int offsetY = getPaddingTop();

        int visibleChildIndex = 0;
        for (int i=0, size=getChildCount(); i<size; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            if (visibleChildIndex % mColumnCount == 0) {
                offsetX = paddingLeft;
                if (visibleChildIndex != 0) {
                    offsetY += mVerticalSpace + mLineHeightList.get(visibleChildIndex / mColumnCount - 1);
                }
            }

            child.layout(offsetX, offsetY, offsetX + child.getMeasuredWidth(), offsetY + child.getMeasuredHeight());

            offsetX += mHorizontalSpace + child.getMeasuredWidth();

            ++visibleChildIndex;
        }
    }

    public interface OnGridItemClickListener {
        void onGridItemClick(View view, int position);
    }
}
