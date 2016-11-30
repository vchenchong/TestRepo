package com.cc.testrepo.activity.transparent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cc.testrepo.R;
import com.cc.testrepo.base.Logger;

public class TransparentTestActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity_layout);
        Logger.log("Transparent");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
