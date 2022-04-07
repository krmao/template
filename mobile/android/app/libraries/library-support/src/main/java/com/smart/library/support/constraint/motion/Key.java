package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.*;
import android.content.*;
import android.util.*;
import java.util.*;

public abstract class Key
{
    public static int UNSET;
    int mFramePosition;
    int mTargetId;
    String mTargetString;
    protected int mType;
    HashMap<String, ConstraintAttribute> mCustomConstraints;
    static final String ALPHA = "alpha";
    static final String ELEVATION = "elevation";
    static final String ROTATION = "rotation";
    static final String ROTATION_X = "rotationX";
    static final String ROTATION_Y = "rotationY";
    static final String TRANSITION_PATH_ROTATE = "transitionPathRotate";
    static final String SCALE_X = "scaleX";
    static final String SCALE_Y = "scaleY";
    static final String WAVE_PERIOD = "wavePeriod";
    static final String WAVE_OFFSET = "waveOffset";
    static final String WAVE_VARIES_BY = "waveVariesBy";
    static final String TRANSLATION_X = "translationX";
    static final String TRANSLATION_Y = "translationY";
    static final String TRANSLATION_Z = "translationZ";
    static final String PROGRESS = "progress";
    static final String CUSTOM = "CUSTOM";
    
    public Key() {
        this.mFramePosition = Key.UNSET;
        this.mTargetId = Key.UNSET;
        this.mTargetString = null;
    }
    
    abstract void load(final Context p0, final AttributeSet p1);
    
    abstract void getAttributeNames(final HashSet<String> p0);
    
    boolean matches(final String constraintTag) {
        return this.mTargetString != null && constraintTag != null && constraintTag.matches(this.mTargetString);
    }
    
    public abstract void addValues(final HashMap<String, SplineSet> p0);
    
    public abstract void setValue(final String p0, final Object p1);
    
    float toFloat(final Object value) {
        return (float)((value instanceof Float) ? value : Float.parseFloat(value.toString()));
    }
    
    int toInt(final Object value) {
        return (int)((value instanceof Integer) ? value : Integer.parseInt(value.toString()));
    }
    
    boolean toBoolean(final Object value) {
        return (boolean)((value instanceof Boolean) ? value : Boolean.parseBoolean(value.toString()));
    }
    
    public void setInterpolation(final HashMap<String, Integer> interpolation) {
    }
    
    static {
        Key.UNSET = -1;
    }
}
