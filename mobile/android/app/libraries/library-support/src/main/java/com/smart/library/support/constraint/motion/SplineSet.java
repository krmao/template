package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.motion.utils.*;
import java.text.*;
import android.view.*;
import com.smart.library.support.constraint.*;
import java.util.*;
import android.os.*;
import android.util.*;
import java.lang.reflect.*;

public abstract class SplineSet
{
    private static final String TAG = "SplineSet";
    protected CurveFit mCurveFit;
    protected int[] mTimePoints;
    protected float[] mValues;
    private int count;
    private String mType;
    
    public SplineSet() {
        this.mTimePoints = new int[10];
        this.mValues = new float[10];
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
    
    public abstract void setProperty(final View p0, final float p1);
    
    public float get(final float t) {
        return (float)this.mCurveFit.getPos(t, 0);
    }
    
    public float getSlope(final float t) {
        return (float)this.mCurveFit.getSlope(t, 0);
    }
    
    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }
    
    static SplineSet makeCustomSpline(final String str, final SparseArray<ConstraintAttribute> attrList) {
        return new CustomSet(str, attrList);
    }
    
    static SplineSet makeSpline(final String str) {
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
                return new PathRotate();
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
    
    public void setPoint(final int position, final float value) {
        if (this.mTimePoints.length < this.count + 1) {
            this.mTimePoints = Arrays.copyOf(this.mTimePoints, this.mTimePoints.length * 2);
            this.mValues = Arrays.copyOf(this.mValues, this.mValues.length * 2);
        }
        this.mTimePoints[this.count] = position;
        this.mValues[this.count] = value;
        ++this.count;
    }
    
    public void setup(final int curveType) {
        if (this.count == 0) {
            return;
        }
        Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, this.count - 1);
        int unique = 1;
        for (int i = 1; i < this.count; ++i) {
            if (this.mTimePoints[i - 1] != this.mTimePoints[i]) {
                ++unique;
            }
        }
        final double[] time = new double[unique];
        final double[][] values = new double[unique][1];
        int k = 0;
        for (int j = 0; j < this.count; ++j) {
            if (j <= 0 || this.mTimePoints[j] != this.mTimePoints[j - 1]) {
                time[k] = this.mTimePoints[j] * 0.01;
                values[k][0] = this.mValues[j];
                ++k;
            }
        }
        this.mCurveFit = CurveFit.get(curveType, time, values);
    }
    
    static class ElevationSet extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(this.get(t));
            }
        }
    }
    
    static class AlphaSet extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setAlpha(this.get(t));
        }
    }
    
    static class RotationSet extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotation(this.get(t));
        }
    }
    
    static class RotationXset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotationX(this.get(t));
        }
    }
    
    static class RotationYset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setRotationY(this.get(t));
        }
    }
    
    static class PathRotate extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
        }
        
        public void setPathRotate(final View view, final float t, final double dx, final double dy) {
            view.setRotation(this.get(t) + (float)Math.toDegrees(Math.atan2(dy, dx)));
        }
    }
    
    static class ScaleXset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setScaleX(this.get(t));
        }
    }
    
    static class ScaleYset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setScaleY(this.get(t));
        }
    }
    
    static class TranslationXset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setTranslationX(this.get(t));
        }
    }
    
    static class TranslationYset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            view.setTranslationY(this.get(t));
        }
    }
    
    static class TranslationZset extends SplineSet
    {
        @Override
        public void setProperty(final View view, final float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(this.get(t));
            }
        }
    }
    
    static class CustomSet extends SplineSet
    {
        String mAttributeName;
        SparseArray<ConstraintAttribute> mConstraintAttributeList;
        float[] mTempValues;
        
        public CustomSet(final String attribute, final SparseArray<ConstraintAttribute> attrList) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }
        
        @Override
        public void setup(final int curveType) {
            final int size = this.mConstraintAttributeList.size();
            final int dimensionality = ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).noOfInterpValues();
            final double[] time = new double[size];
            this.mTempValues = new float[dimensionality];
            final double[][] values = new double[size][dimensionality];
            for (int i = 0; i < size; ++i) {
                final int key = this.mConstraintAttributeList.keyAt(i);
                final ConstraintAttribute ca = (ConstraintAttribute)this.mConstraintAttributeList.valueAt(i);
                time[i] = key * 0.01;
                ca.getValuesToInterpolate(this.mTempValues);
                for (int k = 0; k < this.mTempValues.length; ++k) {
                    values[i][k] = this.mTempValues[k];
                }
            }
            this.mCurveFit = CurveFit.get(curveType, time, values);
        }
        
        @Override
        public void setPoint(final int position, final float value) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }
        
        public void setPoint(final int position, final ConstraintAttribute value) {
            this.mConstraintAttributeList.append(position,value);
        }
        
        @Override
        public void setProperty(final View view, final float t) {
            this.mCurveFit.getPos(t, this.mTempValues);
            ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).setInterpolatedValue(view, this.mTempValues);
        }
    }
    
    static class ProgressSet extends SplineSet
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
                        Log.e("SplineSet", "unable to setProgress", (Throwable)e);
                    }
                    catch (InvocationTargetException e2) {
                        Log.e("SplineSet", "unable to setProgress", (Throwable)e2);
                    }
                }
            }
        }
    }
    
    private static class Sort
    {
        static void doubleQuickSort(final int[] key, final float[] value, int low, int hi) {
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
}
