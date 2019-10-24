package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;

public class VerticalWidgetRun extends WidgetRun
{
    public DependencyNode baseline;
    DimensionDependency baselineDimension;
    
    public VerticalWidgetRun(final ConstraintWidget widget) {
        super(widget);
        this.baseline = new DependencyNode(this);
        this.baselineDimension = null;
        this.start.type = DependencyNode.Type.TOP;
        this.end.type = DependencyNode.Type.BOTTOM;
        this.baseline.type = DependencyNode.Type.BASELINE;
        this.orientation = 1;
    }
    
    @Override
    public String toString() {
        return "VerticalRun " + this.widget.getDebugName();
    }
    
    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.baseline.clear();
        this.dimension.clear();
        this.resolved = false;
    }
    
    @Override
    void reset() {
        this.resolved = false;
        this.start.resolved = false;
        this.end.resolved = false;
        this.baseline.resolved = false;
        this.dimension.resolved = false;
    }
    
    @Override
    boolean supportsWrapComputation() {
        return super.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || super.widget.mMatchConstraintDefaultHeight == 0;
    }
    
    @Override
    public void update(final Dependency dependency) {
        switch (this.mRunType) {
            case START: {
                this.updateRunStart(dependency);
                break;
            }
            case END: {
                this.updateRunEnd(dependency);
                break;
            }
            case CENTER: {
                this.updateRunCenter(dependency, this.widget.mTop, this.widget.mBottom, 1);
                return;
            }
        }
        if (this.dimension.readyToSolve && !this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            switch (this.widget.mMatchConstraintDefaultHeight) {
                case 3: {
                    if (this.widget.horizontalRun.dimension.resolved) {
                        int size = 0;
                        final int ratioSide = this.widget.getDimensionRatioSide();
                        switch (ratioSide) {
                            case 0: {
                                size = (int)(0.5f + this.widget.horizontalRun.dimension.value * this.widget.getDimensionRatio());
                                break;
                            }
                            case 1: {
                                size = (int)(0.5f + this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio());
                                break;
                            }
                            case -1: {
                                size = (int)(0.5f + this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio());
                                break;
                            }
                        }
                        this.dimension.resolve(size);
                        break;
                    }
                    break;
                }
                case 2: {
                    final ConstraintWidget parent = this.widget.getParent();
                    if (parent != null && parent.verticalRun.dimension.resolved) {
                        final float percent = this.widget.mMatchConstraintPercentHeight;
                        final int targetDimensionValue = parent.verticalRun.dimension.value;
                        final int size2 = (int)(0.5f + targetDimensionValue * percent);
                        this.dimension.resolve(size2);
                        break;
                    }
                    break;
                }
            }
        }
        if (!this.start.readyToSolve || !this.end.readyToSolve) {
            return;
        }
        if (this.start.resolved && this.end.resolved && this.dimension.resolved) {
            return;
        }
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInVerticalChain()) {
            final DependencyNode startTarget = this.start.targets.get(0);
            final DependencyNode endTarget = this.end.targets.get(0);
            final int startPos = startTarget.value + this.start.margin;
            final int endPos = endTarget.value + this.end.margin;
            final int distance = endPos - startPos;
            this.start.resolve(startPos);
            this.end.resolve(endPos);
            this.dimension.resolve(distance);
            return;
        }
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
            final DependencyNode startTarget = this.start.targets.get(0);
            final DependencyNode endTarget = this.end.targets.get(0);
            final int startPos = startTarget.value + this.start.margin;
            final int endPos = endTarget.value + this.end.margin;
            final int availableSpace = endPos - startPos;
            if (availableSpace < this.dimension.wrapValue) {
                this.dimension.resolve(availableSpace);
            }
            else {
                this.dimension.resolve(this.dimension.wrapValue);
            }
        }
        if (!this.dimension.resolved) {
            return;
        }
        if (this.start.targets.size() > 0 && this.end.targets.size() > 0) {
            final DependencyNode startTarget = this.start.targets.get(0);
            final DependencyNode endTarget = this.end.targets.get(0);
            int startPos = startTarget.value + this.start.margin;
            int endPos = endTarget.value + this.end.margin;
            float bias = this.widget.getVerticalBiasPercent();
            if (startTarget == endTarget) {
                startPos = startTarget.value;
                endPos = endTarget.value;
                bias = 0.5f;
            }
            final int distance2 = endPos - startPos - this.dimension.value;
            this.start.resolve((int)(0.5f + startPos + distance2 * bias));
            this.end.resolve(this.start.value + this.dimension.value);
        }
    }
    
    @Override
    void apply() {
        if (this.widget.measured) {
            this.dimension.resolve(this.widget.getHeight());
        }
        if (!this.dimension.resolved) {
            super.dimensionBehavior = this.widget.getVerticalDimensionBehaviour();
            if (this.widget.hasBaseline()) {
                this.baselineDimension = new BaselineDimensionDependency(this);
            }
            if (super.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                    final ConstraintWidget parent = this.widget.getParent();
                    if (parent != null && parent.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                        final int resolvedDimension = parent.getHeight() - this.widget.mTop.getMargin() - this.widget.mBottom.getMargin();
                        this.addTarget(this.start, parent.verticalRun.start, this.widget.mTop.getMargin());
                        this.addTarget(this.end, parent.verticalRun.end, -this.widget.mBottom.getMargin());
                        this.dimension.resolve(resolvedDimension);
                        return;
                    }
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getHeight());
                }
            }
        }
        else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final ConstraintWidget parent = this.widget.getParent();
            if (parent != null && parent.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                this.addTarget(this.start, parent.verticalRun.start, this.widget.mTop.getMargin());
                this.addTarget(this.end, parent.verticalRun.end, -this.widget.mBottom.getMargin());
                return;
            }
        }
        if (this.dimension.resolved && this.widget.measured) {
            if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
                if (this.widget.isInVerticalChain()) {
                    this.start.margin = this.widget.mListAnchors[2].getMargin();
                    this.end.margin = -this.widget.mListAnchors[3].getMargin();
                }
                else {
                    final DependencyNode startTarget = this.getTarget(this.widget.mListAnchors[2]);
                    if (startTarget != null) {
                        this.addTarget(this.start, startTarget, this.widget.mListAnchors[2].getMargin());
                    }
                    final DependencyNode endTarget = this.getTarget(this.widget.mListAnchors[3]);
                    if (endTarget != null) {
                        this.addTarget(this.end, endTarget, -this.widget.mListAnchors[3].getMargin());
                    }
                    this.start.delegateToWidgetRun = true;
                    this.end.delegateToWidgetRun = true;
                }
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                }
            }
            else if (this.widget.mListAnchors[2].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[2]);
                if (target != null) {
                    this.addTarget(this.start, target, this.widget.mListAnchors[2].getMargin());
                    this.addTarget(this.end, this.start, this.dimension.value);
                    if (this.widget.hasBaseline()) {
                        this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                    }
                }
            }
            else if (this.widget.mListAnchors[3].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[3]);
                if (target != null) {
                    this.addTarget(this.end, target, -this.widget.mListAnchors[3].getMargin());
                    this.addTarget(this.start, this.end, -this.dimension.value);
                }
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                }
            }
            else if (this.widget.mListAnchors[4].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[4]);
                if (target != null) {
                    this.addTarget(this.baseline, target, 0);
                    this.addTarget(this.start, this.baseline, -this.widget.getBaselineDistance());
                    this.addTarget(this.end, this.start, this.dimension.value);
                }
            }
            else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor(ConstraintAnchor.Type.CENTER).mTarget == null) {
                final DependencyNode top = this.widget.getParent().verticalRun.start;
                this.addTarget(this.start, top, this.widget.getY());
                this.addTarget(this.end, this.start, this.dimension.value);
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                }
            }
        }
        else {
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                switch (this.widget.mMatchConstraintDefaultHeight) {
                    case 3: {
                        if (this.widget.isInVerticalChain()) {
                            break;
                        }
                        if (this.widget.mMatchConstraintDefaultWidth == 3) {
                            break;
                        }
                        final DependencyNode targetDimension = this.widget.horizontalRun.dimension;
                        this.dimension.targets.add(targetDimension);
                        targetDimension.dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                        break;
                    }
                    case 2: {
                        final ConstraintWidget parent = this.widget.getParent();
                        if (parent == null) {
                            break;
                        }
                        final DependencyNode targetDimension2 = parent.verticalRun.dimension;
                        this.dimension.targets.add(targetDimension2);
                        targetDimension2.dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                        break;
                    }
                }
            }
            else {
                this.dimension.addDependency(this);
            }
            if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
                if (this.widget.isInVerticalChain()) {
                    this.start.margin = this.widget.mListAnchors[2].getMargin();
                    this.end.margin = -this.widget.mListAnchors[3].getMargin();
                }
                else {
                    final DependencyNode startTarget = this.getTarget(this.widget.mListAnchors[2]);
                    final DependencyNode endTarget = this.getTarget(this.widget.mListAnchors[3]);
                    startTarget.addDependency(this);
                    endTarget.addDependency(this);
                    this.mRunType = RunType.CENTER;
                }
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
            }
            else if (this.widget.mListAnchors[2].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[2]);
                if (target != null) {
                    this.addTarget(this.start, target, this.widget.mListAnchors[2].getMargin());
                    this.addTarget(this.end, this.start, 1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                    if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                        this.dimension.targets.add(this.widget.horizontalRun.dimension);
                        this.dimension.updateDelegate = this;
                    }
                }
            }
            else if (this.widget.mListAnchors[3].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[3]);
                if (target != null) {
                    this.addTarget(this.end, target, -this.widget.mListAnchors[3].getMargin());
                    this.addTarget(this.start, this.end, -1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                }
            }
            else if (this.widget.mListAnchors[4].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[4]);
                if (target != null) {
                    this.addTarget(this.baseline, target, 0);
                    this.addTarget(this.start, this.baseline, -1, this.baselineDimension);
                    this.addTarget(this.end, this.start, 1, this.dimension);
                }
            }
            else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                final DependencyNode top = this.widget.getParent().verticalRun.start;
                this.addTarget(this.start, top, this.widget.getY());
                this.addTarget(this.end, this.start, 1, this.dimension);
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                    this.dimension.targets.add(this.widget.horizontalRun.dimension);
                    this.dimension.updateDelegate = this;
                }
            }
            if (this.dimension.targets.size() == 0) {
                this.dimension.readyToSolve = true;
            }
        }
    }
    
    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setY(this.start.value);
        }
    }
}
