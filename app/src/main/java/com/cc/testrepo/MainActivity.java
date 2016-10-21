package com.cc.testrepo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cc.testrepo.expandgridview.EqualDivisionGridTestActivity;
import com.cc.testrepo.expandgridview.ExpandViewTestActivity;
import com.cc.testrepo.ime.IMETestActivity;
import com.cc.testrepo.multitouch.MultiTouchTestActivity;
import com.cc.testrepo.nestedscroll.NestedScrollTestActivity;
import com.cc.testrepo.overscroll.OverScrollTestActivity;

public class MainActivity extends AppCompatActivity {

    private static final Class TEST_CLASS = EqualDivisionGridTestActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TEST_CLASS != null) {
            Intent intent = new Intent(this, TEST_CLASS);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
    }

    public void performClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.overscroll:
                intent = new Intent(this, OverScrollTestActivity.class);
                startActivity(intent);
                break;
            case R.id.nestedcroll:
                intent = new Intent(this, NestedScrollTestActivity.class);
                startActivity(intent);
                break;
            case R.id.multitouch:
                intent = new Intent(this, MultiTouchTestActivity.class);
                startActivity(intent);
                break;
            case R.id.ime:
                intent = new Intent(this, IMETestActivity.class);
                startActivity(intent);
                break;
            case R.id.expand_grid_view:
                intent = new Intent(this, ExpandViewTestActivity.class);
                startActivity(intent);
                break;
            case R.id.equal_division_grid:
                intent = new Intent(this, EqualDivisionGridTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
