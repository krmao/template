package com.smart.library.support.constraint.solver.widgets;

import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidgetContainer;
import com.smart.library.support.constraint.solver.widgets.Helper;

import java.util.*;

public class HelperWidget extends ConstraintWidget implements Helper
{
    public ConstraintWidget[] mWidgets;
    public int mWidgetsCount;
    
    public HelperWidget() {
        this.mWidgets = new ConstraintWidget[4];
        this.mWidgetsCount = 0;
    }
    
    @Override
    public void updateConstraints(final ConstraintWidgetContainer container) {
    }
    
    @Override
    public void add(final ConstraintWidget widget) {
        if (this.mWidgetsCount + 1 > this.mWidgets.length) {
            this.mWidgets = Arrays.copyOf(this.mWidgets, this.mWidgets.length * 2);
        }
        this.mWidgets[this.mWidgetsCount] = widget;
        ++this.mWidgetsCount;
    }
    
    @Override
    public void copy(final ConstraintWidget src, final HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        final HelperWidget srcHelper = (HelperWidget)src;
        this.mWidgetsCount = 0;
        for (int count = srcHelper.mWidgetsCount, i = 0; i < count; ++i) {
            this.add(map.get(srcHelper.mWidgets[i]));
        }
        this.mWidgetsCount = srcHelper.mWidgetsCount;
    }
    
    @Override
    public void removeAllIds() {
        this.mWidgetsCount = 0;
        Arrays.fill(this.mWidgets, null);
    }
}
