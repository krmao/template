package com.smart.library.support.constraint.solver.widgets;

import java.util.*;
import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ChainHead;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

class Chain
{
    private static final boolean DEBUG = false;
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem system, final int orientation) {
        int offset = 0;
        int chainsSize = 0;
        ChainHead[] chainsArray = null;
        if (orientation == 0) {
            offset = 0;
            chainsSize = constraintWidgetContainer.mHorizontalChainsSize;
            chainsArray = constraintWidgetContainer.mHorizontalChainsArray;
        }
        else {
            offset = 2;
            chainsSize = constraintWidgetContainer.mVerticalChainsSize;
            chainsArray = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (final ChainHead first : chainsArray) {
            first.define();
            applyChainConstraints(constraintWidgetContainer, system, orientation, offset, first);
        }
    }
    
    static void applyChainConstraints(final ConstraintWidgetContainer container, final LinearSystem system, final int orientation, final int offset, final ChainHead chainHead) {
        final ConstraintWidget first = chainHead.mFirst;
        final ConstraintWidget last = chainHead.mLast;
        final ConstraintWidget firstVisibleWidget = chainHead.mFirstVisibleWidget;
        ConstraintWidget lastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget head = chainHead.mHead;
        ConstraintWidget widget = first;
        ConstraintWidget next = null;
        boolean done = false;
        float totalWeights = chainHead.mTotalWeight;
        final ConstraintWidget firstMatchConstraintsWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget previousMatchConstraintsWidget = chainHead.mLastMatchConstraintWidget;
        final boolean isWrapContent = container.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean isChainSpread = false;
        boolean isChainSpreadInside = false;
        boolean isChainPacked = false;
        if (orientation == 0) {
            isChainSpread = (head.mHorizontalChainStyle == 0);
            isChainSpreadInside = (head.mHorizontalChainStyle == 1);
            isChainPacked = (head.mHorizontalChainStyle == 2);
        }
        else {
            isChainSpread = (head.mVerticalChainStyle == 0);
            isChainSpreadInside = (head.mVerticalChainStyle == 1);
            isChainPacked = (head.mVerticalChainStyle == 2);
        }
        while (!done) {
            final ConstraintAnchor begin = widget.mListAnchors[offset];
            int strength = 4;
            if (isWrapContent || isChainPacked) {
                strength = 1;
            }
            int margin = begin.getMargin();
            if (begin.mTarget != null && widget != first) {
                margin += begin.mTarget.getMargin();
            }
            if (isChainPacked && widget != first && widget != firstVisibleWidget) {
                strength = 6;
            }
            else if (isChainSpread && isWrapContent) {
                strength = 4;
            }
            if (begin.mTarget != null) {
                if (widget == firstVisibleWidget) {
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 5);
                }
                else {
                    system.addGreaterThan(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, 6);
                }
                system.addEquality(begin.mSolverVariable, begin.mTarget.mSolverVariable, margin, strength);
            }
            if (isWrapContent) {
                if (widget.getVisibility() != 8 && widget.mListDimensionBehaviors[orientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    system.addGreaterThan(widget.mListAnchors[offset + 1].mSolverVariable, widget.mListAnchors[offset].mSolverVariable, 0, 5);
                }
                system.addGreaterThan(widget.mListAnchors[offset].mSolverVariable, container.mListAnchors[offset].mSolverVariable, 0, 6);
            }
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
        if (lastVisibleWidget != null && last.mListAnchors[offset + 1].mTarget != null) {
            final ConstraintAnchor end = lastVisibleWidget.mListAnchors[offset + 1];
            system.addLowerThan(end.mSolverVariable, last.mListAnchors[offset + 1].mTarget.mSolverVariable, -end.getMargin(), 5);
        }
        if (isWrapContent) {
            system.addGreaterThan(container.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].mSolverVariable, last.mListAnchors[offset + 1].getMargin(), 6);
        }
        final ArrayList<ConstraintWidget> listMatchConstraints = chainHead.mWeightedMatchConstraintsWidgets;
        if (listMatchConstraints != null) {
            final int count = listMatchConstraints.size();
            if (count > 1) {
                ConstraintWidget lastMatch = null;
                float lastWeight = 0.0f;
                if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                    totalWeights = chainHead.mWidgetsMatchCount;
                }
                for (int i = 0; i < count; ++i) {
                    final ConstraintWidget match = listMatchConstraints.get(i);
                    float currentWeight = match.mWeight[orientation];
                    if (currentWeight < 0.0f) {
                        if (chainHead.mHasComplexMatchWeights) {
                            system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 4);
                            continue;
                        }
                        currentWeight = 1.0f;
                    }
                    if (currentWeight == 0.0f) {
                        system.addEquality(match.mListAnchors[offset + 1].mSolverVariable, match.mListAnchors[offset].mSolverVariable, 0, 6);
                    }
                    else {
                        if (lastMatch != null) {
                            final SolverVariable begin2 = lastMatch.mListAnchors[offset].mSolverVariable;
                            final SolverVariable end2 = lastMatch.mListAnchors[offset + 1].mSolverVariable;
                            final SolverVariable nextBegin = match.mListAnchors[offset].mSolverVariable;
                            final SolverVariable nextEnd = match.mListAnchors[offset + 1].mSolverVariable;
                            final ArrayRow row = system.createRow();
                            row.createRowEqualMatchDimensions(lastWeight, totalWeights, currentWeight, begin2, end2, nextBegin, nextEnd);
                            system.addConstraint(row);
                        }
                        lastMatch = match;
                        lastWeight = currentWeight;
                    }
                }
            }
        }
        if (firstVisibleWidget != null && (firstVisibleWidget == lastVisibleWidget || isChainPacked)) {
            ConstraintAnchor begin3 = first.mListAnchors[offset];
            ConstraintAnchor end3 = last.mListAnchors[offset + 1];
            final SolverVariable beginTarget = (first.mListAnchors[offset].mTarget != null) ? first.mListAnchors[offset].mTarget.mSolverVariable : null;
            final SolverVariable endTarget = (last.mListAnchors[offset + 1].mTarget != null) ? last.mListAnchors[offset + 1].mTarget.mSolverVariable : null;
            if (firstVisibleWidget == lastVisibleWidget) {
                begin3 = firstVisibleWidget.mListAnchors[offset];
                end3 = firstVisibleWidget.mListAnchors[offset + 1];
            }
            if (beginTarget != null && endTarget != null) {
                float bias = 0.5f;
                if (orientation == 0) {
                    bias = head.mHorizontalBiasPercent;
                }
                else {
                    bias = head.mVerticalBiasPercent;
                }
                final int beginMargin = begin3.getMargin();
                final int endMargin = end3.getMargin();
                system.addCentering(begin3.mSolverVariable, beginTarget, beginMargin, bias, endTarget, end3.mSolverVariable, endMargin, 5);
            }
        }
        else if (isChainSpread && firstVisibleWidget != null) {
            widget = firstVisibleWidget;
            ConstraintWidget previousVisibleWidget = firstVisibleWidget;
            final boolean applyFixedEquality = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            while (widget != null) {
                for (next = widget.mNextChainWidget[orientation]; next != null && next.getVisibility() == 8; next = next.mNextChainWidget[orientation]) {}
                if (next != null || widget == lastVisibleWidget) {
                    final ConstraintAnchor beginAnchor = widget.mListAnchors[offset];
                    final SolverVariable begin4 = beginAnchor.mSolverVariable;
                    SolverVariable beginTarget2 = (beginAnchor.mTarget != null) ? beginAnchor.mTarget.mSolverVariable : null;
                    if (previousVisibleWidget != widget) {
                        beginTarget2 = previousVisibleWidget.mListAnchors[offset + 1].mSolverVariable;
                    }
                    else if (widget == firstVisibleWidget && previousVisibleWidget == widget) {
                        beginTarget2 = ((first.mListAnchors[offset].mTarget != null) ? first.mListAnchors[offset].mTarget.mSolverVariable : null);
                    }
                    ConstraintAnchor beginNextAnchor = null;
                    SolverVariable beginNext = null;
                    SolverVariable beginNextTarget = null;
                    int beginMargin2 = beginAnchor.getMargin();
                    int nextMargin = widget.mListAnchors[offset + 1].getMargin();
                    if (next != null) {
                        beginNextAnchor = next.mListAnchors[offset];
                        beginNext = beginNextAnchor.mSolverVariable;
                        beginNextTarget = widget.mListAnchors[offset + 1].mSolverVariable;
                    }
                    else {
                        beginNextAnchor = last.mListAnchors[offset + 1].mTarget;
                        if (beginNextAnchor != null) {
                            beginNext = beginNextAnchor.mSolverVariable;
                        }
                        beginNextTarget = widget.mListAnchors[offset + 1].mSolverVariable;
                    }
                    if (beginNextAnchor != null) {
                        nextMargin += beginNextAnchor.getMargin();
                    }
                    if (previousVisibleWidget != null) {
                        beginMargin2 += previousVisibleWidget.mListAnchors[offset + 1].getMargin();
                    }
                    if (begin4 != null && beginTarget2 != null && beginNext != null && beginNextTarget != null) {
                        int margin2 = beginMargin2;
                        if (widget == firstVisibleWidget) {
                            margin2 = firstVisibleWidget.mListAnchors[offset].getMargin();
                        }
                        int margin3 = nextMargin;
                        if (widget == lastVisibleWidget) {
                            margin3 = lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                        }
                        int strength2 = 4;
                        if (applyFixedEquality) {
                            strength2 = 6;
                        }
                        system.addCentering(begin4, beginTarget2, margin2, 0.5f, beginNext, beginNextTarget, margin3, strength2);
                    }
                }
                if (widget.getVisibility() != 8) {
                    previousVisibleWidget = widget;
                }
                widget = next;
            }
        }
        else if (isChainSpreadInside && firstVisibleWidget != null) {
            widget = firstVisibleWidget;
            ConstraintWidget previousVisibleWidget = firstVisibleWidget;
            final boolean applyFixedEquality = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            while (widget != null) {
                for (next = widget.mNextChainWidget[orientation]; next != null && next.getVisibility() == 8; next = next.mNextChainWidget[orientation]) {}
                if (widget != firstVisibleWidget && widget != lastVisibleWidget && next != null) {
                    if (next == lastVisibleWidget) {
                        next = null;
                    }
                    final ConstraintAnchor beginAnchor = widget.mListAnchors[offset];
                    final SolverVariable begin4 = beginAnchor.mSolverVariable;
                    SolverVariable beginTarget2 = (beginAnchor.mTarget != null) ? beginAnchor.mTarget.mSolverVariable : null;
                    beginTarget2 = previousVisibleWidget.mListAnchors[offset + 1].mSolverVariable;
                    ConstraintAnchor beginNextAnchor = null;
                    SolverVariable beginNext = null;
                    SolverVariable beginNextTarget = null;
                    int beginMargin2 = beginAnchor.getMargin();
                    int nextMargin = widget.mListAnchors[offset + 1].getMargin();
                    if (next != null) {
                        beginNextAnchor = next.mListAnchors[offset];
                        beginNext = beginNextAnchor.mSolverVariable;
                        beginNextTarget = ((beginNextAnchor.mTarget != null) ? beginNextAnchor.mTarget.mSolverVariable : null);
                    }
                    else {
                        beginNextAnchor = widget.mListAnchors[offset + 1].mTarget;
                        if (beginNextAnchor != null) {
                            beginNext = beginNextAnchor.mSolverVariable;
                        }
                        beginNextTarget = widget.mListAnchors[offset + 1].mSolverVariable;
                    }
                    if (beginNextAnchor != null) {
                        nextMargin += beginNextAnchor.getMargin();
                    }
                    if (previousVisibleWidget != null) {
                        beginMargin2 += previousVisibleWidget.mListAnchors[offset + 1].getMargin();
                    }
                    int strength3 = 4;
                    if (applyFixedEquality) {
                        strength3 = 6;
                    }
                    if (begin4 != null && beginTarget2 != null && beginNext != null && beginNextTarget != null) {
                        system.addCentering(begin4, beginTarget2, beginMargin2, 0.5f, beginNext, beginNextTarget, nextMargin, strength3);
                    }
                }
                if (widget.getVisibility() != 8) {
                    previousVisibleWidget = widget;
                }
                widget = next;
            }
            final ConstraintAnchor begin5 = firstVisibleWidget.mListAnchors[offset];
            final ConstraintAnchor beginTarget3 = first.mListAnchors[offset].mTarget;
            final ConstraintAnchor end4 = lastVisibleWidget.mListAnchors[offset + 1];
            final ConstraintAnchor endTarget2 = last.mListAnchors[offset + 1].mTarget;
            if (beginTarget3 != null) {
                if (firstVisibleWidget != lastVisibleWidget) {
                    system.addEquality(begin5.mSolverVariable, beginTarget3.mSolverVariable, begin5.getMargin(), 4);
                }
                else if (endTarget2 != null) {
                    system.addCentering(begin5.mSolverVariable, beginTarget3.mSolverVariable, begin5.getMargin(), 0.5f, end4.mSolverVariable, endTarget2.mSolverVariable, end4.getMargin(), 4);
                }
            }
            if (endTarget2 != null && firstVisibleWidget != lastVisibleWidget) {
                system.addEquality(end4.mSolverVariable, endTarget2.mSolverVariable, -end4.getMargin(), 4);
            }
        }
        if ((isChainSpread || isChainSpreadInside) && firstVisibleWidget != null) {
            ConstraintAnchor begin3 = firstVisibleWidget.mListAnchors[offset];
            ConstraintAnchor end3 = lastVisibleWidget.mListAnchors[offset + 1];
            final SolverVariable beginTarget = (begin3.mTarget != null) ? begin3.mTarget.mSolverVariable : null;
            SolverVariable endTarget = (end3.mTarget != null) ? end3.mTarget.mSolverVariable : null;
            if (last != lastVisibleWidget) {
                final ConstraintAnchor realEnd = last.mListAnchors[offset + 1];
                endTarget = ((realEnd.mTarget != null) ? realEnd.mTarget.mSolverVariable : null);
            }
            if (firstVisibleWidget == lastVisibleWidget) {
                begin3 = firstVisibleWidget.mListAnchors[offset];
                end3 = firstVisibleWidget.mListAnchors[offset + 1];
            }
            if (beginTarget != null && endTarget != null) {
                final float bias = 0.5f;
                final int beginMargin = begin3.getMargin();
                if (lastVisibleWidget == null) {
                    lastVisibleWidget = last;
                }
                final int endMargin = lastVisibleWidget.mListAnchors[offset + 1].getMargin();
                system.addCentering(begin3.mSolverVariable, beginTarget, beginMargin, bias, endTarget, end3.mSolverVariable, endMargin, 5);
            }
        }
    }
}
