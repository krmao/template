package com.smart.library.util.swipeback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * https://github.com/wya-team/wya-android-kit/blob/a3082d47ce295c555c008951fcf328039d9a2fa4/wyauikit/uikit/src/main/java/com/wya/uikit/toolbar/swipeback/SwipeBackLayout.java
 */
@SuppressWarnings("JavaReflectionMemberAccess")
public class STSwipeBackUtils {
    private STSwipeBackUtils() {
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     * * 在Activity被convertFromTranslucent方法转为不透明之后，将其再从不透明转为透明
     */
    public static boolean convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity, TranslucentListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity, listener);
        } else {
            convertActivityToTranslucentBeforeL(activity, listener);
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    @SuppressWarnings("rawtypes")
    private static void convertActivityToTranslucentBeforeL(Activity activity, TranslucentListener listener) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            ConvertInvocationHandler myInvocationHandler = new ConvertInvocationHandler(new WeakReference<>(listener));
            Object obj = Proxy.newProxyInstance(Activity.class.getClassLoader(), new Class[]{translucentConversionListenerClazz}, myInvocationHandler);

            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
            method.setAccessible(true);
            method.invoke(activity, obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("rawtypes")
    private static void convertActivityToTranslucentAfterL(Activity activity, TranslucentListener listener) {
        try {
            @SuppressLint("DiscouragedPrivateApi") Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            ConvertInvocationHandler myInvocationHandler = new ConvertInvocationHandler(new WeakReference<>(listener));
            Object obj = Proxy.newProxyInstance(Activity.class.getClassLoader(), new Class[]{translucentConversionListenerClazz}, myInvocationHandler);

            Method convertToTranslucent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
            }
            if (null != convertToTranslucent) {
                convertToTranslucent.setAccessible(true);
                convertToTranslucent.invoke(activity, obj, options);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public interface TranslucentListener {

        /**
         * onTranslucent
         */
        void onTranslucent();

    }

    static class ConvertInvocationHandler implements InvocationHandler {
        private final WeakReference<TranslucentListener> mListener;

        public ConvertInvocationHandler(WeakReference<TranslucentListener> listener) {
            mListener = listener;
        }

        @SuppressWarnings("RedundantThrows")
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                boolean success = (boolean) args[0];
                if (success && mListener.get() != null) {
                    mListener.get().onTranslucent();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
