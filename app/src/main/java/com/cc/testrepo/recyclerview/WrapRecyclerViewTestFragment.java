package com.cc.testrepo.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseFragment;
import com.cc.testrepo.recyclerview.adapter.SimpleAdapter;
import com.cc.testrepo.recyclerview.model.SimpleStringModel;
import com.cc.testrepo.recyclerview.view.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WrapRecyclerViewTestFragment extends BaseFragment {

    private WrapRecyclerView mRecyclerView;

    private List<View> mHeaderViewList = new ArrayList<>();
    private List<View> mFooterViewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wrap_recycler_view_test_fragment_layout, container, false);

        mRecyclerView = (WrapRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//        mRecyclerView.setAdapter(new SimpleAdapter(new SimpleStringModel.SimpleStringModelProvider().get(0)));

        view.findViewById(R.id.add_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = new TextView(v.getContext());
                tv.setText("Header : " + mHeaderViewList.size());
                mHeaderViewList.add(tv);
                mRecyclerView.addHeaderView(tv);
            }
        });

        view.findViewById(R.id.remove_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHeaderViewList.isEmpty()) {
                    View lastHeaderView = mHeaderViewList.remove(mHeaderViewList.size() - 1);
                    mRecyclerView.removeHeaderView(lastHeaderView);
                }
            }
        });

        view.findViewById(R.id.add_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = new TextView(getContext());
                tv.setText("Footer : " + mFooterViewList.size());
                mFooterViewList.add(tv);
                mRecyclerView.addFooterView(tv);
            }
        });

        view.findViewById(R.id.remove_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFooterViewList.isEmpty()) {
                    View lastFooterView = mFooterViewList.remove(mFooterViewList.size() - 1);
                    mRecyclerView.removeFooterView(lastFooterView);
                }
            }
        });

        view.findViewById(R.id.adapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setAdapter(new SimpleAdapter(new SimpleStringModel.SimpleStringModelProvider().get(20)));
            }
        });

        return view;
    }
}
