package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.motion.utils.*;
import android.view.*;
import android.support.annotation.*;
import com.smart.library.support.constraint.*;
import java.util.*;

class MotionPaths implements Comparable<MotionPaths>
{
    public static final String TAG = "MotionPaths";
    public static final boolean DEBUG = false;
    public static final boolean OLD_WAY = false;
    static final int OFF_POSITION = 0;
    static final int OFF_X = 1;
    static final int OFF_Y = 2;
    static final int OFF_WIDTH = 3;
    static final int OFF_HEIGHT = 4;
    static final int OFF_PATH_ROTATE = 5;
    static final int PERPENDICULAR = 1;
    static final int CARTESIAN = 2;
    static final int SCREEN = 3;
    static String[] names;
    Easing mKeyFrameEasing;
    int mDrawPath;
    float time;
    float position;
    float x;
    float y;
    float width;
    float height;
    float mPathRotate;
    float mProgress;
    int mPathMotionArc;
    LinkedHashMap<String, ConstraintAttribute> attributes;
    int mMode;
    double[] mTempValue;
    double[] mTempDelta;
    
    public MotionPaths() {
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.mPathMotionArc = Key.UNSET;
        this.attributes = new LinkedHashMap<String, ConstraintAttribute>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }
    
    void initCartesian(final KeyPosition c, final MotionPaths startTimePoint, final MotionPaths endTimePoint) {
        final float position = c.mFramePosition / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        final float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        final float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        final float scaleX = endTimePoint.width - startTimePoint.width;
        final float scaleY = endTimePoint.height - startTimePoint.height;
        this.position = this.time;
        final float path = position;
        final float startCenterX = startTimePoint.x + startTimePoint.width / 2.0f;
        final float startCenterY = startTimePoint.y + startTimePoint.height / 2.0f;
        final float endCenterX = endTimePoint.x + endTimePoint.width / 2.0f;
        final float endCenterY = endTimePoint.y + endTimePoint.height / 2.0f;
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        this.x = (int)(startTimePoint.x + pathVectorX * path - scaleX * scaleWidth / 2.0f);
        this.y = (int)(startTimePoint.y + pathVectorY * path - scaleY * scaleHeight / 2.0f);
        this.width = (int)(startTimePoint.width + scaleX * scaleWidth);
        this.height = (int)(startTimePoint.height + scaleY * scaleHeight);
        final float dxdx = Float.isNaN(c.mPercentX) ? position : c.mPercentX;
        final float dydx = Float.isNaN(c.mAltPercentY) ? 0.0f : c.mAltPercentY;
        final float dydy = Float.isNaN(c.mPercentY) ? position : c.mPercentY;
        final float dxdy = Float.isNaN(c.mAltPercentX) ? 0.0f : c.mAltPercentX;
        this.mMode = 2;
        this.x = (int)(startTimePoint.x + pathVectorX * dxdx + pathVectorY * dxdy - scaleX * scaleWidth / 2.0f);
        this.y = (int)(startTimePoint.y + pathVectorX * dydx + pathVectorY * dydy - scaleY * scaleHeight / 2.0f);
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }
    
    public MotionPaths(final int parentWidth, final int parentHeight, final KeyPosition c, final MotionPaths startTimePoint, final MotionPaths endTimePoint) {
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.mPathMotionArc = Key.UNSET;
        this.attributes = new LinkedHashMap<String, ConstraintAttribute>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
        switch (c.mPositionType) {
            case 2: {
                this.initScreen(parentWidth, parentHeight, c, startTimePoint, endTimePoint);
            }
            case 1: {
                this.initPath(c, startTimePoint, endTimePoint);
            }
            default: {
                this.initCartesian(c, startTimePoint, endTimePoint);
            }
        }
    }
    
    void initScreen(int parentWidth, int parentHeight, final KeyPosition c, final MotionPaths startTimePoint, final MotionPaths endTimePoint) {
        final float position = c.mFramePosition / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        final float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        final float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        final float scaleX = endTimePoint.width - startTimePoint.width;
        final float scaleY = endTimePoint.height - startTimePoint.height;
        this.position = this.time;
        final float path = position;
        final float startCenterX = startTimePoint.x + startTimePoint.width / 2.0f;
        final float startCenterY = startTimePoint.y + startTimePoint.height / 2.0f;
        final float endCenterX = endTimePoint.x + endTimePoint.width / 2.0f;
        final float endCenterY = endTimePoint.y + endTimePoint.height / 2.0f;
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        this.x = (int)(startTimePoint.x + pathVectorX * path - scaleX * scaleWidth / 2.0f);
        this.y = (int)(startTimePoint.y + pathVectorY * path - scaleY * scaleHeight / 2.0f);
        this.width = (int)(startTimePoint.width + scaleX * scaleWidth);
        this.height = (int)(startTimePoint.height + scaleY * scaleHeight);
        this.mMode = 3;
        if (!Float.isNaN(c.mPercentX)) {
            parentWidth -= (int)this.width;
            this.x = (int)(c.mPercentX * parentWidth);
        }
        if (!Float.isNaN(c.mPercentY)) {
            parentHeight -= (int)this.height;
            this.y = (int)(c.mPercentY * parentHeight);
        }
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }
    
    void initPath(final KeyPosition c, final MotionPaths startTimePoint, final MotionPaths endTimePoint) {
        final float position = c.mFramePosition / 100.0f;
        this.time = position;
        this.mDrawPath = c.mDrawPath;
        final float scaleWidth = Float.isNaN(c.mPercentWidth) ? position : c.mPercentWidth;
        final float scaleHeight = Float.isNaN(c.mPercentHeight) ? position : c.mPercentHeight;
        final float scaleX = endTimePoint.width - startTimePoint.width;
        final float scaleY = endTimePoint.height - startTimePoint.height;
        this.position = this.time;
        final float path = Float.isNaN(c.mPercentX) ? position : c.mPercentX;
        final float startCenterX = startTimePoint.x + startTimePoint.width / 2.0f;
        final float startCenterY = startTimePoint.y + startTimePoint.height / 2.0f;
        final float endCenterX = endTimePoint.x + endTimePoint.width / 2.0f;
        final float endCenterY = endTimePoint.y + endTimePoint.height / 2.0f;
        final float pathVectorX = endCenterX - startCenterX;
        final float pathVectorY = endCenterY - startCenterY;
        this.x = (int)(startTimePoint.x + pathVectorX * path - scaleX * scaleWidth / 2.0f);
        this.y = (int)(startTimePoint.y + pathVectorY * path - scaleY * scaleHeight / 2.0f);
        this.width = (int)(startTimePoint.width + scaleX * scaleWidth);
        this.height = (int)(startTimePoint.height + scaleY * scaleHeight);
        final float perpendicular = Float.isNaN(c.mPercentY) ? 0.0f : c.mPercentY;
        final float perpendicularX = -pathVectorY;
        final float perpendicularY = pathVectorX;
        final float normalX = perpendicularX * perpendicular;
        final float normalY = perpendicularY * perpendicular;
        this.mMode = 1;
        this.x = (int)(startTimePoint.x + pathVectorX * path - scaleX * scaleWidth / 2.0f);
        this.y = (int)(startTimePoint.y + pathVectorY * path - scaleY * scaleHeight / 2.0f);
        this.x += normalX;
        this.y += normalY;
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }
    
    private static final float xRotate(final float sin, final float cos, final float cx, final float cy, float x, float y) {
        x -= cx;
        y -= cy;
        return x * cos - y * sin + cx;
    }
    
    private static final float yRotate(final float sin, final float cos, final float cx, final float cy, float x, float y) {
        x -= cx;
        y -= cy;
        return x * sin + y * cos + cy;
    }
    
    private boolean diff(final float a, final float b) {
        if (Float.isNaN(a) || Float.isNaN(b)) {
            return Float.isNaN(a) != Float.isNaN(b);
        }
        return Math.abs(a - b) > 1.0E-6f;
    }
    
    void different(final MotionPaths points, final boolean[] mask, final String[] custom, final boolean arcMode) {
        int c = 0;
        final int n = c++;
        mask[n] |= this.diff(this.position, points.position);
        final int n2 = c++;
        mask[n2] |= (this.diff(this.x, points.x) | arcMode);
        final int n3 = c++;
        mask[n3] |= (this.diff(this.y, points.y) | arcMode);
        final int n4 = c++;
        mask[n4] |= this.diff(this.width, points.width);
        final int n5 = c++;
        mask[n5] |= this.diff(this.height, points.height);
    }
    
    void getCenter(final int[] toUse, final double[] data, final float[] point, final int offset) {
        float v_x = this.x;
        float v_y = this.y;
        float v_width = this.width;
        float v_height = this.height;
        final float translationX = 0.0f;
        final float translationY = 0.0f;
        for (int i = 0; i < toUse.length; ++i) {
            final float value = (float)data[i];
            switch (toUse[i]) {
                case 1: {
                    v_x = value;
                    break;
                }
                case 2: {
                    v_y = value;
                    break;
                }
                case 3: {
                    v_width = value;
                    break;
                }
                case 4: {
                    v_height = value;
                    break;
                }
            }
        }
        point[offset] = v_x + v_width / 2.0f + translationX;
        point[offset + 1] = v_y + v_height / 2.0f + translationY;
    }
    
    void setView(final View view, final int[] toUse, final double[] data, final double[] slope, final double[] cycle) {
        float v_x = this.x;
        float v_y = this.y;
        float v_width = this.width;
        float v_height = this.height;
        float dv_x = 0.0f;
        float dv_y = 0.0f;
        float dv_width = 0.0f;
        float dv_height = 0.0f;
        float delta_path = 0.0f;
        final float view_rotate = Float.NaN;
        float path_rotate = Float.NaN;
        if (toUse.length != 0 && this.mTempValue.length <= toUse[toUse.length - 1]) {
            final int scratch_data_length = toUse[toUse.length - 1] + 1;
            this.mTempValue = new double[scratch_data_length];
            this.mTempDelta = new double[scratch_data_length];
        }
        Arrays.fill(this.mTempValue, Double.NaN);
        for (int i = 0; i < toUse.length; ++i) {
            this.mTempValue[toUse[i]] = data[i];
            this.mTempDelta[toUse[i]] = slope[i];
        }
        for (int i = 0; i < this.mTempValue.length; ++i) {
            if (Double.isNaN(this.mTempValue[i])) {
                if (cycle == null) {
                    continue;
                }
                if (cycle[i] == 0.0) {
                    continue;
                }
            }
            final double deltaCycle = (cycle != null) ? cycle[i] : 0.0;
            final float value = (float)(Double.isNaN(this.mTempValue[i]) ? deltaCycle : (this.mTempValue[i] + deltaCycle));
            final float dvalue = (float)this.mTempDelta[i];
            switch (i) {
                case 0: {
                    delta_path = value;
                    break;
                }
                case 1: {
                    v_x = value;
                    dv_x = dvalue;
                    break;
                }
                case 2: {
                    v_y = value;
                    dv_y = dvalue;
                    break;
                }
                case 3: {
                    v_width = value;
                    dv_width = dvalue;
                    break;
                }
                case 4: {
                    v_height = value;
                    dv_height = dvalue;
                    break;
                }
                case 5: {
                    path_rotate = value;
                    break;
                }
            }
        }
        if (Float.isNaN(path_rotate)) {
            if (!Float.isNaN(view_rotate)) {
                view.setRotation(view_rotate);
            }
        }
        else {
            float rot = Float.isNaN(view_rotate) ? 0.0f : view_rotate;
            final float dx = dv_x + dv_width / 2.0f;
            final float dy = dv_y + dv_height / 2.0f;
            rot += (float)(path_rotate + Math.toDegrees(Math.atan2(dy, dx)));
            view.setRotation(rot);
        }
        final int l = (int)(0.5f + v_x);
        final int t = (int)(0.5f + v_y);
        final int r = (int)(0.5f + v_x + v_width);
        final int b = (int)(0.5f + v_y + v_height);
        final int i_width = r - l;
        final int i_height = b - t;
        if (i_width != view.getWidth() || i_height != view.getHeight()) {
            final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(i_width, View.MeasureSpec.EXACTLY);
            final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(i_height, View.MeasureSpec.EXACTLY);
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
        view.layout(l, t, r, b);
    }
    
    void getRect(final int[] toUse, final double[] data, final float[] path, int offset) {
        float v_x = this.x;
        float v_y = this.y;
        float v_width = this.width;
        float v_height = this.height;
        float delta_path = 0.0f;
        final float rotation = 0.0f;
        final float alpha = 0.0f;
        final float rotationX = 0.0f;
        final float rotationY = 0.0f;
        final float scaleX = 1.0f;
        final float scaleY = 1.0f;
        final float pivotX = Float.NaN;
        final float pivotY = Float.NaN;
        final float translationX = 0.0f;
        final float translationY = 0.0f;
        for (int i = 0; i < toUse.length; ++i) {
            final float value = (float)data[i];
            switch (toUse[i]) {
                case 0: {
                    delta_path = value;
                    break;
                }
                case 1: {
                    v_x = value;
                    break;
                }
                case 2: {
                    v_y = value;
                    break;
                }
                case 3: {
                    v_width = value;
                    break;
                }
                case 4: {
                    v_height = value;
                    break;
                }
            }
        }
        float x1 = v_x;
        float y1 = v_y;
        float x2 = v_x + v_width;
        float y2 = y1;
        float x3 = x2;
        float y3 = v_y + v_height;
        float x4 = x1;
        float y4 = y3;
        float cx = x1 + v_width / 2.0f;
        float cy = y1 + v_height / 2.0f;
        if (!Float.isNaN(pivotX)) {
            cx = x1 + (x2 - x1) * pivotX;
        }
        if (!Float.isNaN(pivotY)) {
            cy = y1 + (y3 - y1) * pivotY;
        }
        if (scaleX != 1.0f) {
            final float midx = (x1 + x2) / 2.0f;
            x1 = (x1 - midx) * scaleX + midx;
            x2 = (x2 - midx) * scaleX + midx;
            x3 = (x3 - midx) * scaleX + midx;
            x4 = (x4 - midx) * scaleX + midx;
        }
        if (scaleY != 1.0f) {
            final float midy = (y1 + y3) / 2.0f;
            y1 = (y1 - midy) * scaleY + midy;
            y2 = (y2 - midy) * scaleY + midy;
            y3 = (y3 - midy) * scaleY + midy;
            y4 = (y4 - midy) * scaleY + midy;
        }
        if (rotation != 0.0f) {
            final float sin = (float)Math.sin(Math.toRadians(rotation));
            final float cos = (float)Math.cos(Math.toRadians(rotation));
            final float tx1 = xRotate(sin, cos, cx, cy, x1, y1);
            final float ty1 = yRotate(sin, cos, cx, cy, x1, y1);
            final float tx2 = xRotate(sin, cos, cx, cy, x2, y2);
            final float ty2 = yRotate(sin, cos, cx, cy, x2, y2);
            final float tx3 = xRotate(sin, cos, cx, cy, x3, y3);
            final float ty3 = yRotate(sin, cos, cx, cy, x3, y3);
            final float tx4 = xRotate(sin, cos, cx, cy, x4, y4);
            final float ty4 = yRotate(sin, cos, cx, cy, x4, y4);
            x1 = tx1;
            y1 = ty1;
            x2 = tx2;
            y2 = ty2;
            x3 = tx3;
            y3 = ty3;
            x4 = tx4;
            y4 = ty4;
        }
        x1 += translationX;
        y1 += translationY;
        x2 += translationX;
        y2 += translationY;
        x3 += translationX;
        y3 += translationY;
        x4 += translationX;
        y4 += translationY;
        path[offset++] = x1;
        path[offset++] = y1;
        path[offset++] = x2;
        path[offset++] = y2;
        path[offset++] = x3;
        path[offset++] = y3;
        path[offset++] = x4;
        path[offset++] = y4;
    }
    
    void setDpDt(final float locationX, final float locationY, final float[] mAnchorDpDt, final int[] toUse, final double[] deltaData, final double[] data) {
        float d_x = 0.0f;
        float d_y = 0.0f;
        float d_width = 0.0f;
        float d_height = 0.0f;
        final float deltaScaleX = 0.0f;
        final float deltaScaleY = 0.0f;
        final float mPathRotate = Float.NaN;
        final float deltaTranslationX = 0.0f;
        final float deltaTranslationY = 0.0f;
        final String mod = " dd = ";
        for (int i = 0; i < toUse.length; ++i) {
            final float deltaV = (float)deltaData[i];
            final float value = (float)data[i];
            switch (toUse[i]) {
                case 1: {
                    d_x = deltaV;
                    break;
                }
                case 2: {
                    d_y = deltaV;
                    break;
                }
                case 3: {
                    d_width = deltaV;
                    break;
                }
                case 4: {
                    d_height = deltaV;
                    break;
                }
            }
        }
        final float deltaX = d_x - deltaScaleX * d_width / 2.0f;
        final float deltaY = d_y - deltaScaleY * d_height / 2.0f;
        final float deltaWidth = d_width * (1.0f + deltaScaleX);
        final float deltaHeight = d_height * (1.0f + deltaScaleY);
        final float deltaRight = deltaX + deltaWidth;
        final float deltaBottom = deltaY + deltaHeight;
        mAnchorDpDt[0] = deltaX * (1.0f - locationX) + deltaRight * locationX + deltaTranslationX;
        mAnchorDpDt[1] = deltaY * (1.0f - locationY) + deltaBottom * locationY + deltaTranslationY;
    }
    
    void fillStandard(final double[] data, final int[] toUse) {
        final float[] set = { this.position, this.x, this.y, this.width, this.height, this.mPathRotate };
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
    public int compareTo(@NonNull final MotionPaths o) {
        return Float.compare(this.position, o.position);
    }
    
    public void applyParameters(final ConstraintSet.Constraint c) {
        this.mKeyFrameEasing = Easing.getInterpolator(c.motion.mTransitionEasing);
        this.mPathMotionArc = c.motion.mPathMotionArc;
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
    
    static {
        MotionPaths.names = new String[] { "position", "x", "y", "width", "height", "pathRotate" };
    }
}
