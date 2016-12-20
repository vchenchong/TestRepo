package com.cc.testrepo.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.testrepo.R;
import com.cc.testrepo.recyclerview.model.SimpleStringModel;

import java.util.Collections;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private List<SimpleStringModel> mData;

    private OnItemClickListener mOnItemClickListener;

    public SimpleAdapter(@NonNull List<SimpleStringModel> data) {
        mData = data;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_view_holder_layout, parent, false);
        itemView.setBackgroundColor(0xbbabcdef);
        ((TextView) itemView.findViewById(R.id.type)).setText("Type : " + viewType);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.position)).setText(mData.get(position).content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 如果这里的viewType是仅跟position关联的(与数据无关),那ItemTouchHelper的拖拽可能会不生效:
        //  如果位置i的viewType和位置i+1的viewType不同,当位置为i的被拖拽元素被拖到位置i+1上时,拖拽会马上终止
        return mData.get(position).viewType;
    }

    public void insert(int position, String val) {
        if (position >= mData.size()) {
            mData.add(new SimpleStringModel(val, 0));
            notifyItemInserted(mData.size() - 1);
        } else if (position < 0) {
            mData.add(0, new SimpleStringModel(val, 0));
            notifyItemInserted(0);
        } else {
            mData.add(position, new SimpleStringModel(val, 0));
            notifyItemInserted(position);
        }
    }

    public void delete(int position) {
        if (position >= mData.size()) {
            mData.remove(mData.size() - 1);
            notifyItemRemoved(mData.size());
        } else if (position < 0) {
            mData.remove(0);
            notifyItemRemoved(0);
        } else {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void move(int from, int to) {
        if (from == RecyclerView.NO_POSITION || to == RecyclerView.NO_POSITION) {
            return;
        }

        if (from < to) {
            for (int i=from; i<to; ++i) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i=from; i>to; --i) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
    }

    public void remove(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder vh);
    }
}
