package com.smart.library.support.constraint;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class Guideline extends View {
    public Guideline(final Context context) {
        super(context);
        super.setVisibility(View.GONE);
    }

    public Guideline(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        super.setVisibility(View.GONE);
    }

    public Guideline(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setVisibility(View.GONE);
    }

    public Guideline(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr);
        super.setVisibility(View.GONE);
    }

    public void setVisibility(final int visibility) {
    }

    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        this.setMeasuredDimension(0, 0);
    }

    public void setGuidelineBegin(final int margin) {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.getLayoutParams();
        params.guideBegin = margin;
        this.setLayoutParams((ViewGroup.LayoutParams) params);
    }

    public void setGuidelineEnd(final int margin) {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.getLayoutParams();
        params.guideEnd = margin;
        this.setLayoutParams((ViewGroup.LayoutParams) params);
    }

    public void setGuidelinePercent(final float ratio) {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.getLayoutParams();
        params.guidePercent = ratio;
        this.setLayoutParams((ViewGroup.LayoutParams) params);
    }
}
