package com.smart.library.flutter.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smart.library.flutter.utils.FlutterRouter;
import com.smart.library.flutter.R;

import java.lang.ref.WeakReference;

public class NativeMainActivity extends AppCompatActivity {

    public static WeakReference<NativeMainActivity> sRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.native_main_activity);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }
}
