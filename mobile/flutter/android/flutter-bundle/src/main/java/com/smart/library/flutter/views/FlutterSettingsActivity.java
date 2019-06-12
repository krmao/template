package com.smart.library.flutter.views;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.smart.library.flutter.R;
import com.smart.library.flutter.utils.FlutterRouter;
import com.taobao.idlefish.flutterboost.containers.BoostFlutterFragment;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformPlugin;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class FlutterSettingsActivity extends AppCompatActivity {

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

        setContentView(R.layout.flutter_settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_stub, FlutterSettingsFragment.instance("hello"))
                .commit();
    }


    public static class FlutterSettingsFragment extends BoostFlutterFragment {

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
            return FlutterRouter.URL_SETTINGS;
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

        public static FlutterSettingsFragment instance(String tag) {
            FlutterSettingsFragment fragment = new FlutterSettingsFragment();
            fragment.setTabTag(tag);
            return fragment;
        }
    }

}
