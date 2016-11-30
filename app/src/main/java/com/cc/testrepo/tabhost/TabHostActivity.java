package com.cc.testrepo.tabhost;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;
import com.cc.testrepo.tabhost.fragment.TabHostFragmentFactory;
import com.cc.testrepo.tabhost.fragment.TitleListener;
import com.cc.testrepo.tabhost.view.BottomTabGroup;
import com.cc.testrepo.tabhost.view.CustomActionBar;

import java.util.ArrayList;
import java.util.List;


public class TabHostActivity extends BaseActivity implements BottomTabGroup.OnTagSelectListener {

    private CustomActionBar mActionBar;
    private FrameLayout mContent;
    private BottomTabGroup mBottomTagGroup;

    private TabHostFragmentFactory mFragmentFactory;

    private int mSelectedBottomTag = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host_activity_layout);
        mFragmentFactory = new TabHostFragmentFactory();
        initView();
    }

    private void initView() {
        mActionBar = (CustomActionBar)findViewById(R.id.custom_action_bar);
        mContent = (FrameLayout)findViewById(R.id.content);
        mBottomTagGroup = (BottomTabGroup)findViewById(R.id.bottomTagGroup);

        mActionBar.setOnBackPressedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<Drawable> drawableList = new ArrayList<>(4);
        drawableList.add(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        drawableList.add(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        drawableList.add(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        drawableList.add(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        List<String> textList = new ArrayList<>(4);
        textList.add("首页");
        textList.add("品质优惠");
        textList.add("发现");
        textList.add("我的");
        mBottomTagGroup.setTag(drawableList, textList);
        mBottomTagGroup.setOnTagSelectListener(this);

    }

    @Override
    public void onTagSelect(View view, int position) {
        if (isFinishing()) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentByTag(mFragmentFactory.getFragmentTag(mSelectedBottomTag));
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
            if (currentFragment instanceof TitleListener) {
                TitleListener l = (TitleListener)currentFragment;
                l.onBlur();
            }
        }
        String newFragmentTag = mFragmentFactory.getFragmentTag(position);
        Fragment newFragment = fm.findFragmentByTag(newFragmentTag);
        if (newFragment == null) {
            newFragment = mFragmentFactory.createFragment(position);
            ft.add(R.id.content, newFragment, newFragmentTag);
        } else {
            ft.show(newFragment);
        }
        if (newFragment instanceof TitleListener) {
            TitleListener l = (TitleListener)newFragment;
            l.onFocus();
            l.onUpdateActionBar(mActionBar);
        }

        mSelectedBottomTag = position;
        ft.commitAllowingStateLoss();
    }

}
