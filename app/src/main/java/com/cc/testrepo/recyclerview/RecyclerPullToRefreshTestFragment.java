package com.cc.testrepo.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.base.BaseFragment;
import com.cc.testrepo.recyclerview.adapter.SimpleAdapter;
import com.cc.testrepo.recyclerview.model.SimpleStringModel;
import com.cc.testrepo.recyclerview.view.PullToRefreshRecyclerView;
import com.cc.testrepo.recyclerview.view.PullToRefreshRecyclerViewHeader;

import java.util.List;

public class RecyclerPullToRefreshTestFragment extends BaseFragment {

    private PullToRefreshRecyclerView mRecyclerView;
    private List<SimpleStringModel> mModelList;
    private SimpleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new PullToRefreshRecyclerView(getContext());
        mModelList = new SimpleStringModel.SimpleStringModelProvider().get(5);
        mAdapter = new SimpleAdapter(mModelList);
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Log.d("chen", "AdapterPosition : " + vh.getAdapterPosition());
                Log.d("chen", "LayoutPosition : " + vh.getLayoutPosition());
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.enablePullToRefresh(true);
        mRecyclerView.setRefreshHeader(new PullToRefreshRecyclerViewHeader(getContext()));
        mRecyclerView.setOnRefreshListener(new PullToRefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("chen", "onRefresh");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mModelList.addAll(new SimpleStringModel.SimpleStringModelProvider().get(5));
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.stopRefresh();
                    }
                }, 1500);
            }
        });

        return mRecyclerView;
    }
}
