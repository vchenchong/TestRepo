package com.cc.testrepo.base;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;


public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.SimpleViewHolder> {

    private int mItemCount;

    public SimpleRecyclerViewAdapter() {
        this(100);
    }

    public SimpleRecyclerViewAdapter(int itemCount) {
        super();
        mItemCount = itemCount;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new RecyclerView.LayoutParams(-1, 300));
        return new SimpleViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.mTextView.setText("Position : " + position);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        SimpleViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }
}
