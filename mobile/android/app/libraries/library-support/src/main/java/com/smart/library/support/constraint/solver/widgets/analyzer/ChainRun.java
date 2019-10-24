package com.smart.library.support.constraint.solver.widgets.analyzer;

import java.util.*;
import com.smart.library.support.constraint.solver.widgets.*;

public class ChainRun extends WidgetRun
{
    ArrayList<WidgetRun> widgets;
    private int chainStyle;
    
    public ChainRun(final ConstraintWidget widget, final int orientation) {
        super(widget);
        this.widgets = new ArrayList<WidgetRun>();
        this.orientation = orientation;
        this.build();
    }
    
    @Override
    public String toString() {
        String log = "ChainRun " + ((this.orientation == 0) ? "horizontal : " : "vertical : ");
        for (final WidgetRun run : this.widgets) {
            log += "<";
            log += run;
            log += "> ";
        }
        return log;
    }
    
    @Override
    boolean supportsWrapComputation() {
        for (int count = this.widgets.size(), i = 0; i < count; ++i) {
            final WidgetRun run = this.widgets.get(i);
            if (!run.supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public long getWrapDimension() {
        final int count = this.widgets.size();
        long wrapDimension = 0L;
        for (int i = 0; i < count; ++i) {
            final WidgetRun run = this.widgets.get(i);
            wrapDimension += run.start.margin;
            wrapDimension += run.getWrapDimension();
            wrapDimension += run.end.margin;
        }
        return wrapDimension;
    }
    
    private void build() {
        ConstraintWidget current = this.widget;
        for (ConstraintWidget previous = current.getPreviousChainMember(this.orientation); previous != null; previous = current.getPreviousChainMember(this.orientation)) {
            current = previous;
        }
        this.widget = current;
        this.widgets.add(current.getRun(this.orientation));
        for (ConstraintWidget next = current.getNextChainMember(this.orientation); next != null; next = current.getNextChainMember(this.orientation)) {
            current = next;
            this.widgets.add(current.getRun(this.orientation));
        }
        for (final WidgetRun run : this.widgets) {
            if (this.orientation == 0) {
                run.widget.horizontalChainRun = this;
            }
            else {
                if (this.orientation != 1) {
                    continue;
                }
                run.widget.verticalChainRun = this;
            }
        }
        final boolean isInRtl = this.orientation == 0 && ((ConstraintWidgetContainer)this.widget.getParent()).isRtl();
        if (isInRtl && this.widgets.size() > 1) {
            this.widget = this.widgets.get(this.widgets.size() - 1).widget;
        }
        this.chainStyle = ((this.orientation == 0) ? this.widget.getHorizontalChainStyle() : this.widget.getVerticalChainStyle());
    }
    
    @Override
    void clear() {
        this.runGroup = null;
        for (final WidgetRun run : this.widgets) {
            run.clear();
        }
    }
    
    @Override
    void reset() {
        this.start.resolved = false;
        this.end.resolved = false;
    }
    
    @Override
    public void update(final Dependency dependency) {
        if (!this.start.resolved || !this.end.resolved) {
            return;
        }
        final ConstraintWidget parent = this.widget.getParent();
        boolean isInRtl = false;
        if (parent != null && parent instanceof ConstraintWidgetContainer) {
            isInRtl = ((ConstraintWidgetContainer)parent).isRtl();
        }
        final int distance = this.end.value - this.start.value;
        int size = 0;
        int numMatchConstraints = 0;
        float weights = 0.0f;
        int numVisibleWidgets = 0;
        final int count = this.widgets.size();
        int firstVisibleWidget = -1;
        for (int i = 0; i < count; ++i) {
            final WidgetRun run = this.widgets.get(i);
            if (run.widget.getVisibility() != 8) {
                firstVisibleWidget = i;
                break;
            }
        }
        int lastVisibleWidget = -1;
        for (int j = count - 1; j >= 0; --j) {
            final WidgetRun run2 = this.widgets.get(j);
            if (run2.widget.getVisibility() != 8) {
                lastVisibleWidget = j;
                break;
            }
        }
        for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < count; ++l) {
                final WidgetRun run3 = this.widgets.get(l);
                if (run3.widget.getVisibility() != 8) {
                    ++numVisibleWidgets;
                    if (l > 0 && l >= firstVisibleWidget) {
                        size += run3.start.margin;
                    }
                    int dimension = run3.dimension.value;
                    boolean treatAsFixed = run3.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                    if (treatAsFixed) {
                        if (this.orientation == 0 && !run3.widget.horizontalRun.dimension.resolved) {
                            return;
                        }
                        if (this.orientation == 1 && !run3.widget.verticalRun.dimension.resolved) {
                            return;
                        }
                    }
                    else if (run3.matchConstraintsType == 1 && k == 0) {
                        treatAsFixed = true;
                        dimension = run3.dimension.wrapValue;
                        ++numMatchConstraints;
                    }
                    else if (run3.dimension.resolved) {
                        treatAsFixed = true;
                    }
                    if (!treatAsFixed) {
                        ++numMatchConstraints;
                        final float weight = run3.widget.mWeight[this.orientation];
                        if (weight >= 0.0f) {
                            weights += weight;
                        }
                    }
                    else {
                        size += dimension;
                    }
                    if (l < count - 1 && l < lastVisibleWidget) {
                        size += -run3.end.margin;
                    }
                }
            }
            if (size < distance) {
                break;
            }
            if (numMatchConstraints == 0) {
                break;
            }
            numVisibleWidgets = 0;
            numMatchConstraints = 0;
            size = 0;
            weights = 0.0f;
        }
        int position = this.start.value;
        if (isInRtl) {
            position = this.end.value;
        }
        if (size > distance) {
            if (isInRtl) {
                position += (int)(0.5f + (size - distance) / 2.0f);
            }
            else {
                position -= (int)(0.5f + (size - distance) / 2.0f);
            }
        }
        int matchConstraintsDimension = 0;
        if (numMatchConstraints > 0) {
            matchConstraintsDimension = (int)(0.5f + (distance - size) / numMatchConstraints);
            int appliedLimits = 0;
            for (int m = 0; m < count; ++m) {
                final WidgetRun run4 = this.widgets.get(m);
                if (run4.widget.getVisibility() != 8) {
                    if (run4.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !run4.dimension.resolved) {
                        int dimension2 = matchConstraintsDimension;
                        if (weights > 0.0f) {
                            final float weight2 = run4.widget.mWeight[this.orientation];
                            dimension2 = (int)(0.5f + weight2 * (distance - size) / weights);
                        }
                        if (this.orientation == 0) {
                            final int max = run4.widget.mMatchConstraintMaxWidth;
                            final int min = run4.widget.mMatchConstraintMinWidth;
                            int value = dimension2;
                            if (run4.matchConstraintsType == 1) {
                                value = Math.min(value, run4.dimension.wrapValue);
                            }
                            value = Math.max(min, value);
                            if (max > 0) {
                                value = Math.min(max, value);
                            }
                            if (value != dimension2) {
                                ++appliedLimits;
                                dimension2 = value;
                            }
                        }
                        else {
                            final int max = run4.widget.mMatchConstraintMaxHeight;
                            final int min = run4.widget.mMatchConstraintMinHeight;
                            int value = dimension2;
                            if (run4.matchConstraintsType == 1) {
                                value = Math.min(value, run4.dimension.wrapValue);
                            }
                            value = Math.max(min, value);
                            if (max > 0) {
                                value = Math.min(max, value);
                            }
                            if (value != dimension2) {
                                ++appliedLimits;
                                dimension2 = value;
                            }
                        }
                        run4.dimension.resolve(dimension2);
                    }
                }
            }
            if (appliedLimits > 0) {
                numMatchConstraints -= appliedLimits;
                size = 0;
                for (int m = 0; m < count; ++m) {
                    final WidgetRun run4 = this.widgets.get(m);
                    if (run4.widget.getVisibility() != 8) {
                        if (m > 0 && m >= firstVisibleWidget) {
                            size += run4.start.margin;
                        }
                        size += run4.dimension.value;
                        if (m < count - 1 && m < lastVisibleWidget) {
                            size += -run4.end.margin;
                        }
                    }
                }
            }
            if (this.chainStyle == 2 && appliedLimits == 0) {
                this.chainStyle = 0;
            }
        }
        if (size > distance) {
            this.chainStyle = 2;
        }
        if (numVisibleWidgets > 0 && numMatchConstraints == 0 && firstVisibleWidget == lastVisibleWidget) {
            this.chainStyle = 2;
        }
        if (this.chainStyle == 1) {
            int gap = 0;
            if (numVisibleWidgets > 1) {
                gap = (distance - size) / (numVisibleWidgets - 1);
            }
            else if (numVisibleWidgets == 1) {
                gap = (distance - size) / 2;
            }
            if (numMatchConstraints > 0) {
                gap = 0;
            }
            for (int m = 0; m < count; ++m) {
                int index = m;
                if (isInRtl) {
                    index = count - (m + 1);
                }
                final WidgetRun run5 = this.widgets.get(index);
                if (run5.widget.getVisibility() == 8) {
                    run5.start.resolve(position);
                    run5.end.resolve(position);
                }
                else {
                    if (m > 0) {
                        if (isInRtl) {
                            position -= gap;
                        }
                        else {
                            position += gap;
                        }
                    }
                    if (m > 0 && m >= firstVisibleWidget) {
                        if (isInRtl) {
                            position -= run5.start.margin;
                        }
                        else {
                            position += run5.start.margin;
                        }
                    }
                    if (isInRtl) {
                        run5.end.resolve(position);
                    }
                    else {
                        run5.start.resolve(position);
                    }
                    int dimension3 = run5.dimension.value;
                    if (run5.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && run5.matchConstraintsType == 1) {
                        dimension3 = run5.dimension.wrapValue;
                    }
                    if (isInRtl) {
                        position -= dimension3;
                    }
                    else {
                        position += dimension3;
                    }
                    if (isInRtl) {
                        run5.start.resolve(position);
                    }
                    else {
                        run5.end.resolve(position);
                    }
                    run5.resolved = true;
                    if (m < count - 1 && m < lastVisibleWidget) {
                        if (isInRtl) {
                            position -= -run5.end.margin;
                        }
                        else {
                            position += -run5.end.margin;
                        }
                    }
                }
            }
        }
        else if (this.chainStyle == 0) {
            int gap = (distance - size) / (numVisibleWidgets + 1);
            if (numMatchConstraints > 0) {
                gap = 0;
            }
            for (int m = 0; m < count; ++m) {
                int index = m;
                if (isInRtl) {
                    index = count - (m + 1);
                }
                final WidgetRun run5 = this.widgets.get(index);
                if (run5.widget.getVisibility() == 8) {
                    run5.start.resolve(position);
                    run5.end.resolve(position);
                }
                else {
                    if (isInRtl) {
                        position -= gap;
                    }
                    else {
                        position += gap;
                    }
                    if (m > 0 && m >= firstVisibleWidget) {
                        if (isInRtl) {
                            position -= run5.start.margin;
                        }
                        else {
                            position += run5.start.margin;
                        }
                    }
                    if (isInRtl) {
                        run5.end.resolve(position);
                    }
                    else {
                        run5.start.resolve(position);
                    }
                    int dimension3 = run5.dimension.value;
                    if (run5.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && run5.matchConstraintsType == 1) {
                        dimension3 = Math.min(dimension3, run5.dimension.wrapValue);
                    }
                    if (isInRtl) {
                        position -= dimension3;
                    }
                    else {
                        position += dimension3;
                    }
                    if (isInRtl) {
                        run5.start.resolve(position);
                    }
                    else {
                        run5.end.resolve(position);
                    }
                    if (m < count - 1 && m < lastVisibleWidget) {
                        if (isInRtl) {
                            position -= -run5.end.margin;
                        }
                        else {
                            position += -run5.end.margin;
                        }
                    }
                }
            }
        }
        else if (this.chainStyle == 2) {
            float bias = (this.orientation == 0) ? this.widget.getHorizontalBiasPercent() : this.widget.getVerticalBiasPercent();
            if (isInRtl) {
                bias = 1.0f - bias;
            }
            int gap2 = (int)(0.5f + (distance - size) * bias);
            if (gap2 < 0 || numMatchConstraints > 0) {
                gap2 = 0;
            }
            if (isInRtl) {
                position -= gap2;
            }
            else {
                position += gap2;
            }
            for (int i2 = 0; i2 < count; ++i2) {
                int index2 = i2;
                if (isInRtl) {
                    index2 = count - (i2 + 1);
                }
                final WidgetRun run6 = this.widgets.get(index2);
                if (run6.widget.getVisibility() == 8) {
                    run6.start.resolve(position);
                    run6.end.resolve(position);
                }
                else {
                    if (i2 > 0 && i2 >= firstVisibleWidget) {
                        if (isInRtl) {
                            position -= run6.start.margin;
                        }
                        else {
                            position += run6.start.margin;
                        }
                    }
                    if (isInRtl) {
                        run6.end.resolve(position);
                    }
                    else {
                        run6.start.resolve(position);
                    }
                    int dimension4 = run6.dimension.value;
                    if (run6.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && run6.matchConstraintsType == 1) {
                        dimension4 = run6.dimension.wrapValue;
                    }
                    if (isInRtl) {
                        position -= dimension4;
                    }
                    else {
                        position += dimension4;
                    }
                    if (isInRtl) {
                        run6.start.resolve(position);
                    }
                    else {
                        run6.end.resolve(position);
                    }
                    if (i2 < count - 1 && i2 < lastVisibleWidget) {
                        if (isInRtl) {
                            position -= -run6.end.margin;
                        }
                        else {
                            position += -run6.end.margin;
                        }
                    }
                }
            }
        }
    }
    
    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); ++i) {
            final WidgetRun run = this.widgets.get(i);
            run.applyToWidget();
        }
    }
    
    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); ++i) {
            final WidgetRun run = this.widgets.get(i);
            if (run.widget.getVisibility() != 8) {
                return run.widget;
            }
        }
        return null;
    }
    
    private ConstraintWidget getLastVisibleWidget() {
        for (int i = this.widgets.size() - 1; i >= 0; --i) {
            final WidgetRun run = this.widgets.get(i);
            if (run.widget.getVisibility() != 8) {
                return run.widget;
            }
        }
        return null;
    }
    
    @Override
    void apply() {
        for (final WidgetRun run : this.widgets) {
            run.apply();
        }
        final int count = this.widgets.size();
        if (count < 1) {
            return;
        }
        final ConstraintWidget firstWidget = this.widgets.get(0).widget;
        final ConstraintWidget lastWidget = this.widgets.get(count - 1).widget;
        if (this.orientation == 0) {
            final ConstraintAnchor startAnchor = firstWidget.mLeft;
            final ConstraintAnchor endAnchor = lastWidget.mRight;
            final DependencyNode startTarget = this.getTarget(startAnchor, 0);
            int startMargin = startAnchor.getMargin();
            final ConstraintWidget firstVisibleWidget = this.getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                startMargin = firstVisibleWidget.mLeft.getMargin();
            }
            if (startTarget != null) {
                this.addTarget(this.start, startTarget, startMargin);
            }
            final DependencyNode endTarget = this.getTarget(endAnchor, 0);
            int endMargin = endAnchor.getMargin();
            final ConstraintWidget lastVisibleWidget = this.getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                endMargin = lastVisibleWidget.mRight.getMargin();
            }
            if (endTarget != null) {
                this.addTarget(this.end, endTarget, -endMargin);
            }
        }
        else {
            final ConstraintAnchor startAnchor = firstWidget.mTop;
            final ConstraintAnchor endAnchor = lastWidget.mBottom;
            final DependencyNode startTarget = this.getTarget(startAnchor, 1);
            int startMargin = startAnchor.getMargin();
            final ConstraintWidget firstVisibleWidget = this.getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                startMargin = firstVisibleWidget.mTop.getMargin();
            }
            if (startTarget != null) {
                this.addTarget(this.start, startTarget, startMargin);
            }
            final DependencyNode endTarget = this.getTarget(endAnchor, 1);
            int endMargin = endAnchor.getMargin();
            final ConstraintWidget lastVisibleWidget = this.getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                endMargin = lastVisibleWidget.mBottom.getMargin();
            }
            if (endTarget != null) {
                this.addTarget(this.end, endTarget, -endMargin);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }
}
