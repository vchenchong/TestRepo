package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;


public class PullToRefreshRecyclerViewHeader extends AbsPullToRefreshRecyclerViewHeader {

    private TextView mTextView;

    private int mVisibleHeight;

    public PullToRefreshRecyclerViewHeader(Context context) {
        super(context);
        mTextView = new TextView(context);
        mTextView.setBackgroundColor(Color.YELLOW);
        mTextView.setText("HAHA");
        setBackgroundColor(Color.BLUE);
        addView(mTextView, new AbsPullToRefreshRecyclerViewHeader.LayoutParams(-2, -2, Gravity.CENTER));
    }

    @Override
    protected int getIntrinsicHeight() {
        return 300;
    }

    @Override
    protected void setVisibleHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
        mVisibleHeight = height;
    }

    @Override
    protected int getVisibleHeight() {
        return mVisibleHeight;
    }

    @Override
    protected void setState(@IntRange(from = 0, to = 3) int state) {
        if (mState == state) {
            return;
        }

        mState = state;
        switch (state) {
            case STATE_DEFAULT:
                mTextView.setText("Default");
                Log.d("chen", "State to Default");
                break;
            case STATE_PULLING_DOWN:
                mTextView.setText("Pulling Down");
                Log.d("chen", "State to Pulling Down");
                break;
            case STATE_READY:
                mTextView.setText("Ready");
                Log.d("chen", "State to Ready");
                break;
            case STATE_REFRESHING:
                mTextView.setText("Refreshing");
                Log.d("chen", "State to Refreshing");
                break;
        }
    }
}
