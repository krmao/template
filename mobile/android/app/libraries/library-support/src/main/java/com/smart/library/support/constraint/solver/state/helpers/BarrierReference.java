package com.smart.library.support.constraint.solver.state.helpers;

import com.smart.library.support.constraint.solver.state.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class BarrierReference extends HelperReference
{
    private State.Direction mDirection;
    private int mMargin;
    private Barrier mBarrierWidget;
    
    public BarrierReference(final State state) {
        super(state, State.Helper.BARRIER);
    }
    
    public void setBarrierDirection(final State.Direction barrierDirection) {
        this.mDirection = barrierDirection;
    }
    
    public void margin(final Object value) {
        this.margin(this.mState.convertDimension(value));
    }
    
    public void margin(final int value) {
        this.mMargin = value;
    }
    
    @Override
    public HelperWidget getHelperWidget() {
        if (this.mBarrierWidget == null) {
            this.mBarrierWidget = new Barrier();
        }
        return this.mBarrierWidget;
    }
    
    @Override
    public void apply() {
        this.getHelperWidget();
        int direction = 0;
        switch (this.mDirection) {
            case LEFT:
            case START: {
                direction = 0;
                break;
            }
            case RIGHT:
            case END: {
                direction = 1;
                break;
            }
            case TOP: {
                direction = 2;
                break;
            }
            case BOTTOM: {
                direction = 3;
                break;
            }
        }
        this.mBarrierWidget.setBarrierType(direction);
        this.mBarrierWidget.setMargin(this.mMargin);
    }
}
