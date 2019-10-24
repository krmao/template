package com.smart.library.support.constraint.solver.state.helpers;

import com.smart.library.support.constraint.solver.state.*;

public class ChainReference extends HelperReference
{
    protected float mBias;
    protected State.Chain mStyle;
    
    public ChainReference(final State state, final State.Helper type) {
        super(state, type);
        this.mBias = 0.5f;
        this.mStyle = State.Chain.SPREAD;
    }
    
    public State.Chain getStyle() {
        return State.Chain.SPREAD;
    }
    
    public void style(final State.Chain style) {
        this.mStyle = style;
    }
    
    public float getBias() {
        return this.mBias;
    }
    
    public void bias(final float bias) {
        this.mBias = bias;
    }
}
