package com.smart.library.support.constraint.motion.utils;

public abstract class CurveFit
{
    public static final int SPLINE = 0;
    public static final int LINEAR = 1;
    public static final int CONSTANT = 2;
    
    public static CurveFit get(int type, final double[] time, final double[][] y) {
        if (time.length == 1) {
            type = 2;
        }
        switch (type) {
            case 0: {
                return new MonotonicCurveFit(time, y);
            }
            case 2: {
                return new Constant(time[0], y[0]);
            }
            default: {
                return new LinearCurveFit(time, y);
            }
        }
    }
    
    public static CurveFit getArc(final int[] arcModes, final double[] time, final double[][] y) {
        return new ArcCurveFit(arcModes, time, y);
    }
    
    public abstract void getPos(final double p0, final double[] p1);
    
    public abstract void getPos(final double p0, final float[] p1);
    
    public abstract double getPos(final double p0, final int p1);
    
    public abstract void getSlope(final double p0, final double[] p1);
    
    public abstract double getSlope(final double p0, final int p1);
    
    public abstract double[] getTimePoints();
    
    static class Constant extends CurveFit
    {
        double mTime;
        double[] mValue;
        
        Constant(final double time, final double[] value) {
            this.mTime = time;
            this.mValue = value;
        }
        
        @Override
        public void getPos(final double t, final double[] v) {
            System.arraycopy(this.mValue, 0, v, 0, this.mValue.length);
        }
        
        @Override
        public void getPos(final double t, final float[] v) {
            for (int i = 0; i < this.mValue.length; ++i) {
                v[i] = (float)this.mValue[i];
            }
        }
        
        @Override
        public double getPos(final double t, final int j) {
            return this.mValue[j];
        }
        
        @Override
        public void getSlope(final double t, final double[] v) {
            for (int i = 0; i < this.mValue.length; ++i) {
                v[i] = 0.0;
            }
        }
        
        @Override
        public double getSlope(final double t, final int j) {
            return 0.0;
        }
        
        @Override
        public double[] getTimePoints() {
            return new double[] { this.mTime };
        }
    }
}
