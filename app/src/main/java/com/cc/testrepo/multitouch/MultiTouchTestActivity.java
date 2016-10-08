package com.cc.testrepo.multitouch;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

public class MultiTouchTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_touch_view_layout);
    }
}
