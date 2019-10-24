package com.smart.library.support.constraint.solver.widgets;

import android.view.View;

import java.util.*;
import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

public class Flow extends VirtualLayout
{
    public static final int HORIZONTAL_ALIGN_START = 0;
    public static final int HORIZONTAL_ALIGN_END = 1;
    public static final int HORIZONTAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int VERTICAL_ALIGN_BOTTOM = 1;
    public static final int VERTICAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    public static final int WRAP_NONE = 0;
    public static final int WRAP_CHAIN = 1;
    public static final int WRAP_ALIGNED = 2;
    private int mHorizontalStyle;
    private int mVerticalStyle;
    private int mFirstHorizontalStyle;
    private int mFirstVerticalStyle;
    private int mLastHorizontalStyle;
    private int mLastVerticalStyle;
    private float mHorizontalBias;
    private float mVerticalBias;
    private float mFirstHorizontalBias;
    private float mFirstVerticalBias;
    private float mLastHorizontalBias;
    private float mLastVerticalBias;
    private int mHorizontalGap;
    private int mVerticalGap;
    private int mHorizontalAlign;
    private int mVerticalAlign;
    private int mWrapMode;
    private int mMaxElementsWrap;
    private int mOrientation;
    private ArrayList<WidgetsList> mChainList;
    private ConstraintWidget[] mAlignedBiggestElementsInRows;
    private ConstraintWidget[] mAlignedBiggestElementsInCols;
    private int[] mAlignedDimensions;
    
    public Flow() {
        this.mHorizontalStyle = -1;
        this.mVerticalStyle = -1;
        this.mFirstHorizontalStyle = -1;
        this.mFirstVerticalStyle = -1;
        this.mLastHorizontalStyle = -1;
        this.mLastVerticalStyle = -1;
        this.mHorizontalBias = 0.5f;
        this.mVerticalBias = 0.5f;
        this.mFirstHorizontalBias = 0.5f;
        this.mFirstVerticalBias = 0.5f;
        this.mLastHorizontalBias = 0.5f;
        this.mLastVerticalBias = 0.5f;
        this.mHorizontalGap = 0;
        this.mVerticalGap = 0;
        this.mHorizontalAlign = 2;
        this.mVerticalAlign = 2;
        this.mWrapMode = 0;
        this.mMaxElementsWrap = -1;
        this.mOrientation = 0;
        this.mChainList = new ArrayList<WidgetsList>();
        this.mAlignedBiggestElementsInRows = null;
        this.mAlignedBiggestElementsInCols = null;
        this.mAlignedDimensions = null;
    }
    
    @Override
    public void copy(final ConstraintWidget src, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        final Flow srcFLow = (Flow)src;
        this.mHorizontalStyle = srcFLow.mHorizontalStyle;
        this.mVerticalStyle = srcFLow.mVerticalStyle;
        this.mFirstHorizontalStyle = srcFLow.mFirstHorizontalStyle;
        this.mFirstVerticalStyle = srcFLow.mFirstVerticalStyle;
        this.mLastHorizontalStyle = srcFLow.mLastHorizontalStyle;
        this.mLastVerticalStyle = srcFLow.mLastVerticalStyle;
        this.mHorizontalBias = srcFLow.mHorizontalBias;
        this.mVerticalBias = srcFLow.mVerticalBias;
        this.mFirstHorizontalBias = srcFLow.mFirstHorizontalBias;
        this.mFirstVerticalBias = srcFLow.mFirstVerticalBias;
        this.mLastHorizontalBias = srcFLow.mLastHorizontalBias;
        this.mLastVerticalBias = srcFLow.mLastVerticalBias;
        this.mHorizontalGap = srcFLow.mHorizontalGap;
        this.mVerticalGap = srcFLow.mVerticalGap;
        this.mHorizontalAlign = srcFLow.mHorizontalAlign;
        this.mVerticalAlign = srcFLow.mVerticalAlign;
        this.mWrapMode = srcFLow.mWrapMode;
        this.mMaxElementsWrap = srcFLow.mMaxElementsWrap;
        this.mOrientation = srcFLow.mOrientation;
    }
    
    public void setOrientation(final int value) {
        this.mOrientation = value;
    }
    
    public void setFirstHorizontalStyle(final int value) {
        this.mFirstHorizontalStyle = value;
    }
    
    public void setFirstVerticalStyle(final int value) {
        this.mFirstVerticalStyle = value;
    }
    
    public void setLastHorizontalStyle(final int value) {
        this.mLastHorizontalStyle = value;
    }
    
    public void setLastVerticalStyle(final int value) {
        this.mLastVerticalStyle = value;
    }
    
    public void setHorizontalStyle(final int value) {
        this.mHorizontalStyle = value;
    }
    
    public void setVerticalStyle(final int value) {
        this.mVerticalStyle = value;
    }
    
    public void setHorizontalBias(final float value) {
        this.mHorizontalBias = value;
    }
    
    public void setVerticalBias(final float value) {
        this.mVerticalBias = value;
    }
    
    public void setFirstHorizontalBias(final float value) {
        this.mFirstHorizontalBias = value;
    }
    
    public void setFirstVerticalBias(final float value) {
        this.mFirstVerticalBias = value;
    }
    
    public void setLastHorizontalBias(final float value) {
        this.mLastHorizontalBias = value;
    }
    
    public void setLastVerticalBias(final float value) {
        this.mLastVerticalBias = value;
    }
    
    public void setHorizontalAlign(final int value) {
        this.mHorizontalAlign = value;
    }
    
    public void setVerticalAlign(final int value) {
        this.mVerticalAlign = value;
    }
    
    public void setWrapMode(final int value) {
        this.mWrapMode = value;
    }
    
    public void setHorizontalGap(final int value) {
        this.mHorizontalGap = value;
    }
    
    public void setVerticalGap(final int value) {
        this.mVerticalGap = value;
    }
    
    public void setMaxElementsWrap(final int value) {
        this.mMaxElementsWrap = value;
    }
    
    private final int getWidgetWidth(final ConstraintWidget widget) {
        if (widget == null) {
            return 0;
        }
        if (widget.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultWidth == 0) {
            return 0;
        }
        return widget.getWidth();
    }
    
    private final int getWidgetHeight(final ConstraintWidget widget) {
        if (widget == null) {
            return 0;
        }
        if (widget.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultHeight == 0) {
            return 0;
        }
        return widget.getHeight();
    }
    
    @Override
    public void measure(final int widthMode, final int widthSize, final int heightMode, final int heightSize) {
        if (this.mWidgetsCount > 0 && !this.measureChildren()) {
            this.setMeasure(0, 0);
            this.needsCallbackFromSolver(false);
            return;
        }
        int width = 0;
        int height = 0;
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int[] measured = new int[2];
        int max = widthSize - paddingLeft - paddingRight;
        if (this.mOrientation == 1) {
            max = heightSize - paddingTop - paddingBottom;
        }
        if (this.mOrientation == 0) {
            if (this.mHorizontalStyle == -1) {
                this.mHorizontalStyle = 0;
            }
            if (this.mVerticalStyle == -1) {
                this.mVerticalStyle = 0;
            }
        }
        else {
            if (this.mHorizontalStyle == -1) {
                this.mHorizontalStyle = 0;
            }
            if (this.mVerticalStyle == -1) {
                this.mVerticalStyle = 0;
            }
        }
        switch (this.mWrapMode) {
            case 2: {
                this.measureAligned(this.mWidgets, this.mOrientation, max, measured);
                break;
            }
            case 1: {
                this.measureChainWrap(this.mWidgets, this.mOrientation, max, measured);
                break;
            }
            case 0: {
                this.measureNoWrap(this.mWidgets, this.mOrientation, max, measured);
                break;
            }
        }
        width = measured[0] + paddingLeft + paddingRight;
        height = measured[1] + paddingTop + paddingBottom;
        int measuredWidth = 0;
        int measuredHeight = 0;
        if (widthMode == View.MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        }
        else if (widthMode == Integer.MIN_VALUE) {
            measuredWidth = Math.min(width, widthSize);
        }
        else if (widthMode == 0) {
            measuredWidth = width;
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        }
        else if (heightMode == Integer.MIN_VALUE) {
            measuredHeight = Math.min(height, heightSize);
        }
        else if (heightMode == 0) {
            measuredHeight = height;
        }
        this.setMeasure(measuredWidth, measuredHeight);
        this.needsCallbackFromSolver(this.mWidgetsCount > 0);
    }
    
    private void measureChainWrap(final ConstraintWidget[] widgets, final int orientation, final int max, final int[] measured) {
        final int count = this.mWidgetsCount;
        if (count == 0) {
            return;
        }
        this.mChainList.clear();
        WidgetsList list = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom);
        this.mChainList.add(list);
        if (orientation == 0) {
            int width = this.mHorizontalGap * 2;
            for (int i = 0; i < count; ++i) {
                final ConstraintWidget widget = widgets[i];
                final int w = this.getWidgetWidth(widget);
                boolean doWrap = width + w + this.mHorizontalGap > max && list.biggest != null;
                if (!doWrap && i > 0 && this.mMaxElementsWrap > 0 && i % this.mMaxElementsWrap == 0) {
                    doWrap = true;
                }
                if (doWrap) {
                    width = this.mHorizontalGap * 2;
                    list = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom);
                    list.setStartIndex(i);
                    this.mChainList.add(list);
                }
                width += w + this.mHorizontalGap;
                list.add(widget);
            }
        }
        else {
            int height = this.mVerticalGap * 2;
            for (int i = 0; i < count; ++i) {
                final ConstraintWidget widget = widgets[i];
                final int h = this.getWidgetHeight(widget);
                boolean doWrap = height + h + this.mVerticalGap > max && list.biggest != null;
                if (!doWrap && i > 0 && this.mMaxElementsWrap > 0 && i % this.mMaxElementsWrap == 0) {
                    doWrap = true;
                }
                if (doWrap) {
                    height = this.mVerticalGap * 2;
                    list = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom);
                    list.setStartIndex(i);
                    this.mChainList.add(list);
                }
                height += h;
                list.add(widget);
            }
        }
        final int listCount = this.mChainList.size();
        ConstraintAnchor left = this.mLeft;
        ConstraintAnchor top = this.mTop;
        ConstraintAnchor right = this.mRight;
        ConstraintAnchor bottom = this.mBottom;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int maxWidth = 0;
        int maxHeight = 0;
        for (int j = 0; j < listCount; ++j) {
            final WidgetsList current = this.mChainList.get(j);
            if (orientation == 0) {
                if (j < listCount - 1) {
                    final WidgetsList next = this.mChainList.get(j + 1);
                    bottom = next.biggest.mTop;
                    paddingBottom = 0;
                }
                else {
                    bottom = this.mBottom;
                    paddingBottom = this.getPaddingBottom();
                }
                final ConstraintAnchor currentBottom = current.biggest.mBottom;
                current.setup(orientation, left, top, right, bottom, paddingLeft, paddingTop, paddingRight, paddingBottom);
                top = currentBottom;
                paddingTop = 0;
                maxWidth = Math.max(maxWidth, current.getWidth());
                maxHeight += current.getHeight();
                if (j > 0) {
                    maxHeight += this.mVerticalGap;
                }
            }
            else {
                if (j < listCount - 1) {
                    final WidgetsList next = this.mChainList.get(j + 1);
                    right = next.biggest.mLeft;
                    paddingRight = 0;
                }
                else {
                    right = this.mRight;
                    paddingRight = this.getPaddingRight();
                }
                final ConstraintAnchor currentRight = current.biggest.mRight;
                current.setup(orientation, left, top, right, bottom, paddingLeft, paddingTop, paddingRight, paddingBottom);
                left = currentRight;
                paddingLeft = 0;
                maxWidth += current.getWidth();
                maxHeight = Math.max(maxHeight, current.getHeight());
                if (j > 0) {
                    maxWidth += this.mHorizontalGap;
                }
            }
        }
        measured[0] = maxWidth;
        measured[1] = maxHeight;
    }
    
    private void measureNoWrap(final ConstraintWidget[] widgets, final int orientation, final int max, final int[] measured) {
        final int width = 0;
        final int height = 0;
        final int count = this.mWidgetsCount;
        if (count == 0) {
            return;
        }
        WidgetsList list = null;
        if (this.mChainList.size() == 0) {
            list = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mChainList.add(list);
        }
        else {
            list = this.mChainList.get(0);
            list.clear();
            list.setup(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
        for (final ConstraintWidget widget : widgets) {
            list.add(widget);
        }
        measured[0] = list.getWidth();
        measured[1] = list.getHeight();
    }
    
    private void measureAligned(final ConstraintWidget[] widgets, final int orientation, final int max, final int[] measured) {
        boolean done = false;
        int rows = 0;
        int cols = 0;
        if (orientation == 0) {
            cols = this.mMaxElementsWrap;
            if (cols <= 0) {
                int w = 0;
                cols = 0;
                for (int i = 0; i < this.mWidgetsCount; ++i) {
                    if (i > 0) {
                        w += this.mHorizontalGap;
                    }
                    final ConstraintWidget widget = widgets[i];
                    if (widget != null) {
                        w += this.getWidgetWidth(widget);
                        if (w > max) {
                            break;
                        }
                        ++cols;
                    }
                }
            }
        }
        else {
            rows = this.mMaxElementsWrap;
            if (rows <= 0) {
                int h = 0;
                rows = 0;
                for (int i = 0; i < this.mWidgetsCount; ++i) {
                    if (i > 0) {
                        h += this.mVerticalGap;
                    }
                    final ConstraintWidget widget = widgets[i];
                    if (widget != null) {
                        h += this.getWidgetHeight(widget);
                        if (h > max) {
                            break;
                        }
                        ++rows;
                    }
                }
            }
        }
        if (this.mAlignedDimensions == null) {
            this.mAlignedDimensions = new int[2];
        }
        if ((rows == 0 && orientation == 1) || (cols == 0 && orientation == 0)) {
            done = true;
        }
        while (!done) {
            if (orientation == 0) {
                rows = (int)Math.ceil(this.mWidgetsCount / cols);
            }
            else {
                cols = (int)Math.ceil(this.mWidgetsCount / rows);
            }
            if (this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInCols.length < cols) {
                this.mAlignedBiggestElementsInCols = new ConstraintWidget[cols];
            }
            else {
                Arrays.fill(this.mAlignedBiggestElementsInCols, null);
            }
            if (this.mAlignedBiggestElementsInRows == null || this.mAlignedBiggestElementsInRows.length < rows) {
                this.mAlignedBiggestElementsInRows = new ConstraintWidget[rows];
            }
            else {
                Arrays.fill(this.mAlignedBiggestElementsInRows, null);
            }
            for (int j = 0; j < cols; ++j) {
                for (int k = 0; k < rows; ++k) {
                    int index = k * cols + j;
                    if (orientation == 1) {
                        index = j * rows + k;
                    }
                    if (index < widgets.length) {
                        final ConstraintWidget widget2 = widgets[index];
                        if (widget2 != null) {
                            final int w2 = this.getWidgetWidth(widget2);
                            if (this.mAlignedBiggestElementsInCols[j] == null || this.mAlignedBiggestElementsInCols[j].getWidth() < w2) {
                                this.mAlignedBiggestElementsInCols[j] = widget2;
                            }
                            final int h2 = this.getWidgetHeight(widget2);
                            if (this.mAlignedBiggestElementsInRows[k] == null || this.mAlignedBiggestElementsInRows[k].getHeight() < h2) {
                                this.mAlignedBiggestElementsInRows[k] = widget2;
                            }
                        }
                    }
                }
            }
            int w = 0;
            for (int i = 0; i < cols; ++i) {
                final ConstraintWidget widget = this.mAlignedBiggestElementsInCols[i];
                if (widget != null) {
                    if (i > 0) {
                        w += this.mHorizontalGap;
                    }
                    w += this.getWidgetWidth(widget);
                }
            }
            int h3 = 0;
            for (int l = 0; l < rows; ++l) {
                final ConstraintWidget widget2 = this.mAlignedBiggestElementsInRows[l];
                if (widget2 != null) {
                    if (l > 0) {
                        h3 += this.mVerticalGap;
                    }
                    h3 += this.getWidgetHeight(widget2);
                }
            }
            measured[0] = w;
            measured[1] = h3;
            if (orientation == 0) {
                if (w > max) {
                    if (cols > 1) {
                        --cols;
                    }
                    else {
                        done = true;
                    }
                }
                else {
                    done = true;
                }
            }
            else if (h3 > max) {
                if (rows > 1) {
                    --rows;
                }
                else {
                    done = true;
                }
            }
            else {
                done = true;
            }
        }
        this.mAlignedDimensions[0] = cols;
        this.mAlignedDimensions[1] = rows;
    }
    
    private void createAlignedConstraints(final boolean isInRtl) {
        if (this.mAlignedDimensions == null || this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInRows == null) {
            return;
        }
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            final ConstraintWidget widget = this.mWidgets[i];
            widget.resetAnchors();
        }
        final int cols = this.mAlignedDimensions[0];
        final int rows = this.mAlignedDimensions[1];
        ConstraintWidget previous = null;
        for (int j = 0; j < cols; ++j) {
            int index = j;
            if (isInRtl) {
                index = cols - j - 1;
            }
            final ConstraintWidget widget2 = this.mAlignedBiggestElementsInCols[index];
            if (widget2 != null) {
                if (j == 0) {
                    widget2.connect(widget2.mLeft, this.mLeft, this.getPaddingLeft());
                    widget2.setHorizontalChainStyle(this.mHorizontalStyle);
                    widget2.setHorizontalBiasPercent(this.mHorizontalBias);
                }
                if (j == cols - 1) {
                    widget2.connect(widget2.mRight, this.mRight, this.getPaddingRight());
                }
                if (j > 0) {
                    widget2.connect(widget2.mLeft, previous.mRight, this.mHorizontalGap);
                    previous.connect(previous.mRight, widget2.mLeft, 0);
                }
                previous = widget2;
            }
        }
        for (int k = 0; k < rows; ++k) {
            final ConstraintWidget widget3 = this.mAlignedBiggestElementsInRows[k];
            if (widget3 != null) {
                if (k == 0) {
                    widget3.connect(widget3.mTop, this.mTop, this.getPaddingTop());
                    widget3.setVerticalChainStyle(this.mVerticalStyle);
                    widget3.setVerticalBiasPercent(this.mVerticalBias);
                }
                if (k == rows - 1) {
                    widget3.connect(widget3.mBottom, this.mBottom, this.getPaddingBottom());
                }
                if (k > 0) {
                    widget3.connect(widget3.mTop, previous.mBottom, this.mVerticalGap);
                    previous.connect(previous.mBottom, widget3.mTop, 0);
                }
                previous = widget3;
            }
        }
        for (int j = 0; j < cols; ++j) {
            for (int l = 0; l < rows; ++l) {
                int index2 = l * cols + j;
                if (this.mOrientation == 1) {
                    index2 = j * rows + l;
                }
                if (index2 < this.mWidgets.length) {
                    final ConstraintWidget widget4 = this.mWidgets[index2];
                    if (widget4 != null) {
                        final ConstraintWidget biggestInCol = this.mAlignedBiggestElementsInCols[j];
                        final ConstraintWidget biggestInRow = this.mAlignedBiggestElementsInRows[l];
                        if (widget4 != biggestInCol) {
                            widget4.connect(widget4.mLeft, biggestInCol.mLeft, 0);
                            widget4.connect(widget4.mRight, biggestInCol.mRight, 0);
                        }
                        if (widget4 != biggestInRow) {
                            widget4.connect(widget4.mTop, biggestInRow.mTop, 0);
                            widget4.connect(widget4.mBottom, biggestInRow.mBottom, 0);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void addToSolver(final LinearSystem system) {
        super.addToSolver(system);
        final boolean isInRtl = this.getParent() != null && ((ConstraintWidgetContainer)this.getParent()).isRtl();
        switch (this.mWrapMode) {
            case 1: {
                for (int count = this.mChainList.size(), i = 0; i < count; ++i) {
                    final WidgetsList list = this.mChainList.get(i);
                    list.createConstraints(isInRtl, i, i == count - 1);
                }
                break;
            }
            case 0: {
                if (this.mChainList.size() > 0) {
                    final WidgetsList list2 = this.mChainList.get(0);
                    list2.createConstraints(isInRtl, 0, true);
                    break;
                }
                break;
            }
            case 2: {
                this.createAlignedConstraints(isInRtl);
                break;
            }
        }
        this.needsCallbackFromSolver(false);
    }
    
    private class WidgetsList
    {
        private int mOrientation;
        private ConstraintWidget biggest;
        int biggestDimension;
        private ConstraintAnchor mLeft;
        private ConstraintAnchor mTop;
        private ConstraintAnchor mRight;
        private ConstraintAnchor mBottom;
        private int mPaddingLeft;
        private int mPaddingTop;
        private int mPaddingRight;
        private int mPaddingBottom;
        private int mWidth;
        private int mHeight;
        private int mStartIndex;
        private int mCount;
        
        public WidgetsList(final int orientation, final ConstraintAnchor left, final ConstraintAnchor top, final ConstraintAnchor right, final ConstraintAnchor bottom) {
            this.mOrientation = 0;
            this.biggest = null;
            this.biggestDimension = 0;
            this.mPaddingLeft = 0;
            this.mPaddingTop = 0;
            this.mPaddingRight = 0;
            this.mPaddingBottom = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mStartIndex = 0;
            this.mCount = 0;
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = Flow.this.getPaddingLeft();
            this.mPaddingTop = Flow.this.getPaddingTop();
            this.mPaddingRight = Flow.this.getPaddingRight();
            this.mPaddingBottom = Flow.this.getPaddingBottom();
        }
        
        public void setup(final int orientation, final ConstraintAnchor left, final ConstraintAnchor top, final ConstraintAnchor right, final ConstraintAnchor bottom, final int paddingLeft, final int paddingTop, final int paddingRight, final int paddingBottom) {
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = paddingLeft;
            this.mPaddingTop = paddingTop;
            this.mPaddingRight = paddingRight;
            this.mPaddingBottom = paddingBottom;
        }
        
        public void clear() {
            this.biggestDimension = 0;
            this.biggest = null;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mStartIndex = 0;
            this.mCount = 0;
        }
        
        public void setStartIndex(final int value) {
            this.mStartIndex = value;
        }
        
        public int getWidth() {
            if (this.mOrientation == 0) {
                return this.mWidth - Flow.this.mHorizontalGap;
            }
            return this.mWidth;
        }
        
        public int getHeight() {
            if (this.mOrientation == 1) {
                return this.mHeight - Flow.this.mVerticalGap;
            }
            return this.mHeight;
        }
        
        public void add(final ConstraintWidget widget) {
            if (this.mOrientation == 0) {
                final int width = Flow.this.getWidgetWidth(widget);
                int gap = Flow.this.mHorizontalGap;
                if (widget.getVisibility() == 8) {
                    gap = 0;
                }
                this.mWidth += width + gap;
                final int height = Flow.this.getWidgetHeight(widget);
                if (this.biggest == null || this.biggestDimension < height) {
                    this.biggest = widget;
                    this.biggestDimension = height;
                    this.mHeight = height;
                }
            }
            else {
                final int width = Flow.this.getWidgetWidth(widget);
                final int height2 = Flow.this.getWidgetHeight(widget);
                int gap2 = Flow.this.mVerticalGap;
                if (widget.getVisibility() == 8) {
                    gap2 = 0;
                }
                this.mHeight += height2 + gap2;
                if (this.biggest == null || this.biggestDimension < width) {
                    this.biggest = widget;
                    this.biggestDimension = width;
                    this.mWidth = width;
                }
            }
            ++this.mCount;
        }
        
        public void createConstraints(final boolean isInRtl, final int chainIndex, final boolean isLastChain) {
            final int count = this.mCount;
            for (int i = 0; i < count; ++i) {
                final ConstraintWidget widget = Flow.this.mWidgets[this.mStartIndex + i];
                widget.resetAnchors();
            }
            if (count == 0 || this.biggest == null) {
                return;
            }
            final boolean singleChain = isLastChain && chainIndex == 0;
            int firstVisible = -1;
            int lastVisible = -1;
            for (int j = 0; j < count; ++j) {
                int index = j;
                if (isInRtl) {
                    index = count - 1 - j;
                }
                final ConstraintWidget widget2 = Flow.this.mWidgets[this.mStartIndex + index];
                if (widget2.getVisibility() == 0) {
                    if (firstVisible == -1) {
                        firstVisible = j;
                    }
                    lastVisible = j;
                }
            }
            ConstraintWidget previous = null;
            if (this.mOrientation == 0) {
                final ConstraintWidget verticalWidget = this.biggest;
                verticalWidget.setVerticalChainStyle(Flow.this.mVerticalStyle);
                int padding = this.mPaddingTop;
                if (chainIndex > 0) {
                    padding += Flow.this.mVerticalGap;
                }
                verticalWidget.mTop.connect(this.mTop, padding);
                if (isLastChain) {
                    verticalWidget.mBottom.connect(this.mBottom, this.mPaddingBottom);
                }
                if (chainIndex > 0) {
                    final ConstraintAnchor bottom = this.mTop.mOwner.mBottom;
                    bottom.connect(verticalWidget.mTop, 0);
                }
                ConstraintWidget baselineVerticalWidget = verticalWidget;
                if (Flow.this.mVerticalAlign == 3 && !verticalWidget.hasBaseline()) {
                    for (int k = 0; k < count; ++k) {
                        int index2 = k;
                        if (isInRtl) {
                            index2 = count - 1 - k;
                        }
                        final ConstraintWidget widget3 = Flow.this.mWidgets[this.mStartIndex + index2];
                        if (widget3.hasBaseline()) {
                            baselineVerticalWidget = widget3;
                            break;
                        }
                    }
                }
                for (int k = 0; k < count; ++k) {
                    int index2 = k;
                    if (isInRtl) {
                        index2 = count - 1 - k;
                    }
                    final ConstraintWidget widget3 = Flow.this.mWidgets[this.mStartIndex + index2];
                    if (k == 0) {
                        widget3.connect(widget3.mLeft, this.mLeft, this.mPaddingLeft);
                    }
                    if (index2 == 0) {
                        int style = Flow.this.mHorizontalStyle;
                        float bias = Flow.this.mHorizontalBias;
                        if (this.mStartIndex == 0 && Flow.this.mFirstHorizontalStyle != -1) {
                            style = Flow.this.mFirstHorizontalStyle;
                            bias = Flow.this.mFirstHorizontalBias;
                        }
                        else if (isLastChain && Flow.this.mLastHorizontalStyle != -1) {
                            style = Flow.this.mLastHorizontalStyle;
                            bias = Flow.this.mLastHorizontalBias;
                        }
                        widget3.setHorizontalChainStyle(style);
                        widget3.setHorizontalBiasPercent(bias);
                    }
                    if (k == count - 1) {
                        widget3.connect(widget3.mRight, this.mRight, this.mPaddingRight);
                    }
                    if (previous != null) {
                        widget3.mLeft.connect(previous.mRight, Flow.this.mHorizontalGap);
                        if (k == firstVisible) {
                            widget3.mLeft.setGoneMargin(this.mPaddingLeft);
                        }
                        previous.mRight.connect(widget3.mLeft, 0);
                        if (k == lastVisible + 1) {
                            previous.mRight.setGoneMargin(this.mPaddingRight);
                        }
                    }
                    if (widget3 != verticalWidget) {
                        if (Flow.this.mVerticalAlign == 3 && baselineVerticalWidget.hasBaseline() && widget3 != baselineVerticalWidget && widget3.hasBaseline()) {
                            widget3.mBaseline.connect(baselineVerticalWidget.mBaseline, 0);
                        }
                        else {
                            switch (Flow.this.mVerticalAlign) {
                                case 0: {
                                    widget3.mTop.connect(verticalWidget.mTop, 0);
                                    break;
                                }
                                case 1: {
                                    widget3.mBottom.connect(verticalWidget.mBottom, 0);
                                    break;
                                }
                                default: {
                                    if (singleChain) {
                                        widget3.mTop.connect(this.mTop, this.mPaddingTop);
                                        widget3.mBottom.connect(this.mBottom, this.mPaddingBottom);
                                        break;
                                    }
                                    widget3.mTop.connect(verticalWidget.mTop, 0);
                                    widget3.mBottom.connect(verticalWidget.mBottom, 0);
                                    break;
                                }
                            }
                        }
                    }
                    previous = widget3;
                }
            }
            else {
                final ConstraintWidget horizontalWidget = this.biggest;
                horizontalWidget.setHorizontalChainStyle(Flow.this.mVerticalStyle);
                int padding = this.mPaddingLeft;
                if (chainIndex > 0) {
                    padding += Flow.this.mHorizontalGap;
                }
                if (isInRtl) {
                    horizontalWidget.mRight.connect(this.mRight, padding);
                    if (isLastChain) {
                        horizontalWidget.mLeft.connect(this.mLeft, this.mPaddingRight);
                    }
                    if (chainIndex > 0) {
                        final ConstraintAnchor left = this.mRight.mOwner.mLeft;
                        left.connect(horizontalWidget.mRight, 0);
                    }
                }
                else {
                    horizontalWidget.mLeft.connect(this.mLeft, padding);
                    if (isLastChain) {
                        horizontalWidget.mRight.connect(this.mRight, this.mPaddingRight);
                    }
                    if (chainIndex > 0) {
                        final ConstraintAnchor right = this.mLeft.mOwner.mRight;
                        right.connect(horizontalWidget.mLeft, 0);
                    }
                }
                for (int l = 0; l < count; ++l) {
                    final ConstraintWidget widget4 = Flow.this.mWidgets[this.mStartIndex + l];
                    if (l == 0) {
                        widget4.connect(widget4.mTop, this.mTop, this.mPaddingTop);
                        int style2 = Flow.this.mVerticalStyle;
                        float bias2 = Flow.this.mVerticalBias;
                        if (this.mStartIndex == 0 && Flow.this.mFirstVerticalStyle != -1) {
                            style2 = Flow.this.mFirstVerticalStyle;
                            bias2 = Flow.this.mFirstVerticalBias;
                        }
                        else if (isLastChain && Flow.this.mLastVerticalStyle != -1) {
                            style2 = Flow.this.mLastVerticalStyle;
                            bias2 = Flow.this.mLastVerticalBias;
                        }
                        widget4.setVerticalChainStyle(style2);
                        widget4.setVerticalBiasPercent(bias2);
                    }
                    if (l == count - 1) {
                        widget4.connect(widget4.mBottom, this.mBottom, this.mPaddingBottom);
                    }
                    if (previous != null) {
                        widget4.mTop.connect(previous.mBottom, Flow.this.mVerticalGap);
                        if (l == firstVisible) {
                            widget4.mTop.setGoneMargin(this.mPaddingTop);
                        }
                        previous.mBottom.connect(widget4.mTop, 0);
                        if (l == lastVisible + 1) {
                            previous.mBottom.setGoneMargin(this.mPaddingBottom);
                        }
                    }
                    if (widget4 != horizontalWidget) {
                        if (isInRtl) {
                            switch (Flow.this.mHorizontalAlign) {
                                case 0: {
                                    widget4.mRight.connect(horizontalWidget.mRight, 0);
                                    break;
                                }
                                case 2: {
                                    widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                    widget4.mRight.connect(horizontalWidget.mRight, 0);
                                    break;
                                }
                                case 1: {
                                    widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                    break;
                                }
                            }
                        }
                        else {
                            switch (Flow.this.mHorizontalAlign) {
                                case 0: {
                                    widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                    break;
                                }
                                case 2: {
                                    if (singleChain) {
                                        widget4.mLeft.connect(this.mLeft, this.mPaddingLeft);
                                        widget4.mRight.connect(this.mRight, this.mPaddingRight);
                                        break;
                                    }
                                    widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                    widget4.mRight.connect(horizontalWidget.mRight, 0);
                                    break;
                                }
                                case 1: {
                                    widget4.mRight.connect(horizontalWidget.mRight, 0);
                                    break;
                                }
                            }
                        }
                    }
                    previous = widget4;
                }
            }
        }
    }
}
