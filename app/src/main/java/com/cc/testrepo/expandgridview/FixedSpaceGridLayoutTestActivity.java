package com.cc.testrepo.expandgridview;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;
import com.cc.testrepo.base.Logger;

public class FixedSpaceGridLayoutTestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_grid_layout);

        FixedSpaceGridLayout gridLayout = (FixedSpaceGridLayout)findViewById(R.id.grid);

        gridLayout.setOnGridItemClickListener(new FixedSpaceGridLayout.OnGridItemClickListener() {
            @Override
            public void onGridItemClick(View view, int position) {
                Logger.log("select : " + position);
            }
        });

        for (int i=0; i<100; ++i) {
            TextView tv = new TextView(this);
            tv.setText("Position : " + i);
            gridLayout.addView(tv);
        }

        gridLayout.selectItem(20);



    }
}
