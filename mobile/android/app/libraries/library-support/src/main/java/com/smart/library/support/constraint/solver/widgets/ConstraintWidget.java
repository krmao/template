package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.widgets.Barrier;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;
import com.smart.library.support.constraint.solver.widgets.analyzer.*;
import com.smart.library.support.constraint.solver.*;
import java.util.*;

public class ConstraintWidget
{
    private static final boolean AUTOTAG_CENTER = false;
    protected static final int SOLVER = 1;
    protected static final int DIRECT = 2;
    public boolean measured;
    public WidgetRun[] run;
    public ChainRun horizontalChainRun;
    public ChainRun verticalChainRun;
    public HorizontalWidgetRun horizontalRun;
    public VerticalWidgetRun verticalRun;
    public boolean[] isTerminalWidget;
    public int[] wrapMeasure;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int UNKNOWN = -1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    public static final int INVISIBLE = 4;
    public static final int GONE = 8;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static final int CHAIN_PACKED = 2;
    public int mHorizontalResolution;
    public int mVerticalResolution;
    private static final int WRAP = -2;
    public int mMatchConstraintDefaultWidth;
    public int mMatchConstraintDefaultHeight;
    public int[] mResolvedMatchConstraintDefault;
    public int mMatchConstraintMinWidth;
    public int mMatchConstraintMaxWidth;
    public float mMatchConstraintPercentWidth;
    public int mMatchConstraintMinHeight;
    public int mMatchConstraintMaxHeight;
    public float mMatchConstraintPercentHeight;
    public boolean mIsWidthWrapContent;
    public boolean mIsHeightWrapContent;
    int mResolvedDimensionRatioSide;
    float mResolvedDimensionRatio;
    private int[] mMaxDimension;
    private float mCircleConstraintAngle;
    private boolean hasBaseline;
    private boolean inPlaceholder;
    private boolean mInVirtuaLayout;
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mLeft;
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mTop;
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mRight;
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mBottom;
    com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mBaseline;
    com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mCenterX;
    com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mCenterY;
    com.smart.library.support.constraint.solver.widgets.ConstraintAnchor mCenter;
    public static final int ANCHOR_LEFT = 0;
    public static final int ANCHOR_RIGHT = 1;
    public static final int ANCHOR_TOP = 2;
    public static final int ANCHOR_BOTTOM = 3;
    public static final int ANCHOR_BASELINE = 4;
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor[] mListAnchors;
    protected ArrayList<com.smart.library.support.constraint.solver.widgets.ConstraintAnchor> mAnchors;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    public DimensionBehaviour[] mListDimensionBehaviors;
    public ConstraintWidget mParent;
    int mWidth;
    int mHeight;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    protected int mX;
    protected int mY;
    int mRelX;
    int mRelY;
    protected int mOffsetX;
    protected int mOffsetY;
    int mBaselineDistance;
    protected int mMinWidth;
    protected int mMinHeight;
    public static float DEFAULT_BIAS;
    float mHorizontalBiasPercent;
    float mVerticalBiasPercent;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private int mVisibility;
    private String mDebugName;
    private String mType;
    int mDistToTop;
    int mDistToLeft;
    int mDistToRight;
    int mDistToBottom;
    boolean mLeftHasCentered;
    boolean mRightHasCentered;
    boolean mTopHasCentered;
    boolean mBottomHasCentered;
    boolean mHorizontalWrapVisited;
    boolean mVerticalWrapVisited;
    boolean mOptimizerMeasurable;
    boolean mGroupsToSolver;
    int mHorizontalChainStyle;
    int mVerticalChainStyle;
    boolean mHorizontalChainFixedPosition;
    boolean mVerticalChainFixedPosition;
    public float[] mWeight;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    protected ConstraintWidget[] mNextChainWidget;
    ConstraintWidget mHorizontalNextWidget;
    ConstraintWidget mVerticalNextWidget;
    
    public WidgetRun getRun(final int orientation) {
        if (orientation == 0) {
            return this.horizontalRun;
        }
        if (orientation == 1) {
            return this.verticalRun;
        }
        return null;
    }
    
    public boolean isInVirtualLayout() {
        return this.mInVirtuaLayout;
    }
    
    public void setInVirtualLayout(final boolean inVirtualLayout) {
        this.mInVirtuaLayout = inVirtualLayout;
    }
    
    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }
    
    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }
    
    public void setMaxWidth(final int maxWidth) {
        this.mMaxDimension[0] = maxWidth;
    }
    
    public void setMaxHeight(final int maxHeight) {
        this.mMaxDimension[1] = maxHeight;
    }
    
    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }
    
    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }
    
    public void setHasBaseline(final boolean hasBaseline) {
        this.hasBaseline = hasBaseline;
    }
    
    public boolean getHasBaseline() {
        return this.hasBaseline;
    }
    
    public boolean isInPlaceholder() {
        return this.inPlaceholder;
    }
    
    public void setInPlaceholder(final boolean inPlaceholder) {
        this.inPlaceholder = inPlaceholder;
    }
    
    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mHorizontalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mVerticalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mWeight[0] = -1.0f;
        this.mWeight[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMaxDimension[0] = Integer.MAX_VALUE;
        this.mMaxDimension[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        this.isTerminalWidget[0] = true;
        this.isTerminalWidget[1] = true;
        this.mInVirtuaLayout = false;
    }
    
    public ConstraintWidget() {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = new HorizontalWidgetRun(this);
        this.verticalRun = new VerticalWidgetRun(this);
        this.isTerminalWidget = new boolean[] { true, true };
        this.wrapMeasure = new int[] { 0, 0 };
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLeft = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
        this.mTop = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
        this.mRight = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
        this.mBottom = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
        this.mAnchors = new ArrayList<com.smart.library.support.constraint.solver.widgets.ConstraintAnchor>();
        this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mVerticalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[] { -1.0f, -1.0f };
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
        this.mNextChainWidget = new ConstraintWidget[] { null, null };
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }
    
    public ConstraintWidget(final int x, final int y, final int width, final int height) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = new HorizontalWidgetRun(this);
        this.verticalRun = new VerticalWidgetRun(this);
        this.isTerminalWidget = new boolean[] { true, true };
        this.wrapMeasure = new int[] { 0, 0 };
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLeft = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
        this.mTop = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
        this.mRight = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
        this.mBottom = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor(this, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new com.smart.library.support.constraint.solver.widgets.ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
        this.mAnchors = new ArrayList<com.smart.library.support.constraint.solver.widgets.ConstraintAnchor>();
        this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mVerticalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[] { -1.0f, -1.0f };
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
        this.mNextChainWidget = new ConstraintWidget[] { null, null };
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
        this.addAnchors();
    }
    
    public ConstraintWidget(final int width, final int height) {
        this(0, 0, width, height);
    }
    
    public void resetSolverVariables(final Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }
    
    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }
    
    public boolean isRoot() {
        return this.mParent == null;
    }
    
    public ConstraintWidget getParent() {
        return this.mParent;
    }
    
    public void setParent(final ConstraintWidget widget) {
        this.mParent = widget;
    }
    
    public void setWidthWrapContent(final boolean widthWrapContent) {
        this.mIsWidthWrapContent = widthWrapContent;
    }
    
    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }
    
    public void setHeightWrapContent(final boolean heightWrapContent) {
        this.mIsHeightWrapContent = heightWrapContent;
    }
    
    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }
    
    public void connectCircularConstraint(final ConstraintWidget target, final float angle, final int radius) {
        this.immediateConnect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER, target, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER, radius, 0);
        this.mCircleConstraintAngle = angle;
    }
    
    public String getType() {
        return this.mType;
    }
    
    public void setType(final String type) {
        this.mType = type;
    }
    
    public void setVisibility(final int visibility) {
        this.mVisibility = visibility;
    }
    
    public int getVisibility() {
        return this.mVisibility;
    }
    
    public String getDebugName() {
        return this.mDebugName;
    }
    
    public void setDebugName(final String name) {
        this.mDebugName = name;
    }
    
    public void setDebugSolverName(final LinearSystem system, final String name) {
        this.mDebugName = name;
        final SolverVariable left = system.createObjectVariable(this.mLeft);
        final SolverVariable top = system.createObjectVariable(this.mTop);
        final SolverVariable right = system.createObjectVariable(this.mRight);
        final SolverVariable bottom = system.createObjectVariable(this.mBottom);
        left.setName(name + ".left");
        top.setName(name + ".top");
        right.setName(name + ".right");
        bottom.setName(name + ".bottom");
        if (this.mBaselineDistance > 0) {
            final SolverVariable baseline = system.createObjectVariable(this.mBaseline);
            baseline.setName(name + ".baseline");
        }
    }
    
    public void createObjectVariables(final LinearSystem system) {
        final SolverVariable left = system.createObjectVariable(this.mLeft);
        final SolverVariable top = system.createObjectVariable(this.mTop);
        final SolverVariable right = system.createObjectVariable(this.mRight);
        final SolverVariable bottom = system.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            system.createObjectVariable(this.mBaseline);
        }
    }
    
    @Override
    public String toString() {
        return ((this.mType != null) ? ("type: " + this.mType + " ") : "") + ((this.mDebugName != null) ? ("id: " + this.mDebugName + " ") : "") + "(" + this.mX + ", " + this.mY + ") - (" + this.mWidth + " x " + this.mHeight + ")";
    }
    
    public int getX() {
        if (this.mParent != null && this.mParent instanceof ConstraintWidgetContainer) {
            return ((ConstraintWidgetContainer)this.mParent).mPaddingLeft + this.mX;
        }
        return this.mX;
    }
    
    public int getY() {
        if (this.mParent != null && this.mParent instanceof ConstraintWidgetContainer) {
            return ((ConstraintWidgetContainer)this.mParent).mPaddingTop + this.mY;
        }
        return this.mY;
    }
    
    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }
    
    public int getOptimizerWrapWidth() {
        int w = this.mWidth;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultWidth == 1) {
                w = Math.max(this.mMatchConstraintMinWidth, w);
            }
            else if (this.mMatchConstraintMinWidth > 0) {
                w = this.mMatchConstraintMinWidth;
                this.mWidth = w;
            }
            else {
                w = 0;
            }
            if (this.mMatchConstraintMaxWidth > 0 && this.mMatchConstraintMaxWidth < w) {
                w = this.mMatchConstraintMaxWidth;
            }
        }
        return w;
    }
    
    public int getOptimizerWrapHeight() {
        int h = this.mHeight;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultHeight == 1) {
                h = Math.max(this.mMatchConstraintMinHeight, h);
            }
            else if (this.mMatchConstraintMinHeight > 0) {
                h = this.mMatchConstraintMinHeight;
                this.mHeight = h;
            }
            else {
                h = 0;
            }
            if (this.mMatchConstraintMaxHeight > 0 && this.mMatchConstraintMaxHeight < h) {
                h = this.mMatchConstraintMaxHeight;
            }
        }
        return h;
    }
    
    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }
    
    public int getLength(final int orientation) {
        if (orientation == 0) {
            return this.getWidth();
        }
        if (orientation == 1) {
            return this.getHeight();
        }
        return 0;
    }
    
    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }
    
    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getLeft() {
        return this.getX();
    }
    
    public int getTop() {
        return this.getY();
    }
    
    public int getRight() {
        return this.getX() + this.mWidth;
    }
    
    public int getBottom() {
        return this.getY() + this.mHeight;
    }
    
    public int getHorizontalMargin() {
        int margin = 0;
        if (this.mLeft != null) {
            margin += this.mLeft.mMargin;
        }
        if (this.mRight != null) {
            margin += this.mRight.mMargin;
        }
        return margin;
    }
    
    public int getVerticalMargin() {
        int margin = 0;
        if (this.mLeft != null) {
            margin += this.mTop.mMargin;
        }
        if (this.mRight != null) {
            margin += this.mBottom.mMargin;
        }
        return margin;
    }
    
    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }
    
    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }
    
    public float getBiasPercent(final int orientation) {
        if (orientation == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (orientation == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }
    
    public boolean hasBaseline() {
        return this.hasBaseline;
    }
    
    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }
    
    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }
    
    public ArrayList<com.smart.library.support.constraint.solver.widgets.ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public void setX(final int x) {
        this.mX = x;
    }
    
    public void setY(final int y) {
        this.mY = y;
    }
    
    public void setOrigin(final int x, final int y) {
        this.mX = x;
        this.mY = y;
    }
    
    public void setOffset(final int x, final int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }
    
    public void setGoneMargin(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type type, final int goneMargin) {
        switch (type) {
            case LEFT: {
                this.mLeft.mGoneMargin = goneMargin;
                break;
            }
            case TOP: {
                this.mTop.mGoneMargin = goneMargin;
                break;
            }
            case RIGHT: {
                this.mRight.mGoneMargin = goneMargin;
                break;
            }
            case BOTTOM: {
                this.mBottom.mGoneMargin = goneMargin;
                break;
            }
        }
    }
    
    public void setWidth(final int w) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setHeight(final int h) {
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    public void setLength(final int length, final int orientation) {
        if (orientation == 0) {
            this.setWidth(length);
        }
        else if (orientation == 1) {
            this.setHeight(length);
        }
    }
    
    public void setHorizontalMatchStyle(final int horizontalMatchStyle, final int min, final int max, final float percent) {
        this.mMatchConstraintDefaultWidth = horizontalMatchStyle;
        this.mMatchConstraintMinWidth = min;
        this.mMatchConstraintMaxWidth = max;
        this.mMatchConstraintPercentWidth = percent;
        if (percent < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }
    
    public void setVerticalMatchStyle(final int verticalMatchStyle, final int min, final int max, final float percent) {
        this.mMatchConstraintDefaultHeight = verticalMatchStyle;
        this.mMatchConstraintMinHeight = min;
        this.mMatchConstraintMaxHeight = max;
        this.mMatchConstraintPercentHeight = percent;
        if (percent < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }
    
    public void setDimensionRatio(final String ratio) {
        if (ratio == null || ratio.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int dimensionRatioSide = -1;
        float dimensionRatio = 0.0f;
        final int len = ratio.length();
        int commaIndex = ratio.indexOf(44);
        if (commaIndex > 0 && commaIndex < len - 1) {
            final String dimension = ratio.substring(0, commaIndex);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
            }
            else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
            }
            ++commaIndex;
        }
        else {
            commaIndex = 0;
        }
        final int colonIndex = ratio.indexOf(58);
        if (colonIndex >= 0 && colonIndex < len - 1) {
            final String nominator = ratio.substring(commaIndex, colonIndex);
            final String denominator = ratio.substring(colonIndex + 1);
            if (nominator.length() > 0 && denominator.length() > 0) {
                try {
                    final float nominatorValue = Float.parseFloat(nominator);
                    final float denominatorValue = Float.parseFloat(denominator);
                    if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                        if (dimensionRatioSide == 1) {
                            dimensionRatio = Math.abs(denominatorValue / nominatorValue);
                        }
                        else {
                            dimensionRatio = Math.abs(nominatorValue / denominatorValue);
                        }
                    }
                }
                catch (NumberFormatException ex) {}
            }
        }
        else {
            final String r = ratio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    dimensionRatio = Float.parseFloat(r);
                }
                catch (NumberFormatException ex2) {}
            }
        }
        if (dimensionRatio > 0.0f) {
            this.mDimensionRatio = dimensionRatio;
            this.mDimensionRatioSide = dimensionRatioSide;
        }
    }
    
    public void setDimensionRatio(final float ratio, final int dimensionRatioSide) {
        this.mDimensionRatio = ratio;
        this.mDimensionRatioSide = dimensionRatioSide;
    }
    
    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }
    
    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }
    
    public void setHorizontalBiasPercent(final float horizontalBiasPercent) {
        this.mHorizontalBiasPercent = horizontalBiasPercent;
    }
    
    public void setVerticalBiasPercent(final float verticalBiasPercent) {
        this.mVerticalBiasPercent = verticalBiasPercent;
    }
    
    public void setMinWidth(final int w) {
        if (w < 0) {
            this.mMinWidth = 0;
        }
        else {
            this.mMinWidth = w;
        }
    }
    
    public void setMinHeight(final int h) {
        if (h < 0) {
            this.mMinHeight = 0;
        }
        else {
            this.mMinHeight = h;
        }
    }
    
    public void setDimension(final int w, final int h) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    public void setFrame(final int left, final int top, final int right, final int bottom) {
        int w = right - left;
        int h = bottom - top;
        this.mX = left;
        this.mY = top;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && w < this.mWidth) {
            w = this.mWidth;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && h < this.mHeight) {
            h = this.mHeight;
        }
        this.mWidth = w;
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setFrame(final int start, final int end, final int orientation) {
        if (orientation == 0) {
            this.setHorizontalDimension(start, end);
        }
        else if (orientation == 1) {
            this.setVerticalDimension(start, end);
        }
    }
    
    public void setHorizontalDimension(final int left, final int right) {
        this.mX = left;
        this.mWidth = right - left;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setVerticalDimension(final int top, final int bottom) {
        this.mY = top;
        this.mHeight = bottom - top;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    int getRelativePositioning(final int orientation) {
        if (orientation == 0) {
            return this.mRelX;
        }
        if (orientation == 1) {
            return this.mRelY;
        }
        return 0;
    }
    
    void setRelativePositioning(final int offset, final int orientation) {
        if (orientation == 0) {
            this.mRelX = offset;
        }
        else if (orientation == 1) {
            this.mRelY = offset;
        }
    }
    
    public void setBaselineDistance(final int baseline) {
        this.mBaselineDistance = baseline;
    }
    
    public void setCompanionWidget(final Object companion) {
        this.mCompanionWidget = companion;
    }
    
    public void setContainerItemSkip(final int skip) {
        if (skip >= 0) {
            this.mContainerItemSkip = skip;
        }
        else {
            this.mContainerItemSkip = 0;
        }
    }
    
    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }
    
    public void setHorizontalWeight(final float horizontalWeight) {
        this.mWeight[0] = horizontalWeight;
    }
    
    public void setVerticalWeight(final float verticalWeight) {
        this.mWeight[1] = verticalWeight;
    }
    
    public void setHorizontalChainStyle(final int horizontalChainStyle) {
        this.mHorizontalChainStyle = horizontalChainStyle;
    }
    
    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }
    
    public void setVerticalChainStyle(final int verticalChainStyle) {
        this.mVerticalChainStyle = verticalChainStyle;
    }
    
    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }
    
    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }
    
    public void immediateConnect(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type startType, final ConstraintWidget target, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type endType, final int margin, final int goneMargin) {
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor startAnchor = this.getAnchor(startType);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor endAnchor = target.getAnchor(endType);
        startAnchor.connect(endAnchor, margin, goneMargin, true);
    }
    
    public void connect(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor from, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor to, final int margin) {
        if (from.getOwner() == this) {
            this.connect(from.getType(), to.getOwner(), to.getType(), margin);
        }
    }
    
    public void connect(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type constraintFrom, final ConstraintWidget target, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type constraintTo) {
        this.connect(constraintFrom, target, constraintTo, 0);
    }
    
    public void connect(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type constraintFrom, final ConstraintWidget target, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type constraintTo, int margin) {
        if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER) {
            if (constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER) {
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor left = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor right = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor top = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottom = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
                boolean centerX = false;
                boolean centerY = false;
                if (left == null || !left.isConnected()) {
                    if (right == null || !right.isConnected()) {
                        this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT, target, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT, 0);
                        this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT, target, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT, 0);
                        centerX = true;
                    }
                }
                if (top == null || !top.isConnected()) {
                    if (bottom == null || !bottom.isConnected()) {
                        this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP, target, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP, 0);
                        this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM, target, com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM, 0);
                        centerY = true;
                    }
                }
                if (centerX && centerY) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
                    center.connect(target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER), 0);
                }
                else if (centerX) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
                    center.connect(target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X), 0);
                }
                else if (centerY) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
                    center.connect(target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y), 0);
                }
            }
            else if (constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT || constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT) {
                this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT, target, constraintTo, 0);
                this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT, target, constraintTo, 0);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
                center2.connect(target.getAnchor(constraintTo), 0);
            }
            else if (constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP || constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM) {
                this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP, target, constraintTo, 0);
                this.connect(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM, target, constraintTo, 0);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
                center2.connect(target.getAnchor(constraintTo), 0);
            }
        }
        else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X && (constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT || constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT)) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor left = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetAnchor = target.getAnchor(constraintTo);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor right2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
            left.connect(targetAnchor, 0);
            right2.connect(targetAnchor, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerX2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
            centerX2.connect(targetAnchor, 0);
        }
        else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y && (constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP || constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM)) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetAnchor2 = target.getAnchor(constraintTo);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor top2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
            top2.connect(targetAnchor2, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottom2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
            bottom2.connect(targetAnchor2, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerY2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
            centerY2.connect(targetAnchor2, 0);
        }
        else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X && constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor left = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor leftTarget = target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
            left.connect(leftTarget, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor right2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor rightTarget = target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
            right2.connect(rightTarget, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerX3 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
            centerX3.connect(target.getAnchor(constraintTo), 0);
        }
        else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y && constraintTo == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor top3 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor topTarget = target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
            top3.connect(topTarget, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottom2 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottomTarget = target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
            bottom2.connect(bottomTarget, 0);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerY3 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
            centerY3.connect(target.getAnchor(constraintTo), 0);
        }
        else {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor fromAnchor = this.getAnchor(constraintFrom);
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor toAnchor = target.getAnchor(constraintTo);
            if (fromAnchor.isValidConnection(toAnchor)) {
                if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor top = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottom = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
                    if (top != null) {
                        top.reset();
                    }
                    if (bottom != null) {
                        bottom.reset();
                    }
                    margin = 0;
                }
                else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP || constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor baseline = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE);
                    if (baseline != null) {
                        baseline.reset();
                    }
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center3 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
                    if (center3.getTarget() != toAnchor) {
                        center3.reset();
                    }
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor opposite = this.getAnchor(constraintFrom).getOpposite();
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerY4 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
                    if (centerY4.isConnected()) {
                        opposite.reset();
                        centerY4.reset();
                    }
                }
                else if (constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT || constraintFrom == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center4 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
                    if (center4.getTarget() != toAnchor) {
                        center4.reset();
                    }
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor opposite2 = this.getAnchor(constraintFrom).getOpposite();
                    final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerX3 = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
                    if (centerX3.isConnected()) {
                        opposite2.reset();
                        centerX3.reset();
                    }
                }
                fromAnchor.connect(toAnchor, margin);
            }
        }
    }
    
    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
    }
    
    public void resetAnchor(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor anchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer) {
            final ConstraintWidgetContainer parent = (ConstraintWidgetContainer)this.getParent();
            if (parent.handlesInternalConstraints()) {
                return;
            }
        }
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor left = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor right = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor top = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor bottom = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor center = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerX = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X);
        final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor centerY = this.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y);
        if (anchor == center) {
            if (left.isConnected() && right.isConnected() && left.getTarget() == right.getTarget()) {
                left.reset();
                right.reset();
            }
            if (top.isConnected() && bottom.isConnected() && top.getTarget() == bottom.getTarget()) {
                top.reset();
                bottom.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (anchor == centerX) {
            if (left.isConnected() && right.isConnected() && left.getTarget().getOwner() == right.getTarget().getOwner()) {
                left.reset();
                right.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        }
        else if (anchor == centerY) {
            if (top.isConnected() && bottom.isConnected() && top.getTarget().getOwner() == bottom.getTarget().getOwner()) {
                top.reset();
                bottom.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (anchor == left || anchor == right) {
            if (left.isConnected() && left.getTarget() == right.getTarget()) {
                center.reset();
            }
        }
        else if ((anchor == top || anchor == bottom) && top.isConnected() && top.getTarget() == bottom.getTarget()) {
            center.reset();
        }
        anchor.reset();
    }
    
    public void resetAnchors() {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer) {
            final ConstraintWidgetContainer parentContainer = (ConstraintWidgetContainer)this.getParent();
            if (parentContainer.handlesInternalConstraints()) {
                return;
            }
        }
        for (int i = 0, mAnchorsSize = this.mAnchors.size(); i < mAnchorsSize; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor anchor = this.mAnchors.get(i);
            anchor.reset();
        }
    }
    
    public com.smart.library.support.constraint.solver.widgets.ConstraintAnchor getAnchor(final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type anchorType) {
        switch (anchorType) {
            case LEFT: {
                return this.mLeft;
            }
            case TOP: {
                return this.mTop;
            }
            case RIGHT: {
                return this.mRight;
            }
            case BOTTOM: {
                return this.mBottom;
            }
            case BASELINE: {
                return this.mBaseline;
            }
            case CENTER_X: {
                return this.mCenterX;
            }
            case CENTER_Y: {
                return this.mCenterY;
            }
            case CENTER: {
                return this.mCenter;
            }
            case NONE: {
                return null;
            }
            default: {
                throw new AssertionError((Object)anchorType.name());
            }
        }
    }
    
    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }
    
    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }
    
    public DimensionBehaviour getDimensionBehaviour(final int orientation) {
        if (orientation == 0) {
            return this.getHorizontalDimensionBehaviour();
        }
        if (orientation == 1) {
            return this.getVerticalDimensionBehaviour();
        }
        return null;
    }
    
    public void setHorizontalDimensionBehaviour(final DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[0] = behaviour;
    }
    
    public void setVerticalDimensionBehaviour(final DimensionBehaviour behaviour) {
        this.mListDimensionBehaviors[1] = behaviour;
    }
    
    public boolean isInHorizontalChain() {
        return (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
    }
    
    public ConstraintWidget getPreviousChainMember(final int orientation) {
        if (orientation == 0) {
            if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) {
                return this.mLeft.mTarget.mOwner;
            }
        }
        else if (orientation == 1 && this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) {
            return this.mTop.mTarget.mOwner;
        }
        return null;
    }
    
    public ConstraintWidget getNextChainMember(final int orientation) {
        if (orientation == 0) {
            if (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
                return this.mRight.mTarget.mOwner;
            }
        }
        else if (orientation == 1 && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
            return this.mBottom.mTarget.mOwner;
        }
        return null;
    }
    
    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintWidget found = null;
        if (this.isInHorizontalChain()) {
            ConstraintWidget tmp = this;
            while (found == null && tmp != null) {
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor anchor = tmp.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetOwner = (anchor == null) ? null : anchor.getTarget();
                final ConstraintWidget target = (targetOwner == null) ? null : targetOwner.getOwner();
                if (target == this.getParent()) {
                    found = tmp;
                    break;
                }
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetAnchor = (target == null) ? null : target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT).getTarget();
                if (targetAnchor != null && targetAnchor.getOwner() != tmp) {
                    found = tmp;
                }
                else {
                    tmp = target;
                }
            }
        }
        return found;
    }
    
    public boolean isInVerticalChain() {
        return (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
    }
    
    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintWidget found = null;
        if (this.isInVerticalChain()) {
            ConstraintWidget tmp = this;
            while (found == null && tmp != null) {
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor anchor = tmp.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP);
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetOwner = (anchor == null) ? null : anchor.getTarget();
                final ConstraintWidget target = (targetOwner == null) ? null : targetOwner.getOwner();
                if (target == this.getParent()) {
                    found = tmp;
                    break;
                }
                final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor targetAnchor = (target == null) ? null : target.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM).getTarget();
                if (targetAnchor != null && targetAnchor.getOwner() != tmp) {
                    found = tmp;
                }
                else {
                    tmp = target;
                }
            }
        }
        return found;
    }
    
    private boolean isChainHead(final int orientation) {
        final int offset = orientation * 2;
        return this.mListAnchors[offset].mTarget != null && this.mListAnchors[offset].mTarget.mTarget != this.mListAnchors[offset] && this.mListAnchors[offset + 1].mTarget != null && this.mListAnchors[offset + 1].mTarget.mTarget == this.mListAnchors[offset + 1];
    }
    
    public void addToSolver(final LinearSystem system) {
        final SolverVariable left = system.createObjectVariable(this.mLeft);
        final SolverVariable right = system.createObjectVariable(this.mRight);
        final SolverVariable top = system.createObjectVariable(this.mTop);
        final SolverVariable bottom = system.createObjectVariable(this.mBottom);
        final SolverVariable baseline = system.createObjectVariable(this.mBaseline);
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.widgets;
        }
        if (this.horizontalRun.start.resolved && this.horizontalRun.end.resolved && this.verticalRun.start.resolved && this.verticalRun.end.resolved) {
            if (LinearSystem.sMetrics != null) {
                final Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.graphSolved;
            }
            system.addEquality(left, this.horizontalRun.start.value);
            system.addEquality(right, this.horizontalRun.end.value);
            system.addEquality(top, this.verticalRun.start.value);
            system.addEquality(bottom, this.verticalRun.end.value);
            system.addEquality(baseline, this.verticalRun.baseline.value);
            if (this.mParent != null) {
                final boolean horizontalParentWrapContent = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
                final boolean verticalParentWrapContent = this.mParent != null && this.mParent.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
                if (horizontalParentWrapContent && this.isTerminalWidget[0] && !this.isInHorizontalChain()) {
                    final SolverVariable parentMax = system.createObjectVariable(this.mParent.mRight);
                    system.addGreaterThan(parentMax, right, 0, 6);
                }
                if (verticalParentWrapContent && this.isTerminalWidget[1] && !this.isInVerticalChain()) {
                    final SolverVariable parentMax = system.createObjectVariable(this.mParent.mBottom);
                    system.addGreaterThan(parentMax, bottom, 0, 6);
                }
            }
            return;
        }
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics3 = LinearSystem.sMetrics;
            ++sMetrics3.linearSolved;
        }
        boolean inHorizontalChain = false;
        boolean inVerticalChain = false;
        boolean horizontalParentWrapContent2 = false;
        boolean verticalParentWrapContent2 = false;
        if (this.mParent != null) {
            horizontalParentWrapContent2 = (this.mParent != null && this.mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT);
            verticalParentWrapContent2 = (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT);
            if (this.isChainHead(0)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                inHorizontalChain = true;
            }
            else {
                inHorizontalChain = this.isInHorizontalChain();
            }
            if (this.isChainHead(1)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                inVerticalChain = true;
            }
            else {
                inVerticalChain = this.isInVerticalChain();
            }
            if (!inHorizontalChain && horizontalParentWrapContent2 && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                final SolverVariable parentRight = system.createObjectVariable(this.mParent.mRight);
                system.addGreaterThan(parentRight, right, 0, 1);
            }
            if (!inVerticalChain && verticalParentWrapContent2 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                final SolverVariable parentBottom = system.createObjectVariable(this.mParent.mBottom);
                system.addGreaterThan(parentBottom, bottom, 0, 1);
            }
        }
        int width = this.mWidth;
        if (width < this.mMinWidth) {
            width = this.mMinWidth;
        }
        int height = this.mHeight;
        if (height < this.mMinHeight) {
            height = this.mMinHeight;
        }
        final boolean horizontalDimensionFixed = this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean verticalDimensionFixed = this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT;
        boolean useRatio = false;
        this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
        this.mResolvedDimensionRatio = this.mDimensionRatio;
        int matchConstraintDefaultWidth = this.mMatchConstraintDefaultWidth;
        int matchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
        if (this.mDimensionRatio > 0.0f && this.mVisibility != 8) {
            useRatio = true;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && matchConstraintDefaultWidth == 0) {
                matchConstraintDefaultWidth = 3;
            }
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && matchConstraintDefaultHeight == 0) {
                matchConstraintDefaultHeight = 3;
            }
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && matchConstraintDefaultWidth == 3 && matchConstraintDefaultHeight == 3) {
                this.setupDimensionRatio(horizontalParentWrapContent2, verticalParentWrapContent2, horizontalDimensionFixed, verticalDimensionFixed);
            }
            else if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && matchConstraintDefaultWidth == 3) {
                this.mResolvedDimensionRatioSide = 0;
                width = (int)(this.mResolvedDimensionRatio * this.mHeight);
                if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
                    matchConstraintDefaultWidth = 4;
                    useRatio = false;
                }
            }
            else if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && matchConstraintDefaultHeight == 3) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
                height = (int)(this.mResolvedDimensionRatio * this.mWidth);
                if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
                    matchConstraintDefaultHeight = 4;
                    useRatio = false;
                }
            }
        }
        this.mResolvedMatchConstraintDefault[0] = matchConstraintDefaultWidth;
        this.mResolvedMatchConstraintDefault[1] = matchConstraintDefaultHeight;
        final boolean useHorizontalRatio = useRatio && (this.mResolvedDimensionRatioSide == 0 || this.mResolvedDimensionRatioSide == -1);
        boolean wrapContent = this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        boolean applyPosition = true;
        if (this.mCenter.isConnected()) {
            applyPosition = false;
        }
        if (this.mHorizontalResolution != 2) {
            if (!this.horizontalRun.start.resolved || !this.horizontalRun.end.resolved) {
                final SolverVariable parentMax2 = (this.mParent != null) ? system.createObjectVariable(this.mParent.mRight) : null;
                final SolverVariable parentMin = (this.mParent != null) ? system.createObjectVariable(this.mParent.mLeft) : null;
                this.applyConstraints(system, horizontalParentWrapContent2, this.isTerminalWidget[0], parentMin, parentMax2, this.mListDimensionBehaviors[0], wrapContent, this.mLeft, this.mRight, this.mX, width, this.mMinWidth, this.mMaxDimension[0], this.mHorizontalBiasPercent, useHorizontalRatio, inHorizontalChain, matchConstraintDefaultWidth, matchConstraintDefaultHeight, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, applyPosition);
            }
            else {
                system.addEquality(left, this.horizontalRun.start.value);
                system.addEquality(right, this.horizontalRun.end.value);
                if (this.mParent != null && horizontalParentWrapContent2 && this.isTerminalWidget[0] && !this.isInHorizontalChain()) {
                    final SolverVariable parentMax2 = system.createObjectVariable(this.mParent.mRight);
                    system.addGreaterThan(parentMax2, right, 0, 6);
                }
            }
        }
        boolean applyVerticalConstraints = true;
        if (this.verticalRun.start.resolved && this.verticalRun.end.resolved) {
            system.addEquality(top, this.verticalRun.start.value);
            system.addEquality(bottom, this.verticalRun.end.value);
            system.addEquality(baseline, this.verticalRun.baseline.value);
            if (this.mParent != null && !inVerticalChain && verticalParentWrapContent2 && this.isTerminalWidget[1]) {
                final SolverVariable parentMax3 = system.createObjectVariable(this.mParent.mBottom);
                system.addGreaterThan(parentMax3, bottom, 0, 6);
            }
            applyVerticalConstraints = false;
        }
        if (this.mVerticalResolution == 2) {
            applyVerticalConstraints = false;
        }
        if (applyVerticalConstraints) {
            wrapContent = (this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer);
            final boolean useVerticalRatio = useRatio && (this.mResolvedDimensionRatioSide == 1 || this.mResolvedDimensionRatioSide == -1);
            if (this.mBaselineDistance > 0) {
                system.addEquality(baseline, top, this.getBaselineDistance(), 6);
                if (this.mBaseline.mTarget != null) {
                    final SolverVariable baselineTarget = system.createObjectVariable(this.mBaseline.mTarget);
                    final int baselineMargin = 0;
                    system.addEquality(baseline, baselineTarget, baselineMargin, 6);
                    applyPosition = false;
                }
            }
            else if (this.mVisibility == 8) {
                system.addEquality(baseline, top, 0, 6);
            }
            final SolverVariable parentMax4 = (this.mParent != null) ? system.createObjectVariable(this.mParent.mBottom) : null;
            final SolverVariable parentMin2 = (this.mParent != null) ? system.createObjectVariable(this.mParent.mTop) : null;
            this.applyConstraints(system, verticalParentWrapContent2, this.isTerminalWidget[1], parentMin2, parentMax4, this.mListDimensionBehaviors[1], wrapContent, this.mTop, this.mBottom, this.mY, height, this.mMinHeight, this.mMaxDimension[1], this.mVerticalBiasPercent, useVerticalRatio, inVerticalChain, matchConstraintDefaultHeight, matchConstraintDefaultWidth, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, applyPosition);
        }
        if (useRatio) {
            final int strength = 6;
            if (this.mResolvedDimensionRatioSide == 1) {
                system.addRatio(bottom, top, right, left, this.mResolvedDimensionRatio, strength);
            }
            else {
                system.addRatio(right, left, bottom, top, this.mResolvedDimensionRatio, strength);
            }
        }
        if (this.mCenter.isConnected()) {
            system.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
        }
    }
    
    public void setupDimensionRatio(final boolean hparentWrapContent, final boolean vparentWrapContent, final boolean horizontalDimensionFixed, final boolean verticalDimensionFixed) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (horizontalDimensionFixed && !verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!horizontalDimensionFixed && verticalDimensionFixed) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        }
        else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (hparentWrapContent && !vparentWrapContent) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!hparentWrapContent && vparentWrapContent) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && hparentWrapContent && vparentWrapContent) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }
    
    private void applyConstraints(final LinearSystem system, final boolean parentWrapContent, boolean isTerminal, final SolverVariable parentMin, final SolverVariable parentMax, final DimensionBehaviour dimensionBehaviour, final boolean wrapContent, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor beginAnchor, final com.smart.library.support.constraint.solver.widgets.ConstraintAnchor endAnchor, final int beginPosition, int dimension, final int minDimension, final int maxDimension, final float bias, final boolean useRatio, final boolean inChain, int matchConstraintDefault, final int oppositeMatchConstraintDefault, int matchMinDimension, int matchMaxDimension, final float matchPercentDimension, final boolean applyPosition) {
        final SolverVariable begin = system.createObjectVariable(beginAnchor);
        final SolverVariable end = system.createObjectVariable(endAnchor);
        final SolverVariable beginTarget = system.createObjectVariable(beginAnchor.getTarget());
        final SolverVariable endTarget = system.createObjectVariable(endAnchor.getTarget());
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics = LinearSystem.getMetrics();
            ++metrics.nonresolvedWidgets;
        }
        final boolean isBeginConnected = beginAnchor.isConnected();
        final boolean isEndConnected = endAnchor.isConnected();
        final boolean isCenterConnected = this.mCenter.isConnected();
        boolean variableSize = false;
        int numConnections = 0;
        if (isBeginConnected) {
            ++numConnections;
        }
        if (isEndConnected) {
            ++numConnections;
        }
        if (isCenterConnected) {
            ++numConnections;
        }
        if (useRatio) {
            matchConstraintDefault = 3;
        }
        switch (dimensionBehaviour) {
            case FIXED: {
                variableSize = false;
                break;
            }
            case WRAP_CONTENT: {
                variableSize = false;
                break;
            }
            case MATCH_PARENT: {
                variableSize = false;
                break;
            }
            case MATCH_CONSTRAINT: {
                variableSize = true;
                if (matchConstraintDefault == 4) {
                    variableSize = false;
                    break;
                }
                break;
            }
        }
        if (this.mVisibility == 8) {
            dimension = 0;
            variableSize = false;
        }
        if (applyPosition) {
            if (!isBeginConnected && !isEndConnected && !isCenterConnected) {
                system.addEquality(begin, beginPosition);
            }
            else if (isBeginConnected && !isEndConnected) {
                system.addEquality(begin, beginTarget, beginAnchor.getMargin(), 6);
            }
        }
        if (!variableSize) {
            if (wrapContent) {
                system.addEquality(end, begin, 0, 3);
                if (minDimension > 0) {
                    system.addGreaterThan(end, begin, minDimension, 6);
                }
                if (maxDimension < Integer.MAX_VALUE) {
                    system.addLowerThan(end, begin, maxDimension, 6);
                }
            }
            else {
                system.addEquality(end, begin, dimension, 6);
            }
        }
        else {
            if (matchMinDimension == -2) {
                matchMinDimension = dimension;
            }
            if (matchMaxDimension == -2) {
                matchMaxDimension = dimension;
            }
            if (matchMinDimension > 0) {
                boolean applyLimit = true;
                if (parentWrapContent && matchConstraintDefault == 1) {
                    applyLimit = false;
                }
                if (applyLimit) {
                    system.addGreaterThan(end, begin, matchMinDimension, 6);
                }
                dimension = Math.max(dimension, matchMinDimension);
            }
            if (matchMaxDimension > 0) {
                boolean applyLimit = true;
                if (parentWrapContent && matchConstraintDefault == 1) {
                    applyLimit = false;
                }
                if (applyLimit) {
                    system.addLowerThan(end, begin, matchMaxDimension, 6);
                }
                dimension = Math.min(dimension, matchMaxDimension);
            }
            if (matchConstraintDefault == 1) {
                if (parentWrapContent) {
                    system.addEquality(end, begin, dimension, 6);
                }
                else if (inChain) {
                    system.addEquality(end, begin, dimension, 4);
                }
                else {
                    system.addEquality(end, begin, dimension, 1);
                }
            }
            else if (matchConstraintDefault == 2) {
                SolverVariable percentBegin = null;
                SolverVariable percentEnd = null;
                if (beginAnchor.getType() == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP || beginAnchor.getType() == com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM) {
                    percentBegin = system.createObjectVariable(this.mParent.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP));
                    percentEnd = system.createObjectVariable(this.mParent.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM));
                }
                else {
                    percentBegin = system.createObjectVariable(this.mParent.getAnchor(com.smart.library.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT));
                    percentEnd = system.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                }
                system.addConstraint(system.createRow().createRowDimensionRatio(end, begin, percentEnd, percentBegin, matchPercentDimension));
                variableSize = false;
            }
            else {
                isTerminal = true;
            }
            if (variableSize && numConnections != 2 && !useRatio) {
                variableSize = false;
                int d = Math.max(matchMinDimension, dimension);
                if (matchMaxDimension > 0) {
                    d = Math.min(matchMaxDimension, d);
                }
                system.addEquality(end, begin, d, 6);
            }
        }
        if (!applyPosition || inChain) {
            if (numConnections < 2 && parentWrapContent && isTerminal) {
                system.addGreaterThan(begin, parentMin, 0, 6);
                system.addGreaterThan(parentMax, end, 0, 6);
            }
            return;
        }
        if (isBeginConnected || isEndConnected || isCenterConnected) {
            if (!isBeginConnected || isEndConnected) {
                if (!isBeginConnected && isEndConnected) {
                    system.addEquality(end, endTarget, -endAnchor.getMargin(), 6);
                    if (parentWrapContent) {
                        system.addGreaterThan(begin, parentMin, 0, 5);
                    }
                }
                else if (isBeginConnected && isEndConnected) {
                    boolean applyBoundsCheck = false;
                    boolean applyCentering = false;
                    int centeringStrength = 5;
                    if (variableSize) {
                        if (parentWrapContent && minDimension == 0) {
                            system.addGreaterThan(end, begin, 0, 6);
                        }
                        if (matchConstraintDefault == 0) {
                            int strength = 6;
                            if (matchMaxDimension > 0 || matchMinDimension > 0) {
                                strength = 4;
                                applyBoundsCheck = true;
                            }
                            system.addEquality(begin, beginTarget, beginAnchor.getMargin(), strength);
                            system.addEquality(end, endTarget, -endAnchor.getMargin(), strength);
                            if (matchMaxDimension > 0 || matchMinDimension > 0) {
                                applyCentering = true;
                            }
                        }
                        else if (matchConstraintDefault == 1) {
                            applyCentering = true;
                            applyBoundsCheck = true;
                            centeringStrength = 6;
                        }
                        else if (matchConstraintDefault == 3) {
                            applyCentering = true;
                            final boolean otherSideInvariable = oppositeMatchConstraintDefault == 2 || oppositeMatchConstraintDefault == 1;
                            if (!otherSideInvariable) {
                                applyBoundsCheck = true;
                                int strength2 = 4;
                                if (!useRatio && this.mResolvedDimensionRatioSide != -1 && matchMaxDimension <= 0) {
                                    strength2 = 6;
                                }
                                system.addEquality(begin, beginTarget, beginAnchor.getMargin(), strength2);
                                system.addEquality(end, endTarget, -endAnchor.getMargin(), strength2);
                            }
                        }
                    }
                    else {
                        applyCentering = true;
                    }
                    int startStrength = 5;
                    int endStrength = 5;
                    boolean applyStartConstraint = parentWrapContent;
                    boolean applyEndConstraint = parentWrapContent;
                    if (applyCentering) {
                        system.addCentering(begin, beginTarget, beginAnchor.getMargin(), bias, endTarget, end, endAnchor.getMargin(), centeringStrength);
                        final boolean isBeginAnchorBarrier = beginAnchor.mTarget.mOwner instanceof com.smart.library.support.constraint.solver.widgets.Barrier;
                        final boolean isEndAnchorBarrier = endAnchor.mTarget.mOwner instanceof Barrier;
                        if (isBeginAnchorBarrier && !isEndAnchorBarrier) {
                            endStrength = 6;
                            applyEndConstraint = true;
                        }
                        else if (!isBeginAnchorBarrier && isEndAnchorBarrier) {
                            startStrength = 6;
                            applyStartConstraint = true;
                        }
                    }
                    if (applyBoundsCheck) {
                        startStrength = 6;
                        endStrength = 6;
                    }
                    if ((!variableSize && applyStartConstraint) || applyBoundsCheck) {
                        system.addGreaterThan(begin, beginTarget, beginAnchor.getMargin(), startStrength);
                    }
                    if ((!variableSize && applyEndConstraint) || applyBoundsCheck) {
                        system.addLowerThan(end, endTarget, -endAnchor.getMargin(), endStrength);
                    }
                    if (parentWrapContent) {
                        system.addGreaterThan(begin, parentMin, 0, 6);
                    }
                }
            }
        }
        if (parentWrapContent && isTerminal) {
            system.addGreaterThan(parentMax, end, 0, 6);
        }
    }
    
    public void updateFromSolver(final LinearSystem system) {
        int left = system.getObjectVariableValue(this.mLeft);
        int top = system.getObjectVariableValue(this.mTop);
        int right = system.getObjectVariableValue(this.mRight);
        int bottom = system.getObjectVariableValue(this.mBottom);
        if (this.horizontalRun.start.resolved && this.horizontalRun.end.resolved) {
            left = this.horizontalRun.start.value;
            right = this.horizontalRun.end.value;
        }
        if (this.verticalRun.start.resolved && this.verticalRun.end.resolved) {
            top = this.verticalRun.start.value;
            bottom = this.verticalRun.end.value;
        }
        final int w = right - left;
        final int h = bottom - top;
        if (w < 0 || h < 0 || left == Integer.MIN_VALUE || left == Integer.MAX_VALUE || top == Integer.MIN_VALUE || top == Integer.MAX_VALUE || right == Integer.MIN_VALUE || right == Integer.MAX_VALUE || bottom == Integer.MIN_VALUE || bottom == Integer.MAX_VALUE) {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
        }
        this.setFrame(left, top, right, bottom);
    }
    
    public void copy(final ConstraintWidget src, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        this.mHorizontalResolution = src.mHorizontalResolution;
        this.mVerticalResolution = src.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = src.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = src.mMatchConstraintDefaultHeight;
        this.mResolvedMatchConstraintDefault[0] = src.mResolvedMatchConstraintDefault[0];
        this.mResolvedMatchConstraintDefault[1] = src.mResolvedMatchConstraintDefault[1];
        this.mMatchConstraintMinWidth = src.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = src.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = src.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = src.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = src.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = src.mIsWidthWrapContent;
        this.mIsHeightWrapContent = src.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = src.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = src.mResolvedDimensionRatio;
        this.mMaxDimension = Arrays.copyOf(src.mMaxDimension, src.mMaxDimension.length);
        this.mCircleConstraintAngle = src.mCircleConstraintAngle;
        this.hasBaseline = src.hasBaseline;
        this.inPlaceholder = src.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = Arrays.copyOf(this.mListDimensionBehaviors, 2);
        this.mParent = ((this.mParent == null) ? null : map.get(src.mParent));
        this.mWidth = src.mWidth;
        this.mHeight = src.mHeight;
        this.mDimensionRatio = src.mDimensionRatio;
        this.mDimensionRatioSide = src.mDimensionRatioSide;
        this.mX = src.mX;
        this.mY = src.mY;
        this.mRelX = src.mRelX;
        this.mRelY = src.mRelY;
        this.mOffsetX = src.mOffsetX;
        this.mOffsetY = src.mOffsetY;
        this.mBaselineDistance = src.mBaselineDistance;
        this.mMinWidth = src.mMinWidth;
        this.mMinHeight = src.mMinHeight;
        this.mHorizontalBiasPercent = src.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = src.mVerticalBiasPercent;
        this.mCompanionWidget = src.mCompanionWidget;
        this.mContainerItemSkip = src.mContainerItemSkip;
        this.mVisibility = src.mVisibility;
        this.mDebugName = src.mDebugName;
        this.mType = src.mType;
        this.mDistToTop = src.mDistToTop;
        this.mDistToLeft = src.mDistToLeft;
        this.mDistToRight = src.mDistToRight;
        this.mDistToBottom = src.mDistToBottom;
        this.mLeftHasCentered = src.mLeftHasCentered;
        this.mRightHasCentered = src.mRightHasCentered;
        this.mTopHasCentered = src.mTopHasCentered;
        this.mBottomHasCentered = src.mBottomHasCentered;
        this.mHorizontalWrapVisited = src.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = src.mVerticalWrapVisited;
        this.mOptimizerMeasurable = src.mOptimizerMeasurable;
        this.mGroupsToSolver = src.mGroupsToSolver;
        this.mHorizontalChainStyle = src.mHorizontalChainStyle;
        this.mVerticalChainStyle = src.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = src.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = src.mVerticalChainFixedPosition;
        this.mWeight[0] = src.mWeight[0];
        this.mWeight[1] = src.mWeight[1];
        this.mListNextMatchConstraintsWidget[0] = src.mListNextMatchConstraintsWidget[0];
        this.mListNextMatchConstraintsWidget[1] = src.mListNextMatchConstraintsWidget[1];
        this.mNextChainWidget[0] = src.mNextChainWidget[0];
        this.mNextChainWidget[1] = src.mNextChainWidget[1];
        this.mHorizontalNextWidget = ((src.mHorizontalNextWidget == null) ? null : map.get(src.mHorizontalNextWidget));
        this.mVerticalNextWidget = ((src.mVerticalNextWidget == null) ? null : map.get(src.mVerticalNextWidget));
    }
    
    public void updateFromRuns(boolean updateHorizontal, boolean updateVertical) {
        updateHorizontal &= this.horizontalRun.isResolved();
        updateVertical &= this.verticalRun.isResolved();
        int left = this.horizontalRun.start.value;
        int top = this.verticalRun.start.value;
        int right = this.horizontalRun.end.value;
        int bottom = this.verticalRun.end.value;
        int w = right - left;
        int h = bottom - top;
        if (w < 0 || h < 0 || left == Integer.MIN_VALUE || left == Integer.MAX_VALUE || top == Integer.MIN_VALUE || top == Integer.MAX_VALUE || right == Integer.MIN_VALUE || right == Integer.MAX_VALUE || bottom == Integer.MIN_VALUE || bottom == Integer.MAX_VALUE) {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
        }
        w = right - left;
        h = bottom - top;
        if (updateHorizontal) {
            this.mX = left;
        }
        if (updateVertical) {
            this.mY = top;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (updateHorizontal) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && w < this.mWidth) {
                w = this.mWidth;
            }
            this.mWidth = w;
            if (this.mWidth < this.mMinWidth) {
                this.mWidth = this.mMinWidth;
            }
        }
        if (updateVertical) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && h < this.mHeight) {
                h = this.mHeight;
            }
            this.mHeight = h;
            if (this.mHeight < this.mMinHeight) {
                this.mHeight = this.mMinHeight;
            }
        }
    }
    
    static {
        ConstraintWidget.DEFAULT_BIAS = 0.5f;
    }
    
    public enum DimensionBehaviour
    {
        FIXED, 
        WRAP_CONTENT, 
        MATCH_CONSTRAINT, 
        MATCH_PARENT;
    }
}
