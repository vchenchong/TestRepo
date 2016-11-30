package com.cc.testrepo.tabhost.fragment;

import android.graphics.Color;

import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

public class Fragment1 extends TabHostBaseFragment {

    @Override
    protected int getFragmentId() {
        return 0;
    }

    @Override
    protected int getFragmentColor() {
        return Color.WHITE;
    }

    @Override
    public void onUpdateActionBar(CustomActionBar customContainer) {
        super.onUpdateActionBar(customContainer);
//        Button btn = new Button(getContext());
//        btn.setText("HAHA");
//        customContainer.getCustomContainer().addView(btn, new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
    }
}
