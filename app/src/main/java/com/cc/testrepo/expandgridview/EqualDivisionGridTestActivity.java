package com.cc.testrepo.expandgridview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;

import java.util.Random;

public class EqualDivisionGridTestActivity extends BaseActivity {

    private static final int CHILD_COUNT = 50;

    private EqualDivisionGridLayout mGrid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.euqual_division_grid_layout);
        mGrid = (EqualDivisionGridLayout)findViewById(R.id.grid);

        Random rand = new Random();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i=0; i<CHILD_COUNT; ++i) {
            TextView tv = (TextView)inflater.inflate(R.layout.expand_grid_layout_item_layout, mGrid, false);
            tv.setText(i + " : " + i);
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.width = rand.nextInt(50) + 150;
            lp.width = -2;
            lp.height = rand.nextInt(50) + 75;
            lp.height = -2;
            mGrid.addView(tv);
//            int r = rand.nextInt(10);
//            if (r == 0) {
//                tv.setVisibility(View.GONE);
//            } else if (r == 1) {
//                tv.setVisibility(View.INVISIBLE);
//            }

            mGrid.selectItem(10);

            mGrid.setOnGridItemClickListener(new EqualDivisionGridLayout.OnGridItemClickListener() {
                @Override
                public void onGridItemClick(View view, int position) {
                    Log.d("chen", "Position : " + position);
                }
            });
        }
    }
}
