package com.smart.library.support.constraint;

import android.os.*;
import android.content.*;
import android.view.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.motion.Debug;
import com.smart.library.support.constraint.solver.widgets.*;
import android.graphics.*;
import java.io.*;
import org.xmlpull.v1.*;
import android.content.res.*;
import com.smart.library.support.constraint.motion.utils.*;
import android.util.*;
import com.smart.library.support.constraint.motion.*;
import java.util.*;
import java.lang.reflect.*;

public class ConstraintSet
{
    private static final String TAG = "ConstraintSet";
    private static final String ERROR_MESSAGE = "XML parser error must be within a Constraint ";
    private boolean mValidate;
    private HashMap<String, ConstraintAttribute> mSavedAttributes;
    private boolean mForceId;
    public static final int UNSET = -1;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int WRAP_CONTENT = -2;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int PARENT_ID = 0;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL_GUIDELINE = 0;
    public static final int VERTICAL_GUIDELINE = 1;
    public static final int VISIBLE = 0;
    public static final int INVISIBLE = 4;
    public static final int GONE = 8;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    public static final int BASELINE = 5;
    public static final int START = 6;
    public static final int END = 7;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static final int VISIBILITY_MODE_NORMAL = 0;
    public static final int VISIBILITY_MODE_IGNORE = 1;
    public static final int CHAIN_PACKED = 2;
    private static final boolean DEBUG = false;
    private static final int[] VISIBILITY_FLAGS;
    private static final int BARRIER_TYPE = 1;
    private HashMap<Integer, Constraint> mConstraints;
    private static SparseIntArray mapToConstant;
    private static final int BASELINE_TO_BASELINE = 1;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    private static final int DIMENSION_RATIO = 5;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    private static final int HORIZONTAL_BIAS = 20;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    private static final int ORIENTATION = 27;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int VERTICAL_BIAS = 37;
    private static final int VIEW_ID = 38;
    private static final int HORIZONTAL_WEIGHT = 39;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int HORIZONTAL_STYLE = 41;
    private static final int VERTICAL_STYLE = 42;
    private static final int ALPHA = 43;
    private static final int ELEVATION = 44;
    private static final int ROTATION_X = 45;
    private static final int ROTATION_Y = 46;
    private static final int SCALE_X = 47;
    private static final int SCALE_Y = 48;
    private static final int TRANSFORM_PIVOT_X = 49;
    private static final int TRANSFORM_PIVOT_Y = 50;
    private static final int TRANSLATION_X = 51;
    private static final int TRANSLATION_Y = 52;
    private static final int TRANSLATION_Z = 53;
    private static final int WIDTH_DEFAULT = 54;
    private static final int HEIGHT_DEFAULT = 55;
    private static final int WIDTH_MAX = 56;
    private static final int HEIGHT_MAX = 57;
    private static final int WIDTH_MIN = 58;
    private static final int HEIGHT_MIN = 59;
    private static final int ROTATION = 60;
    private static final int CIRCLE = 61;
    private static final int CIRCLE_RADIUS = 62;
    private static final int CIRCLE_ANGLE = 63;
    private static final int ANIMATE_RELATIVE_TO = 64;
    private static final int TRANSITION_EASING = 65;
    private static final int DRAW_PATH = 66;
    private static final int TRANSITION_PATH_ROTATE = 67;
    private static final int PROGRESS = 68;
    private static final int WIDTH_PERCENT = 69;
    private static final int HEIGHT_PERCENT = 70;
    private static final int CHAIN_USE_RTL = 71;
    private static final int BARRIER_DIRECTION = 72;
    private static final int BARRIER_MARGIN = 73;
    private static final int CONSTRAINT_REFERENCED_IDS = 74;
    private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
    private static final int PATH_MOTION_ARC = 76;
    private static final int CONSTRAINT_TAG = 77;
    private static final int VISIBILITY_MODE = 78;
    private static final int MOTION_STAGGER = 79;
    private static final int CONSTRAINED_WIDTH = 80;
    private static final int CONSTRAINED_HEIGHT = 81;
    private static final int UNUSED = 82;
    
    public ConstraintSet() {
        this.mSavedAttributes = new HashMap<String, ConstraintAttribute>();
        this.mForceId = true;
        this.mConstraints = new HashMap<Integer, Constraint>();
    }
    
    public HashMap<String, ConstraintAttribute> getCustomAttributeSet() {
        return this.mSavedAttributes;
    }
    
    public Constraint getParameters(final int mId) {
        return this.get(mId);
    }
    
    public void readFallback(final ConstraintSet set) {
        for (final Integer key : set.mConstraints.keySet()) {
            final int id = key;
            final Constraint from = set.mConstraints.get(key);
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            if (!constraint.layout.mApply) {
                constraint.layout.copyFrom(from.layout);
            }
            if (!constraint.propertySet.mApply) {
                constraint.propertySet.copyFrom(from.propertySet);
            }
            if (!constraint.transform.mApply) {
                constraint.transform.copyFrom(from.transform);
            }
            if (!constraint.motion.mApply) {
                constraint.motion.copyFrom(from.motion);
            }
        }
    }
    
    public void readFallback(final ConstraintLayout constraintLayout) {
        for (int count = constraintLayout.getChildCount(), i = 0; i < count; ++i) {
            final View view = constraintLayout.getChildAt(i);
            final ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            final int id = view.getId();
            if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            if (!constraint.layout.mApply) {
                constraint.fillFrom(id, param);
                if (view instanceof ConstraintHelper) {
                    constraint.layout.mReferenceIds = ((ConstraintHelper)view).getReferencedIds();
                    if (view instanceof Barrier) {
                        final Barrier barrier = (Barrier)view;
                        constraint.layout.mBarrierAllowsGoneWidgets = barrier.allowsGoneWidget();
                        constraint.layout.mBarrierDirection = barrier.getType();
                        constraint.layout.mBarrierMargin = barrier.getMargin();
                    }
                }
                constraint.layout.mApply = true;
            }
            if (!constraint.propertySet.mApply) {
                constraint.propertySet.visibility = view.getVisibility();
                constraint.propertySet.alpha = view.getAlpha();
                constraint.propertySet.mApply = true;
            }
            if (Build.VERSION.SDK_INT >= 17 && !constraint.transform.mApply) {
                constraint.transform.mApply = true;
                constraint.transform.rotation = view.getRotation();
                constraint.transform.rotationX = view.getRotationX();
                constraint.transform.rotationY = view.getRotationY();
                constraint.transform.scaleX = view.getScaleX();
                constraint.transform.scaleY = view.getScaleY();
                final float pivotX = view.getPivotX();
                final float pivotY = view.getPivotY();
                if (pivotX != 0.0 || pivotY != 0.0) {
                    constraint.transform.transformPivotX = pivotX;
                    constraint.transform.transformPivotY = pivotY;
                }
                constraint.transform.translationX = view.getTranslationX();
                constraint.transform.translationY = view.getTranslationY();
                if (Build.VERSION.SDK_INT >= 21) {
                    constraint.transform.translationZ = view.getTranslationZ();
                    if (constraint.transform.applyElevation) {
                        constraint.transform.elevation = view.getElevation();
                    }
                }
            }
        }
    }
    
    public void clone(final Context context, final int constraintLayoutId) {
        this.clone((ConstraintLayout)LayoutInflater.from(context).inflate(constraintLayoutId, (ViewGroup)null));
    }
    
    public void clone(final ConstraintSet set) {
        this.mConstraints.clear();
        for (final Integer key : set.mConstraints.keySet()) {
            this.mConstraints.put(key, set.mConstraints.get(key).clone());
        }
    }
    
    public void clone(final ConstraintLayout constraintLayout) {
        final int count = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; ++i) {
            final View view = constraintLayout.getChildAt(i);
            final ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            final int id = view.getId();
            if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            constraint.mCustomConstraints = ConstraintAttribute.extractAttributes(this.mSavedAttributes, view);
            constraint.fillFrom(id, param);
            constraint.propertySet.visibility = view.getVisibility();
            if (Build.VERSION.SDK_INT >= 17) {
                constraint.propertySet.alpha = view.getAlpha();
                constraint.transform.rotation = view.getRotation();
                constraint.transform.rotationX = view.getRotationX();
                constraint.transform.rotationY = view.getRotationY();
                constraint.transform.scaleX = view.getScaleX();
                constraint.transform.scaleY = view.getScaleY();
                final float pivotX = view.getPivotX();
                final float pivotY = view.getPivotY();
                if (pivotX != 0.0 || pivotY != 0.0) {
                    constraint.transform.transformPivotX = pivotX;
                    constraint.transform.transformPivotY = pivotY;
                }
                constraint.transform.translationX = view.getTranslationX();
                constraint.transform.translationY = view.getTranslationY();
                if (Build.VERSION.SDK_INT >= 21) {
                    constraint.transform.translationZ = view.getTranslationZ();
                    if (constraint.transform.applyElevation) {
                        constraint.transform.elevation = view.getElevation();
                    }
                }
            }
            if (view instanceof Barrier) {
                final Barrier barrier = (Barrier)view;
                constraint.layout.mBarrierAllowsGoneWidgets = barrier.allowsGoneWidget();
                constraint.layout.mReferenceIds = barrier.getReferencedIds();
                constraint.layout.mBarrierDirection = barrier.getType();
                constraint.layout.mBarrierMargin = barrier.getMargin();
            }
        }
    }
    
    public void clone(final Constraints constraints) {
        final int count = constraints.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; ++i) {
            final View view = constraints.getChildAt(i);
            final Constraints.LayoutParams param = (Constraints.LayoutParams)view.getLayoutParams();
            final int id = view.getId();
            if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            if (view instanceof ConstraintHelper) {
                final ConstraintHelper helper = (ConstraintHelper)view;
                constraint.fillFromConstraints(helper, id, param);
            }
            constraint.fillFromConstraints(id, param);
        }
    }
    
    public void applyTo(final ConstraintLayout constraintLayout) {
        this.applyToInternal(constraintLayout, true);
        constraintLayout.setConstraintSet(null);
    }
    
    public void applyToWithoutCustom(final ConstraintLayout constraintLayout) {
        this.applyToInternal(constraintLayout, false);
        constraintLayout.setConstraintSet(null);
    }
    
    public void applyCustomAttributes(final ConstraintLayout constraintLayout) {
        for (int count = constraintLayout.getChildCount(), i = 0; i < count; ++i) {
            final View view = constraintLayout.getChildAt(i);
            final int id = view.getId();
            if (!this.mConstraints.containsKey(id)) {
                Log.v("ConstraintSet", "id unknown " + Debug.getName(view));
            }
            else {
                if (this.mForceId && id == -1) {
                    throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
                }
                if (this.mConstraints.containsKey(id)) {
                    final Constraint constraint = this.mConstraints.get(id);
                    ConstraintAttribute.setAttributes(view, constraint.mCustomConstraints);
                }
            }
        }
    }
    
    public void applyToHelper(final ConstraintHelper helper, final ConstraintWidget child, final ConstraintLayout.LayoutParams layoutParams, final SparseArray<ConstraintWidget> mapIdToWidget) {
        final int id = helper.getId();
        if (this.mConstraints.containsKey(id)) {
            final Constraint constraint = this.mConstraints.get(id);
            if (child instanceof HelperWidget) {
                final HelperWidget helperWidget = (HelperWidget)child;
                helper.loadParameters(constraint, helperWidget, layoutParams, mapIdToWidget);
            }
        }
    }
    
    public void applyToLayoutParams(final int id, final ConstraintLayout.LayoutParams layoutParams) {
        if (this.mConstraints.containsKey(id)) {
            final Constraint constraint = this.mConstraints.get(id);
            constraint.applyTo(layoutParams);
        }
    }
    
    void applyToInternal(final ConstraintLayout constraintLayout, final boolean applyPostLayout) {
        final int count = constraintLayout.getChildCount();
        final HashSet<Integer> used = new HashSet<Integer>(this.mConstraints.keySet());
        for (int i = 0; i < count; ++i) {
            final View view = constraintLayout.getChildAt(i);
            final int id = view.getId();
            if (!this.mConstraints.containsKey(id)) {
                Log.w("ConstraintSet", "id unknown " + Debug.getName(view));
            }
            else {
                if (this.mForceId && id == -1) {
                    throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
                }
                if (id != -1) {
                    if (this.mConstraints.containsKey(id)) {
                        used.remove(id);
                        final Constraint constraint = this.mConstraints.get(id);
                        if (view instanceof Barrier) {
                            constraint.layout.mHelperType = 1;
                        }
                        if (constraint.layout.mHelperType != -1) {
                            switch (constraint.layout.mHelperType) {
                                case 1: {
                                    final Barrier barrier = (Barrier)view;
                                    barrier.setId(id);
                                    barrier.setType(constraint.layout.mBarrierDirection);
                                    barrier.setMargin(constraint.layout.mBarrierMargin);
                                    barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
                                    if (constraint.layout.mReferenceIds != null) {
                                        barrier.setReferencedIds(constraint.layout.mReferenceIds);
                                        break;
                                    }
                                    if (constraint.layout.mReferenceIdString != null) {
                                        barrier.setReferencedIds(constraint.layout.mReferenceIds = this.convertReferenceString(barrier, constraint.layout.mReferenceIdString));
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                        final ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams)view.getLayoutParams();
                        param.validate();
                        constraint.applyTo(param);
                        if (applyPostLayout) {
                            ConstraintAttribute.setAttributes(view, constraint.mCustomConstraints);
                        }
                        view.setLayoutParams((ViewGroup.LayoutParams)param);
                        if (constraint.propertySet.mVisibilityMode == 0) {
                            view.setVisibility(constraint.propertySet.visibility);
                        }
                        if (Build.VERSION.SDK_INT >= 17) {
                            view.setAlpha(constraint.propertySet.alpha);
                            view.setRotation(constraint.transform.rotation);
                            view.setRotationX(constraint.transform.rotationX);
                            view.setRotationY(constraint.transform.rotationY);
                            view.setScaleX(constraint.transform.scaleX);
                            view.setScaleY(constraint.transform.scaleY);
                            if (!Float.isNaN(constraint.transform.transformPivotX)) {
                                view.setPivotX(constraint.transform.transformPivotX);
                            }
                            if (!Float.isNaN(constraint.transform.transformPivotY)) {
                                view.setPivotY(constraint.transform.transformPivotY);
                            }
                            view.setTranslationX(constraint.transform.translationX);
                            view.setTranslationY(constraint.transform.translationY);
                            if (Build.VERSION.SDK_INT >= 21) {
                                view.setTranslationZ(constraint.transform.translationZ);
                                if (constraint.transform.applyElevation) {
                                    view.setElevation(constraint.transform.elevation);
                                }
                            }
                        }
                    }
                    else {
                        Log.v("ConstraintSet", "WARNING NO CONSTRAINTS for view " + id);
                    }
                }
            }
        }
        for (final Integer id2 : used) {
            final Constraint constraint2 = this.mConstraints.get(id2);
            if (constraint2.layout.mHelperType != -1) {
                switch (constraint2.layout.mHelperType) {
                    case 1: {
                        final Barrier barrier2 = new Barrier(constraintLayout.getContext());
                        barrier2.setId((int)id2);
                        if (constraint2.layout.mReferenceIds != null) {
                            barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                        }
                        else if (constraint2.layout.mReferenceIdString != null) {
                            barrier2.setReferencedIds(constraint2.layout.mReferenceIds = this.convertReferenceString(barrier2, constraint2.layout.mReferenceIdString));
                        }
                        barrier2.setType(constraint2.layout.mBarrierDirection);
                        barrier2.setMargin(constraint2.layout.mBarrierMargin);
                        final ConstraintLayout.LayoutParams param = constraintLayout.generateDefaultLayoutParams();
                        barrier2.validateParams();
                        constraint2.applyTo(param);
                        constraintLayout.addView((View)barrier2, (ViewGroup.LayoutParams)param);
                        break;
                    }
                }
            }
            if (constraint2.layout.mIsGuideline) {
                final Guideline g = new Guideline(constraintLayout.getContext());
                g.setId((int)id2);
                final ConstraintLayout.LayoutParams param = constraintLayout.generateDefaultLayoutParams();
                constraint2.applyTo(param);
                constraintLayout.addView((View)g, (ViewGroup.LayoutParams)param);
            }
        }
    }
    
    public void center(final int centerID, final int firstID, final int firstSide, final int firstMargin, final int secondId, final int secondSide, final int secondMargin, final float bias) {
        if (firstMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (secondMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (bias <= 0.0f || bias > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        }
        if (firstSide == 1 || firstSide == 2) {
            this.connect(centerID, 1, firstID, firstSide, firstMargin);
            this.connect(centerID, 2, secondId, secondSide, secondMargin);
            final Constraint constraint = this.mConstraints.get(centerID);
            constraint.layout.horizontalBias = bias;
        }
        else if (firstSide == 6 || firstSide == 7) {
            this.connect(centerID, 6, firstID, firstSide, firstMargin);
            this.connect(centerID, 7, secondId, secondSide, secondMargin);
            final Constraint constraint = this.mConstraints.get(centerID);
            constraint.layout.horizontalBias = bias;
        }
        else {
            this.connect(centerID, 3, firstID, firstSide, firstMargin);
            this.connect(centerID, 4, secondId, secondSide, secondMargin);
            final Constraint constraint = this.mConstraints.get(centerID);
            constraint.layout.verticalBias = bias;
        }
    }
    
    public void centerHorizontally(final int centerID, final int leftId, final int leftSide, final int leftMargin, final int rightId, final int rightSide, final int rightMargin, final float bias) {
        this.connect(centerID, 1, leftId, leftSide, leftMargin);
        this.connect(centerID, 2, rightId, rightSide, rightMargin);
        final Constraint constraint = this.mConstraints.get(centerID);
        constraint.layout.horizontalBias = bias;
    }
    
    public void centerHorizontallyRtl(final int centerID, final int startId, final int startSide, final int startMargin, final int endId, final int endSide, final int endMargin, final float bias) {
        this.connect(centerID, 6, startId, startSide, startMargin);
        this.connect(centerID, 7, endId, endSide, endMargin);
        final Constraint constraint = this.mConstraints.get(centerID);
        constraint.layout.horizontalBias = bias;
    }
    
    public void centerVertically(final int centerID, final int topId, final int topSide, final int topMargin, final int bottomId, final int bottomSide, final int bottomMargin, final float bias) {
        this.connect(centerID, 3, topId, topSide, topMargin);
        this.connect(centerID, 4, bottomId, bottomSide, bottomMargin);
        final Constraint constraint = this.mConstraints.get(centerID);
        constraint.layout.verticalBias = bias;
    }
    
    public void createVerticalChain(final int topId, final int topSide, final int bottomId, final int bottomSide, final int[] chainIds, final float[] weights, final int style) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (weights != null && weights.length != chainIds.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (weights != null) {
            this.get(chainIds[0]).layout.verticalWeight = weights[0];
        }
        this.get(chainIds[0]).layout.verticalChainStyle = style;
        this.connect(chainIds[0], 3, topId, topSide, 0);
        for (int i = 1; i < chainIds.length; ++i) {
            final int chainId = chainIds[i];
            this.connect(chainIds[i], 3, chainIds[i - 1], 4, 0);
            this.connect(chainIds[i - 1], 4, chainIds[i], 3, 0);
            if (weights != null) {
                this.get(chainIds[i]).layout.verticalWeight = weights[i];
            }
        }
        this.connect(chainIds[chainIds.length - 1], 4, bottomId, bottomSide, 0);
    }
    
    public void createHorizontalChain(final int leftId, final int leftSide, final int rightId, final int rightSide, final int[] chainIds, final float[] weights, final int style) {
        this.createHorizontalChain(leftId, leftSide, rightId, rightSide, chainIds, weights, style, 1, 2);
    }
    
    public void createHorizontalChainRtl(final int startId, final int startSide, final int endId, final int endSide, final int[] chainIds, final float[] weights, final int style) {
        this.createHorizontalChain(startId, startSide, endId, endSide, chainIds, weights, style, 6, 7);
    }
    
    private void createHorizontalChain(final int leftId, final int leftSide, final int rightId, final int rightSide, final int[] chainIds, final float[] weights, final int style, final int left, final int right) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (weights != null && weights.length != chainIds.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (weights != null) {
            this.get(chainIds[0]).layout.horizontalWeight = weights[0];
        }
        this.get(chainIds[0]).layout.horizontalChainStyle = style;
        this.connect(chainIds[0], left, leftId, leftSide, -1);
        for (int i = 1; i < chainIds.length; ++i) {
            final int chainId = chainIds[i];
            this.connect(chainIds[i], left, chainIds[i - 1], right, -1);
            this.connect(chainIds[i - 1], right, chainIds[i], left, -1);
            if (weights != null) {
                this.get(chainIds[i]).layout.horizontalWeight = weights[i];
            }
        }
        this.connect(chainIds[chainIds.length - 1], right, rightId, rightSide, -1);
    }
    
    public void connect(final int startID, final int startSide, final int endID, final int endSide, final int margin) {
        if (!this.mConstraints.containsKey(startID)) {
            this.mConstraints.put(startID, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(startID);
        switch (startSide) {
            case 1: {
                if (endSide == 1) {
                    constraint.layout.leftToLeft = endID;
                    constraint.layout.leftToRight = -1;
                }
                else {
                    if (endSide != 2) {
                        throw new IllegalArgumentException("Left to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.leftToRight = endID;
                    constraint.layout.leftToLeft = -1;
                }
                constraint.layout.leftMargin = margin;
                break;
            }
            case 2: {
                if (endSide == 1) {
                    constraint.layout.rightToLeft = endID;
                    constraint.layout.rightToRight = -1;
                }
                else {
                    if (endSide != 2) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.rightToRight = endID;
                    constraint.layout.rightToLeft = -1;
                }
                constraint.layout.rightMargin = margin;
                break;
            }
            case 3: {
                if (endSide == 3) {
                    constraint.layout.topToTop = endID;
                    constraint.layout.topToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                }
                else {
                    if (endSide != 4) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.topToBottom = endID;
                    constraint.layout.topToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                }
                constraint.layout.topMargin = margin;
                break;
            }
            case 4: {
                if (endSide == 4) {
                    constraint.layout.bottomToBottom = endID;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                }
                else {
                    if (endSide != 3) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.bottomToTop = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                }
                constraint.layout.bottomMargin = margin;
                break;
            }
            case 5: {
                if (endSide == 5) {
                    constraint.layout.baselineToBaseline = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topToBottom = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 6: {
                if (endSide == 6) {
                    constraint.layout.startToStart = endID;
                    constraint.layout.startToEnd = -1;
                }
                else {
                    if (endSide != 7) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.startToEnd = endID;
                    constraint.layout.startToStart = -1;
                }
                constraint.layout.startMargin = margin;
                break;
            }
            case 7: {
                if (endSide == 7) {
                    constraint.layout.endToEnd = endID;
                    constraint.layout.endToStart = -1;
                }
                else {
                    if (endSide != 6) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    constraint.layout.endToStart = endID;
                    constraint.layout.endToEnd = -1;
                }
                constraint.layout.endMargin = margin;
                break;
            }
            default: {
                throw new IllegalArgumentException(this.sideToString(startSide) + " to " + this.sideToString(endSide) + " unknown");
            }
        }
    }
    
    public void connect(final int startID, final int startSide, final int endID, final int endSide) {
        if (!this.mConstraints.containsKey(startID)) {
            this.mConstraints.put(startID, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(startID);
        switch (startSide) {
            case 1: {
                if (endSide == 1) {
                    constraint.layout.leftToLeft = endID;
                    constraint.layout.leftToRight = -1;
                    break;
                }
                if (endSide == 2) {
                    constraint.layout.leftToRight = endID;
                    constraint.layout.leftToLeft = -1;
                    break;
                }
                throw new IllegalArgumentException("left to " + this.sideToString(endSide) + " undefined");
            }
            case 2: {
                if (endSide == 1) {
                    constraint.layout.rightToLeft = endID;
                    constraint.layout.rightToRight = -1;
                    break;
                }
                if (endSide == 2) {
                    constraint.layout.rightToRight = endID;
                    constraint.layout.rightToLeft = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 3: {
                if (endSide == 3) {
                    constraint.layout.topToTop = endID;
                    constraint.layout.topToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                    break;
                }
                if (endSide == 4) {
                    constraint.layout.topToBottom = endID;
                    constraint.layout.topToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 4: {
                if (endSide == 4) {
                    constraint.layout.bottomToBottom = endID;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.baselineToBaseline = -1;
                    break;
                }
                if (endSide == 3) {
                    constraint.layout.bottomToTop = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.baselineToBaseline = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 5: {
                if (endSide == 5) {
                    constraint.layout.baselineToBaseline = endID;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topToBottom = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 6: {
                if (endSide == 6) {
                    constraint.layout.startToStart = endID;
                    constraint.layout.startToEnd = -1;
                    break;
                }
                if (endSide == 7) {
                    constraint.layout.startToEnd = endID;
                    constraint.layout.startToStart = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 7: {
                if (endSide == 7) {
                    constraint.layout.endToEnd = endID;
                    constraint.layout.endToStart = -1;
                    break;
                }
                if (endSide == 6) {
                    constraint.layout.endToStart = endID;
                    constraint.layout.endToEnd = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            default: {
                throw new IllegalArgumentException(this.sideToString(startSide) + " to " + this.sideToString(endSide) + " unknown");
            }
        }
    }
    
    public void centerHorizontally(final int viewId, final int toView) {
        if (toView == 0) {
            this.center(viewId, 0, 1, 0, 0, 2, 0, 0.5f);
        }
        else {
            this.center(viewId, toView, 2, 0, toView, 1, 0, 0.5f);
        }
    }
    
    public void centerHorizontallyRtl(final int viewId, final int toView) {
        if (toView == 0) {
            this.center(viewId, 0, 6, 0, 0, 7, 0, 0.5f);
        }
        else {
            this.center(viewId, toView, 7, 0, toView, 6, 0, 0.5f);
        }
    }
    
    public void centerVertically(final int viewId, final int toView) {
        if (toView == 0) {
            this.center(viewId, 0, 3, 0, 0, 4, 0, 0.5f);
        }
        else {
            this.center(viewId, toView, 4, 0, toView, 3, 0, 0.5f);
        }
    }
    
    public void clear(final int viewId) {
        this.mConstraints.remove(viewId);
    }
    
    public void clear(final int viewId, final int anchor) {
        if (this.mConstraints.containsKey(viewId)) {
            final Constraint constraint = this.mConstraints.get(viewId);
            switch (anchor) {
                case 1: {
                    constraint.layout.leftToRight = -1;
                    constraint.layout.leftToLeft = -1;
                    constraint.layout.leftMargin = -1;
                    constraint.layout.goneLeftMargin = -1;
                    break;
                }
                case 2: {
                    constraint.layout.rightToRight = -1;
                    constraint.layout.rightToLeft = -1;
                    constraint.layout.rightMargin = -1;
                    constraint.layout.goneRightMargin = -1;
                    break;
                }
                case 3: {
                    constraint.layout.topToBottom = -1;
                    constraint.layout.topToTop = -1;
                    constraint.layout.topMargin = -1;
                    constraint.layout.goneTopMargin = -1;
                    break;
                }
                case 4: {
                    constraint.layout.bottomToTop = -1;
                    constraint.layout.bottomToBottom = -1;
                    constraint.layout.bottomMargin = -1;
                    constraint.layout.goneBottomMargin = -1;
                    break;
                }
                case 5: {
                    constraint.layout.baselineToBaseline = -1;
                    break;
                }
                case 6: {
                    constraint.layout.startToEnd = -1;
                    constraint.layout.startToStart = -1;
                    constraint.layout.startMargin = -1;
                    constraint.layout.goneStartMargin = -1;
                    break;
                }
                case 7: {
                    constraint.layout.endToStart = -1;
                    constraint.layout.endToEnd = -1;
                    constraint.layout.endMargin = -1;
                    constraint.layout.goneEndMargin = -1;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("unknown constraint");
                }
            }
        }
    }
    
    public void setMargin(final int viewId, final int anchor, final int value) {
        final Constraint constraint = this.get(viewId);
        switch (anchor) {
            case 1: {
                constraint.layout.leftMargin = value;
                break;
            }
            case 2: {
                constraint.layout.rightMargin = value;
                break;
            }
            case 3: {
                constraint.layout.topMargin = value;
                break;
            }
            case 4: {
                constraint.layout.bottomMargin = value;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                constraint.layout.startMargin = value;
                break;
            }
            case 7: {
                constraint.layout.endMargin = value;
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
        }
    }
    
    public void setGoneMargin(final int viewId, final int anchor, final int value) {
        final Constraint constraint = this.get(viewId);
        switch (anchor) {
            case 1: {
                constraint.layout.goneLeftMargin = value;
                break;
            }
            case 2: {
                constraint.layout.goneRightMargin = value;
                break;
            }
            case 3: {
                constraint.layout.goneTopMargin = value;
                break;
            }
            case 4: {
                constraint.layout.goneBottomMargin = value;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                constraint.layout.goneStartMargin = value;
                break;
            }
            case 7: {
                constraint.layout.goneEndMargin = value;
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
        }
    }
    
    public void setHorizontalBias(final int viewId, final float bias) {
        this.get(viewId).layout.horizontalBias = bias;
    }
    
    public void setVerticalBias(final int viewId, final float bias) {
        this.get(viewId).layout.verticalBias = bias;
    }
    
    public void setDimensionRatio(final int viewId, final String ratio) {
        this.get(viewId).layout.dimensionRatio = ratio;
    }
    
    public void setVisibility(final int viewId, final int visibility) {
        this.get(viewId).propertySet.visibility = visibility;
    }
    
    public void setVisibilityMode(final int viewId, final int visibilityMode) {
        this.get(viewId).propertySet.mVisibilityMode = visibilityMode;
    }
    
    public int getVisibilityMode(final int viewId) {
        return this.get(viewId).propertySet.mVisibilityMode;
    }
    
    public int getVisibility(final int viewId) {
        return this.get(viewId).propertySet.visibility;
    }
    
    public int getHeight(final int viewId) {
        return this.get(viewId).layout.mHeight;
    }
    
    public int getWidth(final int viewId) {
        return this.get(viewId).layout.mWidth;
    }
    
    public void setAlpha(final int viewId, final float alpha) {
        this.get(viewId).propertySet.alpha = alpha;
    }
    
    public boolean getApplyElevation(final int viewId) {
        return this.get(viewId).transform.applyElevation;
    }
    
    public void setApplyElevation(final int viewId, final boolean apply) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.get(viewId).transform.applyElevation = apply;
        }
    }
    
    public void setElevation(final int viewId, final float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.get(viewId).transform.elevation = elevation;
            this.get(viewId).transform.applyElevation = true;
        }
    }
    
    public void setRotation(final int viewId, final float rotation) {
        this.get(viewId).transform.rotation = rotation;
    }
    
    public void setRotationX(final int viewId, final float rotationX) {
        this.get(viewId).transform.rotationX = rotationX;
    }
    
    public void setRotationY(final int viewId, final float rotationY) {
        this.get(viewId).transform.rotationY = rotationY;
    }
    
    public void setScaleX(final int viewId, final float scaleX) {
        this.get(viewId).transform.scaleX = scaleX;
    }
    
    public void setScaleY(final int viewId, final float scaleY) {
        this.get(viewId).transform.scaleY = scaleY;
    }
    
    public void setTransformPivotX(final int viewId, final float transformPivotX) {
        this.get(viewId).transform.transformPivotX = transformPivotX;
    }
    
    public void setTransformPivotY(final int viewId, final float transformPivotY) {
        this.get(viewId).transform.transformPivotY = transformPivotY;
    }
    
    public void setTransformPivot(final int viewId, final float transformPivotX, final float transformPivotY) {
        final Constraint constraint = this.get(viewId);
        constraint.transform.transformPivotY = transformPivotY;
        constraint.transform.transformPivotX = transformPivotX;
    }
    
    public void setTranslationX(final int viewId, final float translationX) {
        this.get(viewId).transform.translationX = translationX;
    }
    
    public void setTranslationY(final int viewId, final float translationY) {
        this.get(viewId).transform.translationY = translationY;
    }
    
    public void setTranslation(final int viewId, final float translationX, final float translationY) {
        final Constraint constraint = this.get(viewId);
        constraint.transform.translationX = translationX;
        constraint.transform.translationY = translationY;
    }
    
    public void setTranslationZ(final int viewId, final float translationZ) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.get(viewId).transform.translationZ = translationZ;
        }
    }
    
    public void setEditorAbsoluteX(final int viewId, final int position) {
        this.get(viewId).layout.editorAbsoluteX = position;
    }
    
    public void setEditorAbsoluteY(final int viewId, final int position) {
        this.get(viewId).layout.editorAbsoluteY = position;
    }
    
    public void constrainHeight(final int viewId, final int height) {
        this.get(viewId).layout.mHeight = height;
    }
    
    public void constrainWidth(final int viewId, final int width) {
        this.get(viewId).layout.mWidth = width;
    }
    
    public void constrainCircle(final int viewId, final int id, final int radius, final float angle) {
        final Constraint constraint = this.get(viewId);
        constraint.layout.circleConstraint = id;
        constraint.layout.circleRadius = radius;
        constraint.layout.circleAngle = angle;
    }
    
    public void constrainMaxHeight(final int viewId, final int height) {
        this.get(viewId).layout.heightMax = height;
    }
    
    public void constrainMaxWidth(final int viewId, final int width) {
        this.get(viewId).layout.widthMax = width;
    }
    
    public void constrainMinHeight(final int viewId, final int height) {
        this.get(viewId).layout.heightMin = height;
    }
    
    public void constrainMinWidth(final int viewId, final int width) {
        this.get(viewId).layout.widthMin = width;
    }
    
    public void constrainPercentWidth(final int viewId, final float percent) {
        this.get(viewId).layout.widthPercent = percent;
    }
    
    public void constrainPercentHeight(final int viewId, final float percent) {
        this.get(viewId).layout.heightPercent = percent;
    }
    
    public void constrainDefaultHeight(final int viewId, final int height) {
        this.get(viewId).layout.heightDefault = height;
    }
    
    public void constrainedWidth(final int viewId, final boolean constrained) {
        this.get(viewId).layout.constrainedWidth = constrained;
    }
    
    public void constrainedHeight(final int viewId, final boolean constrained) {
        this.get(viewId).layout.constrainedHeight = constrained;
    }
    
    public void constrainDefaultWidth(final int viewId, final int width) {
        this.get(viewId).layout.widthDefault = width;
    }
    
    public void setHorizontalWeight(final int viewId, final float weight) {
        this.get(viewId).layout.horizontalWeight = weight;
    }
    
    public void setVerticalWeight(final int viewId, final float weight) {
        this.get(viewId).layout.verticalWeight = weight;
    }
    
    public void setHorizontalChainStyle(final int viewId, final int chainStyle) {
        this.get(viewId).layout.horizontalChainStyle = chainStyle;
    }
    
    public void setVerticalChainStyle(final int viewId, final int chainStyle) {
        this.get(viewId).layout.verticalChainStyle = chainStyle;
    }
    
    public void addToHorizontalChain(final int viewId, final int leftId, final int rightId) {
        this.connect(viewId, 1, leftId, (leftId == 0) ? 1 : 2, 0);
        this.connect(viewId, 2, rightId, (rightId == 0) ? 2 : 1, 0);
        if (leftId != 0) {
            this.connect(leftId, 2, viewId, 1, 0);
        }
        if (rightId != 0) {
            this.connect(rightId, 1, viewId, 2, 0);
        }
    }
    
    public void addToHorizontalChainRTL(final int viewId, final int leftId, final int rightId) {
        this.connect(viewId, 6, leftId, (leftId == 0) ? 6 : 7, 0);
        this.connect(viewId, 7, rightId, (rightId == 0) ? 7 : 6, 0);
        if (leftId != 0) {
            this.connect(leftId, 7, viewId, 6, 0);
        }
        if (rightId != 0) {
            this.connect(rightId, 6, viewId, 7, 0);
        }
    }
    
    public void addToVerticalChain(final int viewId, final int topId, final int bottomId) {
        this.connect(viewId, 3, topId, (topId == 0) ? 3 : 4, 0);
        this.connect(viewId, 4, bottomId, (bottomId == 0) ? 4 : 3, 0);
        if (topId != 0) {
            this.connect(topId, 4, viewId, 3, 0);
        }
        if (topId != 0) {
            this.connect(bottomId, 3, viewId, 4, 0);
        }
    }
    
    public void removeFromVerticalChain(final int viewId) {
        if (this.mConstraints.containsKey(viewId)) {
            final Constraint constraint = this.mConstraints.get(viewId);
            final int topId = constraint.layout.topToBottom;
            final int bottomId = constraint.layout.bottomToTop;
            if (topId != -1 || bottomId != -1) {
                if (topId != -1 && bottomId != -1) {
                    this.connect(topId, 4, bottomId, 3, 0);
                    this.connect(bottomId, 3, topId, 4, 0);
                }
                else if (topId != -1 || bottomId != -1) {
                    if (constraint.layout.bottomToBottom != -1) {
                        this.connect(topId, 4, constraint.layout.bottomToBottom, 4, 0);
                    }
                    else if (constraint.layout.topToTop != -1) {
                        this.connect(bottomId, 3, constraint.layout.topToTop, 3, 0);
                    }
                }
            }
        }
        this.clear(viewId, 3);
        this.clear(viewId, 4);
    }
    
    public void removeFromHorizontalChain(final int viewId) {
        if (this.mConstraints.containsKey(viewId)) {
            final Constraint constraint = this.mConstraints.get(viewId);
            final int leftId = constraint.layout.leftToRight;
            final int rightId = constraint.layout.rightToLeft;
            if (leftId != -1 || rightId != -1) {
                if (leftId != -1 && rightId != -1) {
                    this.connect(leftId, 2, rightId, 1, 0);
                    this.connect(rightId, 1, leftId, 2, 0);
                }
                else if (leftId != -1 || rightId != -1) {
                    if (constraint.layout.rightToRight != -1) {
                        this.connect(leftId, 2, constraint.layout.rightToRight, 2, 0);
                    }
                    else if (constraint.layout.leftToLeft != -1) {
                        this.connect(rightId, 1, constraint.layout.leftToLeft, 1, 0);
                    }
                }
                this.clear(viewId, 1);
                this.clear(viewId, 2);
            }
            else {
                final int startId = constraint.layout.startToEnd;
                final int endId = constraint.layout.endToStart;
                if (startId != -1 || endId != -1) {
                    if (startId != -1 && endId != -1) {
                        this.connect(startId, 7, endId, 6, 0);
                        this.connect(endId, 6, leftId, 7, 0);
                    }
                    else if (leftId != -1 || endId != -1) {
                        if (constraint.layout.rightToRight != -1) {
                            this.connect(leftId, 7, constraint.layout.rightToRight, 7, 0);
                        }
                        else if (constraint.layout.leftToLeft != -1) {
                            this.connect(endId, 6, constraint.layout.leftToLeft, 6, 0);
                        }
                    }
                }
                this.clear(viewId, 6);
                this.clear(viewId, 7);
            }
        }
    }
    
    public void create(final int guidelineID, final int orientation) {
        final Constraint constraint = this.get(guidelineID);
        constraint.layout.mIsGuideline = true;
        constraint.layout.orientation = orientation;
    }
    
    public void createBarrier(final int id, final int direction, final int margin, final int... referenced) {
        final Constraint constraint = this.get(id);
        constraint.layout.mHelperType = 1;
        constraint.layout.mBarrierDirection = direction;
        constraint.layout.mBarrierMargin = margin;
        constraint.layout.mIsGuideline = false;
        constraint.layout.mReferenceIds = referenced;
    }
    
    public void setGuidelineBegin(final int guidelineID, final int margin) {
        this.get(guidelineID).layout.guideBegin = margin;
        this.get(guidelineID).layout.guideEnd = -1;
        this.get(guidelineID).layout.guidePercent = -1.0f;
    }
    
    public void setGuidelineEnd(final int guidelineID, final int margin) {
        this.get(guidelineID).layout.guideEnd = margin;
        this.get(guidelineID).layout.guideBegin = -1;
        this.get(guidelineID).layout.guidePercent = -1.0f;
    }
    
    public void setGuidelinePercent(final int guidelineID, final float ratio) {
        this.get(guidelineID).layout.guidePercent = ratio;
        this.get(guidelineID).layout.guideEnd = -1;
        this.get(guidelineID).layout.guideBegin = -1;
    }
    
    public void setBarrierType(final int id, final int type) {
        final Constraint constraint = this.get(id);
        constraint.layout.mHelperType = type;
    }
    
    public void removeAttribute(final String attributeName) {
        this.mSavedAttributes.remove(attributeName);
    }
    
    public void setIntValue(final int viewId, final String attributeName, final int value) {
        this.get(viewId).setIntValue(attributeName, value);
    }
    
    public void setColorValue(final int viewId, final String attributeName, final int value) {
        this.get(viewId).setColorValue(attributeName, value);
    }
    
    public void setFloatValue(final int viewId, final String attributeName, final float value) {
        this.get(viewId).setFloatValue(attributeName, value);
    }
    
    public void setStringValue(final int viewId, final String attributeName, final String value) {
        this.get(viewId).setStringValue(attributeName, value);
    }
    
    private void addAttributes(final ConstraintAttribute.AttributeType attributeType, final String... attributeName) {
        ConstraintAttribute constraintAttribute = null;
        for (int i = 0; i < attributeName.length; ++i) {
            if (this.mSavedAttributes.containsKey(attributeName[i])) {
                constraintAttribute = this.mSavedAttributes.get(attributeName[i]);
                if (constraintAttribute.getType() != attributeType) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + constraintAttribute.getType().name());
                }
            }
            else {
                constraintAttribute = new ConstraintAttribute(attributeName[i], attributeType);
                this.mSavedAttributes.put(attributeName[i], constraintAttribute);
            }
        }
    }
    
    public void parseIntAttributes(final Constraint set, final String attributes) {
        final String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; ++i) {
            final String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w("ConstraintSet", " Unable to parse " + sp[i]);
            }
            else {
                set.setFloatValue(attr[0], Integer.decode(attr[1]));
            }
        }
    }
    
    public void parseColorAttributes(final Constraint set, final String attributes) {
        final String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; ++i) {
            final String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w("ConstraintSet", " Unable to parse " + sp[i]);
            }
            else {
                set.setColorValue(attr[0], Color.parseColor(attr[1]));
            }
        }
    }
    
    public void parseFloatAttributes(final Constraint set, final String attributes) {
        final String[] sp = attributes.split(",");
        for (int i = 0; i < sp.length; ++i) {
            final String[] attr = sp[i].split("=");
            if (attr.length != 2) {
                Log.w("ConstraintSet", " Unable to parse " + sp[i]);
            }
            else {
                set.setFloatValue(attr[0], Float.parseFloat(attr[1]));
            }
        }
    }
    
    public void parseStringAttributes(final Constraint set, final String attributes) {
        final String[] sp = splitString(attributes);
        for (int i = 0; i < sp.length; ++i) {
            final String[] attr = sp[i].split("=");
            Log.w("ConstraintSet", " Unable to parse " + sp[i]);
            set.setStringValue(attr[0], attr[1]);
        }
    }
    
    private static String[] splitString(final String str) {
        final char[] chars = str.toCharArray();
        final ArrayList<String> list = new ArrayList<String>();
        boolean indouble = false;
        int start = 0;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == ',' && !indouble) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
            else if (chars[i] == '\"') {
                indouble = !indouble;
            }
        }
        list.add(new String(chars, start, chars.length - start));
        return list.toArray(new String[list.size()]);
    }
    
    public void addIntAttributes(final String... attributeName) {
        this.addAttributes(ConstraintAttribute.AttributeType.INT_TYPE, attributeName);
    }
    
    public void addColorAttributes(final String... attributeName) {
        this.addAttributes(ConstraintAttribute.AttributeType.COLOR_TYPE, attributeName);
    }
    
    public void addFloatAttributes(final String... attributeName) {
        this.addAttributes(ConstraintAttribute.AttributeType.FLOAT_TYPE, attributeName);
    }
    
    public void addStringAttributes(final String... attributeName) {
        this.addAttributes(ConstraintAttribute.AttributeType.STRING_TYPE, attributeName);
    }
    
    private Constraint get(final int id) {
        if (!this.mConstraints.containsKey(id)) {
            this.mConstraints.put(id, new Constraint());
        }
        return this.mConstraints.get(id);
    }
    
    private String sideToString(final int side) {
        switch (side) {
            case 1: {
                return "left";
            }
            case 2: {
                return "right";
            }
            case 3: {
                return "top";
            }
            case 4: {
                return "bottom";
            }
            case 5: {
                return "baseline";
            }
            case 6: {
                return "start";
            }
            case 7: {
                return "end";
            }
            default: {
                return "undefined";
            }
        }
    }
    
    public void load(final Context context, final int resourceId) {
        final Resources res = context.getResources();
        final XmlPullParser parser = (XmlPullParser)res.getXml(resourceId);
        String document = null;
        String tagName = null;
        try {
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0: {
                        document = parser.getName();
                        break;
                    }
                    case 2: {
                        tagName = parser.getName();
                        final Constraint constraint = this.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                        if (tagName.equalsIgnoreCase("Guideline")) {
                            constraint.layout.mIsGuideline = true;
                        }
                        this.mConstraints.put(constraint.mViewId, constraint);
                        break;
                    }
                    case 3: {
                        tagName = null;
                        break;
                    }
                }
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public void load(final Context context, final XmlPullParser parser) {
        String tagName = null;
        try {
            Constraint constraint = null;
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0: {
                        final String document = parser.getName();
                        break;
                    }
                    case 2: {
                        final String name;
                        tagName = (name = parser.getName());
                        switch (name) {
                            case "Constraint": {
                                constraint = this.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            }
                            case "Guideline": {
                                constraint = this.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                constraint.layout.mIsGuideline = true;
                                constraint.layout.mApply = true;
                                break;
                            }
                            case "Barrier": {
                                constraint = this.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                constraint.layout.mHelperType = 1;
                                break;
                            }
                            case "PropertySet": {
                                if (constraint == null) {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + parser.getLineNumber());
                                }
                                constraint.propertySet.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            }
                            case "Transform": {
                                if (constraint == null) {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + parser.getLineNumber());
                                }
                                constraint.transform.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            }
                            case "Layout": {
                                if (constraint == null) {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + parser.getLineNumber());
                                }
                                constraint.layout.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            }
                            case "Motion": {
                                if (constraint == null) {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + parser.getLineNumber());
                                }
                                constraint.motion.fillFromAttributeList(context, Xml.asAttributeSet(parser));
                                break;
                            }
                            case "CustomAttribute": {
                                if (constraint == null) {
                                    throw new RuntimeException("XML parser error must be within a Constraint " + parser.getLineNumber());
                                }
                                ConstraintAttribute.parse(context, parser, constraint.mCustomConstraints);
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        tagName = parser.getName();
                        if ("ConstraintSet".equals(tagName)) {
                            return;
                        }
                        if (tagName.equalsIgnoreCase("Constraint")) {
                            this.mConstraints.put(constraint.mViewId, constraint);
                            constraint = null;
                        }
                        tagName = null;
                        break;
                    }
                }
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private static int lookupID(final TypedArray a, final int index, final int def) {
        int ret = a.getResourceId(index, def);
        if (ret == -1) {
            ret = a.getInt(index, -1);
        }
        return ret;
    }
    
    private Constraint fillFromAttributeList(final Context context, final AttributeSet attrs) {
        final Constraint c = new Constraint();
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Constraint);
        this.populateConstraint(context, c, a);
        a.recycle();
        return c;
    }
    
    private void populateConstraint(final Context ctx, final Constraint c, final TypedArray a) {
        for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
            final int attr = a.getIndex(i);
            if (attr != R.styleable.Constraint_android_id) {
                c.motion.mApply = true;
                c.layout.mApply = true;
                c.propertySet.mApply = true;
                c.transform.mApply = true;
            }
            switch (ConstraintSet.mapToConstant.get(attr)) {
                case 25: {
                    c.layout.leftToLeft = lookupID(a, attr, c.layout.leftToLeft);
                    break;
                }
                case 26: {
                    c.layout.leftToRight = lookupID(a, attr, c.layout.leftToRight);
                    break;
                }
                case 29: {
                    c.layout.rightToLeft = lookupID(a, attr, c.layout.rightToLeft);
                    break;
                }
                case 30: {
                    c.layout.rightToRight = lookupID(a, attr, c.layout.rightToRight);
                    break;
                }
                case 36: {
                    c.layout.topToTop = lookupID(a, attr, c.layout.topToTop);
                    break;
                }
                case 35: {
                    c.layout.topToBottom = lookupID(a, attr, c.layout.topToBottom);
                    break;
                }
                case 4: {
                    c.layout.bottomToTop = lookupID(a, attr, c.layout.bottomToTop);
                    break;
                }
                case 3: {
                    c.layout.bottomToBottom = lookupID(a, attr, c.layout.bottomToBottom);
                    break;
                }
                case 1: {
                    c.layout.baselineToBaseline = lookupID(a, attr, c.layout.baselineToBaseline);
                    break;
                }
                case 6: {
                    c.layout.editorAbsoluteX = a.getDimensionPixelOffset(attr, c.layout.editorAbsoluteX);
                    break;
                }
                case 7: {
                    c.layout.editorAbsoluteY = a.getDimensionPixelOffset(attr, c.layout.editorAbsoluteY);
                    break;
                }
                case 17: {
                    c.layout.guideBegin = a.getDimensionPixelOffset(attr, c.layout.guideBegin);
                    break;
                }
                case 18: {
                    c.layout.guideEnd = a.getDimensionPixelOffset(attr, c.layout.guideEnd);
                    break;
                }
                case 19: {
                    c.layout.guidePercent = a.getFloat(attr, c.layout.guidePercent);
                    break;
                }
                case 27: {
                    c.layout.orientation = a.getInt(attr, c.layout.orientation);
                    break;
                }
                case 32: {
                    c.layout.startToEnd = lookupID(a, attr, c.layout.startToEnd);
                    break;
                }
                case 33: {
                    c.layout.startToStart = lookupID(a, attr, c.layout.startToStart);
                    break;
                }
                case 10: {
                    c.layout.endToStart = lookupID(a, attr, c.layout.endToStart);
                    break;
                }
                case 9: {
                    c.layout.endToEnd = lookupID(a, attr, c.layout.endToEnd);
                    break;
                }
                case 61: {
                    c.layout.circleConstraint = lookupID(a, attr, c.layout.circleConstraint);
                    break;
                }
                case 62: {
                    c.layout.circleRadius = a.getDimensionPixelSize(attr, c.layout.circleRadius);
                    break;
                }
                case 63: {
                    c.layout.circleAngle = a.getFloat(attr, c.layout.circleAngle);
                    break;
                }
                case 13: {
                    c.layout.goneLeftMargin = a.getDimensionPixelSize(attr, c.layout.goneLeftMargin);
                    break;
                }
                case 16: {
                    c.layout.goneTopMargin = a.getDimensionPixelSize(attr, c.layout.goneTopMargin);
                    break;
                }
                case 14: {
                    c.layout.goneRightMargin = a.getDimensionPixelSize(attr, c.layout.goneRightMargin);
                    break;
                }
                case 11: {
                    c.layout.goneBottomMargin = a.getDimensionPixelSize(attr, c.layout.goneBottomMargin);
                    break;
                }
                case 15: {
                    c.layout.goneStartMargin = a.getDimensionPixelSize(attr, c.layout.goneStartMargin);
                    break;
                }
                case 12: {
                    c.layout.goneEndMargin = a.getDimensionPixelSize(attr, c.layout.goneEndMargin);
                    break;
                }
                case 20: {
                    c.layout.horizontalBias = a.getFloat(attr, c.layout.horizontalBias);
                    break;
                }
                case 37: {
                    c.layout.verticalBias = a.getFloat(attr, c.layout.verticalBias);
                    break;
                }
                case 24: {
                    c.layout.leftMargin = a.getDimensionPixelSize(attr, c.layout.leftMargin);
                    break;
                }
                case 28: {
                    c.layout.rightMargin = a.getDimensionPixelSize(attr, c.layout.rightMargin);
                    break;
                }
                case 31: {
                    c.layout.startMargin = a.getDimensionPixelSize(attr, c.layout.startMargin);
                    break;
                }
                case 8: {
                    c.layout.endMargin = a.getDimensionPixelSize(attr, c.layout.endMargin);
                    break;
                }
                case 34: {
                    c.layout.topMargin = a.getDimensionPixelSize(attr, c.layout.topMargin);
                    break;
                }
                case 2: {
                    c.layout.bottomMargin = a.getDimensionPixelSize(attr, c.layout.bottomMargin);
                    break;
                }
                case 23: {
                    c.layout.mWidth = a.getLayoutDimension(attr, c.layout.mWidth);
                    break;
                }
                case 21: {
                    c.layout.mHeight = a.getLayoutDimension(attr, c.layout.mHeight);
                    break;
                }
                case 57: {
                    c.layout.heightMax = a.getDimensionPixelSize(attr, c.layout.heightMax);
                    break;
                }
                case 56: {
                    c.layout.widthMax = a.getDimensionPixelSize(attr, c.layout.widthMax);
                    break;
                }
                case 59: {
                    c.layout.heightMin = a.getDimensionPixelSize(attr, c.layout.heightMin);
                    break;
                }
                case 58: {
                    c.layout.widthMin = a.getDimensionPixelSize(attr, c.layout.widthMin);
                    break;
                }
                case 80: {
                    c.layout.constrainedWidth = a.getBoolean(attr, c.layout.constrainedWidth);
                    break;
                }
                case 81: {
                    c.layout.constrainedHeight = a.getBoolean(attr, c.layout.constrainedHeight);
                    break;
                }
                case 22: {
                    c.propertySet.visibility = a.getInt(attr, c.propertySet.visibility);
                    c.propertySet.visibility = ConstraintSet.VISIBILITY_FLAGS[c.propertySet.visibility];
                    break;
                }
                case 78: {
                    c.propertySet.mVisibilityMode = a.getInt(attr, c.propertySet.mVisibilityMode);
                    break;
                }
                case 43: {
                    c.propertySet.alpha = a.getFloat(attr, c.propertySet.alpha);
                    break;
                }
                case 44: {
                    if (Build.VERSION.SDK_INT >= 21) {
                        c.transform.applyElevation = true;
                        c.transform.elevation = a.getDimension(attr, c.transform.elevation);
                        break;
                    }
                    break;
                }
                case 60: {
                    c.transform.rotation = a.getFloat(attr, c.transform.rotation);
                    break;
                }
                case 45: {
                    c.transform.rotationX = a.getFloat(attr, c.transform.rotationX);
                    break;
                }
                case 46: {
                    c.transform.rotationY = a.getFloat(attr, c.transform.rotationY);
                    break;
                }
                case 47: {
                    c.transform.scaleX = a.getFloat(attr, c.transform.scaleX);
                    break;
                }
                case 48: {
                    c.transform.scaleY = a.getFloat(attr, c.transform.scaleY);
                    break;
                }
                case 49: {
                    c.transform.transformPivotX = a.getFloat(attr, c.transform.transformPivotX);
                    break;
                }
                case 50: {
                    c.transform.transformPivotY = a.getFloat(attr, c.transform.transformPivotY);
                    break;
                }
                case 51: {
                    c.transform.translationX = a.getDimension(attr, c.transform.translationX);
                    break;
                }
                case 52: {
                    c.transform.translationY = a.getDimension(attr, c.transform.translationY);
                    break;
                }
                case 53: {
                    if (Build.VERSION.SDK_INT >= 21) {
                        c.transform.translationZ = a.getDimension(attr, c.transform.translationZ);
                        break;
                    }
                    break;
                }
                case 40: {
                    c.layout.verticalWeight = a.getFloat(attr, c.layout.verticalWeight);
                    break;
                }
                case 39: {
                    c.layout.horizontalWeight = a.getFloat(attr, c.layout.horizontalWeight);
                    break;
                }
                case 42: {
                    c.layout.verticalChainStyle = a.getInt(attr, c.layout.verticalChainStyle);
                    break;
                }
                case 41: {
                    c.layout.horizontalChainStyle = a.getInt(attr, c.layout.horizontalChainStyle);
                    break;
                }
                case 38: {
                    c.mViewId = a.getResourceId(attr, c.mViewId);
                    break;
                }
                case 5: {
                    c.layout.dimensionRatio = a.getString(attr);
                    break;
                }
                case 69: {
                    c.layout.widthPercent = a.getFloat(attr, 1.0f);
                    break;
                }
                case 70: {
                    c.layout.heightPercent = a.getFloat(attr, 1.0f);
                    break;
                }
                case 68: {
                    c.propertySet.mProgress = a.getFloat(attr, c.propertySet.mProgress);
                    break;
                }
                case 64: {
                    c.motion.mAnimateRelativeTo = lookupID(a, attr, c.motion.mAnimateRelativeTo);
                    break;
                }
                case 65: {
                    final TypedValue type = a.peekValue(attr);
                    if (type.type == 3) {
                        c.motion.mTransitionEasing = a.getString(attr);
                        break;
                    }
                    c.motion.mTransitionEasing = Easing.NAMED_EASING[a.getInteger(attr, 0)];
                    break;
                }
                case 76: {
                    c.motion.mPathMotionArc = a.getInt(attr, c.motion.mPathMotionArc);
                    break;
                }
                case 67: {
                    c.motion.mPathRotate = a.getFloat(attr, c.motion.mPathRotate);
                    break;
                }
                case 79: {
                    c.motion.mMotionStagger = a.getFloat(attr, c.motion.mMotionStagger);
                    break;
                }
                case 66: {
                    c.motion.mDrawPath = a.getInt(attr, 0);
                    break;
                }
                case 71: {
                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                    break;
                }
                case 72: {
                    c.layout.mBarrierDirection = a.getInt(attr, c.layout.mBarrierDirection);
                    break;
                }
                case 73: {
                    c.layout.mBarrierMargin = a.getDimensionPixelSize(attr, c.layout.mBarrierMargin);
                    break;
                }
                case 74: {
                    c.layout.mReferenceIdString = a.getString(attr);
                    break;
                }
                case 77: {
                    c.layout.mConstraintTag = a.getString(attr);
                    break;
                }
                case 75: {
                    c.layout.mBarrierAllowsGoneWidgets = a.getBoolean(attr, c.layout.mBarrierAllowsGoneWidgets);
                    break;
                }
                case 82: {
                    Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(attr) + "   " + ConstraintSet.mapToConstant.get(attr));
                    break;
                }
                default: {
                    Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + ConstraintSet.mapToConstant.get(attr));
                    break;
                }
            }
        }
    }
    
    private int[] convertReferenceString(final View view, final String referenceIdString) {
        final String[] split = referenceIdString.split(",");
        final Context context = view.getContext();
        int[] tags = new int[split.length];
        int count = 0;
        for (int i = 0; i < split.length; ++i) {
            String idString = split[i];
            idString = idString.trim();
            int tag = 0;
            try {
                final Class res = R.id.class;
                final Field field = res.getField(idString);
                tag = field.getInt(null);
            }
            catch (Exception ex) {}
            if (tag == 0) {
                tag = context.getResources().getIdentifier(idString, "id", context.getPackageName());
            }
            if (tag == 0 && view.isInEditMode() && view.getParent() instanceof ConstraintLayout) {
                final ConstraintLayout constraintLayout = (ConstraintLayout)view.getParent();
                final Object value = constraintLayout.getDesignInformation(0, idString);
                if (value != null && value instanceof Integer) {
                    tag = (int)value;
                }
            }
            tags[count++] = tag;
        }
        if (count != split.length) {
            tags = Arrays.copyOf(tags, count);
        }
        return tags;
    }
    
    public Constraint getConstraint(final int id) {
        if (this.mConstraints.containsKey(id)) {
            return this.mConstraints.get(id);
        }
        return null;
    }
    
    public int[] getKnownIds() {
        final Integer[] arr = this.mConstraints.keySet().toArray(new Integer[0]);
        final int[] array = new int[arr.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = arr[i];
        }
        return array;
    }
    
    public boolean isForceId() {
        return this.mForceId;
    }
    
    public void setForceId(final boolean forceId) {
        this.mForceId = forceId;
    }
    
    public void setValidateOnParse(final boolean validate) {
        this.mValidate = validate;
    }
    
    public void dump(final MotionScene scene, final int... ids) {
        final Set<Integer> keys = this.mConstraints.keySet();
        HashSet<Integer> set;
        if (ids.length != 0) {
            set = new HashSet<Integer>();
            for (final int id : ids) {
                set.add(id);
            }
        }
        else {
            set = new HashSet<Integer>(keys);
        }
        System.out.println(set.size() + " constraints");
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Integer id2 : set.toArray(new Integer[0])) {
            final Constraint constraint = this.mConstraints.get(id2);
            stringBuilder.append("<Constraint id=");
            stringBuilder.append(id2);
            stringBuilder.append(" \n");
            constraint.layout.dump(scene, stringBuilder);
            stringBuilder.append("/>\n");
        }
        System.out.println(stringBuilder.toString());
    }
    
    static {
        VISIBILITY_FLAGS = new int[] { 0, 4, 8 };
        (ConstraintSet.mapToConstant = new SparseIntArray()).append(R.styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_toRightOf, 26);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toLeftOf, 29);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintRight_toRightOf, 30);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toTopOf, 36);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintTop_toBottomOf, 35);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toTopOf, 4);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteX, 6);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_editor_absoluteY, 7);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_begin, 17);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_end, 18);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintGuide_percent, 19);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_orientation, 27);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toEndOf, 32);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintStart_toStartOf, 33);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toStartOf, 10);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintEnd_toEndOf, 9);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginLeft, 13);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginTop, 16);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginRight, 14);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginBottom, 11);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginStart, 15);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_goneMarginEnd, 12);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_weight, 40);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_weight, 39);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_chainStyle, 42);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHorizontal_bias, 20);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintVertical_bias, 37);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintDimensionRatio, 5);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintLeft_creator, 82);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintTop_creator, 82);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintRight_creator, 82);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintBottom_creator, 82);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintBaseline_creator, 82);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginLeft, 24);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginRight, 28);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginStart, 31);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginEnd, 8);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginTop, 34);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_marginBottom, 2);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_width, 23);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_layout_height, 21);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_visibility, 22);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_alpha, 43);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_elevation, 44);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_rotationX, 45);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_rotationY, 46);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_rotation, 60);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_scaleX, 47);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_scaleY, 48);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_transformPivotX, 49);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_transformPivotY, 50);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_translationX, 51);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_translationY, 52);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_translationZ, 53);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_default, 54);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_default, 55);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_max, 56);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_max, 57);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_min, 58);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_min, 59);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintCircle, 61);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintCircleRadius, 62);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintCircleAngle, 63);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_animate_relativeTo, 64);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_transitionEasing, 65);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_drawPath, 66);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_transitionPathRotate, 67);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_motionStagger, 79);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_android_id, 38);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_progress, 68);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintWidth_percent, 69);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintHeight_percent, 70);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_chainUseRtl, 71);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_barrierDirection, 72);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_barrierMargin, 73);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_constraint_referenced_ids, 74);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_barrierAllowsGoneWidgets, 75);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_pathMotionArc, 76);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constraintTag, 77);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_visibilityMode, 78);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constrainedWidth, 80);
        ConstraintSet.mapToConstant.append(R.styleable.Constraint_layout_constrainedHeight, 81);
    }
    
    public static class Layout
    {
        public boolean mIsGuideline;
        public boolean mApply;
        public int mWidth;
        public int mHeight;
        public static final int UNSET = -1;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public int leftToLeft;
        public int leftToRight;
        public int rightToLeft;
        public int rightToRight;
        public int topToTop;
        public int topToBottom;
        public int bottomToTop;
        public int bottomToBottom;
        public int baselineToBaseline;
        public int startToEnd;
        public int startToStart;
        public int endToStart;
        public int endToEnd;
        public float horizontalBias;
        public float verticalBias;
        public String dimensionRatio;
        public int circleConstraint;
        public int circleRadius;
        public float circleAngle;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int orientation;
        public int leftMargin;
        public int rightMargin;
        public int topMargin;
        public int bottomMargin;
        public int endMargin;
        public int startMargin;
        public int goneLeftMargin;
        public int goneTopMargin;
        public int goneRightMargin;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneStartMargin;
        public float verticalWeight;
        public float horizontalWeight;
        public int horizontalChainStyle;
        public int verticalChainStyle;
        public int widthDefault;
        public int heightDefault;
        public int widthMax;
        public int heightMax;
        public int widthMin;
        public int heightMin;
        public float widthPercent;
        public float heightPercent;
        public int mBarrierDirection;
        public int mBarrierMargin;
        public int mHelperType;
        public int[] mReferenceIds;
        public String mReferenceIdString;
        public String mConstraintTag;
        public boolean constrainedWidth;
        public boolean constrainedHeight;
        public boolean mBarrierAllowsGoneWidgets;
        private static SparseIntArray mapToConstant;
        private static final int BASELINE_TO_BASELINE = 1;
        private static final int BOTTOM_MARGIN = 2;
        private static final int BOTTOM_TO_BOTTOM = 3;
        private static final int BOTTOM_TO_TOP = 4;
        private static final int DIMENSION_RATIO = 5;
        private static final int EDITOR_ABSOLUTE_X = 6;
        private static final int EDITOR_ABSOLUTE_Y = 7;
        private static final int END_MARGIN = 8;
        private static final int END_TO_END = 9;
        private static final int END_TO_START = 10;
        private static final int GONE_BOTTOM_MARGIN = 11;
        private static final int GONE_END_MARGIN = 12;
        private static final int GONE_LEFT_MARGIN = 13;
        private static final int GONE_RIGHT_MARGIN = 14;
        private static final int GONE_START_MARGIN = 15;
        private static final int GONE_TOP_MARGIN = 16;
        private static final int GUIDE_BEGIN = 17;
        private static final int GUIDE_END = 18;
        private static final int GUIDE_PERCENT = 19;
        private static final int HORIZONTAL_BIAS = 20;
        private static final int LAYOUT_HEIGHT = 21;
        private static final int LAYOUT_WIDTH = 22;
        private static final int LEFT_MARGIN = 23;
        private static final int LEFT_TO_LEFT = 24;
        private static final int LEFT_TO_RIGHT = 25;
        private static final int ORIENTATION = 26;
        private static final int RIGHT_MARGIN = 27;
        private static final int RIGHT_TO_LEFT = 28;
        private static final int RIGHT_TO_RIGHT = 29;
        private static final int START_MARGIN = 30;
        private static final int START_TO_END = 31;
        private static final int START_TO_START = 32;
        private static final int TOP_MARGIN = 33;
        private static final int TOP_TO_BOTTOM = 34;
        private static final int TOP_TO_TOP = 35;
        private static final int VERTICAL_BIAS = 36;
        private static final int HORIZONTAL_WEIGHT = 37;
        private static final int VERTICAL_WEIGHT = 38;
        private static final int HORIZONTAL_STYLE = 39;
        private static final int VERTICAL_STYLE = 40;
        private static final int CIRCLE = 61;
        private static final int CIRCLE_RADIUS = 62;
        private static final int CIRCLE_ANGLE = 63;
        private static final int WIDTH_PERCENT = 69;
        private static final int HEIGHT_PERCENT = 70;
        private static final int CHAIN_USE_RTL = 71;
        private static final int BARRIER_DIRECTION = 72;
        private static final int BARRIER_MARGIN = 73;
        private static final int CONSTRAINT_REFERENCED_IDS = 74;
        private static final int BARRIER_ALLOWS_GONE_WIDGETS = 75;
        private static final int UNUSED = 76;
        
        public Layout() {
            this.mIsGuideline = false;
            this.mApply = false;
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.leftMargin = -1;
            this.rightMargin = -1;
            this.topMargin = -1;
            this.bottomMargin = -1;
            this.endMargin = -1;
            this.startMargin = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneEndMargin = -1;
            this.goneStartMargin = -1;
            this.verticalWeight = -1.0f;
            this.horizontalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.widthDefault = 0;
            this.heightDefault = 0;
            this.widthMax = -1;
            this.heightMax = -1;
            this.widthMin = -1;
            this.heightMin = -1;
            this.widthPercent = 1.0f;
            this.heightPercent = 1.0f;
            this.mBarrierDirection = -1;
            this.mBarrierMargin = 0;
            this.mHelperType = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.mBarrierAllowsGoneWidgets = true;
        }
        
        public void copyFrom(final Layout src) {
            this.mIsGuideline = src.mIsGuideline;
            this.mWidth = src.mWidth;
            this.mApply = src.mApply;
            this.mHeight = src.mHeight;
            this.guideBegin = src.guideBegin;
            this.guideEnd = src.guideEnd;
            this.guidePercent = src.guidePercent;
            this.leftToLeft = src.leftToLeft;
            this.leftToRight = src.leftToRight;
            this.rightToLeft = src.rightToLeft;
            this.rightToRight = src.rightToRight;
            this.topToTop = src.topToTop;
            this.topToBottom = src.topToBottom;
            this.bottomToTop = src.bottomToTop;
            this.bottomToBottom = src.bottomToBottom;
            this.baselineToBaseline = src.baselineToBaseline;
            this.startToEnd = src.startToEnd;
            this.startToStart = src.startToStart;
            this.endToStart = src.endToStart;
            this.endToEnd = src.endToEnd;
            this.horizontalBias = src.horizontalBias;
            this.verticalBias = src.verticalBias;
            this.dimensionRatio = src.dimensionRatio;
            this.circleConstraint = src.circleConstraint;
            this.circleRadius = src.circleRadius;
            this.circleAngle = src.circleAngle;
            this.editorAbsoluteX = src.editorAbsoluteX;
            this.editorAbsoluteY = src.editorAbsoluteY;
            this.orientation = src.orientation;
            this.leftMargin = src.leftMargin;
            this.rightMargin = src.rightMargin;
            this.topMargin = src.topMargin;
            this.bottomMargin = src.bottomMargin;
            this.endMargin = src.endMargin;
            this.startMargin = src.startMargin;
            this.goneLeftMargin = src.goneLeftMargin;
            this.goneTopMargin = src.goneTopMargin;
            this.goneRightMargin = src.goneRightMargin;
            this.goneBottomMargin = src.goneBottomMargin;
            this.goneEndMargin = src.goneEndMargin;
            this.goneStartMargin = src.goneStartMargin;
            this.verticalWeight = src.verticalWeight;
            this.horizontalWeight = src.horizontalWeight;
            this.horizontalChainStyle = src.horizontalChainStyle;
            this.verticalChainStyle = src.verticalChainStyle;
            this.widthDefault = src.widthDefault;
            this.heightDefault = src.heightDefault;
            this.widthMax = src.widthMax;
            this.heightMax = src.heightMax;
            this.widthMin = src.widthMin;
            this.heightMin = src.heightMin;
            this.widthPercent = src.widthPercent;
            this.heightPercent = src.heightPercent;
            this.mBarrierDirection = src.mBarrierDirection;
            this.mBarrierMargin = src.mBarrierMargin;
            this.mHelperType = src.mHelperType;
            this.mConstraintTag = src.mConstraintTag;
            if (src.mReferenceIds != null) {
                this.mReferenceIds = Arrays.copyOf(src.mReferenceIds, src.mReferenceIds.length);
            }
            else {
                this.mReferenceIds = null;
            }
            this.mReferenceIdString = src.mReferenceIdString;
            this.constrainedWidth = src.constrainedWidth;
            this.constrainedHeight = src.constrainedHeight;
            this.mBarrierAllowsGoneWidgets = src.mBarrierAllowsGoneWidgets;
        }
        
        void fillFromAttributeList(final Context context, final AttributeSet attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Layout);
            this.mApply = true;
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Layout.mapToConstant.get(attr)) {
                    case 24: {
                        this.leftToLeft = lookupID(a, attr, this.leftToLeft);
                        break;
                    }
                    case 25: {
                        this.leftToRight = lookupID(a, attr, this.leftToRight);
                        break;
                    }
                    case 28: {
                        this.rightToLeft = lookupID(a, attr, this.rightToLeft);
                        break;
                    }
                    case 29: {
                        this.rightToRight = lookupID(a, attr, this.rightToRight);
                        break;
                    }
                    case 35: {
                        this.topToTop = lookupID(a, attr, this.topToTop);
                        break;
                    }
                    case 34: {
                        this.topToBottom = lookupID(a, attr, this.topToBottom);
                        break;
                    }
                    case 4: {
                        this.bottomToTop = lookupID(a, attr, this.bottomToTop);
                        break;
                    }
                    case 3: {
                        this.bottomToBottom = lookupID(a, attr, this.bottomToBottom);
                        break;
                    }
                    case 1: {
                        this.baselineToBaseline = lookupID(a, attr, this.baselineToBaseline);
                        break;
                    }
                    case 6: {
                        this.editorAbsoluteX = a.getDimensionPixelOffset(attr, this.editorAbsoluteX);
                        break;
                    }
                    case 7: {
                        this.editorAbsoluteY = a.getDimensionPixelOffset(attr, this.editorAbsoluteY);
                        break;
                    }
                    case 17: {
                        this.guideBegin = a.getDimensionPixelOffset(attr, this.guideBegin);
                        break;
                    }
                    case 18: {
                        this.guideEnd = a.getDimensionPixelOffset(attr, this.guideEnd);
                        break;
                    }
                    case 19: {
                        this.guidePercent = a.getFloat(attr, this.guidePercent);
                        break;
                    }
                    case 26: {
                        this.orientation = a.getInt(attr, this.orientation);
                        break;
                    }
                    case 31: {
                        this.startToEnd = lookupID(a, attr, this.startToEnd);
                        break;
                    }
                    case 32: {
                        this.startToStart = lookupID(a, attr, this.startToStart);
                        break;
                    }
                    case 10: {
                        this.endToStart = lookupID(a, attr, this.endToStart);
                        break;
                    }
                    case 9: {
                        this.endToEnd = lookupID(a, attr, this.endToEnd);
                        break;
                    }
                    case 61: {
                        this.circleConstraint = lookupID(a, attr, this.circleConstraint);
                        break;
                    }
                    case 62: {
                        this.circleRadius = a.getDimensionPixelSize(attr, this.circleRadius);
                        break;
                    }
                    case 63: {
                        this.circleAngle = a.getFloat(attr, this.circleAngle);
                        break;
                    }
                    case 13: {
                        this.goneLeftMargin = a.getDimensionPixelSize(attr, this.goneLeftMargin);
                        break;
                    }
                    case 16: {
                        this.goneTopMargin = a.getDimensionPixelSize(attr, this.goneTopMargin);
                        break;
                    }
                    case 14: {
                        this.goneRightMargin = a.getDimensionPixelSize(attr, this.goneRightMargin);
                        break;
                    }
                    case 11: {
                        this.goneBottomMargin = a.getDimensionPixelSize(attr, this.goneBottomMargin);
                        break;
                    }
                    case 15: {
                        this.goneStartMargin = a.getDimensionPixelSize(attr, this.goneStartMargin);
                        break;
                    }
                    case 12: {
                        this.goneEndMargin = a.getDimensionPixelSize(attr, this.goneEndMargin);
                        break;
                    }
                    case 20: {
                        this.horizontalBias = a.getFloat(attr, this.horizontalBias);
                        break;
                    }
                    case 36: {
                        this.verticalBias = a.getFloat(attr, this.verticalBias);
                        break;
                    }
                    case 23: {
                        this.leftMargin = a.getDimensionPixelSize(attr, this.leftMargin);
                        break;
                    }
                    case 27: {
                        this.rightMargin = a.getDimensionPixelSize(attr, this.rightMargin);
                        break;
                    }
                    case 30: {
                        this.startMargin = a.getDimensionPixelSize(attr, this.startMargin);
                        break;
                    }
                    case 8: {
                        this.endMargin = a.getDimensionPixelSize(attr, this.endMargin);
                        break;
                    }
                    case 33: {
                        this.topMargin = a.getDimensionPixelSize(attr, this.topMargin);
                        break;
                    }
                    case 2: {
                        this.bottomMargin = a.getDimensionPixelSize(attr, this.bottomMargin);
                        break;
                    }
                    case 22: {
                        this.mWidth = a.getLayoutDimension(attr, this.mWidth);
                        break;
                    }
                    case 21: {
                        this.mHeight = a.getLayoutDimension(attr, this.mHeight);
                        break;
                    }
                    case 38: {
                        this.verticalWeight = a.getFloat(attr, this.verticalWeight);
                        break;
                    }
                    case 37: {
                        this.horizontalWeight = a.getFloat(attr, this.horizontalWeight);
                        break;
                    }
                    case 40: {
                        this.verticalChainStyle = a.getInt(attr, this.verticalChainStyle);
                        break;
                    }
                    case 39: {
                        this.horizontalChainStyle = a.getInt(attr, this.horizontalChainStyle);
                        break;
                    }
                    case 5: {
                        this.dimensionRatio = a.getString(attr);
                        break;
                    }
                    case 57: {
                        this.heightMax = a.getDimensionPixelSize(attr, this.heightMax);
                        break;
                    }
                    case 56: {
                        this.widthMax = a.getDimensionPixelSize(attr, this.widthMax);
                        break;
                    }
                    case 59: {
                        this.heightMin = a.getDimensionPixelSize(attr, this.heightMin);
                        break;
                    }
                    case 58: {
                        this.widthMin = a.getDimensionPixelSize(attr, this.widthMin);
                        break;
                    }
                    case 69: {
                        this.widthPercent = a.getFloat(attr, 1.0f);
                        break;
                    }
                    case 70: {
                        this.heightPercent = a.getFloat(attr, 1.0f);
                        break;
                    }
                    case 80: {
                        this.constrainedWidth = a.getBoolean(attr, this.constrainedWidth);
                        break;
                    }
                    case 81: {
                        this.constrainedHeight = a.getBoolean(attr, this.constrainedHeight);
                        break;
                    }
                    case 71: {
                        Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                        break;
                    }
                    case 72: {
                        this.mBarrierDirection = a.getInt(attr, this.mBarrierDirection);
                        break;
                    }
                    case 73: {
                        this.mBarrierMargin = a.getDimensionPixelSize(attr, this.mBarrierMargin);
                        break;
                    }
                    case 74: {
                        this.mReferenceIdString = a.getString(attr);
                        break;
                    }
                    case 75: {
                        this.mBarrierAllowsGoneWidgets = a.getBoolean(attr, this.mBarrierAllowsGoneWidgets);
                        break;
                    }
                    case 77: {
                        this.mConstraintTag = a.getString(attr);
                        break;
                    }
                    case 76: {
                        Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(attr) + "   " + Layout.mapToConstant.get(attr));
                        break;
                    }
                    default: {
                        Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + Layout.mapToConstant.get(attr));
                        break;
                    }
                }
            }
            a.recycle();
        }
        
        public void dump(final MotionScene scene, final StringBuilder stringBuilder) {
            final Field[] fields = this.getClass().getDeclaredFields();
            stringBuilder.append("\n");
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                final String name = field.getName();
                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        final Object value = field.get(this);
                        final Class<?> type = field.getType();
                        if (type == Integer.TYPE) {
                            final Integer iValue = (Integer)value;
                            if (iValue != -1) {
                                final String stringid = scene.lookUpConstraintName(iValue);
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append((stringid == null) ? iValue : stringid);
                                stringBuilder.append("\"\n");
                            }
                        }
                        else if (type == Float.TYPE) {
                            final Float fValue = (Float)value;
                            if (fValue != -1.0f) {
                                stringBuilder.append("    ");
                                stringBuilder.append(name);
                                stringBuilder.append(" = \"");
                                stringBuilder.append(fValue);
                                stringBuilder.append("\"\n");
                            }
                        }
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        static {
            (Layout.mapToConstant = new SparseIntArray()).append(R.styleable.Layout_layout_constraintLeft_toLeftOf, 24);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintLeft_toRightOf, 25);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintRight_toLeftOf, 28);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintRight_toRightOf, 29);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintTop_toTopOf, 35);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintTop_toBottomOf, 34);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toTopOf, 4);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintBottom_toBottomOf, 3);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
            Layout.mapToConstant.append(R.styleable.Layout_layout_editor_absoluteX, 6);
            Layout.mapToConstant.append(R.styleable.Layout_layout_editor_absoluteY, 7);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintGuide_begin, 17);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintGuide_end, 18);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintGuide_percent, 19);
            Layout.mapToConstant.append(R.styleable.Layout_android_orientation, 26);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintStart_toEndOf, 31);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintStart_toStartOf, 32);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toStartOf, 10);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintEnd_toEndOf, 9);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginLeft, 13);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginTop, 16);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginRight, 14);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginBottom, 11);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginStart, 15);
            Layout.mapToConstant.append(R.styleable.Layout_layout_goneMarginEnd, 12);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintVertical_weight, 38);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_weight, 37);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintVertical_chainStyle, 40);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintHorizontal_bias, 20);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintVertical_bias, 36);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintDimensionRatio, 5);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintLeft_creator, 76);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintTop_creator, 76);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintRight_creator, 76);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintBottom_creator, 76);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintBaseline_creator, 76);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginLeft, 23);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginRight, 27);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginStart, 30);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginEnd, 8);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginTop, 33);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_marginBottom, 2);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_width, 22);
            Layout.mapToConstant.append(R.styleable.Layout_android_layout_height, 21);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintCircle, 61);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintCircleRadius, 62);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintCircleAngle, 63);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintWidth_percent, 69);
            Layout.mapToConstant.append(R.styleable.Layout_layout_constraintHeight_percent, 70);
            Layout.mapToConstant.append(R.styleable.Layout_chainUseRtl, 71);
            Layout.mapToConstant.append(R.styleable.Layout_barrierDirection, 72);
            Layout.mapToConstant.append(R.styleable.Layout_barrierMargin, 73);
            Layout.mapToConstant.append(R.styleable.Layout_constraint_referenced_ids, 74);
            Layout.mapToConstant.append(R.styleable.Layout_barrierAllowsGoneWidgets, 75);
        }
    }
    
    public static class Transform
    {
        public boolean mApply;
        public float rotation;
        public float rotationX;
        public float rotationY;
        public float scaleX;
        public float scaleY;
        public float transformPivotX;
        public float transformPivotY;
        public float translationX;
        public float translationY;
        public float translationZ;
        public boolean applyElevation;
        public float elevation;
        private static SparseIntArray mapToConstant;
        private static final int ROTATION = 1;
        private static final int ROTATION_X = 2;
        private static final int ROTATION_Y = 3;
        private static final int SCALE_X = 4;
        private static final int SCALE_Y = 5;
        private static final int TRANSFORM_PIVOT_X = 6;
        private static final int TRANSFORM_PIVOT_Y = 7;
        private static final int TRANSLATION_X = 8;
        private static final int TRANSLATION_Y = 9;
        private static final int TRANSLATION_Z = 10;
        private static final int ELEVATION = 11;
        
        public Transform() {
            this.mApply = false;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = Float.NaN;
            this.transformPivotY = Float.NaN;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
        }
        
        public void copyFrom(final Transform src) {
            this.rotation = src.rotation;
            this.rotationX = src.rotationX;
            this.rotationY = src.rotationY;
            this.scaleX = src.scaleX;
            this.scaleY = src.scaleY;
            this.transformPivotX = src.transformPivotX;
            this.transformPivotY = src.transformPivotY;
            this.translationX = src.translationX;
            this.translationY = src.translationY;
            this.translationZ = src.translationZ;
            this.applyElevation = src.applyElevation;
            this.elevation = src.elevation;
        }
        
        void fillFromAttributeList(final Context context, final AttributeSet attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Transform);
            this.mApply = true;
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Transform.mapToConstant.get(attr)) {
                    case 1: {
                        this.rotation = a.getFloat(attr, this.rotation);
                        break;
                    }
                    case 2: {
                        this.rotationX = a.getFloat(attr, this.rotationX);
                        break;
                    }
                    case 3: {
                        this.rotationY = a.getFloat(attr, this.rotationY);
                        break;
                    }
                    case 4: {
                        this.scaleX = a.getFloat(attr, this.scaleX);
                        break;
                    }
                    case 5: {
                        this.scaleY = a.getFloat(attr, this.scaleY);
                        break;
                    }
                    case 6: {
                        this.transformPivotX = a.getFloat(attr, this.transformPivotX);
                        break;
                    }
                    case 7: {
                        this.transformPivotY = a.getFloat(attr, this.transformPivotY);
                        break;
                    }
                    case 8: {
                        this.translationX = a.getDimension(attr, this.translationX);
                        break;
                    }
                    case 9: {
                        this.translationY = a.getDimension(attr, this.translationY);
                        break;
                    }
                    case 10: {
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.translationZ = a.getDimension(attr, this.translationZ);
                            break;
                        }
                        break;
                    }
                    case 11: {
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.applyElevation = true;
                            this.elevation = a.getDimension(attr, this.elevation);
                            break;
                        }
                        break;
                    }
                }
            }
            a.recycle();
        }
        
        static {
            (Transform.mapToConstant = new SparseIntArray()).append(R.styleable.Transform_android_rotation, 1);
            Transform.mapToConstant.append(R.styleable.Transform_android_rotationX, 2);
            Transform.mapToConstant.append(R.styleable.Transform_android_rotationY, 3);
            Transform.mapToConstant.append(R.styleable.Transform_android_scaleX, 4);
            Transform.mapToConstant.append(R.styleable.Transform_android_scaleY, 5);
            Transform.mapToConstant.append(R.styleable.Transform_android_transformPivotX, 6);
            Transform.mapToConstant.append(R.styleable.Transform_android_transformPivotY, 7);
            Transform.mapToConstant.append(R.styleable.Transform_android_translationX, 8);
            Transform.mapToConstant.append(R.styleable.Transform_android_translationY, 9);
            Transform.mapToConstant.append(R.styleable.Transform_android_translationZ, 10);
            Transform.mapToConstant.append(R.styleable.Transform_android_elevation, 11);
        }
    }
    
    public static class PropertySet
    {
        public boolean mApply;
        public int visibility;
        public int mVisibilityMode;
        public float alpha;
        public float mProgress;
        
        public PropertySet() {
            this.mApply = false;
            this.visibility = 0;
            this.mVisibilityMode = 0;
            this.alpha = 1.0f;
            this.mProgress = Float.NaN;
        }
        
        public void copyFrom(final PropertySet src) {
            this.mApply = src.mApply;
            this.visibility = src.visibility;
            this.alpha = src.alpha;
            this.mProgress = src.mProgress;
        }
        
        void fillFromAttributeList(final Context context, final AttributeSet attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PropertySet);
            this.mApply = true;
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.PropertySet_android_alpha) {
                    this.alpha = a.getFloat(attr, this.alpha);
                }
                else if (attr == R.styleable.PropertySet_android_visibility) {
                    this.visibility = a.getInt(attr, this.visibility);
                    this.visibility = ConstraintSet.VISIBILITY_FLAGS[this.visibility];
                }
                else if (attr == R.styleable.PropertySet_visibilityMode) {
                    this.mVisibilityMode = a.getInt(attr, this.mVisibilityMode);
                }
                else if (attr == R.styleable.PropertySet_motionProgress) {
                    this.mProgress = a.getFloat(attr, this.mProgress);
                }
            }
            a.recycle();
        }
    }
    
    public static class Motion
    {
        public boolean mApply;
        public int mAnimateRelativeTo;
        public String mTransitionEasing;
        public int mPathMotionArc;
        public int mDrawPath;
        public float mMotionStagger;
        public float mPathRotate;
        private static SparseIntArray mapToConstant;
        private static final int TRANSITION_PATH_ROTATE = 1;
        private static final int PATH_MOTION_ARC = 2;
        private static final int TRANSITION_EASING = 3;
        private static final int MOTION_DRAW_PATH = 4;
        private static final int ANIMATE_RELATIVE_TO = 5;
        private static final int MOTION_STAGGER = 6;
        
        public Motion() {
            this.mApply = false;
            this.mAnimateRelativeTo = -1;
            this.mTransitionEasing = null;
            this.mPathMotionArc = -1;
            this.mDrawPath = 0;
            this.mMotionStagger = Float.NaN;
            this.mPathRotate = Float.NaN;
        }
        
        public void copyFrom(final Motion src) {
            this.mApply = src.mApply;
            this.mAnimateRelativeTo = src.mAnimateRelativeTo;
            this.mTransitionEasing = src.mTransitionEasing;
            this.mPathMotionArc = src.mPathMotionArc;
            this.mDrawPath = src.mDrawPath;
            this.mPathRotate = src.mPathRotate;
            this.mMotionStagger = src.mMotionStagger;
        }
        
        void fillFromAttributeList(final Context context, final AttributeSet attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Motion);
            this.mApply = true;
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                switch (Motion.mapToConstant.get(attr)) {
                    case 1: {
                        this.mPathRotate = a.getFloat(attr, this.mPathRotate);
                        break;
                    }
                    case 2: {
                        this.mPathMotionArc = a.getInt(attr, this.mPathMotionArc);
                        break;
                    }
                    case 3: {
                        final TypedValue type = a.peekValue(attr);
                        if (type.type == 3) {
                            this.mTransitionEasing = a.getString(attr);
                            break;
                        }
                        this.mTransitionEasing = Easing.NAMED_EASING[a.getInteger(attr, 0)];
                        break;
                    }
                    case 4: {
                        this.mDrawPath = a.getInt(attr, 0);
                        break;
                    }
                    case 5: {
                        this.mAnimateRelativeTo = lookupID(a, attr, this.mAnimateRelativeTo);
                        break;
                    }
                    case 6: {
                        this.mMotionStagger = a.getFloat(attr, this.mMotionStagger);
                        break;
                    }
                }
            }
            a.recycle();
        }
        
        static {
            (Motion.mapToConstant = new SparseIntArray()).append(R.styleable.Motion_motionPathRotate, 1);
            Motion.mapToConstant.append(R.styleable.Motion_pathMotionArc, 2);
            Motion.mapToConstant.append(R.styleable.Motion_transitionEasing, 3);
            Motion.mapToConstant.append(R.styleable.Motion_drawPath, 4);
            Motion.mapToConstant.append(R.styleable.Motion_animate_relativeTo, 5);
            Motion.mapToConstant.append(R.styleable.Motion_motionStagger, 6);
        }
    }
    
    public static class Constraint
    {
        int mViewId;
        public final PropertySet propertySet;
        public final Motion motion;
        public final Layout layout;
        public final Transform transform;
        public HashMap<String, ConstraintAttribute> mCustomConstraints;
        
        public Constraint() {
            this.propertySet = new PropertySet();
            this.motion = new Motion();
            this.layout = new Layout();
            this.transform = new Transform();
            this.mCustomConstraints = new HashMap<String, ConstraintAttribute>();
        }
        
        private ConstraintAttribute get(final String attributeName, final ConstraintAttribute.AttributeType attributeType) {
            ConstraintAttribute ret;
            if (this.mCustomConstraints.containsKey(attributeName)) {
                ret = this.mCustomConstraints.get(attributeName);
                if (ret.getType() != attributeType) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + ret.getType().name());
                }
            }
            else {
                ret = new ConstraintAttribute(attributeName, attributeType);
                this.mCustomConstraints.put(attributeName, ret);
            }
            return ret;
        }
        
        private void setStringValue(final String attributeName, final String value) {
            this.get(attributeName, ConstraintAttribute.AttributeType.STRING_TYPE).setStringValue(value);
        }
        
        private void setFloatValue(final String attributeName, final float value) {
            this.get(attributeName, ConstraintAttribute.AttributeType.FLOAT_TYPE).setFloatValue(value);
        }
        
        private void setIntValue(final String attributeName, final int value) {
            this.get(attributeName, ConstraintAttribute.AttributeType.INT_TYPE).setIntValue(value);
        }
        
        private void setColorValue(final String attributeName, final int value) {
            this.get(attributeName, ConstraintAttribute.AttributeType.COLOR_TYPE).setColorValue(value);
        }
        
        public Constraint clone() {
            final Constraint clone = new Constraint();
            clone.layout.copyFrom(this.layout);
            clone.motion.copyFrom(this.motion);
            clone.propertySet.copyFrom(this.propertySet);
            clone.transform.copyFrom(this.transform);
            clone.mViewId = this.mViewId;
            return clone;
        }
        
        private void fillFromConstraints(final ConstraintHelper helper, final int viewId, final Constraints.LayoutParams param) {
            this.fillFromConstraints(viewId, param);
            if (helper instanceof Barrier) {
                this.layout.mHelperType = 1;
                final Barrier barrier = (Barrier)helper;
                this.layout.mBarrierDirection = barrier.getType();
                this.layout.mReferenceIds = barrier.getReferencedIds();
                this.layout.mBarrierMargin = barrier.getMargin();
            }
        }
        
        private void fillFromConstraints(final int viewId, final Constraints.LayoutParams param) {
            this.fillFrom(viewId, param);
            this.propertySet.alpha = param.alpha;
            this.transform.rotation = param.rotation;
            this.transform.rotationX = param.rotationX;
            this.transform.rotationY = param.rotationY;
            this.transform.scaleX = param.scaleX;
            this.transform.scaleY = param.scaleY;
            this.transform.transformPivotX = param.transformPivotX;
            this.transform.transformPivotY = param.transformPivotY;
            this.transform.translationX = param.translationX;
            this.transform.translationY = param.translationY;
            this.transform.translationZ = param.translationZ;
            this.transform.elevation = param.elevation;
            this.transform.applyElevation = param.applyElevation;
        }
        
        private void fillFrom(final int viewId, final ConstraintLayout.LayoutParams param) {
            this.mViewId = viewId;
            this.layout.leftToLeft = param.leftToLeft;
            this.layout.leftToRight = param.leftToRight;
            this.layout.rightToLeft = param.rightToLeft;
            this.layout.rightToRight = param.rightToRight;
            this.layout.topToTop = param.topToTop;
            this.layout.topToBottom = param.topToBottom;
            this.layout.bottomToTop = param.bottomToTop;
            this.layout.bottomToBottom = param.bottomToBottom;
            this.layout.baselineToBaseline = param.baselineToBaseline;
            this.layout.startToEnd = param.startToEnd;
            this.layout.startToStart = param.startToStart;
            this.layout.endToStart = param.endToStart;
            this.layout.endToEnd = param.endToEnd;
            this.layout.horizontalBias = param.horizontalBias;
            this.layout.verticalBias = param.verticalBias;
            this.layout.dimensionRatio = param.dimensionRatio;
            this.layout.circleConstraint = param.circleConstraint;
            this.layout.circleRadius = param.circleRadius;
            this.layout.circleAngle = param.circleAngle;
            this.layout.editorAbsoluteX = param.editorAbsoluteX;
            this.layout.editorAbsoluteY = param.editorAbsoluteY;
            this.layout.orientation = param.orientation;
            this.layout.guidePercent = param.guidePercent;
            this.layout.guideBegin = param.guideBegin;
            this.layout.guideEnd = param.guideEnd;
            this.layout.mWidth = param.width;
            this.layout.mHeight = param.height;
            this.layout.leftMargin = param.leftMargin;
            this.layout.rightMargin = param.rightMargin;
            this.layout.topMargin = param.topMargin;
            this.layout.bottomMargin = param.bottomMargin;
            this.layout.verticalWeight = param.verticalWeight;
            this.layout.horizontalWeight = param.horizontalWeight;
            this.layout.verticalChainStyle = param.verticalChainStyle;
            this.layout.horizontalChainStyle = param.horizontalChainStyle;
            this.layout.constrainedWidth = param.constrainedWidth;
            this.layout.constrainedHeight = param.constrainedHeight;
            this.layout.widthDefault = param.matchConstraintDefaultWidth;
            this.layout.heightDefault = param.matchConstraintDefaultHeight;
            this.layout.widthMax = param.matchConstraintMaxWidth;
            this.layout.heightMax = param.matchConstraintMaxHeight;
            this.layout.widthMin = param.matchConstraintMinWidth;
            this.layout.heightMin = param.matchConstraintMinHeight;
            this.layout.widthPercent = param.matchConstraintPercentWidth;
            this.layout.heightPercent = param.matchConstraintPercentHeight;
            this.layout.mConstraintTag = param.constraintTag;
            this.layout.goneTopMargin = param.goneTopMargin;
            this.layout.goneBottomMargin = param.goneBottomMargin;
            this.layout.goneLeftMargin = param.goneLeftMargin;
            this.layout.goneRightMargin = param.goneRightMargin;
            this.layout.goneStartMargin = param.goneStartMargin;
            this.layout.goneEndMargin = param.goneEndMargin;
            final int currentapiVersion = Build.VERSION.SDK_INT;
            if (currentapiVersion >= 17) {
                this.layout.endMargin = param.getMarginEnd();
                this.layout.startMargin = param.getMarginStart();
            }
        }
        
        public void applyTo(final ConstraintLayout.LayoutParams param) {
            param.leftToLeft = this.layout.leftToLeft;
            param.leftToRight = this.layout.leftToRight;
            param.rightToLeft = this.layout.rightToLeft;
            param.rightToRight = this.layout.rightToRight;
            param.topToTop = this.layout.topToTop;
            param.topToBottom = this.layout.topToBottom;
            param.bottomToTop = this.layout.bottomToTop;
            param.bottomToBottom = this.layout.bottomToBottom;
            param.baselineToBaseline = this.layout.baselineToBaseline;
            param.startToEnd = this.layout.startToEnd;
            param.startToStart = this.layout.startToStart;
            param.endToStart = this.layout.endToStart;
            param.endToEnd = this.layout.endToEnd;
            param.leftMargin = this.layout.leftMargin;
            param.rightMargin = this.layout.rightMargin;
            param.topMargin = this.layout.topMargin;
            param.bottomMargin = this.layout.bottomMargin;
            param.goneStartMargin = this.layout.goneStartMargin;
            param.goneEndMargin = this.layout.goneEndMargin;
            param.goneTopMargin = this.layout.goneTopMargin;
            param.goneBottomMargin = this.layout.goneBottomMargin;
            param.horizontalBias = this.layout.horizontalBias;
            param.verticalBias = this.layout.verticalBias;
            param.circleConstraint = this.layout.circleConstraint;
            param.circleRadius = this.layout.circleRadius;
            param.circleAngle = this.layout.circleAngle;
            param.dimensionRatio = this.layout.dimensionRatio;
            param.editorAbsoluteX = this.layout.editorAbsoluteX;
            param.editorAbsoluteY = this.layout.editorAbsoluteY;
            param.verticalWeight = this.layout.verticalWeight;
            param.horizontalWeight = this.layout.horizontalWeight;
            param.verticalChainStyle = this.layout.verticalChainStyle;
            param.horizontalChainStyle = this.layout.horizontalChainStyle;
            param.constrainedWidth = this.layout.constrainedWidth;
            param.constrainedHeight = this.layout.constrainedHeight;
            param.matchConstraintDefaultWidth = this.layout.widthDefault;
            param.matchConstraintDefaultHeight = this.layout.heightDefault;
            param.matchConstraintMaxWidth = this.layout.widthMax;
            param.matchConstraintMaxHeight = this.layout.heightMax;
            param.matchConstraintMinWidth = this.layout.widthMin;
            param.matchConstraintMinHeight = this.layout.heightMin;
            param.matchConstraintPercentWidth = this.layout.widthPercent;
            param.matchConstraintPercentHeight = this.layout.heightPercent;
            param.orientation = this.layout.orientation;
            param.guidePercent = this.layout.guidePercent;
            param.guideBegin = this.layout.guideBegin;
            param.guideEnd = this.layout.guideEnd;
            param.width = this.layout.mWidth;
            param.height = this.layout.mHeight;
            if (this.layout.mConstraintTag != null) {
                param.constraintTag = this.layout.mConstraintTag;
            }
            if (Build.VERSION.SDK_INT >= 17) {
                param.setMarginStart(this.layout.startMargin);
                param.setMarginEnd(this.layout.endMargin);
            }
            param.validate();
        }
    }
}
