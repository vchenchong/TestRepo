package com.cc.testrepo.tabhost.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;
import com.cc.testrepo.tabhost.view.BadgeViewGroup;
import com.cc.testrepo.tabhost.view.CustomActionBar;

/**
 * Created by chenchong on 16/11/30.
 */

public class Fragment1 extends TabHostBaseFragment implements View.OnClickListener {

    private BadgeViewGroup mLeft, mRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_host_first_fragment_layout, container, false);
    }

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
        if (customContainer == null) {
            return;
        }

        Context context = customContainer.getContext();
        View customActionBar = LayoutInflater.from(context).inflate(R.layout.custom_action_bar_layout,
                customContainer.getCustomContainer(), false);
        mLeft = (BadgeViewGroup)customActionBar.findViewById(R.id.custom_action_bar_left_btn);
        mRight = (BadgeViewGroup)customActionBar.findViewById(R.id.custom_action_bar_right_btn);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        customContainer.getCustomContainer().addView(customActionBar);
    }

    @Override
    public void onClick(View v) {
        if (v == mLeft) {
            if (v.isSelected()) {
                return;
            }
            mLeft.setSelected(true);
            mRight.setSelected(false);
            handleClick(true);
        } else if (v == mRight) {
            if (v.isSelected()) {
                return;
            }
            mLeft.setSelected(false);
            mRight.setSelected(true);
            handleClick(false);
        }
    }

    private void handleClick(boolean isLeft) {
        String currentTag = isLeft ? "left" : "right";
        String newTag = isLeft ? "right" : "left";
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentByTag(currentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment newFragment = fm.findFragmentByTag(newTag);
        if (newFragment == null) {
            newFragment = isLeft ? (new InnerFragment1()) : (new InnerFragment2());
            ft.add(R.id.tab_host_first_fragment_container, newFragment, newTag);
        } else {
            ft.show(newFragment);
        }
        ft.commitAllowingStateLoss();
    }

    public static class InnerFragment1 extends TabHostBaseFragment {
        @Override
        protected int getFragmentId() {
            return 11;
        }

        @Override
        protected int getFragmentColor() {
            return Color.DKGRAY;
        }
    }

    public static class InnerFragment2 extends TabHostBaseFragment {
        @Override
        protected int getFragmentId() {
            return 12;
        }

        @Override
        protected int getFragmentColor() {
            return Color.LTGRAY;
        }
    }
}
