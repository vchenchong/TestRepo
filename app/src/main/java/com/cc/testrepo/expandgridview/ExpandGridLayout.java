package com.cc.testrepo.expandgridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.cc.testrepo.R;


public class ExpandGridLayout extends LinearLayout {

    private static final int ANIM_DURATION = 300;

    private GridLayout mTopGrid;
    private FrameLayout mBottomGridWrapper;
    private GridLayout mBottomGrid;

    private boolean mIsExpanded;
    private boolean mIsFirstMeasure = true;
    private int mBottomGridHeight;
    private ValueAnimator mCurrentAnimator;

    private int mVisibleLineCount;
    private int mColumnCount;

    private ExpandGridViewAdapter mAdapter;

    private OnItemClickListener mOnItemClickListener;
    private View mSelectedChildView;

    public ExpandGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandGridLayout, 0, 0);
        for (int i=0, size=a.length(); i<size; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ExpandGridLayout_visibleLineCount:
                    mVisibleLineCount = a.getInteger(attr, 1);
                    if (mVisibleLineCount < 0) {
                        throw new IllegalArgumentException("visibleLineCount can not be negative");
                    }
                    break;
                case R.styleable.ExpandGridLayout_columnCount:
                    mColumnCount = a.getInteger(attr, 1);
                    if (mColumnCount < 0) {
                        throw new IllegalArgumentException("columnCount can not be negative");
                    }
                    break;
            }
        }
        a.recycle();

        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mTopGrid = new GridLayout(context);
        mTopGrid.setColumnCount(mColumnCount);
        addView(mTopGrid, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mBottomGridWrapper = new FrameLayout(context);
        mBottomGrid = new GridLayout(context);
        mBottomGrid.setColumnCount(mColumnCount);
        mBottomGridWrapper.addView(mBottomGrid, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        addView(mBottomGridWrapper, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public void setAdapter(ExpandGridViewAdapter adapter) {
        if (adapter != mAdapter) {
            mTopGrid.removeAllViews();
            mBottomGrid.removeAllViews();
            mAdapter = adapter;
            mIsFirstMeasure = true;
            if (mAdapter != null) {
                int visibleItemCount = mVisibleLineCount * mColumnCount;
                int itemCount = mAdapter.getCount();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                for (int i=0; i<itemCount; ++i) {
                    ViewGroup parent;
                    if (i < visibleItemCount) {
                        parent = mTopGrid;
                    } else {
                        parent = mBottomGrid;
                    }
                    final int position = i;
                    final View view = mAdapter.getView(inflater, position, parent);
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v == mSelectedChildView) {
                                return;
                            }

                            if (mSelectedChildView != null) {
                                mSelectedChildView.setSelected(false);
                            }
                            v.setSelected(true);
                            mSelectedChildView = v;

                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(view, position);
                            }
                        }
                    });
                    parent.addView(view);
                }
            }
        }
    }

    public void setExpandState(boolean expand) {
        setExpandState(expand, false);
    }

    public void setExpandState(boolean expand, boolean withAnim) {
        if (!canExpand()) {
            return;
        }

        if (expand) {
            if (!mIsExpanded) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.end();
                }

                if (withAnim) {
                    mCurrentAnimator = ValueAnimator.ofInt(0, mBottomGridHeight);
                    mCurrentAnimator.setDuration(ANIM_DURATION);
                    mCurrentAnimator.addUpdateListener(mExpandAnimListener);
                    mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mIsExpanded = true;
                            mCurrentAnimator = null;
                        }
                    });
                    mCurrentAnimator.start();
                } else {
                    mBottomGridWrapper.getLayoutParams().height = mBottomGridHeight;
                    mIsExpanded = true;
                    requestLayout();
                }
            }
        } else {
            if (mIsExpanded) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.end();
                }

                if (withAnim) {
                    mCurrentAnimator = ValueAnimator.ofInt(mBottomGridHeight, 0);
                    mCurrentAnimator.setDuration(ANIM_DURATION);
                    mCurrentAnimator.addUpdateListener(mExpandAnimListener);
                    mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mIsExpanded = false;
                            mCurrentAnimator = null;
                        }
                    });
                    mCurrentAnimator.start();
                } else {
                    mBottomGridWrapper.getLayoutParams().height = 0;
                    mIsExpanded = false;
                    requestLayout();
                }
            }
        }
    }

    private ValueAnimator.AnimatorUpdateListener mExpandAnimListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mBottomGridWrapper.getLayoutParams().height = (int)animation.getAnimatedValue();
            requestLayout();
        }
    };

    private boolean canExpand() {
        return mAdapter != null && mAdapter.getCount() >= mColumnCount;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsFirstMeasure) {
            mBottomGridHeight = mBottomGridWrapper.getMeasuredHeight();
            mBottomGridWrapper.getLayoutParams().height = 0;
            mIsFirstMeasure = false;
        }
    }

    public interface ExpandGridViewAdapter {
        int getCount();
        @NonNull
        View getView(LayoutInflater inflater, int position, ViewGroup parent);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
