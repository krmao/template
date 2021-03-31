package com.smart.library.util.swipeback.relate;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.smart.library.util.swipeback.STSwipeBackLayout;

/**
 * Created by Mr.Jude on 2015/8/26.
 */
@SuppressLint("ObsoleteSdkInt")
//@Keep
public class STSwipeBackRelateListenerAdapter implements STSwipeBackLayout.SwipeListenerEx {
    public STSwipeBackPage currentPage;
    private static final int DEFAULT_OFFSET = 40;
    private int offset = 432;

    public STSwipeBackRelateListenerAdapter(@NonNull STSwipeBackPage currentSwipeBackPage) {
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
        if (Build.VERSION.SDK_INT > 11) {
            STSwipeBackPage prePage = STSwipeBackHelper.getPrePage(currentPage);
            if (prePage != null) {
                /*
                  重要
                  一定要 + DEFAULT_OFFSET 使得最终 x > 0 , 否则 activity 无法 finish
                  详见代码 STSwipeBackLayout 631 行 left 必须 > 0  才会触发
                  if (left > 0 && !mActivity.isFinishing()) {
                      mActivity.finish();
                      mActivity.overridePendingTransition(0, R.anim.st_anim_left_right_close_exit);
                  }
                 */
                prePage.getSwipeBackLayout().setX((int) Math.min(-offset * Math.max(1 - scrollPercent, 0) + DEFAULT_OFFSET, 0));
                if (scrollPercent == 0) {
                    prePage.getSwipeBackLayout().setX(0);
                }
            }
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
    }

    @Override
    public void onScrollOverThreshold() {
    }

    @Override
    public void onContentViewSwipedBack() {
        STSwipeBackPage prePage = STSwipeBackHelper.getPrePage(currentPage);
        if (Build.VERSION.SDK_INT > 11) {
            if (prePage != null) prePage.getSwipeBackLayout().setX(0);
        }
    }
}
