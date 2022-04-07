package com.smart.library.support.constraint;

import android.os.*;
import android.view.*;

public class ConstraintProperties
{
    ConstraintLayout.LayoutParams mParams;
    View mView;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    public static final int BASELINE = 5;
    public static final int START = 6;
    public static final int END = 7;
    public static final int UNSET = -1;
    public static final int PARENT_ID = 0;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int WRAP_CONTENT = -2;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public ConstraintProperties center(final int firstID, final int firstSide, final int firstMargin, final int secondId, final int secondSide, final int secondMargin, final float bias) {
        if (firstMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (secondMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (bias <= 0.0f || bias > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        }
        if (firstSide == 1 || firstSide == 2) {
            this.connect(1, firstID, firstSide, firstMargin);
            this.connect(2, secondId, secondSide, secondMargin);
            this.mParams.horizontalBias = bias;
        }
        else if (firstSide == 6 || firstSide == 7) {
            this.connect(6, firstID, firstSide, firstMargin);
            this.connect(7, secondId, secondSide, secondMargin);
            this.mParams.horizontalBias = bias;
        }
        else {
            this.connect(3, firstID, firstSide, firstMargin);
            this.connect(4, secondId, secondSide, secondMargin);
            this.mParams.verticalBias = bias;
        }
        return this;
    }
    
    public ConstraintProperties centerHorizontally(final int leftId, final int leftSide, final int leftMargin, final int rightId, final int rightSide, final int rightMargin, final float bias) {
        this.connect(1, leftId, leftSide, leftMargin);
        this.connect(2, rightId, rightSide, rightMargin);
        this.mParams.horizontalBias = bias;
        return this;
    }
    
    public ConstraintProperties centerHorizontallyRtl(final int startId, final int startSide, final int startMargin, final int endId, final int endSide, final int endMargin, final float bias) {
        this.connect(6, startId, startSide, startMargin);
        this.connect(7, endId, endSide, endMargin);
        this.mParams.horizontalBias = bias;
        return this;
    }
    
    public ConstraintProperties centerVertically(final int topId, final int topSide, final int topMargin, final int bottomId, final int bottomSide, final int bottomMargin, final float bias) {
        this.connect(3, topId, topSide, topMargin);
        this.connect(4, bottomId, bottomSide, bottomMargin);
        this.mParams.verticalBias = bias;
        return this;
    }
    
    public ConstraintProperties centerHorizontally(final int toView) {
        if (toView == 0) {
            this.center(0, 1, 0, 0, 2, 0, 0.5f);
        }
        else {
            this.center(toView, 2, 0, toView, 1, 0, 0.5f);
        }
        return this;
    }
    
    public ConstraintProperties centerHorizontallyRtl(final int toView) {
        if (toView == 0) {
            this.center(0, 6, 0, 0, 7, 0, 0.5f);
        }
        else {
            this.center(toView, 7, 0, toView, 6, 0, 0.5f);
        }
        return this;
    }
    
    public ConstraintProperties centerVertically(final int toView) {
        if (toView == 0) {
            this.center(0, 3, 0, 0, 4, 0, 0.5f);
        }
        else {
            this.center(toView, 4, 0, toView, 3, 0, 0.5f);
        }
        return this;
    }
    
    public ConstraintProperties removeConstraints(final int anchor) {
        switch (anchor) {
            case 1: {
                final ConstraintLayout.LayoutParams mParams = this.mParams;
                final ConstraintLayout.LayoutParams mParams2 = this.mParams;
                mParams.leftToRight = -1;
                final ConstraintLayout.LayoutParams mParams3 = this.mParams;
                final ConstraintLayout.LayoutParams mParams4 = this.mParams;
                mParams3.leftToLeft = -1;
                final ConstraintLayout.LayoutParams mParams5 = this.mParams;
                final ConstraintLayout.LayoutParams mParams6 = this.mParams;
                mParams5.leftMargin = -1;
                final ConstraintLayout.LayoutParams mParams7 = this.mParams;
                final ConstraintLayout.LayoutParams mParams8 = this.mParams;
                mParams7.goneLeftMargin = -1;
                break;
            }
            case 2: {
                final ConstraintLayout.LayoutParams mParams9 = this.mParams;
                final ConstraintLayout.LayoutParams mParams10 = this.mParams;
                mParams9.rightToRight = -1;
                final ConstraintLayout.LayoutParams mParams11 = this.mParams;
                final ConstraintLayout.LayoutParams mParams12 = this.mParams;
                mParams11.rightToLeft = -1;
                final ConstraintLayout.LayoutParams mParams13 = this.mParams;
                final ConstraintLayout.LayoutParams mParams14 = this.mParams;
                mParams13.rightMargin = -1;
                final ConstraintLayout.LayoutParams mParams15 = this.mParams;
                final ConstraintLayout.LayoutParams mParams16 = this.mParams;
                mParams15.goneRightMargin = -1;
                break;
            }
            case 3: {
                final ConstraintLayout.LayoutParams mParams17 = this.mParams;
                final ConstraintLayout.LayoutParams mParams18 = this.mParams;
                mParams17.topToBottom = -1;
                final ConstraintLayout.LayoutParams mParams19 = this.mParams;
                final ConstraintLayout.LayoutParams mParams20 = this.mParams;
                mParams19.topToTop = -1;
                final ConstraintLayout.LayoutParams mParams21 = this.mParams;
                final ConstraintLayout.LayoutParams mParams22 = this.mParams;
                mParams21.topMargin = -1;
                final ConstraintLayout.LayoutParams mParams23 = this.mParams;
                final ConstraintLayout.LayoutParams mParams24 = this.mParams;
                mParams23.goneTopMargin = -1;
                break;
            }
            case 4: {
                final ConstraintLayout.LayoutParams mParams25 = this.mParams;
                final ConstraintLayout.LayoutParams mParams26 = this.mParams;
                mParams25.bottomToTop = -1;
                final ConstraintLayout.LayoutParams mParams27 = this.mParams;
                final ConstraintLayout.LayoutParams mParams28 = this.mParams;
                mParams27.bottomToBottom = -1;
                final ConstraintLayout.LayoutParams mParams29 = this.mParams;
                final ConstraintLayout.LayoutParams mParams30 = this.mParams;
                mParams29.bottomMargin = -1;
                final ConstraintLayout.LayoutParams mParams31 = this.mParams;
                final ConstraintLayout.LayoutParams mParams32 = this.mParams;
                mParams31.goneBottomMargin = -1;
                break;
            }
            case 5: {
                final ConstraintLayout.LayoutParams mParams33 = this.mParams;
                final ConstraintLayout.LayoutParams mParams34 = this.mParams;
                mParams33.baselineToBaseline = -1;
                break;
            }
            case 6: {
                final ConstraintLayout.LayoutParams mParams35 = this.mParams;
                final ConstraintLayout.LayoutParams mParams36 = this.mParams;
                mParams35.startToEnd = -1;
                final ConstraintLayout.LayoutParams mParams37 = this.mParams;
                final ConstraintLayout.LayoutParams mParams38 = this.mParams;
                mParams37.startToStart = -1;
                final ConstraintLayout.LayoutParams mParams39 = this.mParams;
                final ConstraintLayout.LayoutParams mParams40 = this.mParams;
                mParams39.setMarginStart(-1);
                final ConstraintLayout.LayoutParams mParams41 = this.mParams;
                final ConstraintLayout.LayoutParams mParams42 = this.mParams;
                mParams41.goneStartMargin = -1;
                break;
            }
            case 7: {
                final ConstraintLayout.LayoutParams mParams43 = this.mParams;
                final ConstraintLayout.LayoutParams mParams44 = this.mParams;
                mParams43.endToStart = -1;
                final ConstraintLayout.LayoutParams mParams45 = this.mParams;
                final ConstraintLayout.LayoutParams mParams46 = this.mParams;
                mParams45.endToEnd = -1;
                final ConstraintLayout.LayoutParams mParams47 = this.mParams;
                final ConstraintLayout.LayoutParams mParams48 = this.mParams;
                mParams47.setMarginEnd(-1);
                final ConstraintLayout.LayoutParams mParams49 = this.mParams;
                final ConstraintLayout.LayoutParams mParams50 = this.mParams;
                mParams49.goneEndMargin = -1;
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
        }
        return this;
    }
    
    public ConstraintProperties margin(final int anchor, final int value) {
        switch (anchor) {
            case 1: {
                this.mParams.leftMargin = value;
                break;
            }
            case 2: {
                this.mParams.rightMargin = value;
                break;
            }
            case 3: {
                this.mParams.topMargin = value;
                break;
            }
            case 4: {
                this.mParams.bottomMargin = value;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                this.mParams.setMarginStart(value);
                break;
            }
            case 7: {
                this.mParams.setMarginEnd(value);
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
        }
        return this;
    }
    
    public ConstraintProperties goneMargin(final int anchor, final int value) {
        switch (anchor) {
            case 1: {
                this.mParams.goneLeftMargin = value;
                break;
            }
            case 2: {
                this.mParams.goneRightMargin = value;
                break;
            }
            case 3: {
                this.mParams.goneTopMargin = value;
                break;
            }
            case 4: {
                this.mParams.goneBottomMargin = value;
                break;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                this.mParams.goneStartMargin = value;
                break;
            }
            case 7: {
                this.mParams.goneEndMargin = value;
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
        }
        return this;
    }
    
    public ConstraintProperties horizontalBias(final float bias) {
        this.mParams.horizontalBias = bias;
        return this;
    }
    
    public ConstraintProperties verticalBias(final float bias) {
        this.mParams.verticalBias = bias;
        return this;
    }
    
    public ConstraintProperties dimensionRatio(final String ratio) {
        this.mParams.dimensionRatio = ratio;
        return this;
    }
    
    public ConstraintProperties visibility(final int visibility) {
        this.mView.setVisibility(visibility);
        return this;
    }
    
    public ConstraintProperties alpha(final float alpha) {
        this.mView.setAlpha(alpha);
        return this;
    }
    
    public ConstraintProperties elevation(final float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mView.setElevation(elevation);
        }
        return this;
    }
    
    public ConstraintProperties rotation(final float rotation) {
        this.mView.setRotation(rotation);
        return this;
    }
    
    public ConstraintProperties rotationX(final float rotationX) {
        this.mView.setRotationX(rotationX);
        return this;
    }
    
    public ConstraintProperties rotationY(final float rotationY) {
        this.mView.setRotationY(rotationY);
        return this;
    }
    
    public ConstraintProperties scaleX(final float scaleX) {
        this.mView.setScaleY(scaleX);
        return this;
    }
    
    public ConstraintProperties scaleY(final float scaleY) {
        return this;
    }
    
    public ConstraintProperties transformPivotX(final float transformPivotX) {
        this.mView.setPivotX(transformPivotX);
        return this;
    }
    
    public ConstraintProperties transformPivotY(final float transformPivotY) {
        this.mView.setPivotY(transformPivotY);
        return this;
    }
    
    public ConstraintProperties transformPivot(final float transformPivotX, final float transformPivotY) {
        this.mView.setPivotX(transformPivotX);
        this.mView.setPivotY(transformPivotY);
        return this;
    }
    
    public ConstraintProperties translationX(final float translationX) {
        this.mView.setTranslationX(translationX);
        return this;
    }
    
    public ConstraintProperties translationY(final float translationY) {
        this.mView.setTranslationY(translationY);
        return this;
    }
    
    public ConstraintProperties translation(final float translationX, final float translationY) {
        this.mView.setTranslationX(translationX);
        this.mView.setTranslationY(translationY);
        return this;
    }
    
    public ConstraintProperties translationZ(final float translationZ) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mView.setTranslationZ(translationZ);
        }
        return this;
    }
    
    public ConstraintProperties constrainHeight(final int height) {
        this.mParams.height = height;
        return this;
    }
    
    public ConstraintProperties constrainWidth(final int width) {
        this.mParams.width = width;
        return this;
    }
    
    public ConstraintProperties constrainMaxHeight(final int height) {
        this.mParams.matchConstraintMaxHeight = height;
        return this;
    }
    
    public ConstraintProperties constrainMaxWidth(final int width) {
        this.mParams.matchConstraintMaxWidth = width;
        return this;
    }
    
    public ConstraintProperties constrainMinHeight(final int height) {
        this.mParams.matchConstraintMinHeight = height;
        return this;
    }
    
    public ConstraintProperties constrainMinWidth(final int width) {
        this.mParams.matchConstraintMinWidth = width;
        return this;
    }
    
    public ConstraintProperties constrainDefaultHeight(final int height) {
        this.mParams.matchConstraintDefaultHeight = height;
        return this;
    }
    
    public ConstraintProperties constrainDefaultWidth(final int width) {
        this.mParams.matchConstraintDefaultWidth = width;
        return this;
    }
    
    public ConstraintProperties horizontalWeight(final float weight) {
        this.mParams.horizontalWeight = weight;
        return this;
    }
    
    public ConstraintProperties verticalWeight(final float weight) {
        this.mParams.verticalWeight = weight;
        return this;
    }
    
    public ConstraintProperties horizontalChainStyle(final int chainStyle) {
        this.mParams.horizontalChainStyle = chainStyle;
        return this;
    }
    
    public ConstraintProperties verticalChainStyle(final int chainStyle) {
        this.mParams.verticalChainStyle = chainStyle;
        return this;
    }
    
    public ConstraintProperties addToHorizontalChain(final int leftId, final int rightId) {
        this.connect(1, leftId, (leftId == 0) ? 1 : 2, 0);
        this.connect(2, rightId, (rightId == 0) ? 2 : 1, 0);
        if (leftId != 0) {
            final View leftView = ((ViewGroup)this.mView.getParent()).findViewById(leftId);
            final ConstraintProperties leftProp = new ConstraintProperties(leftView);
            leftProp.connect(2, this.mView.getId(), 1, 0);
        }
        if (rightId != 0) {
            final View rightView = ((ViewGroup)this.mView.getParent()).findViewById(rightId);
            final ConstraintProperties rightProp = new ConstraintProperties(rightView);
            rightProp.connect(1, this.mView.getId(), 2, 0);
        }
        return this;
    }
    
    public ConstraintProperties addToHorizontalChainRTL(final int leftId, final int rightId) {
        this.connect(6, leftId, (leftId == 0) ? 6 : 7, 0);
        this.connect(7, rightId, (rightId == 0) ? 7 : 6, 0);
        if (leftId != 0) {
            final View leftView = ((ViewGroup)this.mView.getParent()).findViewById(leftId);
            final ConstraintProperties leftProp = new ConstraintProperties(leftView);
            leftProp.connect(7, this.mView.getId(), 6, 0);
        }
        if (rightId != 0) {
            final View rightView = ((ViewGroup)this.mView.getParent()).findViewById(rightId);
            final ConstraintProperties rightProp = new ConstraintProperties(rightView);
            rightProp.connect(6, this.mView.getId(), 7, 0);
        }
        return this;
    }
    
    public ConstraintProperties addToVerticalChain(final int topId, final int bottomId) {
        this.connect(3, topId, (topId == 0) ? 3 : 4, 0);
        this.connect(4, bottomId, (bottomId == 0) ? 4 : 3, 0);
        if (topId != 0) {
            final View topView = ((ViewGroup)this.mView.getParent()).findViewById(topId);
            final ConstraintProperties topProp = new ConstraintProperties(topView);
            topProp.connect(4, this.mView.getId(), 3, 0);
        }
        if (bottomId != 0) {
            final View bottomView = ((ViewGroup)this.mView.getParent()).findViewById(bottomId);
            final ConstraintProperties bottomProp = new ConstraintProperties(bottomView);
            bottomProp.connect(3, this.mView.getId(), 4, 0);
        }
        return this;
    }
    
    public ConstraintProperties removeFromVerticalChain() {
        final int topId = this.mParams.topToBottom;
        final int bottomId = this.mParams.bottomToTop;
        final int n = topId;
        final ConstraintLayout.LayoutParams mParams = this.mParams;
        Label_0224: {
            if (n == -1) {
                final int n2 = bottomId;
                final ConstraintLayout.LayoutParams mParams2 = this.mParams;
                if (n2 == -1) {
                    break Label_0224;
                }
            }
            final View topView = ((ViewGroup)this.mView.getParent()).findViewById(topId);
            final ConstraintProperties topProp = new ConstraintProperties(topView);
            final View bottomView = ((ViewGroup)this.mView.getParent()).findViewById(bottomId);
            final ConstraintProperties bottomProp = new ConstraintProperties(bottomView);
            final int n3 = topId;
            final ConstraintLayout.LayoutParams mParams3 = this.mParams;
            if (n3 != -1) {
                final int n4 = bottomId;
                final ConstraintLayout.LayoutParams mParams4 = this.mParams;
                if (n4 != -1) {
                    topProp.connect(4, bottomId, 3, 0);
                    bottomProp.connect(3, topId, 4, 0);
                    break Label_0224;
                }
            }
            final int n5 = topId;
            final ConstraintLayout.LayoutParams mParams5 = this.mParams;
            if (n5 == -1) {
                final int n6 = bottomId;
                final ConstraintLayout.LayoutParams mParams6 = this.mParams;
                if (n6 == -1) {
                    break Label_0224;
                }
            }
            final int bottomToBottom = this.mParams.bottomToBottom;
            final ConstraintLayout.LayoutParams mParams7 = this.mParams;
            if (bottomToBottom != -1) {
                topProp.connect(4, this.mParams.bottomToBottom, 4, 0);
            }
            else {
                final int topToTop = this.mParams.topToTop;
                final ConstraintLayout.LayoutParams mParams8 = this.mParams;
                if (topToTop != -1) {
                    bottomProp.connect(3, this.mParams.topToTop, 3, 0);
                }
            }
        }
        this.removeConstraints(3);
        this.removeConstraints(4);
        return this;
    }
    
    public ConstraintProperties removeFromHorizontalChain() {
        final int leftId = this.mParams.leftToRight;
        final int rightId = this.mParams.rightToLeft;
        final int n = leftId;
        final ConstraintLayout.LayoutParams mParams = this.mParams;
        if (n == -1) {
            final int n2 = rightId;
            final ConstraintLayout.LayoutParams mParams2 = this.mParams;
            if (n2 == -1) {
                final int startId = this.mParams.startToEnd;
                final int endId = this.mParams.endToStart;
                final int n3 = startId;
                final ConstraintLayout.LayoutParams mParams3 = this.mParams;
                Label_0479: {
                    if (n3 == -1) {
                        final int n4 = endId;
                        final ConstraintLayout.LayoutParams mParams4 = this.mParams;
                        if (n4 == -1) {
                            break Label_0479;
                        }
                    }
                    final View startView = ((ViewGroup)this.mView.getParent()).findViewById(startId);
                    final ConstraintProperties startProp = new ConstraintProperties(startView);
                    final View endView = ((ViewGroup)this.mView.getParent()).findViewById(endId);
                    final ConstraintProperties endProp = new ConstraintProperties(endView);
                    final int n5 = startId;
                    final ConstraintLayout.LayoutParams mParams5 = this.mParams;
                    if (n5 != -1) {
                        final int n6 = endId;
                        final ConstraintLayout.LayoutParams mParams6 = this.mParams;
                        if (n6 != -1) {
                            startProp.connect(7, endId, 6, 0);
                            endProp.connect(6, leftId, 7, 0);
                            break Label_0479;
                        }
                    }
                    final int n7 = leftId;
                    final ConstraintLayout.LayoutParams mParams7 = this.mParams;
                    if (n7 == -1) {
                        final int n8 = endId;
                        final ConstraintLayout.LayoutParams mParams8 = this.mParams;
                        if (n8 == -1) {
                            break Label_0479;
                        }
                    }
                    final int rightToRight = this.mParams.rightToRight;
                    final ConstraintLayout.LayoutParams mParams9 = this.mParams;
                    if (rightToRight != -1) {
                        startProp.connect(7, this.mParams.rightToRight, 7, 0);
                    }
                    else {
                        final int leftToLeft = this.mParams.leftToLeft;
                        final ConstraintLayout.LayoutParams mParams10 = this.mParams;
                        if (leftToLeft != -1) {
                            endProp.connect(6, this.mParams.leftToLeft, 6, 0);
                        }
                    }
                }
                this.removeConstraints(6);
                this.removeConstraints(7);
                return this;
            }
        }
        final View leftView = ((ViewGroup)this.mView.getParent()).findViewById(leftId);
        final ConstraintProperties leftProp = new ConstraintProperties(leftView);
        final View rightView = ((ViewGroup)this.mView.getParent()).findViewById(rightId);
        final ConstraintProperties rightProp = new ConstraintProperties(rightView);
        final int n9 = leftId;
        final ConstraintLayout.LayoutParams mParams11 = this.mParams;
        Label_0224: {
            if (n9 != -1) {
                final int n10 = rightId;
                final ConstraintLayout.LayoutParams mParams12 = this.mParams;
                if (n10 != -1) {
                    leftProp.connect(2, rightId, 1, 0);
                    rightProp.connect(1, leftId, 2, 0);
                    break Label_0224;
                }
            }
            final int n11 = leftId;
            final ConstraintLayout.LayoutParams mParams13 = this.mParams;
            if (n11 == -1) {
                final int n12 = rightId;
                final ConstraintLayout.LayoutParams mParams14 = this.mParams;
                if (n12 == -1) {
                    break Label_0224;
                }
            }
            final int rightToRight2 = this.mParams.rightToRight;
            final ConstraintLayout.LayoutParams mParams15 = this.mParams;
            if (rightToRight2 != -1) {
                leftProp.connect(2, this.mParams.rightToRight, 2, 0);
            }
            else {
                final int leftToLeft2 = this.mParams.leftToLeft;
                final ConstraintLayout.LayoutParams mParams16 = this.mParams;
                if (leftToLeft2 != -1) {
                    rightProp.connect(1, this.mParams.leftToLeft, 1, 0);
                }
            }
        }
        this.removeConstraints(1);
        this.removeConstraints(2);
        return this;
    }
    
    public ConstraintProperties connect(final int startSide, final int endID, final int endSide, final int margin) {
        switch (startSide) {
            case 1: {
                if (endSide == 1) {
                    this.mParams.leftToLeft = endID;
                    final ConstraintLayout.LayoutParams mParams = this.mParams;
                    final ConstraintLayout.LayoutParams mParams2 = this.mParams;
                    mParams.leftToRight = -1;
                }
                else {
                    if (endSide != 2) {
                        throw new IllegalArgumentException("Left to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.leftToRight = endID;
                    final ConstraintLayout.LayoutParams mParams3 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams4 = this.mParams;
                    mParams3.leftToLeft = -1;
                }
                this.mParams.leftMargin = margin;
                break;
            }
            case 2: {
                if (endSide == 1) {
                    this.mParams.rightToLeft = endID;
                    final ConstraintLayout.LayoutParams mParams5 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams6 = this.mParams;
                    mParams5.rightToRight = -1;
                }
                else {
                    if (endSide != 2) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.rightToRight = endID;
                    final ConstraintLayout.LayoutParams mParams7 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams8 = this.mParams;
                    mParams7.rightToLeft = -1;
                }
                this.mParams.rightMargin = margin;
                break;
            }
            case 3: {
                if (endSide == 3) {
                    this.mParams.topToTop = endID;
                    final ConstraintLayout.LayoutParams mParams9 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams10 = this.mParams;
                    mParams9.topToBottom = -1;
                    final ConstraintLayout.LayoutParams mParams11 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams12 = this.mParams;
                    mParams11.baselineToBaseline = -1;
                }
                else {
                    if (endSide != 4) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.topToBottom = endID;
                    final ConstraintLayout.LayoutParams mParams13 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams14 = this.mParams;
                    mParams13.topToTop = -1;
                    final ConstraintLayout.LayoutParams mParams15 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams16 = this.mParams;
                    mParams15.baselineToBaseline = -1;
                }
                this.mParams.topMargin = margin;
                break;
            }
            case 4: {
                if (endSide == 4) {
                    this.mParams.bottomToBottom = endID;
                    final ConstraintLayout.LayoutParams mParams17 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams18 = this.mParams;
                    mParams17.bottomToTop = -1;
                    final ConstraintLayout.LayoutParams mParams19 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams20 = this.mParams;
                    mParams19.baselineToBaseline = -1;
                }
                else {
                    if (endSide != 3) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.bottomToTop = endID;
                    final ConstraintLayout.LayoutParams mParams21 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams22 = this.mParams;
                    mParams21.bottomToBottom = -1;
                    final ConstraintLayout.LayoutParams mParams23 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams24 = this.mParams;
                    mParams23.baselineToBaseline = -1;
                }
                this.mParams.bottomMargin = margin;
                break;
            }
            case 5: {
                if (endSide == 5) {
                    this.mParams.baselineToBaseline = endID;
                    final ConstraintLayout.LayoutParams mParams25 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams26 = this.mParams;
                    mParams25.bottomToBottom = -1;
                    final ConstraintLayout.LayoutParams mParams27 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams28 = this.mParams;
                    mParams27.bottomToTop = -1;
                    final ConstraintLayout.LayoutParams mParams29 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams30 = this.mParams;
                    mParams29.topToTop = -1;
                    final ConstraintLayout.LayoutParams mParams31 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams32 = this.mParams;
                    mParams31.topToBottom = -1;
                    break;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
            }
            case 6: {
                if (endSide == 6) {
                    this.mParams.startToStart = endID;
                    final ConstraintLayout.LayoutParams mParams33 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams34 = this.mParams;
                    mParams33.startToEnd = -1;
                }
                else {
                    if (endSide != 7) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.startToEnd = endID;
                    final ConstraintLayout.LayoutParams mParams35 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams36 = this.mParams;
                    mParams35.startToStart = -1;
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    this.mParams.setMarginStart(margin);
                    break;
                }
                break;
            }
            case 7: {
                if (endSide == 7) {
                    this.mParams.endToEnd = endID;
                    final ConstraintLayout.LayoutParams mParams37 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams38 = this.mParams;
                    mParams37.endToStart = -1;
                }
                else {
                    if (endSide != 6) {
                        throw new IllegalArgumentException("right to " + this.sideToString(endSide) + " undefined");
                    }
                    this.mParams.endToStart = endID;
                    final ConstraintLayout.LayoutParams mParams39 = this.mParams;
                    final ConstraintLayout.LayoutParams mParams40 = this.mParams;
                    mParams39.endToEnd = -1;
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    this.mParams.setMarginEnd(margin);
                    break;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException(this.sideToString(startSide) + " to " + this.sideToString(endSide) + " unknown");
            }
        }
        return this;
    }
    
    private String sideToString(final int side) {
        switch (side) {
            case 1: {
                return "left";
            }
            case 2: {
                return "right";
            }
            case 3: {
                return "top";
            }
            case 4: {
                return "bottom";
            }
            case 5: {
                return "baseline";
            }
            case 6: {
                return "start";
            }
            case 7: {
                return "end";
            }
            default: {
                return "undefined";
            }
        }
    }
    
    public ConstraintProperties(final View view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof ConstraintLayout.LayoutParams) {
            this.mParams = (ConstraintLayout.LayoutParams)params;
            this.mView = view;
            return;
        }
        throw new RuntimeException("Only children of ConstraintLayout.LayoutParams supported");
    }
    
    public void apply() {
    }
}
