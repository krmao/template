package com.smart.library.support.constraint.solver.widgets.analyzer;

import java.util.*;

class DimensionDependency extends DependencyNode
{
    public int wrapValue;
    
    public DimensionDependency(final WidgetRun run) {
        super(run);
        if (run instanceof HorizontalWidgetRun) {
            this.type = Type.HORIZONTAL_DIMENSION;
        }
        else {
            this.type = Type.VERTICAL_DIMENSION;
        }
    }
    
    @Override
    public void resolve(final int value) {
        if (this.resolved) {
            return;
        }
        this.resolved = true;
        this.value = value;
        for (final Dependency node : this.dependencies) {
            node.update(node);
        }
    }
}
