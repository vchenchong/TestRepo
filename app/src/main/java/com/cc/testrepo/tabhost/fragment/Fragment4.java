package com.cc.testrepo.tabhost.fragment;

import android.graphics.Color;

import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

public class Fragment4 extends TabHostBaseFragment {

    @Override
    protected int getFragmentId() {
        return 3;
    }

    @Override
    protected int getFragmentColor() {
        return Color.BLUE;
    }

    @Override
    public void onUpdateActionBar(CustomActionBar customContainer) {
        super.onUpdateActionBar(customContainer);
        customContainer.resetCustomContainer();
    }
}
