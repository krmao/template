package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;

public abstract class WidgetRun implements Dependency
{
    public int matchConstraintsType;
    ConstraintWidget widget;
    RunGroup runGroup;
    protected ConstraintWidget.DimensionBehaviour dimensionBehavior;
    DimensionDependency dimension;
    public int orientation;
    boolean resolved;
    public DependencyNode start;
    public DependencyNode end;
    protected RunType mRunType;
    
    public WidgetRun(final ConstraintWidget widget) {
        this.dimension = new DimensionDependency(this);
        this.orientation = 0;
        this.resolved = false;
        this.start = new DependencyNode(this);
        this.end = new DependencyNode(this);
        this.mRunType = RunType.NONE;
        this.widget = widget;
    }
    
    abstract void clear();
    
    abstract void apply();
    
    abstract void applyToWidget();
    
    abstract void reset();
    
    abstract boolean supportsWrapComputation();
    
    public boolean isDimensionResolved() {
        return this.dimension.resolved;
    }
    
    public boolean isCenterConnection() {
        int connections = 0;
        for (int count = this.start.targets.size(), i = 0; i < count; ++i) {
            final DependencyNode dependency = this.start.targets.get(i);
            if (dependency.run != this) {
                ++connections;
            }
        }
        for (int count = this.end.targets.size(), i = 0; i < count; ++i) {
            final DependencyNode dependency = this.end.targets.get(i);
            if (dependency.run != this) {
                ++connections;
            }
        }
        return connections >= 2;
    }
    
    public long wrapSize(final int direction) {
        if (this.dimension.resolved) {
            long size = this.dimension.value;
            if (this.isCenterConnection()) {
                size += this.start.margin - this.end.margin;
            }
            else if (direction == 0) {
                size += this.start.margin;
            }
            else {
                size -= this.end.margin;
            }
            return size;
        }
        return 0L;
    }
    
    protected final DependencyNode getTarget(final ConstraintAnchor anchor) {
        if (anchor.mTarget == null) {
            return null;
        }
        DependencyNode target = null;
        final ConstraintWidget targetWidget = anchor.mTarget.mOwner;
        final ConstraintAnchor.Type targetType = anchor.mTarget.mType;
        switch (targetType) {
            case LEFT: {
                final HorizontalWidgetRun run = targetWidget.horizontalRun;
                target = run.start;
                break;
            }
            case RIGHT: {
                final HorizontalWidgetRun run = targetWidget.horizontalRun;
                target = run.end;
                break;
            }
            case TOP: {
                final VerticalWidgetRun run2 = targetWidget.verticalRun;
                target = run2.start;
                break;
            }
            case BASELINE: {
                final VerticalWidgetRun run2 = targetWidget.verticalRun;
                target = run2.baseline;
                break;
            }
            case BOTTOM: {
                final VerticalWidgetRun run2 = targetWidget.verticalRun;
                target = run2.end;
                break;
            }
        }
        return target;
    }
    
    protected void updateRunCenter(final Dependency dependency, final ConstraintAnchor startAnchor, final ConstraintAnchor endAnchor, final int orientation) {
        final DependencyNode startTarget = this.getTarget(startAnchor);
        final DependencyNode endTarget = this.getTarget(endAnchor);
        if (!startTarget.resolved || !endTarget.resolved) {
            return;
        }
        int startPos = startTarget.value + startAnchor.getMargin();
        int endPos = endTarget.value - endAnchor.getMargin();
        final int distance = endPos - startPos;
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            this.resolveDimension(orientation, distance);
        }
        if (!this.dimension.resolved) {
            return;
        }
        if (this.dimension.value == distance) {
            this.start.resolve(startPos);
            this.end.resolve(endPos);
            return;
        }
        float bias = (orientation == 0) ? this.widget.getHorizontalBiasPercent() : this.widget.getVerticalBiasPercent();
        if (startTarget == endTarget) {
            startPos = startTarget.value;
            endPos = endTarget.value;
            bias = 0.5f;
        }
        final int availableDistance = endPos - startPos - this.dimension.value;
        this.start.resolve((int)(0.5f + startPos + availableDistance * bias));
        this.end.resolve(this.start.value + this.dimension.value);
    }
    
    private void resolveDimension(final int orientation, final int distance) {
        switch (this.matchConstraintsType) {
            case 0: {
                this.dimension.resolve(this.getLimitedDimension(distance, orientation));
                break;
            }
            case 2: {
                final ConstraintWidget parent = this.widget.getParent();
                if (parent != null) {
                    final WidgetRun run = (orientation == 0) ? parent.horizontalRun : parent.verticalRun;
                    if (run.dimension.resolved) {
                        final float percent = (orientation == 0) ? this.widget.mMatchConstraintPercentWidth : this.widget.mMatchConstraintPercentHeight;
                        final int targetDimensionValue = run.dimension.value;
                        final int size = (int)(0.5f + targetDimensionValue * percent);
                        this.dimension.resolve(this.getLimitedDimension(size, orientation));
                    }
                }
                break;
            }
            case 1: {
                final int wrapValue = this.getLimitedDimension(this.dimension.wrapValue, orientation);
                this.dimension.resolve(Math.min(wrapValue, distance));
                break;
            }
            case 3: {
                if (this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.horizontalRun.matchConstraintsType == 3 && this.widget.verticalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.verticalRun.matchConstraintsType == 3) {
                    break;
                }
                final WidgetRun run2 = (orientation == 0) ? this.widget.verticalRun : this.widget.horizontalRun;
                if (run2.dimension.resolved) {
                    final float ratio = this.widget.getDimensionRatio();
                    int value;
                    if (orientation == 1) {
                        value = (int)(0.5f + run2.dimension.value / ratio);
                    }
                    else {
                        value = (int)(0.5f + ratio * run2.dimension.value);
                    }
                    this.dimension.resolve(value);
                    break;
                }
                break;
            }
        }
    }
    
    protected void updateRunStart(final Dependency dependency) {
    }
    
    protected void updateRunEnd(final Dependency dependency) {
    }
    
    @Override
    public void update(final Dependency dependency) {
    }
    
    protected final int getLimitedDimension(int dimension, final int orientation) {
        if (orientation == 0) {
            final int max = this.widget.mMatchConstraintMaxWidth;
            final int min = this.widget.mMatchConstraintMinWidth;
            int value = Math.max(min, dimension);
            if (max > 0) {
                value = Math.min(max, dimension);
            }
            if (value != dimension) {
                dimension = value;
            }
        }
        else {
            final int max = this.widget.mMatchConstraintMaxHeight;
            final int min = this.widget.mMatchConstraintMinHeight;
            int value = Math.max(min, dimension);
            if (max > 0) {
                value = Math.min(max, dimension);
            }
            if (value != dimension) {
                dimension = value;
            }
        }
        return dimension;
    }
    
    protected final DependencyNode getTarget(final ConstraintAnchor anchor, final int orientation) {
        if (anchor.mTarget == null) {
            return null;
        }
        DependencyNode target = null;
        final ConstraintWidget targetWidget = anchor.mTarget.mOwner;
        final WidgetRun run = (orientation == 0) ? targetWidget.horizontalRun : targetWidget.verticalRun;
        final ConstraintAnchor.Type targetType = anchor.mTarget.mType;
        switch (targetType) {
            case LEFT:
            case TOP: {
                target = run.start;
                break;
            }
            case RIGHT:
            case BOTTOM: {
                target = run.end;
                break;
            }
        }
        return target;
    }
    
    protected final void addTarget(final DependencyNode node, final DependencyNode target, final int margin) {
        node.targets.add(target);
        node.margin = margin;
        target.dependencies.add(node);
    }
    
    protected final void addTarget(final DependencyNode node, final DependencyNode target, final int marginFactor, final DimensionDependency dimensionDependency) {
        node.targets.add(target);
        node.targets.add(this.dimension);
        node.marginFactor = marginFactor;
        node.marginDependency = dimensionDependency;
        target.dependencies.add(node);
        dimensionDependency.dependencies.add(node);
    }
    
    public long getWrapDimension() {
        if (this.dimension.resolved) {
            return this.dimension.value;
        }
        return 0L;
    }
    
    public boolean isResolved() {
        return this.resolved;
    }
    
    enum RunType
    {
        NONE, 
        START, 
        END, 
        CENTER;
    }
}
