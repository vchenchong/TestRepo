package com.cc.testrepo.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseFragment;
import com.cc.testrepo.base.Logger;
import com.cc.testrepo.recyclerview.adapter.SimpleAdapter;
import com.cc.testrepo.recyclerview.itemdecoration.SimpleLinearDecoration;
import com.cc.testrepo.recyclerview.model.SimpleStringModel;

/**
 * Created by chenchong on 16/12/28.
 */

public class RecyclerItemTouchHelperTestFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private SimpleAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());

        mAdapter = new SimpleAdapter(new SimpleStringModel.SimpleStringModelProvider().get());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.addItemDecoration(new SimpleLinearDecoration(30, new ColorDrawable(Color.parseColor("#abcdef"))));

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(null);



        initClickListener();
        initTouchHelper();

        return mRecyclerView;
    }

    private void initClickListener() {
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Logger.log("VH : " + vh.getLayoutPosition());
            }
        });
    }

    private void initTouchHelper() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

//                if (viewHolder.getItemViewType() != target.getItemViewType()) {
//                    return false;
//                }

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                mAdapter.move(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.remove(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                // 拖拽元素增加边界限制:不允许拖出RecyclerView
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    if (dY < 0) {
                        // 向上拖动
                        dY = Math.max(dY, -(viewHolder.itemView.getTop() - mRecyclerView.getPaddingTop()));
                    } else {
                        // 向下拖动
                        dY = Math.min(dY, recyclerView.getHeight() - recyclerView.getPaddingBottom() - viewHolder.itemView.getBottom());
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // 侧滑删除的alpha效果
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    float progress = Math.abs(dX) / viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(1f - progress);
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);

                // 拖拽元素的放大效果
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder.itemView.setScaleY(1.5f);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setScaleY(1f);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recycler_view_item_touch_helper_menu_layout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                mAdapter.insert(mAdapter.getItemCount() / 2, String.valueOf(mAdapter.getItemCount()));
                return true;
            case R.id.action_delete:
                mAdapter.delete(mAdapter.getItemCount() / 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
