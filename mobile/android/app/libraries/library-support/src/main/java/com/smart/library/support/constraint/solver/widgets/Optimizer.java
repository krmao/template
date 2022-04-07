package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

public class Optimizer
{
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_GROUPS = 32;
    public static final int OPTIMIZATION_GRAPH = 64;
    public static final int OPTIMIZATION_GRAPH_WRAP = 128;
    public static final int OPTIMIZATION_STANDARD = 7;
    static boolean[] flags;
    static final int FLAG_USE_OPTIMIZE = 0;
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    
    static void checkMatchParent(final ConstraintWidgetContainer container, final LinearSystem system, final ConstraintWidget widget) {
        widget.mHorizontalResolution = -1;
        widget.mVerticalResolution = -1;
        if (container.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int left = widget.mLeft.mMargin;
            final int right = container.getWidth() - widget.mRight.mMargin;
            widget.mLeft.mSolverVariable = system.createObjectVariable(widget.mLeft);
            widget.mRight.mSolverVariable = system.createObjectVariable(widget.mRight);
            system.addEquality(widget.mLeft.mSolverVariable, left);
            system.addEquality(widget.mRight.mSolverVariable, right);
            widget.mHorizontalResolution = 2;
            widget.setHorizontalDimension(left, right);
        }
        if (container.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && widget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int top = widget.mTop.mMargin;
            final int bottom = container.getHeight() - widget.mBottom.mMargin;
            widget.mTop.mSolverVariable = system.createObjectVariable(widget.mTop);
            widget.mBottom.mSolverVariable = system.createObjectVariable(widget.mBottom);
            system.addEquality(widget.mTop.mSolverVariable, top);
            system.addEquality(widget.mBottom.mSolverVariable, bottom);
            if (widget.mBaselineDistance > 0 || widget.getVisibility() == 8) {
                system.addEquality(widget.mBaseline.mSolverVariable = system.createObjectVariable(widget.mBaseline), top + widget.mBaselineDistance);
            }
            widget.mVerticalResolution = 2;
            widget.setVerticalDimension(top, bottom);
        }
    }
    
    public static final boolean enabled(final int optimizationLevel, final int optimization) {
        return (optimizationLevel & optimization) == optimization;
    }
    
    static {
        Optimizer.flags = new boolean[3];
    }
}
