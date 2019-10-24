package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;

class GuidelineReference extends WidgetRun
{
    public GuidelineReference(final ConstraintWidget widget) {
        super(widget);
        widget.horizontalRun.clear();
        widget.verticalRun.clear();
        this.orientation = ((Guideline)widget).getOrientation();
    }
    
    @Override
    void clear() {
        this.start.clear();
    }
    
    @Override
    void reset() {
        this.start.resolved = false;
        this.end.resolved = false;
    }
    
    @Override
    boolean supportsWrapComputation() {
        return false;
    }
    
    private void addDependency(final DependencyNode node) {
        this.start.dependencies.add(node);
        node.targets.add(this.start);
    }
    
    @Override
    public void update(final Dependency dependency) {
        if (!this.start.readyToSolve) {
            return;
        }
        if (this.start.resolved) {
            return;
        }
        final DependencyNode startTarget = this.start.targets.get(0);
        final Guideline guideline = (Guideline)this.widget;
        final int startPos = (int)(0.5f + startTarget.value * guideline.getRelativePercent());
        this.start.resolve(startPos);
    }
    
    @Override
    void apply() {
        final Guideline guideline = (Guideline)this.widget;
        final int relativeBegin = guideline.getRelativeBegin();
        final int relativeEnd = guideline.getRelativeEnd();
        final float percent = guideline.getRelativePercent();
        if (guideline.getOrientation() == 1) {
            if (relativeBegin != -1) {
                this.start.targets.add(this.widget.mParent.horizontalRun.start);
                this.widget.mParent.horizontalRun.start.dependencies.add(this.start);
                this.start.margin = relativeBegin;
            }
            else if (relativeEnd != -1) {
                this.start.targets.add(this.widget.mParent.horizontalRun.end);
                this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
                this.start.margin = -relativeEnd;
            }
            else {
                this.start.delegateToWidgetRun = true;
                this.start.targets.add(this.widget.mParent.horizontalRun.end);
                this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
            }
            this.addDependency(this.widget.horizontalRun.start);
            this.addDependency(this.widget.horizontalRun.end);
        }
        else {
            if (relativeBegin != -1) {
                this.start.targets.add(this.widget.mParent.verticalRun.start);
                this.widget.mParent.verticalRun.start.dependencies.add(this.start);
                this.start.margin = relativeBegin;
            }
            else if (relativeEnd != -1) {
                this.start.targets.add(this.widget.mParent.verticalRun.end);
                this.widget.mParent.verticalRun.end.dependencies.add(this.start);
                this.start.margin = -relativeEnd;
            }
            else {
                this.start.delegateToWidgetRun = true;
                this.start.targets.add(this.widget.mParent.verticalRun.end);
                this.widget.mParent.verticalRun.end.dependencies.add(this.start);
            }
            this.addDependency(this.widget.verticalRun.start);
            this.addDependency(this.widget.verticalRun.end);
        }
    }
    
    public void applyToWidget() {
        final Guideline guideline = (Guideline)this.widget;
        if (guideline.getOrientation() == 1) {
            this.widget.setX(this.start.value);
        }
        else {
            this.widget.setY(this.start.value);
        }
    }
}
