package com.smart.library.support.constraint.solver.state;

import com.smart.library.support.constraint.solver.widgets.*;

public interface Reference
{
    ConstraintWidget getConstraintWidget();
    
    void setConstraintWidget(final ConstraintWidget p0);
    
    void setKey(final Object p0);
    
    Object getKey();
    
    void apply();
}
