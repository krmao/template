package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.*;
import java.text.*;
import android.view.*;
import android.annotation.*;
import android.os.*;
import android.util.*;
import java.lang.reflect.*;
import com.smart.library.support.constraint.motion.utils.*;
import java.util.*;

public abstract class KeyCycleOscillator
{
    private static final String TAG = "KeyCycleOscillator";
    private CurveFit mCurveFit;
    private CycleOscillator mCycleOscillator;
    protected ConstraintAttribute mCustom;
    private String mType;
    private int mWaveShape;
    public int mVariesBy;
    ArrayList<WavePoint> mWavePoints;
    
    public KeyCycleOscillator() {
        this.mWaveShape = 0;
        this.mVariesBy = 0;
        this.mWavePoints = new ArrayList<WavePoint>();
    }
    
    public boolean variesByPath() {
        return this.mVariesBy == 1;
    }
    
    @Override
    public String toString() {
        String str = this.mType;
        final DecimalFormat df = new DecimalFormat("##.##");
        for (final WavePoint wp : this.mWavePoints) {
            str = str + "[" + wp.mPosition + " , " + df.format(wp.mValue) + "] ";
        }
        return str;
    }
    
    public void setType(final String type) {
        this.mType = type;
    }
    
    public abstract void setProperty(final View p0, final float p1);
    
    public float get(final float t) {
        return (float)this.mCycleOscillator.getValues(t);
    }
    
    public float getSlope(final float position) {
        return (float)this.mCycleOscillator.getSlope(position);
    }
    
    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }
    
    static KeyCycleOscillator makeSpline(final String str) {
        if (str.startsWith("CUSTOM")) {
            return new CustomSet();
        }
        switch (str) {
            case "alpha": {
                return new AlphaSet();
            }
            case "elevation": {
                return new ElevationSet();
            }
            case "rotation": {
                return new RotationSet();
            }
            case "rotationX": {
                return new RotationXset();
            }
            case "rotationY": {
                return new RotationYset();
            }
            case "transitionPathRotate": {
                return new PathRotateSet();
            }
            case "scaleX": {
                return new ScaleXset();
            }
            case "scaleY": {
                return new ScaleYset();
            }
            case "waveOffset": {
                return new AlphaSet();
            }
            case "waveVariesBy": {
                return new AlphaSet();
            }
            case "translationX": {
                return new TranslationXset();
            }
            case "translationY": {
                return new TranslationYset();
            }
            case "translationZ": {
                return new TranslationZset();
            }
            case "progress": {
                return new ProgressSet();
            }
            default: {
                return null;
            }
        }
    }
    
    public void setPoint(final int framePosition, final int shape, final int variesBy, final float period, final float offset, final float value, final ConstraintAttribute custom) {
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            this.mVariesBy = variesBy;
        }
        this.mWaveShape = shape;
        this.mCustom = custom;
    }
    
    public void setPoint(final int framePosition, final int shape, final int variesBy, final float period, final float offset, final float value) {
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            this.mVariesBy = variesBy;
        }
        this.mWaveShape = shape;
    }
    
    @TargetApi(19)
    public void setup(final float pathLength) {
        final int count = this.mWavePoints.size();
        if (count == 0) {
            return;
        }
        Collections.sort(this.mWavePoints, new Comparator<WavePoint>() {
            @Override
            public int compare(final WavePoint lhs, final WavePoint rhs) {
                return Integer.compare(lhs.mPosition, rhs.mPosition);
            }
        });
        final double[] time = new double[count];
        final double[][] values = new double[count][2];
        this.mCycleOscillator = new CycleOscillator(this.mWaveShape, this.mVariesBy, count);
        int i = 0;
        for (final WavePoint wp : this.mWavePoints) {
            time[i] = wp.mPeriod * 0.01;
            values[i][0] = wp.mValue;
            values[i][1] = wp.mOffset;
            this.mCycleOscillator.setPoint(i, wp.mPosition, wp.mPeriod, wp.mOffset, wp.mValue);
            ++i;
        }
        this.mCycleOscillator.setup(pathLength);
        this.mCurveFit = CurveFit.get(0, time, values);
    }
    
    static class WavePoint
    {
        int mPosition;
        float mValue;
        float mOffset;
        float mPeriod;
        
        public WavePoint(final int position, final float period, final float offset, final float value) {
            this.mPosition = position;
            this.mValue = value;
            this.mOffset = offset;
            this.mPeriod = period;
        }
    }
    
    static class ElevationSet extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(this.get(t));
            }
        }
    }
    
    static class AlphaSet extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setAlpha(this.get(t));
        }
    }
    
    static class RotationSet extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotation(this.get(t));
        }
    }
    
    static class RotationXset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotationX(this.get(t));
        }
    }
    
    static class RotationYset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotationY(this.get(t));
        }
    }
    
    static class PathRotateSet extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
        }
        
        public void setPathRotate(final View view, final float t, final double dx, final double dy) {
            view.setRotation(this.get(t) + (float)Math.toDegrees(Math.atan2(dy, dx)));
        }
    }
    
    static class ScaleXset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setScaleX(this.get(t));
        }
    }
    
    static class ScaleYset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setScaleY(this.get(t));
        }
    }
    
    static class TranslationXset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setTranslationX(this.get(t));
        }
    }
    
    static class TranslationYset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setTranslationY(this.get(t));
        }
    }
    
    static class TranslationZset extends KeyCycleOscillator
    {
        @Override
        public void setProperty(final View view, final float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(this.get(t));
            }
        }
    }
    
    static class CustomSet extends KeyCycleOscillator
    {
        float[] value;
        
        CustomSet() {
            this.value = new float[1];
        }
        
        @Override
        public void setProperty(final View view, final float t) {
            this.value[0] = this.get(t);
            this.mCustom.setInterpolatedValue(view, this.value);
        }
    }
    
    static class ProgressSet extends KeyCycleOscillator
    {
        boolean mNoMethod;
        
        ProgressSet() {
            this.mNoMethod = false;
        }
        
        @Override
        public void setProperty(final View view, final float t) {
            if (view instanceof MotionLayout) {
                ((MotionLayout)view).setProgress(this.get(t));
            }
            else {
                if (this.mNoMethod) {
                    return;
                }
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                }
                catch (NoSuchMethodException e3) {
                    this.mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, this.get(t));
                    }
                    catch (IllegalAccessException e) {
                        Log.e("KeyCycleOscillator", "unable to setProgress", (Throwable)e);
                    }
                    catch (InvocationTargetException e2) {
                        Log.e("KeyCycleOscillator", "unable to setProgress", (Throwable)e2);
                    }
                }
            }
        }
    }
    
    private static class IntDoubleSort
    {
        static void sort(final int[] key, final float[] value, int low, int hi) {
            final int[] stack = new int[key.length + 10];
            int count = 0;
            stack[count++] = hi;
            stack[count++] = low;
            while (count > 0) {
                low = stack[--count];
                hi = stack[--count];
                if (low < hi) {
                    final int p = partition(key, value, low, hi);
                    stack[count++] = p - 1;
                    stack[count++] = low;
                    stack[count++] = hi;
                    stack[count++] = p + 1;
                }
            }
        }
        
        private static int partition(final int[] array, final float[] value, final int low, final int hi) {
            final int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; ++j) {
                if (array[j] <= pivot) {
                    swap(array, value, i, j);
                    ++i;
                }
            }
            swap(array, value, i, hi);
            return i;
        }
        
        private static void swap(final int[] array, final float[] value, final int a, final int b) {
            final int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            final float tmpv = value[a];
            value[a] = value[b];
            value[b] = tmpv;
        }
    }
    
    private static class IntFloatFloatSort
    {
        static void sort(final int[] key, final float[] value1, final float[] value2, int low, int hi) {
            final int[] stack = new int[key.length + 10];
            int count = 0;
            stack[count++] = hi;
            stack[count++] = low;
            while (count > 0) {
                low = stack[--count];
                hi = stack[--count];
                if (low < hi) {
                    final int p = partition(key, value1, value2, low, hi);
                    stack[count++] = p - 1;
                    stack[count++] = low;
                    stack[count++] = hi;
                    stack[count++] = p + 1;
                }
            }
        }
        
        private static int partition(final int[] array, final float[] value1, final float[] value2, final int low, final int hi) {
            final int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; ++j) {
                if (array[j] <= pivot) {
                    swap(array, value1, value2, i, j);
                    ++i;
                }
            }
            swap(array, value1, value2, i, hi);
            return i;
        }
        
        private static void swap(final int[] array, final float[] value1, final float[] value2, final int a, final int b) {
            final int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            float tmpFloat = value1[a];
            value1[a] = value1[b];
            value1[b] = tmpFloat;
            tmpFloat = value2[a];
            value2[a] = value2[b];
            value2[b] = tmpFloat;
        }
    }
    
    static class CycleOscillator
    {
        static final int UNSET = -1;
        private static final String TAG = "CycleOscillator";
        private final int mVariesBy;
        Oscillator mOscillator;
        float[] mValues;
        double[] mPosition;
        float[] mPeriod;
        float[] mOffset;
        float[] mScale;
        int mWaveShape;
        CurveFit mCurveFit;
        double[] mSplineValueCache;
        double[] mSplineSlopeCache;
        float mPathLength;
        public HashMap<String, ConstraintAttribute> mCustomConstraints;
        
        CycleOscillator(final int waveShape, final int variesBy, final int steps) {
            this.mOscillator = new Oscillator();
            this.mCustomConstraints = new HashMap<String, ConstraintAttribute>();
            this.mWaveShape = waveShape;
            this.mVariesBy = variesBy;
            this.mOscillator.setType(waveShape);
            this.mValues = new float[steps];
            this.mPosition = new double[steps];
            this.mPeriod = new float[steps];
            this.mOffset = new float[steps];
            this.mScale = new float[steps];
        }
        
        public double getValues(final float time) {
            if (this.mCurveFit != null) {
                this.mCurveFit.getPos(time, this.mSplineValueCache);
            }
            else {
                this.mSplineValueCache[0] = this.mOffset[0];
                this.mSplineValueCache[1] = this.mValues[0];
            }
            final double offset = this.mSplineValueCache[0];
            final double waveValue = this.mOscillator.getValue(time);
            return offset + waveValue * this.mSplineValueCache[1];
        }
        
        public double getSlope(final float time) {
            if (this.mCurveFit != null) {
                this.mCurveFit.getSlope(time, this.mSplineSlopeCache);
                this.mCurveFit.getPos(time, this.mSplineValueCache);
            }
            else {
                this.mSplineSlopeCache[0] = 0.0;
                this.mSplineSlopeCache[1] = 0.0;
            }
            final double waveValue = this.mOscillator.getValue(time);
            final double waveSlope = this.mOscillator.getSlope(time);
            return this.mSplineSlopeCache[0] + waveValue * this.mSplineSlopeCache[1] + waveSlope * this.mSplineValueCache[1];
        }
        
        private ConstraintAttribute get(final String attributeName, final ConstraintAttribute.AttributeType attributeType) {
            ConstraintAttribute ret;
            if (this.mCustomConstraints.containsKey(attributeName)) {
                ret = this.mCustomConstraints.get(attributeName);
                if (ret.getType() != attributeType) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + ret.getType().name());
                }
            }
            else {
                ret = new ConstraintAttribute(attributeName, attributeType);
                this.mCustomConstraints.put(attributeName, ret);
            }
            return ret;
        }
        
        public void setPoint(final int index, final int framePosition, final float wavePeriod, final float offset, final float values) {
            this.mPosition[index] = framePosition / 100.0;
            this.mPeriod[index] = wavePeriod;
            this.mOffset[index] = offset;
            this.mValues[index] = values;
        }
        
        public void setup(final float pathLength) {
            this.mPathLength = pathLength;
            final double[][] splineValues = new double[this.mPosition.length][2];
            this.mSplineValueCache = new double[1 + this.mValues.length];
            this.mSplineSlopeCache = new double[1 + this.mValues.length];
            if (this.mPosition[0] > 0.0) {
                this.mOscillator.addPoint(0.0, this.mPeriod[0]);
            }
            final int last = this.mPosition.length - 1;
            if (this.mPosition[last] < 1.0) {
                this.mOscillator.addPoint(1.0, this.mPeriod[last]);
            }
            for (int i = 0; i < splineValues.length; ++i) {
                splineValues[i][0] = this.mOffset[i];
                for (int j = 0; j < this.mValues.length; ++j) {
                    splineValues[j][1] = this.mValues[j];
                }
                this.mOscillator.addPoint(this.mPosition[i], this.mPeriod[i]);
            }
            this.mOscillator.normalize();
            if (this.mPosition.length > 1) {
                this.mCurveFit = CurveFit.get(0, this.mPosition, splineValues);
            }
            else {
                this.mCurveFit = null;
            }
        }
    }
}
