package com.yunmin.demo2.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.yunmin.demo2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResources().getDrawable(R.mipmap.ic_launcher);
        new WindowManager.LayoutParams(100,200);
    }
}
