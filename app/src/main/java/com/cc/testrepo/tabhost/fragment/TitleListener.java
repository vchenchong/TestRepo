package com.cc.testrepo.tabhost.fragment;

import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

public interface TitleListener {

    void onFocus();
    void onBlur();
    void onUpdateActionBar(CustomActionBar actionBar);

}
