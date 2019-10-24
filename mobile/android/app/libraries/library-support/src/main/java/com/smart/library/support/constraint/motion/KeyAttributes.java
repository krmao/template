package com.smart.library.support.constraint.motion;

import android.content.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import java.util.*;
import android.util.*;
import android.os.*;

public class KeyAttributes extends Key
{
    static final String NAME = "KeyAttribute";
    private static final String TAG = "KeyAttribute";
    private String mTransitionEasing;
    private int mCurveFit;
    private boolean mVisibility;
    private float mAlpha;
    private float mElevation;
    private float mRotation;
    private float mRotationX;
    private float mRotationY;
    private float mTransitionPathRotate;
    private float mScaleX;
    private float mScaleY;
    private float mTranslationX;
    private float mTranslationY;
    private float mTranslationZ;
    private float mProgress;
    public static final int KEY_TYPE = 1;
    
    public KeyAttributes() {
        this.mCurveFit = -1;
        this.mVisibility = false;
        this.mAlpha = Float.NaN;
        this.mElevation = Float.NaN;
        this.mRotation = Float.NaN;
        this.mRotationX = Float.NaN;
        this.mRotationY = Float.NaN;
        this.mTransitionPathRotate = Float.NaN;
        this.mScaleX = Float.NaN;
        this.mScaleY = Float.NaN;
        this.mTranslationX = Float.NaN;
        this.mTranslationY = Float.NaN;
        this.mTranslationZ = Float.NaN;
        this.mProgress = Float.NaN;
        this.mType = 1;
        this.mCustomConstraints = new HashMap<String, ConstraintAttribute>();
    }
    
    public void load(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyAttribute);
        Loader.read(this, a);
    }
    
    int getCurveFit() {
        return this.mCurveFit;
    }
    
    public void getAttributeNames(final HashSet<String> attributes) {
        if (!Float.isNaN(this.mAlpha)) {
            attributes.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            attributes.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            attributes.add("rotation");
        }
        if (!Float.isNaN(this.mRotationX)) {
            attributes.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            attributes.add("rotationY");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            attributes.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            attributes.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            attributes.add("translationZ");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            attributes.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mScaleX)) {
            attributes.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleX)) {
            attributes.add("scaleY");
        }
        if (!Float.isNaN(this.mProgress)) {
            attributes.add("progress");
        }
        if (this.mCustomConstraints.size() > 0) {
            for (final String s : this.mCustomConstraints.keySet()) {
                attributes.add("CUSTOM," + s);
            }
        }
    }
    
    @Override
    public void setInterpolation(final HashMap<String, Integer> interpolation) {
        if (this.mCurveFit == -1) {
            return;
        }
        if (!Float.isNaN(this.mAlpha)) {
            interpolation.put("alpha", this.mCurveFit);
        }
        if (!Float.isNaN(this.mElevation)) {
            interpolation.put("elevation", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotation)) {
            interpolation.put("rotation", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotationX)) {
            interpolation.put("rotationX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotationY)) {
            interpolation.put("rotationY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationX)) {
            interpolation.put("translationX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationY)) {
            interpolation.put("translationY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            interpolation.put("translationZ", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            interpolation.put("transitionPathRotate", this.mCurveFit);
        }
        if (!Float.isNaN(this.mScaleX)) {
            interpolation.put("scaleX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mScaleY)) {
            interpolation.put("scaleY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mProgress)) {
            interpolation.put("progress", this.mCurveFit);
        }
        if (this.mCustomConstraints.size() > 0) {
            for (final String s : this.mCustomConstraints.keySet()) {
                interpolation.put("CUSTOM," + s, this.mCurveFit);
            }
        }
    }
    
    @Override
    public void addValues(final HashMap<String, SplineSet> splines) {
        for (final String s : splines.keySet()) {
            final SplineSet splineSet = splines.get(s);
            if (s.startsWith("CUSTOM")) {
                final String ckey = s.substring("CUSTOM".length() + 1);
                final ConstraintAttribute cvalue = this.mCustomConstraints.get(ckey);
                if (cvalue == null) {
                    continue;
                }
                ((SplineSet.CustomSet)splineSet).setPoint(this.mFramePosition, cvalue);
            }
            else {
                final String s2 = s;
                switch (s2) {
                    case "alpha": {
                        if (!Float.isNaN(this.mAlpha)) {
                            splineSet.setPoint(this.mFramePosition, this.mAlpha);
                            continue;
                        }
                        continue;
                    }
                    case "elevation": {
                        if (!Float.isNaN(this.mElevation)) {
                            splineSet.setPoint(this.mFramePosition, this.mElevation);
                            continue;
                        }
                        continue;
                    }
                    case "rotation": {
                        if (!Float.isNaN(this.mRotation)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotation);
                            continue;
                        }
                        continue;
                    }
                    case "rotationX": {
                        if (!Float.isNaN(this.mRotationX)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotationX);
                            continue;
                        }
                        continue;
                    }
                    case "rotationY": {
                        if (!Float.isNaN(this.mRotationY)) {
                            splineSet.setPoint(this.mFramePosition, this.mRotationY);
                            continue;
                        }
                        continue;
                    }
                    case "transitionPathRotate": {
                        if (!Float.isNaN(this.mTransitionPathRotate)) {
                            splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate);
                            continue;
                        }
                        continue;
                    }
                    case "scaleX": {
                        if (!Float.isNaN(this.mScaleX)) {
                            splineSet.setPoint(this.mFramePosition, this.mScaleX);
                            continue;
                        }
                        continue;
                    }
                    case "scaleY": {
                        if (!Float.isNaN(this.mScaleY)) {
                            splineSet.setPoint(this.mFramePosition, this.mScaleY);
                            continue;
                        }
                        continue;
                    }
                    case "translationX": {
                        if (!Float.isNaN(this.mTranslationX)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationX);
                            continue;
                        }
                        continue;
                    }
                    case "translationY": {
                        if (!Float.isNaN(this.mTranslationY)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationY);
                            continue;
                        }
                        continue;
                    }
                    case "translationZ": {
                        if (!Float.isNaN(this.mTranslationZ)) {
                            splineSet.setPoint(this.mFramePosition, this.mTranslationZ);
                            continue;
                        }
                        continue;
                    }
                    case "progress": {
                        if (!Float.isNaN(this.mProgress)) {
                            splineSet.setPoint(this.mFramePosition, this.mProgress);
                            continue;
                        }
                        continue;
                    }
                    default: {
                        Log.v("KeyAttributes", "UNKNOWN addValues \"" + s + "\"");
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void setValue(final String tag, final Object value) {
        switch (tag) {
            case "alpha": {
                this.mAlpha = this.toFloat(value);
                break;
            }
            case "curveFit": {
                this.mCurveFit = this.toInt(value);
                break;
            }
            case "elevation": {
                this.mElevation = this.toFloat(value);
                break;
            }
            case "motionProgress": {
                this.mProgress = this.toFloat(value);
                break;
            }
            case "rotation": {
                this.mRotation = this.toFloat(value);
                break;
            }
            case "rotationX": {
                this.mRotationX = this.toFloat(value);
                break;
            }
            case "rotationY": {
                this.mRotationY = this.toFloat(value);
                break;
            }
            case "scaleX": {
                this.mScaleX = this.toFloat(value);
                break;
            }
            case "scaleY": {
                this.mScaleY = this.toFloat(value);
                break;
            }
            case "transitionEasing": {
                this.mTransitionEasing = value.toString();
                break;
            }
            case "visibility": {
                this.mVisibility = this.toBoolean(value);
                break;
            }
            case "transitionPathRotate": {
                this.mTransitionPathRotate = this.toFloat(value);
                break;
            }
            case "translationX": {
                this.mTranslationX = this.toFloat(value);
                break;
            }
            case "translationY": {
                this.mTranslationY = this.toFloat(value);
                break;
            }
            case "mTranslationZ": {
                this.mTranslationZ = this.toFloat(value);
                break;
            }
        }
    }
    
    private static class Loader
    {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_ROTATION = 4;
        private static final int ANDROID_ROTATION_X = 5;
        private static final int ANDROID_ROTATION_Y = 6;
        private static final int TRANSITION_PATH_ROTATE = 8;
        private static final int ANDROID_SCALE_X = 7;
        private static final int TRANSITION_EASING = 9;
        private static final int TARGET_ID = 10;
        private static final int FRAME_POSITION = 12;
        private static final int CURVE_FIT = 13;
        private static final int ANDROID_VISIBILITY = 14;
        private static final int ANDROID_SCALE_Y = 15;
        private static final int ANDROID_TRANSLATION_X = 16;
        private static final int ANDROID_TRANSLATION_Y = 17;
        private static final int ANDROID_TRANSLATION_Z = 18;
        private static final int PROGRESS = 19;
        private static SparseIntArray mAttrMap;
        
        public static void read(final KeyAttributes c, final TypedArray a) {
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Loader.mAttrMap.get(attr)) {
                    case 10: {
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
                    case 12: {
                        c.mFramePosition = a.getInt(attr, c.mFramePosition);
                        break;
                    }
                    case 1: {
                        c.mAlpha = a.getFloat(attr, c.mAlpha);
                        break;
                    }
                    case 2: {
                        c.mElevation = a.getDimension(attr, c.mElevation);
                        break;
                    }
                    case 4: {
                        c.mRotation = a.getFloat(attr, c.mRotation);
                        break;
                    }
                    case 13: {
                        c.mCurveFit = a.getInteger(attr, c.mCurveFit);
                        break;
                    }
                    case 7: {
                        c.mScaleX = a.getFloat(attr, c.mScaleX);
                        break;
                    }
                    case 5: {
                        c.mRotationX = a.getFloat(attr, c.mRotationX);
                        break;
                    }
                    case 6: {
                        c.mRotationY = a.getFloat(attr, c.mRotationY);
                        break;
                    }
                    case 9: {
                        c.mTransitionEasing = a.getString(attr);
                        break;
                    }
                    case 14: {
                        c.mVisibility = a.getBoolean(attr, c.mVisibility);
                        break;
                    }
                    case 15: {
                        c.mScaleY = a.getFloat(attr, c.mScaleY);
                        break;
                    }
                    case 8: {
                        c.mTransitionPathRotate = a.getFloat(attr, c.mTransitionPathRotate);
                        break;
                    }
                    case 16: {
                        c.mTranslationX = a.getDimension(attr, c.mTranslationX);
                        break;
                    }
                    case 17: {
                        c.mTranslationY = a.getDimension(attr, c.mTranslationY);
                        break;
                    }
                    case 18: {
                        if (Build.VERSION.SDK_INT >= 21) {
                            c.mTranslationZ = a.getDimension(attr, c.mTranslationZ);
                            break;
                        }
                        break;
                    }
                    case 19: {
                        c.mProgress = a.getFloat(attr, c.mProgress);
                        break;
                    }
                    default: {
                        Log.e("KeyAttribute", "unused attribute 0x" + Integer.toHexString(attr) + "   " + Loader.mAttrMap.get(attr));
                        break;
                    }
                }
            }
        }
        
        static {
            (Loader.mAttrMap = new SparseIntArray()).append(R.styleable.KeyAttribute_android_alpha, 1);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_elevation, 2);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_rotation, 4);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_rotationX, 5);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_rotationY, 6);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_scaleX, 7);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_transitionPathRotate, 8);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_transitionEasing, 9);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_motionTarget, 10);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_framePosition, 12);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_curveFit, 13);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_visibility, 14);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_scaleY, 15);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_translationX, 16);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_translationY, 17);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_android_translationZ, 18);
            Loader.mAttrMap.append(R.styleable.KeyAttribute_motionProgress, 19);
        }
    }
}
