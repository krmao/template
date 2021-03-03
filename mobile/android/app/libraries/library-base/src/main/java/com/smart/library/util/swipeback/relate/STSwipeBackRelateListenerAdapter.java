package com.smart.library.util.swipeback.relate;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;

import com.smart.library.util.swipeback.STSwipeBackLayout;
import com.smart.library.util.swipeback.STSwipeBackListenerAdapter;

/**
 * Created by Mr.Jude on 2015/8/26.
 */
@SuppressLint("ObsoleteSdkInt")
public class STSwipeBackRelateListenerAdapter extends STSwipeBackListenerAdapter {
    public STSwipeBackPage currentPage;
    private int offset = 360;

    public STSwipeBackRelateListenerAdapter(@NonNull STSwipeBackPage currentSwipeBackPage, STSwipeBackLayout layout, boolean toChangeWindowTranslucent) {
        super(currentSwipeBackPage.mActivity, layout, toChangeWindowTranslucent);
        this.currentPage = currentSwipeBackPage;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setEnable(boolean enable) {
        if (enable) currentPage.addListener(this);
        else currentPage.removeListener(this);
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        super.onScrollStateChange(state, scrollPercent);
        if (Build.VERSION.SDK_INT > 11) {
            STSwipeBackPage prePage = STSwipeBackHelper.getPrePage(currentPage);
            if (prePage != null) {
                prePage.getSwipeBackLayout().setX((int) Math.min(-offset * Math.max(1 - scrollPercent, 0), 0));
                if (scrollPercent == 0) {
                    prePage.getSwipeBackLayout().setX(0);
                }
            }
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        super.onEdgeTouch(edgeFlag);
    }

    @Override
    public void onScrollOverThreshold() {
        super.onScrollOverThreshold();
    }

    @Override
    public void onContentViewSwipedBack() {
        super.onContentViewSwipedBack();
        STSwipeBackPage prePage = STSwipeBackHelper.getPrePage(currentPage);
        if (Build.VERSION.SDK_INT > 11) {
            if (prePage != null) prePage.getSwipeBackLayout().setX(0);
        }
    }
}
