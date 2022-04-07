package com.smart.library.support.constraint.motion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import com.smart.library.support.R;
import com.smart.library.support.constraint.motion.utils.Easing;

public class KeyPosition extends KeyPositionBase
{
    private static final String TAG = "KeyPosition";
    static final String NAME = "KeyPosition";
    String mTransitionEasing;
    int mPathMotionArc;
    int mDrawPath;
    float mPercentWidth;
    float mPercentHeight;
    float mPercentX;
    float mPercentY;
    float mAltPercentX;
    float mAltPercentY;
    public static final int TYPE_SCREEN = 2;
    public static final int TYPE_PATH = 1;
    public static final int TYPE_CARTESIAN = 0;
    int mPositionType;
    private float mCalculatedPositionX;
    private float mCalculatedPositionY;
    static final int KEY_TYPE = 2;
    private static final String PERCENT_Y = "percentY";
    private static final String PERCENT_X = "percentX";

    public KeyPosition() {
        this.mTransitionEasing = null;
        this.mPathMotionArc = KeyPosition.UNSET;
        this.mDrawPath = 0;
        this.mPercentWidth = Float.NaN;
        this.mPercentHeight = Float.NaN;
        this.mPercentX = Float.NaN;
        this.mPercentY = Float.NaN;
        this.mAltPercentX = Float.NaN;
        this.mAltPercentY = Float.NaN;
        this.mPositionType = 0;
        this.mCalculatedPositionX = Float.NaN;
        this.mCalculatedPositionY = Float.NaN;
        this.mType = 2;
    }

    public void load(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyPosition);
        Loader.read(this, a);
    }

    @Override
    public void addValues(final HashMap<String, SplineSet> splines) {
    }

    @Override
    void calcPosition(final int layoutWidth, final int layoutHeight, final float start_x, final float start_y, final float end_x, final float end_y) {
        switch (this.mPositionType) {
            case 2: {
                this.calcScreenPosition(layoutWidth, layoutHeight);
            }
            case 1: {
                this.calcPathPosition(start_x, start_y, end_x, end_y);
            }
            default: {
                this.calcCartesianPosition(start_x, start_y, end_x, end_y);
            }
        }
    }

    private void calcScreenPosition(final int layoutWidth, final int layoutHeight) {
        final int viewWidth = 0;
        final int viewHeight = 0;
        this.mCalculatedPositionX = (layoutWidth - viewWidth) * this.mPercentX + viewWidth / 2;
        this.mCalculatedPositionY = (layoutHeight - viewHeight) * this.mPercentX + viewHeight / 2;
    }

    private void calcPathPosition(final float start_x, final float start_y, final float end_x, final float end_y) {
        final float pathVectorX = end_x - start_x;
        final float pathVectorY = end_y - start_y;
        final float perpendicularX = -pathVectorY;
        final float perpendicularY = pathVectorX;
        this.mCalculatedPositionX = start_x + pathVectorX * this.mPercentX + perpendicularX * this.mPercentY;
        this.mCalculatedPositionY = start_y + pathVectorY * this.mPercentX + perpendicularY * this.mPercentY;
    }

    private void calcCartesianPosition(final float start_x, final float start_y, final float end_x, final float end_y) {
        final float pathVectorX = end_x - start_x;
        final float pathVectorY = end_y - start_y;
        final float dxdx = Float.isNaN(this.mPercentX) ? 0.0f : this.mPercentX;
        final float dydx = Float.isNaN(this.mAltPercentY) ? 0.0f : this.mAltPercentY;
        final float dydy = Float.isNaN(this.mPercentY) ? 0.0f : this.mPercentY;
        final float dxdy = Float.isNaN(this.mAltPercentX) ? 0.0f : this.mAltPercentX;
        this.mCalculatedPositionX = (int)(start_x + pathVectorX * dxdx + pathVectorY * dxdy);
        this.mCalculatedPositionY = (int)(start_y + pathVectorX * dydx + pathVectorY * dydy);
    }

    @Override
    float getPositionX() {
        return this.mCalculatedPositionX;
    }

    @Override
    float getPositionY() {
        return this.mCalculatedPositionY;
    }

    public void positionAttributes(final View view, final RectF start, final RectF end, final float x, final float y, final String[] attribute, final float[] value) {
        switch (this.mPositionType) {
            case 1: {
                this.positionPathAttributes(start, end, x, y, attribute, value);
            }
            case 2: {
                this.positionScreenAttributes(view, start, end, x, y, attribute, value);
            }
            default: {
                this.positionCartAttributes(start, end, x, y, attribute, value);
            }
        }
    }

    void positionPathAttributes(final RectF start, final RectF end, final float x, final float y, final String[] attribute, final float[] value) {
        final float startCenterX = start.centerX();
        final float startCenterY = start.centerY();
        final float endCenterX = end.centerX();
        final float endCenterY = end.centerY();
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        final float distance = (float)Math.hypot(pathVectorX, pathVectorY);
        if (distance < 1.0E-4) {
            System.out.println("distance ~ 0");
            value[1] = (value[0] = 0.0f);
            return;
        }
        final float dx = pathVectorX / distance;
        final float dy = pathVectorY / distance;
        final float perpendicular = (dx * (y - startCenterY) - (x - startCenterX) * dy) / distance;
        final float dist = (dx * (x - startCenterX) + dy * (y - startCenterY)) / distance;
        if (attribute[0] != null) {
            if ("percentX".equals(attribute[0])) {
                value[0] = dist;
                value[1] = perpendicular;
            }
        }
        else {
            attribute[0] = "percentX";
            attribute[1] = "percentY";
            value[0] = dist;
            value[1] = perpendicular;
        }
    }

    void positionScreenAttributes(final View view, final RectF start, final RectF end, final float x, final float y, final String[] attribute, final float[] value) {
        final float startCenterX = start.centerX();
        final float startCenterY = start.centerY();
        final float endCenterX = end.centerX();
        final float endCenterY = end.centerY();
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        final int width = viewGroup.getWidth();
        final int height = viewGroup.getHeight();
        if (attribute[0] != null) {
            if ("percentX".equals(attribute[0])) {
                value[0] = x / width;
                value[1] = y / height;
            }
            else {
                value[1] = x / width;
                value[0] = y / height;
            }
        }
        else {
            attribute[0] = "percentX";
            value[0] = x / width;
            attribute[1] = "percentY";
            value[1] = y / height;
        }
    }

    void positionCartAttributes(final RectF start, final RectF end, final float x, final float y, final String[] attribute, final float[] value) {
        final float startCenterX = start.centerX();
        final float startCenterY = start.centerY();
        final float endCenterX = end.centerX();
        final float endCenterY = end.centerY();
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        if (attribute[0] != null) {
            if ("percentX".equals(attribute[0])) {
                value[0] = (x - startCenterX) / pathVectorX;
                value[1] = (y - startCenterY) / pathVectorY;
            }
            else {
                value[1] = (x - startCenterX) / pathVectorX;
                value[0] = (y - startCenterY) / pathVectorY;
            }
        }
        else {
            attribute[0] = "percentX";
            value[0] = (x - startCenterX) / pathVectorX;
            attribute[1] = "percentY";
            value[1] = (y - startCenterY) / pathVectorY;
        }
    }

    @Override
    public boolean intersects(final int layoutWidth, final int layoutHeight, final RectF start, final RectF end, final float x, final float y) {
        this.calcPosition(layoutWidth, layoutHeight, start.centerX(), start.centerY(), end.centerX(), end.centerY());
        return Math.abs(x - this.mCalculatedPositionX) < 20.0f && Math.abs(y - this.mCalculatedPositionY) < 20.0f;
    }

    @Override
    public void setValue(final String tag, final Object value) {
        switch (tag) {
            case "transitionEasing": {
                this.mTransitionEasing = value.toString();
                break;
            }
            case "drawPath": {
                this.mDrawPath = this.toInt(value);
                break;
            }
            case "percentWidth": {
                this.mPercentWidth = this.toFloat(value);
                break;
            }
            case "percentHeight": {
                this.mPercentHeight = this.toFloat(value);
                break;
            }
            case "sizePercent": {
                final float float1 = this.toFloat(value);
                this.mPercentWidth = float1;
                this.mPercentHeight = float1;
                break;
            }
            case "percentX": {
                this.mPercentX = this.toFloat(value);
                break;
            }
            case "percentY": {
                this.mPercentY = this.toFloat(value);
                break;
            }
        }
    }

    private static class Loader
    {
        private static final int TARGET_ID = 1;
        private static final int FRAME_POSITION = 2;
        private static final int TRANSITION_EASING = 3;
        private static final int CURVE_FIT = 4;
        private static final int DRAW_PATH = 5;
        private static final int PERCENT_X = 6;
        private static final int PERCENT_Y = 7;
        private static final int SIZE_PERCENT = 8;
        private static final int TYPE = 9;
        private static final int PATH_MOTION_ARC = 10;
        private static final int PERCENT_WIDTH = 11;
        private static final int PERCENT_HEIGHT = 12;
        private static SparseIntArray mAttrMap;

        private static void read(final KeyPosition c, final TypedArray a) {
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Loader.mAttrMap.get(attr)) {
                    case 1: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            if (c.mTargetId == -1) {
                                c.mTargetString = a.getString(attr);
                                break;
                            }
                            break;
                        }
                        else {
                            if (a.peekValue(attr).type == 3) {
                                c.mTargetString = a.getString(attr);
                                break;
                            }
                            c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            break;
                        }
                    }
                    case 2: {
                        c.mFramePosition = a.getInt(attr, c.mFramePosition);
                        break;
                    }
                    case 3: {
                        if (a.peekValue(attr).type == 3) {
                            c.mTransitionEasing = a.getString(attr);
                            break;
                        }
                        c.mTransitionEasing = Easing.NAMED_EASING[a.getInteger(attr, 0)];
                        break;
                    }
                    case 10: {
                        c.mPathMotionArc = a.getInt(attr, c.mPathMotionArc);
                        break;
                    }
                    case 4: {
                        c.mCurveFit = a.getInteger(attr, c.mCurveFit);
                        break;
                    }
                    case 5: {
                        c.mDrawPath = a.getInt(attr, c.mDrawPath);
                        break;
                    }
                    case 6: {
                        c.mPercentX = a.getFloat(attr, c.mPercentX);
                        break;
                    }
                    case 7: {
                        c.mPercentY = a.getFloat(attr, c.mPercentY);
                        break;
                    }
                    case 8: {
                        final float float1 = a.getFloat(attr, c.mPercentHeight);
                        c.mPercentWidth = float1;
                        c.mPercentHeight = float1;
                        break;
                    }
                    case 11: {
                        c.mPercentWidth = a.getFloat(attr, c.mPercentWidth);
                        break;
                    }
                    case 12: {
                        c.mPercentHeight = a.getFloat(attr, c.mPercentHeight);
                        break;
                    }
                    case 9: {
                        c.mPositionType = a.getInt(attr, c.mPositionType);
                        break;
                    }
                    default: {
                        Log.e("KeyPosition", "unused attribute 0x" + Integer.toHexString(attr) + "   " + Loader.mAttrMap.get(attr));
                        break;
                    }
                }
            }
            if (c.mFramePosition == -1) {
                Log.e("KeyPosition", "no frame position");
            }
        }

        static {
            (Loader.mAttrMap = new SparseIntArray()).append(R.styleable.KeyPosition_motionTarget, 1);
            Loader.mAttrMap.append(R.styleable.KeyPosition_framePosition, 2);
            Loader.mAttrMap.append(R.styleable.KeyPosition_transitionEasing, 3);
            Loader.mAttrMap.append(R.styleable.KeyPosition_curveFit, 4);
            Loader.mAttrMap.append(R.styleable.KeyPosition_drawPath, 5);
            Loader.mAttrMap.append(R.styleable.KeyPosition_percentX, 6);
            Loader.mAttrMap.append(R.styleable.KeyPosition_percentY, 7);
            Loader.mAttrMap.append(R.styleable.KeyPosition_keyPositionType, 9);
            Loader.mAttrMap.append(R.styleable.KeyPosition_sizePercent, 8);
            Loader.mAttrMap.append(R.styleable.KeyPosition_percentWidth, 11);
            Loader.mAttrMap.append(R.styleable.KeyPosition_percentHeight, 12);
            Loader.mAttrMap.append(R.styleable.KeyPosition_pathMotionArc, 10);
        }
    }
}
