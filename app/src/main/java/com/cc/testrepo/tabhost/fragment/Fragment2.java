package com.cc.testrepo.tabhost.fragment;

import android.graphics.Color;

import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

public class Fragment2 extends TabHostBaseFragment {

    @Override
    protected int getFragmentId() {
        return 1;
    }

    @Override
    protected int getFragmentColor() {
        return Color.RED;
    }

    @Override
    public void onUpdateActionBar(CustomActionBar customContainer) {
        super.onUpdateActionBar(customContainer);
        customContainer.resetCustomContainer();
    }
}
