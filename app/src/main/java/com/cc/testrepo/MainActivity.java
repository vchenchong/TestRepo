package com.cc.testrepo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cc.testrepo.overscroll.OverScrollTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.overscroll:
                Intent intent = new Intent(this, OverScrollTestActivity.class);
                startActivity(intent);
        }
    }
}
