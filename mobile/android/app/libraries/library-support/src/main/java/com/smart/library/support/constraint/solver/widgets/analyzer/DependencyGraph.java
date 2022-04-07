package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;
import java.util.*;

public class DependencyGraph
{
    private static final boolean USE_GROUPS = true;
    private ConstraintWidgetContainer container;
    private boolean mNeedBuildGraph;
    private boolean mNeedRedoMeasures;
    private ConstraintWidgetContainer mContainer;
    private ArrayList<WidgetRun> mRuns;
    private ArrayList<RunGroup> runGroups;
    private BasicMeasure.Measurer mMeasurer;
    private BasicMeasure.Measure mMeasure;
    ArrayList<RunGroup> mGroups;
    
    public DependencyGraph(final ConstraintWidgetContainer container) {
        this.mNeedBuildGraph = true;
        this.mNeedRedoMeasures = true;
        this.mRuns = new ArrayList<WidgetRun>();
        this.runGroups = new ArrayList<RunGroup>();
        this.mMeasurer = null;
        this.mMeasure = new BasicMeasure.Measure();
        this.mGroups = new ArrayList<RunGroup>();
        this.container = container;
        this.mContainer = container;
    }
    
    public void setMeasurer(final BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
    }
    
    private int computeWrap(final ConstraintWidgetContainer container, final int orientation) {
        final int count = this.mGroups.size();
        long wrapSize = 0L;
        for (int i = 0; i < count; ++i) {
            final RunGroup run = this.mGroups.get(i);
            final long size = run.computeWrapSize(container, orientation);
            wrapSize = Math.max(wrapSize, size);
        }
        return (int)wrapSize;
    }
    
    public void defineTerminalWidgets(final ConstraintWidget.DimensionBehaviour horizontalBehavior, final ConstraintWidget.DimensionBehaviour verticalBehavior) {
        if (this.mNeedBuildGraph) {
            this.buildGraph();
            boolean hasBarrier = false;
            for (final ConstraintWidget widget : this.container.mChildren) {
                widget.isTerminalWidget[0] = true;
                widget.isTerminalWidget[1] = true;
                if (widget instanceof Barrier) {
                    hasBarrier = true;
                }
            }
            if (!hasBarrier) {
                for (final RunGroup group : this.mGroups) {
                    group.defineTerminalWidgets(horizontalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, verticalBehavior == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }
    }
    
    public boolean directMeasure(boolean optimizeWrap) {
        optimizeWrap &= true;
        if (this.mNeedBuildGraph || this.mNeedRedoMeasures) {
            for (final ConstraintWidget widget : this.container.mChildren) {
                widget.measured = false;
                widget.horizontalRun.dimension.resolved = false;
                widget.verticalRun.dimension.resolved = false;
                widget.horizontalRun.resolved = false;
                widget.verticalRun.resolved = false;
                widget.horizontalRun.reset();
                widget.verticalRun.reset();
            }
            this.container.measured = false;
            this.container.horizontalRun.dimension.resolved = false;
            this.container.verticalRun.dimension.resolved = false;
            this.container.horizontalRun.resolved = false;
            this.container.verticalRun.resolved = false;
            this.container.horizontalRun.reset();
            this.container.verticalRun.reset();
            this.mNeedRedoMeasures = false;
        }
        final boolean avoid = this.basicMeasureWidgets(this.mContainer);
        if (avoid) {
            return false;
        }
        this.container.setX(0);
        this.container.setY(0);
        final ConstraintWidget.DimensionBehaviour originalHorizontalDimension = this.container.getDimensionBehaviour(0);
        final ConstraintWidget.DimensionBehaviour originalVerticalDimension = this.container.getDimensionBehaviour(1);
        if (this.mNeedBuildGraph) {
            this.buildGraph();
        }
        final int x1 = this.container.getX();
        final int y1 = this.container.getY();
        this.container.horizontalRun.start.resolve(x1);
        this.container.verticalRun.start.resolve(y1);
        this.measureWidgets();
        if (originalHorizontalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || originalVerticalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            if (optimizeWrap) {
                for (final WidgetRun run : this.mRuns) {
                    if (!run.supportsWrapComputation()) {
                        optimizeWrap = false;
                        break;
                    }
                }
            }
            if (optimizeWrap && originalHorizontalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                this.container.setWidth(this.computeWrap(this.container, 0));
                this.container.horizontalRun.dimension.resolve(this.container.getWidth());
            }
            if (optimizeWrap && originalVerticalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                this.container.setHeight(this.computeWrap(this.container, 1));
                this.container.verticalRun.dimension.resolve(this.container.getHeight());
            }
        }
        boolean checkRoot = false;
        if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int x2 = x1 + this.container.getWidth();
            this.container.horizontalRun.end.resolve(x2);
            this.container.horizontalRun.dimension.resolve(x2 - x1);
            this.measureWidgets();
            if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                final int y2 = y1 + this.container.getHeight();
                this.container.verticalRun.end.resolve(y2);
                this.container.verticalRun.dimension.resolve(y2 - y1);
            }
            this.measureWidgets();
            checkRoot = true;
        }
        for (final WidgetRun run2 : this.mRuns) {
            if (run2.widget == this.container && !run2.resolved) {
                continue;
            }
            run2.applyToWidget();
        }
        boolean allResolved = true;
        for (final WidgetRun run3 : this.mRuns) {
            if (!checkRoot && run3.widget == this.container) {
                continue;
            }
            if (!run3.start.resolved) {
                allResolved = false;
                break;
            }
            if (!run3.end.resolved && !(run3 instanceof GuidelineReference)) {
                allResolved = false;
                break;
            }
            if (!run3.dimension.resolved && !(run3 instanceof ChainRun) && !(run3 instanceof GuidelineReference)) {
                allResolved = false;
                break;
            }
        }
        this.container.setHorizontalDimensionBehaviour(originalHorizontalDimension);
        this.container.setVerticalDimensionBehaviour(originalVerticalDimension);
        return allResolved;
    }
    
    public boolean directMeasureSetup(final boolean optimizeWrap) {
        if (this.mNeedBuildGraph) {
            for (final ConstraintWidget widget : this.container.mChildren) {
                widget.measured = false;
                widget.horizontalRun.dimension.resolved = false;
                widget.horizontalRun.resolved = false;
                widget.horizontalRun.reset();
                widget.verticalRun.dimension.resolved = false;
                widget.verticalRun.resolved = false;
                widget.verticalRun.reset();
            }
            this.container.measured = false;
            this.container.horizontalRun.dimension.resolved = false;
            this.container.horizontalRun.resolved = false;
            this.container.horizontalRun.reset();
            this.container.verticalRun.dimension.resolved = false;
            this.container.verticalRun.resolved = false;
            this.container.verticalRun.reset();
            this.buildGraph();
        }
        final boolean avoid = this.basicMeasureWidgets(this.mContainer);
        if (avoid) {
            return false;
        }
        this.container.setX(0);
        this.container.setY(0);
        this.container.horizontalRun.start.resolve(0);
        this.container.verticalRun.start.resolve(0);
        return true;
    }
    
    public boolean directMeasureWithOrientation(boolean optimizeWrap, final int orientation) {
        optimizeWrap &= true;
        final ConstraintWidget.DimensionBehaviour originalHorizontalDimension = this.container.getDimensionBehaviour(0);
        final ConstraintWidget.DimensionBehaviour originalVerticalDimension = this.container.getDimensionBehaviour(1);
        final int x1 = this.container.getX();
        final int y1 = this.container.getY();
        if (optimizeWrap && (originalHorizontalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || originalVerticalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
            for (final WidgetRun run : this.mRuns) {
                if (run.orientation == orientation && !run.supportsWrapComputation()) {
                    optimizeWrap = false;
                    break;
                }
            }
            if (orientation == 0) {
                if (optimizeWrap && originalHorizontalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    this.container.setWidth(this.computeWrap(this.container, 0));
                    this.container.horizontalRun.dimension.resolve(this.container.getWidth());
                }
            }
            else if (optimizeWrap && originalVerticalDimension == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                this.container.setHeight(this.computeWrap(this.container, 1));
                this.container.verticalRun.dimension.resolve(this.container.getHeight());
            }
        }
        boolean checkRoot = false;
        if (orientation == 0) {
            if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                final int x2 = x1 + this.container.getWidth();
                this.container.horizontalRun.end.resolve(x2);
                this.container.horizontalRun.dimension.resolve(x2 - x1);
                checkRoot = true;
            }
        }
        else if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int y2 = y1 + this.container.getHeight();
            this.container.verticalRun.end.resolve(y2);
            this.container.verticalRun.dimension.resolve(y2 - y1);
            checkRoot = true;
        }
        this.measureWidgets();
        for (final WidgetRun run2 : this.mRuns) {
            if (run2.orientation != orientation) {
                continue;
            }
            if (run2.widget == this.container && !run2.resolved) {
                continue;
            }
            run2.applyToWidget();
        }
        boolean allResolved = true;
        for (final WidgetRun run3 : this.mRuns) {
            if (run3.orientation != orientation) {
                continue;
            }
            if (!checkRoot && run3.widget == this.container) {
                continue;
            }
            if (!run3.start.resolved) {
                allResolved = false;
                break;
            }
            if (!run3.end.resolved) {
                allResolved = false;
                break;
            }
            if (!(run3 instanceof ChainRun) && !run3.dimension.resolved) {
                allResolved = false;
                break;
            }
        }
        this.container.setHorizontalDimensionBehaviour(originalHorizontalDimension);
        this.container.setVerticalDimensionBehaviour(originalVerticalDimension);
        return allResolved;
    }
    
    private void measure(final ConstraintWidget widget, final ConstraintWidget.DimensionBehaviour horizontalBehavior, final int horizontalDimension, final ConstraintWidget.DimensionBehaviour verticalBehavior, final int verticalDimension) {
        this.mMeasure.horizontalBehavior = horizontalBehavior;
        this.mMeasure.verticalBehavior = verticalBehavior;
        this.mMeasure.horizontalDimension = horizontalDimension;
        this.mMeasure.verticalDimension = verticalDimension;
        this.mMeasurer.measure(widget, this.mMeasure);
        widget.setWidth(this.mMeasure.measuredWidth);
        widget.setHeight(this.mMeasure.measuredHeight);
        widget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
    }
    
    private boolean basicMeasureWidgets(final ConstraintWidgetContainer constraintWidgetContainer) {
        for (final ConstraintWidget widget : constraintWidgetContainer.mChildren) {
            ConstraintWidget.DimensionBehaviour horiz = widget.mListDimensionBehaviors[0];
            ConstraintWidget.DimensionBehaviour vert = widget.mListDimensionBehaviors[1];
            if (widget.getVisibility() == 8) {
                widget.measured = true;
            }
            else {
                if (widget.mMatchConstraintPercentWidth < 1.0f && horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widget.mMatchConstraintDefaultWidth = 2;
                }
                if (widget.mMatchConstraintPercentHeight < 1.0f && vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widget.mMatchConstraintDefaultHeight = 2;
                }
                if (widget.getDimensionRatio() > 0.0f) {
                    if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (vert == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || vert == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        widget.mMatchConstraintDefaultWidth = 3;
                    }
                    else if (vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (horiz == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || horiz == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        widget.mMatchConstraintDefaultHeight = 3;
                    }
                    else if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (widget.mMatchConstraintDefaultWidth == 0) {
                            widget.mMatchConstraintDefaultWidth = 3;
                        }
                        if (widget.mMatchConstraintDefaultHeight == 0) {
                            widget.mMatchConstraintDefaultHeight = 3;
                        }
                    }
                }
                if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultWidth == 1 && (widget.mLeft.mTarget == null || widget.mRight.mTarget == null)) {
                    horiz = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                }
                if (vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultHeight == 1 && (widget.mTop.mTarget == null || widget.mBottom.mTarget == null)) {
                    vert = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                }
                widget.horizontalRun.dimensionBehavior = horiz;
                widget.horizontalRun.matchConstraintsType = widget.mMatchConstraintDefaultWidth;
                widget.verticalRun.dimensionBehavior = vert;
                widget.verticalRun.matchConstraintsType = widget.mMatchConstraintDefaultHeight;
                if ((horiz == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || horiz == ConstraintWidget.DimensionBehaviour.FIXED || horiz == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) && (vert == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || vert == ConstraintWidget.DimensionBehaviour.FIXED || vert == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
                    int width = widget.getWidth();
                    if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                        width = constraintWidgetContainer.getWidth() - widget.mLeft.mMargin - widget.mRight.mMargin;
                        horiz = ConstraintWidget.DimensionBehaviour.FIXED;
                    }
                    int height = widget.getHeight();
                    if (vert == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                        height = constraintWidgetContainer.getHeight() - widget.mTop.mMargin - widget.mBottom.mMargin;
                        vert = ConstraintWidget.DimensionBehaviour.FIXED;
                    }
                    this.measure(widget, horiz, width, vert, height);
                    widget.horizontalRun.dimension.resolve(widget.getWidth());
                    widget.verticalRun.dimension.resolve(widget.getHeight());
                    widget.measured = true;
                }
                else {
                    if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (vert == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || vert == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        if (widget.mMatchConstraintDefaultWidth == 3) {
                            if (vert == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            }
                            final int height2 = widget.getHeight();
                            final int width2 = (int)(height2 * widget.mDimensionRatio + 0.5f);
                            this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, width2, ConstraintWidget.DimensionBehaviour.FIXED, height2);
                            widget.horizontalRun.dimension.resolve(widget.getWidth());
                            widget.verticalRun.dimension.resolve(widget.getHeight());
                            widget.measured = true;
                            continue;
                        }
                        if (widget.mMatchConstraintDefaultWidth == 1) {
                            this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, vert, 0);
                            widget.horizontalRun.dimension.wrapValue = widget.getWidth();
                            continue;
                        }
                        if (widget.mMatchConstraintDefaultWidth == 2) {
                            if (constraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                                final float percent = widget.mMatchConstraintPercentWidth;
                                final int width2 = (int)(0.5f + percent * constraintWidgetContainer.getWidth());
                                final int height3 = widget.getHeight();
                                this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, width2, vert, height3);
                                widget.horizontalRun.dimension.resolve(widget.getWidth());
                                widget.verticalRun.dimension.resolve(widget.getHeight());
                                widget.measured = true;
                                continue;
                            }
                        }
                        else if (widget.mListAnchors[0].mTarget == null || widget.mListAnchors[1].mTarget == null) {
                            this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, vert, 0);
                            widget.horizontalRun.dimension.resolve(widget.getWidth());
                            widget.verticalRun.dimension.resolve(widget.getHeight());
                            widget.measured = true;
                            continue;
                        }
                    }
                    if (vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (horiz == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || horiz == ConstraintWidget.DimensionBehaviour.FIXED)) {
                        if (widget.mMatchConstraintDefaultHeight == 3) {
                            if (horiz == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            }
                            final int width = widget.getWidth();
                            float ratio = widget.mDimensionRatio;
                            if (widget.getDimensionRatioSide() == -1) {
                                ratio = 1.0f / ratio;
                            }
                            final int height3 = (int)(width * ratio + 0.5f);
                            this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, width, ConstraintWidget.DimensionBehaviour.FIXED, height3);
                            widget.horizontalRun.dimension.resolve(widget.getWidth());
                            widget.verticalRun.dimension.resolve(widget.getHeight());
                            widget.measured = true;
                            continue;
                        }
                        if (widget.mMatchConstraintDefaultHeight == 1) {
                            this.measure(widget, horiz, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                            widget.verticalRun.dimension.wrapValue = widget.getHeight();
                            continue;
                        }
                        if (widget.mMatchConstraintDefaultHeight == 2) {
                            if (constraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                                final float percent = widget.mMatchConstraintPercentHeight;
                                final int width2 = widget.getWidth();
                                final int height3 = (int)(0.5f + percent * constraintWidgetContainer.getHeight());
                                this.measure(widget, horiz, width2, ConstraintWidget.DimensionBehaviour.FIXED, height3);
                                widget.horizontalRun.dimension.resolve(widget.getWidth());
                                widget.verticalRun.dimension.resolve(widget.getHeight());
                                widget.measured = true;
                                continue;
                            }
                        }
                        else if (widget.mListAnchors[2].mTarget == null || widget.mListAnchors[3].mTarget == null) {
                            this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, vert, 0);
                            widget.horizontalRun.dimension.resolve(widget.getWidth());
                            widget.verticalRun.dimension.resolve(widget.getHeight());
                            widget.measured = true;
                            continue;
                        }
                    }
                    if (horiz != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || vert != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        continue;
                    }
                    if (widget.mMatchConstraintDefaultWidth == 1 || widget.mMatchConstraintDefaultHeight == 1) {
                        this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                        widget.horizontalRun.dimension.wrapValue = widget.getWidth();
                        widget.verticalRun.dimension.wrapValue = widget.getHeight();
                    }
                    else {
                        if (widget.mMatchConstraintDefaultHeight != 2 || widget.mMatchConstraintDefaultWidth != 2 || (constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED) || (constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED)) {
                            continue;
                        }
                        final float horizPercent = widget.mMatchConstraintPercentWidth;
                        final float vertPercent = widget.mMatchConstraintPercentHeight;
                        final int width3 = (int)(0.5f + horizPercent * constraintWidgetContainer.getWidth());
                        final int height4 = (int)(0.5f + vertPercent * constraintWidgetContainer.getHeight());
                        this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, width3, ConstraintWidget.DimensionBehaviour.FIXED, height4);
                        widget.horizontalRun.dimension.resolve(widget.getWidth());
                        widget.verticalRun.dimension.resolve(widget.getHeight());
                        widget.measured = true;
                    }
                }
            }
        }
        return false;
    }
    
    public void measureWidgets() {
        for (final ConstraintWidget widget : this.container.mChildren) {
            if (widget.measured) {
                continue;
            }
            final ConstraintWidget.DimensionBehaviour horiz = widget.mListDimensionBehaviors[0];
            final ConstraintWidget.DimensionBehaviour vert = widget.mListDimensionBehaviors[1];
            final int horizMatchConstraintsType = widget.mMatchConstraintDefaultWidth;
            final int vertMatchConstraintsType = widget.mMatchConstraintDefaultHeight;
            final boolean horizWrap = horiz == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && horizMatchConstraintsType == 1);
            final boolean vertWrap = vert == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && vertMatchConstraintsType == 1);
            final boolean horizResolved = widget.horizontalRun.dimension.resolved;
            final boolean vertResolved = widget.verticalRun.dimension.resolved;
            if (horizResolved && vertResolved) {
                this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, widget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, widget.verticalRun.dimension.value);
                widget.measured = true;
            }
            else if (horizResolved && vertWrap) {
                this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, widget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, widget.verticalRun.dimension.value);
                if (vert == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widget.verticalRun.dimension.wrapValue = widget.getHeight();
                }
                else {
                    widget.verticalRun.dimension.resolve(widget.getHeight());
                    widget.measured = true;
                }
            }
            else if (vertResolved && horizWrap) {
                this.measure(widget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, widget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, widget.verticalRun.dimension.value);
                if (horiz == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    widget.horizontalRun.dimension.wrapValue = widget.getWidth();
                }
                else {
                    widget.horizontalRun.dimension.resolve(widget.getWidth());
                    widget.measured = true;
                }
            }
            if (!widget.measured || widget.verticalRun.baselineDimension == null) {
                continue;
            }
            widget.verticalRun.baselineDimension.resolve(widget.getBaselineDistance());
        }
    }
    
    public void invalidateGraph() {
        this.mNeedBuildGraph = true;
    }
    
    public void invalidateMeasures() {
        this.mNeedRedoMeasures = true;
    }
    
    public void buildGraph() {
        this.buildGraph(this.mRuns);
        this.mGroups.clear();
        RunGroup.index = 0;
        this.findGroup(this.container.horizontalRun, 0, this.mGroups);
        this.findGroup(this.container.verticalRun, 1, this.mGroups);
        this.mNeedBuildGraph = false;
    }
    
    public void buildGraph(final ArrayList<WidgetRun> runs) {
        runs.clear();
        this.mContainer.horizontalRun.clear();
        this.mContainer.verticalRun.clear();
        runs.add(this.mContainer.horizontalRun);
        runs.add(this.mContainer.verticalRun);
        HashSet<ChainRun> chainRuns = null;
        for (final ConstraintWidget widget : this.mContainer.mChildren) {
            if (widget instanceof Guideline) {
                runs.add(new GuidelineReference(widget));
            }
            else {
                if (widget.isInHorizontalChain()) {
                    if (widget.horizontalChainRun == null) {
                        final ChainRun chainRun = new ChainRun(widget, 0);
                        widget.horizontalChainRun = chainRun;
                    }
                    if (chainRuns == null) {
                        chainRuns = new HashSet<ChainRun>();
                    }
                    chainRuns.add(widget.horizontalChainRun);
                }
                else {
                    runs.add(widget.horizontalRun);
                }
                if (widget.isInVerticalChain()) {
                    if (widget.verticalChainRun == null) {
                        final ChainRun chainRun = new ChainRun(widget, 1);
                        widget.verticalChainRun = chainRun;
                    }
                    if (chainRuns == null) {
                        chainRuns = new HashSet<ChainRun>();
                    }
                    chainRuns.add(widget.verticalChainRun);
                }
                else {
                    runs.add(widget.verticalRun);
                }
                if (!(widget instanceof HelperWidget)) {
                    continue;
                }
                runs.add(new HelperReferences(widget));
            }
        }
        if (chainRuns != null) {
            runs.addAll(chainRuns);
        }
        for (final WidgetRun run : runs) {
            run.clear();
        }
        for (final WidgetRun run : runs) {
            if (run.widget == this.mContainer) {
                continue;
            }
            run.apply();
        }
    }
    
    private void displayGraph() {
        String content = "digraph {\n";
        for (final WidgetRun run : this.mRuns) {
            content = this.generateDisplayGraph(run, content);
        }
        content += "\n}\n";
        System.out.println("content:<<\n" + content + "\n>>");
    }
    
    private void applyGroup(final DependencyNode node, final int orientation, final int direction, final DependencyNode end, final ArrayList<RunGroup> groups, RunGroup group) {
        final WidgetRun run = node.run;
        if (run.runGroup != null || run == this.container.horizontalRun || run == this.container.verticalRun) {
            return;
        }
        if (group == null) {
            group = new RunGroup(run, direction);
            groups.add(group);
        }
        (run.runGroup = group).add(run);
        for (final Dependency dependent : run.start.dependencies) {
            if (dependent instanceof DependencyNode) {
                this.applyGroup((DependencyNode)dependent, orientation, 0, end, groups, group);
            }
        }
        for (final Dependency dependent : run.end.dependencies) {
            if (dependent instanceof DependencyNode) {
                this.applyGroup((DependencyNode)dependent, orientation, 1, end, groups, group);
            }
        }
        if (orientation == 1 && run instanceof VerticalWidgetRun) {
            for (final Dependency dependent : ((VerticalWidgetRun)run).baseline.dependencies) {
                if (dependent instanceof DependencyNode) {
                    this.applyGroup((DependencyNode)dependent, orientation, 2, end, groups, group);
                }
            }
        }
        for (final DependencyNode target : run.start.targets) {
            if (target == end) {
                group.dual = true;
            }
            this.applyGroup(target, orientation, 0, end, groups, group);
        }
        for (final DependencyNode target : run.end.targets) {
            if (target == end) {
                group.dual = true;
            }
            this.applyGroup(target, orientation, 1, end, groups, group);
        }
        if (orientation == 1 && run instanceof VerticalWidgetRun) {
            for (final DependencyNode target : ((VerticalWidgetRun)run).baseline.targets) {
                this.applyGroup(target, orientation, 2, end, groups, group);
            }
        }
    }
    
    private void findGroup(final WidgetRun run, final int orientation, final ArrayList<RunGroup> groups) {
        for (final Dependency dependent : run.start.dependencies) {
            if (dependent instanceof DependencyNode) {
                final DependencyNode node = (DependencyNode)dependent;
                this.applyGroup(node, orientation, 0, run.end, groups, null);
            }
            else {
                if (!(dependent instanceof WidgetRun)) {
                    continue;
                }
                final WidgetRun dependentRun = (WidgetRun)dependent;
                this.applyGroup(dependentRun.start, orientation, 0, run.end, groups, null);
            }
        }
        for (final Dependency dependent : run.end.dependencies) {
            if (dependent instanceof DependencyNode) {
                final DependencyNode node = (DependencyNode)dependent;
                this.applyGroup(node, orientation, 1, run.start, groups, null);
            }
            else {
                if (!(dependent instanceof WidgetRun)) {
                    continue;
                }
                final WidgetRun dependentRun = (WidgetRun)dependent;
                this.applyGroup(dependentRun.end, orientation, 1, run.start, groups, null);
            }
        }
        if (orientation == 1) {
            for (final Dependency dependent : ((VerticalWidgetRun)run).baseline.dependencies) {
                if (dependent instanceof DependencyNode) {
                    final DependencyNode node = (DependencyNode)dependent;
                    this.applyGroup(node, orientation, 2, null, groups, null);
                }
            }
        }
    }
    
    private String generateDisplayNode(final DependencyNode node, final boolean centeredConnection, String content) {
        for (final DependencyNode target : node.targets) {
            String constraint = "\n" + node.name();
            constraint = constraint + " -> " + target.name();
            if (node.margin > 0 || centeredConnection || node.run instanceof HelperReferences) {
                constraint += "[";
                if (node.margin > 0) {
                    constraint = constraint + "label=\"" + node.margin + "\"";
                    if (centeredConnection) {
                        constraint += ",";
                    }
                }
                if (centeredConnection) {
                    constraint += " style=dashed ";
                }
                if (node.run instanceof HelperReferences) {
                    constraint += " style=bold,color=gray ";
                }
                constraint += "]";
            }
            constraint += "\n";
            content += constraint;
        }
        return content;
    }
    
    private String nodeDefinition(final WidgetRun run) {
        final int orientation = (run instanceof VerticalWidgetRun) ? 1 : 0;
        String definition;
        final String name = definition = run.widget.getDebugName();
        final ConstraintWidget.DimensionBehaviour behaviour = (orientation == 0) ? run.widget.getHorizontalDimensionBehaviour() : run.widget.getVerticalDimensionBehaviour();
        final RunGroup runGroup = run.runGroup;
        if (orientation == 0) {
            definition += "_HORIZONTAL";
        }
        else {
            definition += "_VERTICAL";
        }
        definition += " [shape=none, label=<";
        definition += "<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">";
        definition += "  <TR>";
        if (orientation == 0) {
            definition += "    <TD ";
            if (run.start.resolved) {
                definition += " BGCOLOR=\"green\"";
            }
            definition += " PORT=\"LEFT\" BORDER=\"1\">L</TD>";
        }
        else {
            definition += "    <TD ";
            if (run.start.resolved) {
                definition += " BGCOLOR=\"green\"";
            }
            definition += " PORT=\"TOP\" BORDER=\"1\">T</TD>";
        }
        definition += "    <TD BORDER=\"1\" ";
        if (run.dimension.resolved && !run.widget.measured) {
            definition += " BGCOLOR=\"green\" ";
        }
        else if (run.dimension.resolved && run.widget.measured) {
            definition += " BGCOLOR=\"lightgray\" ";
        }
        else if (!run.dimension.resolved && run.widget.measured) {
            definition += " BGCOLOR=\"yellow\" ";
        }
        if (behaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            definition += "style=\"dashed\"";
        }
        String group = "";
        if (runGroup != null) {
            group = " [" + (runGroup.groupIndex + 1) + "/" + RunGroup.index + "]";
        }
        definition = definition + ">" + name + group + " </TD>";
        if (orientation == 0) {
            definition += "    <TD ";
            if (run.end.resolved) {
                definition += " BGCOLOR=\"green\"";
            }
            definition += " PORT=\"RIGHT\" BORDER=\"1\">R</TD>";
        }
        else {
            definition += "    <TD ";
            if (run instanceof VerticalWidgetRun && ((VerticalWidgetRun)run).baseline.resolved) {
                definition += " BGCOLOR=\"green\"";
            }
            definition += " PORT=\"BASELINE\" BORDER=\"1\">b</TD>";
            definition += "    <TD ";
            if (run.end.resolved) {
                definition += " BGCOLOR=\"green\"";
            }
            definition += " PORT=\"BOTTOM\" BORDER=\"1\">B</TD>";
        }
        definition += "  </TR></TABLE>";
        definition += ">];\n";
        return definition;
    }
    
    private String generateChainDisplayGraph(final ChainRun chain, final String content) {
        final int orientation = chain.orientation;
        String name = "cluster_" + chain.widget.getDebugName();
        if (orientation == 0) {
            name += "_h";
        }
        else {
            name += "_v";
        }
        String subgroup = "subgraph " + name + " {\n";
        String definitions = "";
        for (final WidgetRun run : chain.widgets) {
            String runName = run.widget.getDebugName();
            if (orientation == 0) {
                runName += "_HORIZONTAL";
            }
            else {
                runName += "_VERTICAL";
            }
            subgroup = subgroup + runName + ";\n";
            definitions = this.generateDisplayGraph(run, definitions);
        }
        subgroup += "}\n";
        return content + definitions + subgroup;
    }
    
    private boolean isCenteredConnection(final DependencyNode start, final DependencyNode end) {
        int startTargets = 0;
        int endTargets = 0;
        for (final DependencyNode s : start.targets) {
            if (s != end) {
                ++startTargets;
            }
        }
        for (final DependencyNode e : end.targets) {
            if (e != start) {
                ++endTargets;
            }
        }
        return startTargets > 0 && endTargets > 0;
    }
    
    private String generateDisplayGraph(final WidgetRun root, String content) {
        final DependencyNode start = root.start;
        final DependencyNode end = root.end;
        if (!(root instanceof HelperReferences) && start.dependencies.isEmpty() && (end.dependencies.isEmpty() & start.targets.isEmpty()) && end.targets.isEmpty()) {
            return content;
        }
        content += this.nodeDefinition(root);
        final boolean centeredConnection = this.isCenteredConnection(start, end);
        content = this.generateDisplayNode(start, centeredConnection, content);
        content = this.generateDisplayNode(end, centeredConnection, content);
        if (root instanceof VerticalWidgetRun) {
            final DependencyNode baseline = ((VerticalWidgetRun)root).baseline;
            content = this.generateDisplayNode(baseline, centeredConnection, content);
        }
        if (root instanceof HorizontalWidgetRun || (root instanceof ChainRun && ((ChainRun)root).orientation == 0)) {
            final ConstraintWidget.DimensionBehaviour behaviour = root.widget.getHorizontalDimensionBehaviour();
            if (behaviour == ConstraintWidget.DimensionBehaviour.FIXED || behaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (!start.targets.isEmpty() && end.targets.isEmpty()) {
                    final String constraint = "\n" + end.name() + " -> " + start.name() + "\n";
                    content += constraint;
                }
                else if (start.targets.isEmpty() && !end.targets.isEmpty()) {
                    final String constraint = "\n" + start.name() + " -> " + end.name() + "\n";
                    content += constraint;
                }
            }
            else if (behaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && root.widget.getDimensionRatio() > 0.0f) {
                final String name = root.widget.getDebugName();
                new StringBuilder().append("\n").append(name).append("_HORIZONTAL -> ").append(name).append("_VERTICAL;\n").toString();
            }
        }
        else if (root instanceof VerticalWidgetRun || (root instanceof ChainRun && ((ChainRun)root).orientation == 1)) {
            final ConstraintWidget.DimensionBehaviour behaviour = root.widget.getVerticalDimensionBehaviour();
            if (behaviour == ConstraintWidget.DimensionBehaviour.FIXED || behaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (!start.targets.isEmpty() && end.targets.isEmpty()) {
                    final String constraint = "\n" + end.name() + " -> " + start.name() + "\n";
                    content += constraint;
                }
                else if (start.targets.isEmpty() && !end.targets.isEmpty()) {
                    final String constraint = "\n" + start.name() + " -> " + end.name() + "\n";
                    content += constraint;
                }
            }
            else if (behaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && root.widget.getDimensionRatio() > 0.0f) {
                final String name = root.widget.getDebugName();
                new StringBuilder().append("\n").append(name).append("_VERTICAL -> ").append(name).append("_HORIZONTAL;\n").toString();
            }
        }
        if (root instanceof ChainRun) {
            return this.generateChainDisplayGraph((ChainRun)root, content);
        }
        return content;
    }
}
