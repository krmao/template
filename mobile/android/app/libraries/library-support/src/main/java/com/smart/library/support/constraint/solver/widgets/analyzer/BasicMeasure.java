package com.smart.library.support.constraint.solver.widgets.analyzer;

import android.view.View;

import java.util.*;

import com.smart.library.support.constraint.solver.LinearSystem;
import com.smart.library.support.constraint.solver.Metrics;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;
import com.smart.library.support.constraint.solver.widgets.Guideline;
import com.smart.library.support.constraint.solver.widgets.Helper;
import com.smart.library.support.constraint.solver.widgets.Optimizer;
import com.smart.library.support.constraint.solver.widgets.VirtualLayout;

public class BasicMeasure
{
    private static final boolean DEBUG = false;
    private static final int MODE_SHIFT = 30;
    public static final int UNSPECIFIED = 0;
    public static final int EXACTLY = View.MeasureSpec.EXACTLY;
    public static final int AT_MOST = Integer.MIN_VALUE;
    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;
    public static final int FIXED = -3;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    private Measure mMeasure;
    private ConstraintWidgetContainer constraintWidgetContainer;
    
    public void updateHierarchy(final ConstraintWidgetContainer layout) {
        this.mVariableDimensionsWidgets.clear();
        for (int childCount = layout.mChildren.size(), i = 0; i < childCount; ++i) {
            final ConstraintWidget widget = layout.mChildren.get(i);
            if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                this.mVariableDimensionsWidgets.add(widget);
            }
        }
        layout.invalidateGraph();
    }
    
    public BasicMeasure(final ConstraintWidgetContainer constraintWidgetContainer) {
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>();
        this.mMeasure = new Measure();
        this.constraintWidgetContainer = constraintWidgetContainer;
    }
    
    private void solveLinearSystem(final String reason) {
        this.constraintWidgetContainer.layout();
    }
    
    private void measureChildren(final ConstraintWidgetContainer layout) {
        final int childCount = layout.mChildren.size();
        final Measurer measurer = layout.getMeasurer();
        for (int i = 0; i < childCount; ++i) {
            final ConstraintWidget child = layout.mChildren.get(i);
            if (!(child instanceof Guideline)) {
                if (!child.horizontalRun.dimension.resolved || !child.verticalRun.dimension.resolved) {
                    final ConstraintWidget.DimensionBehaviour widthBehavior = child.getDimensionBehaviour(0);
                    final ConstraintWidget.DimensionBehaviour heightBehavior = child.getDimensionBehaviour(1);
                    final boolean skip = widthBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultWidth != 1 && heightBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && child.mMatchConstraintDefaultHeight != 1;
                    if (!skip) {
                        this.measure(measurer, child, false);
                    }
                }
            }
        }
        measurer.didMeasures();
    }
    
    public void solverMeasure(final ConstraintWidgetContainer layout, final int optimizationLevel, final int paddingX, final int paddingY, final int widthMode, int widthSize, final int heightMode, int heightSize, final int lastMeasureWidth, final int lastMeasureHeight) {
        final Measurer measurer = layout.getMeasurer();
        final int childCount = layout.mChildren.size();
        final int startingWidth = layout.getWidth();
        final int startingHeight = layout.getHeight();
        final boolean optimizeWrap = Optimizer.enabled(optimizationLevel, 128);
        boolean optimize = optimizeWrap || Optimizer.enabled(optimizationLevel, 64);
        if (optimize) {
            for (int i = 0; i < childCount; ++i) {
                final ConstraintWidget child = layout.mChildren.get(i);
                final boolean matchWidth = child.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                final boolean matchHeight = child.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                final boolean ratio = matchWidth && matchHeight && child.getDimensionRatio() > 0.0f;
                if (child.isInHorizontalChain() && ratio) {
                    optimize = false;
                    break;
                }
                if (child.isInVerticalChain() && ratio) {
                    optimize = false;
                    break;
                }
                if (child instanceof VirtualLayout) {
                    optimize = false;
                    break;
                }
            }
        }
        if (optimize && LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.measures;
        }
        boolean allSolved = false;
        optimize &= (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY);
        int computations = 0;
        if (optimize) {
            widthSize = Math.min(layout.getMaxWidth(), widthSize);
            heightSize = Math.min(layout.getMaxHeight(), heightSize);
            if (widthMode == View.MeasureSpec.EXACTLY && layout.getWidth() != widthSize) {
                layout.setWidth(widthSize);
                layout.invalidateGraph();
            }
            if (heightMode == View.MeasureSpec.EXACTLY && layout.getHeight() != heightSize) {
                layout.setHeight(heightSize);
                layout.invalidateGraph();
            }
            if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {
                allSolved = layout.directMeasure(optimizeWrap);
                computations = 2;
            }
            else {
                allSolved = layout.directMeasureSetup(optimizeWrap);
                if (widthMode == View.MeasureSpec.EXACTLY) {
                    allSolved &= layout.directMeasureWithOrientation(optimizeWrap, 0);
                    ++computations;
                }
                if (heightMode == View.MeasureSpec.EXACTLY) {
                    allSolved &= layout.directMeasureWithOrientation(optimizeWrap, 1);
                    ++computations;
                }
            }
            if (allSolved) {
                layout.updateFromRuns(widthMode == View.MeasureSpec.EXACTLY, heightMode == View.MeasureSpec.EXACTLY);
            }
        }
        else {
            layout.horizontalRun.clear();
            layout.verticalRun.clear();
            for (final ConstraintWidget child2 : layout.getChildren()) {
                child2.horizontalRun.clear();
                child2.verticalRun.clear();
            }
        }
        if (!allSolved || computations != 2) {
            if (childCount > 0) {
                this.measureChildren(layout);
            }
            final int optimizations = layout.getOptimizationLevel();
            final int sizeDependentWidgetsCount = this.mVariableDimensionsWidgets.size();
            if (childCount > 0) {
                this.solveLinearSystem("First pass");
            }
            if (sizeDependentWidgetsCount > 0) {
                boolean needSolverPass = false;
                final boolean containerWrapWidth = layout.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                final boolean containerWrapHeight = layout.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                int minWidth = Math.max(layout.getWidth(), this.constraintWidgetContainer.getMinWidth());
                int minHeight = Math.max(layout.getHeight(), this.constraintWidgetContainer.getMinHeight());
                for (int j = 0; j < sizeDependentWidgetsCount; ++j) {
                    final ConstraintWidget widget = this.mVariableDimensionsWidgets.get(j);
                    if (widget instanceof VirtualLayout) {
                        final int preWidth = widget.getWidth();
                        final int preHeight = widget.getHeight();
                        needSolverPass |= this.measure(measurer, widget, true);
                        final int measuredWidth = widget.getWidth();
                        final int measuredHeight = widget.getHeight();
                        if (measuredWidth != preWidth) {
                            widget.setWidth(measuredWidth);
                            if (containerWrapWidth && widget.getRight() > minWidth) {
                                final int w = widget.getRight() + widget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin();
                                minWidth = Math.max(minWidth, w);
                            }
                            needSolverPass = true;
                        }
                        if (measuredHeight != preHeight) {
                            widget.setHeight(measuredHeight);
                            if (containerWrapHeight && widget.getBottom() > minHeight) {
                                final int h = widget.getBottom() + widget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin();
                                minHeight = Math.max(minHeight, h);
                            }
                            needSolverPass = true;
                        }
                        final VirtualLayout virtualLayout = (VirtualLayout)widget;
                        needSolverPass |= virtualLayout.needSolverPass();
                    }
                }
                if (needSolverPass) {
                    layout.setWidth(startingWidth);
                    layout.setHeight(startingHeight);
                    this.solveLinearSystem("2nd pass");
                    needSolverPass = false;
                }
                for (int maxIterations = 2, k = 0; k < maxIterations; ++k) {
                    for (int l = 0; l < sizeDependentWidgetsCount; ++l) {
                        final ConstraintWidget widget2 = this.mVariableDimensionsWidgets.get(l);
                        if (!(widget2 instanceof Helper) || widget2 instanceof VirtualLayout) {
                            if (!(widget2 instanceof Guideline)) {
                                if (widget2.getVisibility() != 8) {
                                    if (!widget2.horizontalRun.dimension.resolved || !widget2.verticalRun.dimension.resolved) {
                                        if (!(widget2 instanceof VirtualLayout)) {
                                            final int preWidth2 = widget2.getWidth();
                                            final int preHeight2 = widget2.getHeight();
                                            final int preBaselineDistance = widget2.getBaselineDistance();
                                            needSolverPass |= this.measure(measurer, widget2, true);
                                            final int measuredWidth2 = widget2.getWidth();
                                            final int measuredHeight2 = widget2.getHeight();
                                            if (measuredWidth2 != preWidth2) {
                                                widget2.setWidth(measuredWidth2);
                                                if (containerWrapWidth && widget2.getRight() > minWidth) {
                                                    final int w2 = widget2.getRight() + widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin();
                                                    minWidth = Math.max(minWidth, w2);
                                                }
                                                needSolverPass = true;
                                            }
                                            if (measuredHeight2 != preHeight2) {
                                                widget2.setHeight(measuredHeight2);
                                                if (containerWrapHeight && widget2.getBottom() > minHeight) {
                                                    final int h2 = widget2.getBottom() + widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin();
                                                    minHeight = Math.max(minHeight, h2);
                                                }
                                                needSolverPass = true;
                                            }
                                            if (widget2.hasBaseline() && preBaselineDistance != widget2.getBaselineDistance()) {
                                                needSolverPass = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (needSolverPass) {
                        layout.setWidth(startingWidth);
                        layout.setHeight(startingHeight);
                        this.solveLinearSystem("intermediate pass");
                        needSolverPass = false;
                    }
                }
                if (needSolverPass) {
                    layout.setWidth(startingWidth);
                    layout.setHeight(startingHeight);
                    this.solveLinearSystem("2nd pass");
                    needSolverPass = false;
                    if (layout.getWidth() < minWidth) {
                        layout.setWidth(minWidth);
                        needSolverPass = true;
                    }
                    if (layout.getHeight() < minHeight) {
                        layout.setHeight(minHeight);
                        needSolverPass = true;
                    }
                    if (needSolverPass) {
                        this.solveLinearSystem("3rd pass");
                    }
                }
            }
            layout.setOptimizationLevel(optimizations);
        }
    }
    
    private boolean measure(final Measurer measurer, final ConstraintWidget widget, final boolean useDeprecated) {
        this.mMeasure.horizontalBehavior = widget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = widget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = widget.getWidth();
        this.mMeasure.verticalDimension = widget.getHeight();
        this.mMeasure.measuredNeedsSolverPass = false;
        this.mMeasure.useDeprecated = useDeprecated;
        measurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
        this.mMeasure.useDeprecated = false;
        return this.mMeasure.measuredNeedsSolverPass;
    }
    
    public enum MeasureType
    {
    }
    
    public static class Measure
    {
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int horizontalDimension;
        public int verticalDimension;
        public int measuredWidth;
        public int measuredHeight;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public boolean measuredNeedsSolverPass;
        public boolean useDeprecated;
    }
    
    public interface Measurer
    {
        void measure(final ConstraintWidget p0, final Measure p1);
        
        void didMeasures();
    }
}
