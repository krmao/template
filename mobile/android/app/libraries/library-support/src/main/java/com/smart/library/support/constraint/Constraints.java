package com.smart.library.support.constraint;

import android.view.*;
import android.content.*;
import android.util.*;
import android.os.*;
import android.content.res.*;

import com.smart.library.support.R;

public class Constraints extends ViewGroup
{
    public static final String TAG = "Constraints";
    ConstraintSet myConstraintSet;
    
    public Constraints(final Context context) {
        super(context);
        super.setVisibility(View.GONE);
    }
    
    public Constraints(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
        super.setVisibility(View.GONE);
    }
    
    public Constraints(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
        super.setVisibility(View.GONE);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    private void init(final AttributeSet attrs) {
        Log.v("Constraints", " ################# init");
    }
    
    protected ViewGroup.LayoutParams generateLayoutParams(final ViewGroup.LayoutParams p) {
        return (ViewGroup.LayoutParams)new ConstraintLayout.LayoutParams(p);
    }
    
    public ConstraintSet getConstraintSet() {
        if (this.myConstraintSet == null) {
            this.myConstraintSet = new ConstraintSet();
        }
        this.myConstraintSet.clone(this);
        return this.myConstraintSet;
    }
    
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
    }
    
    public static class LayoutParams extends ConstraintLayout.LayoutParams
    {
        public float alpha;
        public boolean applyElevation;
        public float elevation;
        public float rotation;
        public float rotationX;
        public float rotationY;
        public float scaleX;
        public float scaleY;
        public float transformPivotX;
        public float transformPivotY;
        public float translationX;
        public float translationY;
        public float translationZ;
        
        public LayoutParams(final int width, final int height) {
            super(width, height);
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = 0.0f;
            this.transformPivotY = 0.0f;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
        }
        
        public LayoutParams(final LayoutParams source) {
            super(source);
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = 0.0f;
            this.transformPivotY = 0.0f;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
        }
        
        public LayoutParams(final Context c, final AttributeSet attrs) {
            super(c, attrs);
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = 0.0f;
            this.transformPivotY = 0.0f;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
            final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ConstraintSet);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintSet_android_alpha) {
                    this.alpha = a.getFloat(attr, this.alpha);
                }
                else if (attr == R.styleable.ConstraintSet_android_elevation) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.elevation = a.getFloat(attr, this.elevation);
                        this.applyElevation = true;
                    }
                }
                else if (attr == R.styleable.ConstraintSet_android_rotationX) {
                    this.rotationX = a.getFloat(attr, this.rotationX);
                }
                else if (attr == R.styleable.ConstraintSet_android_rotationY) {
                    this.rotationY = a.getFloat(attr, this.rotationY);
                }
                else if (attr == R.styleable.ConstraintSet_android_rotation) {
                    this.rotation = a.getFloat(attr, this.rotation);
                }
                else if (attr == R.styleable.ConstraintSet_android_scaleX) {
                    this.scaleX = a.getFloat(attr, this.scaleX);
                }
                else if (attr == R.styleable.ConstraintSet_android_scaleY) {
                    this.scaleY = a.getFloat(attr, this.scaleY);
                }
                else if (attr == R.styleable.ConstraintSet_android_transformPivotX) {
                    this.transformPivotX = a.getFloat(attr, this.transformPivotX);
                }
                else if (attr == R.styleable.ConstraintSet_android_transformPivotY) {
                    this.transformPivotY = a.getFloat(attr, this.transformPivotY);
                }
                else if (attr == R.styleable.ConstraintSet_android_translationX) {
                    this.translationX = a.getFloat(attr, this.translationX);
                }
                else if (attr == R.styleable.ConstraintSet_android_translationY) {
                    this.translationY = a.getFloat(attr, this.translationY);
                }
                else if (attr == R.styleable.ConstraintSet_android_translationZ && Build.VERSION.SDK_INT >= 21) {
                    this.translationZ = a.getFloat(attr, this.translationZ);
                }
            }
        }
    }
}
