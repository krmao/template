package com.smart.library.support.constraint.motion.utils;

import java.util.*;

class ArcCurveFit extends CurveFit
{
    public static final int ARC_START_VERTICAL = 1;
    public static final int ARC_START_HORIZONTAL = 2;
    public static final int ARC_START_FLIP = 3;
    public static final int ARC_START_LINEAR = 0;
    private static final int START_VERTICAL = 1;
    private static final int START_HORIZONTAL = 2;
    private static final int START_LINEAR = 3;
    private final double[] mTime;
    Arc[] mArcs;
    
    @Override
    public void getPos(double t, final double[] v) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        int i = 0;
        while (i < this.mArcs.length) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    v[0] = this.mArcs[i].getLinearX(t);
                    v[1] = this.mArcs[i].getLinearY(t);
                    return;
                }
                this.mArcs[i].setPoint(t);
                v[0] = this.mArcs[i].getX();
                v[1] = this.mArcs[i].getY();
            }
            else {
                ++i;
            }
        }
    }
    
    @Override
    public void getPos(double t, final float[] v) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        else if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        int i = 0;
        while (i < this.mArcs.length) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    v[0] = (float)this.mArcs[i].getLinearX(t);
                    v[1] = (float)this.mArcs[i].getLinearY(t);
                    return;
                }
                this.mArcs[i].setPoint(t);
                v[0] = (float)this.mArcs[i].getX();
                v[1] = (float)this.mArcs[i].getY();
            }
            else {
                ++i;
            }
        }
    }
    
    @Override
    public void getSlope(double t, final double[] v) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        else if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        int i = 0;
        while (i < this.mArcs.length) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    v[0] = this.mArcs[i].getLinearDX(t);
                    v[1] = this.mArcs[i].getLinearDY(t);
                    return;
                }
                this.mArcs[i].setPoint(t);
                v[0] = this.mArcs[i].getDX();
                v[1] = this.mArcs[i].getDY();
            }
            else {
                ++i;
            }
        }
    }
    
    @Override
    public double getPos(double t, final int j) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        else if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        int i = 0;
        while (i < this.mArcs.length) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    if (j == 0) {
                        return this.mArcs[i].getLinearX(t);
                    }
                    return this.mArcs[i].getLinearY(t);
                }
                else {
                    this.mArcs[i].setPoint(t);
                    if (j == 0) {
                        return this.mArcs[i].getX();
                    }
                    return this.mArcs[i].getY();
                }
            }
            else {
                ++i;
            }
        }
        return Double.NaN;
    }
    
    @Override
    public double getSlope(double t, final int j) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        if (t > this.mArcs[this.mArcs.length - 1].mTime2) {
            t = this.mArcs[this.mArcs.length - 1].mTime2;
        }
        int i = 0;
        while (i < this.mArcs.length) {
            if (t <= this.mArcs[i].mTime2) {
                if (this.mArcs[i].linear) {
                    if (j == 0) {
                        return this.mArcs[i].getLinearDX(t);
                    }
                    return this.mArcs[i].getLinearDY(t);
                }
                else {
                    this.mArcs[i].setPoint(t);
                    if (j == 0) {
                        return this.mArcs[i].getDX();
                    }
                    return this.mArcs[i].getDY();
                }
            }
            else {
                ++i;
            }
        }
        return Double.NaN;
    }
    
    @Override
    public double[] getTimePoints() {
        return this.mTime;
    }
    
    public ArcCurveFit(final int[] arcModes, final double[] time, final double[][] y) {
        this.mTime = time;
        this.mArcs = new Arc[time.length - 1];
        int mode = 1;
        int last = 1;
        for (int i = 0; i < this.mArcs.length; ++i) {
            switch (arcModes[i]) {
                case 1: {
                    mode = (last = 1);
                    break;
                }
                case 2: {
                    mode = (last = 2);
                    break;
                }
                case 3: {
                    mode = (last = ((last == 1) ? 2 : 1));
                    break;
                }
                case 0: {
                    mode = 3;
                    break;
                }
            }
            this.mArcs[i] = new Arc(mode, time[i], time[i + 1], y[i][0], y[i][1], y[i + 1][0], y[i + 1][1]);
        }
    }
    
    private static class Arc
    {
        private static final String TAG = "Arc";
        private static double[] ourPercent;
        double[] mLut;
        double mArcDistance;
        double mTime1;
        double mTime2;
        double mX1;
        double mX2;
        double mY1;
        double mY2;
        double mOneOverDeltaTime;
        double mEllipseA;
        double mEllipseB;
        double mEllipseCenterX;
        double mEllipseCenterY;
        double mArcVelocity;
        double mTmpSinAngle;
        double mTmpCosAngle;
        boolean mVertical;
        boolean linear;
        private static final double EPSILON = 0.001;
        
        Arc(final int mode, final double t1, final double t2, final double x1, final double y1, final double x2, final double y2) {
            this.linear = false;
            this.mVertical = (mode == 1);
            this.mTime1 = t1;
            this.mTime2 = t2;
            this.mOneOverDeltaTime = 1.0 / (this.mTime2 - this.mTime1);
            if (3 == mode) {
                this.linear = true;
            }
            final double dx = x2 - x1;
            final double dy = y2 - y1;
            if (this.linear || Math.abs(dx) < 0.001 || Math.abs(dy) < 0.001) {
                this.linear = true;
                this.mX1 = x1;
                this.mX2 = x2;
                this.mY1 = y1;
                this.mY2 = y2;
                this.mArcDistance = Math.hypot(dy, dx);
                this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
                this.mEllipseCenterX = dx / (this.mTime2 - this.mTime1);
                this.mEllipseCenterY = dy / (this.mTime2 - this.mTime1);
                return;
            }
            this.mLut = new double[101];
            this.mEllipseA = dx * (this.mVertical ? -1 : 1);
            this.mEllipseB = dy * (this.mVertical ? 1 : -1);
            this.mEllipseCenterX = (this.mVertical ? x2 : x1);
            this.mEllipseCenterY = (this.mVertical ? y1 : y2);
            this.buildTable(x1, y1, x2, y2);
            this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
        }
        
        void setPoint(final double time) {
            final double percent = (this.mVertical ? (this.mTime2 - time) : (time - this.mTime1)) * this.mOneOverDeltaTime;
            final double angle = 1.5707963267948966 * this.lookup(percent);
            this.mTmpSinAngle = Math.sin(angle);
            this.mTmpCosAngle = Math.cos(angle);
        }
        
        double getX() {
            return this.mEllipseCenterX + this.mEllipseA * this.mTmpSinAngle;
        }
        
        double getY() {
            return this.mEllipseCenterY + this.mEllipseB * this.mTmpCosAngle;
        }
        
        double getDX() {
            final double vx = this.mEllipseA * this.mTmpCosAngle;
            final double vy = -this.mEllipseB * this.mTmpSinAngle;
            final double norm = this.mArcVelocity / Math.hypot(vx, vy);
            return this.mVertical ? (-vx * norm) : (vx * norm);
        }
        
        double getDY() {
            final double vx = this.mEllipseA * this.mTmpCosAngle;
            final double vy = -this.mEllipseB * this.mTmpSinAngle;
            final double norm = this.mArcVelocity / Math.hypot(vx, vy);
            return this.mVertical ? (-vy * norm) : (vy * norm);
        }
        
        public double getLinearX(double t) {
            t = (t - this.mTime1) * this.mOneOverDeltaTime;
            return this.mX1 + t * (this.mX2 - this.mX1);
        }
        
        public double getLinearY(double t) {
            t = (t - this.mTime1) * this.mOneOverDeltaTime;
            return this.mY1 + t * (this.mY2 - this.mY1);
        }
        
        public double getLinearDX(final double t) {
            return this.mEllipseCenterX;
        }
        
        public double getLinearDY(final double t) {
            return this.mEllipseCenterY;
        }
        
        double lookup(final double v) {
            if (v <= 0.0) {
                return 0.0;
            }
            if (v >= 1.0) {
                return 1.0;
            }
            final double pos = v * (this.mLut.length - 1);
            final int iv = (int)pos;
            final double off = pos - (int)pos;
            return this.mLut[iv] + off * (this.mLut[iv + 1] - this.mLut[iv]);
        }
        
        private void buildTable(final double x1, final double y1, final double x2, final double y2) {
            final double a = x2 - x1;
            final double b = y1 - y2;
            double lx = 0.0;
            double ly = 0.0;
            double dist = 0.0;
            for (int i = 0; i < Arc.ourPercent.length; ++i) {
                final double angle = Math.toRadians(90.0 * i / (Arc.ourPercent.length - 1));
                final double s = Math.sin(angle);
                final double c = Math.cos(angle);
                final double px = a * s;
                final double py = b * c;
                if (i > 0) {
                    dist += Math.hypot(px - lx, py - ly);
                    Arc.ourPercent[i] = dist;
                }
                lx = px;
                ly = py;
            }
            this.mArcDistance = dist;
            for (int i = 0; i < Arc.ourPercent.length; ++i) {
                final double[] ourPercent = Arc.ourPercent;
                final int n = i;
                ourPercent[n] /= dist;
            }
            for (int i = 0; i < this.mLut.length; ++i) {
                final double pos = i / (this.mLut.length - 1);
                final int index = Arrays.binarySearch(Arc.ourPercent, pos);
                if (index >= 0) {
                    this.mLut[i] = index / (Arc.ourPercent.length - 1);
                }
                else if (index == -1) {
                    this.mLut[i] = 0.0;
                }
                else {
                    final int p1 = -index - 2;
                    final int p2 = -index - 1;
                    final double ans = (p1 + (pos - Arc.ourPercent[p1]) / (Arc.ourPercent[p2] - Arc.ourPercent[p1])) / (Arc.ourPercent.length - 1);
                    this.mLut[i] = ans;
                }
            }
        }
        
        static {
            Arc.ourPercent = new double[91];
        }
    }
}
