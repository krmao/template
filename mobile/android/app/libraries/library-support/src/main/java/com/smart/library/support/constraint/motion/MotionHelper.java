package com.smart.library.support.constraint.motion;

import android.content.*;
import android.util.*;
import android.content.res.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.view.*;

public class MotionHelper extends ConstraintHelper implements Animatable
{
    private boolean mUseOnShow;
    private boolean mUseOnHide;
    private float mProgress;
    protected View[] views;
    
    public MotionHelper(final Context context) {
        super(context);
        this.mUseOnShow = false;
        this.mUseOnHide = false;
    }
    
    public MotionHelper(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mUseOnShow = false;
        this.mUseOnHide = false;
        this.init(attrs);
    }
    
    public MotionHelper(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mUseOnShow = false;
        this.mUseOnHide = false;
        this.init(attrs);
    }
    
    @Override
    protected void init(final AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.MotionHelper);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.MotionHelper_onShow) {
                    this.mUseOnShow = a.getBoolean(attr, this.mUseOnShow);
                }
                else if (attr == R.styleable.MotionHelper_onHide) {
                    this.mUseOnHide = a.getBoolean(attr, this.mUseOnHide);
                }
            }
        }
    }
    
    public boolean isUsedOnShow() {
        return this.mUseOnShow;
    }
    
    public boolean isUseOnHide() {
        return this.mUseOnHide;
    }
    
    @Override
    public float getProgress() {
        return this.mProgress;
    }
    
    @Override
    public void setProgress(final float progress) {
        this.mProgress = progress;
        if (this.mCount > 0) {
            this.views = this.getViews((ConstraintLayout)this.getParent());
            for (int i = 0; i < this.mCount; ++i) {
                final View view = this.views[i];
                this.setProgress(view, progress);
            }
        }
        else {
            final ViewGroup group = (ViewGroup)this.getParent();
            for (int count = group.getChildCount(), j = 0; j < count; ++j) {
                final View view2 = group.getChildAt(j);
                if (!(view2 instanceof MotionHelper)) {
                    this.setProgress(view2, progress);
                }
            }
        }
    }
    
    public void setProgress(final View view, final float progress) {
    }
}
