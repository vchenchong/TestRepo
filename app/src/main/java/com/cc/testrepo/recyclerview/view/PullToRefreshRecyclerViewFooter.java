package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class PullToRefreshRecyclerViewFooter extends AbsPullToRefreshRecyclerViewFooter {

    private TextView mTextView;

    public PullToRefreshRecyclerViewFooter(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshRecyclerViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mTextView = new TextView(context);
        mTextView.setText("HEHE");
        mTextView.setGravity(Gravity.CENTER);
        addView(mTextView, new AbsPullToRefreshRecyclerViewFooter.LayoutParams(-1, 200, Gravity.CENTER));
        setBackgroundColor(Color.YELLOW);
    }

    @Override
    public void setState(@IntRange(from = 0, to = 3) int state) {
        if (state == mState) {
            return;
        }
        mState = state;

        switch (state) {
            case AbsPullToRefreshRecyclerViewFooter.STATE_DEFAULT:
                mTextView.setText("HEHE");
                Log.d("chen", "Footer State to Default");
                break;
            case STATE_LOADING:
                mTextView.setText("Loading");
                Log.d("chen", "Footer State to Loading");
                break;
            case STATE_LOAD_FAILED:
                mTextView.setText("Load Failed");
                Log.d("chen", "Footer State to Load Failed");
                break;
            case STATE_NO_DATA:
                mTextView.setText("No More Data");
                Log.d("chen", "Footer State to No More Data");
                break;
        }
    }
}
