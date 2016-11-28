package com.cc.testrepo.tabhost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;

public class BadgeViewGroup extends ViewGroup {

    private static final int ICON_GRAVITY_LEFT = 0;
    private static final int ICON_GRAVITY_RIGHT = 1;

    private int mIconGravity;
    private int mIconOffsetX;
    private int mIconOffsetY;

    private IconView mIconView;
    private View mContentView;

    public BadgeViewGroup(Context context) {
        this(context, null);
    }

    public BadgeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeViewGroup, 0, 0);
        mIconGravity = a.getInt(R.styleable.BadgeViewGroup_icon_gravity, ICON_GRAVITY_RIGHT);
        mIconOffsetX = (int)a.getDimension(R.styleable.BadgeViewGroup_icon_offset_x, 0);
        mIconOffsetY = (int)a.getDimension(R.styleable.BadgeViewGroup_icon_offset_y, 0);

        //  初始化标签视图
        mIconView = new IconView(context);
        mIconView.setText(a.getString(R.styleable.BadgeViewGroup_icon_text));
        mIconView.setTextSize(a.getDimension(R.styleable.BadgeViewGroup_icon_text_size, 30));
        mIconView.setTextColor(a.getColor(R.styleable.BadgeViewGroup_icon_text_color, Color.WHITE));
        mIconView.setDotSize((int)a.getDimension(R.styleable.BadgeViewGroup_icon_dot_size, 9));
        if (Build.VERSION.SDK_INT >= 16) {
            mIconView.setBackground(a.getDrawable(R.styleable.BadgeViewGroup_icon_background));
        } else {
            mIconView.setBackgroundDrawable(a.getDrawable(R.styleable.BadgeViewGroup_icon_background));
        }
        if (!a.getBoolean(R.styleable.BadgeViewGroup_icon_visible, false)) {
            mIconView.setVisibility(View.GONE);
        }
        addView(mIconView, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        a.recycle();

        setClipChildren(false);
        setChildrenDrawingOrderEnabled(true);
    }

    public void showIcon() {
        mIconView.setVisibility(View.VISIBLE);
    }

    public void hideIcon() {
        mIconView.setVisibility(View.GONE);
    }

    public void setIconText(String iconText) {
        mIconView.setText(iconText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            mContentView = null;
            return;
        }

        mContentView = getChildAt(0);
        if (mContentView == mIconView) {
            mContentView = getChildAt(1);
        }

        if (mContentView.getVisibility() == View.GONE) {
            setMeasuredDimension(0, 0);
            return;
        }

        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        if (mIconView.getVisibility() == View.VISIBLE) {
            measureChild(mIconView, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mContentView == null || mContentView.getVisibility() == View.GONE) {
            return;
        }

        int contentWidth = mContentView.getMeasuredWidth();
        int contentHeight = mContentView.getMeasuredHeight();
        mContentView.layout(0, 0, contentWidth, contentHeight);

        // layout mIconView
        int offsetX;
        int offsetY;
        int iconWidth = mIconView.getMeasuredWidth();
        int iconHeight = mIconView.getMeasuredHeight();

        switch (mIconGravity) {
            case ICON_GRAVITY_LEFT:
                offsetX = -iconWidth / 2;
                offsetY = -iconHeight / 2;
                break;
            default:
                offsetX = contentWidth - iconWidth / 2;
                offsetY = -iconHeight / 2;
        }
        offsetX += mIconOffsetX;
        offsetY += mIconOffsetY;
        mIconView.layout(offsetX, offsetY, offsetX + iconWidth, offsetY + iconHeight);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        // 确保标签页在内容页上面
        if (getChildAt(i) == mIconView) {
            return 1;
        } else {
            return 0;
        }
    }

    private static class IconView extends View {

        private String mText;

        private Paint mPaint;
        private Rect mRect;

        private int mDotSize;

        public IconView(Context context) {
            super(context);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setTextAlign(Paint.Align.CENTER);

            mRect = new Rect();
        }

        public void setText(String text) {
            if (mText == null) {
                mText = "";
            }
            if (text == null) {
                text = "";
            }
            if (mText.equals(text)) {
                return;
            }

            mText = text;
            requestLayout();
        }

        public void setTextColor(int textColor) {
            if (mPaint.getColor() == textColor) {
                return;
            }

            mPaint.setColor(textColor);
            invalidate();
        }

        public void setTextSize(float textSize) {
            if (mPaint.getTextSize() == textSize) {
                return;
            }

            mPaint.setTextSize(textSize);
            requestLayout();
        }

        public void setDotSize(int dotSize) {
            if (dotSize == mDotSize) {
                return;
            }

            mDotSize = dotSize;
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (TextUtils.isEmpty(mText)) {
                setMeasuredDimension(mDotSize, mDotSize);
            } else {
                mPaint.getTextBounds(mText, 0, mText.length(), mRect);
                int width = mRect.width();
                int height = mRect.height();
                if (width < height) {
                    width = height;
                }
//                width += getPaddingLeft() + getPaddingRight();
//                height += getPaddingTop() + getPaddingBottom();

                Drawable drawable = getBackground();
                if (drawable != null && drawable.getPadding(mRect)) {
                    width += mRect.left + mRect.right;
                    height += mRect.top + mRect.bottom;
                }

                setMeasuredDimension(width, height);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (TextUtils.isEmpty(mText)) {
                return;
            }


            Paint.FontMetrics fm = mPaint.getFontMetrics();
            int offsetX = getWidth() / 2;
//            float baselineY = getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2;
            float baselineY = (getHeight() - fm.descent - fm.ascent) / 2;
            canvas.drawText(mText, offsetX, baselineY, mPaint);
        }
    }
}
