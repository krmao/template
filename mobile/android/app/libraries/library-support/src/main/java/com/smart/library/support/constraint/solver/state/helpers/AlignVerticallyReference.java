package com.smart.library.support.constraint.solver.state.helpers;

import java.util.*;
import com.smart.library.support.constraint.solver.state.*;

public class AlignVerticallyReference extends HelperReference
{
    private float mBias;
    private Object mTopToTop;
    private Object mTopToBottom;
    private Object mBottomToTop;
    private Object mBottomToBottom;
    
    public AlignVerticallyReference(final State state) {
        super(state, State.Helper.ALIGN_VERTICALLY);
        this.mBias = 0.5f;
    }
    
    @Override
    public void apply() {
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            reference.clearVertical();
            if (this.mTopToTop != null) {
                reference.topToTop(this.mTopToTop);
            }
            else if (this.mTopToBottom != null) {
                reference.topToBottom(this.mTopToBottom);
            }
            else {
                reference.topToTop(State.PARENT);
            }
            if (this.mBottomToTop != null) {
                reference.bottomToTop(this.mBottomToTop);
            }
            else if (this.mBottomToBottom != null) {
                reference.bottomToBottom(this.mBottomToBottom);
            }
            else {
                reference.bottomToBottom(State.PARENT);
            }
            if (this.mBias != 0.5f) {
                reference.verticalBias(this.mBias);
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
    
    public void bias(final float bias) {
        this.mBias = bias;
    }
}
