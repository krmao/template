package com.smart.library.support.constraint.solver.state.helpers;

import com.smart.library.support.constraint.solver.state.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class GuidelineReference implements Reference
{
    final State mState;
    private int mOrientation;
    private Guideline mGuidelineWidget;
    private int mStart;
    private int mEnd;
    private float mPercent;
    private Object key;
    
    @Override
    public void setKey(final Object key) {
        this.key = key;
    }
    
    @Override
    public Object getKey() {
        return this.key;
    }
    
    public GuidelineReference(final State state) {
        this.mStart = -1;
        this.mEnd = -1;
        this.mPercent = 0.0f;
        this.mState = state;
    }
    
    public void start(final Object margin) {
        this.mStart = this.mState.convertDimension(margin);
        this.mEnd = -1;
        this.mPercent = 0.0f;
    }
    
    public void end(final Object margin) {
        this.mStart = -1;
        this.mEnd = this.mState.convertDimension(margin);
        this.mPercent = 0.0f;
    }
    
    public void percent(final float percent) {
        this.mStart = -1;
        this.mEnd = -1;
        this.mPercent = percent;
    }
    
    public void setOrientation(final int orientation) {
        this.mOrientation = orientation;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    @Override
    public void apply() {
        this.mGuidelineWidget.setOrientation(this.mOrientation);
        if (this.mStart != -1) {
            this.mGuidelineWidget.setGuideBegin(this.mStart);
        }
        else if (this.mEnd != -1) {
            this.mGuidelineWidget.setGuideEnd(this.mEnd);
        }
        else {
            this.mGuidelineWidget.setGuidePercent(this.mPercent);
        }
    }
    
    @Override
    public ConstraintWidget getConstraintWidget() {
        if (this.mGuidelineWidget == null) {
            this.mGuidelineWidget = new Guideline();
        }
        return this.mGuidelineWidget;
    }
    
    @Override
    public void setConstraintWidget(final ConstraintWidget widget) {
        if (widget instanceof Guideline) {
            this.mGuidelineWidget = (Guideline)widget;
        }
        else {
            this.mGuidelineWidget = null;
        }
    }
}
