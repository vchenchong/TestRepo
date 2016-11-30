package com.cc.testrepo.tabhost.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cc.testrepo.R;

/**
 * Created by chenchong on 16/11/29.
 */

public class CustomActionBar extends FrameLayout {

    private View mBackButton;
    private FrameLayout mCustomContainer;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBackButton = findViewById(R.id.action_bar_back);
        mCustomContainer = (FrameLayout) findViewById(R.id.action_bar_custom);
    }

    public void setOnBackPressedListener(OnClickListener l) {
        mBackButton.setOnClickListener(l);
    }

    public ViewGroup getCustomContainer() {
        return mCustomContainer;
    }

    public void resetCustomContainer() {
        mCustomContainer.removeAllViews();
    }
}
