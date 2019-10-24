package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;

import java.util.*;

public class ChainHead
{
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstVisibleWidget;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastVisibleWidget;
    protected ConstraintWidget mHead;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;
    protected float mTotalWeight;
    int mVisibleWidgets;
    int mTotalSize;
    int mTotalMargins;
    boolean mOptimizable;
    private int mOrientation;
    private boolean mIsRtl;
    protected boolean mHasUndefinedWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasComplexMatchWeights;
    private boolean mDefined;
    
    public ChainHead(final ConstraintWidget first, final int orientation, final boolean isRtl) {
        this.mTotalWeight = 0.0f;
        this.mIsRtl = false;
        this.mFirst = first;
        this.mOrientation = orientation;
        this.mIsRtl = isRtl;
    }
    
    private static boolean isMatchConstraintEqualityCandidate(final ConstraintWidget widget, final int orientation) {
        return widget.getVisibility() != 8 && widget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (widget.mResolvedMatchConstraintDefault[orientation] == 0 || widget.mResolvedMatchConstraintDefault[orientation] == 3);
    }
    
    private void defineChainProperties() {
        final int offset = this.mOrientation * 2;
        ConstraintWidget lastVisited = this.mFirst;
        this.mOptimizable = true;
        ConstraintWidget widget = this.mFirst;
        ConstraintWidget next = this.mFirst;
        boolean done = false;
        while (!done) {
            ++this.mWidgetsCount;
            widget.mNextChainWidget[this.mOrientation] = null;
            widget.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (widget.getVisibility() != 8) {
                ++this.mVisibleWidgets;
                if (widget.getDimensionBehaviour(this.mOrientation) != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.mTotalSize += widget.getLength(this.mOrientation);
                }
                this.mTotalSize += widget.mListAnchors[offset].getMargin();
                this.mTotalSize += widget.mListAnchors[offset + 1].getMargin();
                this.mTotalMargins += widget.mListAnchors[offset].getMargin();
                this.mTotalMargins += widget.mListAnchors[offset + 1].getMargin();
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = widget;
                }
                this.mLastVisibleWidget = widget;
                if (widget.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (widget.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || widget.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || widget.mResolvedMatchConstraintDefault[this.mOrientation] == 2) {
                        ++this.mWidgetsMatchCount;
                        final float weight = widget.mWeight[this.mOrientation];
                        if (weight > 0.0f) {
                            this.mTotalWeight += widget.mWeight[this.mOrientation];
                        }
                        if (isMatchConstraintEqualityCandidate(widget, this.mOrientation)) {
                            if (weight < 0.0f) {
                                this.mHasUndefinedWeights = true;
                            }
                            else {
                                this.mHasDefinedWeights = true;
                            }
                            if (this.mWeightedMatchConstraintsWidgets == null) {
                                this.mWeightedMatchConstraintsWidgets = new ArrayList<ConstraintWidget>();
                            }
                            this.mWeightedMatchConstraintsWidgets.add(widget);
                        }
                        if (this.mFirstMatchConstraintWidget == null) {
                            this.mFirstMatchConstraintWidget = widget;
                        }
                        if (this.mLastMatchConstraintWidget != null) {
                            this.mLastMatchConstraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = widget;
                        }
                        this.mLastMatchConstraintWidget = widget;
                    }
                    if (this.mOrientation == 0) {
                        if (widget.mMatchConstraintDefaultWidth != 0) {
                            this.mOptimizable = false;
                        }
                        else if (widget.mMatchConstraintMinWidth != 0 || widget.mMatchConstraintMaxWidth != 0) {
                            this.mOptimizable = false;
                        }
                    }
                    else if (widget.mMatchConstraintDefaultHeight != 0) {
                        this.mOptimizable = false;
                    }
                    else if (widget.mMatchConstraintMinHeight != 0 || widget.mMatchConstraintMaxHeight != 0) {
                        this.mOptimizable = false;
                    }
                    if (widget.mDimensionRatio != 0.0f) {
                        this.mOptimizable = false;
                    }
                }
            }
            if (lastVisited != widget) {
                lastVisited.mNextChainWidget[this.mOrientation] = widget;
            }
            lastVisited = widget;
            final ConstraintAnchor nextAnchor = widget.mListAnchors[offset + 1].mTarget;
            if (nextAnchor != null) {
                next = nextAnchor.mOwner;
                if (next.mListAnchors[offset].mTarget == null || next.mListAnchors[offset].mTarget.mOwner != widget) {
                    next = null;
                }
            }
            else {
                next = null;
            }
            if (next != null) {
                widget = next;
            }
            else {
                done = true;
            }
        }
        if (this.mFirstVisibleWidget != null) {
            this.mTotalSize -= this.mFirstVisibleWidget.mListAnchors[offset].getMargin();
        }
        if (this.mLastVisibleWidget != null) {
            this.mTotalSize -= this.mLastVisibleWidget.mListAnchors[offset + 1].getMargin();
        }
        this.mLast = widget;
        if (this.mOrientation == 0 && this.mIsRtl) {
            this.mHead = this.mLast;
        }
        else {
            this.mHead = this.mFirst;
        }
        this.mHasComplexMatchWeights = (this.mHasDefinedWeights && this.mHasUndefinedWeights);
    }
    
    public ConstraintWidget getFirst() {
        return this.mFirst;
    }
    
    public ConstraintWidget getFirstVisibleWidget() {
        return this.mFirstVisibleWidget;
    }
    
    public ConstraintWidget getLast() {
        return this.mLast;
    }
    
    public ConstraintWidget getLastVisibleWidget() {
        return this.mLastVisibleWidget;
    }
    
    public ConstraintWidget getHead() {
        return this.mHead;
    }
    
    public ConstraintWidget getFirstMatchConstraintWidget() {
        return this.mFirstMatchConstraintWidget;
    }
    
    public ConstraintWidget getLastMatchConstraintWidget() {
        return this.mLastMatchConstraintWidget;
    }
    
    public float getTotalWeight() {
        return this.mTotalWeight;
    }
    
    public void define() {
        if (!this.mDefined) {
            this.defineChainProperties();
        }
        this.mDefined = true;
    }
}
