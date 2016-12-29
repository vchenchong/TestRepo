package com.cc.testrepo.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WrapRecyclerView extends RecyclerView {

    private WrapAdapter mAdapter;
    private List<View> mHeaderViewList = new ArrayList<>();
    private List<View> mFooterViewList = new ArrayList<>();

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void addHeaderView(View headerView) {
        mHeaderViewList.add(headerView);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View headerView) {
        if (mHeaderViewList.remove(headerView)) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addFooterView(View footerView) {
        mFooterViewList.add(footerView);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View footerView) {
        if (mFooterViewList.remove(footerView)) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = new WrapAdapter(adapter, mHeaderViewList, mFooterViewList);
        super.setAdapter(mAdapter);
    }

    private static class WrapAdapter<VH extends ViewHolder> extends RecyclerView.Adapter {

        private Adapter mInnerAdapter;

        private List<View> mHeaderViewList;
        private List<View> mFooterViewList;

        WrapAdapter(Adapter adapter, List<View> headerViewList, List<View> footerViewList) {
            mInnerAdapter = adapter;
            mHeaderViewList = headerViewList;
            mFooterViewList = footerViewList;
        }

        @Override
        public int getItemCount() {
            return getHeaderViewCount() + getContentItemCount() + getFooterViewCount();
        }

        private int getContentItemCount() {
            return mInnerAdapter != null ? mInnerAdapter.getItemCount() : 0;
        }

        private int getHeaderViewCount() {
            return mHeaderViewList == null ? 0 : mHeaderViewList.size();
        }

        private int getFooterViewCount() {
            return mFooterViewList == null ? 0 : mFooterViewList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType > getHeaderViewTypeLowerBound()) {
                return new HeaderFooterViewHolder(mHeaderViewList.get(Integer.MAX_VALUE - viewType));
            } else if (viewType < getFooterViewTypeUpperBound()) {
                return new HeaderFooterViewHolder(mFooterViewList.get(viewType - Integer.MIN_VALUE));
            } else {
                return mInnerAdapter != null ? mInnerAdapter.onCreateViewHolder(parent, viewType) : null;
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int headerViewCount = getHeaderViewCount();
            if (position < headerViewCount) {
                return;
            }
            int contentItemCount = getItemCount();
            if (position < headerViewCount + contentItemCount) {
                if (mInnerAdapter != null) {
                    mInnerAdapter.onBindViewHolder(holder, position - headerViewCount);
                }
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List payloads) {
            int headerViewCount = getHeaderViewCount();
            if (position < headerViewCount) {
                return;
            }
            int contentItemCount = getContentItemCount();
            if (position < headerViewCount + contentItemCount) {
                if (mInnerAdapter != null) {
                    mInnerAdapter.onBindViewHolder(holder, position - headerViewCount, payloads);
                }
            }
        }

        // HeaderView的ViewType从Integer.MAX_VALUE开始,不断减小
        // FooterView的ViewType从Integer.MIN_VALUE开始,不断增加

        @Override
        public int getItemViewType(int position) {
            if (isHeaderViewType(position)) {
                return Integer.MAX_VALUE - position;
            } else if (isFooterViewType(position)) {
                return Integer.MIN_VALUE + position - getHeaderViewCount() - getContentItemCount();
            } else {
                return mInnerAdapter != null ? mInnerAdapter.getItemViewType(position - getHeaderViewCount()) : super.getItemViewType(position);
            }
        }

        private boolean isHeaderViewType(int position) {
            return position < getHeaderViewCount() && position >= 0;
        }

        private boolean isFooterViewType(int position) {
            int headerAndContentViewCount = getHeaderViewCount() + getContentItemCount();
            return position >= headerAndContentViewCount && position < headerAndContentViewCount + getFooterViewCount();
        }

        /**
         * ViewType大于返回值的viewType对应的是HeaderView
         * @return 所有HeaderView的ViewType的下界
         */
        private int getHeaderViewTypeLowerBound() {
            return Integer.MAX_VALUE - getHeaderViewCount();
        }

        /**
         * ViewType小于返回值的viewType对应的是FooterView
         * @return 所有FooterView的ViewType的上界
         */
        private int getFooterViewTypeUpperBound() {
            return Integer.MIN_VALUE + getFooterViewCount();
        }

        // 将相关方法委托给mInnerAdapter

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            if (mInnerAdapter != null) {
                mInnerAdapter.onAttachedToRecyclerView(recyclerView);
            }
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            if (mInnerAdapter != null) {
                mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
            }
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            if (mInnerAdapter != null && ! (holder instanceof HeaderFooterViewHolder)) {
                mInnerAdapter.onViewAttachedToWindow(holder);
            }
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            if (mInnerAdapter != null && ! (holder instanceof HeaderFooterViewHolder)) {
                mInnerAdapter.onViewDetachedFromWindow(holder);
            }
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            if (mInnerAdapter != null) {
                mInnerAdapter.setHasStableIds(hasStableIds);
            }
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            if (mInnerAdapter != null && !(holder instanceof HeaderFooterViewHolder)) {
                mInnerAdapter.onViewRecycled(holder);
            }
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            if (mInnerAdapter != null && ! (holder instanceof HeaderFooterViewHolder)) {
                return mInnerAdapter.onFailedToRecycleView(holder);
            }
            return super.onFailedToRecycleView(holder);
        }

        @Override
        public long getItemId(int position) {
            int headerViewCount = getHeaderViewCount();
            if (position < headerViewCount) {
                return Integer.MAX_VALUE;
            }
            int contentItemCount = getContentItemCount();
            if (position < headerViewCount + contentItemCount) {
                return mInnerAdapter != null ? mInnerAdapter.getItemId(position - headerViewCount) : super.getItemId(position);
            }
            int footerViewCount = getFooterViewCount();
            if (position < headerViewCount + contentItemCount + footerViewCount) {
                return Integer.MIN_VALUE;
            }
            return super.getItemId(position);
        }

        static class HeaderFooterViewHolder extends ViewHolder {
            HeaderFooterViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
