package com.smart.library.support.constraint.motion;

import java.lang.reflect.*;
import android.graphics.*;
import android.content.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import java.util.*;
import android.view.*;
import android.util.*;

public class KeyTrigger extends Key
{
    static final String NAME = "KeyTrigger";
    private static final String TAG = "KeyTrigger";
    private int mCurveFit;
    private String mCross;
    private int mTriggerReceiver;
    private String mNegativeCross;
    private String mPositiveCross;
    private int mTriggerID;
    private int mTriggerCollisionId;
    private View mTriggerCollisionView;
    float mTriggerSlack;
    private boolean mFireCrossReset;
    private boolean mFireNegativeReset;
    private boolean mFirePositiveReset;
    private float mFireThreshold;
    private Method mFireCross;
    private Method mFireNegativeCross;
    private Method mFirePositiveCross;
    private float mFireLastPos;
    private boolean mPostLayout;
    RectF mCollisionRect;
    RectF mTargetRect;
    public static final int KEY_TYPE = 5;
    
    public KeyTrigger() {
        this.mCurveFit = -1;
        this.mCross = null;
        this.mTriggerReceiver = KeyTrigger.UNSET;
        this.mNegativeCross = null;
        this.mPositiveCross = null;
        this.mTriggerID = KeyTrigger.UNSET;
        this.mTriggerCollisionId = KeyTrigger.UNSET;
        this.mTriggerCollisionView = null;
        this.mTriggerSlack = 0.1f;
        this.mFireCrossReset = true;
        this.mFireNegativeReset = true;
        this.mFirePositiveReset = true;
        this.mFireThreshold = Float.NaN;
        this.mPostLayout = false;
        this.mCollisionRect = new RectF();
        this.mTargetRect = new RectF();
        this.mType = 5;
        this.mCustomConstraints = new HashMap<String, ConstraintAttribute>();
    }
    
    public void load(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyTrigger);
        Loader.read(this, a, context);
    }
    
    int getCurveFit() {
        return this.mCurveFit;
    }
    
    public void getAttributeNames(final HashSet<String> attributes) {
    }
    
    @Override
    public void addValues(final HashMap<String, SplineSet> splines) {
    }
    
    @Override
    public void setValue(final String tag, final Object value) {
    }
    
    private void setUpRect(final RectF rect, final View child, final boolean postLayout) {
        rect.top = child.getTop();
        rect.bottom = child.getBottom();
        rect.left = child.getLeft();
        rect.right = child.getRight();
        if (postLayout) {
            child.getMatrix().mapRect(rect);
        }
    }
    
    public void conditionallyFire(final float pos, final View child) {
        boolean fireCross = false;
        boolean fireNegative = false;
        boolean firePositive = false;
        if (this.mTriggerCollisionId != KeyTrigger.UNSET) {
            if (this.mTriggerCollisionView == null) {
                this.mTriggerCollisionView = ((ViewGroup)child.getParent()).findViewById(this.mTriggerCollisionId);
            }
            this.setUpRect(this.mCollisionRect, this.mTriggerCollisionView, this.mPostLayout);
            this.setUpRect(this.mTargetRect, child, this.mPostLayout);
            final boolean in = this.mCollisionRect.intersect(this.mTargetRect);
            if (in) {
                if (this.mFireCrossReset) {
                    fireCross = true;
                    this.mFireCrossReset = false;
                }
                if (this.mFirePositiveReset) {
                    firePositive = true;
                    this.mFirePositiveReset = false;
                }
                this.mFireNegativeReset = true;
            }
            else {
                if (!this.mFireCrossReset) {
                    fireCross = true;
                    this.mFireCrossReset = true;
                }
                if (this.mFireNegativeReset) {
                    fireNegative = true;
                    this.mFireNegativeReset = false;
                }
                this.mFirePositiveReset = true;
            }
        }
        else {
            if (this.mFireCrossReset) {
                final float offset = pos - this.mFireThreshold;
                final float lastOffset = this.mFireLastPos - this.mFireThreshold;
                if (offset * lastOffset < 0.0f) {
                    fireCross = true;
                    this.mFireCrossReset = false;
                }
            }
            else if (Math.abs(pos - this.mFireThreshold) > this.mTriggerSlack) {
                this.mFireCrossReset = true;
            }
            if (this.mFireNegativeReset) {
                final float offset = pos - this.mFireThreshold;
                final float lastOffset = this.mFireLastPos - this.mFireThreshold;
                if (offset * lastOffset < 0.0f && offset < 0.0f) {
                    fireNegative = true;
                    this.mFireNegativeReset = false;
                }
            }
            else if (Math.abs(pos - this.mFireThreshold) > this.mTriggerSlack) {
                this.mFireNegativeReset = true;
            }
            if (this.mFirePositiveReset) {
                final float offset = pos - this.mFireThreshold;
                final float lastOffset = this.mFireLastPos - this.mFireThreshold;
                if (offset * lastOffset < 0.0f && offset > 0.0f) {
                    firePositive = true;
                    this.mFirePositiveReset = false;
                }
            }
            else if (Math.abs(pos - this.mFireThreshold) > this.mTriggerSlack) {
                this.mFirePositiveReset = true;
            }
        }
        this.mFireLastPos = pos;
        if (fireNegative || fireCross || firePositive) {
            ((MotionLayout)child.getParent()).fireTrigger(this.mTriggerID, firePositive, pos);
        }
        final View call = (this.mTriggerReceiver == KeyTrigger.UNSET) ? child : ((MotionLayout)child.getParent()).findViewById(this.mTriggerReceiver);
        if (fireNegative && this.mNegativeCross != null) {
            if (this.mFireNegativeCross == null) {
                try {
                    this.mFireNegativeCross = call.getClass().getMethod(this.mNegativeCross, (Class<?>[])new Class[0]);
                }
                catch (NoSuchMethodException e) {
                    Log.e("KeyTrigger", "Could not find method \"" + this.mNegativeCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
                }
            }
            try {
                this.mFireNegativeCross.invoke(call, new Object[0]);
            }
            catch (Exception e2) {
                Log.e("KeyTrigger", "Exception in call \"" + this.mNegativeCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
            }
        }
        if (firePositive && this.mPositiveCross != null) {
            if (this.mFirePositiveCross == null) {
                try {
                    this.mFirePositiveCross = call.getClass().getMethod(this.mPositiveCross, (Class<?>[])new Class[0]);
                }
                catch (NoSuchMethodException e) {
                    Log.e("KeyTrigger", "Could not find method \"" + this.mPositiveCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
                }
            }
            try {
                this.mFirePositiveCross.invoke(call, new Object[0]);
            }
            catch (Exception e2) {
                Log.e("KeyTrigger", "Exception in call \"" + this.mPositiveCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
            }
        }
        if (fireCross && this.mCross != null) {
            if (this.mFireCross == null) {
                try {
                    this.mFireCross = call.getClass().getMethod(this.mCross, (Class<?>[])new Class[0]);
                }
                catch (NoSuchMethodException e) {
                    Log.e("KeyTrigger", "Could not find method \"" + this.mCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
                }
            }
            try {
                this.mFireCross.invoke(call, new Object[0]);
            }
            catch (Exception e2) {
                Log.e("KeyTrigger", "Exception in call \"" + this.mCross + "\"on class " + call.getClass().getSimpleName() + " " + Debug.getName(call));
            }
        }
    }
    
    private static class Loader
    {
        private static final int NEGATIVE_CROSS = 1;
        private static final int POSITIVE_CROSS = 2;
        private static final int CROSS = 4;
        private static final int TRIGGER_SLACK = 5;
        private static final int TRIGGER_ID = 6;
        private static final int TARGET_ID = 7;
        private static final int FRAME_POS = 8;
        private static final int COLLISION = 9;
        private static final int POST_LAYOUT = 10;
        private static final int TRIGGER_RECEIVER = 11;
        private static SparseIntArray mAttrMap;
        
        public static void read(final KeyTrigger c, final TypedArray a, final Context context) {
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Loader.mAttrMap.get(attr)) {
                    case 8: {
                        c.mFramePosition = a.getInteger(attr, c.mFramePosition);
                        c.mFireThreshold = (c.mFramePosition + 0.5f) / 100.0f;
                        continue;
                    }
                    case 7: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            if (c.mTargetId == -1) {
                                c.mTargetString = a.getString(attr);
                            }
                            continue;
                        }
                        else {
                            if (a.peekValue(attr).type == 3) {
                                c.mTargetString = a.getString(attr);
                                continue;
                            }
                            c.mTargetId = a.getResourceId(attr, c.mTargetId);
                            continue;
                        }
                    }
                    case 1: {
                        c.mNegativeCross = a.getString(attr);
                        continue;
                    }
                    case 2: {
                        c.mPositiveCross = a.getString(attr);
                        continue;
                    }
                    case 4: {
                        c.mCross = a.getString(attr);
                        continue;
                    }
                    case 5: {
                        c.mTriggerSlack = a.getFloat(attr, c.mTriggerSlack);
                        continue;
                    }
                    case 6: {
                        c.mTriggerID = a.getResourceId(attr, c.mTriggerID);
                        continue;
                    }
                    case 9: {
                        c.mTriggerCollisionId = a.getResourceId(attr, c.mTriggerCollisionId);
                        continue;
                    }
                    case 10: {
                        c.mPostLayout = a.getBoolean(attr, c.mPostLayout);
                        continue;
                    }
                    case 11: {
                        c.mTriggerReceiver = a.getResourceId(attr, c.mTriggerReceiver);
                        break;
                    }
                }
                Log.e("KeyTrigger", "unused attribute 0x" + Integer.toHexString(attr) + "   " + Loader.mAttrMap.get(attr));
            }
        }
        
        static {
            (Loader.mAttrMap = new SparseIntArray()).append(R.styleable.KeyTrigger_framePosition, 8);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_onCross, 4);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_onNegativeCross, 1);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_onPositiveCross, 2);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_motionTarget, 7);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_triggerId, 6);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_triggerSlack, 5);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_motion_triggerOnCollision, 9);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_motion_postLayoutCollision, 10);
            Loader.mAttrMap.append(R.styleable.KeyTrigger_triggerReceiver, 11);
        }
    }
}
