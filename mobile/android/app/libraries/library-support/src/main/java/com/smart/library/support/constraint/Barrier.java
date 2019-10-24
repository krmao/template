package com.smart.library.support.constraint;

import android.content.*;
import android.os.*;
import android.content.res.*;
import android.util.*;
import android.view.View;

import com.smart.library.support.R;
import com.smart.library.support.constraint.solver.widgets.*;

public class Barrier extends ConstraintHelper
{
    public static final int LEFT = 0;
    public static final int TOP = 2;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 3;
    public static final int START = 5;
    public static final int END = 6;
    private int mIndicatedType;
    private int mResolvedType;
    private com.smart.library.support.constraint.solver.widgets.Barrier mBarrier;
    
    public Barrier(final Context context) {
        super(context);
        super.setVisibility(View.GONE);
    }
    
    public Barrier(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        super.setVisibility(View.GONE);
    }
    
    public Barrier(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setVisibility(View.GONE);
    }
    
    public int getType() {
        return this.mIndicatedType;
    }
    
    public void setType(final int type) {
        this.mIndicatedType = type;
    }
    
    private void updateType(final ConstraintWidget widget, final int type, final boolean isRtl) {
        this.mResolvedType = type;
        if (Build.VERSION.SDK_INT < 17) {
            if (this.mIndicatedType == 5) {
                this.mResolvedType = 0;
            }
            else if (this.mIndicatedType == 6) {
                this.mResolvedType = 1;
            }
        }
        else if (isRtl) {
            if (this.mIndicatedType == 5) {
                this.mResolvedType = 1;
            }
            else if (this.mIndicatedType == 6) {
                this.mResolvedType = 0;
            }
        }
        else if (this.mIndicatedType == 5) {
            this.mResolvedType = 0;
        }
        else if (this.mIndicatedType == 6) {
            this.mResolvedType = 1;
        }
        if (widget instanceof com.smart.library.support.constraint.solver.widgets.Barrier) {
            final com.smart.library.support.constraint.solver.widgets.Barrier barrier = (com.smart.library.support.constraint.solver.widgets.Barrier)widget;
            barrier.setBarrierType(this.mResolvedType);
        }
    }
    
    @Override
    public void resolveRtl(final ConstraintWidget widget, final boolean isRtl) {
        this.updateType(widget, this.mIndicatedType, isRtl);
    }
    
    @Override
    protected void init(final AttributeSet attrs) {
        super.init(attrs);
        this.mBarrier = new com.smart.library.support.constraint.solver.widgets.Barrier();
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_barrierDirection) {
                    this.setType(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
                    this.mBarrier.setAllowsGoneWidget(a.getBoolean(attr, true));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_barrierMargin) {
                    final int margin = a.getDimensionPixelSize(attr, 0);
                    this.mBarrier.setMargin(margin);
                }
            }
        }
        this.mHelperWidget = (Helper)this.mBarrier;
        this.validateParams();
    }
    
    public void setAllowsGoneWidget(final boolean supportGone) {
        this.mBarrier.setAllowsGoneWidget(supportGone);
    }
    
    public boolean allowsGoneWidget() {
        return this.mBarrier.allowsGoneWidget();
    }
    
    public void setDpMargin(final int margin) {
        final float density = this.getResources().getDisplayMetrics().density;
        final int px = (int)(0.5f + margin * density);
        this.mBarrier.setMargin(px);
    }
    
    public int getMargin() {
        return this.mBarrier.getMargin();
    }
    
    public void setMargin(final int margin) {
        this.mBarrier.setMargin(margin);
    }
    
    @Override
    public void loadParameters(final ConstraintSet.Constraint constraint, final HelperWidget child, final ConstraintLayout.LayoutParams layoutParams, final SparseArray<ConstraintWidget> mapIdToWidget) {
        super.loadParameters(constraint, child, layoutParams, mapIdToWidget);
        if (child instanceof com.smart.library.support.constraint.solver.widgets.Barrier) {
            final com.smart.library.support.constraint.solver.widgets.Barrier barrier = (com.smart.library.support.constraint.solver.widgets.Barrier)child;
            final ConstraintWidgetContainer container = (ConstraintWidgetContainer)child.getParent();
            final boolean isRtl = container.isRtl();
            this.updateType((ConstraintWidget)barrier, constraint.layout.mBarrierDirection, isRtl);
            barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
            barrier.setMargin(constraint.layout.mBarrierMargin);
        }
    }
}
