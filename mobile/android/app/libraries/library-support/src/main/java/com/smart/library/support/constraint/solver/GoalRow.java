package com.smart.library.support.constraint.solver;

import com.smart.library.support.constraint.solver.ArrayRow;
import com.smart.library.support.constraint.solver.Cache;
import com.smart.library.support.constraint.solver.SolverVariable;

public class GoalRow extends ArrayRow
{
    public GoalRow(final Cache cache) {
        super(cache);
    }
    
    @Override
    public void addError(final SolverVariable error) {
        super.addError(error);
        --error.usageInRowCount;
    }
}
