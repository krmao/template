package com.smart.library.support.constraint.motion;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.HashSet;

import com.smart.library.support.R;
import com.smart.library.support.constraint.ConstraintAttribute;

public class KeyCycle extends Key
{
    private static final String TAG = "KeyCycle";
    static final String NAME = "KeyCycle";
    private String mTransitionEasing;
    private int mCurveFit;
    private int mWaveShape;
    private float mWavePeriod;
    private float mWaveOffset;
    private float mProgress;
    private int mWaveVariesBy;
    private float mAlpha;
    private float mElevation;
    private float mRotation;
    private float mTransitionPathRotate;
    private float mRotationX;
    private float mRotationY;
    private float mScaleX;
    private float mScaleY;
    private float mTranslationX;
    private float mTranslationY;
    private float mTranslationZ;
    public static final int KEY_TYPE = 4;
    
    public KeyCycle() {
        this.mTransitionEasing = null;
        this.mCurveFit = 0;
        this.mWaveShape = -1;
        this.mWavePeriod = Float.NaN;
        this.mWaveOffset = 0.0f;
        this.mProgress = Float.NaN;
        this.mWaveVariesBy = -1;
        this.mAlpha = Float.NaN;
        this.mElevation = Float.NaN;
        this.mRotation = Float.NaN;
        this.mTransitionPathRotate = Float.NaN;
        this.mRotationX = Float.NaN;
        this.mRotationY = Float.NaN;
        this.mScaleX = Float.NaN;
        this.mScaleY = Float.NaN;
        this.mTranslationX = Float.NaN;
        this.mTranslationY = Float.NaN;
        this.mTranslationZ = Float.NaN;
        this.mType = 4;
        this.mCustomConstraints = new HashMap<String, ConstraintAttribute>();
    }
    
    public void load(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyCycle);
        Loader.read(this, a);
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
        if (!Float.isNaN(this.mScaleX)) {
            attributes.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            attributes.add("scaleY");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            attributes.add("transitionPathRotate");
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
        if (this.mCustomConstraints.size() > 0) {
            for (final String s : this.mCustomConstraints.keySet()) {
                attributes.add("CUSTOM," + s);
            }
        }
    }
    
    public void addCycleValues(final HashMap<String, KeyCycleOscillator> oscSet) {
        for (final String key : oscSet.keySet()) {
            if (key.startsWith("CUSTOM")) {
                final String ckey = key.substring("CUSTOM".length() + 1);
                final ConstraintAttribute cvalue = this.mCustomConstraints.get(ckey);
                if (cvalue != null && cvalue.getType() == ConstraintAttribute.AttributeType.FLOAT_TYPE) {
                    oscSet.get(key).setPoint(this.mFramePosition, this.mWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, cvalue.getValueToInterpolate(), cvalue);
                }
            }
            final float value = this.getValue(key);
            if (!Float.isNaN(value)) {
                oscSet.get(key).setPoint(this.mFramePosition, this.mWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, value);
            }
        }
    }
    
    public float getValue(final String key) {
        switch (key) {
            case "alpha": {
                return this.mAlpha;
            }
            case "elevation": {
                return this.mElevation;
            }
            case "rotation": {
                return this.mRotation;
            }
            case "rotationX": {
                return this.mRotationX;
            }
            case "rotationY": {
                return this.mRotationY;
            }
            case "transitionPathRotate": {
                return this.mTransitionPathRotate;
            }
            case "scaleX": {
                return this.mScaleX;
            }
            case "scaleY": {
                return this.mScaleY;
            }
            case "translationX": {
                return this.mTranslationX;
            }
            case "translationY": {
                return this.mTranslationY;
            }
            case "translationZ": {
                return this.mTranslationZ;
            }
            case "waveOffset": {
                return this.mWaveOffset;
            }
            case "progress": {
                return this.mProgress;
            }
            default: {
                Log.v("WARNING! KeyCycle", "  UNKNOWN  " + key);
                return Float.NaN;
            }
        }
    }
    
    @Override
    public void addValues(final HashMap<String, SplineSet> splines) {
        Debug.logStack("KeyCycle", "add " + splines.size() + " values", 2);
        for (final String s : splines.keySet()) {
            final SplineSet splineSet = splines.get(s);
            final String s2 = s;
            switch (s2) {
                case "alpha": {
                    splineSet.setPoint(this.mFramePosition, this.mAlpha);
                    continue;
                }
                case "elevation": {
                    splineSet.setPoint(this.mFramePosition, this.mElevation);
                    continue;
                }
                case "rotation": {
                    splineSet.setPoint(this.mFramePosition, this.mRotation);
                    continue;
                }
                case "rotationX": {
                    splineSet.setPoint(this.mFramePosition, this.mRotationX);
                    continue;
                }
                case "rotationY": {
                    splineSet.setPoint(this.mFramePosition, this.mRotationY);
                    continue;
                }
                case "transitionPathRotate": {
                    splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate);
                    continue;
                }
                case "scaleX": {
                    splineSet.setPoint(this.mFramePosition, this.mScaleX);
                    continue;
                }
                case "scaleY": {
                    splineSet.setPoint(this.mFramePosition, this.mScaleY);
                    continue;
                }
                case "translationX": {
                    splineSet.setPoint(this.mFramePosition, this.mTranslationX);
                    continue;
                }
                case "translationY": {
                    splineSet.setPoint(this.mFramePosition, this.mTranslationY);
                    continue;
                }
                case "translationZ": {
                    splineSet.setPoint(this.mFramePosition, this.mTranslationZ);
                    continue;
                }
                case "waveOffset": {
                    splineSet.setPoint(this.mFramePosition, this.mWaveOffset);
                    continue;
                }
                case "progress": {
                    splineSet.setPoint(this.mFramePosition, this.mProgress);
                    continue;
                }
                default: {
                    Log.v("WARNING KeyCycle", "  UNKNOWN  " + s);
                    continue;
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
            case "progress": {
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
            case "wavePeriod": {
                this.mWavePeriod = this.toFloat(value);
                break;
            }
            case "waveOffset": {
                this.mWaveOffset = this.toFloat(value);
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
        private static final int WAVE_SHAPE = 5;
        private static final int WAVE_PERIOD = 6;
        private static final int WAVE_OFFSET = 7;
        private static final int WAVE_VARIES_BY = 8;
        private static final int ANDROID_ALPHA = 9;
        private static final int ANDROID_ELEVATION = 10;
        private static final int ANDROID_ROTATION = 11;
        private static final int ANDROID_ROTATION_X = 12;
        private static final int ANDROID_ROTATION_Y = 13;
        private static final int TRANSITION_PATH_ROTATE = 14;
        private static final int ANDROID_SCALE_X = 15;
        private static final int ANDROID_SCALE_Y = 16;
        private static final int ANDROID_TRANSLATION_X = 17;
        private static final int ANDROID_TRANSLATION_Y = 18;
        private static final int ANDROID_TRANSLATION_Z = 19;
        private static final int PROGRESS = 20;
        private static SparseIntArray mAttrMap;
        
        private static void read(final KeyCycle c, final TypedArray a) {
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
                        c.mTransitionEasing = a.getString(attr);
                        break;
                    }
                    case 4: {
                        c.mCurveFit = a.getInteger(attr, c.mCurveFit);
                        break;
                    }
                    case 5: {
                        c.mWaveShape = a.getInt(attr, c.mWaveShape);
                        break;
                    }
                    case 6: {
                        c.mWavePeriod = a.getFloat(attr, c.mWavePeriod);
                        break;
                    }
                    case 7: {
                        final TypedValue type = a.peekValue(attr);
                        if (type.type == 5) {
                            c.mWaveOffset = a.getDimension(attr, c.mWaveOffset);
                            break;
                        }
                        c.mWaveOffset = a.getFloat(attr, c.mWaveOffset);
                        break;
                    }
                    case 8: {
                        c.mWaveVariesBy = a.getInt(attr, c.mWaveVariesBy);
                        break;
                    }
                    case 9: {
                        c.mAlpha = a.getFloat(attr, c.mAlpha);
                        break;
                    }
                    case 10: {
                        c.mElevation = a.getDimension(attr, c.mElevation);
                        break;
                    }
                    case 11: {
                        c.mRotation = a.getFloat(attr, c.mRotation);
                        break;
                    }
                    case 12: {
                        c.mRotationX = a.getFloat(attr, c.mRotationX);
                        break;
                    }
                    case 13: {
                        c.mRotationY = a.getFloat(attr, c.mRotationY);
                        break;
                    }
                    case 14: {
                        c.mTransitionPathRotate = a.getFloat(attr, c.mTransitionPathRotate);
                        break;
                    }
                    case 15: {
                        c.mScaleX = a.getFloat(attr, c.mScaleX);
                        break;
                    }
                    case 16: {
                        c.mScaleY = a.getFloat(attr, c.mScaleY);
                        break;
                    }
                    case 17: {
                        c.mTranslationX = a.getDimension(attr, c.mTranslationX);
                        break;
                    }
                    case 18: {
                        c.mTranslationY = a.getDimension(attr, c.mTranslationY);
                        break;
                    }
                    case 19: {
                        if (Build.VERSION.SDK_INT >= 21) {
                            c.mTranslationZ = a.getDimension(attr, c.mTranslationZ);
                            break;
                        }
                        break;
                    }
                    case 20: {
                        c.mProgress = a.getFloat(attr, c.mProgress);
                        break;
                    }
                    default: {
                        Log.e("KeyCycle", "unused attribute 0x" + Integer.toHexString(attr) + "   " + Loader.mAttrMap.get(attr));
                        break;
                    }
                }
            }
        }
        
        static {
            (Loader.mAttrMap = new SparseIntArray()).append(R.styleable.KeyCycle_motionTarget, 1);
            Loader.mAttrMap.append(R.styleable.KeyCycle_framePosition, 2);
            Loader.mAttrMap.append(R.styleable.KeyCycle_transitionEasing, 3);
            Loader.mAttrMap.append(R.styleable.KeyCycle_curveFit, 4);
            Loader.mAttrMap.append(R.styleable.KeyCycle_waveShape, 5);
            Loader.mAttrMap.append(R.styleable.KeyCycle_wavePeriod, 6);
            Loader.mAttrMap.append(R.styleable.KeyCycle_waveOffset, 7);
            Loader.mAttrMap.append(R.styleable.KeyCycle_waveVariesBy, 8);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_alpha, 9);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_elevation, 10);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_rotation, 11);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_rotationX, 12);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_rotationY, 13);
            Loader.mAttrMap.append(R.styleable.KeyCycle_transitionPathRotate, 14);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_scaleX, 15);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_scaleY, 16);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_translationX, 17);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_translationY, 18);
            Loader.mAttrMap.append(R.styleable.KeyCycle_android_translationZ, 19);
            Loader.mAttrMap.append(R.styleable.KeyCycle_motionProgress, 20);
        }
    }
}
