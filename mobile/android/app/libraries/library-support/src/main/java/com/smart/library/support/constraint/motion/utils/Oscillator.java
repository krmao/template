package com.smart.library.support.constraint.motion.utils;

import java.util.*;

public class Oscillator
{
    public static String TAG;
    float[] mPeriod;
    double[] mPosition;
    double[] mArea;
    public static final int SIN_WAVE = 0;
    public static final int SQUARE_WAVE = 1;
    public static final int TRIANGLE_WAVE = 2;
    public static final int SAW_WAVE = 3;
    public static final int REVERSE_SAW_WAVE = 4;
    public static final int COS_WAVE = 5;
    public static final int BOUNCE = 6;
    int mType;
    double PI2;
    private boolean mNormalized;
    
    public Oscillator() {
        this.mPeriod = new float[0];
        this.mPosition = new double[0];
        this.PI2 = 6.283185307179586;
        this.mNormalized = false;
    }
    
    @Override
    public String toString() {
        return "pos =" + Arrays.toString(this.mPosition) + " period=" + Arrays.toString(this.mPeriod);
    }
    
    public void setType(final int type) {
        this.mType = type;
    }
    
    public void addPoint(final double position, final float period) {
        final int len = this.mPeriod.length + 1;
        int j = Arrays.binarySearch(this.mPosition, position);
        if (j < 0) {
            j = -j - 1;
        }
        this.mPosition = Arrays.copyOf(this.mPosition, len);
        this.mPeriod = Arrays.copyOf(this.mPeriod, len);
        this.mArea = new double[len];
        System.arraycopy(this.mPosition, j, this.mPosition, j + 1, len - j - 1);
        this.mPosition[j] = position;
        this.mPeriod[j] = period;
        this.mNormalized = false;
    }
    
    public void normalize() {
        double totalArea = 0.0;
        double totalCount = 0.0;
        for (int i = 0; i < this.mPeriod.length; ++i) {
            totalCount += this.mPeriod[i];
        }
        for (int i = 1; i < this.mPeriod.length; ++i) {
            final float h = (this.mPeriod[i - 1] + this.mPeriod[i]) / 2.0f;
            final double w = this.mPosition[i] - this.mPosition[i - 1];
            totalArea += w * h;
        }
        for (int i = 0; i < this.mPeriod.length; ++i) {
            final float[] mPeriod = this.mPeriod;
            final int n = i;
            mPeriod[n] *= (float)(totalCount / totalArea);
        }
        this.mArea[0] = 0.0;
        for (int i = 1; i < this.mPeriod.length; ++i) {
            final float h = (this.mPeriod[i - 1] + this.mPeriod[i]) / 2.0f;
            final double w = this.mPosition[i] - this.mPosition[i - 1];
            this.mArea[i] = this.mArea[i - 1] + w * h;
        }
        this.mNormalized = true;
    }
    
    double getP(double time) {
        if (time < 0.0) {
            time = 0.0;
        }
        else if (time > 1.0) {
            time = 1.0;
        }
        int index = Arrays.binarySearch(this.mPosition, time);
        double p = 0.0;
        if (index > 0) {
            p = 1.0;
        }
        else if (index != 0) {
            index = -index - 1;
            final double t = time;
            final double m = (this.mPeriod[index] - this.mPeriod[index - 1]) / (this.mPosition[index] - this.mPosition[index - 1]);
            p = this.mArea[index - 1] + (this.mPeriod[index - 1] - m * this.mPosition[index - 1]) * (t - this.mPosition[index - 1]) + m * (t * t - this.mPosition[index - 1] * this.mPosition[index - 1]) / 2.0;
        }
        return p;
    }
    
    public double getValue(final double time) {
        switch (this.mType) {
            default: {
                return Math.sin(this.PI2 * this.getP(time));
            }
            case 1: {
                return Math.signum(0.5 - this.getP(time) % 1.0);
            }
            case 2: {
                return 1.0 - Math.abs((this.getP(time) * 4.0 + 1.0) % 4.0 - 2.0);
            }
            case 3: {
                return (this.getP(time) * 2.0 + 1.0) % 2.0 - 1.0;
            }
            case 4: {
                return 1.0 - (this.getP(time) * 2.0 + 1.0) % 2.0;
            }
            case 5: {
                return Math.cos(this.PI2 * this.getP(time));
            }
            case 6: {
                final double x = 1.0 - Math.abs(this.getP(time) * 4.0 % 4.0 - 2.0);
                return 1.0 - x * x;
            }
        }
    }
    
    double getDP(double time) {
        if (time <= 0.0) {
            time = 1.0E-5;
        }
        else if (time >= 1.0) {
            time = 0.999999;
        }
        int index = Arrays.binarySearch(this.mPosition, time);
        double p = 0.0;
        if (index > 0) {
            return 0.0;
        }
        if (index != 0) {
            index = -index - 1;
            final double t = time;
            final double m = (this.mPeriod[index] - this.mPeriod[index - 1]) / (this.mPosition[index] - this.mPosition[index - 1]);
            p = m * t + (this.mPeriod[index - 1] - m * this.mPosition[index - 1]);
        }
        return p;
    }
    
    public double getSlope(final double time) {
        switch (this.mType) {
            default: {
                return this.PI2 * this.getDP(time) * Math.cos(this.PI2 * this.getP(time));
            }
            case 1: {
                return 0.0;
            }
            case 2: {
                return 4.0 * this.getDP(time) * Math.signum((this.getP(time) * 4.0 + 3.0) % 4.0 - 2.0);
            }
            case 3: {
                return this.getDP(time) * 2.0;
            }
            case 4: {
                return -this.getDP(time) * 2.0;
            }
            case 5: {
                return -this.PI2 * this.getDP(time) * Math.sin(this.PI2 * this.getP(time));
            }
            case 6: {
                return 4.0 * this.getDP(time) * ((this.getP(time) * 4.0 + 2.0) % 4.0 - 2.0);
            }
        }
    }
    
    static {
        Oscillator.TAG = "Oscillator";
    }
}
