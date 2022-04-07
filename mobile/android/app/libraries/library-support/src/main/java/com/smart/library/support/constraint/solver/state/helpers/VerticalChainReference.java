package com.smart.library.support.constraint.solver.state.helpers;

import com.smart.library.support.constraint.solver.state.*;
import java.util.*;

public class VerticalChainReference extends ChainReference
{
    private Object mTopToTop;
    private Object mTopToBottom;
    private Object mBottomToTop;
    private Object mBottomToBottom;
    
    public VerticalChainReference(final State state) {
        super(state, State.Helper.VERTICAL_CHAIN);
    }
    
    @Override
    public void apply() {
        ConstraintReference first = null;
        ConstraintReference previous = null;
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            reference.clearVertical();
        }
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            if (first == null) {
                first = reference;
                if (this.mTopToTop != null) {
                    first.topToTop(this.mTopToTop);
                }
                else if (this.mTopToBottom != null) {
                    first.topToBottom(this.mTopToBottom);
                }
                else {
                    first.topToTop(State.PARENT);
                }
            }
            if (previous != null) {
                previous.bottomToTop(reference.getKey());
                reference.topToBottom(previous.getKey());
            }
            previous = reference;
        }
        if (previous != null) {
            if (this.mBottomToTop != null) {
                previous.bottomToTop(this.mBottomToTop);
            }
            else if (this.mBottomToBottom != null) {
                previous.bottomToBottom(this.mBottomToBottom);
            }
            else {
                previous.bottomToBottom(State.PARENT);
            }
        }
        if (first != null && this.mBias != 0.5f) {
            first.verticalBias(this.mBias);
        }
        switch (this.mStyle) {
            case SPREAD: {
                first.setVerticalChainStyle(0);
                break;
            }
            case SPREAD_INSIDE: {
                first.setVerticalChainStyle(1);
                break;
            }
            case PACKED: {
                first.setVerticalChainStyle(2);
                break;
            }
        }
    }
    
    public void topToTop(final Object target) {
        this.mTopToTop = target;
    }
    
    public void topToBottom(final Object target) {
        this.mTopToBottom = target;
    }
    
    public void bottomToTop(final Object target) {
        this.mBottomToTop = target;
    }
    
    public void bottomToBottom(final Object target) {
        this.mBottomToBottom = target;
    }
}
