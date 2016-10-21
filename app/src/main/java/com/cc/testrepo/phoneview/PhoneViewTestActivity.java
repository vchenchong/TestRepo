package com.cc.testrepo.phoneview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

/**
 * Created by chenchong on 16/10/21.
 */

public class PhoneViewTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_view_layout);
    }
}
