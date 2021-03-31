package com.smart.library.util.swipeback;

import android.app.Activity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * https://github.com/wya-team/wya-android-kit/blob/a3082d47ce295c555c008951fcf328039d9a2fa4/wyauikit/uikit/src/main/java/com/wya/uikit/toolbar/swipeback/SwipeBackLayout.java
 */
@Keep
public class STSwipeBackListenerAdapter implements STSwipeBackLayout.SwipeListenerEx {
    private final WeakReference<Activity> mActivity;
    private final STSwipeBackLayout mLayout;

    public STSwipeBackListenerAdapter(@NonNull WeakReference<Activity> activity, STSwipeBackLayout layout) {
        mActivity = activity;
        mLayout = layout;
    }

    @Override
    public void onContentViewSwipedBack() {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            activity.finish();
            activity.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        // 侧滑未退出页面，切换至透明
        if (state == STSwipeBacViewDragHelper.STATE_IDLE) {
            if (null != mActivity.get() && null != mLayout && mLayout.toChangeWindowTranslucent) {
                // 将透明的 activity 转换成不透明
                STSwipeBackUtils.convertActivityFromTranslucent(mActivity.get());
            }
        }
    }

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void onEdgeTouch(int edgeFlag) {
        // 侧滑切换至透明
        if (null != mActivity.get() && null != mLayout && mLayout.toChangeWindowTranslucent) {
            // 将不透明的 activity 转换成透明
            STSwipeBackUtils.convertActivityToTranslucent(mActivity.get(), new STSwipeBackUtils.TranslucentListener() {
                @Override
                public void onTranslucent() {
                    mLayout.setPageTranslucent(true);
                }
            });
        }
    }

    @Override
    public void onScrollOverThreshold() {

    }

}
