package com.idlefish.flutterboost.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.containers.FlutterBoostActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivityLaunchConfigs;
import com.codesdancing.flutterexample.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static WeakReference<MainActivity> sRef;

    private TextView mOpenNative;
    private TextView mOpenFlutter;
    private TextView mOpenFlutterFragment;
    private TextView mOpenCustomViewTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.native_page);

        mOpenNative = findViewById(R.id.open_native);
        mOpenFlutter = findViewById(R.id.open_flutter);
        mOpenFlutterFragment = findViewById(R.id.open_flutter_fragment);
        mOpenCustomViewTab = findViewById(R.id.open_custom_view_tab);

        mOpenNative.setOnClickListener(this);
        mOpenFlutter.setOnClickListener(this);
        mOpenFlutterFragment.setOnClickListener(this);
        mOpenCustomViewTab.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> params = new HashMap<>();
        params.put("test1","v_test1");
        params.put("test2","v_test2");
        //Add some params if needed.
        if (v == mOpenNative) {
            NativeRouter.openPageByUrl(this, NativeRouter.NATIVE_PAGE_URL , params);
        } else if (v == mOpenFlutter) {
            Intent intent = new FlutterBoostActivity.CachedEngineIntentBuilder(FlutterBoostActivity.class, FlutterBoost.ENGINE_ID)
                    .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                    .destroyEngineWithActivity(false)
                    .url("/")
                    .urlParams(params)
                    .build(this);
            startActivity(intent);
        } else if (v == mOpenFlutterFragment) {
            NativeRouter.openPageByUrl(this, NativeRouter.FLUTTER_FRAGMENT_PAGE_URL,params);
        } else if (v == mOpenCustomViewTab) {
            NativeRouter.openPageByUrl(this, NativeRouter.FLUTTER_CUSTOM_VIEW_URL, params);
        }
    }
}
