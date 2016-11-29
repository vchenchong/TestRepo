package com.cc.testrepo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cc.testrepo.dialog.DialogTestActivity;
import com.cc.testrepo.expandgridview.EqualDivisionGridTestActivity;
import com.cc.testrepo.expandgridview.ExpandViewTestActivity;
import com.cc.testrepo.ime.IMETestActivity;
import com.cc.testrepo.multitouch.MultiTouchTestActivity;
import com.cc.testrepo.nestedscroll.NestedScrollTestActivity;
import com.cc.testrepo.overscroll.OverScrollTestActivity;
import com.cc.testrepo.phoneview.PhoneViewTestActivity;
import com.cc.testrepo.progressbar.ProgressBarTestActivity;
import com.cc.testrepo.tabhost.TabHostActivity;

public class MainActivity extends AppCompatActivity {

    private static final Class TEST_CLASS = ProgressBarTestActivity.class;

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
            case R.id.phone_view:
                intent = new Intent(this, PhoneViewTestActivity.class);
                startActivity(intent);
                break;
            case R.id.dialog:
                intent = new Intent(this, DialogTestActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_host:
                intent = new Intent(this, TabHostActivity.class);
                startActivity(intent);
                break;
            case R.id.progress_bar:
                intent = new Intent(this, ProgressBarTestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
