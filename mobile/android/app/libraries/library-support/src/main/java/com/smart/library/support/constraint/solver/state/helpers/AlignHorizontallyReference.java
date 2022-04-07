package com.smart.library.support.constraint.solver.state.helpers;

import java.util.*;
import com.smart.library.support.constraint.solver.state.*;

public class AlignHorizontallyReference extends HelperReference
{
    private float mBias;
    private Object mStartToStart;
    private Object mStartToEnd;
    private Object mEndToStart;
    private Object mEndToEnd;
    
    public AlignHorizontallyReference(final State state) {
        super(state, State.Helper.ALIGN_VERTICALLY);
        this.mBias = 0.5f;
    }
    
    @Override
    public void apply() {
        for (final Object key : this.mReferences) {
            final ConstraintReference reference = this.mState.constraints(key);
            reference.clearHorizontal();
            if (this.mStartToStart != null) {
                reference.startToStart(this.mStartToStart);
            }
            else if (this.mStartToEnd != null) {
                reference.startToEnd(this.mStartToEnd);
            }
            else {
                reference.startToStart(State.PARENT);
            }
            if (this.mEndToStart != null) {
                reference.endToStart(this.mEndToStart);
            }
            else if (this.mEndToEnd != null) {
                reference.endToEnd(this.mEndToEnd);
            }
            else {
                reference.endToEnd(State.PARENT);
            }
            if (this.mBias != 0.5f) {
                reference.horizontalBias(this.mBias);
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
    
    public void bias(final float bias) {
        this.mBias = bias;
    }
}
