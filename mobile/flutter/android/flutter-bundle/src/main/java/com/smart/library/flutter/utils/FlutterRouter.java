package com.smart.library.flutter.utils;

import android.content.Context;
import android.content.Intent;

import com.smart.library.flutter.views.FlutterOrderActivity;
import com.smart.library.flutter.views.FlutterSettingsActivity;
import com.smart.library.flutter.views.NativeMineActivity;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class FlutterRouter {
    public static final String URL_MINE = "flutter://native/mine";
    public static final String URL_ORDER = "flutter://flutter/order";
    public static final String URL_SETTINGS = "flutter://flutter/settings";

    public static boolean openPageByUrl(Context context, String url) {
        return openPageByUrl(context, url, 0);
    }

    public static boolean openPageByUrl(Context context, String url, int requestCode) {
        try {
            if (url.startsWith(URL_ORDER)) {
                context.startActivity(new Intent(context, FlutterOrderActivity.class));
                return true;
            } else if (url.startsWith(URL_SETTINGS)) {
                context.startActivity(new Intent(context, FlutterSettingsActivity.class));
                return true;
            } else if (url.startsWith(URL_MINE)) {
                context.startActivity(new Intent(context, NativeMineActivity.class));
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            return false;
        }
    }
}
