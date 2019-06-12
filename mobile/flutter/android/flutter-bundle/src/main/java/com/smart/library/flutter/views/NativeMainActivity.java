package com.smart.library.flutter.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smart.library.flutter.utils.PageRouter;
import com.smart.library.flutter.R;

import java.lang.ref.WeakReference;

public class NativeMainActivity extends AppCompatActivity {

    public static WeakReference<NativeMainActivity> sRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.flutter_native_activity_main);


        findViewById(R.id.native_open_native).setOnClickListener(v -> {
            PageRouter.openPageByUrl(this, PageRouter.NATIVE_PAGE_URL);
        });

        findViewById(R.id.native_open_flutter).setOnClickListener(v -> {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL);
        });

        findViewById(R.id.native_open_flutter_fragment).setOnClickListener(v -> {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_FRAGMENT_PAGE_URL);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }
}
