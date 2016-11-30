package com.cc.testrepo.expandgridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EqualDivisionGridLayout extends ViewGroup {

    public static final int GRAVITY_LEFT = 0x01;
    public static final int GRAVITY_CENTER_HORIZONTAL = 0x02;
    public static final int GRAVITY_RIGHT = 0x04;
    public static final int GRAVITY_TOP = 0x10;
    public static final int GRAVITY_CENTER_VERTICAL = 0x20;
    public static final int GRAVITY_BOTTOM = 0x40;
    public static final int GRAVITY_CENTER = GRAVITY_CENTER_HORIZONTAL | GRAVITY_CENTER_VERTICAL;
    private static final int GRAVITY_HORIZONTAL_MASK = 0x0F;
    private static final int GRAVITY_VERTICAL_MASK = 0xF0;

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

    private int mGravity;

    private int mSpaceMode;

    /**
     *  每一列的最大宽度
     */
    private int[] mMaxWidthPerColumn;

    /**
     *  每一行元素的最大高度
     */
    private List<Integer> mLineHeightList;

    private int mVisibleChildCount;

    private OnClickListener mCommonOnClickListener;
    private OnGridItemClickListener mGridItemClickListener;
    private int mSelectedItem = -1;

    public EqualDivisionGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EqualDivisionGridLayout, 0, 0);

        mColumnCount = a.getInteger(R.styleable.EqualDivisionGridLayout_column_count, 1);
        mColumnCount = Math.max(mColumnCount, 1);

        mDividerHeight = (int)a.getDimension(R.styleable.EqualDivisionGridLayout_divider_height, 0);
        mDividerHeight = Math.max(mDividerHeight, 0);

        mSpaceMode = a.getInt(R.styleable.EqualDivisionGridLayout_space_mode, SPACE_MODE_WITH_BOUND);

        mGravity = a.getInt(R.styleable.EqualDivisionGridLayout_gravity, GRAVITY_CENTER);

        a.recycle();

        init();
        initClickListener();
    }

    private void init() {
        mMaxWidthPerColumn = new int[mColumnCount];
        mLineHeightList = new ArrayList<>();
    }

    private void initClickListener() {
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

                if (mGridItemClickListener != null) {
                    mGridItemClickListener.onGridItemClick(v, newSelectedItem);
                }
            }
        };
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        if (child != null) {
            child.setOnClickListener(mCommonOnClickListener);
        }
    }

    public void setOnGridItemClickListener(OnGridItemClickListener listener) {
        mGridItemClickListener = listener;
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

        if (mGridItemClickListener != null) {
            mGridItemClickListener.onGridItemClick(newSelectedView, position);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        mVisibleChildCount = childCount;

        Arrays.fill(mMaxWidthPerColumn, 0);
        mLineHeightList.clear();

        int lineHeight = 0;
        for (int i=0, visibleIndex=0; i<childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                --mVisibleChildCount;
                continue;
            }

            if (visibleIndex % mColumnCount == 0 && visibleIndex != 0) {
                mLineHeightList.add(lineHeight);
                lineHeight = 0;
            }

            int childWidth = child.getMeasuredWidth();
            int columnIndex = visibleIndex % mColumnCount;
            if (childWidth > mMaxWidthPerColumn[columnIndex]) {
                mMaxWidthPerColumn[columnIndex] = childWidth;
            }

            int childHeight = child.getMeasuredHeight();
            if (childHeight > lineHeight) {
                lineHeight = childHeight;
            }

            ++visibleIndex;
        }
        if (lineHeight > 0) {
            mLineHeightList.add(lineHeight);
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //  计算宽度需要考虑可见子视图个数不满mColumn的情况
            for (int i=0; i!=mColumnCount; ++i) {
                width += mMaxWidthPerColumn[i];
            }
            width = scaleWidthIfNeeded(width);
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width + getPaddingLeft() + getPaddingRight(), widthSize);
            }
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            for (int i=0, size=mLineHeightList.size(); i!=size; ++i) {
                height += mLineHeightList.get(i);
            }
            height = height + getPaddingTop() + getPaddingBottom() +
                    (mLineHeightList.size() > 0 ? mDividerHeight * (mLineHeightList.size() - 1) : 0);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();

        int horizontalGravity = mGravity & GRAVITY_HORIZONTAL_MASK;
        int verticalGravity = mGravity & GRAVITY_VERTICAL_MASK;

        //  计算子视图水平间距space
        int childrenWidth = 0;
        for (int i=0; i!=mColumnCount; ++i) {
            childrenWidth += mMaxWidthPerColumn[i];
        }
        childrenWidth = scaleWidthIfNeeded(childrenWidth);
        int spaceCount = mSpaceMode == SPACE_MODE_WITH_BOUND || mColumnCount == 1 ? mColumnCount + 1 : mColumnCount - 1;
        int space = Math.max(((getMeasuredWidth() - paddingLeft - getPaddingRight() - childrenWidth) / spaceCount), 0);

        int lineHeight = 0;
        int lineIndex = 0;
        int x = 0;
        int y = getPaddingTop();

        for (int i=0, visibleIndex=0, childCount=getChildCount(); i<childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            if (visibleIndex % mColumnCount == 0) {
                if (mSpaceMode == SPACE_MODE_WITH_BOUND) {
                    x = paddingLeft + space;
                } else {
                    x = paddingLeft;
                }

                lineIndex = visibleIndex / mColumnCount;
                if (lineIndex > 0) {
                    y += lineHeight + mDividerHeight;
                }
                lineHeight = mLineHeightList.get(lineIndex);
            }

            //  水平
            int leftOffset;
            int measuredWidth = child.getMeasuredWidth();
            int columnIndex = visibleIndex % mColumnCount;
            switch (horizontalGravity) {
                case GRAVITY_LEFT:
                    leftOffset = 0;
                    break;
                case GRAVITY_RIGHT:
                    leftOffset = mMaxWidthPerColumn[columnIndex] - measuredWidth;
                    break;
                case GRAVITY_CENTER_HORIZONTAL:
                default:
                    leftOffset = (mMaxWidthPerColumn[columnIndex] - measuredWidth) / 2;
                    break;
            }

            //  垂直
            int topOffset;
            int measuredHeight = child.getMeasuredHeight();
            switch (verticalGravity) {
                case GRAVITY_TOP:
                    topOffset = 0;
                    break;
                case GRAVITY_BOTTOM:
                    topOffset = mLineHeightList.get(lineIndex) - measuredHeight;
                    break;
                case GRAVITY_CENTER_VERTICAL:
                default:
                    topOffset = (mLineHeightList.get(lineIndex) - measuredHeight) / 2;
                    break;
            }

            child.layout(x + leftOffset, y + topOffset, x + leftOffset + measuredWidth, y + topOffset + measuredHeight);
            x += mMaxWidthPerColumn[columnIndex] + space;
            ++visibleIndex;
        }
    }

    /**
     *  若可见子视图个数不足列数,按比例进行放大
     * @param width 可见视图的宽度和
     * @return 放大后的宽度
     */
    private int scaleWidthIfNeeded(int width) {
        if (mVisibleChildCount > 0 && mVisibleChildCount < mColumnCount) {
            return (int)((double)width * mColumnCount / mVisibleChildCount);
        }
        return width;
    }

    public interface OnGridItemClickListener {
        void onGridItemClick(View view, int position);
    }
}
