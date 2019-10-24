package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;

public class HorizontalWidgetRun extends WidgetRun
{
    private static int[] tempDimensions;
    
    public HorizontalWidgetRun(final ConstraintWidget widget) {
        super(widget);
        this.start.type = DependencyNode.Type.LEFT;
        this.end.type = DependencyNode.Type.RIGHT;
        this.orientation = 0;
    }
    
    @Override
    public String toString() {
        return "HorizontalRun " + this.widget.getDebugName();
    }
    
    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.dimension.clear();
        this.resolved = false;
    }
    
    @Override
    void reset() {
        this.resolved = false;
        this.start.resolved = false;
        this.end.resolved = false;
        this.dimension.resolved = false;
    }
    
    @Override
    boolean supportsWrapComputation() {
        return super.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || super.widget.mMatchConstraintDefaultWidth == 0;
    }
    
    @Override
    void apply() {
        if (this.widget.measured) {
            this.dimension.resolve(this.widget.getWidth());
        }
        if (!this.dimension.resolved) {
            super.dimensionBehavior = this.widget.getHorizontalDimensionBehaviour();
            if (super.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                    final ConstraintWidget parent = this.widget.getParent();
                    if ((parent != null && parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                        final int resolvedDimension = parent.getWidth() - this.widget.mLeft.getMargin() - this.widget.mRight.getMargin();
                        this.addTarget(this.start, parent.horizontalRun.start, this.widget.mLeft.getMargin());
                        this.addTarget(this.end, parent.horizontalRun.end, -this.widget.mRight.getMargin());
                        this.dimension.resolve(resolvedDimension);
                        return;
                    }
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getWidth());
                }
            }
        }
        else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final ConstraintWidget parent = this.widget.getParent();
            if ((parent != null && parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) || parent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                this.addTarget(this.start, parent.horizontalRun.start, this.widget.mLeft.getMargin());
                this.addTarget(this.end, parent.horizontalRun.end, -this.widget.mRight.getMargin());
                return;
            }
        }
        if (this.dimension.resolved && this.widget.measured) {
            if (this.widget.mListAnchors[0].mTarget != null && this.widget.mListAnchors[1].mTarget != null) {
                if (this.widget.isInHorizontalChain()) {
                    this.start.margin = this.widget.mListAnchors[0].getMargin();
                    this.end.margin = -this.widget.mListAnchors[1].getMargin();
                }
                else {
                    final DependencyNode startTarget = this.getTarget(this.widget.mListAnchors[0]);
                    if (startTarget != null) {
                        this.addTarget(this.start, startTarget, this.widget.mListAnchors[0].getMargin());
                    }
                    final DependencyNode endTarget = this.getTarget(this.widget.mListAnchors[1]);
                    if (endTarget != null) {
                        this.addTarget(this.end, endTarget, -this.widget.mListAnchors[1].getMargin());
                    }
                    this.start.delegateToWidgetRun = true;
                    this.end.delegateToWidgetRun = true;
                }
            }
            else if (this.widget.mListAnchors[0].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[0]);
                if (target != null) {
                    this.addTarget(this.start, target, this.widget.mListAnchors[0].getMargin());
                    this.addTarget(this.end, this.start, this.dimension.value);
                }
            }
            else if (this.widget.mListAnchors[1].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[1]);
                if (target != null) {
                    this.addTarget(this.end, target, -this.widget.mListAnchors[1].getMargin());
                    this.addTarget(this.start, this.end, -this.dimension.value);
                }
            }
            else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor(ConstraintAnchor.Type.CENTER).mTarget == null) {
                final DependencyNode left = this.widget.getParent().horizontalRun.start;
                this.addTarget(this.start, left, this.widget.getX());
                this.addTarget(this.end, this.start, this.dimension.value);
            }
        }
        else {
            if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                switch (this.widget.mMatchConstraintDefaultWidth) {
                    case 3: {
                        if (this.widget.mMatchConstraintDefaultHeight != 3) {
                            final DependencyNode targetDimension = this.widget.verticalRun.dimension;
                            this.dimension.targets.add(targetDimension);
                            targetDimension.dependencies.add(this.dimension);
                            this.widget.verticalRun.start.dependencies.add(this.dimension);
                            this.widget.verticalRun.end.dependencies.add(this.dimension);
                            this.dimension.delegateToWidgetRun = true;
                            this.dimension.dependencies.add(this.start);
                            this.dimension.dependencies.add(this.end);
                            this.start.targets.add(this.dimension);
                            this.end.targets.add(this.dimension);
                            break;
                        }
                        this.start.updateDelegate = this;
                        this.end.updateDelegate = this;
                        this.widget.verticalRun.start.updateDelegate = this;
                        this.widget.verticalRun.end.updateDelegate = this;
                        this.dimension.updateDelegate = this;
                        if (this.widget.isInVerticalChain()) {
                            this.dimension.targets.add(this.widget.verticalRun.dimension);
                            this.widget.verticalRun.dimension.dependencies.add(this.dimension);
                            this.widget.verticalRun.dimension.updateDelegate = this;
                            this.dimension.targets.add(this.widget.verticalRun.start);
                            this.dimension.targets.add(this.widget.verticalRun.end);
                            this.widget.verticalRun.start.dependencies.add(this.dimension);
                            this.widget.verticalRun.end.dependencies.add(this.dimension);
                            break;
                        }
                        if (this.widget.isInHorizontalChain()) {
                            this.widget.verticalRun.dimension.targets.add(this.dimension);
                            this.dimension.dependencies.add(this.widget.verticalRun.dimension);
                            break;
                        }
                        this.widget.verticalRun.dimension.targets.add(this.dimension);
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
            if (this.widget.mListAnchors[0].mTarget != null && this.widget.mListAnchors[1].mTarget != null) {
                if (this.widget.isInHorizontalChain()) {
                    this.start.margin = this.widget.mListAnchors[0].getMargin();
                    this.end.margin = -this.widget.mListAnchors[1].getMargin();
                }
                else {
                    final DependencyNode startTarget = this.getTarget(this.widget.mListAnchors[0]);
                    final DependencyNode endTarget = this.getTarget(this.widget.mListAnchors[1]);
                    startTarget.addDependency(this);
                    endTarget.addDependency(this);
                    this.mRunType = RunType.CENTER;
                }
            }
            else if (this.widget.mListAnchors[0].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[0]);
                if (target != null) {
                    this.addTarget(this.start, target, this.widget.mListAnchors[0].getMargin());
                    this.addTarget(this.end, this.start, 1, this.dimension);
                }
            }
            else if (this.widget.mListAnchors[1].mTarget != null) {
                final DependencyNode target = this.getTarget(this.widget.mListAnchors[1]);
                if (target != null) {
                    this.addTarget(this.end, target, -this.widget.mListAnchors[1].getMargin());
                    this.addTarget(this.start, this.end, -1, this.dimension);
                }
            }
            else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                final DependencyNode left = this.widget.getParent().horizontalRun.start;
                this.addTarget(this.start, left, this.widget.getX());
                this.addTarget(this.end, this.start, 1, this.dimension);
            }
        }
    }
    
    private void computeInsetRatio(final int[] dimensions, final int x1, final int x2, final int y1, final int y2, final float ratio, final int side) {
        final int dx = x2 - x1;
        final int dy = y2 - y1;
        switch (side) {
            case -1: {
                final int candidateX1 = (int)(0.5f + dy * ratio);
                final int candidateY1 = dy;
                final int candidateX2 = dx;
                final int candidateY2 = (int)(0.5f + dx / ratio);
                if (candidateX1 <= dx && candidateY1 <= dy) {
                    dimensions[0] = candidateX1;
                    dimensions[1] = candidateY1;
                }
                else if (candidateX2 <= dx && candidateY2 <= dy) {
                    dimensions[0] = candidateX2;
                    dimensions[1] = candidateY2;
                }
                break;
            }
            case 0: {
                final int horizontalSide = (int)(0.5f + dy * ratio);
                dimensions[0] = horizontalSide;
                dimensions[1] = dy;
                break;
            }
            case 1: {
                final int verticalSide = (int)(0.5f + dx * ratio);
                dimensions[0] = dx;
                dimensions[1] = verticalSide;
                break;
            }
        }
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
                this.updateRunCenter(dependency, this.widget.mLeft, this.widget.mRight, 0);
                return;
            }
        }
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            switch (this.widget.mMatchConstraintDefaultWidth) {
                case 3: {
                    if (this.widget.mMatchConstraintDefaultHeight == 0 || this.widget.mMatchConstraintDefaultHeight == 3) {
                        final DependencyNode secondStart = this.widget.verticalRun.start;
                        final DependencyNode secondEnd = this.widget.verticalRun.end;
                        final boolean s1 = this.widget.mLeft.mTarget != null;
                        final boolean s2 = this.widget.mTop.mTarget != null;
                        final boolean e1 = this.widget.mRight.mTarget != null;
                        final boolean e2 = this.widget.mBottom.mTarget != null;
                        final int definedSide = this.widget.getDimensionRatioSide();
                        if (s1 && s2 && e1 && e2) {
                            final float ratio = this.widget.getDimensionRatio();
                            if (secondStart.resolved && secondEnd.resolved) {
                                if (!this.start.readyToSolve || !this.end.readyToSolve) {
                                    return;
                                }
                                final int x1 = this.start.targets.get(0).value + this.start.margin;
                                final int x2 = this.end.targets.get(0).value - this.end.margin;
                                final int y1 = secondStart.value + secondStart.margin;
                                final int y2 = secondEnd.value - secondEnd.margin;
                                this.computeInsetRatio(HorizontalWidgetRun.tempDimensions, x1, x2, y1, y2, ratio, definedSide);
                                this.dimension.resolve(HorizontalWidgetRun.tempDimensions[0]);
                                this.widget.verticalRun.dimension.resolve(HorizontalWidgetRun.tempDimensions[1]);
                                return;
                            }
                            else {
                                if (this.start.resolved && this.end.resolved) {
                                    if (!secondStart.readyToSolve || !secondEnd.readyToSolve) {
                                        return;
                                    }
                                    final int x1 = this.start.value + this.start.margin;
                                    final int x2 = this.end.value - this.end.margin;
                                    final int y1 = secondStart.targets.get(0).value + secondStart.margin;
                                    final int y2 = secondEnd.targets.get(0).value - secondEnd.margin;
                                    this.computeInsetRatio(HorizontalWidgetRun.tempDimensions, x1, x2, y1, y2, ratio, definedSide);
                                    this.dimension.resolve(HorizontalWidgetRun.tempDimensions[0]);
                                    this.widget.verticalRun.dimension.resolve(HorizontalWidgetRun.tempDimensions[1]);
                                }
                                if (!this.start.readyToSolve || !this.end.readyToSolve || !secondStart.readyToSolve || !secondEnd.readyToSolve) {
                                    return;
                                }
                                final int x1 = this.start.targets.get(0).value + this.start.margin;
                                final int x2 = this.end.targets.get(0).value - this.end.margin;
                                final int y1 = secondStart.targets.get(0).value + secondStart.margin;
                                final int y2 = secondEnd.targets.get(0).value - secondEnd.margin;
                                this.computeInsetRatio(HorizontalWidgetRun.tempDimensions, x1, x2, y1, y2, ratio, definedSide);
                                this.dimension.resolve(HorizontalWidgetRun.tempDimensions[0]);
                                this.widget.verticalRun.dimension.resolve(HorizontalWidgetRun.tempDimensions[1]);
                            }
                        }
                        else if (s1 && e1) {
                            if (!this.start.readyToSolve || !this.end.readyToSolve) {
                                return;
                            }
                            final float ratio = this.widget.getDimensionRatio();
                            final int x1 = this.start.targets.get(0).value + this.start.margin;
                            final int x2 = this.end.targets.get(0).value - this.end.margin;
                            switch (definedSide) {
                                case -1:
                                case 0: {
                                    final int dx = x2 - x1;
                                    int ldx = this.getLimitedDimension(dx, 0);
                                    final int dy = (int)(0.5f + ldx * ratio);
                                    final int ldy = this.getLimitedDimension(dy, 1);
                                    if (dy != ldy) {
                                        ldx = (int)(0.5f + ldy / ratio);
                                    }
                                    this.dimension.resolve(ldx);
                                    this.widget.verticalRun.dimension.resolve(ldy);
                                    break;
                                }
                                case 1: {
                                    final int dx = x2 - x1;
                                    int ldx = this.getLimitedDimension(dx, 0);
                                    final int dy = (int)(0.5f + ldx / ratio);
                                    final int ldy = this.getLimitedDimension(dy, 1);
                                    if (dy != ldy) {
                                        ldx = (int)(0.5f + ldy * ratio);
                                    }
                                    this.dimension.resolve(ldx);
                                    this.widget.verticalRun.dimension.resolve(ldy);
                                    break;
                                }
                            }
                        }
                        else if (s2 && e2) {
                            if (!secondStart.readyToSolve || !secondEnd.readyToSolve) {
                                return;
                            }
                            final float ratio = this.widget.getDimensionRatio();
                            final int y3 = secondStart.targets.get(0).value + secondStart.margin;
                            final int y4 = secondEnd.targets.get(0).value - secondEnd.margin;
                            switch (definedSide) {
                                case -1:
                                case 1: {
                                    final int dy2 = y4 - y3;
                                    int ldy2 = this.getLimitedDimension(dy2, 1);
                                    final int dx2 = (int)(0.5f + ldy2 / ratio);
                                    final int ldx2 = this.getLimitedDimension(dx2, 0);
                                    if (dx2 != ldx2) {
                                        ldy2 = (int)(0.5f + ldx2 * ratio);
                                    }
                                    this.dimension.resolve(ldx2);
                                    this.widget.verticalRun.dimension.resolve(ldy2);
                                    break;
                                }
                                case 0: {
                                    final int dy2 = y4 - y3;
                                    int ldy2 = this.getLimitedDimension(dy2, 1);
                                    final int dx2 = (int)(0.5f + ldy2 * ratio);
                                    final int ldx2 = this.getLimitedDimension(dx2, 0);
                                    if (dx2 != ldx2) {
                                        ldy2 = (int)(0.5f + ldx2 / ratio);
                                    }
                                    this.dimension.resolve(ldx2);
                                    this.widget.verticalRun.dimension.resolve(ldy2);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    int size = 0;
                    final int ratioSide = this.widget.getDimensionRatioSide();
                    switch (ratioSide) {
                        case 0: {
                            size = (int)(0.5f + this.widget.verticalRun.dimension.value / this.widget.getDimensionRatio());
                            break;
                        }
                        case 1: {
                            size = (int)(0.5f + this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio());
                            break;
                        }
                        case -1: {
                            size = (int)(0.5f + this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio());
                            break;
                        }
                    }
                    this.dimension.resolve(size);
                    break;
                }
                case 2: {
                    final ConstraintWidget parent = this.widget.getParent();
                    if (parent != null && parent.horizontalRun.dimension.resolved) {
                        final float percent = this.widget.mMatchConstraintPercentWidth;
                        final int targetDimensionValue = parent.horizontalRun.dimension.value;
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
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInHorizontalChain()) {
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
            int value = Math.min(availableSpace, this.dimension.wrapValue);
            final int max = this.widget.mMatchConstraintMaxWidth;
            final int min = this.widget.mMatchConstraintMinWidth;
            value = Math.max(min, value);
            if (max > 0) {
                value = Math.min(max, value);
            }
            this.dimension.resolve(value);
        }
        if (!this.dimension.resolved) {
            return;
        }
        final DependencyNode startTarget = this.start.targets.get(0);
        final DependencyNode endTarget = this.end.targets.get(0);
        int startPos = startTarget.value + this.start.margin;
        int endPos = endTarget.value + this.end.margin;
        float bias = this.widget.getHorizontalBiasPercent();
        if (startTarget == endTarget) {
            startPos = startTarget.value;
            endPos = endTarget.value;
            bias = 0.5f;
        }
        final int distance2 = endPos - startPos - this.dimension.value;
        this.start.resolve((int)(0.5f + startPos + distance2 * bias));
        this.end.resolve(this.start.value + this.dimension.value);
    }
    
    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setX(this.start.value);
        }
    }
    
    static {
        HorizontalWidgetRun.tempDimensions = new int[2];
    }
}
