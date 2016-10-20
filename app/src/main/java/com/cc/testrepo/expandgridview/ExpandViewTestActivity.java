package com.cc.testrepo.expandgridview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;
import com.cc.testrepo.base.Logger;

public class ExpandViewTestActivity extends BaseActivity {

    private ExpandGridLayout mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_grid_layout_layout);
        mGridView = (ExpandGridLayout)findViewById(R.id.expand_grid_view);

        mGridView.setAdapter(new ExpandGridLayout.ExpandGridViewAdapter() {
            @Override
            public int getCount() {
                return 14;
            }

            @NonNull
            @Override
            public View getView(LayoutInflater inflater, int position, ViewGroup parent) {
                TextView tv = (TextView)inflater.inflate(R.layout.expand_grid_layout_item_layout, parent, false);
                tv.setText(position + " : " + position);
                return tv;
            }
        });

        mGridView.setOnItemClickListener(new ExpandGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logger.log("Click : " + position);
            }
        });

        Button btn = (Button)findViewById(R.id.btn);
        btn.setText("Expand");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpanded = mGridView.isExpanded();
                mGridView.setExpandState(!isExpanded, true);
                ((Button)v).setText(isExpanded ? "Expand" : "Hide");
            }
        });
    }
}
