package com.smart.library.support.constraint.solver.state;

import java.util.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class ConstraintReference implements Reference
{
    private Object key;
    final State mState;
    int mHorizontalChainStyle;
    int mVerticalChainStyle;
    float mHorizontalBias;
    float mVerticalBias;
    int mMarginLeft;
    int mMarginRight;
    int mMarginStart;
    int mMarginEnd;
    int mMarginTop;
    int mMarginBottom;
    Object mLeftToLeft;
    Object mLeftToRight;
    Object mRightToLeft;
    Object mRightToRight;
    Object mStartToStart;
    Object mStartToEnd;
    Object mEndToStart;
    Object mEndToEnd;
    Object mTopToTop;
    Object mTopToBottom;
    Object mBottomToTop;
    Object mBottomToBottom;
    Object mBaselineToBaseline;
    State.Constraint mLast;
    Dimension mHorizontalDimension;
    Dimension mVerticalDimension;
    private Object mView;
    private ConstraintWidget mConstraintWidget;
    
    @Override
    public void setKey(final Object key) {
        this.key = key;
    }
    
    @Override
    public Object getKey() {
        return this.key;
    }
    
    public void setView(final Object view) {
        this.mView = view;
        if (this.mConstraintWidget != null) {
            this.mConstraintWidget.setCompanionWidget(this.mView);
        }
    }
    
    public Object getView() {
        return this.mView;
    }
    
    @Override
    public void setConstraintWidget(final ConstraintWidget widget) {
        if (widget == null) {
            return;
        }
        (this.mConstraintWidget = widget).setCompanionWidget(this.mView);
    }
    
    @Override
    public ConstraintWidget getConstraintWidget() {
        if (this.mConstraintWidget == null) {
            (this.mConstraintWidget = this.createConstraintWidget()).setCompanionWidget(this.mView);
        }
        return this.mConstraintWidget;
    }
    
    public ConstraintWidget createConstraintWidget() {
        return new ConstraintWidget(this.getWidth().getValue(), this.getHeight().getValue());
    }
    
    public void validate() throws IncorrectConstraintException {
        final ArrayList<String> errors = new ArrayList<String>();
        if (this.mLeftToLeft != null && this.mLeftToRight != null) {
            errors.add("LeftToLeft and LeftToRight both defined");
        }
        if (this.mRightToLeft != null && this.mRightToRight != null) {
            errors.add("RightToLeft and RightToRight both defined");
        }
        if (this.mStartToStart != null && this.mStartToEnd != null) {
            errors.add("StartToStart and StartToEnd both defined");
        }
        if (this.mEndToStart != null && this.mEndToEnd != null) {
            errors.add("EndToStart and EndToEnd both defined");
        }
        if ((this.mLeftToLeft != null || this.mLeftToRight != null || this.mRightToLeft != null || this.mRightToRight != null) && (this.mStartToStart != null || this.mStartToEnd != null || this.mEndToStart != null || this.mEndToEnd != null)) {
            errors.add("Both left/right and start/end constraints defined");
        }
        if (errors.size() > 0) {
            throw new IncorrectConstraintException(errors);
        }
    }
    
    private Object get(final Object reference) {
        if (reference == null) {
            return null;
        }
        if (!(reference instanceof ConstraintReference)) {
            return this.mState.reference(reference);
        }
        return reference;
    }
    
    public ConstraintReference(final State state) {
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalBias = 0.5f;
        this.mVerticalBias = 0.5f;
        this.mMarginLeft = 0;
        this.mMarginRight = 0;
        this.mMarginStart = 0;
        this.mMarginEnd = 0;
        this.mMarginTop = 0;
        this.mMarginBottom = 0;
        this.mLeftToLeft = null;
        this.mLeftToRight = null;
        this.mRightToLeft = null;
        this.mRightToRight = null;
        this.mStartToStart = null;
        this.mStartToEnd = null;
        this.mEndToStart = null;
        this.mEndToEnd = null;
        this.mTopToTop = null;
        this.mTopToBottom = null;
        this.mBottomToTop = null;
        this.mBottomToBottom = null;
        this.mBaselineToBaseline = null;
        this.mLast = null;
        this.mHorizontalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
        this.mVerticalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
        this.mState = state;
    }
    
    public void setHorizontalChainStyle(final int chainStyle) {
        this.mHorizontalChainStyle = chainStyle;
    }
    
    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }
    
    public void setVerticalChainStyle(final int chainStyle) {
        this.mVerticalChainStyle = chainStyle;
    }
    
    public int getVerticalChainStyle(final int chainStyle) {
        return this.mVerticalChainStyle;
    }
    
    public ConstraintReference clearVertical() {
        this.top().clear();
        this.baseline().clear();
        this.bottom().clear();
        return this;
    }
    
    public ConstraintReference clearHorizontal() {
        this.start().clear();
        this.end().clear();
        this.left().clear();
        this.right().clear();
        return this;
    }
    
    public ConstraintReference left() {
        if (this.mLeftToLeft != null) {
            this.mLast = State.Constraint.LEFT_TO_LEFT;
        }
        else {
            this.mLast = State.Constraint.LEFT_TO_RIGHT;
        }
        return this;
    }
    
    public ConstraintReference right() {
        if (this.mRightToLeft != null) {
            this.mLast = State.Constraint.RIGHT_TO_LEFT;
        }
        else {
            this.mLast = State.Constraint.RIGHT_TO_RIGHT;
        }
        return this;
    }
    
    public ConstraintReference start() {
        if (this.mStartToStart != null) {
            this.mLast = State.Constraint.START_TO_START;
        }
        else {
            this.mLast = State.Constraint.START_TO_END;
        }
        return this;
    }
    
    public ConstraintReference end() {
        if (this.mEndToStart != null) {
            this.mLast = State.Constraint.END_TO_START;
        }
        else {
            this.mLast = State.Constraint.END_TO_END;
        }
        return this;
    }
    
    public ConstraintReference top() {
        if (this.mTopToTop != null) {
            this.mLast = State.Constraint.TOP_TO_TOP;
        }
        else {
            this.mLast = State.Constraint.TOP_TO_BOTTOM;
        }
        return this;
    }
    
    public ConstraintReference bottom() {
        if (this.mBottomToTop != null) {
            this.mLast = State.Constraint.BOTTOM_TO_TOP;
        }
        else {
            this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
        }
        return this;
    }
    
    public ConstraintReference baseline() {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        return this;
    }
    
    private void dereference() {
        this.mLeftToLeft = this.get(this.mLeftToLeft);
        this.mLeftToRight = this.get(this.mLeftToRight);
        this.mRightToLeft = this.get(this.mRightToLeft);
        this.mRightToRight = this.get(this.mRightToRight);
        this.mStartToStart = this.get(this.mStartToStart);
        this.mStartToEnd = this.get(this.mStartToEnd);
        this.mEndToStart = this.get(this.mEndToStart);
        this.mEndToEnd = this.get(this.mEndToEnd);
        this.mTopToTop = this.get(this.mTopToTop);
        this.mTopToBottom = this.get(this.mTopToBottom);
        this.mBottomToTop = this.get(this.mBottomToTop);
        this.mBottomToBottom = this.get(this.mBottomToBottom);
        this.mBaselineToBaseline = this.get(this.mBaselineToBaseline);
    }
    
    public ConstraintReference leftToLeft(final Object reference) {
        this.mLast = State.Constraint.LEFT_TO_LEFT;
        this.mLeftToLeft = reference;
        return this;
    }
    
    public ConstraintReference leftToRight(final Object reference) {
        this.mLast = State.Constraint.LEFT_TO_RIGHT;
        this.mLeftToRight = reference;
        return this;
    }
    
    public ConstraintReference rightToLeft(final Object reference) {
        this.mLast = State.Constraint.RIGHT_TO_LEFT;
        this.mRightToLeft = reference;
        return this;
    }
    
    public ConstraintReference rightToRight(final Object reference) {
        this.mLast = State.Constraint.RIGHT_TO_RIGHT;
        this.mRightToRight = reference;
        return this;
    }
    
    public ConstraintReference startToStart(final Object reference) {
        this.mLast = State.Constraint.START_TO_START;
        this.mStartToStart = reference;
        return this;
    }
    
    public ConstraintReference startToEnd(final Object reference) {
        this.mLast = State.Constraint.START_TO_END;
        this.mStartToEnd = reference;
        return this;
    }
    
    public ConstraintReference endToStart(final Object reference) {
        this.mLast = State.Constraint.END_TO_START;
        this.mEndToStart = reference;
        return this;
    }
    
    public ConstraintReference endToEnd(final Object reference) {
        this.mLast = State.Constraint.END_TO_END;
        this.mEndToEnd = reference;
        return this;
    }
    
    public ConstraintReference topToTop(final Object reference) {
        this.mLast = State.Constraint.TOP_TO_TOP;
        this.mTopToTop = reference;
        return this;
    }
    
    public ConstraintReference topToBottom(final Object reference) {
        this.mLast = State.Constraint.TOP_TO_BOTTOM;
        this.mTopToBottom = reference;
        return this;
    }
    
    public ConstraintReference bottomToTop(final Object reference) {
        this.mLast = State.Constraint.BOTTOM_TO_TOP;
        this.mBottomToTop = reference;
        return this;
    }
    
    public ConstraintReference bottomToBottom(final Object reference) {
        this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
        this.mBottomToBottom = reference;
        return this;
    }
    
    public ConstraintReference baselineToBaseline(final Object reference) {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        this.mBaselineToBaseline = reference;
        return this;
    }
    
    public ConstraintReference centerHorizontally(final Object reference) {
        final Object ref = this.get(reference);
        this.mStartToStart = ref;
        this.mEndToEnd = ref;
        this.mLast = State.Constraint.CENTER_HORIZONTALLY;
        this.mHorizontalBias = 0.5f;
        return this;
    }
    
    public ConstraintReference centerVertically(final Object reference) {
        final Object ref = this.get(reference);
        this.mTopToTop = ref;
        this.mBottomToBottom = ref;
        this.mLast = State.Constraint.CENTER_VERTICALLY;
        this.mVerticalBias = 0.5f;
        return this;
    }
    
    public ConstraintReference width(final Dimension dimension) {
        return this.setWidth(dimension);
    }
    
    public ConstraintReference height(final Dimension dimension) {
        return this.setHeight(dimension);
    }
    
    public Dimension getWidth() {
        return this.mHorizontalDimension;
    }
    
    public ConstraintReference setWidth(final Dimension dimension) {
        this.mHorizontalDimension = dimension;
        return this;
    }
    
    public Dimension getHeight() {
        return this.mVerticalDimension;
    }
    
    public ConstraintReference setHeight(final Dimension dimension) {
        this.mVerticalDimension = dimension;
        return this;
    }
    
    public ConstraintReference margin(final Object marginValue) {
        return this.margin(this.mState.convertDimension(marginValue));
    }
    
    public ConstraintReference margin(final int value) {
        if (this.mLast != null) {
            switch (this.mLast) {
                case LEFT_TO_LEFT:
                case LEFT_TO_RIGHT: {
                    this.mMarginLeft = value;
                    break;
                }
                case RIGHT_TO_LEFT:
                case RIGHT_TO_RIGHT: {
                    this.mMarginRight = value;
                    break;
                }
                case START_TO_START:
                case START_TO_END: {
                    this.mMarginStart = value;
                    break;
                }
                case END_TO_START:
                case END_TO_END: {
                    this.mMarginEnd = value;
                    break;
                }
                case TOP_TO_TOP:
                case TOP_TO_BOTTOM: {
                    this.mMarginTop = value;
                    break;
                }
                case BOTTOM_TO_TOP:
                case BOTTOM_TO_BOTTOM: {
                    this.mMarginBottom = value;
                    break;
                }
            }
        }
        else {
            this.mMarginLeft = value;
            this.mMarginRight = value;
            this.mMarginStart = value;
            this.mMarginEnd = value;
            this.mMarginTop = value;
            this.mMarginBottom = value;
        }
        return this;
    }
    
    public ConstraintReference horizontalBias(final float value) {
        this.mHorizontalBias = value;
        return this;
    }
    
    public ConstraintReference verticalBias(final float value) {
        this.mVerticalBias = value;
        return this;
    }
    
    public ConstraintReference bias(final float value) {
        if (this.mLast == null) {
            return this;
        }
        switch (this.mLast) {
            case LEFT_TO_LEFT:
            case LEFT_TO_RIGHT:
            case RIGHT_TO_LEFT:
            case RIGHT_TO_RIGHT:
            case START_TO_START:
            case START_TO_END:
            case END_TO_START:
            case END_TO_END:
            case CENTER_HORIZONTALLY: {
                this.mHorizontalBias = value;
                break;
            }
            case TOP_TO_TOP:
            case TOP_TO_BOTTOM:
            case BOTTOM_TO_TOP:
            case BOTTOM_TO_BOTTOM:
            case CENTER_VERTICALLY: {
                this.mVerticalBias = value;
                break;
            }
        }
        return this;
    }
    
    public ConstraintReference clear() {
        if (this.mLast != null) {
            switch (this.mLast) {
                case LEFT_TO_LEFT:
                case LEFT_TO_RIGHT: {
                    this.mLeftToLeft = null;
                    this.mLeftToRight = null;
                    this.mMarginLeft = 0;
                    break;
                }
                case RIGHT_TO_LEFT:
                case RIGHT_TO_RIGHT: {
                    this.mRightToLeft = null;
                    this.mRightToRight = null;
                    this.mMarginRight = 0;
                    break;
                }
                case START_TO_START:
                case START_TO_END: {
                    this.mStartToStart = null;
                    this.mStartToEnd = null;
                    this.mMarginStart = 0;
                    break;
                }
                case END_TO_START:
                case END_TO_END: {
                    this.mEndToStart = null;
                    this.mEndToEnd = null;
                    this.mMarginEnd = 0;
                    break;
                }
                case TOP_TO_TOP:
                case TOP_TO_BOTTOM: {
                    this.mTopToTop = null;
                    this.mTopToBottom = null;
                    this.mMarginTop = 0;
                    break;
                }
                case BOTTOM_TO_TOP:
                case BOTTOM_TO_BOTTOM: {
                    this.mBottomToTop = null;
                    this.mBottomToBottom = null;
                    this.mMarginBottom = 0;
                    break;
                }
                case BASELINE_TO_BASELINE: {
                    this.mBaselineToBaseline = null;
                    break;
                }
            }
        }
        else {
            this.mLeftToLeft = null;
            this.mLeftToRight = null;
            this.mMarginLeft = 0;
            this.mRightToLeft = null;
            this.mRightToRight = null;
            this.mMarginRight = 0;
            this.mStartToStart = null;
            this.mStartToEnd = null;
            this.mMarginStart = 0;
            this.mEndToStart = null;
            this.mEndToEnd = null;
            this.mMarginEnd = 0;
            this.mTopToTop = null;
            this.mTopToBottom = null;
            this.mMarginTop = 0;
            this.mBottomToTop = null;
            this.mBottomToBottom = null;
            this.mMarginBottom = 0;
            this.mBaselineToBaseline = null;
            this.mHorizontalBias = 0.5f;
            this.mVerticalBias = 0.5f;
        }
        return this;
    }
    
    private ConstraintWidget getTarget(final Object target) {
        if (target instanceof Reference) {
            final Reference referenceTarget = (Reference)target;
            return referenceTarget.getConstraintWidget();
        }
        return null;
    }
    
    private void applyConnection(final ConstraintWidget widget, final Object opaqueTarget, final State.Constraint type) {
        final ConstraintWidget target = this.getTarget(opaqueTarget);
        if (target == null) {
            return;
        }
        // final int n = ConstraintReference$1.$SwitchMap$android$support$constraint$solver$state$State$Constraint[type.ordinal()];
        switch (type) {
            case START_TO_START: {
                widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginStart);
                break;
            }
            case START_TO_END: {
                widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginStart);
                break;
            }
            case END_TO_START: {
                widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginEnd);
                break;
            }
            case END_TO_END: {
                widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginEnd);
                break;
            }
            case LEFT_TO_LEFT: {
                widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginLeft);
                break;
            }
            case LEFT_TO_RIGHT: {
                widget.getAnchor(ConstraintAnchor.Type.LEFT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginLeft);
                break;
            }
            case RIGHT_TO_LEFT: {
                widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginRight);
                break;
            }
            case RIGHT_TO_RIGHT: {
                widget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(target.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginRight);
                break;
            }
            case TOP_TO_TOP: {
                widget.getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginTop);
                break;
            }
            case TOP_TO_BOTTOM: {
                widget.getAnchor(ConstraintAnchor.Type.TOP).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginTop);
                break;
            }
            case BOTTOM_TO_TOP: {
                widget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginBottom);
                break;
            }
            case BOTTOM_TO_BOTTOM: {
                widget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(target.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginBottom);
                break;
            }
            case BASELINE_TO_BASELINE: {
                widget.immediateConnect(ConstraintAnchor.Type.BASELINE, target, ConstraintAnchor.Type.BASELINE, 0, 0);
                break;
            }
        }
    }
    
    @Override
    public void apply() {
        if (this.mConstraintWidget == null) {
            return;
        }
        this.mHorizontalDimension.apply(this.mState, this.mConstraintWidget, 0);
        this.mVerticalDimension.apply(this.mState, this.mConstraintWidget, 1);
        this.dereference();
        this.applyConnection(this.mConstraintWidget, this.mLeftToLeft, State.Constraint.LEFT_TO_LEFT);
        this.applyConnection(this.mConstraintWidget, this.mLeftToRight, State.Constraint.LEFT_TO_RIGHT);
        this.applyConnection(this.mConstraintWidget, this.mRightToLeft, State.Constraint.RIGHT_TO_LEFT);
        this.applyConnection(this.mConstraintWidget, this.mRightToRight, State.Constraint.RIGHT_TO_RIGHT);
        this.applyConnection(this.mConstraintWidget, this.mStartToStart, State.Constraint.START_TO_START);
        this.applyConnection(this.mConstraintWidget, this.mStartToEnd, State.Constraint.START_TO_END);
        this.applyConnection(this.mConstraintWidget, this.mEndToStart, State.Constraint.END_TO_START);
        this.applyConnection(this.mConstraintWidget, this.mEndToEnd, State.Constraint.END_TO_END);
        this.applyConnection(this.mConstraintWidget, this.mTopToTop, State.Constraint.TOP_TO_TOP);
        this.applyConnection(this.mConstraintWidget, this.mTopToBottom, State.Constraint.TOP_TO_BOTTOM);
        this.applyConnection(this.mConstraintWidget, this.mBottomToTop, State.Constraint.BOTTOM_TO_TOP);
        this.applyConnection(this.mConstraintWidget, this.mBottomToBottom, State.Constraint.BOTTOM_TO_BOTTOM);
        this.applyConnection(this.mConstraintWidget, this.mBaselineToBaseline, State.Constraint.BASELINE_TO_BASELINE);
        if (this.mHorizontalChainStyle != 0) {
            this.mConstraintWidget.setHorizontalChainStyle(this.mHorizontalChainStyle);
        }
        if (this.mVerticalChainStyle != 0) {
            this.mConstraintWidget.setVerticalChainStyle(this.mVerticalChainStyle);
        }
        this.mConstraintWidget.setHorizontalBiasPercent(this.mHorizontalBias);
        this.mConstraintWidget.setVerticalBiasPercent(this.mVerticalBias);
    }
    
    class IncorrectConstraintException extends Exception
    {
        private final ArrayList<String> mErrors;
        
        public IncorrectConstraintException(final ArrayList<String> errors) {
            this.mErrors = errors;
        }
        
        public ArrayList<String> getErrors() {
            return this.mErrors;
        }
        
        @Override
        public String toString() {
            return "IncorrectConstraintException: " + this.mErrors.toString();
        }
    }
    
    public interface ConstraintReferenceFactory
    {
        ConstraintReference create(final State p0);
    }
}
