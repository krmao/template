package com.smart.library.support.constraint.motion.utils;

import com.smart.library.support.constraint.motion.*;

public class VelocityMatrix
{
    float mDScaleX;
    float mDScaleY;
    float mDTranslateX;
    float mDTranslateY;
    float mDRotate;
    float mRotate;
    private static String TAG;
    
    public void clear() {
        final float mdScaleX = 0.0f;
        this.mDRotate = mdScaleX;
        this.mDTranslateY = mdScaleX;
        this.mDTranslateX = mdScaleX;
        this.mDScaleY = mdScaleX;
        this.mDScaleX = mdScaleX;
    }
    
    public void setRotationVelocity(final SplineSet rot, final float position) {
        if (rot != null) {
            this.mDRotate = rot.getSlope(position);
            this.mRotate = rot.get(position);
        }
    }
    
    public void setTranslationVelocity(final SplineSet trans_x, final SplineSet trans_y, final float position) {
        if (trans_x != null) {
            this.mDTranslateX = trans_x.getSlope(position);
        }
        if (trans_y != null) {
            this.mDTranslateY = trans_y.getSlope(position);
        }
    }
    
    public void setScaleVelocity(final SplineSet scale_x, final SplineSet scale_y, final float position) {
        if (scale_x != null) {
            this.mDScaleX = scale_x.getSlope(position);
        }
        if (scale_y != null) {
            this.mDScaleY = scale_y.getSlope(position);
        }
    }
    
    public void setRotationVelocity(final KeyCycleOscillator osc_r, final float position) {
        if (osc_r != null) {
            this.mDRotate = osc_r.getSlope(position);
        }
    }
    
    public void setTranslationVelocity(final KeyCycleOscillator osc_x, final KeyCycleOscillator osc_y, final float position) {
        if (osc_x != null) {
            this.mDTranslateX = osc_x.getSlope(position);
        }
        if (osc_y != null) {
            this.mDTranslateY = osc_y.getSlope(position);
        }
    }
    
    public void setScaleVelocity(final KeyCycleOscillator osc_sx, final KeyCycleOscillator osc_sy, final float position) {
        if (osc_sx == null && osc_sy == null) {
            return;
        }
        if (osc_sx == null) {
            this.mDScaleX = osc_sx.getSlope(position);
        }
        if (osc_sy == null) {
            this.mDScaleY = osc_sy.getSlope(position);
        }
    }
    
    public void applyTransform(final float locationX, final float locationY, final int width, final int height, final float[] mAnchorDpDt) {
        float dx = mAnchorDpDt[0];
        float dy = mAnchorDpDt[1];
        final float offx = 2.0f * (locationX - 0.5f);
        final float offy = 2.0f * (locationY - 0.5f);
        dx += this.mDTranslateX;
        dy += this.mDTranslateY;
        dx += offx * this.mDScaleX;
        dy += offy * this.mDScaleY;
        final float r = (float)Math.toRadians(this.mRotate);
        final float dr = (float)Math.toRadians(this.mDRotate);
        dx += dr * (float)(-width * offx * Math.sin(r) - height * offy * Math.cos(r));
        dy += dr * (float)(width * offx * Math.cos(r) - height * offy * Math.sin(r));
        mAnchorDpDt[0] = dx;
        mAnchorDpDt[1] = dy;
    }
    
    static {
        VelocityMatrix.TAG = "VelocityMatrix";
    }
}
