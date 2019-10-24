package com.smart.library.support.constraint.motion;

import android.graphics.*;
import android.util.*;
import java.util.*;
import android.view.*;
import com.smart.library.support.constraint.solver.widgets.*;
import com.smart.library.support.constraint.*;
import com.smart.library.support.constraint.motion.utils.*;
import android.content.*;

public class MotionController
{
    public static final int PATH_PERCENT = 0;
    public static final int PATH_PERPENDICULAR = 1;
    public static final int HORIZONTAL_PATH_X = 2;
    public static final int HORIZONTAL_PATH_Y = 3;
    public static final int VERTICAL_PATH_X = 4;
    public static final int VERTICAL_PATH_Y = 5;
    public static final int DRAW_PATH_NONE = 0;
    public static final int DRAW_PATH_BASIC = 1;
    public static final int DRAW_PATH_RELATIVE = 2;
    public static final int DRAW_PATH_CARTESIAN = 3;
    public static final int DRAW_PATH_AS_CONFIGURED = 4;
    public static final int DRAW_PATH_RECTANGLE = 5;
    public static final int DRAW_PATH_SCREEN = 6;
    private static final String TAG = "MotionController";
    private static final boolean DEBUG = false;
    private static final boolean FAVOR_FIXED_SIZE_VIEWS = false;
    View mView;
    int mId;
    String mConstraintTag;
    private int mCurveFitType;
    private MotionPaths mStartMotionPath;
    private MotionPaths mEndMotionPath;
    private MotionConstrainedPoint mStartPoint;
    private MotionConstrainedPoint mEndPoint;
    private CurveFit[] mSpline;
    private CurveFit mArcSpline;
    float mMotionStagger;
    float mStaggerOffset;
    float mStaggerScale;
    private int[] mInterpolateVariables;
    private double[] mInterpolateData;
    private double[] mInterpolateVelocity;
    private String[] mAttributeNames;
    private int[] mAttributeInterpCount;
    private int MAX_DIMENSION;
    private float[] mValuesBuff;
    private ArrayList<MotionPaths> mMotionPaths;
    private float[] mVelocity;
    private ArrayList<Key> mKeyList;
    private HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
    private HashMap<String, SplineSet> mAttributesMap;
    private HashMap<String, KeyCycleOscillator> mCycleMap;
    private KeyTrigger[] mKeyTriggers;
    String[] attributeTable;
    
    MotionPaths getKeyFrame(final int i) {
        return this.mMotionPaths.get(i);
    }
    
    MotionController(final View view) {
        this.mCurveFitType = -1;
        this.mStartMotionPath = new MotionPaths();
        this.mEndMotionPath = new MotionPaths();
        this.mStartPoint = new MotionConstrainedPoint();
        this.mEndPoint = new MotionConstrainedPoint();
        this.mMotionStagger = Float.NaN;
        this.mStaggerOffset = 0.0f;
        this.mStaggerScale = 1.0f;
        this.MAX_DIMENSION = 4;
        this.mValuesBuff = new float[this.MAX_DIMENSION];
        this.mMotionPaths = new ArrayList<MotionPaths>();
        this.mVelocity = new float[1];
        this.mKeyList = new ArrayList<Key>();
        this.setView(view);
    }
    
    float getStartX() {
        return this.mStartMotionPath.x;
    }
    
    float getStartY() {
        return this.mStartMotionPath.y;
    }
    
    float getFinalX() {
        return this.mEndMotionPath.x;
    }
    
    float getFinalY() {
        return this.mEndMotionPath.y;
    }
    
    void buildPath(final float[] points, final int pointCount) {
        final float mils = 1.0f / (pointCount - 1);
        final SplineSet trans_x = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("translationX");
        final SplineSet trans_y = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("translationY");
        final KeyCycleOscillator osc_x = (this.mCycleMap == null) ? null : this.mCycleMap.get("translationX");
        final KeyCycleOscillator osc_y = (this.mCycleMap == null) ? null : this.mCycleMap.get("translationY");
        for (int i = 0; i < pointCount; ++i) {
            float position = i * mils;
            if (this.mStaggerScale != 1.0f) {
                if (position < this.mStaggerOffset) {
                    position = 0.0f;
                }
                if (position > this.mStaggerOffset && position < 1.0) {
                    position -= this.mStaggerOffset;
                    position *= this.mStaggerScale;
                }
            }
            double p = position;
            Easing easing = this.mStartMotionPath.mKeyFrameEasing;
            float start = 0.0f;
            float end = Float.NaN;
            for (final MotionPaths frame : this.mMotionPaths) {
                if (frame.mKeyFrameEasing != null) {
                    if (frame.time < position) {
                        easing = frame.mKeyFrameEasing;
                        start = frame.time;
                    }
                    else {
                        if (!Float.isNaN(end)) {
                            continue;
                        }
                        end = frame.time;
                    }
                }
            }
            if (easing != null) {
                if (Float.isNaN(end)) {
                    end = 1.0f;
                }
                float offset = (position - start) / (end - start);
                offset = (float)easing.get(offset);
                p = offset * (end - start) + start;
            }
            this.mSpline[0].getPos(p, this.mInterpolateData);
            if (this.mArcSpline != null && this.mInterpolateData.length > 0) {
                this.mArcSpline.getPos(p, this.mInterpolateData);
            }
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, points, i * 2);
            if (osc_x != null) {
                final int n = i * 2;
                points[n] += osc_x.get(position);
            }
            else if (trans_x != null) {
                final int n2 = i * 2;
                points[n2] += trans_x.get(position);
            }
            if (osc_y != null) {
                final int n3 = i * 2 + 1;
                points[n3] += osc_y.get(position);
            }
            else if (trans_y != null) {
                final int n4 = i * 2 + 1;
                points[n4] += trans_y.get(position);
            }
        }
    }
    
    private float getPreCycleDistance() {
        final int pointCount = 100;
        final float[] points = new float[2];
        float sum = 0.0f;
        final float mils = 1.0f / (pointCount - 1);
        double x = 0.0;
        double y = 0.0;
        for (int i = 0; i < pointCount; ++i) {
            final float position = i * mils;
            double p = position;
            Easing easing = this.mStartMotionPath.mKeyFrameEasing;
            float start = 0.0f;
            float end = Float.NaN;
            for (final MotionPaths frame : this.mMotionPaths) {
                if (frame.mKeyFrameEasing != null) {
                    if (frame.time < position) {
                        easing = frame.mKeyFrameEasing;
                        start = frame.time;
                    }
                    else {
                        if (!Float.isNaN(end)) {
                            continue;
                        }
                        end = frame.time;
                    }
                }
            }
            if (easing != null) {
                if (Float.isNaN(end)) {
                    end = 1.0f;
                }
                float offset = (position - start) / (end - start);
                offset = (float)easing.get(offset);
                p = offset * (end - start) + start;
            }
            this.mSpline[0].getPos(p, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, points, 0);
            if (i > 0) {
                sum += (float)Math.hypot(y - points[1], x - points[0]);
            }
            x = points[0];
            y = points[1];
        }
        return sum;
    }
    
    KeyPositionBase getPositionKeyframe(final int layoutWidth, final int layoutHeight, final float x, final float y) {
        final RectF start = new RectF();
        start.left = this.mStartMotionPath.x;
        start.top = this.mStartMotionPath.y;
        start.right = start.left + this.mStartMotionPath.width;
        start.bottom = start.top + this.mStartMotionPath.height;
        final RectF end = new RectF();
        end.left = this.mEndMotionPath.x;
        end.top = this.mEndMotionPath.y;
        end.right = end.left + this.mEndMotionPath.width;
        end.bottom = end.top + this.mEndMotionPath.height;
        for (final Key key : this.mKeyList) {
            if (key instanceof KeyPositionBase && ((KeyPositionBase)key).intersects(layoutWidth, layoutHeight, start, end, x, y)) {
                return (KeyPositionBase)key;
            }
        }
        return null;
    }
    
    int buildKeyFrames(final float[] keyFrames, final int[] mode) {
        if (keyFrames != null) {
            int count = 0;
            final double[] time = this.mSpline[0].getTimePoints();
            if (mode != null) {
                for (final MotionPaths keyFrame : this.mMotionPaths) {
                    mode[count++] = keyFrame.mMode;
                }
                count = 0;
            }
            for (int i = 0; i < time.length; ++i) {
                this.mSpline[0].getPos(time[i], this.mInterpolateData);
                this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, keyFrames, count);
                count += 2;
            }
            return count / 2;
        }
        return 0;
    }
    
    int getAttributeValues(final String attributeType, final float[] points, final int pointCount) {
        final float mils = 1.0f / (pointCount - 1);
        final SplineSet spline = this.mAttributesMap.get(attributeType);
        if (spline == null) {
            return -1;
        }
        for (int j = 0; j < points.length; ++j) {
            points[j] = spline.get(j / (points.length - 1));
        }
        return points.length;
    }
    
    void buildRect(float p, final float[] path, final int offset) {
        p = this.getAdjustedPosition(p, null);
        this.mSpline[0].getPos(p, this.mInterpolateData);
        this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, offset);
    }
    
    void buildRectangles(final float[] path, final int pointCount) {
        final float mils = 1.0f / (pointCount - 1);
        for (int i = 0; i < pointCount; ++i) {
            float position = i * mils;
            position = this.getAdjustedPosition(position, null);
            this.mSpline[0].getPos(position, this.mInterpolateData);
            this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, i * 8);
        }
    }
    
    float getKeyFrameParameter(final int type, final float x, final float y) {
        final float dx = this.mEndMotionPath.x - this.mStartMotionPath.x;
        final float dy = this.mEndMotionPath.y - this.mStartMotionPath.y;
        final float startCenterX = this.mStartMotionPath.x + this.mStartMotionPath.width / 2.0f;
        final float startCenterY = this.mStartMotionPath.y + this.mStartMotionPath.height / 2.0f;
        final float hypot = (float)Math.hypot(dx, dy);
        if (hypot < 1.0E-7) {
            return Float.NaN;
        }
        final float vx = x - startCenterX;
        final float vy = y - startCenterY;
        final float distFromStart = (float)Math.hypot(vx, vy);
        if (distFromStart == 0.0f) {
            return 0.0f;
        }
        final float pathDistance = vx * dx + vy * dy;
        switch (type) {
            case 0: {
                return pathDistance / hypot;
            }
            case 1: {
                return (float)Math.sqrt(hypot * hypot - pathDistance * pathDistance);
            }
            case 2: {
                return vx / dx;
            }
            case 3: {
                return vy / dx;
            }
            case 4: {
                return vx / dy;
            }
            case 5: {
                return vy / dy;
            }
            default: {
                return 0.0f;
            }
        }
    }
    
    private void insertKey(final MotionPaths point) {
        final int pos = Collections.binarySearch(this.mMotionPaths, point);
        if (pos == 0) {
            Log.e("MotionController", " KeyPath positon \"" + point.position + "\" outside of range");
        }
        this.mMotionPaths.add(-pos - 1, point);
    }
    
    void addKeys(final ArrayList<Key> list) {
        this.mKeyList.addAll(list);
    }
    
    void addKey(final Key key) {
        this.mKeyList.add(key);
    }
    
    public void setup(final int parentWidth, final int parentHeight, final float transitionDuration, final long currentTime) {
        final HashSet<String> springAttributes = new HashSet<String>();
        final HashSet<String> timeCycleAttributes = new HashSet<String>();
        final HashSet<String> splineAttributes = new HashSet<String>();
        final HashSet<String> cycleAttributes = new HashSet<String>();
        final HashMap<String, Integer> interpolation = new HashMap<String, Integer>();
        ArrayList<KeyTrigger> triggerList = null;
        this.mStartPoint.different(this.mEndPoint, splineAttributes);
        if (this.mKeyList != null) {
            for (final Key key : this.mKeyList) {
                if (key instanceof KeyPosition) {
                    final KeyPosition keyPath = (KeyPosition)key;
                    this.insertKey(new MotionPaths(parentWidth, parentHeight, keyPath, this.mStartMotionPath, this.mEndMotionPath));
                    if (keyPath.mCurveFit == Key.UNSET) {
                        continue;
                    }
                    this.mCurveFitType = keyPath.mCurveFit;
                }
                else if (key instanceof KeyCycle) {
                    key.getAttributeNames(cycleAttributes);
                }
                else if (key instanceof KeyTimeCycle) {
                    key.getAttributeNames(timeCycleAttributes);
                }
                else if (key instanceof KeyTrigger) {
                    if (triggerList == null) {
                        triggerList = new ArrayList<KeyTrigger>();
                    }
                    triggerList.add((KeyTrigger)key);
                }
                else {
                    key.setInterpolation(interpolation);
                    key.getAttributeNames(splineAttributes);
                }
            }
        }
        if (triggerList != null) {
            this.mKeyTriggers = triggerList.toArray(new KeyTrigger[0]);
        }
        if (!splineAttributes.isEmpty()) {
            this.mAttributesMap = new HashMap<String, SplineSet>();
            for (final String attribute : splineAttributes) {
                SplineSet splineSets;
                if (attribute.startsWith("CUSTOM,")) {
                    final SparseArray<ConstraintAttribute> attrList = (SparseArray<ConstraintAttribute>)new SparseArray();
                    final String customAttributeName = attribute.split(",")[1];
                    for (final Key key2 : this.mKeyList) {
                        if (key2.mCustomConstraints == null) {
                            continue;
                        }
                        final ConstraintAttribute customAttribute = key2.mCustomConstraints.get(customAttributeName);
                        if (customAttribute == null) {
                            continue;
                        }
                        attrList.append(key2.mFramePosition,customAttribute);
                    }
                    splineSets = SplineSet.makeCustomSpline(attribute, attrList);
                }
                else {
                    splineSets = SplineSet.makeSpline(attribute);
                }
                if (splineSets == null) {
                    continue;
                }
                splineSets.setType(attribute);
                this.mAttributesMap.put(attribute, splineSets);
            }
            if (this.mKeyList != null) {
                for (final Key key : this.mKeyList) {
                    if (key instanceof KeyAttributes) {
                        key.addValues(this.mAttributesMap);
                    }
                }
            }
            this.mStartPoint.addValues(this.mAttributesMap, 0);
            this.mEndPoint.addValues(this.mAttributesMap, 100);
            for (final String spline : this.mAttributesMap.keySet()) {
                int curve = 0;
                if (interpolation.containsKey(spline)) {
                    curve = interpolation.get(spline);
                }
                this.mAttributesMap.get(spline).setup(curve);
            }
        }
        if (!timeCycleAttributes.isEmpty()) {
            if (this.mTimeCycleAttributesMap == null) {
                this.mTimeCycleAttributesMap = new HashMap<String, TimeCycleSplineSet>();
            }
            for (final String attribute : timeCycleAttributes) {
                if (this.mTimeCycleAttributesMap.containsKey(attribute)) {
                    continue;
                }
                TimeCycleSplineSet splineSets2 = null;
                if (attribute.startsWith("CUSTOM,")) {
                    final SparseArray<ConstraintAttribute> attrList = (SparseArray<ConstraintAttribute>)new SparseArray();
                    final String customAttributeName = attribute.split(",")[1];
                    for (final Key key2 : this.mKeyList) {
                        if (key2.mCustomConstraints == null) {
                            continue;
                        }
                        final ConstraintAttribute customAttribute = key2.mCustomConstraints.get(customAttributeName);
                        if (customAttribute == null) {
                            continue;
                        }
                        attrList.append(key2.mFramePosition,customAttribute);
                    }
                    splineSets2 = TimeCycleSplineSet.makeCustomSpline(attribute, attrList);
                }
                else {
                    splineSets2 = TimeCycleSplineSet.makeSpline(attribute, currentTime);
                }
                if (splineSets2 == null) {
                    continue;
                }
                splineSets2.setType(attribute);
                this.mTimeCycleAttributesMap.put(attribute, splineSets2);
            }
            if (this.mKeyList != null) {
                for (final Key key : this.mKeyList) {
                    if (key instanceof KeyTimeCycle) {
                        ((KeyTimeCycle)key).addTimeValues(this.mTimeCycleAttributesMap);
                    }
                }
            }
            for (final String spline : this.mTimeCycleAttributesMap.keySet()) {
                int curve = 0;
                if (interpolation.containsKey(spline)) {
                    curve = interpolation.get(spline);
                }
                this.mTimeCycleAttributesMap.get(spline).setup(curve);
            }
        }
        final MotionPaths[] points = new MotionPaths[2 + this.mMotionPaths.size()];
        int count = 1;
        points[0] = this.mStartMotionPath;
        points[points.length - 1] = this.mEndMotionPath;
        if (this.mMotionPaths.size() > 0 && this.mCurveFitType == -1) {
            this.mCurveFitType = 0;
        }
        for (final MotionPaths point : this.mMotionPaths) {
            points[count++] = point;
        }
        final int variables = 18;
        final HashSet<String> attributeNameSet = new HashSet<String>();
        for (final String s : this.mEndMotionPath.attributes.keySet()) {
            if (this.mStartMotionPath.attributes.containsKey(s) && !splineAttributes.contains("CUSTOM," + s)) {
                attributeNameSet.add(s);
            }
        }
        this.mAttributeNames = attributeNameSet.toArray(new String[0]);
        this.mAttributeInterpCount = new int[this.mAttributeNames.length];
        for (int i = 0; i < this.mAttributeNames.length; ++i) {
            final String attributeName = this.mAttributeNames[i];
            this.mAttributeInterpCount[i] = 1;
            for (int j = 0; j < points.length; ++j) {
                if (points[i].attributes.containsKey(attributeName)) {
                    this.mAttributeInterpCount[i] = points[i].attributes.get(attributeName).noOfInterpValues();
                    break;
                }
            }
        }
        final boolean arcMode = points[0].mPathMotionArc != Key.UNSET;
        final boolean[] mask = new boolean[variables + this.mAttributeNames.length];
        for (int k = 1; k < points.length; ++k) {
            points[k].different(points[k - 1], mask, this.mAttributeNames, arcMode);
        }
        count = 0;
        for (int k = 1; k < mask.length; ++k) {
            if (mask[k]) {
                ++count;
            }
        }
        this.mInterpolateVariables = new int[count];
        this.mInterpolateData = new double[this.mInterpolateVariables.length];
        this.mInterpolateVelocity = new double[this.mInterpolateVariables.length];
        count = 0;
        for (int k = 1; k < mask.length; ++k) {
            if (mask[k]) {
                this.mInterpolateVariables[count++] = k;
            }
        }
        final double[][] splineData = new double[points.length][this.mInterpolateVariables.length];
        final double[] timePoint = new double[points.length];
        for (int l = 0; l < points.length; ++l) {
            points[l].fillStandard(splineData[l], this.mInterpolateVariables);
            timePoint[l] = points[l].time;
        }
        for (int m = 0; m < this.mInterpolateVariables.length; ++m) {
            final int interpolateVariable = this.mInterpolateVariables[m];
            if (interpolateVariable < MotionPaths.names.length) {
                String s2 = MotionPaths.names[this.mInterpolateVariables[m]] + " [";
                for (int i2 = 0; i2 < points.length; ++i2) {
                    s2 += splineData[i2][m];
                }
            }
        }
        this.mSpline = new CurveFit[1 + this.mAttributeNames.length];
        for (int l = 0; l < this.mAttributeNames.length; ++l) {
            int pointCount = 0;
            double[][] splinePoints = null;
            double[] timePoints = null;
            final String name = this.mAttributeNames[l];
            for (int j2 = 0; j2 < points.length; ++j2) {
                if (points[j2].hasCustomData(name)) {
                    if (splinePoints == null) {
                        timePoints = new double[points.length];
                        splinePoints = new double[points.length][points[j2].getCustomDataCount(name)];
                    }
                    timePoints[pointCount] = points[j2].time;
                    points[j2].getCustomData(name, splinePoints[pointCount], 0);
                    ++pointCount;
                }
            }
            timePoints = Arrays.copyOf(timePoints, pointCount);
            splinePoints = Arrays.copyOf(splinePoints, pointCount);
            this.mSpline[l + 1] = CurveFit.get(this.mCurveFitType, timePoints, splinePoints);
        }
        this.mSpline[0] = CurveFit.get(this.mCurveFitType, timePoint, splineData);
        if (points[0].mPathMotionArc != Key.UNSET) {
            final int size = points.length;
            final int[] mode = new int[size];
            final double[] time = new double[size];
            final double[][] values = new double[size][2];
            for (int i3 = 0; i3 < size; ++i3) {
                mode[i3] = points[i3].mPathMotionArc;
                time[i3] = points[i3].time;
                values[i3][0] = points[i3].x;
                values[i3][1] = points[i3].y;
            }
            this.mArcSpline = CurveFit.getArc(mode, time, values);
        }
        float distance = Float.NaN;
        this.mCycleMap = new HashMap<String, KeyCycleOscillator>();
        if (this.mKeyList != null) {
            for (final String attribute2 : cycleAttributes) {
                final KeyCycleOscillator cycle = KeyCycleOscillator.makeSpline(attribute2);
                if (cycle == null) {
                    continue;
                }
                if (cycle.variesByPath() && Float.isNaN(distance)) {
                    distance = this.getPreCycleDistance();
                }
                cycle.setType(attribute2);
                this.mCycleMap.put(attribute2, cycle);
            }
            for (final Key key3 : this.mKeyList) {
                if (key3 instanceof KeyCycle) {
                    ((KeyCycle)key3).addCycleValues(this.mCycleMap);
                }
            }
            for (final KeyCycleOscillator cycle2 : this.mCycleMap.values()) {
                cycle2.setup(distance);
            }
        }
    }
    
    @Override
    public String toString() {
        return " start: x: " + this.mStartMotionPath.x + " y: " + this.mStartMotionPath.y + " end: x: " + this.mEndMotionPath.x + " y: " + this.mEndMotionPath.y;
    }
    
    private void readView(final MotionPaths motionPaths) {
        motionPaths.setBounds((int)this.mView.getX(), (int)this.mView.getY(), this.mView.getWidth(), this.mView.getHeight());
    }
    
    public void setView(final View view) {
        this.mView = view;
        this.mId = view.getId();
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ConstraintLayout.LayoutParams) {
            this.mConstraintTag = ((ConstraintLayout.LayoutParams)lp).getConstraintTag();
        }
    }
    
    void setStartCurrentState(final View v) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mStartMotionPath.setBounds(v.getX(), v.getY(), v.getWidth(), v.getHeight());
        this.mStartPoint.setState(v);
    }
    
    void setStartState(final ConstraintWidget cw, final ConstraintSet constraintSet) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.readView(this.mStartMotionPath);
        this.mStartMotionPath.setBounds(cw.getX(), cw.getY(), cw.getWidth(), cw.getHeight());
        final ConstraintSet.Constraint constraint = constraintSet.getParameters(this.mId);
        this.mStartMotionPath.applyParameters(constraint);
        this.mMotionStagger = constraint.motion.mMotionStagger;
        this.mStartPoint.setState(cw, constraintSet, this.mId);
    }
    
    void setEndState(final ConstraintWidget cw, final ConstraintSet constraintSet) {
        this.mEndMotionPath.time = 1.0f;
        this.mEndMotionPath.position = 1.0f;
        this.readView(this.mEndMotionPath);
        this.mEndMotionPath.setBounds(cw.getX(), cw.getY(), cw.getWidth(), cw.getHeight());
        this.mEndMotionPath.applyParameters(constraintSet.getParameters(this.mId));
        this.mEndPoint.setState(cw, constraintSet, this.mId);
    }
    
    private float getAdjustedPosition(float position, final float[] velocity) {
        if (velocity != null) {
            velocity[0] = 1.0f;
        }
        else if (this.mStaggerScale != 1.0) {
            if (position < this.mStaggerOffset) {
                position = 0.0f;
            }
            if (position > this.mStaggerOffset && position < 1.0) {
                position -= this.mStaggerOffset;
                position *= this.mStaggerScale;
            }
        }
        float adjusted = position;
        Easing easing = this.mStartMotionPath.mKeyFrameEasing;
        float start = 0.0f;
        float end = Float.NaN;
        for (final MotionPaths frame : this.mMotionPaths) {
            if (frame.mKeyFrameEasing != null) {
                if (frame.time < position) {
                    easing = frame.mKeyFrameEasing;
                    start = frame.time;
                }
                else {
                    if (!Float.isNaN(end)) {
                        continue;
                    }
                    end = frame.time;
                }
            }
        }
        if (easing != null) {
            if (Float.isNaN(end)) {
                end = 1.0f;
            }
            final float offset = (position - start) / (end - start);
            final float new_offset = (float)easing.get(offset);
            adjusted = new_offset * (end - start) + start;
            if (velocity != null) {
                velocity[0] = (float)easing.getDiff(offset);
            }
        }
        return adjusted;
    }
    
    boolean interpolate(final View child, final float global_position, final long time, final KeyCache keyCache) {
        boolean timeAnimation = false;
        final float position = this.getAdjustedPosition(global_position, null);
        TimeCycleSplineSet.PathRotate timePathRotate = null;
        if (this.mAttributesMap != null) {
            for (final SplineSet aSpline : this.mAttributesMap.values()) {
                aSpline.setProperty(child, position);
            }
        }
        if (this.mTimeCycleAttributesMap != null) {
            for (final TimeCycleSplineSet aSpline2 : this.mTimeCycleAttributesMap.values()) {
                if (aSpline2 instanceof TimeCycleSplineSet.PathRotate) {
                    timePathRotate = (TimeCycleSplineSet.PathRotate)aSpline2;
                }
                else {
                    timeAnimation |= aSpline2.setProperty(child, position, time, keyCache);
                }
            }
        }
        if (this.mSpline != null) {
            this.mSpline[0].getPos(position, this.mInterpolateData);
            this.mSpline[0].getSlope(position, this.mInterpolateVelocity);
            if (this.mArcSpline != null && this.mInterpolateData.length > 0) {
                this.mArcSpline.getPos(position, this.mInterpolateData);
                this.mArcSpline.getSlope(position, this.mInterpolateVelocity);
            }
            this.mStartMotionPath.setView(child, this.mInterpolateVariables, this.mInterpolateData, this.mInterpolateVelocity, null);
            if (this.mAttributesMap != null) {
                for (final SplineSet aSpline : this.mAttributesMap.values()) {
                    if (aSpline instanceof SplineSet.PathRotate) {
                        ((SplineSet.PathRotate)aSpline).setPathRotate(child, position, this.mInterpolateVelocity[0], this.mInterpolateVelocity[1]);
                    }
                }
            }
            if (timePathRotate != null) {
                timeAnimation |= timePathRotate.setPathRotate(child, keyCache, position, time, this.mInterpolateVelocity[0], this.mInterpolateVelocity[1]);
            }
            for (int i = 1; i < this.mSpline.length; ++i) {
                final CurveFit spline = this.mSpline[i];
                spline.getPos(position, this.mValuesBuff);
                this.mStartMotionPath.attributes.get(this.mAttributeNames[i - 1]).setInterpolatedValue(child, this.mValuesBuff);
            }
            if (this.mStartPoint.mVisibilityMode == 0) {
                if (position <= 0.0f) {
                    child.setVisibility(this.mStartPoint.visibility);
                }
                else if (position >= 1.0f) {
                    child.setVisibility(this.mEndPoint.visibility);
                }
                else if (this.mEndPoint.visibility != this.mStartPoint.visibility) {
                    child.setVisibility(View.VISIBLE);
                }
            }
            if (this.mKeyTriggers != null) {
                for (int i = 0; i < this.mKeyTriggers.length; ++i) {
                    this.mKeyTriggers[i].conditionallyFire(position, child);
                }
            }
        }
        else {
            final float float_l = this.mStartMotionPath.x + (this.mEndMotionPath.x - this.mStartMotionPath.x) * position;
            final float float_t = this.mStartMotionPath.y + (this.mEndMotionPath.y - this.mStartMotionPath.y) * position;
            final float float_width = this.mStartMotionPath.width + (this.mEndMotionPath.width - this.mStartMotionPath.width) * position;
            final float float_height = this.mStartMotionPath.height + (this.mEndMotionPath.height - this.mStartMotionPath.height) * position;
            final int l = (int)(0.5f + float_l);
            final int t = (int)(0.5f + float_t);
            final int r = (int)(0.5f + float_l + float_width);
            final int b = (int)(0.5f + float_t + float_height);
            final int width = r - l;
            final int height = b - t;
            if (this.mEndMotionPath.width != this.mStartMotionPath.width || this.mEndMotionPath.height != this.mStartMotionPath.height) {
                final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                child.measure(widthMeasureSpec, heightMeasureSpec);
            }
            child.layout(l, t, r, b);
        }
        if (this.mCycleMap != null) {
            for (final KeyCycleOscillator osc : this.mCycleMap.values()) {
                if (osc instanceof KeyCycleOscillator.PathRotateSet) {
                    ((KeyCycleOscillator.PathRotateSet)osc).setPathRotate(child, position, this.mInterpolateVelocity[0], this.mInterpolateVelocity[1]);
                }
                else {
                    osc.setProperty(child, position);
                }
            }
        }
        return timeAnimation;
    }
    
    void getDpDt(float position, final float locationX, final float locationY, final float[] mAnchorDpDt) {
        position = this.getAdjustedPosition(position, this.mVelocity);
        if (this.mSpline == null) {
            final float dleft = this.mEndMotionPath.x - this.mStartMotionPath.x;
            final float dTop = this.mEndMotionPath.y - this.mStartMotionPath.y;
            final float dWidth = this.mEndMotionPath.width - this.mStartMotionPath.width;
            final float dHeight = this.mEndMotionPath.height - this.mStartMotionPath.height;
            final float dRight = dleft + dWidth;
            final float dBottom = dTop + dHeight;
            mAnchorDpDt[0] = dleft * (1.0f - locationX) + dRight * locationX;
            mAnchorDpDt[1] = dTop * (1.0f - locationY) + dBottom * locationY;
            return;
        }
        this.mSpline[0].getSlope(position, this.mInterpolateVelocity);
        this.mSpline[0].getPos(position, this.mInterpolateData);
        final float v = this.mVelocity[0];
        for (int i = 0; i < this.mInterpolateVelocity.length; ++i) {
            final double[] mInterpolateVelocity = this.mInterpolateVelocity;
            final int n = i;
            mInterpolateVelocity[n] *= v;
        }
        if (this.mArcSpline != null) {
            if (this.mInterpolateData.length > 0) {
                this.mArcSpline.getPos(position, this.mInterpolateData);
                this.mArcSpline.getSlope(position, this.mInterpolateVelocity);
                this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            }
            return;
        }
        this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
    }
    
    void getPostLayoutDvDp(float position, final int width, final int height, final float locationX, final float locationY, final float[] mAnchorDpDt) {
        position = this.getAdjustedPosition(position, this.mVelocity);
        final SplineSet trans_x = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("translationX");
        final SplineSet trans_y = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("translationY");
        final SplineSet rotation = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("rotation");
        final SplineSet scale_x = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("scaleX");
        final SplineSet scale_y = (this.mAttributesMap == null) ? null : this.mAttributesMap.get("scaleY");
        final KeyCycleOscillator osc_x = (this.mCycleMap == null) ? null : this.mCycleMap.get("translationX");
        final KeyCycleOscillator osc_y = (this.mCycleMap == null) ? null : this.mCycleMap.get("translationY");
        final KeyCycleOscillator osc_r = (this.mCycleMap == null) ? null : this.mCycleMap.get("rotation");
        final KeyCycleOscillator osc_sx = (this.mCycleMap == null) ? null : this.mCycleMap.get("scaleX");
        final KeyCycleOscillator osc_sy = (this.mCycleMap == null) ? null : this.mCycleMap.get("scaleY");
        final VelocityMatrix vmat = new VelocityMatrix();
        vmat.clear();
        vmat.setRotationVelocity(rotation, position);
        vmat.setTranslationVelocity(trans_x, trans_y, position);
        vmat.setScaleVelocity(scale_x, scale_y, position);
        vmat.setRotationVelocity(osc_r, position);
        vmat.setTranslationVelocity(osc_x, osc_y, position);
        vmat.setScaleVelocity(osc_sx, osc_sy, position);
        if (this.mArcSpline != null) {
            if (this.mInterpolateData.length > 0) {
                this.mArcSpline.getPos(position, this.mInterpolateData);
                this.mArcSpline.getSlope(position, this.mInterpolateVelocity);
                this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            }
            vmat.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
            return;
        }
        if (this.mSpline != null) {
            position = this.getAdjustedPosition(position, this.mVelocity);
            this.mSpline[0].getSlope(position, this.mInterpolateVelocity);
            this.mSpline[0].getPos(position, this.mInterpolateData);
            final float v = this.mVelocity[0];
            for (int i = 0; i < this.mInterpolateVelocity.length; ++i) {
                final double[] mInterpolateVelocity = this.mInterpolateVelocity;
                final int n = i;
                mInterpolateVelocity[n] *= v;
            }
            this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            vmat.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
            return;
        }
        final float dleft = this.mEndMotionPath.x - this.mStartMotionPath.x;
        final float dTop = this.mEndMotionPath.y - this.mStartMotionPath.y;
        final float dWidth = this.mEndMotionPath.width - this.mStartMotionPath.width;
        final float dHeight = this.mEndMotionPath.height - this.mStartMotionPath.height;
        final float dRight = dleft + dWidth;
        final float dBottom = dTop + dHeight;
        mAnchorDpDt[0] = dleft * (1.0f - locationX) + dRight * locationX;
        mAnchorDpDt[1] = dTop * (1.0f - locationY) + dBottom * locationY;
        vmat.clear();
        vmat.setRotationVelocity(rotation, position);
        vmat.setTranslationVelocity(trans_x, trans_y, position);
        vmat.setScaleVelocity(scale_x, scale_y, position);
        vmat.setRotationVelocity(osc_r, position);
        vmat.setTranslationVelocity(osc_x, osc_y, position);
        vmat.setScaleVelocity(osc_sx, osc_sy, position);
        vmat.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
    }
    
    public int getDrawPath() {
        int mode = this.mStartMotionPath.mDrawPath;
        for (final MotionPaths keyFrame : this.mMotionPaths) {
            mode = Math.max(mode, keyFrame.mDrawPath);
        }
        mode = Math.max(mode, this.mEndMotionPath.mDrawPath);
        return mode;
    }
    
    public void setDrawPath(final int debugMode) {
        this.mStartMotionPath.mDrawPath = debugMode;
    }
    
    String name() {
        final Context context = this.mView.getContext();
        return context.getResources().getResourceEntryName(this.mView.getId());
    }
    
    void positionKeyframe(final View view, final KeyPositionBase key, final float x, final float y, final String[] attribute, final float[] value) {
        final RectF start = new RectF();
        start.left = this.mStartMotionPath.x;
        start.top = this.mStartMotionPath.y;
        start.right = start.left + this.mStartMotionPath.width;
        start.bottom = start.top + this.mStartMotionPath.height;
        final RectF end = new RectF();
        end.left = this.mEndMotionPath.x;
        end.top = this.mEndMotionPath.y;
        end.right = end.left + this.mEndMotionPath.width;
        end.bottom = end.top + this.mEndMotionPath.height;
        key.positionAttributes(view, start, end, x, y, attribute, value);
    }
    
    public int getkeyFramePositions(final int[] type, final float[] pos) {
        int i = 0;
        int count = 0;
        for (final Key key : this.mKeyList) {
            type[i++] = key.mFramePosition + 1000 * key.mType;
            final float time = key.mFramePosition / 100.0f;
            this.mSpline[0].getPos(time, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, pos, count);
            count += 2;
        }
        return i;
    }
}
