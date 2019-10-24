package com.smart.library.support.constraint.motion.utils;

public class LinearCurveFit extends CurveFit
{
    private static final String TAG = "LinearCurveFit";
    private double[] mT;
    private double[][] mY;
    private double mTotalLength;
    
    public LinearCurveFit(final double[] time, final double[][] y) {
        this.mTotalLength = Double.NaN;
        final int N = time.length;
        final int dim = y[0].length;
        this.mT = time;
        this.mY = y;
        if (dim > 2) {
            double sum = 0.0;
            double lastx = 0.0;
            double lasty = 0.0;
            for (int i = 0; i < time.length; ++i) {
                final double px = y[i][0];
                final double py = y[i][0];
                if (i > 0) {
                    sum += Math.hypot(px - lastx, py - lasty);
                }
                lastx = px;
                lasty = py;
            }
            this.mTotalLength = 0.0;
        }
    }
    
    private double getLength2D(final double t) {
        if (Double.isNaN(this.mTotalLength)) {
            return 0.0;
        }
        final int n = this.mT.length;
        if (t <= this.mT[0]) {
            return 0.0;
        }
        if (t >= this.mT[n - 1]) {
            return this.mTotalLength;
        }
        double sum = 0.0;
        double last_x = 0.0;
        double last_y = 0.0;
        for (int i = 0; i < n - 1; ++i) {
            double px = this.mY[i][0];
            double py = this.mY[i][1];
            if (i > 0) {
                sum += Math.hypot(px - last_x, py - last_y);
            }
            last_x = px;
            last_y = py;
            if (t == this.mT[i]) {
                return sum;
            }
            if (t < this.mT[i + 1]) {
                final double h = this.mT[i + 1] - this.mT[i];
                final double x = (t - this.mT[i]) / h;
                final double x2 = this.mY[i][0];
                final double x3 = this.mY[i + 1][0];
                final double y1 = this.mY[i][1];
                final double y2 = this.mY[i + 1][1];
                py -= y1 * (1.0 - x) + y2 * x;
                px -= x2 * (1.0 - x) + x3 * x;
                sum += Math.hypot(py, px);
                return sum;
            }
        }
        return 0.0;
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
                    v[l] = y1 * (1.0 - x) + y2 * x;
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
                    v[l] = (float)(y1 * (1.0 - x) + y2 * x);
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
                return y1 * (1.0 - x) + y2 * x;
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
                    v[j] = (y2 - y1) / h;
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
                return (y2 - y1) / h;
            }
        }
        return 0.0;
    }
    
    @Override
    public double[] getTimePoints() {
        return this.mT;
    }
}
