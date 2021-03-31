package com.smart.library.util.swipeback.relate;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.annotation.Keep;

import com.smart.library.R;
import com.smart.library.util.swipeback.STSwipeBackLayout;
import com.smart.library.util.swipeback.STSwipeBackUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Mr.Jude on 2015/8/3.
 * 每个滑动页面的管理
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "RedundantSuppression", "IfStatementWithIdenticalBranches"})
@Keep
public class STSwipeBackPage {
    //仅为判断是否需要将mSwipeBackLayout注入进去
    private boolean mEnable = true;
    private boolean mRelativeEnable = false;

    WeakReference<Activity> mActivity;
    STSwipeBackLayout mSwipeBackLayout;
    STSwipeBackRelateListenerAdapter mRelateListenerAdapter;

    STSwipeBackPage(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    //页面的回调用于配置滑动效果
    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    void onCreate() {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            activity.getWindow().getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mSwipeBackLayout = (STSwipeBackLayout) LayoutInflater.from(activity).inflate(R.layout.st_swipeback_layout, null);
        }
        mRelateListenerAdapter = new STSwipeBackRelateListenerAdapter(this);
    }

    void onPostCreate() {
        handleLayout();
    }

    @TargetApi(11)
    public STSwipeBackPage setSwipeRelateEnable(boolean enable) {
        mRelativeEnable = enable;
        mRelateListenerAdapter.setEnable(enable);
        return this;
    }

    public STSwipeBackPage setSwipeRelateOffset(int offset) {
        mRelateListenerAdapter.setOffset(offset);
        return this;
    }

    //是否可滑动关闭
    public STSwipeBackPage setSwipeBackEnable(boolean enable) {
        setSwipeBackEnable(enable, null);
        return this;
    }

    public STSwipeBackPage setSwipeBackEnable(boolean enable, Boolean isActivityThemeTranslucent) {
        mEnable = enable;
        mSwipeBackLayout.setEnableGesture(enable);
        handleLayout();
        if (enable && isActivityThemeTranslucent != null) {
            Activity activity = mActivity.get();
            if (null != activity && !activity.isFinishing()) {
                if (!isActivityThemeTranslucent) {
                    // 如果 activity theme 是不透明的, 增加该行代码将会使得 activity 打开或者关闭时, 上一个页面有位移动画; 不加该行代码则上一个页面没有位移动画
                    boolean opaque = STSwipeBackUtils.convertActivityFromTranslucent(activity);
                    mSwipeBackLayout.setToChangeWindowTranslucent(true);
                } else {
                    // 如果 activity theme 是透明的, 增加该行代码将会使得 activity 打开或者关闭时, 上一个页面有位移动画, 但是 activity 将变成不透明效果; 不加该行代码则上一个页面没有位移动画, 且 activity 是透明效果
                    // boolean opaque = STSwipeBackUtils.convertActivityFromTranslucent(activity);
                    mSwipeBackLayout.setToChangeWindowTranslucent(false);
                }
            }
        }
        return this;
    }

    private void handleLayout() {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            if (mEnable || mRelativeEnable) {
                mSwipeBackLayout.attachToActivity(activity);
            } else {
                mSwipeBackLayout.removeFromActivity(activity);
            }
        }
    }

    //可滑动的范围。百分比。200表示为左边200px的屏幕
    public STSwipeBackPage setSwipeEdge(int swipeEdge) {
        mSwipeBackLayout.setEdgeSize(swipeEdge);
        return this;
    }

    //可滑动的范围。百分比。0.2表示为左边20%的屏幕
    public STSwipeBackPage setSwipeEdgePercent(float swipeEdgePercent) {
        mSwipeBackLayout.setEdgeSizePercent(swipeEdgePercent);
        return this;
    }

    //对横向滑动手势的敏感程度。0为迟钝 1为敏感
    public STSwipeBackPage setSwipeSensitivity(float sensitivity) {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            mSwipeBackLayout.setSensitivity(activity, sensitivity);
        }
        return this;
    }

    public STSwipeBackPage setSwipeBackShadowEnable(boolean enable) {
        Activity activity = mActivity.get();
        if (null != activity && !activity.isFinishing()) {
            mSwipeBackLayout.setSwipeBackShadowEnable(enable);
        }
        return this;
    }

    //底层阴影颜色
    public STSwipeBackPage setScrimColor(int color) {
        mSwipeBackLayout.setScrimColor(color);
        return this;
    }

    //触发关闭Activity百分比
    public STSwipeBackPage setClosePercent(float percent) {
        mSwipeBackLayout.setScrollThresHold(percent);
        return this;
    }

    public STSwipeBackPage setDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mSwipeBackLayout.setDisallowInterceptTouchEvent(disallowIntercept);
        return this;
    }

    public STSwipeBackPage addListener(STSwipeBackLayout.SwipeListener listener) {
        mSwipeBackLayout.addSwipeListener(listener);
        return this;
    }

    public STSwipeBackPage removeListener(STSwipeBackLayout.SwipeListener listener) {
        mSwipeBackLayout.removeSwipeListener(listener);
        return this;
    }

    public STSwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void scrollToFinishActivity() {
        mSwipeBackLayout.scrollToFinishActivity();
    }

}
