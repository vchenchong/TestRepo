package com.cc.testrepo.base;

import android.util.Log;

public class Logger {

    private static final String TAG = "chen";

    public static void log(String text) {
        Log.d(TAG, text);
    }
}
