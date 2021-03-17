package com.smart.library.util.swipeback.relate;

import android.app.Activity;

import java.util.Stack;

/**
 * 滑动的全局管理类
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public class STSwipeBackHelper {

    private static final Stack<STSwipeBackPage> mPageStack = new Stack<>();

    public static STSwipeBackPage findPageByActivity(Activity activity) {
        for (STSwipeBackPage swipeBackPage : mPageStack) {
            if (swipeBackPage.mActivity.get() == activity) return swipeBackPage;
        }
        return null;
    }

    public static STSwipeBackPage onCreate(Activity activity) {
        STSwipeBackPage page;
        if ((page = findPageByActivity(activity)) == null) {
            page = mPageStack.push(new STSwipeBackPage(activity));
        }
        page.onCreate();
        return page;
    }

    public static void onPostCreate(STSwipeBackPage page) {
        if (page != null) page.onPostCreate();
    }

    public static void onDestroy(STSwipeBackPage page) {
        if (page != null) {
            mPageStack.remove(page);
            page.mActivity = null;
        }
    }

    public static void finish(STSwipeBackPage page) {
        if (page != null) page.scrollToFinishActivity();
    }

    static STSwipeBackPage getPrePage(STSwipeBackPage page) {
        int index = mPageStack.indexOf(page);
        if (index > 0) return mPageStack.get(index - 1);
        else return null;
    }

}
