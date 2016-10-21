package com.cc.testrepo.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

public class DialogTestActivity extends BaseActivity {

    private ToastDialogFragment mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_test);

        mToast = ToastDialogFragment.newInstance(2000);
    }

    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.toast_dialog_fragment:
                mToast.show(getFragmentManager(), "ToastDialog");
                break;
        }
    }
}
