package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;
import com.smart.library.support.constraint.solver.widgets.Guideline;
import com.smart.library.support.constraint.solver.widgets.analyzer.*;

public class VirtualLayout extends HelperWidget
{
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private boolean mNeedsCallFromSolver;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    protected BasicMeasure.Measure mMeasure;
    
    public VirtualLayout() {
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mNeedsCallFromSolver = false;
        this.mMeasuredWidth = 0;
        this.mMeasuredHeight = 0;
        this.mMeasure = new BasicMeasure.Measure();
    }
    
    public void setPadding(final int value) {
        this.mPaddingLeft = value;
        this.mPaddingTop = value;
        this.mPaddingRight = value;
        this.mPaddingBottom = value;
    }
    
    public void setPaddingLeft(final int value) {
        this.mPaddingLeft = value;
    }
    
    public void setPaddingTop(final int value) {
        this.mPaddingTop = value;
    }
    
    public void setPaddingRight(final int value) {
        this.mPaddingRight = value;
    }
    
    public void setPaddingBottom(final int value) {
        this.mPaddingBottom = value;
    }
    
    public int getPaddingTop() {
        return this.mPaddingTop;
    }
    
    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }
    
    public int getPaddingLeft() {
        return this.mPaddingLeft;
    }
    
    public int getPaddingRight() {
        return this.mPaddingRight;
    }
    
    protected void needsCallbackFromSolver(final boolean value) {
        this.mNeedsCallFromSolver = value;
    }
    
    public boolean needSolverPass() {
        return this.mNeedsCallFromSolver;
    }
    
    public void measure(final int widthMode, final int widthSize, final int heightMode, final int heightSize) {
    }
    
    @Override
    public void updateConstraints(final ConstraintWidgetContainer container) {
        this.captureWidgets();
    }
    
    public void captureWidgets() {
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            final ConstraintWidget widget = this.mWidgets[i];
            if (widget != null) {
                widget.setInVirtualLayout(true);
            }
        }
    }
    
    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }
    
    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }
    
    public void setMeasure(final int width, final int height) {
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }
    
    protected boolean measureChildren() {
        BasicMeasure.Measurer measurer = null;
        if (this.mParent != null) {
            measurer = ((ConstraintWidgetContainer)this.mParent).getMeasurer();
        }
        if (measurer == null) {
            return false;
        }
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            final ConstraintWidget widget = this.mWidgets[i];
            if (widget != null) {
                if (!(widget instanceof Guideline)) {
                    DimensionBehaviour widthBehavior = widget.getDimensionBehaviour(0);
                    DimensionBehaviour heightBehavior = widget.getDimensionBehaviour(1);
                    final boolean skip = widthBehavior == DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultWidth != 1 && heightBehavior == DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultHeight != 1;
                    if (!skip) {
                        if (widthBehavior == DimensionBehaviour.MATCH_CONSTRAINT) {
                            widthBehavior = DimensionBehaviour.WRAP_CONTENT;
                        }
                        if (heightBehavior == DimensionBehaviour.MATCH_CONSTRAINT) {
                            heightBehavior = DimensionBehaviour.WRAP_CONTENT;
                        }
                        this.mMeasure.horizontalBehavior = widthBehavior;
                        this.mMeasure.verticalBehavior = heightBehavior;
                        this.mMeasure.horizontalDimension = widget.getWidth();
                        this.mMeasure.verticalDimension = widget.getHeight();
                        measurer.measure(widget, this.mMeasure);
                        widget.setWidth(this.mMeasure.measuredWidth);
                        widget.setHeight(this.mMeasure.measuredHeight);
                        widget.setBaselineDistance(this.mMeasure.measuredBaseline);
                    }
                }
            }
        }
        return true;
    }
}
