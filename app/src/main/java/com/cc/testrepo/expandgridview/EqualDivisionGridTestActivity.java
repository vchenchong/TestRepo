package com.cc.testrepo.expandgridview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

/**
 * Created by chenchong on 16/10/20.
 */

public class EqualDivisionGridTestActivity extends BaseActivity {

    private static final int CHILD_COUNT = 5;

    private EqualDivisionGridLayout mGrid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.euqual_division_grid_layout);
        mGrid = (EqualDivisionGridLayout)findViewById(R.id.grid);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i=0; i<CHILD_COUNT; ++i) {
            TextView tv = (TextView)inflater.inflate(R.layout.expand_grid_layout_item_layout, mGrid, false);
            tv.setText(i + " : " + i);
            mGrid.addView(tv);
        }
    }
}
