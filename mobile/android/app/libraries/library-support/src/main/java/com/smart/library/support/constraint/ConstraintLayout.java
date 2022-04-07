package com.smart.library.support.constraint;

import android.view.*;
import java.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.solver.*;
import android.content.*;
import com.smart.library.support.constraint.solver.widgets.analyzer.*;
import android.content.res.*;
import android.os.*;
import com.smart.library.support.constraint.solver.widgets.*;
import android.graphics.*;
import android.annotation.*;
import android.util.*;

public class ConstraintLayout extends ViewGroup
{
    public static final String VERSION = "ConstraintLayout-2.0-beta3";
    private static final String TAG = "ConstraintLayout";
    private static final boolean USE_CONSTRAINTS_HELPER = true;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_DRAW_CONSTRAINTS = false;
    SparseArray<View> mChildrenByIds;
    private ArrayList<ConstraintHelper> mConstraintHelpers;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    protected ConstraintWidgetContainer mLayoutWidget;
    private int mMinWidth;
    private int mMinHeight;
    private int mMaxWidth;
    private int mMaxHeight;
    protected boolean mDirtyHierarchy;
    private int mOptimizationLevel;
    private ConstraintSet mConstraintSet;
    protected ConstraintLayoutStates mConstraintLayoutSpec;
    private int mConstraintSetId;
    private HashMap<String, Integer> mDesignIds;
    private int mLastMeasureWidth;
    private int mLastMeasureHeight;
    int mLastMeasureWidthSize;
    int mLastMeasureHeightSize;
    int mLastMeasureWidthMode;
    int mLastMeasureHeightMode;
    private SparseArray<ConstraintWidget> mTempMapIdToWidget;
    public static final int DESIGN_INFO_ID = 0;
    private ConstraintsChangedListener mConstraintsChangedListener;
    private Metrics mMetrics;
    Measurer mMeasurer;
    private int mOnMeasureWidthMeasureSpec;
    private int mOnMeasureHeightMeasureSpec;
    
    public void setDesignInformation(final int type, final Object value1, final Object value2) {
        if (type == 0 && value1 instanceof String && value2 instanceof Integer) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<String, Integer>();
            }
            String name = (String)value1;
            final int index = name.indexOf("/");
            if (index != -1) {
                name = name.substring(index + 1);
            }
            final int id = (int)value2;
            this.mDesignIds.put(name, id);
        }
    }
    
    public Object getDesignInformation(final int type, final Object value) {
        if (type == 0 && value instanceof String) {
            final String name = (String)value;
            if (this.mDesignIds != null && this.mDesignIds.containsKey(name)) {
                return this.mDesignIds.get(name);
            }
        }
        return null;
    }
    
    public ConstraintLayout(final Context context) {
        super(context);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintLayoutSpec = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.mTempMapIdToWidget = (SparseArray<ConstraintWidget>)new SparseArray();
        this.mMeasurer = new Measurer(this);
        this.mOnMeasureWidthMeasureSpec = 0;
        this.init(null, this.mOnMeasureHeightMeasureSpec = 0, 0);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintLayoutSpec = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.mTempMapIdToWidget = (SparseArray<ConstraintWidget>)new SparseArray();
        this.mMeasurer = new Measurer(this);
        this.mOnMeasureWidthMeasureSpec = 0;
        this.init(attrs, this.mOnMeasureHeightMeasureSpec = 0, 0);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintLayoutSpec = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.mTempMapIdToWidget = (SparseArray<ConstraintWidget>)new SparseArray();
        this.mMeasurer = new Measurer(this);
        this.mOnMeasureWidthMeasureSpec = 0;
        this.init(attrs, defStyleAttr, this.mOnMeasureHeightMeasureSpec = 0);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 7;
        this.mConstraintSet = null;
        this.mConstraintLayoutSpec = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.mTempMapIdToWidget = (SparseArray<ConstraintWidget>)new SparseArray();
        this.mMeasurer = new Measurer(this);
        this.mOnMeasureWidthMeasureSpec = 0;
        this.mOnMeasureHeightMeasureSpec = 0;
        this.init(attrs, defStyleAttr, defStyleRes);
    }
    
    public void setId(final int id) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(id);
        this.mChildrenByIds.put(this.getId(),this);
    }
    
    private void init(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        this.mLayoutWidget.setCompanionWidget((Object)this);
        this.mLayoutWidget.setMeasurer((BasicMeasure.Measurer)this.mMeasurer);
        this.mChildrenByIds.put(this.getId(),this);
        this.mConstraintSet = null;
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout, defStyleAttr, defStyleRes);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = a.getDimensionPixelOffset(attr, this.mMinWidth);
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = a.getDimensionPixelOffset(attr, this.mMinHeight);
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = a.getDimensionPixelOffset(attr, this.mMaxWidth);
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = a.getDimensionPixelOffset(attr, this.mMaxHeight);
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = a.getInt(attr, this.mOptimizationLevel);
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_layoutDescription) {
                    final int id = a.getResourceId(attr, 0);
                    if (id != 0) {
                        try {
                            this.parseLayoutDescription(id);
                        }
                        catch (Resources.NotFoundException e) {
                            this.mConstraintLayoutSpec = null;
                        }
                    }
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    final int id = a.getResourceId(attr, 0);
                    try {
                        (this.mConstraintSet = new ConstraintSet()).load(this.getContext(), id);
                    }
                    catch (Resources.NotFoundException e) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = id;
                }
            }
            a.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }
    
    protected void parseLayoutDescription(final int id) {
        this.mConstraintLayoutSpec = new ConstraintLayoutStates(this.getContext(), this, id);
    }
    
    public void addView(final View child, final int index, final ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewAdded(child);
        }
    }
    
    public void removeView(final View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }
    
    public void onViewAdded(final View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        final ConstraintWidget widget = this.getViewWidget(view);
        if (view instanceof Guideline && !(widget instanceof com.smart.library.support.constraint.solver.widgets.Guideline)) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.widget = (ConstraintWidget)new com.smart.library.support.constraint.solver.widgets.Guideline();
            layoutParams.isGuideline = true;
            ((com.smart.library.support.constraint.solver.widgets.Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            final ConstraintHelper helper = (ConstraintHelper)view;
            helper.validateParams();
            final LayoutParams layoutParams2 = (LayoutParams)view.getLayoutParams();
            layoutParams2.isHelper = true;
            if (!this.mConstraintHelpers.contains(helper)) {
                this.mConstraintHelpers.add(helper);
            }
        }
        this.mChildrenByIds.put(view.getId(),view);
        this.mDirtyHierarchy = true;
    }
    
    public void onViewRemoved(final View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        final ConstraintWidget widget = this.getViewWidget(view);
        this.mLayoutWidget.remove(widget);
        this.mConstraintHelpers.remove(view);
        this.mVariableDimensionsWidgets.remove(widget);
        this.mDirtyHierarchy = true;
    }
    
    public void setMinWidth(final int value) {
        if (value == this.mMinWidth) {
            return;
        }
        this.mMinWidth = value;
        this.requestLayout();
    }
    
    public void setMinHeight(final int value) {
        if (value == this.mMinHeight) {
            return;
        }
        this.mMinHeight = value;
        this.requestLayout();
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public void setMaxWidth(final int value) {
        if (value == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = value;
        this.requestLayout();
    }
    
    public void setMaxHeight(final int value) {
        if (value == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = value;
        this.requestLayout();
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public int getMaxHeight() {
        return this.mMaxHeight;
    }
    
    private boolean updateHierarchy() {
        final int count = this.getChildCount();
        boolean recompute = false;
        for (int i = 0; i < count; ++i) {
            final View child = this.getChildAt(i);
            if (child.isLayoutRequested()) {
                recompute = true;
                break;
            }
        }
        if (recompute) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
        }
        return recompute;
    }
    
    private void setChildrenConstraints() {
        final boolean isInEditMode = this.isInEditMode();
        final int count = this.getChildCount();
        for (int i = 0; i < count; ++i) {
            final View child = this.getChildAt(i);
            final ConstraintWidget widget = this.getViewWidget(child);
            if (widget != null) {
                widget.reset();
            }
        }
        if (isInEditMode) {
            for (int i = 0; i < count; ++i) {
                final View view = this.getChildAt(i);
                try {
                    String IdAsString = this.getResources().getResourceName(view.getId());
                    this.setDesignInformation(0, IdAsString, view.getId());
                    final int slashIndex = IdAsString.indexOf(47);
                    if (slashIndex != -1) {
                        IdAsString = IdAsString.substring(slashIndex + 1);
                    }
                    this.getTargetWidget(view.getId()).setDebugName(IdAsString);
                }
                catch (Resources.NotFoundException ex) {}
            }
        }
        if (this.mConstraintSetId != -1) {
            for (int i = 0; i < count; ++i) {
                final View child = this.getChildAt(i);
                if (child.getId() == this.mConstraintSetId && child instanceof Constraints) {
                    this.mConstraintSet = ((Constraints)child).getConstraintSet();
                }
            }
        }
        if (this.mConstraintSet != null) {
            this.mConstraintSet.applyToInternal(this, true);
        }
        this.mLayoutWidget.removeAllChildren();
        final int helperCount = this.mConstraintHelpers.size();
        if (helperCount > 0) {
            for (int j = 0; j < helperCount; ++j) {
                final ConstraintHelper helper = this.mConstraintHelpers.get(j);
                helper.updatePreLayout(this);
            }
        }
        for (int j = 0; j < count; ++j) {
            final View child2 = this.getChildAt(j);
            if (child2 instanceof Placeholder) {
                ((Placeholder)child2).updatePreLayout(this);
            }
        }
        this.mTempMapIdToWidget.clear();
        this.mTempMapIdToWidget.put(0,this.mLayoutWidget);
        this.mTempMapIdToWidget.put(this.getId(),this.mLayoutWidget);
        for (int j = 0; j < count; ++j) {
            final View child2 = this.getChildAt(j);
            final ConstraintWidget widget2 = this.getViewWidget(child2);
            this.mTempMapIdToWidget.put(child2.getId(),widget2);
        }
        for (int j = 0; j < count; ++j) {
            final View child2 = this.getChildAt(j);
            final ConstraintWidget widget2 = this.getViewWidget(child2);
            if (widget2 != null) {
                final LayoutParams layoutParams = (LayoutParams)child2.getLayoutParams();
                this.mLayoutWidget.add(widget2);
                this.applyConstraintsFromLayoutParams(isInEditMode, child2, widget2, layoutParams, this.mTempMapIdToWidget);
            }
        }
    }
    
    protected void applyConstraintsFromLayoutParams(final boolean isInEditMode, final View child, final ConstraintWidget widget, final LayoutParams layoutParams, final SparseArray<ConstraintWidget> idToWidget) {
        layoutParams.validate();
        layoutParams.helped = false;
        widget.setVisibility(child.getVisibility());
        if (layoutParams.isInPlaceholder) {
            widget.setInPlaceholder(true);
            widget.setVisibility(View.GONE);
        }
        widget.setCompanionWidget((Object)child);
        if (!layoutParams.verticalDimensionFixed || !layoutParams.horizontalDimensionFixed) {
            this.mVariableDimensionsWidgets.add(widget);
        }
        if (child instanceof ConstraintHelper) {
            final ConstraintHelper helper = (ConstraintHelper)child;
            helper.resolveRtl(widget, this.mLayoutWidget.isRtl());
        }
        if (layoutParams.isGuideline) {
            final com.smart.library.support.constraint.solver.widgets.Guideline guideline = (com.smart.library.support.constraint.solver.widgets.Guideline)widget;
            int resolvedGuideBegin = layoutParams.resolvedGuideBegin;
            int resolvedGuideEnd = layoutParams.resolvedGuideEnd;
            float resolvedGuidePercent = layoutParams.resolvedGuidePercent;
            if (Build.VERSION.SDK_INT < 17) {
                resolvedGuideBegin = layoutParams.guideBegin;
                resolvedGuideEnd = layoutParams.guideEnd;
                resolvedGuidePercent = layoutParams.guidePercent;
            }
            if (resolvedGuidePercent != -1.0f) {
                guideline.setGuidePercent(resolvedGuidePercent);
            }
            else if (resolvedGuideBegin != -1) {
                guideline.setGuideBegin(resolvedGuideBegin);
            }
            else if (resolvedGuideEnd != -1) {
                guideline.setGuideEnd(resolvedGuideEnd);
            }
        }
        else {
            int resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            int resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            int resolvedRightToRight = layoutParams.resolvedRightToRight;
            int resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            int resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            float resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            if (Build.VERSION.SDK_INT < 17) {
                resolvedLeftToLeft = layoutParams.leftToLeft;
                resolvedLeftToRight = layoutParams.leftToRight;
                resolvedRightToLeft = layoutParams.rightToLeft;
                resolvedRightToRight = layoutParams.rightToRight;
                resolveGoneLeftMargin = layoutParams.goneLeftMargin;
                resolveGoneRightMargin = layoutParams.goneRightMargin;
                resolvedHorizontalBias = layoutParams.horizontalBias;
                if (resolvedLeftToLeft == -1 && resolvedLeftToRight == -1) {
                    if (layoutParams.startToStart != -1) {
                        resolvedLeftToLeft = layoutParams.startToStart;
                    }
                    else if (layoutParams.startToEnd != -1) {
                        resolvedLeftToRight = layoutParams.startToEnd;
                    }
                }
                if (resolvedRightToLeft == -1 && resolvedRightToRight == -1) {
                    if (layoutParams.endToStart != -1) {
                        resolvedRightToLeft = layoutParams.endToStart;
                    }
                    else if (layoutParams.endToEnd != -1) {
                        resolvedRightToRight = layoutParams.endToEnd;
                    }
                }
            }
            if (layoutParams.circleConstraint != -1) {
                final ConstraintWidget target = (ConstraintWidget)idToWidget.get(layoutParams.circleConstraint);
                if (target != null) {
                    widget.connectCircularConstraint(target, layoutParams.circleAngle, layoutParams.circleRadius);
                }
            }
            else {
                if (resolvedLeftToLeft != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(resolvedLeftToLeft);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.LEFT, target, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                    }
                }
                else if (resolvedLeftToRight != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(resolvedLeftToRight);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.LEFT, target, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                    }
                }
                if (resolvedRightToLeft != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(resolvedRightToLeft);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.RIGHT, target, ConstraintAnchor.Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                    }
                }
                else if (resolvedRightToRight != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(resolvedRightToRight);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.RIGHT, target, ConstraintAnchor.Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                    }
                }
                if (layoutParams.topToTop != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(layoutParams.topToTop);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.TOP, target, ConstraintAnchor.Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                    }
                }
                else if (layoutParams.topToBottom != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(layoutParams.topToBottom);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.TOP, target, ConstraintAnchor.Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                    }
                }
                if (layoutParams.bottomToTop != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(layoutParams.bottomToTop);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.BOTTOM, target, ConstraintAnchor.Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                    }
                }
                else if (layoutParams.bottomToBottom != -1) {
                    final ConstraintWidget target = (ConstraintWidget)idToWidget.get(layoutParams.bottomToBottom);
                    if (target != null) {
                        widget.immediateConnect(ConstraintAnchor.Type.BOTTOM, target, ConstraintAnchor.Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                    }
                }
                if (layoutParams.baselineToBaseline != -1) {
                    final View view = (View)this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                    final ConstraintWidget target2 = (ConstraintWidget)idToWidget.get(layoutParams.baselineToBaseline);
                    if (target2 != null && view != null && view.getLayoutParams() instanceof LayoutParams) {
                        final LayoutParams targetParams = (LayoutParams)view.getLayoutParams();
                        layoutParams.needsBaseline = true;
                        targetParams.needsBaseline = true;
                        final ConstraintAnchor baseline = widget.getAnchor(ConstraintAnchor.Type.BASELINE);
                        final ConstraintAnchor targetBaseline = target2.getAnchor(ConstraintAnchor.Type.BASELINE);
                        baseline.connect(targetBaseline, 0, -1, true);
                        widget.setHasBaseline(true);
                        targetParams.widget.setHasBaseline(true);
                        widget.getAnchor(ConstraintAnchor.Type.TOP).reset();
                        widget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                    }
                }
                if (resolvedHorizontalBias >= 0.0f) {
                    widget.setHorizontalBiasPercent(resolvedHorizontalBias);
                }
                if (layoutParams.verticalBias >= 0.0f) {
                    widget.setVerticalBiasPercent(layoutParams.verticalBias);
                }
            }
            if (isInEditMode && (layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1)) {
                widget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
            }
            if (!layoutParams.horizontalDimensionFixed) {
                if (layoutParams.width == -1) {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    widget.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                    widget.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
                }
                else {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    widget.setWidth(0);
                }
            }
            else {
                widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                widget.setWidth(layoutParams.width);
                if (layoutParams.width == -2) {
                    widget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
            if (!layoutParams.verticalDimensionFixed) {
                if (layoutParams.height == -1) {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    widget.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                    widget.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
                }
                else {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    widget.setHeight(0);
                }
            }
            else {
                widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                widget.setHeight(layoutParams.height);
                if (layoutParams.height == -2) {
                    widget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
            if (layoutParams.dimensionRatio != null) {
                widget.setDimensionRatio(layoutParams.dimensionRatio);
            }
            widget.setHorizontalWeight(layoutParams.horizontalWeight);
            widget.setVerticalWeight(layoutParams.verticalWeight);
            widget.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
            widget.setVerticalChainStyle(layoutParams.verticalChainStyle);
            widget.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth, layoutParams.matchConstraintPercentWidth);
            widget.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight, layoutParams.matchConstraintPercentHeight);
        }
    }
    
    private final ConstraintWidget getTargetWidget(final int id) {
        if (id == 0) {
            return (ConstraintWidget)this.mLayoutWidget;
        }
        View view = (View)this.mChildrenByIds.get(id);
        if (view == null) {
            view = this.findViewById(id);
            if (view != null && view != this && view.getParent() == this) {
                this.onViewAdded(view);
            }
        }
        if (view == this) {
            return (ConstraintWidget)this.mLayoutWidget;
        }
        return (view == null) ? null : ((LayoutParams)view.getLayoutParams()).widget;
    }
    
    public final ConstraintWidget getViewWidget(final View view) {
        if (view == this) {
            return (ConstraintWidget)this.mLayoutWidget;
        }
        return (view == null) ? null : ((LayoutParams)view.getLayoutParams()).widget;
    }
    
    public void fillMetrics(final Metrics metrics) {
        this.mMetrics = metrics;
        this.mLayoutWidget.fillMetrics(metrics);
    }
    
    protected void resolveSystem(final ConstraintWidgetContainer layout, final int optimizationLevel, final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int paddingY = this.getPaddingTop();
        final int paddingHeight = paddingY + this.getPaddingBottom();
        int paddingWidth = 0;
        int paddingX = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            paddingX = this.getPaddingStart();
            paddingWidth = paddingX + this.getPaddingEnd();
        }
        if (paddingWidth == 0) {
            paddingX = this.getPaddingLeft();
            paddingWidth = paddingX + this.getPaddingRight();
        }
        widthSize -= paddingWidth;
        heightSize -= paddingHeight;
        this.setSelfDimensionBehaviour(layout, widthMode, widthSize, heightMode, heightSize);
        layout.measure(optimizationLevel, widthMode, widthSize, heightMode, heightSize, this.mLastMeasureWidth, this.mLastMeasureHeight, paddingX, paddingY);
    }
    
    protected void resolveMeasuredDimension(final int widthMeasureSpec, final int heightMeasureSpec, final int measuredWidth, final int measuredHeight, final boolean isWidthMeasuredTooSmall, final boolean isHeightMeasuredTooSmall) {
        final int childState = 0;
        final int heightPadding = this.getPaddingTop() + this.getPaddingBottom();
        final int widthPadding = this.getPaddingLeft() + this.getPaddingRight();
        final int androidLayoutWidth = measuredWidth + widthPadding;
        final int androidLayoutHeight = measuredHeight + heightPadding;
        if (Build.VERSION.SDK_INT >= 11) {
            int resolvedWidthSize = resolveSizeAndState(androidLayoutWidth, widthMeasureSpec, childState);
            int resolvedHeightSize = resolveSizeAndState(androidLayoutHeight, heightMeasureSpec, childState << 16);
            resolvedWidthSize &= 0xFFFFFF;
            resolvedHeightSize &= 0xFFFFFF;
            resolvedWidthSize = Math.min(this.mMaxWidth, resolvedWidthSize);
            resolvedHeightSize = Math.min(this.mMaxHeight, resolvedHeightSize);
            if (isWidthMeasuredTooSmall) {
                resolvedWidthSize |= 0x1000000;
            }
            if (isHeightMeasuredTooSmall) {
                resolvedHeightSize |= 0x1000000;
            }
            this.setMeasuredDimension(resolvedWidthSize, resolvedHeightSize);
            this.mLastMeasureWidth = resolvedWidthSize;
            this.mLastMeasureHeight = resolvedHeightSize;
        }
        else {
            this.setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
            this.mLastMeasureWidth = androidLayoutWidth;
            this.mLastMeasureHeight = androidLayoutHeight;
        }
    }
    
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final long time = 0L;
        this.mOnMeasureWidthMeasureSpec = widthMeasureSpec;
        this.mOnMeasureHeightMeasureSpec = heightMeasureSpec;
        final boolean isRtlSupported = (this.getContext().getApplicationInfo().flags & 0x400000) != 0x0;
        final boolean isRtl = isRtlSupported && View.LAYOUT_DIRECTION_RTL == this.getLayoutDirection();
        this.mLayoutWidget.setRtl(isRtl);
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            if (this.updateHierarchy()) {
                this.mLayoutWidget.updateHierarchy();
            }
        }
        this.resolveSystem(this.mLayoutWidget, this.mOptimizationLevel, widthMeasureSpec, heightMeasureSpec);
        this.resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
    }
    
    protected void setSelfDimensionBehaviour(final ConstraintWidgetContainer layout, final int widthMode, final int widthSize, final int heightMode, final int heightSize) {
        final int heightPadding = this.getPaddingTop() + this.getPaddingBottom();
        final int widthPadding = this.getPaddingLeft() + this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour widthBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour heightBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        int desiredWidth = 0;
        int desiredHeight = 0;
        switch (widthMode) {
            case Integer.MIN_VALUE: {
                widthBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                desiredWidth = widthSize;
                break;
            }
            case 0: {
                widthBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                break;
            }
            case MeasureSpec.EXACTLY: {
                desiredWidth = Math.min(this.mMaxWidth, widthSize);
                break;
            }
        }
        switch (heightMode) {
            case Integer.MIN_VALUE: {
                heightBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                desiredHeight = heightSize;
                break;
            }
            case 0: {
                heightBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                break;
            }
            case MeasureSpec.EXACTLY: {
                desiredHeight = Math.min(this.mMaxHeight, heightSize);
                break;
            }
        }
        if (desiredWidth != layout.getWidth() || desiredHeight != layout.getHeight()) {
            layout.invalidateMeasures();
        }
        layout.setX(0);
        layout.setY(0);
        layout.setMaxWidth(this.mMaxWidth);
        layout.setMaxHeight(this.mMaxHeight);
        layout.setMinWidth(0);
        layout.setMinHeight(0);
        layout.setHorizontalDimensionBehaviour(widthBehaviour);
        layout.setWidth(desiredWidth);
        layout.setVerticalDimensionBehaviour(heightBehaviour);
        layout.setHeight(desiredHeight);
        layout.setMinWidth(this.mMinWidth - widthPadding);
        layout.setMinHeight(this.mMinHeight - heightPadding);
    }
    
    public void setState(final int id, final int screenWidth, final int screenHeight) {
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.updateConstraints(id, screenWidth, screenHeight);
        }
    }
    
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        final int widgetsCount = this.getChildCount();
        final boolean isInEditMode = this.isInEditMode();
        for (int i = 0; i < widgetsCount; ++i) {
            final View child = this.getChildAt(i);
            final LayoutParams params = (LayoutParams)child.getLayoutParams();
            final ConstraintWidget widget = params.widget;
            if (child.getVisibility() != View.GONE || params.isGuideline || params.isHelper || params.isVirtualGroup || isInEditMode) {
                if (!params.isInPlaceholder) {
                    final int l = widget.getX();
                    final int t = widget.getY();
                    final int r = l + widget.getWidth();
                    final int b = t + widget.getHeight();
                    child.layout(l, t, r, b);
                    if (child instanceof Placeholder) {
                        final Placeholder holder = (Placeholder)child;
                        final View content = holder.getContent();
                        if (content != null) {
                            content.setVisibility(View.VISIBLE);
                            content.layout(l, t, r, b);
                        }
                    }
                }
            }
        }
        final int helperCount = this.mConstraintHelpers.size();
        if (helperCount > 0) {
            for (int j = 0; j < helperCount; ++j) {
                final ConstraintHelper helper = this.mConstraintHelpers.get(j);
                helper.updatePostLayout(this);
            }
        }
    }
    
    public void setOptimizationLevel(final int level) {
        this.mOptimizationLevel = level;
        this.mLayoutWidget.setOptimizationLevel(level);
    }
    
    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    protected ViewGroup.LayoutParams generateLayoutParams(final ViewGroup.LayoutParams p) {
        return (ViewGroup.LayoutParams)new LayoutParams(p);
    }
    
    protected boolean checkLayoutParams(final ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }
    
    public void setConstraintSet(final ConstraintSet set) {
        this.mConstraintSet = set;
    }
    
    public View getViewById(final int id) {
        return (View)this.mChildrenByIds.get(id);
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.isInEditMode()) {
            final int count = this.getChildCount();
            final float cw = this.getWidth();
            final float ch = this.getHeight();
            final float ow = 1080.0f;
            final float oh = 1920.0f;
            for (int i = 0; i < count; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    final Object tag = child.getTag();
                    if (tag != null && tag instanceof String) {
                        final String coordinates = (String)tag;
                        final String[] split = coordinates.split(",");
                        if (split.length == 4) {
                            int x = Integer.parseInt(split[0]);
                            int y = Integer.parseInt(split[1]);
                            int w = Integer.parseInt(split[2]);
                            int h = Integer.parseInt(split[3]);
                            x = (int)(x / ow * cw);
                            y = (int)(y / oh * ch);
                            w = (int)(w / ow * cw);
                            h = (int)(h / oh * ch);
                            final Paint paint = new Paint();
                            paint.setColor(-65536);
                            canvas.drawLine((float)x, (float)y, (float)(x + w), (float)y, paint);
                            canvas.drawLine((float)(x + w), (float)y, (float)(x + w), (float)(y + h), paint);
                            canvas.drawLine((float)(x + w), (float)(y + h), (float)x, (float)(y + h), paint);
                            canvas.drawLine((float)x, (float)(y + h), (float)x, (float)y, paint);
                            paint.setColor(-16711936);
                            canvas.drawLine((float)x, (float)y, (float)(x + w), (float)(y + h), paint);
                            canvas.drawLine((float)x, (float)(y + h), (float)(x + w), (float)y, paint);
                        }
                    }
                }
            }
        }
    }
    
    public void setOnConstraintsChanged(final ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.setOnConstraintsChanged(constraintsChangedListener);
        }
    }
    
    public void loadLayoutDescription(final int layoutDescription) {
        if (layoutDescription != 0) {
            try {
                this.mConstraintLayoutSpec = new ConstraintLayoutStates(this.getContext(), this, layoutDescription);
            }
            catch (Resources.NotFoundException e) {
                this.mConstraintLayoutSpec = null;
            }
        }
        else {
            this.mConstraintLayoutSpec = null;
        }
    }
    
    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    class Measurer implements BasicMeasure.Measurer
    {
        ConstraintLayout layout;
        
        public Measurer(final ConstraintLayout l) {
            this.layout = l;
        }
        
        @SuppressLint({ "WrongCall" })
        public final void measure(final ConstraintWidget widget, final BasicMeasure.Measure measure) {
            if (widget == null) {
                return;
            }
            if (widget.getVisibility() == 8) {
                measure.measuredWidth = 0;
                measure.measuredHeight = 0;
                measure.measuredBaseline = 0;
                return;
            }
            final ConstraintWidget.DimensionBehaviour horizontalBehavior = measure.horizontalBehavior;
            final ConstraintWidget.DimensionBehaviour verticalBehavior = measure.verticalBehavior;
            final int horizontalDimension = measure.horizontalDimension;
            final int verticalDimension = measure.verticalDimension;
            int horizontalSpec = 0;
            int verticalSpec = 0;
            final int heightPadding = this.layout.getPaddingTop() + this.layout.getPaddingBottom();
            final int widthPadding = this.layout.getPaddingLeft() + this.layout.getPaddingRight();
            boolean didHorizontalWrap = false;
            boolean didVerticalWrap = false;
            switch (horizontalBehavior) {
                case FIXED: {
                    horizontalSpec = MeasureSpec.makeMeasureSpec(horizontalDimension, MeasureSpec.EXACTLY);
                    break;
                }
                case WRAP_CONTENT: {
                    horizontalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureWidthMeasureSpec, widthPadding, -2);
                    didHorizontalWrap = true;
                    break;
                }
                case MATCH_PARENT: {
                    horizontalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureWidthMeasureSpec, widthPadding + widget.getHorizontalMargin(), -1);
                    break;
                }
                case MATCH_CONSTRAINT: {
                    horizontalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureWidthMeasureSpec, widthPadding, -2);
                    final boolean shouldDoWrap = widget.mMatchConstraintDefaultWidth == 1;
                    if (measure.useDeprecated && (!shouldDoWrap || (shouldDoWrap && widget.wrapMeasure[0] != widget.getWidth()))) {
                        horizontalSpec = MeasureSpec.makeMeasureSpec(widget.getWidth(), MeasureSpec.EXACTLY);
                        break;
                    }
                    didHorizontalWrap = true;
                    break;
                }
            }
            switch (verticalBehavior) {
                case FIXED: {
                    verticalSpec = MeasureSpec.makeMeasureSpec(verticalDimension, MeasureSpec.EXACTLY);
                    break;
                }
                case WRAP_CONTENT: {
                    verticalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureHeightMeasureSpec, heightPadding, -2);
                    didVerticalWrap = true;
                    break;
                }
                case MATCH_PARENT: {
                    verticalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureHeightMeasureSpec, heightPadding + widget.getVerticalMargin(), -1);
                    break;
                }
                case MATCH_CONSTRAINT: {
                    verticalSpec = ViewGroup.getChildMeasureSpec(this.layout.mOnMeasureHeightMeasureSpec, heightPadding, -2);
                    final boolean shouldDoWrap = widget.mMatchConstraintDefaultHeight == 1;
                    if (measure.useDeprecated && (!shouldDoWrap || (shouldDoWrap && widget.wrapMeasure[1] != widget.getHeight()))) {
                        verticalSpec = MeasureSpec.makeMeasureSpec(widget.getHeight(), MeasureSpec.EXACTLY);
                        break;
                    }
                    didVerticalWrap = true;
                    break;
                }
            }
            final boolean horizontalMatchConstraints = horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            final boolean verticalMatchConstraints = verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            final boolean verticalDimensionKnown = verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED;
            final boolean horizontalDimensionKnown = horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED;
            final boolean horizontalUseRatio = horizontalMatchConstraints && widget.mDimensionRatio > 0.0f;
            final boolean verticalUseRatio = verticalMatchConstraints && widget.mDimensionRatio > 0.0f;
            final View child = (View)widget.getCompanionWidget();
            final LayoutParams params = (LayoutParams)child.getLayoutParams();
            int width = 0;
            int height = 0;
            int baseline = 0;
            if (measure.useDeprecated || !horizontalMatchConstraints || widget.mMatchConstraintDefaultWidth != 0 || !verticalMatchConstraints || widget.mMatchConstraintDefaultHeight != 0) {
                if (child instanceof VirtualLayout && widget instanceof com.smart.library.support.constraint.solver.widgets.VirtualLayout) {
                    final com.smart.library.support.constraint.solver.widgets.VirtualLayout layout = (com.smart.library.support.constraint.solver.widgets.VirtualLayout)widget;
                    ((VirtualLayout)child).onMeasure(layout, horizontalSpec, verticalSpec);
                }
                else {
                    child.measure(horizontalSpec, verticalSpec);
                }
                final int w = child.getMeasuredWidth();
                final int h = child.getMeasuredHeight();
                baseline = child.getBaseline();
                width = w;
                height = h;
                if (didHorizontalWrap) {
                    widget.wrapMeasure[0] = w;
                }
                if (didVerticalWrap) {
                    widget.wrapMeasure[1] = h;
                }
                if (widget.mMatchConstraintMinWidth > 0) {
                    width = Math.max(widget.mMatchConstraintMinWidth, width);
                }
                if (widget.mMatchConstraintMaxWidth > 0) {
                    width = Math.min(widget.mMatchConstraintMaxWidth, width);
                }
                if (widget.mMatchConstraintMinHeight > 0) {
                    height = Math.max(widget.mMatchConstraintMinHeight, height);
                }
                if (widget.mMatchConstraintMaxHeight > 0) {
                    height = Math.min(widget.mMatchConstraintMaxHeight, height);
                }
                if (horizontalUseRatio && verticalDimensionKnown) {
                    final float ratio = widget.mDimensionRatio;
                    width = (int)(0.5f + height * ratio);
                }
                else if (verticalUseRatio && horizontalDimensionKnown) {
                    final float ratio = widget.mDimensionRatio;
                    height = (int)(0.5f + width / ratio);
                }
                if (w != width || h != height) {
                    if (w != width) {
                        horizontalSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                    }
                    if (h != height) {
                        verticalSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                    }
                    child.measure(horizontalSpec, verticalSpec);
                    width = child.getMeasuredWidth();
                    height = child.getMeasuredHeight();
                    baseline = child.getBaseline();
                }
            }
            boolean hasBaseline = baseline != -1;
            measure.measuredNeedsSolverPass = (width != measure.horizontalDimension || height != measure.verticalDimension);
            if (params.needsBaseline) {
                hasBaseline = true;
            }
            if (hasBaseline && baseline != -1 && widget.getBaselineDistance() != baseline) {
                measure.measuredNeedsSolverPass = true;
            }
            measure.measuredWidth = width;
            measure.measuredHeight = height;
            measure.measuredHasBaseline = hasBaseline;
            measure.measuredBaseline = baseline;
        }
        
        public final void didMeasures() {
            for (int widgetsCount = this.layout.getChildCount(), i = 0; i < widgetsCount; ++i) {
                final View child = this.layout.getChildAt(i);
                if (child instanceof Placeholder) {
                    ((Placeholder)child).updatePostMeasure(this.layout);
                }
            }
            final int helperCount = this.layout.mConstraintHelpers.size();
            if (helperCount > 0) {
                for (int j = 0; j < helperCount; ++j) {
                    final ConstraintHelper helper = this.layout.mConstraintHelpers.get(j);
                    helper.updatePostMeasure(this.layout);
                }
            }
        }
    }
    
    public static class LayoutParams extends MarginLayoutParams
    {
        public static final int MATCH_CONSTRAINT = 0;
        public static final int PARENT_ID = 0;
        public static final int UNSET = -1;
        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;
        public static final int LEFT = 1;
        public static final int RIGHT = 2;
        public static final int TOP = 3;
        public static final int BOTTOM = 4;
        public static final int BASELINE = 5;
        public static final int START = 6;
        public static final int END = 7;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_PERCENT = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int CHAIN_PACKED = 2;
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
        public int circleConstraint;
        public int circleRadius;
        public float circleAngle;
        public int startToEnd;
        public int startToStart;
        public int endToStart;
        public int endToEnd;
        public int goneLeftMargin;
        public int goneTopMargin;
        public int goneRightMargin;
        public int goneBottomMargin;
        public int goneStartMargin;
        public int goneEndMargin;
        public float horizontalBias;
        public float verticalBias;
        public String dimensionRatio;
        float dimensionRatioValue;
        int dimensionRatioSide;
        public float horizontalWeight;
        public float verticalWeight;
        public int horizontalChainStyle;
        public int verticalChainStyle;
        public int matchConstraintDefaultWidth;
        public int matchConstraintDefaultHeight;
        public int matchConstraintMinWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMaxHeight;
        public float matchConstraintPercentWidth;
        public float matchConstraintPercentHeight;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int orientation;
        public boolean constrainedWidth;
        public boolean constrainedHeight;
        public String constraintTag;
        boolean horizontalDimensionFixed;
        boolean verticalDimensionFixed;
        boolean needsBaseline;
        boolean isGuideline;
        boolean isHelper;
        boolean isInPlaceholder;
        boolean isVirtualGroup;
        int resolvedLeftToLeft;
        int resolvedLeftToRight;
        int resolvedRightToLeft;
        int resolvedRightToRight;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
        float resolvedHorizontalBias;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        ConstraintWidget widget;
        public boolean helped;
        
        public ConstraintWidget getConstraintWidget() {
            return this.widget;
        }
        
        public void setWidgetDebugName(final String text) {
            this.widget.setDebugName(text);
        }
        
        public void reset() {
            if (this.widget != null) {
                this.widget.reset();
            }
        }
        
        public LayoutParams(final LayoutParams source) {
            super((MarginLayoutParams)source);
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
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            this.guideBegin = source.guideBegin;
            this.guideEnd = source.guideEnd;
            this.guidePercent = source.guidePercent;
            this.leftToLeft = source.leftToLeft;
            this.leftToRight = source.leftToRight;
            this.rightToLeft = source.rightToLeft;
            this.rightToRight = source.rightToRight;
            this.topToTop = source.topToTop;
            this.topToBottom = source.topToBottom;
            this.bottomToTop = source.bottomToTop;
            this.bottomToBottom = source.bottomToBottom;
            this.baselineToBaseline = source.baselineToBaseline;
            this.circleConstraint = source.circleConstraint;
            this.circleRadius = source.circleRadius;
            this.circleAngle = source.circleAngle;
            this.startToEnd = source.startToEnd;
            this.startToStart = source.startToStart;
            this.endToStart = source.endToStart;
            this.endToEnd = source.endToEnd;
            this.goneLeftMargin = source.goneLeftMargin;
            this.goneTopMargin = source.goneTopMargin;
            this.goneRightMargin = source.goneRightMargin;
            this.goneBottomMargin = source.goneBottomMargin;
            this.goneStartMargin = source.goneStartMargin;
            this.goneEndMargin = source.goneEndMargin;
            this.horizontalBias = source.horizontalBias;
            this.verticalBias = source.verticalBias;
            this.dimensionRatio = source.dimensionRatio;
            this.dimensionRatioValue = source.dimensionRatioValue;
            this.dimensionRatioSide = source.dimensionRatioSide;
            this.horizontalWeight = source.horizontalWeight;
            this.verticalWeight = source.verticalWeight;
            this.horizontalChainStyle = source.horizontalChainStyle;
            this.verticalChainStyle = source.verticalChainStyle;
            this.constrainedWidth = source.constrainedWidth;
            this.constrainedHeight = source.constrainedHeight;
            this.matchConstraintDefaultWidth = source.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = source.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = source.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = source.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = source.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = source.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = source.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = source.matchConstraintPercentHeight;
            this.editorAbsoluteX = source.editorAbsoluteX;
            this.editorAbsoluteY = source.editorAbsoluteY;
            this.orientation = source.orientation;
            this.horizontalDimensionFixed = source.horizontalDimensionFixed;
            this.verticalDimensionFixed = source.verticalDimensionFixed;
            this.needsBaseline = source.needsBaseline;
            this.isGuideline = source.isGuideline;
            this.resolvedLeftToLeft = source.resolvedLeftToLeft;
            this.resolvedLeftToRight = source.resolvedLeftToRight;
            this.resolvedRightToLeft = source.resolvedRightToLeft;
            this.resolvedRightToRight = source.resolvedRightToRight;
            this.resolveGoneLeftMargin = source.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = source.resolveGoneRightMargin;
            this.resolvedHorizontalBias = source.resolvedHorizontalBias;
            this.constraintTag = source.constraintTag;
            this.widget = source.widget;
        }
        
        public LayoutParams(final Context c, final AttributeSet attrs) {
            super(c, attrs);
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
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                final int look = Table.map.get(attr);
                switch (look) {
                    case 8: {
                        this.leftToLeft = a.getResourceId(attr, this.leftToLeft);
                        if (this.leftToLeft == -1) {
                            this.leftToLeft = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 9: {
                        this.leftToRight = a.getResourceId(attr, this.leftToRight);
                        if (this.leftToRight == -1) {
                            this.leftToRight = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 10: {
                        this.rightToLeft = a.getResourceId(attr, this.rightToLeft);
                        if (this.rightToLeft == -1) {
                            this.rightToLeft = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 11: {
                        this.rightToRight = a.getResourceId(attr, this.rightToRight);
                        if (this.rightToRight == -1) {
                            this.rightToRight = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 12: {
                        this.topToTop = a.getResourceId(attr, this.topToTop);
                        if (this.topToTop == -1) {
                            this.topToTop = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 13: {
                        this.topToBottom = a.getResourceId(attr, this.topToBottom);
                        if (this.topToBottom == -1) {
                            this.topToBottom = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 14: {
                        this.bottomToTop = a.getResourceId(attr, this.bottomToTop);
                        if (this.bottomToTop == -1) {
                            this.bottomToTop = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 15: {
                        this.bottomToBottom = a.getResourceId(attr, this.bottomToBottom);
                        if (this.bottomToBottom == -1) {
                            this.bottomToBottom = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 16: {
                        this.baselineToBaseline = a.getResourceId(attr, this.baselineToBaseline);
                        if (this.baselineToBaseline == -1) {
                            this.baselineToBaseline = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        this.circleConstraint = a.getResourceId(attr, this.circleConstraint);
                        if (this.circleConstraint == -1) {
                            this.circleConstraint = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        this.circleRadius = a.getDimensionPixelSize(attr, this.circleRadius);
                        break;
                    }
                    case 4: {
                        this.circleAngle = a.getFloat(attr, this.circleAngle) % 360.0f;
                        if (this.circleAngle < 0.0f) {
                            this.circleAngle = (360.0f - this.circleAngle) % 360.0f;
                            break;
                        }
                        break;
                    }
                    case 49: {
                        this.editorAbsoluteX = a.getDimensionPixelOffset(attr, this.editorAbsoluteX);
                        break;
                    }
                    case 50: {
                        this.editorAbsoluteY = a.getDimensionPixelOffset(attr, this.editorAbsoluteY);
                        break;
                    }
                    case 5: {
                        this.guideBegin = a.getDimensionPixelOffset(attr, this.guideBegin);
                        break;
                    }
                    case 6: {
                        this.guideEnd = a.getDimensionPixelOffset(attr, this.guideEnd);
                        break;
                    }
                    case 7: {
                        this.guidePercent = a.getFloat(attr, this.guidePercent);
                        break;
                    }
                    case 1: {
                        this.orientation = a.getInt(attr, this.orientation);
                        break;
                    }
                    case 17: {
                        this.startToEnd = a.getResourceId(attr, this.startToEnd);
                        if (this.startToEnd == -1) {
                            this.startToEnd = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 18: {
                        this.startToStart = a.getResourceId(attr, this.startToStart);
                        if (this.startToStart == -1) {
                            this.startToStart = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 19: {
                        this.endToStart = a.getResourceId(attr, this.endToStart);
                        if (this.endToStart == -1) {
                            this.endToStart = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 20: {
                        this.endToEnd = a.getResourceId(attr, this.endToEnd);
                        if (this.endToEnd == -1) {
                            this.endToEnd = a.getInt(attr, -1);
                            break;
                        }
                        break;
                    }
                    case 21: {
                        this.goneLeftMargin = a.getDimensionPixelSize(attr, this.goneLeftMargin);
                        break;
                    }
                    case 22: {
                        this.goneTopMargin = a.getDimensionPixelSize(attr, this.goneTopMargin);
                        break;
                    }
                    case 23: {
                        this.goneRightMargin = a.getDimensionPixelSize(attr, this.goneRightMargin);
                        break;
                    }
                    case 24: {
                        this.goneBottomMargin = a.getDimensionPixelSize(attr, this.goneBottomMargin);
                        break;
                    }
                    case 25: {
                        this.goneStartMargin = a.getDimensionPixelSize(attr, this.goneStartMargin);
                        break;
                    }
                    case 26: {
                        this.goneEndMargin = a.getDimensionPixelSize(attr, this.goneEndMargin);
                        break;
                    }
                    case 29: {
                        this.horizontalBias = a.getFloat(attr, this.horizontalBias);
                        break;
                    }
                    case 30: {
                        this.verticalBias = a.getFloat(attr, this.verticalBias);
                        break;
                    }
                    case 44: {
                        this.dimensionRatio = a.getString(attr);
                        this.dimensionRatioValue = Float.NaN;
                        this.dimensionRatioSide = -1;
                        if (this.dimensionRatio != null) {
                            final int len = this.dimensionRatio.length();
                            int commaIndex = this.dimensionRatio.indexOf(44);
                            if (commaIndex > 0 && commaIndex < len - 1) {
                                final String dimension = this.dimensionRatio.substring(0, commaIndex);
                                if (dimension.equalsIgnoreCase("W")) {
                                    this.dimensionRatioSide = 0;
                                }
                                else if (dimension.equalsIgnoreCase("H")) {
                                    this.dimensionRatioSide = 1;
                                }
                                ++commaIndex;
                            }
                            else {
                                commaIndex = 0;
                            }
                            final int colonIndex = this.dimensionRatio.indexOf(58);
                            if (colonIndex >= 0 && colonIndex < len - 1) {
                                final String nominator = this.dimensionRatio.substring(commaIndex, colonIndex);
                                final String denominator = this.dimensionRatio.substring(colonIndex + 1);
                                if (nominator.length() > 0 && denominator.length() > 0) {
                                    try {
                                        final float nominatorValue = Float.parseFloat(nominator);
                                        final float denominatorValue = Float.parseFloat(denominator);
                                        if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                                            if (this.dimensionRatioSide == 1) {
                                                this.dimensionRatioValue = Math.abs(denominatorValue / nominatorValue);
                                            }
                                            else {
                                                this.dimensionRatioValue = Math.abs(nominatorValue / denominatorValue);
                                            }
                                        }
                                    }
                                    catch (NumberFormatException ex) {}
                                }
                            }
                            else {
                                final String r = this.dimensionRatio.substring(commaIndex);
                                if (r.length() > 0) {
                                    try {
                                        this.dimensionRatioValue = Float.parseFloat(r);
                                    }
                                    catch (NumberFormatException ex2) {}
                                }
                            }
                            break;
                        }
                        break;
                    }
                    case 45: {
                        this.horizontalWeight = a.getFloat(attr, this.horizontalWeight);
                        break;
                    }
                    case 46: {
                        this.verticalWeight = a.getFloat(attr, this.verticalWeight);
                        break;
                    }
                    case 47: {
                        this.horizontalChainStyle = a.getInt(attr, 0);
                        break;
                    }
                    case 48: {
                        this.verticalChainStyle = a.getInt(attr, 0);
                        break;
                    }
                    case 27: {
                        this.constrainedWidth = a.getBoolean(attr, this.constrainedWidth);
                        break;
                    }
                    case 28: {
                        this.constrainedHeight = a.getBoolean(attr, this.constrainedHeight);
                        break;
                    }
                    case 31: {
                        this.matchConstraintDefaultWidth = a.getInt(attr, 0);
                        if (this.matchConstraintDefaultWidth == 1) {
                            Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                            break;
                        }
                        break;
                    }
                    case 32: {
                        this.matchConstraintDefaultHeight = a.getInt(attr, 0);
                        if (this.matchConstraintDefaultHeight == 1) {
                            Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                            break;
                        }
                        break;
                    }
                    case 33: {
                        try {
                            this.matchConstraintMinWidth = a.getDimensionPixelSize(attr, this.matchConstraintMinWidth);
                        }
                        catch (Exception e) {
                            final int value = a.getInt(attr, this.matchConstraintMinWidth);
                            if (value == -2) {
                                this.matchConstraintMinWidth = -2;
                            }
                        }
                        break;
                    }
                    case 34: {
                        try {
                            this.matchConstraintMaxWidth = a.getDimensionPixelSize(attr, this.matchConstraintMaxWidth);
                        }
                        catch (Exception e) {
                            final int value = a.getInt(attr, this.matchConstraintMaxWidth);
                            if (value == -2) {
                                this.matchConstraintMaxWidth = -2;
                            }
                        }
                        break;
                    }
                    case 35: {
                        this.matchConstraintPercentWidth = Math.max(0.0f, a.getFloat(attr, this.matchConstraintPercentWidth));
                        break;
                    }
                    case 36: {
                        try {
                            this.matchConstraintMinHeight = a.getDimensionPixelSize(attr, this.matchConstraintMinHeight);
                        }
                        catch (Exception e) {
                            final int value = a.getInt(attr, this.matchConstraintMinHeight);
                            if (value == -2) {
                                this.matchConstraintMinHeight = -2;
                            }
                        }
                        break;
                    }
                    case 37: {
                        try {
                            this.matchConstraintMaxHeight = a.getDimensionPixelSize(attr, this.matchConstraintMaxHeight);
                        }
                        catch (Exception e) {
                            final int value = a.getInt(attr, this.matchConstraintMaxHeight);
                            if (value == -2) {
                                this.matchConstraintMaxHeight = -2;
                            }
                        }
                        break;
                    }
                    case 38: {
                        this.matchConstraintPercentHeight = Math.max(0.0f, a.getFloat(attr, this.matchConstraintPercentHeight));
                        break;
                    }
                    case 51: {
                        this.constraintTag = a.getString(attr);
                    }
                    case 39: {}
                    case 40: {}
                    case 41: {}
                }
            }
            a.recycle();
            this.validate();
        }
        
        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                if (this.matchConstraintDefaultWidth == 0) {
                    this.matchConstraintDefaultWidth = 1;
                }
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                if (this.matchConstraintDefaultHeight == 0) {
                    this.matchConstraintDefaultHeight = 1;
                }
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof com.smart.library.support.constraint.solver.widgets.Guideline)) {
                    this.widget = (ConstraintWidget)new com.smart.library.support.constraint.solver.widgets.Guideline();
                }
                ((com.smart.library.support.constraint.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
            }
        }
        
        public LayoutParams(final int width, final int height) {
            super(width, height);
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
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        public LayoutParams(final ViewGroup.LayoutParams source) {
            super(source);
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
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.constraintTag = null;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.isVirtualGroup = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        @TargetApi(17)
        public void resolveLayoutDirection(final int layoutDirection) {
            final int preLeftMargin = this.leftMargin;
            final int preRightMargin = this.rightMargin;
            if (Build.VERSION.SDK_INT >= 17) {
                super.resolveLayoutDirection(layoutDirection);
            }
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            this.resolvedGuideBegin = this.guideBegin;
            this.resolvedGuideEnd = this.guideEnd;
            this.resolvedGuidePercent = this.guidePercent;
            final boolean isRtl = 1 == this.getLayoutDirection();
            if (isRtl) {
                boolean startEndDefined = false;
                if (this.startToEnd != -1) {
                    this.resolvedRightToLeft = this.startToEnd;
                    startEndDefined = true;
                }
                else if (this.startToStart != -1) {
                    this.resolvedRightToRight = this.startToStart;
                    startEndDefined = true;
                }
                if (this.endToStart != -1) {
                    this.resolvedLeftToRight = this.endToStart;
                    startEndDefined = true;
                }
                if (this.endToEnd != -1) {
                    this.resolvedLeftToLeft = this.endToEnd;
                    startEndDefined = true;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneRightMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneEndMargin;
                }
                if (startEndDefined) {
                    this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
                }
                if (this.isGuideline && this.orientation == 1) {
                    if (this.guidePercent != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - this.guidePercent;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    }
                    else if (this.guideBegin != -1) {
                        this.resolvedGuideEnd = this.guideBegin;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                    else if (this.guideEnd != -1) {
                        this.resolvedGuideBegin = this.guideEnd;
                        this.resolvedGuideEnd = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                }
            }
            else {
                if (this.startToEnd != -1) {
                    this.resolvedLeftToRight = this.startToEnd;
                }
                if (this.startToStart != -1) {
                    this.resolvedLeftToLeft = this.startToStart;
                }
                if (this.endToStart != -1) {
                    this.resolvedRightToLeft = this.endToStart;
                }
                if (this.endToEnd != -1) {
                    this.resolvedRightToRight = this.endToEnd;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneRightMargin = this.goneEndMargin;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                if (this.rightToLeft != -1) {
                    this.resolvedRightToLeft = this.rightToLeft;
                    if (this.rightMargin <= 0 && preRightMargin > 0) {
                        this.rightMargin = preRightMargin;
                    }
                }
                else if (this.rightToRight != -1) {
                    this.resolvedRightToRight = this.rightToRight;
                    if (this.rightMargin <= 0 && preRightMargin > 0) {
                        this.rightMargin = preRightMargin;
                    }
                }
                if (this.leftToLeft != -1) {
                    this.resolvedLeftToLeft = this.leftToLeft;
                    if (this.leftMargin <= 0 && preLeftMargin > 0) {
                        this.leftMargin = preLeftMargin;
                    }
                }
                else if (this.leftToRight != -1) {
                    this.resolvedLeftToRight = this.leftToRight;
                    if (this.leftMargin <= 0 && preLeftMargin > 0) {
                        this.leftMargin = preLeftMargin;
                    }
                }
            }
        }
        
        public String getConstraintTag() {
            return this.constraintTag;
        }
        
        private static class Table
        {
            public static final int UNUSED = 0;
            public static final int ANDROID_ORIENTATION = 1;
            public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
            public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
            public static final int LAYOUT_GONE_MARGIN_TOP = 22;
            public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
            public static final int LAYOUT_GONE_MARGIN_START = 25;
            public static final int LAYOUT_GONE_MARGIN_END = 26;
            public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
            public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
            public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
            public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
            public static final int LAYOUT_CONSTRAINT_TAG = 51;
            public static final SparseIntArray map;
            
            static {
                (map = new SparseIntArray()).append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                Table.map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
            }
        }
    }
}
