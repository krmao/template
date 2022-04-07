package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.motion.utils.*;
import android.view.*;
import android.os.*;
import com.smart.library.support.constraint.*;
import java.util.*;
import android.util.*;
import com.smart.library.support.constraint.solver.widgets.*;

class MotionConstrainedPoint implements Comparable<MotionConstrainedPoint>
{
    public static final String TAG = "MotionPaths";
    public static final boolean DEBUG = false;
    private float alpha;
    int mVisibilityMode;
    int visibility;
    private boolean applyElevation;
    private float elevation;
    private float rotation;
    private float rotationX;
    public float rotationY;
    private float scaleX;
    private float scaleY;
    private float mPivotX;
    private float mPivotY;
    private float translationX;
    private float translationY;
    private float translationZ;
    private Easing mKeyFrameEasing;
    private int mDrawPath;
    private float position;
    private float x;
    private float y;
    private float width;
    private float height;
    private float mPathRotate;
    private float mProgress;
    static final int PERPENDICULAR = 1;
    static final int CARTESIAN = 2;
    static String[] names;
    LinkedHashMap<String, ConstraintAttribute> attributes;
    int mMode;
    double[] mTempValue;
    double[] mTempDelta;
    
    public MotionConstrainedPoint() {
        this.alpha = 1.0f;
        this.mVisibilityMode = 0;
        this.applyElevation = false;
        this.elevation = 0.0f;
        this.rotation = 0.0f;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.mPivotX = Float.NaN;
        this.mPivotY = Float.NaN;
        this.translationX = 0.0f;
        this.translationY = 0.0f;
        this.translationZ = 0.0f;
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.attributes = new LinkedHashMap<String, ConstraintAttribute>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }
    
    private boolean diff(final float a, final float b) {
        if (Float.isNaN(a) || Float.isNaN(b)) {
            return Float.isNaN(a) != Float.isNaN(b);
        }
        return Math.abs(a - b) > 1.0E-6f;
    }
    
    void different(final MotionConstrainedPoint points, final HashSet<String> keySet) {
        if (this.diff(this.alpha, points.alpha)) {
            keySet.add("alpha");
        }
        if (this.diff(this.elevation, points.elevation)) {
            keySet.add("elevation");
        }
        if (this.visibility != points.visibility && this.mVisibilityMode == 0 && (this.visibility == 0 || points.visibility == 0)) {
            keySet.add("alpha");
        }
        if (this.diff(this.rotation, points.rotation)) {
            keySet.add("rotation");
        }
        if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(points.mPathRotate)) {
            keySet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mProgress) || !Float.isNaN(points.mProgress)) {
            keySet.add("progress");
        }
        if (this.diff(this.rotationX, points.rotationX)) {
            keySet.add("rotationX");
        }
        if (this.diff(this.rotationY, points.rotationY)) {
            keySet.add("rotationY");
        }
        if (this.diff(this.scaleX, points.scaleX)) {
            keySet.add("scaleX");
        }
        if (this.diff(this.scaleY, points.scaleY)) {
            keySet.add("scaleY");
        }
        if (this.diff(this.translationX, points.translationX)) {
            keySet.add("translationX");
        }
        if (this.diff(this.translationY, points.translationY)) {
            keySet.add("translationY");
        }
        if (this.diff(this.translationZ, points.translationZ)) {
            keySet.add("translationZ");
        }
    }
    
    void different(final MotionConstrainedPoint points, final boolean[] mask, final String[] custom) {
        int c = 0;
        final int n = c++;
        mask[n] |= this.diff(this.position, points.position);
        final int n2 = c++;
        mask[n2] |= this.diff(this.x, points.x);
        final int n3 = c++;
        mask[n3] |= this.diff(this.y, points.y);
        final int n4 = c++;
        mask[n4] |= this.diff(this.width, points.width);
        final int n5 = c++;
        mask[n5] |= this.diff(this.height, points.height);
    }
    
    void fillStandard(final double[] data, final int[] toUse) {
        final float[] set = { this.position, this.x, this.y, this.width, this.height, this.alpha, this.elevation, this.rotation, this.rotationX, this.rotationY, this.scaleX, this.scaleY, this.mPivotX, this.mPivotY, this.translationX, this.translationY, this.translationZ, this.mPathRotate };
        int c = 0;
        for (int i = 0; i < toUse.length; ++i) {
            if (toUse[i] < set.length) {
                data[c++] = set[toUse[i]];
            }
        }
    }
    
    boolean hasCustomData(final String name) {
        return this.attributes.containsKey(name);
    }
    
    int getCustomDataCount(final String name) {
        return this.attributes.get(name).noOfInterpValues();
    }
    
    int getCustomData(final String name, final double[] value, int offset) {
        final ConstraintAttribute a = this.attributes.get(name);
        if (a.noOfInterpValues() == 1) {
            value[offset] = a.getValueToInterpolate();
            return 1;
        }
        final int N = a.noOfInterpValues();
        final float[] f = new float[N];
        a.getValuesToInterpolate(f);
        for (int i = 0; i < N; ++i) {
            value[offset++] = f[i];
        }
        return N;
    }
    
    void setBounds(final float x, final float y, final float w, final float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
    
    @Override
    public int compareTo(final MotionConstrainedPoint o) {
        return Float.compare(this.position, o.position);
    }
    
    public void applyParameters(final View view) {
        this.visibility = view.getVisibility();
        this.alpha = ((view.getVisibility() != View.VISIBLE) ? 0.0f : view.getAlpha());
        this.applyElevation = false;
        if (Build.VERSION.SDK_INT >= 21) {
            this.elevation = view.getElevation();
        }
        this.rotation = view.getRotation();
        this.rotationX = view.getRotationX();
        this.rotationY = view.getRotationY();
        this.scaleX = view.getScaleX();
        this.scaleY = view.getScaleY();
        this.mPivotX = view.getPivotX();
        this.mPivotY = view.getPivotY();
        this.translationX = view.getTranslationX();
        this.translationY = view.getTranslationY();
        if (Build.VERSION.SDK_INT >= 21) {
            this.translationZ = view.getTranslationZ();
        }
    }
    
    public void applyParameters(final ConstraintSet.Constraint c) {
        this.mVisibilityMode = c.propertySet.mVisibilityMode;
        this.visibility = c.propertySet.visibility;
        this.alpha = ((c.propertySet.visibility != 0 && this.mVisibilityMode == 0) ? 0.0f : c.propertySet.alpha);
        this.applyElevation = c.transform.applyElevation;
        this.elevation = c.transform.elevation;
        this.rotation = c.transform.rotation;
        this.rotationX = c.transform.rotationX;
        this.rotationY = c.transform.rotationY;
        this.scaleX = c.transform.scaleX;
        this.scaleY = c.transform.scaleY;
        this.mPivotX = c.transform.transformPivotX;
        this.mPivotY = c.transform.transformPivotY;
        this.translationX = c.transform.translationX;
        this.translationY = c.transform.translationY;
        this.translationZ = c.transform.translationZ;
        this.mKeyFrameEasing = Easing.getInterpolator(c.motion.mTransitionEasing);
        this.mPathRotate = c.motion.mPathRotate;
        this.mDrawPath = c.motion.mDrawPath;
        this.mProgress = c.propertySet.mProgress;
        final Set<String> at = c.mCustomConstraints.keySet();
        for (final String s : at) {
            final ConstraintAttribute attr = c.mCustomConstraints.get(s);
            if (attr.getType() != ConstraintAttribute.AttributeType.STRING_TYPE) {
                this.attributes.put(s, attr);
            }
        }
    }
    
    public void addValues(final HashMap<String, SplineSet> splines, final int mFramePosition) {
        for (final String s : splines.keySet()) {
            final SplineSet splineSet = splines.get(s);
            final String s2 = s;
            switch (s2) {
                case "alpha": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.alpha) ? 1.0f : this.alpha);
                    continue;
                }
                case "elevation": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.elevation) ? 0.0f : this.elevation);
                    continue;
                }
                case "rotation": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.rotation) ? 0.0f : this.rotation);
                    continue;
                }
                case "rotationX": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.rotationX) ? 0.0f : this.rotationX);
                    continue;
                }
                case "rotationY": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.rotationY) ? 0.0f : this.rotationY);
                    continue;
                }
                case "transitionPathRotate": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.mPathRotate) ? 0.0f : this.mPathRotate);
                    continue;
                }
                case "progress": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.mProgress) ? 0.0f : this.mProgress);
                    continue;
                }
                case "scaleX": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.scaleX) ? 1.0f : this.scaleX);
                    continue;
                }
                case "scaleY": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.scaleY) ? 1.0f : this.scaleY);
                    continue;
                }
                case "translationX": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.translationX) ? 0.0f : this.translationX);
                    continue;
                }
                case "translationY": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.translationY) ? 0.0f : this.translationY);
                    continue;
                }
                case "translationZ": {
                    splineSet.setPoint(mFramePosition, Float.isNaN(this.translationZ) ? 0.0f : this.translationZ);
                    continue;
                }
                default: {
                    if (s.startsWith("CUSTOM")) {
                        final String customName = s.split(",")[1];
                        if (this.attributes.containsKey(customName)) {
                            final ConstraintAttribute custom = this.attributes.get(customName);
                            if (splineSet instanceof SplineSet.CustomSet) {
                                ((SplineSet.CustomSet)splineSet).setPoint(mFramePosition, custom);
                            }
                            else {
                                Log.e("MotionPaths", s + " splineSet not a CustomSet frame = " + mFramePosition + ", value" + custom.getValueToInterpolate() + splineSet);
                            }
                        }
                        else {
                            Log.e("MotionPaths", "UNKNOWN customName " + customName);
                        }
                        continue;
                    }
                    Log.e("MotionPaths", "UNKNOWN spline " + s);
                    continue;
                }
            }
        }
    }
    
    public void setState(final View view) {
        this.setBounds(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        this.applyParameters(view);
    }
    
    public void setState(final ConstraintWidget cw, final ConstraintSet constraintSet, final int viewId) {
        this.setBounds(cw.getX(), cw.getY(), cw.getWidth(), cw.getHeight());
        this.applyParameters(constraintSet.getParameters(viewId));
    }
    
    static {
        MotionConstrainedPoint.names = new String[] { "position", "x", "y", "width", "height", "pathRotate" };
    }
}
