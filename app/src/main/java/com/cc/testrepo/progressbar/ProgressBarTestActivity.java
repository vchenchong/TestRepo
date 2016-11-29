package com.cc.testrepo.progressbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

import java.util.Random;

/**
 * Created by chenchong on 16/11/28.
 */

public class ProgressBarTestActivity extends BaseActivity {

    private HorizontalProgressBar mProgressBar;

    private Button mLeft, mRight;
    private TextView mBoard;

    private Random mRand = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_progress_bar_test_layout);
        mProgressBar = (HorizontalProgressBar)findViewById(R.id.progress_bar);
        mLeft = (Button)findViewById(R.id.leftBtn);
        mRight = (Button)findViewById(R.id.rightBtn);
        mBoard = (TextView)findViewById(R.id.val);

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int decreaseVal = mRand.nextInt(10);
                mBoard.setText(String.valueOf(-decreaseVal));
                mProgressBar.setProgress(mProgressBar.getProgress() - decreaseVal);
            }
        });

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int increaseVal = mRand.nextInt(10);
                mBoard.setText(String.valueOf(increaseVal));
                mProgressBar.setProgress(mProgressBar.getProgress() + increaseVal);
            }
        });
    }
}
