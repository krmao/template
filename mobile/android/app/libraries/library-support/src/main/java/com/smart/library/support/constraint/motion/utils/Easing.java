package com.smart.library.support.constraint.motion.utils;

import java.util.*;
import android.util.*;

public class Easing
{
    static Easing sDefault;
    String str;
    private static final String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
    private static final String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
    private static final String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
    private static final String LINEAR = "cubic(1, 1, 0, 0)";
    private static final String DECELERATE_NAME = "decelerate";
    private static final String ACCELERATE_NAME = "accelerate";
    private static final String STANDARD_NAME = "standard";
    private static final String LINEAR_NAME = "linear";
    public static String[] NAMED_EASING;
    
    public Easing() {
        this.str = "identity";
    }
    
    public static Easing getInterpolator(final String configString) {
        if (configString == null) {
            return null;
        }
        if (configString.startsWith("cubic")) {
            return new CubicEasing(configString);
        }
        switch (configString) {
            case "standard": {
                return new CubicEasing("cubic(0.4, 0.0, 0.2, 1)");
            }
            case "accelerate": {
                return new CubicEasing("cubic(0.4, 0.05, 0.8, 0.7)");
            }
            case "decelerate": {
                return new CubicEasing("cubic(0.0, 0.0, 0.2, 0.95)");
            }
            case "linear": {
                return new CubicEasing("cubic(1, 1, 0, 0)");
            }
            default: {
                Log.e("ConstraintSet", "transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " + Arrays.toString(Easing.NAMED_EASING));
                return Easing.sDefault;
            }
        }
    }
    
    public double get(final double x) {
        return x;
    }
    
    @Override
    public String toString() {
        return this.str;
    }
    
    public double getDiff(final double x) {
        return 1.0;
    }
    
    static {
        Easing.sDefault = new Easing();
        Easing.NAMED_EASING = new String[] { "standard", "accelerate", "decelerate", "linear" };
    }
    
    static class CubicEasing extends Easing
    {
        private static double error;
        private static double d_error;
        double x1;
        double y1;
        double x2;
        double y2;
        
        CubicEasing(final String configString) {
            this.str = configString;
            final int start = configString.indexOf(40);
            final int off1 = configString.indexOf(44, start);
            this.x1 = Double.parseDouble(configString.substring(start + 1, off1).trim());
            final int off2 = configString.indexOf(44, off1 + 1);
            this.y1 = Double.parseDouble(configString.substring(off1 + 1, off2).trim());
            final int off3 = configString.indexOf(44, off2 + 1);
            this.x2 = Double.parseDouble(configString.substring(off2 + 1, off3).trim());
            final int end = configString.indexOf(41, off3 + 1);
            this.y2 = Double.parseDouble(configString.substring(off3 + 1, end).trim());
        }
        
        public CubicEasing(final double x1, final double y1, final double x2, final double y2) {
            this.setup(x1, y1, x2, y2);
        }
        
        void setup(final double x1, final double y1, final double x2, final double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        
        private double getX(final double t) {
            final double t2 = 1.0 - t;
            final double f1 = 3.0 * t2 * t2 * t;
            final double f2 = 3.0 * t2 * t * t;
            final double f3 = t * t * t;
            return this.x1 * f1 + this.x2 * f2 + f3;
        }
        
        private double getY(final double t) {
            final double t2 = 1.0 - t;
            final double f1 = 3.0 * t2 * t2 * t;
            final double f2 = 3.0 * t2 * t * t;
            final double f3 = t * t * t;
            return this.y1 * f1 + this.y2 * f2 + f3;
        }
        
        private double getDiffX(final double t) {
            final double t2 = 1.0 - t;
            return 3.0 * t2 * t2 * this.x1 + 6.0 * t2 * t * (this.x2 - this.x1) + 3.0 * t * t * (1.0 - this.x2);
        }
        
        private double getDiffY(final double t) {
            final double t2 = 1.0 - t;
            return 3.0 * t2 * t2 * this.y1 + 6.0 * t2 * t * (this.y2 - this.y1) + 3.0 * t * t * (1.0 - this.y2);
        }
        
        @Override
        public double getDiff(final double x) {
            double t = 0.5;
            double range = 0.5;
            while (range > CubicEasing.d_error) {
                final double tx = this.getX(t);
                range *= 0.5;
                if (tx < x) {
                    t += range;
                }
                else {
                    t -= range;
                }
            }
            final double x2 = this.getX(t - range);
            final double x3 = this.getX(t + range);
            final double y1 = this.getY(t - range);
            final double y2 = this.getY(t + range);
            return (y2 - y1) / (x3 - x2);
        }
        
        @Override
        public double get(final double x) {
            if (x <= 0.0) {
                return 0.0;
            }
            if (x >= 1.0) {
                return 1.0;
            }
            double t = 0.5;
            double range = 0.5;
            while (range > CubicEasing.error) {
                final double tx = this.getX(t);
                range *= 0.5;
                if (tx < x) {
                    t += range;
                }
                else {
                    t -= range;
                }
            }
            final double x2 = this.getX(t - range);
            final double x3 = this.getX(t + range);
            final double y1 = this.getY(t - range);
            final double y2 = this.getY(t + range);
            return (y2 - y1) * (x - x2) / (x3 - x2) + y1;
        }
        
        static {
            CubicEasing.error = 0.01;
            CubicEasing.d_error = 1.0E-4;
        }
    }
}
