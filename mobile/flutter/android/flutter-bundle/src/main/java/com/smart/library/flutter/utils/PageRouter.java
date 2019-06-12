package com.smart.library.flutter.utils;

import android.content.Context;
import android.content.Intent;

import com.smart.library.flutter.views.FlutterPageInActivity;
import com.smart.library.flutter.views.FlutterPageInFragmentInActivity;
import com.smart.library.flutter.views.NativeMineActivity;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class PageRouter {

    public static final String NATIVE_PAGE_URL = "sample://nativePage";
    public static final String FLUTTER_PAGE_URL = "sample://flutterPage";
    public static final String FLUTTER_FRAGMENT_PAGE_URL = "sample://flutterFragmentPage";

    public static boolean openPageByUrl(Context context, String url) {
        return openPageByUrl(context, url, 0);
    }

    public static boolean openPageByUrl(Context context, String url, int requestCode) {
        try {
            if (url.startsWith(FLUTTER_PAGE_URL)) {
                context.startActivity(new Intent(context, FlutterPageInActivity.class));
                return true;
            } else if (url.startsWith(FLUTTER_FRAGMENT_PAGE_URL)) {
                context.startActivity(new Intent(context, FlutterPageInFragmentInActivity.class));
                return true;
            } else if (url.startsWith(NATIVE_PAGE_URL)) {
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
