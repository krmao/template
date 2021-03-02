package com.smart.library.util.swipeback.relate;

import android.app.Activity;

import java.util.Stack;

/**
 * 滑动的全局管理类
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public class STSwipeBackHelper {

    private static final Stack<STSwipeBackPage> mPageStack = new Stack<>();

    private static STSwipeBackPage findHelperByActivity(Activity activity) {
        for (STSwipeBackPage swipeBackPage : mPageStack) {
            if (swipeBackPage.mActivity == activity) return swipeBackPage;
        }
        return null;
    }

    public static STSwipeBackPage getCurrentPage(Activity activity) {
        STSwipeBackPage page;
        if ((page = findHelperByActivity(activity)) == null) {
            throw new RuntimeException("You Should call STSwipeBackHelper.onCreate(activity) first");
        }
        return page;
    }

    public static void onCreate(Activity activity) {
        STSwipeBackPage page;
        if ((page = findHelperByActivity(activity)) == null) {
            page = mPageStack.push(new STSwipeBackPage(activity));
        }
        page.onCreate();
    }

    public static void onPostCreate(Activity activity) {
        STSwipeBackPage page;
        if ((page = findHelperByActivity(activity)) == null) {
            throw new RuntimeException("You Should call STSwipeBackHelper.onCreate(activity) first");
        }
        page.onPostCreate();
    }

    public static void onDestroy(Activity activity) {
        STSwipeBackPage page;
        if ((page = findHelperByActivity(activity)) == null) {
            throw new RuntimeException("You Should call STSwipeBackHelper.onCreate(activity) first");
        }
        mPageStack.remove(page);
        page.mActivity = null;
    }

    public static void finish(Activity activity) {
        STSwipeBackPage page;
        if ((page = findHelperByActivity(activity)) == null) {
            throw new RuntimeException("You Should call STSwipeBackHelper.onCreate(activity) first");
        }
        page.scrollToFinishActivity();
    }

    static STSwipeBackPage getPrePage(STSwipeBackPage activity) {
        int index = mPageStack.indexOf(activity);
        if (index > 0) return mPageStack.get(index - 1);
        else return null;
    }

}
