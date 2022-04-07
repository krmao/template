package com.smart.library.support.constraint.solver.widgets;

import java.util.*;
import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;

public class Barrier extends HelperWidget
{
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    private int mBarrierType;
    private boolean mAllowsGoneWidget;
    private int mMargin;
    
    public Barrier() {
        this.mBarrierType = 0;
        this.mAllowsGoneWidget = true;
        this.mMargin = 0;
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    public int getBarrierType() {
        return this.mBarrierType;
    }
    
    public void setBarrierType(final int barrierType) {
        this.mBarrierType = barrierType;
    }
    
    public void setAllowsGoneWidget(final boolean allowsGoneWidget) {
        this.mAllowsGoneWidget = allowsGoneWidget;
    }
    
    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }
    
    @Override
    public void copy(final ConstraintWidget src, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        final Barrier srcBarrier = (Barrier)src;
        this.mBarrierType = srcBarrier.mBarrierType;
        this.mAllowsGoneWidget = srcBarrier.mAllowsGoneWidget;
        this.mMargin = srcBarrier.mMargin;
    }
    
    @Override
    public void addToSolver(final LinearSystem system) {
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i = 0; i < this.mListAnchors.length; ++i) {
            this.mListAnchors[i].mSolverVariable = system.createObjectVariable(this.mListAnchors[i]);
        }
        if (this.mBarrierType >= 0 && this.mBarrierType < 4) {
            final ConstraintAnchor position = this.mListAnchors[this.mBarrierType];
            boolean hasMatchConstraintWidgets = false;
            for (int j = 0; j < this.mWidgetsCount; ++j) {
                final ConstraintWidget widget = this.mWidgets[j];
                if (this.mAllowsGoneWidget || widget.allowedInBarrier()) {
                    if ((this.mBarrierType == 0 || this.mBarrierType == 1) && widget.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        hasMatchConstraintWidgets = true;
                        break;
                    }
                    if ((this.mBarrierType == 2 || this.mBarrierType == 3) && widget.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        hasMatchConstraintWidgets = true;
                        break;
                    }
                }
            }
            if (this.mBarrierType == 0 || this.mBarrierType == 1) {
                if (this.getParent().getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
                    hasMatchConstraintWidgets = false;
                }
            }
            else if (this.getParent().getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
                hasMatchConstraintWidgets = false;
            }
            for (int j = 0; j < this.mWidgetsCount; ++j) {
                final ConstraintWidget widget = this.mWidgets[j];
                if (this.mAllowsGoneWidget || widget.allowedInBarrier()) {
                    final SolverVariable target = system.createObjectVariable(widget.mListAnchors[this.mBarrierType]);
                    widget.mListAnchors[this.mBarrierType].mSolverVariable = target;
                    if (this.mBarrierType == 0 || this.mBarrierType == 2) {
                        system.addLowerBarrier(position.mSolverVariable, target, this.mMargin, hasMatchConstraintWidgets);
                    }
                    else {
                        system.addGreaterBarrier(position.mSolverVariable, target, this.mMargin, hasMatchConstraintWidgets);
                    }
                }
            }
            if (this.mBarrierType == 0) {
                system.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
                if (!hasMatchConstraintWidgets) {
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
                }
            }
            else if (this.mBarrierType == 1) {
                system.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
                if (!hasMatchConstraintWidgets) {
                    system.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
                }
            }
            else if (this.mBarrierType == 2) {
                system.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
                if (!hasMatchConstraintWidgets) {
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
                }
            }
            else if (this.mBarrierType == 3) {
                system.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
                if (!hasMatchConstraintWidgets) {
                    system.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
                }
            }
        }
    }
    
    public void setMargin(final int margin) {
        this.mMargin = margin;
    }
    
    public int getMargin() {
        return this.mMargin;
    }
}
