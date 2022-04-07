package com.smart.library.support.constraint.motion.utils;

public class HyperSpline
{
    int mPoints;
    Cubic[][] mCurve;
    int mDimensionality;
    double[] mCurveLength;
    double mTotalLength;
    double[][] mCtl;
    
    public HyperSpline(final double[][] points) {
        this.setup(points);
    }
    
    public HyperSpline() {
    }
    
    public void setup(final double[][] points) {
        this.mDimensionality = points[0].length;
        this.mPoints = points.length;
        this.mCtl = new double[this.mDimensionality][this.mPoints];
        this.mCurve = new Cubic[this.mDimensionality][];
        for (int d = 0; d < this.mDimensionality; ++d) {
            for (int p = 0; p < this.mPoints; ++p) {
                this.mCtl[d][p] = points[p][d];
            }
        }
        for (int d = 0; d < this.mDimensionality; ++d) {
            this.mCurve[d] = calcNaturalCubic(this.mCtl[d].length, this.mCtl[d]);
        }
        this.mCurveLength = new double[this.mPoints - 1];
        this.mTotalLength = 0.0;
        final Cubic[] temp = new Cubic[this.mDimensionality];
        for (int p = 0; p < this.mCurveLength.length; ++p) {
            for (int d2 = 0; d2 < this.mDimensionality; ++d2) {
                temp[d2] = this.mCurve[d2][p];
            }
            final double mTotalLength = this.mTotalLength;
            final double[] mCurveLength = this.mCurveLength;
            final int n = p;
            final double approxLength = this.approxLength(temp);
            mCurveLength[n] = approxLength;
            this.mTotalLength = mTotalLength + approxLength;
        }
    }
    
    public void getVelocity(final double p, final double[] v) {
        double pos = p * this.mTotalLength;
        final double sum = 0.0;
        int k;
        for (k = 0; k < this.mCurveLength.length - 1 && this.mCurveLength[k] < pos; pos -= this.mCurveLength[k], ++k) {}
        for (int i = 0; i < v.length; ++i) {
            v[i] = this.mCurve[i][k].vel(pos / this.mCurveLength[k]);
        }
    }
    
    public void getPos(final double p, final double[] x) {
        double pos = p * this.mTotalLength;
        final double sum = 0.0;
        int k;
        for (k = 0; k < this.mCurveLength.length - 1 && this.mCurveLength[k] < pos; pos -= this.mCurveLength[k], ++k) {}
        for (int i = 0; i < x.length; ++i) {
            x[i] = this.mCurve[i][k].eval(pos / this.mCurveLength[k]);
        }
    }
    
    public void getPos(final double p, final float[] x) {
        double pos = p * this.mTotalLength;
        final double sum = 0.0;
        int k;
        for (k = 0; k < this.mCurveLength.length - 1 && this.mCurveLength[k] < pos; pos -= this.mCurveLength[k], ++k) {}
        for (int i = 0; i < x.length; ++i) {
            x[i] = (float)this.mCurve[i][k].eval(pos / this.mCurveLength[k]);
        }
    }
    
    public double getPos(final double p, final int splineNumber) {
        double pos = p * this.mTotalLength;
        final double sum = 0.0;
        int k;
        for (k = 0; k < this.mCurveLength.length - 1 && this.mCurveLength[k] < pos; pos -= this.mCurveLength[k], ++k) {}
        return this.mCurve[splineNumber][k].eval(pos / this.mCurveLength[k]);
    }
    
    public double approxLength(final Cubic[] curve) {
        double sum = 0.0;
        final int N = curve.length;
        final double[] old = new double[curve.length];
        for (double i = 0.0; i < 1.0; i += 0.1) {
            double s = 0.0;
            for (int j = 0; j < curve.length; ++j) {
                final double n;
                double tmp = n = old[j];
                final double[] array = old;
                final int n2 = j;
                final double eval = curve[j].eval(i);
                array[n2] = eval;
                tmp = n - eval;
                s += tmp * tmp;
            }
            if (i > 0.0) {
                sum += Math.sqrt(s);
            }
        }
        double s2 = 0.0;
        for (int k = 0; k < curve.length; ++k) {
            final double n3;
            double tmp2 = n3 = old[k];
            final double[] array2 = old;
            final int n4 = k;
            final double eval2 = curve[k].eval(1.0);
            array2[n4] = eval2;
            tmp2 = n3 - eval2;
            s2 += tmp2 * tmp2;
        }
        sum += Math.sqrt(s2);
        return sum;
    }
    
    static Cubic[] calcNaturalCubic(int n, final double[] x) {
        final double[] gamma = new double[n];
        final double[] delta = new double[n];
        final double[] D = new double[n];
        --n;
        gamma[0] = 0.5;
        for (int i = 1; i < n; ++i) {
            gamma[i] = 1.0 / (4.0 - gamma[i - 1]);
        }
        gamma[n] = 1.0 / (2.0 - gamma[n - 1]);
        delta[0] = 3.0 * (x[1] - x[0]) * gamma[0];
        for (int i = 1; i < n; ++i) {
            delta[i] = (3.0 * (x[i + 1] - x[i - 1]) - delta[i - 1]) * gamma[i];
        }
        D[n] = (delta[n] = (3.0 * (x[n] - x[n - 1]) - delta[n - 1]) * gamma[n]);
        for (int i = n - 1; i >= 0; --i) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }
        final Cubic[] C = new Cubic[n];
        for (int j = 0; j < n; ++j) {
            C[j] = new Cubic((float)x[j], D[j], 3.0 * (x[j + 1] - x[j]) - 2.0 * D[j] - D[j + 1], 2.0 * (x[j] - x[j + 1]) + D[j] + D[j + 1]);
        }
        return C;
    }
    
    public static class Cubic
    {
        double mA;
        double mB;
        double mC;
        double mD;
        public static final double THIRD = 0.3333333333333333;
        public static final double HALF = 0.5;
        
        public Cubic(final double a, final double b, final double c, final double d) {
            this.mA = a;
            this.mB = b;
            this.mC = c;
            this.mD = d;
        }
        
        public double eval(final double u) {
            return ((this.mD * u + this.mC) * u + this.mB) * u + this.mA;
        }
        
        public double vel(final double v) {
            return (this.mD * 0.3333333333333333 * v + this.mC * 0.5) * v + this.mB;
        }
    }
}
