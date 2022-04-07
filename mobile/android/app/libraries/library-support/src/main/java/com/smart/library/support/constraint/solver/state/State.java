package com.smart.library.support.constraint.solver.state;

import com.smart.library.support.constraint.solver.state.helpers.*;
import java.util.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class State
{
    protected HashMap<Object, Reference> mReferences;
    protected HashMap<Object, HelperReference> mHelperReferences;
    static final int UNKNOWN = -1;
    static final int CONSTRAINT_SPREAD = 0;
    static final int CONSTRAINT_WRAP = 1;
    static final int CONSTRAINT_RATIO = 2;
    public static final Integer PARENT;
    public final ConstraintReference mParent;
    private int numHelpers;
    
    public State() {
        this.mReferences = new HashMap<Object, Reference>();
        this.mHelperReferences = new HashMap<Object, HelperReference>();
        this.mParent = new ConstraintReference(this);
        this.numHelpers = 0;
        this.mReferences.put(State.PARENT, this.mParent);
    }
    
    public void reset() {
        this.mHelperReferences.clear();
    }
    
    public int convertDimension(final Object value) {
        if (value instanceof Float) {
            return (int)value;
        }
        if (value instanceof Integer) {
            return (int)value;
        }
        return 0;
    }
    
    public ConstraintReference createConstraintReference(final Object key) {
        return new ConstraintReference(this);
    }
    
    public State width(final Dimension dimension) {
        return this.setWidth(dimension);
    }
    
    public State height(final Dimension dimension) {
        return this.setHeight(dimension);
    }
    
    public State setWidth(final Dimension dimension) {
        this.mParent.setWidth(dimension);
        return this;
    }
    
    public State setHeight(final Dimension dimension) {
        this.mParent.setHeight(dimension);
        return this;
    }
    
    Reference reference(final Object key) {
        return this.mReferences.get(key);
    }
    
    public ConstraintReference constraints(final Object key) {
        Reference reference = this.mReferences.get(key);
        if (reference == null) {
            reference = this.createConstraintReference(key);
            this.mReferences.put(key, reference);
            reference.setKey(key);
        }
        if (reference instanceof ConstraintReference) {
            return (ConstraintReference)reference;
        }
        return null;
    }
    
    private String createHelperKey() {
        return "__HELPER_KEY_" + this.numHelpers++ + "__";
    }
    
    public HelperReference helper(Object key, final Helper type) {
        if (key == null) {
            key = this.createHelperKey();
        }
        HelperReference reference = this.mHelperReferences.get(key);
        if (reference == null) {
            switch (type) {
                case HORIZONTAL_CHAIN: {
                    reference = new HorizontalChainReference(this);
                    break;
                }
                case VERTICAL_CHAIN: {
                    reference = new VerticalChainReference(this);
                    break;
                }
                case ALIGN_HORIZONTALLY: {
                    reference = new AlignHorizontallyReference(this);
                    break;
                }
                case ALIGN_VERTICALLY: {
                    reference = new AlignVerticallyReference(this);
                    break;
                }
                case BARRIER: {
                    reference = new BarrierReference(this);
                    break;
                }
                default: {
                    reference = new HelperReference(this, type);
                    break;
                }
            }
            this.mHelperReferences.put(key, reference);
        }
        return reference;
    }
    
    public GuidelineReference horizontalGuideline(final Object key) {
        return this.guideline(key, 0);
    }
    
    public GuidelineReference verticalGuideline(final Object key) {
        return this.guideline(key, 1);
    }
    
    public GuidelineReference guideline(final Object key, final int orientation) {
        Reference reference = this.mReferences.get(key);
        if (reference == null) {
            final GuidelineReference guidelineReference = new GuidelineReference(this);
            guidelineReference.setOrientation(orientation);
            guidelineReference.setKey(key);
            this.mReferences.put(key, guidelineReference);
            reference = guidelineReference;
        }
        return (GuidelineReference)reference;
    }
    
    public BarrierReference barrier(final Object key, final Direction direction) {
        final BarrierReference reference = (BarrierReference)this.helper(key, Helper.BARRIER);
        reference.setBarrierDirection(direction);
        return reference;
    }
    
    public VerticalChainReference verticalChain(final Object... references) {
        final VerticalChainReference reference = (VerticalChainReference)this.helper(null, Helper.VERTICAL_CHAIN);
        reference.add(references);
        return reference;
    }
    
    public HorizontalChainReference horizontalChain(final Object... references) {
        final HorizontalChainReference reference = (HorizontalChainReference)this.helper(null, Helper.HORIZONTAL_CHAIN);
        reference.add(references);
        return reference;
    }
    
    public AlignHorizontallyReference centerHorizontally(final Object... references) {
        final AlignHorizontallyReference reference = (AlignHorizontallyReference)this.helper(null, Helper.ALIGN_HORIZONTALLY);
        reference.add(references);
        return reference;
    }
    
    public AlignVerticallyReference centerVertically(final Object... references) {
        final AlignVerticallyReference reference = (AlignVerticallyReference)this.helper(null, Helper.ALIGN_VERTICALLY);
        reference.add(references);
        return reference;
    }
    
    public void directMapping() {
        for (final Object key : this.mReferences.keySet()) {
            final ConstraintReference reference = this.constraints(key);
            reference.setView(key);
        }
    }
    
    public void map(final Object key, final Object view) {
        final ConstraintReference reference = this.constraints(key);
        reference.setView(view);
    }
    
    public void apply(final ConstraintWidgetContainer container) {
        container.removeAllChildren();
        this.mParent.getWidth().apply(this, container, 0);
        this.mParent.getHeight().apply(this, container, 1);
        for (final Object key : this.mHelperReferences.keySet()) {
            final HelperReference reference = this.mHelperReferences.get(key);
            final HelperWidget helperWidget = reference.getHelperWidget();
            if (helperWidget != null) {
                Reference constraintReference = this.mReferences.get(key);
                if (constraintReference == null) {
                    constraintReference = this.constraints(key);
                }
                constraintReference.setConstraintWidget(helperWidget);
            }
        }
        for (final Object key : this.mReferences.keySet()) {
            final Reference reference2 = this.mReferences.get(key);
            if (reference2 != this.mParent) {
                final ConstraintWidget widget = reference2.getConstraintWidget();
                widget.setParent(null);
                if (reference2 instanceof GuidelineReference) {
                    reference2.apply();
                }
                container.add(widget);
            }
            else {
                reference2.setConstraintWidget(container);
            }
        }
        for (final Object key : this.mHelperReferences.keySet()) {
            final HelperReference reference = this.mHelperReferences.get(key);
            final HelperWidget helperWidget = reference.getHelperWidget();
            if (helperWidget != null) {
                for (final Object keyRef : reference.mReferences) {
                    final Reference constraintReference2 = this.mReferences.get(keyRef);
                    reference.getHelperWidget().add(constraintReference2.getConstraintWidget());
                }
                reference.apply();
            }
        }
        for (final Object key : this.mReferences.keySet()) {
            final Reference reference2 = this.mReferences.get(key);
            reference2.apply();
        }
    }
    
    static {
        PARENT = 0;
    }
    
    public enum Constraint
    {
        LEFT_TO_LEFT, 
        LEFT_TO_RIGHT, 
        RIGHT_TO_LEFT, 
        RIGHT_TO_RIGHT, 
        START_TO_START, 
        START_TO_END, 
        END_TO_START, 
        END_TO_END, 
        TOP_TO_TOP, 
        TOP_TO_BOTTOM, 
        BOTTOM_TO_TOP, 
        BOTTOM_TO_BOTTOM, 
        BASELINE_TO_BASELINE, 
        CENTER_HORIZONTALLY, 
        CENTER_VERTICALLY;
    }
    
    public enum Direction
    {
        LEFT, 
        RIGHT, 
        START, 
        END, 
        TOP, 
        BOTTOM;
    }
    
    public enum Helper
    {
        HORIZONTAL_CHAIN, 
        VERTICAL_CHAIN, 
        ALIGN_HORIZONTALLY, 
        ALIGN_VERTICALLY, 
        BARRIER, 
        LAYER, 
        FLOW;
    }
    
    public enum Chain
    {
        SPREAD, 
        SPREAD_INSIDE, 
        PACKED;
    }
}
