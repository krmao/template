package com.smart.library.util.swipeback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.smart.library.R;

/**
 * https://github.com/wya-team/wya-android-kit/blob/a3082d47ce295c555c008951fcf328039d9a2fa4/wyauikit/uikit/src/main/java/com/wya/uikit/toolbar/swipeback/SwipeBackLayout.java
 */
public class STSwipeBackActivityHelper {
    private final Activity mActivity;

    private STSwipeBackLayout mSwipeBackLayout;

    public STSwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSwipeBackLayout = (STSwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.st_swipeback_layout, null);
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public STSwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
