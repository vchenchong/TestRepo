package com.cc.testrepo.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.Logger;

/**
 * Created by chenchong on 16/10/21.
 */

public class ToastDialogFragment extends DialogFragment {

    private static final String ARGS_KEY_DURATION = "duration";

    private static final int DEFAULT_DURATION = 1000;

    private Handler mHandler;

    private int mDuration = DEFAULT_DURATION;

    public static ToastDialogFragment newInstance(int duration) {
        ToastDialogFragment fragment = new ToastDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_KEY_DURATION, duration);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ToastDialogFragment() {
        super();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.log("onCreate");
        if (savedInstanceState != null) {
            mDuration = savedInstanceState.getInt(ARGS_KEY_DURATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.log("onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        TextView tv = (TextView) inflater.inflate(R.layout.toast_dialog_layout, container, false);
        tv.setText("格式不正确");
        return tv;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.log("onDestroy");
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        autoDismiss(mDuration);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int result = super.show(transaction, tag);
        autoDismiss(mDuration);
        return result;
    }

    private void autoDismiss(int delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissAllowingStateLoss();
            }
        }, delay);
    }
}
