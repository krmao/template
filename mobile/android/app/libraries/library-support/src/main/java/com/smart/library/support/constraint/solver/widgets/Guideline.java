package com.smart.library.support.constraint.solver.widgets;

import java.util.*;
import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

public class Guideline extends ConstraintWidget
{
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_UNKNWON = -1;
    protected float mRelativePercent;
    protected int mRelativeBegin;
    protected int mRelativeEnd;
    private ConstraintAnchor mAnchor;
    private int mOrientation;
    private boolean mIsPositionRelaxed;
    private int mMinimumPosition;
    
    public Guideline() {
        this.mRelativePercent = -1.0f;
        this.mRelativeBegin = -1;
        this.mRelativeEnd = -1;
        this.mAnchor = this.mTop;
        this.mOrientation = 0;
        this.mIsPositionRelaxed = false;
        this.mMinimumPosition = 0;
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        for (int count = this.mListAnchors.length, i = 0; i < count; ++i) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }
    
    @Override
    public void copy(final ConstraintWidget src, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        final Guideline srcGuideline = (Guideline)src;
        this.mRelativePercent = srcGuideline.mRelativePercent;
        this.mRelativeBegin = srcGuideline.mRelativeBegin;
        this.mRelativeEnd = srcGuideline.mRelativeEnd;
        this.setOrientation(srcGuideline.mOrientation);
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        if (this.mRelativeEnd != -1) {
            return 2;
        }
        return -1;
    }
    
    public void setOrientation(final int orientation) {
        if (this.mOrientation == orientation) {
            return;
        }
        this.mOrientation = orientation;
        this.mAnchors.clear();
        if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
        }
        else {
            this.mAnchor = this.mTop;
        }
        this.mAnchors.add(this.mAnchor);
        for (int count = this.mListAnchors.length, i = 0; i < count; ++i) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }
    
    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }
    
    @Override
    public String getType() {
        return "Guideline";
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public void setMinimumPosition(final int minimum) {
        this.mMinimumPosition = minimum;
    }
    
    public void setPositionRelaxed(final boolean value) {
        if (this.mIsPositionRelaxed == value) {
            return;
        }
        this.mIsPositionRelaxed = value;
    }
    
    @Override
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type anchorType) {
        switch (anchorType) {
            case LEFT:
            case RIGHT: {
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            }
            case TOP:
            case BOTTOM: {
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
            }
            case BASELINE:
            case CENTER:
            case CENTER_X:
            case CENTER_Y:
            case NONE: {
                return null;
            }
        }
        throw new AssertionError((Object)anchorType.name());
    }
    
    @Override
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public void setGuidePercent(final int value) {
        this.setGuidePercent(value / 100.0f);
    }
    
    public void setGuidePercent(final float value) {
        if (value > -1.0f) {
            this.mRelativePercent = value;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setGuideBegin(final int value) {
        if (value > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = value;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setGuideEnd(final int value) {
        if (value > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = value;
        }
    }
    
    public float getRelativePercent() {
        return this.mRelativePercent;
    }
    
    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }
    
    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }
    
    @Override
    public void addToSolver(final LinearSystem system) {
        final ConstraintWidgetContainer parent = (ConstraintWidgetContainer)this.getParent();
        if (parent == null) {
            return;
        }
        ConstraintAnchor begin = parent.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor end = parent.getAnchor(ConstraintAnchor.Type.RIGHT);
        boolean parentWrapContent = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
        if (this.mOrientation == 0) {
            begin = parent.getAnchor(ConstraintAnchor.Type.TOP);
            end = parent.getAnchor(ConstraintAnchor.Type.BOTTOM);
            parentWrapContent = (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT);
        }
        if (this.mRelativeBegin != -1) {
            final SolverVariable guide = system.createObjectVariable(this.mAnchor);
            final SolverVariable parentLeft = system.createObjectVariable(begin);
            system.addEquality(guide, parentLeft, this.mRelativeBegin, 6);
            if (parentWrapContent) {
                system.addGreaterThan(system.createObjectVariable(end), guide, 0, 5);
            }
        }
        else if (this.mRelativeEnd != -1) {
            final SolverVariable guide = system.createObjectVariable(this.mAnchor);
            final SolverVariable parentRight = system.createObjectVariable(end);
            system.addEquality(guide, parentRight, -this.mRelativeEnd, 6);
            if (parentWrapContent) {
                system.addGreaterThan(guide, system.createObjectVariable(begin), 0, 5);
                system.addGreaterThan(parentRight, guide, 0, 5);
            }
        }
        else if (this.mRelativePercent != -1.0f) {
            final SolverVariable guide = system.createObjectVariable(this.mAnchor);
            final SolverVariable parentLeft = system.createObjectVariable(begin);
            final SolverVariable parentRight2 = system.createObjectVariable(end);
            system.addConstraint(LinearSystem.createRowDimensionPercent(system, guide, parentLeft, parentRight2, this.mRelativePercent, this.mIsPositionRelaxed));
        }
    }
    
    @Override
    public void updateFromSolver(final LinearSystem system) {
        if (this.getParent() == null) {
            return;
        }
        final int value = system.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(value);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
        }
        else {
            this.setX(0);
            this.setY(value);
            this.setWidth(this.getParent().getWidth());
            this.setHeight(0);
        }
    }
    
    void inferRelativePercentPosition() {
        float percent = this.getX() / this.getParent().getWidth();
        if (this.mOrientation == 0) {
            percent = this.getY() / this.getParent().getHeight();
        }
        this.setGuidePercent(percent);
    }
    
    void inferRelativeBeginPosition() {
        int position = this.getX();
        if (this.mOrientation == 0) {
            position = this.getY();
        }
        this.setGuideBegin(position);
    }
    
    void inferRelativeEndPosition() {
        int position = this.getParent().getWidth() - this.getX();
        if (this.mOrientation == 0) {
            position = this.getParent().getHeight() - this.getY();
        }
        this.setGuideEnd(position);
    }
    
    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            this.inferRelativePercentPosition();
        }
        else if (this.mRelativePercent != -1.0f) {
            this.inferRelativeEndPosition();
        }
        else if (this.mRelativeEnd != -1) {
            this.inferRelativeBeginPosition();
        }
    }
}
