package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.Guideline;

import java.util.*;

public class ConstraintAnchor
{
    private static final boolean ALLOW_BINARY = false;
    private static final int UNSET_GONE_MARGIN = -1;
    public final ConstraintWidget mOwner;
    public final Type mType;
    public ConstraintAnchor mTarget;
    public int mMargin;
    int mGoneMargin;
    SolverVariable mSolverVariable;
    
    public void copyFrom(final ConstraintAnchor source, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        if (source.mTarget != null) {
            final Type type = source.mTarget.getType();
            final ConstraintWidget owner = map.get(source.mTarget.mOwner);
            this.mTarget = owner.getAnchor(type);
        }
        else {
            this.mTarget = null;
        }
        this.mMargin = source.mMargin;
        this.mGoneMargin = source.mGoneMargin;
    }
    
    public ConstraintAnchor(final ConstraintWidget owner, final Type type) {
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mOwner = owner;
        this.mType = type;
    }
    
    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }
    
    public void resetSolverVariable(final Cache cache) {
        if (this.mSolverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
        }
        else {
            this.mSolverVariable.reset();
        }
    }
    
    public ConstraintWidget getOwner() {
        return this.mOwner;
    }
    
    public Type getType() {
        return this.mType;
    }
    
    public int getMargin() {
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && this.mTarget != null && this.mTarget.mOwner.getVisibility() == 8) {
            return this.mGoneMargin;
        }
        return this.mMargin;
    }
    
    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }
    
    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
    }
    
    public boolean connect(final ConstraintAnchor toAnchor, final int margin, final int goneMargin, final boolean forceConnection) {
        if (toAnchor == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            return true;
        }
        if (!forceConnection && !this.isValidConnection(toAnchor)) {
            return false;
        }
        this.mTarget = toAnchor;
        if (margin > 0) {
            this.mMargin = margin;
        }
        else {
            this.mMargin = 0;
        }
        this.mGoneMargin = goneMargin;
        return true;
    }
    
    public boolean connect(final ConstraintAnchor toAnchor, final int margin) {
        return this.connect(toAnchor, margin, -1, false);
    }
    
    public boolean isConnected() {
        return this.mTarget != null;
    }
    
    public boolean isValidConnection(final ConstraintAnchor anchor) {
        if (anchor == null) {
            return false;
        }
        final Type target = anchor.getType();
        if (target == this.mType) {
            return this.mType != Type.BASELINE || (anchor.getOwner().hasBaseline() && this.getOwner().hasBaseline());
        }
        switch (this.mType) {
            case CENTER: {
                return target != Type.BASELINE && target != Type.CENTER_X && target != Type.CENTER_Y;
            }
            case LEFT:
            case RIGHT: {
                boolean isCompatible = target == Type.LEFT || target == Type.RIGHT;
                if (anchor.getOwner() instanceof com.smart.library.support.constraint.solver.widgets.Guideline) {
                    isCompatible = (isCompatible || target == Type.CENTER_X);
                }
                return isCompatible;
            }
            case TOP:
            case BOTTOM: {
                boolean isCompatible = target == Type.TOP || target == Type.BOTTOM;
                if (anchor.getOwner() instanceof Guideline) {
                    isCompatible = (isCompatible || target == Type.CENTER_Y);
                }
                return isCompatible;
            }
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE: {
                return false;
            }
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
        }
    }
    
    public boolean isSideAnchor() {
        switch (this.mType) {
            case LEFT:
            case RIGHT:
            case TOP:
            case BOTTOM: {
                return true;
            }
            case CENTER:
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE: {
                return false;
            }
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
        }
    }
    
    public boolean isSimilarDimensionConnection(final ConstraintAnchor anchor) {
        final Type target = anchor.getType();
        if (target == this.mType) {
            return true;
        }
        switch (this.mType) {
            case CENTER: {
                return target != Type.BASELINE;
            }
            case LEFT:
            case RIGHT:
            case CENTER_X: {
                return target == Type.LEFT || target == Type.RIGHT || target == Type.CENTER_X;
            }
            case TOP:
            case BOTTOM:
            case BASELINE:
            case CENTER_Y: {
                return target == Type.TOP || target == Type.BOTTOM || target == Type.CENTER_Y || target == Type.BASELINE;
            }
            case NONE: {
                return false;
            }
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
        }
    }
    
    public void setMargin(final int margin) {
        if (this.isConnected()) {
            this.mMargin = margin;
        }
    }
    
    public void setGoneMargin(final int margin) {
        if (this.isConnected()) {
            this.mGoneMargin = margin;
        }
    }
    
    public boolean isVerticalAnchor() {
        switch (this.mType) {
            case CENTER:
            case LEFT:
            case RIGHT:
            case CENTER_X: {
                return false;
            }
            case TOP:
            case BOTTOM:
            case BASELINE:
            case CENTER_Y:
            case NONE: {
                return true;
            }
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
        }
    }
    
    @Override
    public String toString() {
        return this.mOwner.getDebugName() + ":" + this.mType.toString();
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget target, final ConstraintAnchor anchor) {
        return this.isConnectionAllowed(target);
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget target) {
        final HashSet<ConstraintWidget> checked = new HashSet<ConstraintWidget>();
        if (this.isConnectionToMe(target, checked)) {
            return false;
        }
        final ConstraintWidget parent = this.getOwner().getParent();
        return parent == target || target.getParent() == parent;
    }
    
    private boolean isConnectionToMe(final ConstraintWidget target, final HashSet<ConstraintWidget> checked) {
        if (checked.contains(target)) {
            return false;
        }
        checked.add(target);
        if (target == this.getOwner()) {
            return true;
        }
        final ArrayList<ConstraintAnchor> targetAnchors = target.getAnchors();
        for (int i = 0, targetAnchorsSize = targetAnchors.size(); i < targetAnchorsSize; ++i) {
            final ConstraintAnchor anchor = targetAnchors.get(i);
            if (anchor.isSimilarDimensionConnection(this) && anchor.isConnected() && this.isConnectionToMe(anchor.getTarget().getOwner(), checked)) {
                return true;
            }
        }
        return false;
    }
    
    public final ConstraintAnchor getOpposite() {
        switch (this.mType) {
            case LEFT: {
                return this.mOwner.mRight;
            }
            case RIGHT: {
                return this.mOwner.mLeft;
            }
            case TOP: {
                return this.mOwner.mBottom;
            }
            case BOTTOM: {
                return this.mOwner.mTop;
            }
            case CENTER:
            case BASELINE:
            case CENTER_X:
            case CENTER_Y:
            case NONE: {
                return null;
            }
            default: {
                throw new AssertionError((Object)this.mType.name());
            }
        }
    }
    
    public enum Type
    {
        NONE, 
        LEFT, 
        TOP, 
        RIGHT, 
        BOTTOM, 
        BASELINE, 
        CENTER, 
        CENTER_X, 
        CENTER_Y;
    }
}
