package com.smart.library.flutter.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smart.library.flutter.utils.FlutterRouter;
import com.smart.library.flutter.R;

public class NativeMineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.native_mine_activity);

        findViewById(R.id.openMine).setOnClickListener(v -> {
            FlutterRouter.openPageByUrl(this, FlutterRouter.URL_MINE);
        });

        findViewById(R.id.openOrder).setOnClickListener(v -> {
            FlutterRouter.openPageByUrl(this, FlutterRouter.URL_ORDER);
        });

        findViewById(R.id.openSettings).setOnClickListener(v -> {
            FlutterRouter.openPageByUrl(this, FlutterRouter.URL_SETTINGS);
        });
    }
}