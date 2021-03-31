package com.smart.library.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.Keep;

import com.smart.library.STInitializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/11/15
 *     desc  : utils about adapt screen
 * </pre>
 *
 * https://blankj.com/2018/12/18/android-adapt-screen-killer/
 *
 *
 * 适配 1920x1080 15.6 inch 的平板/TV
 * Tools -> AVD Manager -> Create Virtual Device -> New Hardware Profile
 * Device Name(新建两个, 一个是真实尺寸, 用于运行模拟器, 一个是预览尺寸, 通过计算获取)
 * 	tv_1920x1080_15.6_simulator API 22 模拟器运行使用, 尺寸与目标平板真实尺寸一致
 * 	tv_1920x1080_30.6_preview   开发时 android studio 预览使用
 * Device Type
 * 	Phone/Tablet
 * Screen
 *  Screen size 模拟器运行与目标平板尺寸一致 15.6 inch / Android Studio 预览 sqrt(1080*1080 + 1920*1920)/72 = 30.6 inch
 *  Resolution 1920x1080 px tv/平板只开启横向, 关闭竖向
 *
 * 适配 750x1334 4.7 inch 的 iphone6
 * Tools -> AVD Manager -> Create Virtual Device -> New Hardware Profile
 * Device Name(新建两个, 一个是真实尺寸, 用于运行模拟器, 一个是预览尺寸, 通过计算获取)
 * 	iphone6_750_1334_4.7_simulator API 22 模拟器运行使用, 尺寸与目标平板真实尺寸一致
 * 	iphone6_750x1334_21.255_preview 开发时 android studio 预览使用
 * Device Type
 * 	Phone/Tablet
 * Screen
 * 	Screen size 模拟器运行与目标真机尺寸一致 4.7 inch / Android Studio 预览 sqrt(750*750 + 1334*1334)/72 = 21.255 inch
 * 	Resolution 750x1334 px
 */
//@Keep
public final class STAdaptScreenUtils {

    private static List<Field> sMetricsFields;

    private STAdaptScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Adapt for the horizontal screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static Resources adaptWidth(final Resources resources, final int designWidth) {
        float newXdpi = (resources.getDisplayMetrics().widthPixels * 72f) / designWidth;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    /**
     * Adapt for the vertical screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static Resources adaptHeight(final Resources resources, final int designHeight) {
        return adaptHeight(resources, designHeight, false);
    }

    /**
     * Adapt for the vertical screen, and call it in {@link android.app.Activity#getResources()}.
     */
    public static Resources adaptHeight(final Resources resources, final int designHeight, final boolean includeNavBar) {
        float screenHeight = (resources.getDisplayMetrics().heightPixels
                + (includeNavBar ? getNavBarHeight(resources) : 0)) * 72f;
        float newXdpi = screenHeight / designHeight;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    private static int getNavBarHeight(final Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * @param resources The resources.
     * @return the resource
     */
    public static Resources closeAdapt(final Resources resources) {
        float newXdpi = Resources.getSystem().getDisplayMetrics().density * 72f;
        applyDisplayMetrics(resources, newXdpi);
        return resources;
    }

    /**
     * Value of pt to value of px.
     *
     * @param ptValue The value of pt.
     * @return value of px
     */
    public static int pt2Px(final float ptValue) {
        DisplayMetrics metrics = STInitializer.application().getResources().getDisplayMetrics();
        return (int) (ptValue * metrics.xdpi / 72f + 0.5);
    }

    /**
     * Value of px to value of pt.
     *
     * @param pxValue The value of px.
     * @return value of pt
     */
    public static int px2Pt(final float pxValue) {
        DisplayMetrics metrics = STInitializer.application().getResources().getDisplayMetrics();
        return (int) (pxValue * 72 / metrics.xdpi + 0.5);
    }

    private static void applyDisplayMetrics(final Resources resources, final float newXdpi) {
        resources.getDisplayMetrics().xdpi = newXdpi;
        STInitializer.application().getResources().getDisplayMetrics().xdpi = newXdpi;
        applyOtherDisplayMetrics(resources, newXdpi);
    }

    public static Runnable getPreLoadRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                preLoad();
            }
        };
    }

    private static void preLoad() {
        applyDisplayMetrics(Resources.getSystem(), Resources.getSystem().getDisplayMetrics().xdpi);
    }

    private static void applyOtherDisplayMetrics(final Resources resources, final float newXdpi) {
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            tmpDm.xdpi = newXdpi;
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            applyMetricsFields(resources, newXdpi);
        }
    }

    private static void applyMetricsFields(final Resources resources, final float newXdpi) {
        for (Field metricsField : sMetricsFields) {
            try {
                DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                if (dm != null) dm.xdpi = newXdpi;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static DisplayMetrics getMetricsFromField(final Resources resources, final Field field) {
        try {
            return (DisplayMetrics) field.get(resources);
        } catch (Exception ignore) {
            return null;
        }
    }
}
