package com.smart.library.util.swipeback;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * https://github.com/wya-team/wya-android-kit/blob/a3082d47ce295c555c008951fcf328039d9a2fa4/wyauikit/uikit/src/main/java/com/wya/uikit/toolbar/swipeback/SwipeBackLayout.java
 */
public class STSwipeBackListenerAdapter implements STSwipeBackLayout.SwipeListenerEx {
    private final WeakReference<Activity> mActivity;
    private final STSwipeBackLayout mLayout;
    private final boolean toChangeWindowTranslucent;
    
    public STSwipeBackListenerAdapter(@NonNull Activity activity, STSwipeBackLayout layout, boolean toChangeWindowTranslucent) {
        mActivity = new WeakReference<>(activity);
        mLayout = layout;
        this.toChangeWindowTranslucent = toChangeWindowTranslucent;
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
        if (state == STSwipeBacViewDragHelper.STATE_IDLE) {// 侧滑未退出页面，切换至透明
            if (null != mActivity.get()) {
                if (toChangeWindowTranslucent) {
                    //判断侧滑未退出页面是否改变主题透明
                    STSwipeBackUtils.convertActivityFromTranslucent(mActivity.get());
                }
            }
        }
    }
    
    @Override
    public void onEdgeTouch(int edgeFlag) {
        // 侧滑切换至透明
        if (null != mActivity.get()) {
            STSwipeBackUtils.convertActivityToTranslucent(mActivity.get(), new STSwipeBackUtils.TranslucentListener() {
                @Override
                public void onTranslucent() {
                    if (null != mLayout) {
                        mLayout.setPageTranslucent(true);
                    }
                }
            });
        }
    }
    
    @Override
    public void onScrollOverThreshold() {
        
    }
    
}
