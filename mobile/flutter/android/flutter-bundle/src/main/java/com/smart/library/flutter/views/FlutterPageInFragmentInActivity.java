package com.smart.library.flutter.views;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.smart.library.flutter.R;
import com.taobao.idlefish.flutterboost.containers.BoostFlutterFragment;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformPlugin;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class FlutterPageInFragmentInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x40000000);
            window.getDecorView().setSystemUiVisibility(PlatformPlugin.DEFAULT_SYSTEM_UI);
        }
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.flutter_page_in_fragment_in_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_stub, FlutterPageInFragment.instance("hello"))
                .commit();
    }


    public static class FlutterPageInFragment extends BoostFlutterFragment {

        @Override
        public void setArguments(@Nullable Bundle args) {
            if (args == null) {
                args = new Bundle();
                args.putString("tag", "");
            }
            super.setArguments(args);
        }

        public void setTabTag(String tag) {
            Bundle args = new Bundle();
            args.putString("tag", tag);
            super.setArguments(args);
        }

        @Override
        public void onRegisterPlugins(PluginRegistry registry) {
            GeneratedPluginRegistrant.registerWith(registry);
        }

        @Override
        public String getContainerName() {
            return "FlutterPageInFragmentInActivity";
        }

        @Override
        public Map getContainerParams() {
            Map<String, String> params = new HashMap<>();
            params.put("tag", getArguments().getString("tag"));
            return params;
        }

        @Override
        public void destroyContainer() {

        }

        public static FlutterPageInFragment instance(String tag) {
            FlutterPageInFragment fragment = new FlutterPageInFragment();
            fragment.setTabTag(tag);
            return fragment;
        }
    }

}
