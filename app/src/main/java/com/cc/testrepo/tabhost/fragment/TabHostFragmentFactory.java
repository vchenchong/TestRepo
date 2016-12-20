package com.cc.testrepo.tabhost.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by chenchong on 16/11/30.
 */

public class TabHostFragmentFactory {

    public Fragment createFragment(int position) {
        Log.d("chen", ""+ position);
        switch (position) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            case 3:
                return new Fragment4();
            default:
                return null;
        }
    }

    public String getFragmentTag(int position) {
        return "Fragment" + position;
    }

}
