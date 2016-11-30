package com.cc.testrepo.tabhost.fragment;

import android.graphics.Color;

/**
 * Created by chenchong on 16/11/30.
 */

public class Fragment3 extends TabHostBaseFragment {

    @Override
    protected int getFragmentId() {
        return 2;
    }

    @Override
    protected int getFragmentColor() {
        return Color.YELLOW;
    }
}
