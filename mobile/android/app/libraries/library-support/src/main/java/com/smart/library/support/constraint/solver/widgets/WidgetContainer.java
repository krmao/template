package com.smart.library.support.constraint.solver.widgets;

import java.util.*;
import com.smart.library.support.constraint.solver.*;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;

public class WidgetContainer extends ConstraintWidget
{
    public ArrayList<ConstraintWidget> mChildren;
    
    public WidgetContainer() {
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int width, final int height) {
        super(width, height);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }
    
    public void add(final ConstraintWidget widget) {
        this.mChildren.add(widget);
        if (widget.getParent() != null) {
            final WidgetContainer container = (WidgetContainer)widget.getParent();
            container.remove(widget);
        }
        widget.setParent(this);
    }
    
    public void add(final ConstraintWidget... widgets) {
        for (int count = widgets.length, i = 0; i < count; ++i) {
            this.add(widgets[i]);
        }
    }
    
    public void remove(final ConstraintWidget widget) {
        this.mChildren.remove(widget);
        widget.setParent(null);
    }
    
    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }
    
    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = this.getParent();
        ConstraintWidgetContainer container = null;
        if (this instanceof ConstraintWidgetContainer) {
            container = (ConstraintWidgetContainer)this;
        }
        while (parent != null) {
            final ConstraintWidget item = parent;
            parent = item.getParent();
            if (item instanceof ConstraintWidgetContainer) {
                container = (ConstraintWidgetContainer)item;
            }
        }
        return container;
    }
    
    @Override
    public void setOffset(final int x, final int y) {
        super.setOffset(x, y);
        for (int count = this.mChildren.size(), i = 0; i < count; ++i) {
            final ConstraintWidget widget = this.mChildren.get(i);
            widget.setOffset(this.getRootX(), this.getRootY());
        }
    }
    
    public void layout() {
        if (this.mChildren == null) {
            return;
        }
        for (int count = this.mChildren.size(), i = 0; i < count; ++i) {
            final ConstraintWidget widget = this.mChildren.get(i);
            if (widget instanceof WidgetContainer) {
                ((WidgetContainer)widget).layout();
            }
        }
    }
    
    @Override
    public void resetSolverVariables(final Cache cache) {
        super.resetSolverVariables(cache);
        for (int count = this.mChildren.size(), i = 0; i < count; ++i) {
            final ConstraintWidget widget = this.mChildren.get(i);
            widget.resetSolverVariables(cache);
        }
    }
    
    public void removeAllChildren() {
        this.mChildren.clear();
    }
}
