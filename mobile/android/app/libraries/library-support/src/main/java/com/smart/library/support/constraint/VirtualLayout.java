package com.smart.library.support.constraint;

import android.content.*;
import android.util.*;

public abstract class VirtualLayout extends ConstraintHelper
{
    public VirtualLayout(final Context context) {
        super(context);
    }
    
    public VirtualLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public VirtualLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public void onMeasure(final com.smart.library.support.constraint.solver.widgets.VirtualLayout layout, final int widthMeasureSpec, final int heightMeasureSpec) {
    }
}
