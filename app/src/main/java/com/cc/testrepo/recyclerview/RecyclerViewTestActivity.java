package com.cc.testrepo.recyclerview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;
import com.cc.testrepo.base.Logger;
import com.cc.testrepo.recyclerview.adapter.SimpleAdapter;
import com.cc.testrepo.recyclerview.itemdecoration.SimpleLinearDecoration;
import com.cc.testrepo.recyclerview.model.SimpleStringModel;


public class RecyclerViewTestActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private SimpleAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = new RecyclerView(this);

        mAdapter = new SimpleAdapter(new SimpleStringModel.SimpleStringModelProvider().get());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new SimpleLinearDecoration(30, new ColorDrawable(Color.parseColor("#abcdef"))));

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(null);

        setContentView(mRecyclerView);

        initClickListener();
        initTouchHelper();
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
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycler_view_menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
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
