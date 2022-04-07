package com.smart.library.support.constraint;

import android.content.*;
import android.util.*;
import android.os.*;
import android.view.*;

public class Group extends ConstraintHelper
{
    public Group(final Context context) {
        super(context);
    }
    
    public Group(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public Group(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void init(final AttributeSet attrs) {
        super.init(attrs);
        this.mUseViewMeasure = false;
    }
    
    @Override
    public void updatePreLayout(final ConstraintLayout container) {
        final int visibility = this.getVisibility();
        float elevation = 0.0f;
        if (Build.VERSION.SDK_INT >= 21) {
            elevation = this.getElevation();
        }
        if (this.mReferenceIds != null) {
            this.setIds(this.mReferenceIds);
        }
        for (int i = 0; i < this.mCount; ++i) {
            final int id = this.mIds[i];
            final View view = container.getViewById(id);
            if (view != null) {
                view.setVisibility(visibility);
                if (elevation > 0.0f && Build.VERSION.SDK_INT >= 21) {
                    view.setElevation(elevation);
                }
            }
        }
    }
    
    @Override
    public void updatePostLayout(final ConstraintLayout container) {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        params.widget.setWidth(0);
        params.widget.setHeight(0);
    }
}
