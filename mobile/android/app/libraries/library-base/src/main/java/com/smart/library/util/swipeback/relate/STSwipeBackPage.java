package com.smart.library.util.swipeback.relate;

import android.annotation.TargetApi;
import android.app.Activity;

import com.smart.library.util.swipeback.STSwipeBackActivityHelper;
import com.smart.library.util.swipeback.STSwipeBackLayout;
import com.smart.library.util.swipeback.STSwipeBackUtils;

/**
 * Created by Mr.Jude on 2015/8/3.
 * 每个滑动页面的管理
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "RedundantSuppression"})
public class STSwipeBackPage {
    //仅为判断是否需要将mSwipeBackLayout注入进去
    private boolean mEnable = true;
    private boolean mRelativeEnable = false;

    Activity mActivity;
    STSwipeBackActivityHelper swipeBackHelper;
    STSwipeBackRelateListenerAdapter relateListenerAdapter;
    /**
     * 默认滑动过程中动态改变主题透明度（当有popupWindow的时候）,当有videoPlayer的时候不改变，一直为透明，不随互动改变，防止变黑
     */
    private boolean toChangeWindowTranslucent = true;

    STSwipeBackPage(Activity activity) {
        this.mActivity = activity;
    }

    //页面的回调用于配置滑动效果
    void onCreate() {
        swipeBackHelper = new STSwipeBackActivityHelper(mActivity);
        swipeBackHelper.onActivityCreate();
        relateListenerAdapter = new STSwipeBackRelateListenerAdapter(this, swipeBackHelper.getSwipeBackLayout(), toChangeWindowTranslucent);
    }

    void onPostCreate() {
        handleLayout();
    }

    public void setToChangeWindowTranslucent(boolean toChangeWindowTranslucent) {
        this.toChangeWindowTranslucent = toChangeWindowTranslucent;
    }

    @TargetApi(11)
    public STSwipeBackPage setSwipeRelateEnable(boolean enable) {
        mRelativeEnable = enable;
        relateListenerAdapter.setEnable(enable);
        return this;
    }

    public STSwipeBackPage setSwipeRelateOffset(int offset) {
        relateListenerAdapter.setOffset(offset);
        return this;
    }

    //是否可滑动关闭
    public STSwipeBackPage setSwipeBackEnable(boolean enable) {
        setSwipeBackEnable(enable, null);
        return this;
    }

    /**
     * isActivityThemeTranslucent 是否需要转换 translucent 状态? null 不需要处理, true 不需要通过代码反射转变 translucent 状态, false 需要通过代码反射转变(需要时转变为 translucent, 不需要时再设置回来 !translucent)
     */
    public STSwipeBackPage setSwipeBackEnable(boolean enable, Boolean isActivityThemeTranslucent) {
        mEnable = enable;
        swipeBackHelper.getSwipeBackLayout().setEnableGesture(enable);
        handleLayout();
        if (enable && isActivityThemeTranslucent != null) {
            if (!isActivityThemeTranslucent) {
                boolean opaque = STSwipeBackUtils.convertActivityFromTranslucent(mActivity);
                swipeBackHelper.getSwipeBackLayout().setPageTranslucent(!opaque);
                swipeBackHelper.getSwipeBackLayout().setToChangeWindowTranslucent(true);
            } else {
                swipeBackHelper.getSwipeBackLayout().setToChangeWindowTranslucent(false);
            }
        }
        return this;
    }

    private void handleLayout() {
        if (mEnable || mRelativeEnable) {
            swipeBackHelper.onPostCreate();
        } else {
            swipeBackHelper.getSwipeBackLayout().removeFromActivity(mActivity);
        }
    }

    //可滑动的范围。百分比。200表示为左边200px的屏幕
    public STSwipeBackPage setSwipeEdge(int swipeEdge) {
        swipeBackHelper.getSwipeBackLayout().setEdgeSize(swipeEdge);
        return this;
    }

    //可滑动的范围。百分比。0.2表示为左边20%的屏幕
    public STSwipeBackPage setSwipeEdgePercent(float swipeEdgePercent) {
        swipeBackHelper.getSwipeBackLayout().setEdgeSizePercent(swipeEdgePercent);
        return this;
    }

    //对横向滑动手势的敏感程度。0为迟钝 1为敏感
    public STSwipeBackPage setSwipeSensitivity(float sensitivity) {
        swipeBackHelper.getSwipeBackLayout().setSensitivity(mActivity, sensitivity);
        return this;
    }

    //底层阴影颜色
    public STSwipeBackPage setScrimColor(int color) {
        swipeBackHelper.getSwipeBackLayout().setScrimColor(color);
        return this;
    }

    //触发关闭Activity百分比
    public STSwipeBackPage setClosePercent(float percent) {
        swipeBackHelper.getSwipeBackLayout().setScrollThresHold(percent);
        return this;
    }

    public STSwipeBackPage setDisallowInterceptTouchEvent(boolean disallowIntercept) {
        swipeBackHelper.getSwipeBackLayout().setDisallowInterceptTouchEvent(disallowIntercept);
        return this;
    }

    public STSwipeBackPage addListener(STSwipeBackLayout.SwipeListener listener) {
        swipeBackHelper.getSwipeBackLayout().addSwipeListener(listener);
        return this;
    }

    public STSwipeBackPage removeListener(STSwipeBackLayout.SwipeListener listener) {
        swipeBackHelper.getSwipeBackLayout().removeSwipeListener(listener);
        return this;
    }

    public STSwipeBackLayout getSwipeBackLayout() {
        return swipeBackHelper.getSwipeBackLayout();
    }

    public void scrollToFinishActivity() {
        swipeBackHelper.getSwipeBackLayout().scrollToFinishActivity();
    }

}
