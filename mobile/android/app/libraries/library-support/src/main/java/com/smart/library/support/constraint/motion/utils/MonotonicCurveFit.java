package com.smart.library.support.constraint.motion.utils;

public class MonotonicCurveFit extends CurveFit
{
    private static final String TAG = "MonotonicCurveFit";
    private double[] mT;
    private double[][] mY;
    private double[][] mTangent;
    
    public MonotonicCurveFit(final double[] time, final double[][] y) {
        final int N = time.length;
        final int dim = y[0].length;
        final double[][] slope = new double[N - 1][dim];
        final double[][] tangent = new double[N][dim];
        for (int j = 0; j < dim; ++j) {
            for (int i = 0; i < N - 1; ++i) {
                final double dt = time[i + 1] - time[i];
                slope[i][j] = (y[i + 1][j] - y[i][j]) / dt;
                if (i == 0) {
                    tangent[i][j] = slope[i][j];
                }
                else {
                    tangent[i][j] = (slope[i - 1][j] + slope[i][j]) * 0.5;
                }
            }
            tangent[N - 1][j] = slope[N - 2][j];
        }
        for (int k = 0; k < N - 1; ++k) {
            for (int l = 0; l < dim; ++l) {
                if (slope[k][l] == 0.0) {
                    tangent[k][l] = 0.0;
                    tangent[k + 1][l] = 0.0;
                }
                else {
                    final double a = tangent[k][l] / slope[k][l];
                    final double b = tangent[k + 1][l] / slope[k][l];
                    final double h = Math.hypot(a, b);
                    if (h > 9.0) {
                        final double t = 3.0 / h;
                        tangent[k][l] = t * a * slope[k][l];
                        tangent[k + 1][l] = t * b * slope[k][l];
                    }
                }
            }
        }
        this.mT = time;
        this.mY = y;
        this.mTangent = tangent;
    }
    
    @Override
    public void getPos(final double t, final double[] v) {
        final int n = this.mT.length;
        final int dim = this.mY[0].length;
        if (t <= this.mT[0]) {
            for (int j = 0; j < dim; ++j) {
                v[j] = this.mY[0][j];
            }
            return;
        }
        if (t >= this.mT[n - 1]) {
            for (int j = 0; j < dim; ++j) {
                v[j] = this.mY[n - 1][j];
            }
            return;
        }
        for (int i = 0; i < n - 1; ++i) {
            if (t == this.mT[i]) {
                for (int k = 0; k < dim; ++k) {
                    v[k] = this.mY[i][k];
                }
            }
            if (t < this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                for (int l = 0; l < dim; ++l) {
                    final double y1 = this.mY[i][l];
                    final double y2 = this.mY[i + 1][l];
                    final double t2 = this.mTangent[i][l];
                    final double t3 = this.mTangent[i + 1][l];
                    v[l] = interpolate(h, x, y1, y2, t2, t3);
                }
                return;
            }
        }
    }
    
    @Override
    public void getPos(final double t, final float[] v) {
        final int n = this.mT.length;
        final int dim = this.mY[0].length;
        if (t <= this.mT[0]) {
            for (int j = 0; j < dim; ++j) {
                v[j] = (float)this.mY[0][j];
            }
            return;
        }
        if (t >= this.mT[n - 1]) {
            for (int j = 0; j < dim; ++j) {
                v[j] = (float)this.mY[n - 1][j];
            }
            return;
        }
        for (int i = 0; i < n - 1; ++i) {
            if (t == this.mT[i]) {
                for (int k = 0; k < dim; ++k) {
                    v[k] = (float)this.mY[i][k];
                }
            }
            if (t < this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                for (int l = 0; l < dim; ++l) {
                    final double y1 = this.mY[i][l];
                    final double y2 = this.mY[i + 1][l];
                    final double t2 = this.mTangent[i][l];
                    final double t3 = this.mTangent[i + 1][l];
                    v[l] = (float)interpolate(h, x, y1, y2, t2, t3);
                }
                return;
            }
        }
    }
    
    @Override
    public double getPos(final double t, final int j) {
        final int n = this.mT.length;
        if (t <= this.mT[0]) {
            return this.mY[0][j];
        }
        if (t >= this.mT[n - 1]) {
            return this.mY[n - 1][j];
        }
        for (int i = 0; i < n - 1; ++i) {
            if (t == this.mT[i]) {
                return this.mY[i][j];
            }
            if (t < this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                final double y1 = this.mY[i][j];
                final double y2 = this.mY[i + 1][j];
                final double t2 = this.mTangent[i][j];
                final double t3 = this.mTangent[i + 1][j];
                return interpolate(h, x, y1, y2, t2, t3);
            }
        }
        return 0.0;
    }
    
    @Override
    public void getSlope(double t, final double[] v) {
        final int n = this.mT.length;
        final int dim = this.mY[0].length;
        if (t <= this.mT[0]) {
            t = this.mT[0];
        }
        else if (t >= this.mT[n - 1]) {
            t = this.mT[n - 1];
        }
        for (int i = 0; i < n - 1; ++i) {
            if (t <= this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                for (int j = 0; j < dim; ++j) {
                    final double y1 = this.mY[i][j];
                    final double y2 = this.mY[i + 1][j];
                    final double t2 = this.mTangent[i][j];
                    final double t3 = this.mTangent[i + 1][j];
                    v[j] = diff(h, x, y1, y2, t2, t3) / h;
                }
                break;
            }
        }
    }
    
    @Override
    public double getSlope(double t, final int j) {
        final int n = this.mT.length;
        if (t < this.mT[0]) {
            t = this.mT[0];
        }
        else if (t >= this.mT[n - 1]) {
            t = this.mT[n - 1];
        }
        for (int i = 0; i < n - 1; ++i) {
            if (t <= this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                final double y1 = this.mY[i][j];
                final double y2 = this.mY[i + 1][j];
                final double t2 = this.mTangent[i][j];
                final double t3 = this.mTangent[i + 1][j];
                return diff(h, x, y1, y2, t2, t3) / h;
            }
        }
        return 0.0;
    }
    
    @Override
    public double[] getTimePoints() {
        return this.mT;
    }
    
    private static double interpolate(final double h, final double x, final double y1, final double y2, final double t1, final double t2) {
        final double x2 = x * x;
        final double x3 = x2 * x;
        return -2.0 * x3 * y2 + 3.0 * x2 * y2 + 2.0 * x3 * y1 - 3.0 * x2 * y1 + y1 + h * t2 * x3 + h * t1 * x3 - h * t2 * x2 - 2.0 * h * t1 * x2 + h * t1 * x;
    }
    
    private static double diff(final double h, final double x, final double y1, final double y2, final double t1, final double t2) {
        final double x2 = x * x;
        return -6.0 * x2 * y2 + 6.0 * x * y2 + 6.0 * x2 * y1 - 6.0 * x * y1 + 3.0 * h * t2 * x2 + 3.0 * h * t1 * x2 - 2.0 * h * t2 * x - 4.0 * h * t1 * x + h * t1;
    }
}
