package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;
import java.util.*;

class HelperReferences extends WidgetRun
{
    public HelperReferences(final ConstraintWidget widget) {
        super(widget);
    }
    
    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
    }
    
    @Override
    void reset() {
        this.start.resolved = false;
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
    void apply() {
        if (this.widget instanceof Barrier) {
            this.start.delegateToWidgetRun = true;
            final Barrier barrier = (Barrier)this.widget;
            final int type = barrier.getBarrierType();
            final boolean allowsGoneWidget = barrier.allowsGoneWidget();
            switch (type) {
                case 0: {
                    this.start.type = DependencyNode.Type.LEFT;
                    for (int i = 0; i < barrier.mWidgetsCount; ++i) {
                        final ConstraintWidget refwidget = barrier.mWidgets[i];
                        if (allowsGoneWidget || refwidget.getVisibility() != 8) {
                            final DependencyNode target = refwidget.horizontalRun.start;
                            target.dependencies.add(this.start);
                            this.start.targets.add(target);
                        }
                    }
                    this.addDependency(this.widget.horizontalRun.start);
                    this.addDependency(this.widget.horizontalRun.end);
                    break;
                }
                case 1: {
                    this.start.type = DependencyNode.Type.RIGHT;
                    for (int i = 0; i < barrier.mWidgetsCount; ++i) {
                        final ConstraintWidget refwidget = barrier.mWidgets[i];
                        if (allowsGoneWidget || refwidget.getVisibility() != 8) {
                            final DependencyNode target = refwidget.horizontalRun.end;
                            target.dependencies.add(this.start);
                            this.start.targets.add(target);
                        }
                    }
                    this.addDependency(this.widget.horizontalRun.start);
                    this.addDependency(this.widget.horizontalRun.end);
                    break;
                }
                case 2: {
                    this.start.type = DependencyNode.Type.TOP;
                    for (int i = 0; i < barrier.mWidgetsCount; ++i) {
                        final ConstraintWidget refwidget = barrier.mWidgets[i];
                        if (allowsGoneWidget || refwidget.getVisibility() != 8) {
                            final DependencyNode target = refwidget.verticalRun.start;
                            target.dependencies.add(this.start);
                            this.start.targets.add(target);
                        }
                    }
                    this.addDependency(this.widget.verticalRun.start);
                    this.addDependency(this.widget.verticalRun.end);
                    break;
                }
                case 3: {
                    this.start.type = DependencyNode.Type.BOTTOM;
                    for (int i = 0; i < barrier.mWidgetsCount; ++i) {
                        final ConstraintWidget refwidget = barrier.mWidgets[i];
                        if (allowsGoneWidget || refwidget.getVisibility() != 8) {
                            final DependencyNode target = refwidget.verticalRun.end;
                            target.dependencies.add(this.start);
                            this.start.targets.add(target);
                        }
                    }
                    this.addDependency(this.widget.verticalRun.start);
                    this.addDependency(this.widget.verticalRun.end);
                    break;
                }
            }
        }
    }
    
    @Override
    public void update(final Dependency dependency) {
        final Barrier barrier = (Barrier)this.widget;
        final int type = barrier.getBarrierType();
        int min = -1;
        int max = 0;
        for (final DependencyNode node : this.start.targets) {
            final int value = node.value;
            if (min == -1 || value < min) {
                min = value;
            }
            if (max < value) {
                max = value;
            }
        }
        if (type == 0 || type == 2) {
            this.start.resolve(min + barrier.getMargin());
        }
        else {
            this.start.resolve(max + barrier.getMargin());
        }
    }
    
    public void applyToWidget() {
        if (this.widget instanceof Barrier) {
            final Barrier barrier = (Barrier)this.widget;
            final int type = barrier.getBarrierType();
            if (type == 0 || type == 1) {
                this.widget.setX(this.start.value);
            }
            else {
                this.widget.setY(this.start.value);
            }
        }
    }
}
