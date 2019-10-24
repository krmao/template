package com.smart.library.support.constraint.helper;

import com.smart.library.support.constraint.*;
import android.view.*;
import android.content.*;
import android.util.*;
import android.os.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class Layer extends ConstraintHelper
{
    private static final String TAG = "Layer";
    private float mRotationCenterX;
    private float mRotationCenterY;
    private float mGroupRotateAngle;
    ConstraintLayout mContainer;
    private float mScaleX;
    private float mScaleY;
    protected float mComputedCenterX;
    protected float mComputedCenterY;
    protected float mComputedMaxX;
    protected float mComputedMaxY;
    protected float mComputedMinX;
    protected float mComputedMinY;
    boolean mNeedBounds;
    View[] mViews;
    private float mShiftX;
    private float mShiftY;
    
    public Layer(final Context context) {
        super(context);
        this.mRotationCenterX = Float.NaN;
        this.mRotationCenterY = Float.NaN;
        this.mGroupRotateAngle = Float.NaN;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        this.mComputedMaxX = Float.NaN;
        this.mComputedMaxY = Float.NaN;
        this.mComputedMinX = Float.NaN;
        this.mComputedMinY = Float.NaN;
        this.mNeedBounds = true;
        this.mViews = null;
        this.mShiftX = 0.0f;
        this.mShiftY = 0.0f;
    }
    
    public Layer(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mRotationCenterX = Float.NaN;
        this.mRotationCenterY = Float.NaN;
        this.mGroupRotateAngle = Float.NaN;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        this.mComputedMaxX = Float.NaN;
        this.mComputedMaxY = Float.NaN;
        this.mComputedMinX = Float.NaN;
        this.mComputedMinY = Float.NaN;
        this.mNeedBounds = true;
        this.mViews = null;
        this.mShiftX = 0.0f;
        this.mShiftY = 0.0f;
    }
    
    public Layer(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRotationCenterX = Float.NaN;
        this.mRotationCenterY = Float.NaN;
        this.mGroupRotateAngle = Float.NaN;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        this.mComputedMaxX = Float.NaN;
        this.mComputedMaxY = Float.NaN;
        this.mComputedMinX = Float.NaN;
        this.mComputedMinY = Float.NaN;
        this.mNeedBounds = true;
        this.mViews = null;
        this.mShiftX = 0.0f;
        this.mShiftY = 0.0f;
    }
    
    @Override
    protected void init(final AttributeSet attrs) {
        super.init(attrs);
        this.mUseViewMeasure = false;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContainer = (ConstraintLayout)this.getParent();
    }
    
    @Override
    public void updatePreLayout(final ConstraintLayout container) {
        this.mContainer = container;
        final int visibility = this.getVisibility();
        final float rotate = this.getRotation();
        if (rotate == 0.0f) {
            if (!Float.isNaN(this.mGroupRotateAngle)) {
                this.mGroupRotateAngle = rotate;
            }
        }
        else {
            this.mGroupRotateAngle = rotate;
        }
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
    
    public void setRotation(final float angle) {
        this.mGroupRotateAngle = angle;
        this.transform();
    }
    
    public void setScaleX(final float scaleX) {
        this.mScaleX = scaleX;
        this.transform();
    }
    
    public void setScaleY(final float scaleY) {
        this.mScaleY = scaleY;
        this.transform();
    }
    
    public void setPivotX(final float pivotX) {
        this.mRotationCenterX = pivotX;
        this.transform();
    }
    
    public void setPivotY(final float pivotY) {
        this.mRotationCenterY = pivotY;
        this.transform();
    }
    
    public void setTranslationX(final float dx) {
        this.mShiftX = dx;
        this.transform();
    }
    
    public void setTranslationY(final float dy) {
        this.mShiftY = dy;
        this.transform();
    }
    
    @Override
    public void updatePostLayout(final ConstraintLayout container) {
        this.reCacheViews();
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        final ConstraintWidget widget = params.getConstraintWidget();
        widget.setWidth(0);
        widget.setHeight(0);
        this.calcCenters();
        final int left = (int)this.mComputedMinX - this.getPaddingLeft();
        final int top = (int)this.mComputedMinY - this.getPaddingTop();
        final int right = (int)this.mComputedMaxX + this.getPaddingRight();
        final int bottom = (int)this.mComputedMaxY + this.getPaddingBottom();
        this.layout(left, top, right, bottom);
        if (!Float.isNaN(this.mGroupRotateAngle)) {
            this.transform();
        }
    }
    
    private void reCacheViews() {
        if (this.mContainer == null) {
            return;
        }
        if (this.mCount == 0) {
            return;
        }
        if (this.mViews == null || this.mViews.length != this.mCount) {
            this.mViews = new View[this.mCount];
        }
        for (int i = 0; i < this.mCount; ++i) {
            final int id = this.mIds[i];
            this.mViews[i] = this.mContainer.getViewById(id);
        }
    }
    
    protected void calcCenters() {
        if (this.mContainer == null) {
            return;
        }
        if (!this.mNeedBounds && !Float.isNaN(this.mComputedCenterX) && !Float.isNaN(this.mComputedCenterY)) {
            return;
        }
        if (Float.isNaN(this.mRotationCenterX) || Float.isNaN(this.mRotationCenterY)) {
            final View[] views = this.getViews(this.mContainer);
            int minx = views[0].getLeft();
            int miny = views[0].getTop();
            int maxx = views[0].getRight();
            int maxy = views[0].getBottom();
            for (int i = 0; i < this.mCount; ++i) {
                final View view = views[i];
                minx = Math.min(minx, view.getLeft());
                miny = Math.min(miny, view.getTop());
                maxx = Math.max(maxx, view.getRight());
                maxy = Math.max(maxy, view.getBottom());
            }
            this.mComputedMaxX = maxx;
            this.mComputedMaxY = maxy;
            this.mComputedMinX = minx;
            this.mComputedMinY = miny;
            if (Float.isNaN(this.mRotationCenterX)) {
                this.mComputedCenterX = (minx + maxx) / 2;
            }
            else {
                this.mComputedCenterX = this.mRotationCenterX;
            }
            if (Float.isNaN(this.mRotationCenterY)) {
                this.mComputedCenterY = (miny + maxy) / 2;
            }
            else {
                this.mComputedCenterY = this.mRotationCenterY;
            }
        }
        else {
            this.mComputedCenterY = this.mRotationCenterY;
            this.mComputedCenterX = this.mRotationCenterX;
        }
    }
    
    private void transform() {
        if (this.mContainer == null) {
            return;
        }
        if (this.mViews == null) {
            this.reCacheViews();
        }
        this.calcCenters();
        final double rad = Math.toRadians(this.mGroupRotateAngle);
        final float sin = (float)Math.sin(rad);
        final float cos = (float)Math.cos(rad);
        final float m11 = this.mScaleX * cos;
        final float m12 = -this.mScaleY * sin;
        final float m13 = this.mScaleX * sin;
        final float m14 = this.mScaleY * cos;
        for (int i = 0; i < this.mCount; ++i) {
            final View view = this.mViews[i];
            final int x = (view.getLeft() + view.getRight()) / 2;
            final int y = (view.getTop() + view.getBottom()) / 2;
            final float dx = x - this.mComputedCenterX;
            final float dy = y - this.mComputedCenterY;
            final float shiftx = m11 * dx + m12 * dy - dx + this.mShiftX;
            final float shifty = m13 * dx + m14 * dy - dy + this.mShiftY;
            view.setTranslationX(shiftx);
            view.setTranslationY(shifty);
            view.setScaleY(this.mScaleY);
            view.setScaleX(this.mScaleX);
            view.setRotation(this.mGroupRotateAngle);
        }
    }
}
