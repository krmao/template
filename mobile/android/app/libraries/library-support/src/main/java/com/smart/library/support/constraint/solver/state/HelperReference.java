package com.smart.library.support.constraint.solver.state;

import java.util.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class HelperReference
{
    protected final State mState;
    final State.Helper mType;
    protected ArrayList<Object> mReferences;
    private HelperWidget mHelperWidget;
    
    public HelperReference(final State state, final State.Helper type) {
        this.mReferences = new ArrayList<Object>();
        this.mState = state;
        this.mType = type;
    }
    
    public State.Helper getType() {
        return this.mType;
    }
    
    public HelperReference add(final Object... objects) {
        for (final Object object : objects) {
            this.mReferences.add(object);
        }
        return this;
    }
    
    public void setHelperWidget(final HelperWidget helperWidget) {
        this.mHelperWidget = helperWidget;
    }
    
    public HelperWidget getHelperWidget() {
        return this.mHelperWidget;
    }
    
    public void apply() {
    }
}
