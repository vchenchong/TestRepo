package com.cc.testrepo.recyclerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.cc.testrepo.R;
import com.cc.testrepo.base.BaseActivity;


public class RecyclerViewTestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_test_activity_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycler_view_menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_touch_helper:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("ItemTouchHelper");
                if (fragment == null) {
                    fragment = new RecyclerItemTouchHelperTestFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_view, fragment, "ItemTouchHelper")
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                return true;
            case R.id.wrap:
                fragment = getSupportFragmentManager().findFragmentByTag("Wrap");
                if (fragment == null) {
                    fragment = new WrapRecyclerViewTestFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_view, fragment, "Wrap")
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
