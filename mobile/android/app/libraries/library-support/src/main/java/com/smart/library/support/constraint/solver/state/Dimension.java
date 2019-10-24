package com.smart.library.support.constraint.solver.state;

import com.smart.library.support.constraint.solver.widgets.*;

public class Dimension
{
    public static final Object FIXED_DIMENSION;
    public static final Object WRAP_DIMENSION;
    public static final Object SPREAD_DIMENSION;
    public static final Object PARENT_DIMENSION;
    public static final Object PERCENT_DIMENSION;
    private final int WRAP_CONTENT = -2;
    int mMin;
    int mMax;
    float mPercent;
    int mValue;
    float mRatio;
    Object mInitialValue;
    boolean mIsSuggested;
    
    private Dimension() {
        this.mMin = 0;
        this.mMax = Integer.MAX_VALUE;
        this.mPercent = 1.0f;
        this.mValue = 0;
        this.mRatio = 1.0f;
        this.mInitialValue = Dimension.WRAP_DIMENSION;
        this.mIsSuggested = false;
    }
    
    private Dimension(final Object type) {
        this.mMin = 0;
        this.mMax = Integer.MAX_VALUE;
        this.mPercent = 1.0f;
        this.mValue = 0;
        this.mRatio = 1.0f;
        this.mInitialValue = Dimension.WRAP_DIMENSION;
        this.mIsSuggested = false;
        this.mInitialValue = type;
    }
    
    public static Dimension Suggested(final int value) {
        final Dimension dimension = new Dimension();
        dimension.suggested(value);
        return dimension;
    }
    
    public static Dimension Suggested(final Object startValue) {
        final Dimension dimension = new Dimension();
        dimension.suggested(startValue);
        return dimension;
    }
    
    public static Dimension Fixed(final int value) {
        final Dimension dimension = new Dimension(Dimension.FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }
    
    public static Dimension Fixed(final Object value) {
        final Dimension dimension = new Dimension(Dimension.FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }
    
    public static Dimension Percent(final Object key, final float value) {
        final Dimension dimension = new Dimension(Dimension.PERCENT_DIMENSION);
        dimension.percent(key, value);
        return dimension;
    }
    
    public static Dimension Parent() {
        return new Dimension(Dimension.PARENT_DIMENSION);
    }
    
    public static Dimension Wrap() {
        return new Dimension(Dimension.WRAP_DIMENSION);
    }
    
    public static Dimension Spread() {
        return new Dimension(Dimension.SPREAD_DIMENSION);
    }
    
    public Dimension percent(final Object key, final float value) {
        return this;
    }
    
    public Dimension min(final int value) {
        if (value >= 0) {
            this.mMin = value;
        }
        return this;
    }
    
    public Dimension min(final Object value) {
        if (value == Dimension.WRAP_DIMENSION) {
            this.mMin = -2;
        }
        return this;
    }
    
    public Dimension max(final int value) {
        if (this.mMax >= 0) {
            this.mMax = value;
        }
        return this;
    }
    
    public Dimension max(final Object value) {
        if (value == Dimension.WRAP_DIMENSION && this.mIsSuggested) {
            this.mInitialValue = Dimension.WRAP_DIMENSION;
            this.mMax = Integer.MAX_VALUE;
        }
        return this;
    }
    
    public Dimension suggested(final int value) {
        this.mIsSuggested = true;
        return this;
    }
    
    public Dimension suggested(final Object value) {
        this.mInitialValue = value;
        this.mIsSuggested = true;
        return this;
    }
    
    public Dimension fixed(final Object value) {
        this.mInitialValue = value;
        if (value instanceof Integer) {
            this.mValue = (int)value;
            this.mInitialValue = null;
        }
        return this;
    }
    
    public Dimension fixed(final int value) {
        this.mInitialValue = null;
        this.mValue = value;
        return this;
    }
    
    public Dimension ratio(final float ratio) {
        return this;
    }
    
    void setValue(final int value) {
        this.mIsSuggested = false;
        this.mInitialValue = null;
        this.mValue = value;
    }
    
    int getValue() {
        return this.mValue;
    }
    
    void setRatio(final float value) {
        this.mRatio = value;
    }
    
    float getRatio() {
        return this.mRatio;
    }
    
    public void apply(final State state, final ConstraintWidget constraintWidget, final int orientation) {
        if (orientation == 0) {
            if (this.mIsSuggested) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                int type = 0;
                if (this.mInitialValue == Dimension.WRAP_DIMENSION) {
                    type = 1;
                }
                else if (this.mInitialValue == Dimension.PERCENT_DIMENSION) {
                    type = 2;
                }
                constraintWidget.setHorizontalMatchStyle(type, this.mMin, this.mMax, this.mPercent);
            }
            else {
                if (this.mMin > 0) {
                    constraintWidget.setMinWidth(this.mMin);
                }
                if (this.mMax < Integer.MAX_VALUE) {
                    constraintWidget.setMaxWidth(this.mMax);
                }
                if (this.mInitialValue == Dimension.WRAP_DIMENSION) {
                    constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
                else if (this.mInitialValue == Dimension.PARENT_DIMENSION) {
                    constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                }
                else if (this.mInitialValue == null) {
                    constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    constraintWidget.setWidth(this.mValue);
                }
            }
        }
        else if (this.mIsSuggested) {
            constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
            int type = 0;
            if (this.mInitialValue == Dimension.WRAP_DIMENSION) {
                type = 1;
            }
            else if (this.mInitialValue == Dimension.PERCENT_DIMENSION) {
                type = 2;
            }
            constraintWidget.setVerticalMatchStyle(type, this.mMin, this.mMax, this.mPercent);
        }
        else {
            if (this.mMin > 0) {
                constraintWidget.setMinHeight(this.mMin);
            }
            if (this.mMax < Integer.MAX_VALUE) {
                constraintWidget.setMaxHeight(this.mMax);
            }
            if (this.mInitialValue == Dimension.WRAP_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.mInitialValue == Dimension.PARENT_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
            }
            else if (this.mInitialValue == null) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidget.setHeight(this.mValue);
            }
        }
    }
    
    static {
        FIXED_DIMENSION = new Object();
        WRAP_DIMENSION = new Object();
        SPREAD_DIMENSION = new Object();
        PARENT_DIMENSION = new Object();
        PERCENT_DIMENSION = new Object();
    }
    
    public enum Type
    {
        FIXED, 
        WRAP, 
        MATCH_PARENT, 
        MATCH_CONSTRAINT;
    }
}
