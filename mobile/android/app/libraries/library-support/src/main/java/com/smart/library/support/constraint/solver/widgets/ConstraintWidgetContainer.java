package com.smart.library.support.constraint.solver.widgets;

import java.util.*;

import com.smart.library.support.constraint.solver.LinearSystem;
import com.smart.library.support.constraint.solver.Metrics;
import com.smart.library.support.constraint.solver.widgets.analyzer.BasicMeasure;
import com.smart.library.support.constraint.solver.widgets.analyzer.DependencyGraph;

public class ConstraintWidgetContainer extends WidgetContainer
{
    private static final int MAX_ITERATIONS = 8;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LAYOUT = false;
    static final boolean DEBUG_GRAPH = false;
    BasicMeasure mBasicMeasureSolver;
    public DependencyGraph mDependencyGraph;
    private BasicMeasure.Measurer mMeasurer;
    private boolean mIsRtl;
    protected LinearSystem mSystem;
    int mPaddingLeft;
    int mPaddingTop;
    int mPaddingRight;
    int mPaddingBottom;
    int mHorizontalChainsSize;
    int mVerticalChainsSize;
    com.smart.library.support.constraint.solver.widgets.ChainHead[] mVerticalChainsArray;
    com.smart.library.support.constraint.solver.widgets.ChainHead[] mHorizontalChainsArray;
    public boolean mGroupsWrapOptimized;
    public boolean mHorizontalWrapOptimized;
    public boolean mVerticalWrapOptimized;
    public int mWrapFixedWidth;
    public int mWrapFixedHeight;
    private int mOptimizationLevel;
    public boolean mSkipSolver;
    private boolean mWidthMeasuredTooSmall;
    private boolean mHeightMeasuredTooSmall;
    int mDebugSolverPassCount;
    
    public void invalidateGraph() {
        this.mDependencyGraph.invalidateGraph();
    }
    
    public void invalidateMeasures() {
        this.mDependencyGraph.invalidateMeasures();
    }
    
    public boolean directMeasure(final boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasure(optimizeWrap);
    }
    
    public boolean directMeasureSetup(final boolean optimizeWrap) {
        return this.mDependencyGraph.directMeasureSetup(optimizeWrap);
    }
    
    public boolean directMeasureWithOrientation(final boolean optimizeWrap, final int orientation) {
        return this.mDependencyGraph.directMeasureWithOrientation(optimizeWrap, orientation);
    }
    
    public void defineTerminalWidgets() {
        this.mDependencyGraph.defineTerminalWidgets(this.getHorizontalDimensionBehaviour(), this.getVerticalDimensionBehaviour());
    }
    
    public void measure(final int optimizationLevel, final int widthMode, final int widthSize, final int heightMode, final int heightSize, final int lastMeasureWidth, final int lastMeasureHeight, final int paddingX, final int paddingY) {
        this.mPaddingLeft = paddingX;
        this.mPaddingTop = paddingY;
        this.mBasicMeasureSolver.solverMeasure(this, optimizationLevel, paddingX, paddingY, widthMode, widthSize, heightMode, heightSize, lastMeasureWidth, lastMeasureHeight);
    }
    
    public void updateHierarchy() {
        this.mBasicMeasureSolver.updateHierarchy(this);
    }
    
    public void setMeasurer(final BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
        this.mDependencyGraph.setMeasurer(measurer);
    }
    
    public BasicMeasure.Measurer getMeasurer() {
        return this.mMeasurer;
    }
    
    public void fillMetrics(final Metrics metrics) {
        this.mSystem.fillMetrics(metrics);
    }
    
    public ConstraintWidgetContainer() {
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mHorizontalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    public ConstraintWidgetContainer(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mHorizontalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    public ConstraintWidgetContainer(final int width, final int height) {
        super(width, height);
        this.mBasicMeasureSolver = new BasicMeasure(this);
        this.mDependencyGraph = new DependencyGraph(this);
        this.mMeasurer = null;
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mHorizontalChainsArray = new com.smart.library.support.constraint.solver.widgets.ChainHead[4];
        this.mGroupsWrapOptimized = false;
        this.mHorizontalWrapOptimized = false;
        this.mVerticalWrapOptimized = false;
        this.mWrapFixedWidth = 0;
        this.mWrapFixedHeight = 0;
        this.mOptimizationLevel = 7;
        this.mSkipSolver = false;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    public void setOptimizationLevel(final int value) {
        this.mOptimizationLevel = value;
    }
    
    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }
    
    public boolean optimizeFor(final int feature) {
        return (this.mOptimizationLevel & feature) == feature;
    }
    
    @Override
    public String getType() {
        return "ConstraintLayout";
    }
    
    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mSkipSolver = false;
        super.reset();
    }
    
    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean addChildrenToSolver(final LinearSystem system) {
        this.addToSolver(system);
        final int count = this.mChildren.size();
        for (int i = 0; i < count; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof VirtualLayout) {
                widget.addToSolver(system);
            }
        }
        for (int i = 0; i < count; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour horizontalBehaviour = widget.mListDimensionBehaviors[0];
                final DimensionBehaviour verticalBehaviour = widget.mListDimensionBehaviors[1];
                if (horizontalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (verticalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                widget.addToSolver(system);
                if (horizontalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(horizontalBehaviour);
                }
                if (verticalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(verticalBehaviour);
                }
            }
            else {
                com.smart.library.support.constraint.solver.widgets.Optimizer.checkMatchParent(this, system, widget);
                if (!(widget instanceof VirtualLayout)) {
                    widget.addToSolver(system);
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            com.smart.library.support.constraint.solver.widgets.Chain.applyChainConstraints(this, system, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            com.smart.library.support.constraint.solver.widgets.Chain.applyChainConstraints(this, system, 1);
        }
        return true;
    }
    
    public void updateChildrenFromSolver(final LinearSystem system, final boolean[] flags) {
        flags[2] = false;
        this.updateFromSolver(system);
        for (int count = this.mChildren.size(), i = 0; i < count; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            widget.updateFromSolver(system);
        }
    }
    
    @Override
    public void updateFromRuns(final boolean updateHorizontal, final boolean updateVertical) {
        super.updateFromRuns(updateHorizontal, updateVertical);
        for (int count = this.mChildren.size(), i = 0; i < count; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            widget.updateFromRuns(updateHorizontal, updateVertical);
        }
    }
    
    public void setPadding(final int left, final int top, final int right, final int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }
    
    public void setRtl(final boolean isRtl) {
        this.mIsRtl = isRtl;
    }
    
    public boolean isRtl() {
        return this.mIsRtl;
    }
    
    @Override
    public void layout() {
        this.mX = 0;
        this.mY = 0;
        final int prew = Math.max(0, this.getWidth());
        final int preh = Math.max(0, this.getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        final boolean useGraphOptimizer = this.optimizeFor(64) || this.optimizeFor(128);
        this.mSystem.graphOptimizer = false;
        this.mSystem.newgraphOptimizer = false;
        if (this.mOptimizationLevel != 0 && useGraphOptimizer) {
            this.mSystem.newgraphOptimizer = true;
        }
        boolean wrap_override = false;
        final DimensionBehaviour originalVerticalDimensionBehaviour = this.mListDimensionBehaviors[1];
        final DimensionBehaviour originalHorizontalDimensionBehaviour = this.mListDimensionBehaviors[0];
        this.resetChains();
        int countSolve = 0;
        final List<com.smart.library.support.constraint.solver.widgets.ConstraintWidget> allChildren = this.mChildren;
        final boolean hasWrapContent = this.getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT || this.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT;
        this.resetChains();
        final int count = this.mChildren.size();
        countSolve = 0;
        for (int i = 0; i < count; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof com.smart.library.support.constraint.solver.widgets.WidgetContainer) {
                ((WidgetContainer)widget).layout();
            }
        }
        boolean needsSolving = true;
        while (needsSolving) {
            ++countSolve;
            try {
                this.mSystem.reset();
                this.resetChains();
                this.createObjectVariables(this.mSystem);
                for (int j = 0; j < count; ++j) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget2 = this.mChildren.get(j);
                    widget2.createObjectVariables(this.mSystem);
                }
                needsSolving = this.addChildrenToSolver(this.mSystem);
                if (needsSolving) {
                    this.mSystem.minimize();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("EXCEPTION : " + e);
            }
            if (needsSolving) {
                this.updateChildrenFromSolver(this.mSystem, com.smart.library.support.constraint.solver.widgets.Optimizer.flags);
            }
            else {
                this.updateFromSolver(this.mSystem);
                for (int j = 0; j < count; ++j) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintWidget constraintWidget = this.mChildren.get(j);
                }
            }
            needsSolving = false;
            if (hasWrapContent && countSolve < 8 && Optimizer.flags[2]) {
                int maxX = 0;
                int maxY = 0;
                for (int k = 0; k < count; ++k) {
                    final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget3 = this.mChildren.get(k);
                    maxX = Math.max(maxX, widget3.mX + widget3.getWidth());
                    maxY = Math.max(maxY, widget3.mY + widget3.getHeight());
                }
                maxX = Math.max(this.mMinWidth, maxX);
                maxY = Math.max(this.mMinHeight, maxY);
                if (originalHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this.getWidth() < maxX) {
                    this.setWidth(maxX);
                    this.mListDimensionBehaviors[0] = DimensionBehaviour.WRAP_CONTENT;
                    wrap_override = true;
                    needsSolving = true;
                }
                if (originalVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this.getHeight() < maxY) {
                    this.setHeight(maxY);
                    this.mListDimensionBehaviors[1] = DimensionBehaviour.WRAP_CONTENT;
                    wrap_override = true;
                    needsSolving = true;
                }
            }
            final int width = Math.max(this.mMinWidth, this.getWidth());
            if (width > this.getWidth()) {
                this.setWidth(width);
                this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                wrap_override = true;
                needsSolving = true;
            }
            final int height = Math.max(this.mMinHeight, this.getHeight());
            if (height > this.getHeight()) {
                this.setHeight(height);
                this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                wrap_override = true;
                needsSolving = true;
            }
            if (!wrap_override) {
                if (this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && prew > 0 && this.getWidth() > prew) {
                    this.mWidthMeasuredTooSmall = true;
                    wrap_override = true;
                    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                    this.setWidth(prew);
                    needsSolving = true;
                }
                if (this.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT || preh <= 0 || this.getHeight() <= preh) {
                    continue;
                }
                this.mHeightMeasuredTooSmall = true;
                wrap_override = true;
                this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                this.setHeight(preh);
                needsSolving = true;
            }
        }
        this.mChildren = (ArrayList<com.smart.library.support.constraint.solver.widgets.ConstraintWidget>)(ArrayList)allChildren;
        if (wrap_override) {
            this.mListDimensionBehaviors[0] = originalHorizontalDimensionBehaviour;
            this.mListDimensionBehaviors[1] = originalVerticalDimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline> getVerticalGuidelines() {
        final ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline> guidelines = new ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline>();
        for (int i = 0, mChildrenSize = this.mChildren.size(); i < mChildrenSize; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof com.smart.library.support.constraint.solver.widgets.Guideline) {
                final com.smart.library.support.constraint.solver.widgets.Guideline guideline = (com.smart.library.support.constraint.solver.widgets.Guideline)widget;
                if (guideline.getOrientation() == 1) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }
    
    public ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline> getHorizontalGuidelines() {
        final ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline> guidelines = new ArrayList<com.smart.library.support.constraint.solver.widgets.Guideline>();
        for (int i = 0, mChildrenSize = this.mChildren.size(); i < mChildrenSize; ++i) {
            final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof com.smart.library.support.constraint.solver.widgets.Guideline) {
                final com.smart.library.support.constraint.solver.widgets.Guideline guideline = (Guideline)widget;
                if (guideline.getOrientation() == 0) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }
    
    public LinearSystem getSystem() {
        return this.mSystem;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    void addChain(final com.smart.library.support.constraint.solver.widgets.ConstraintWidget constraintWidget, final int type) {
        if (type == 0) {
            this.addHorizontalChain(constraintWidget);
        }
        else if (type == 1) {
            this.addVerticalChain(constraintWidget);
        }
    }
    
    private void addHorizontalChain(final com.smart.library.support.constraint.solver.widgets.ConstraintWidget widget) {
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new com.smart.library.support.constraint.solver.widgets.ChainHead(widget, 0, this.isRtl());
        ++this.mHorizontalChainsSize;
    }
    
    private void addVerticalChain(final ConstraintWidget widget) {
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(widget, 1, this.isRtl());
        ++this.mVerticalChainsSize;
    }
}
