package com.smart.library.support.constraint.solver.state.helpers;

import com.smart.library.support.constraint.solver.state.*;
import java.util.*;

public class HorizontalChainReference extends ChainReference
{
    private Object mStartToStart;
    private Object mStartToEnd;
    private Object mEndToStart;
    private Object mEndToEnd;
    
    public HorizontalChainReference(final State state) {
        super(state, State.Helper.HORIZONTAL_CHAIN);
    }
    
    @Override
    public void apply() {
        ConstraintReference first = null;
        ConstraintReference previous = null;
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            reference.clearHorizontal();
        }
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            if (first == null) {
                first = reference;
                if (this.mStartToStart != null) {
                    first.startToStart(this.mStartToStart);
                }
                else if (this.mStartToEnd != null) {
                    first.startToEnd(this.mStartToEnd);
                }
                else {
                    first.startToStart(State.PARENT);
                }
            }
            if (previous != null) {
                previous.endToStart(reference.getKey());
                reference.startToEnd(previous.getKey());
            }
            previous = reference;
        }
        if (previous != null) {
            if (this.mEndToStart != null) {
                previous.endToStart(this.mEndToStart);
            }
            else if (this.mEndToEnd != null) {
                previous.endToEnd(this.mEndToEnd);
            }
            else {
                previous.endToEnd(State.PARENT);
            }
        }
        if (first != null && this.mBias != 0.5f) {
            first.horizontalBias(this.mBias);
        }
        switch (this.mStyle) {
            case SPREAD: {
                first.setHorizontalChainStyle(0);
                break;
            }
            case SPREAD_INSIDE: {
                first.setHorizontalChainStyle(1);
                break;
            }
            case PACKED: {
                first.setHorizontalChainStyle(2);
                break;
            }
        }
    }
    
    public void startToStart(final Object target) {
        this.mStartToStart = target;
    }
    
    public void startToEnd(final Object target) {
        this.mStartToEnd = target;
    }
    
    public void endToStart(final Object target) {
        this.mEndToStart = target;
    }
    
    public void endToEnd(final Object target) {
        this.mEndToEnd = target;
    }
}
