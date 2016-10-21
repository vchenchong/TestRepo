package com.cc.testrepo.expandgridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;

import java.util.ArrayList;
import java.util.List;

public class EqualDivisionGridLayout extends ViewGroup {

    /**
     *  SPACE_MODE表示计算水平间隔的方式,WITH_OUT_BOUND表示仅将水平多余的空间分摊在子视图之间
     */
    public static final int SPACE_MODE_WITH_BOUND = 0;
    public static final int SPACE_MODE_WITHOUT_BOUND = 1;

    private int mColumnCount;

    /**
     *  垂直间隔
     */
    private int mDividerHeight;

    private int mSpaceMode;

    /**
     *  每一行元素的宽度和
     */
    private List<Integer> mLineWidthList;

    /**
     *  每一行元素的最大高度
     */
    private List<Integer> mLineHeightList;

    public EqualDivisionGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EqualDivisionGridLayout, 0, 0);
        for (int i=0, size=a.length(); i<size; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.EqualDivisionGridLayout_column_count:
                    mColumnCount = a.getInteger(attr, 1);
                    break;
                case R.styleable.EqualDivisionGridLayout_divider_height:
                    mDividerHeight = (int)a.getDimension(attr, 0);
                    break;
                case R.styleable.EqualDivisionGridLayout_space_mode:
                    mSpaceMode = a.getInt(R.styleable.EqualDivisionGridLayout_space_mode, SPACE_MODE_WITH_BOUND);
                    break;
            }
        }
        a.recycle();

        checkParameterValidity();
        init();
    }

    private void checkParameterValidity() {
        mColumnCount = Math.max(mColumnCount, 1);
        mDividerHeight = Math.max(mDividerHeight, 0);
    }

    private void init() {
        mLineWidthList = new ArrayList<>();
        mLineHeightList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();

        mLineWidthList.clear();
        mLineHeightList.clear();

        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i=0; i<childCount; ++i) {
            if (i % mColumnCount == 0 && i != 0) {
                if (lineWidth > width) {
                    width = lineWidth;
                }
                mLineWidthList.add(lineWidth);
                lineWidth = 0;

                height += lineHeight;
                mLineHeightList.add(lineHeight);
                lineHeight = 0;
            }
            lineWidth += getChildAt(i).getMeasuredWidth();
            int childHeight = getChildAt(i).getMeasuredHeight();
            if (childHeight > lineHeight) {
                lineHeight = childHeight;
            }
        }
        if (lineWidth > 0) {
            //  最后一行,若子视图个数不足mColumnCount,按比例计算该行应该占据的宽度
            int lastLineViewCount = childCount % mColumnCount;
            if (lastLineViewCount != 0) {
                lineWidth = (int)(lineWidth * (double)(mColumnCount) / lastLineViewCount);
            }
            mLineWidthList.add(lineWidth);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        if (lineHeight > 0) {
            mLineHeightList.add(lineHeight);
            height += lineHeight;
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(width + getPaddingLeft() + getPaddingRight(), widthSize);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = height + getPaddingTop() + getPaddingBottom() + mDividerHeight * (mLineHeightList.size() - 1);
            height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int childCount = getChildCount();
        int width = getMeasuredWidth();

        int lineIndex;
        int lineHeight = 0;
        int x = 0;
        int y = getPaddingTop();
        int space = 0;
        int spaceCount = mSpaceMode == SPACE_MODE_WITH_BOUND || mColumnCount == 1 ? mColumnCount + 1 : mColumnCount - 1;
        for (int i=0; i<childCount; ++i) {
            if (i % mColumnCount == 0) {
                lineIndex = i / mColumnCount;
                lineHeight = mLineHeightList.get(lineIndex);
                space = (width - paddingLeft - paddingRight - mLineWidthList.get(lineIndex)) / spaceCount;
                if (space < 0) {
                    space = 0;
                }
                if (mSpaceMode == SPACE_MODE_WITH_BOUND) {
                    x = paddingLeft + space;
                } else {
                    x = paddingLeft;
                }
                if (lineIndex > 0) {
                    y += mLineHeightList.get(lineIndex - 1) + mDividerHeight;
                }
            }

            View view = getChildAt(i);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            int topOffset = (lineHeight - measuredHeight) / 2;
            view.layout(x, y + topOffset, x + measuredWidth, y + topOffset + measuredHeight);
            x += measuredWidth + space;
        }
    }
}
