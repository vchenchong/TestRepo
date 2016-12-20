package com.cc.testrepo.tabhost.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.Logger;
import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

abstract class TabHostBaseFragment extends Fragment implements TitleListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = (TextView)inflater.inflate(R.layout.tab_host_fragment_layout, container, false);
        tv.setText("Fragment : " + getFragmentId());
        tv.setBackgroundColor(getFragmentColor());
        return tv;
    }

    @Override
    public void onFocus() {
        Logger.log("Fragment " + getFragmentId() + " onFocus");
    }

    @Override
    public void onBlur() {
        Logger.log("Fragment " + getFragmentId() + " onBlur");
    }

    @Override
    public void onUpdateActionBar(CustomActionBar customContainer) {
        Logger.log("Fragment " + getFragmentId() + " onUpdateActionBar");
        customContainer.resetCustomContainer();
    }

    abstract protected int getFragmentId();
    abstract protected int getFragmentColor();
}
