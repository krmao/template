package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

public interface Helper
{
    void updateConstraints(final ConstraintWidgetContainer p0);
    
    void add(final ConstraintWidget p0);
    
    void removeAllIds();
}
