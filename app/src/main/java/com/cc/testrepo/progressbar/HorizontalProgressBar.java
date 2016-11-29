package com.cc.testrepo.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.cc.testrepo.R;

public class HorizontalProgressBar extends View {

    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    private static final float PROGRESS_THRESHOLD = 0.1f;

    private int mProgress;
    private int mMinDisplayableProgress;

    private GradientDrawable mBackgroundDrawable;
    private GradientDrawable mProgressDrawable;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar, 0, 0);
        int color = a.getColor(R.styleable.HorizontalProgressBar_color, Color.BLACK);
        int backgroundStrokeWidth = (int)a.getDimension(R.styleable.HorizontalProgressBar_stroke_width, 1);
        mProgress = a.getInt(R.styleable.HorizontalProgressBar_progress, MIN_PROGRESS);
        a.recycle();

        mBackgroundDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.horizontal_progress_bar_bg);
        mProgressDrawable = (GradientDrawable)ContextCompat.getDrawable(context, R.drawable.horizontal_progress_bar_progress);
        mBackgroundDrawable.setStroke(backgroundStrokeWidth, color);
        mProgressDrawable.setColor(color);

        mMinDisplayableProgress = (int)(MAX_PROGRESS * PROGRESS_THRESHOLD);
    }

    public void setProgress(int progress) {
        if (progress < MIN_PROGRESS) {
            progress = MIN_PROGRESS;
        } else if (progress > MAX_PROGRESS) {
            progress = MAX_PROGRESS;
        }

        if (mProgress != progress) {
            mProgress = progress;
            invalidate();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress < MIN_PROGRESS) {
            mProgress = MIN_PROGRESS;
        }
        if (mProgress > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
        }

        int width = getWidth();
        int height = getHeight();
        mBackgroundDrawable.setBounds(0, 0, width - 1, height - 1);
        mBackgroundDrawable.draw(canvas);

        /**
         * 当mProgress过小时,mProgressDrawable会定位在mBackgroundDrawable外面,所以加了个限制
         */
        if (mProgress < mMinDisplayableProgress) {
            mProgress = mMinDisplayableProgress;
        }
        mProgressDrawable.setBounds(0, 0, (int)((width - 1) * (mProgress * 1f / MAX_PROGRESS)), height - 1);
        mProgressDrawable.draw(canvas);
    }
}
