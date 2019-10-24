package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.motion.utils.*;
import java.text.*;
import android.view.*;
import com.smart.library.support.constraint.*;
import android.util.*;
import android.os.*;
import java.lang.reflect.*;

public abstract class TimeCycleSplineSet
{
    private static final String TAG = "SplineSet";
    protected CurveFit mCurveFit;
    protected int mWaveShape;
    protected int[] mTimePoints;
    protected float[][] mValues;
    private int count;
    private String mType;
    private float[] mCache;
    private static final int CURVE_VALUE = 0;
    private static final int CURVE_PERIOD = 1;
    private static final int CURVE_OFFSET = 2;
    private static float VAL_2PI;
    protected boolean mContinue;
    long last_time;
    float last_cycle;
    
    public TimeCycleSplineSet() {
        this.mWaveShape = 0;
        this.mTimePoints = new int[10];
        this.mValues = new float[10][3];
        this.mCache = new float[3];
        this.mContinue = false;
        this.last_cycle = Float.NaN;
    }
    
    @Override
    public String toString() {
        String str = this.mType;
        final DecimalFormat df = new DecimalFormat("##.##");
        for (int i = 0; i < this.count; ++i) {
            str = str + "[" + this.mTimePoints[i] + " , " + df.format(this.mValues[i]) + "] ";
        }
        return str;
    }
    
    public void setType(final String type) {
        this.mType = type;
    }
    
    public abstract boolean setProperty(final View p0, final float p1, final long p2, final KeyCache p3);
    
    public float get(final float pos, final long time, final View view, final KeyCache cache) {
        this.mCurveFit.getPos(pos, this.mCache);
        final float period = this.mCache[1];
        if (period == 0.0f) {
            this.mContinue = false;
            return this.mCache[2];
        }
        if (Float.isNaN(this.last_cycle)) {
            this.last_cycle = cache.getFloatValue(view, this.mType, 0);
            if (Float.isNaN(this.last_cycle)) {
                this.last_cycle = 0.0f;
            }
        }
        final long delta_time = time - this.last_time;
        this.last_cycle = (float)((this.last_cycle + delta_time * 1.0E-9 * period) % 1.0);
        cache.setFloatValue(view, this.mType, 0, this.last_cycle);
        this.last_time = time;
        final float v = this.mCache[0];
        final float wave = this.calcWave(this.last_cycle);
        final float offset = this.mCache[2];
        final float value = v * wave + offset;
        this.mContinue = (v != 0.0f || period != 0.0f);
        return value;
    }
    
    protected float calcWave(final float period) {
        switch (this.mWaveShape) {
            default: {
                return (float)Math.sin(period * TimeCycleSplineSet.VAL_2PI);
            }
            case 1: {
                return Math.signum(period * TimeCycleSplineSet.VAL_2PI);
            }
            case 2: {
                return 1.0f - Math.abs(period);
            }
            case 3: {
                return (period * 2.0f + 1.0f) % 2.0f - 1.0f;
            }
            case 4: {
                return 1.0f - (period * 2.0f + 1.0f) % 2.0f;
            }
            case 5: {
                return (float)Math.cos(period * TimeCycleSplineSet.VAL_2PI);
            }
            case 6: {
                final float x = 1.0f - Math.abs(period * 4.0f % 4.0f - 2.0f);
                return 1.0f - x * x;
            }
        }
    }
    
    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }
    
    static TimeCycleSplineSet makeCustomSpline(final String str, final SparseArray<ConstraintAttribute> attrList) {
        return new CustomSet(str, attrList);
    }
    
    static TimeCycleSplineSet makeSpline(final String str, final long currentTime) {
        TimeCycleSplineSet timeCycle = null;
        switch (str) {
            case "alpha": {
                timeCycle = new AlphaSet();
                break;
            }
            case "elevation": {
                timeCycle = new ElevationSet();
                break;
            }
            case "rotation": {
                timeCycle = new RotationSet();
                break;
            }
            case "rotationX": {
                timeCycle = new RotationXset();
                break;
            }
            case "rotationY": {
                timeCycle = new RotationYset();
                break;
            }
            case "transitionPathRotate": {
                timeCycle = new PathRotate();
                break;
            }
            case "scaleX": {
                timeCycle = new ScaleXset();
                break;
            }
            case "scaleY": {
                timeCycle = new ScaleYset();
                break;
            }
            case "translationX": {
                timeCycle = new TranslationXset();
                break;
            }
            case "translationY": {
                timeCycle = new TranslationYset();
                break;
            }
            case "translationZ": {
                timeCycle = new TranslationZset();
                break;
            }
            case "progress": {
                timeCycle = new ProgressSet();
                break;
            }
            default: {
                return null;
            }
        }
        timeCycle.setStartTime(currentTime);
        return timeCycle;
    }
    
    protected void setStartTime(final long currentTime) {
        this.last_time = currentTime;
    }
    
    public void setPoint(final int position, final float value, final float period, final int shape, final float offset) {
        this.mTimePoints[this.count] = position;
        this.mValues[this.count][0] = value;
        this.mValues[this.count][1] = period;
        this.mValues[this.count][2] = offset;
        this.mWaveShape = Math.max(this.mWaveShape, shape);
        ++this.count;
    }
    
    public void setup(final int curveType) {
        if (this.count == 0) {
            Log.e("SplineSet", "Error no points added to " + this.mType);
            return;
        }
        Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, this.count - 1);
        int unique = 0;
        for (int i = 1; i < this.mTimePoints.length; ++i) {
            if (this.mTimePoints[i] != this.mTimePoints[i - 1]) {
                ++unique;
            }
        }
        final double[] time = new double[unique];
        final double[][] values = new double[unique][3];
        int k = 0;
        for (int j = 0; j < this.count; ++j) {
            if (j <= 0 || this.mTimePoints[j] != this.mTimePoints[j - 1]) {
                time[k] = this.mTimePoints[j] * 0.01;
                values[k][0] = this.mValues[j][0];
                values[k][1] = this.mValues[j][1];
                values[k][2] = this.mValues[j][2];
                ++k;
            }
        }
        this.mCurveFit = CurveFit.get(curveType, time, values);
    }
    
    static {
        TimeCycleSplineSet.VAL_2PI = 6.2831855f;
    }
    
    static class ElevationSet extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(this.get(t, time, view, cache));
            }
            return this.mContinue;
        }
    }
    
    static class AlphaSet extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setAlpha(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class RotationSet extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setRotation(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class RotationXset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setRotationX(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class RotationYset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setRotationY(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class PathRotate extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            return this.mContinue;
        }
        
        public boolean setPathRotate(final View view, final KeyCache cache, final float t, final long time, final double dx, final double dy) {
            view.setRotation(this.get(t, time, view, cache) + (float)Math.toDegrees(Math.atan2(dy, dx)));
            return this.mContinue;
        }
    }
    
    static class ScaleXset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setScaleX(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class ScaleYset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setScaleY(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class TranslationXset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setTranslationX(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class TranslationYset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            view.setTranslationY(this.get(t, time, view, cache));
            return this.mContinue;
        }
    }
    
    static class TranslationZset extends TimeCycleSplineSet
    {
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(this.get(t, time, view, cache));
            }
            return this.mContinue;
        }
    }
    
    static class CustomSet extends TimeCycleSplineSet
    {
        String mAttributeName;
        SparseArray<ConstraintAttribute> mConstraintAttributeList;
        SparseArray<float[]> mWaveProperties;
        float[] mTempValues;
        float[] mCache;
        
        public CustomSet(final String attribute, final SparseArray<ConstraintAttribute> attrList) {
            this.mWaveProperties = (SparseArray<float[]>)new SparseArray();
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }
        
        @Override
        public void setup(final int curveType) {
            final int size = this.mConstraintAttributeList.size();
            final int dimensionality = ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).noOfInterpValues();
            final double[] time = new double[size];
            this.mTempValues = new float[dimensionality + 2];
            this.mCache = new float[dimensionality];
            final double[][] values = new double[size][dimensionality + 2];
            for (int i = 0; i < size; ++i) {
                final int key = this.mConstraintAttributeList.keyAt(i);
                final ConstraintAttribute ca = (ConstraintAttribute)this.mConstraintAttributeList.valueAt(i);
                final float[] waveProp = (float[])this.mWaveProperties.valueAt(i);
                time[i] = key * 0.01;
                ca.getValuesToInterpolate(this.mTempValues);
                for (int k = 0; k < this.mTempValues.length; ++k) {
                    values[i][k] = this.mTempValues[k];
                }
                values[i][dimensionality] = waveProp[0];
                values[i][dimensionality + 1] = waveProp[1];
            }
            this.mCurveFit = CurveFit.get(curveType, time, values);
        }
        
        @Override
        public void setPoint(final int position, final float value, final float period, final int shape, final float offset) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }
        
        public void setPoint(final int position, final ConstraintAttribute value, final float period, final int shape, final float offset) {
            this.mConstraintAttributeList.append(position,value);
            this.mWaveProperties.append(position,new float[] { period, offset });
            this.mWaveShape = Math.max(this.mWaveShape, shape);
        }
        
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            this.mCurveFit.getPos(t, this.mTempValues);
            final float period = this.mTempValues[this.mTempValues.length - 2];
            final float offset = this.mTempValues[this.mTempValues.length - 1];
            final long delta_time = time - this.last_time;
            this.last_cycle = (float)((this.last_cycle + delta_time * 1.0E-9 * period) % 1.0);
            this.last_time = time;
            final float wave = this.calcWave(this.last_cycle);
            this.mContinue = false;
            for (int i = 0; i < this.mCache.length; ++i) {
                this.mContinue |= (this.mTempValues[i] != 0.0);
                this.mCache[i] = this.mTempValues[i] * wave + offset;
            }
            ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).setInterpolatedValue(view, this.mCache);
            if (period != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }
    }
    
    static class ProgressSet extends TimeCycleSplineSet
    {
        boolean mNoMethod;
        
        ProgressSet() {
            this.mNoMethod = false;
        }
        
        @Override
        public boolean setProperty(final View view, final float t, final long time, final KeyCache cache) {
            if (view instanceof MotionLayout) {
                ((MotionLayout)view).setProgress(this.get(t, time, view, cache));
            }
            else {
                if (this.mNoMethod) {
                    return false;
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
                        method.invoke(view, this.get(t, time, view, cache));
                    }
                    catch (IllegalAccessException e) {
                        Log.e("SplineSet", "unable to setProgress", (Throwable)e);
                    }
                    catch (InvocationTargetException e2) {
                        Log.e("SplineSet", "unable to setProgress", (Throwable)e2);
                    }
                }
            }
            return this.mContinue;
        }
    }
    
    private static class Sort
    {
        static void doubleQuickSort(final int[] key, final float[][] value, int low, int hi) {
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
        
        private static int partition(final int[] array, final float[][] value, final int low, final int hi) {
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
        
        private static void swap(final int[] array, final float[][] value, final int a, final int b) {
            final int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            final float[] tmpv = value[a];
            value[a] = value[b];
            value[b] = tmpv;
        }
    }
}
