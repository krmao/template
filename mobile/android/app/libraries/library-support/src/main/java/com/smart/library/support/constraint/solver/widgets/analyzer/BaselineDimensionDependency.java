package com.smart.library.support.constraint.solver.widgets.analyzer;

class BaselineDimensionDependency extends DimensionDependency
{
    public BaselineDimensionDependency(final WidgetRun run) {
        super(run);
    }
    
    public void update(final DependencyNode node) {
        final VerticalWidgetRun vrun = (VerticalWidgetRun)this.run;
        vrun.baseline.margin = this.run.widget.getBaselineDistance();
        this.resolved = true;
    }
}
