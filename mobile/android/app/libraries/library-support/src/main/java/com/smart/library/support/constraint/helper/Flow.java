package com.smart.library.support.constraint.helper;

import android.content.*;
import android.annotation.*;
import android.view.*;
import android.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;

import com.smart.library.support.constraint.VirtualLayout;
import com.smart.library.support.constraint.solver.widgets.*;

public class Flow extends VirtualLayout
{
    private static final String TAG = "Flow";
    private com.smart.library.support.constraint.solver.widgets.Flow mFlow;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int WRAP_NONE = 0;
    public static final int WRAP_CHAIN = 1;
    public static final int WRAP_ALIGNED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static final int CHAIN_PACKED = 2;
    public static final int HORIZONTAL_ALIGN_START = 0;
    public static final int HORIZONTAL_ALIGN_END = 1;
    public static final int HORIZONTAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int VERTICAL_ALIGN_BOTTOM = 1;
    public static final int VERTICAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    
    public Flow(final Context context) {
        super(context);
    }
    
    public Flow(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public Flow(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    public void resolveRtl(final ConstraintWidget widget, final boolean isRtl) {
    }
    
    @SuppressLint({ "WrongCall" })
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        this.onMeasure((com.smart.library.support.constraint.solver.widgets.VirtualLayout)this.mFlow, widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    public void onMeasure(final com.smart.library.support.constraint.solver.widgets.VirtualLayout layout, final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (layout != null) {
            layout.measure(widthMode, widthSize, heightMode, heightSize);
            this.setMeasuredDimension(layout.getMeasuredWidth(), layout.getMeasuredHeight());
        }
        else {
            this.setMeasuredDimension(0, 0);
        }
    }
    
    @Override
    public void loadParameters(final ConstraintSet.Constraint constraint, final HelperWidget child, final ConstraintLayout.LayoutParams layoutParams, final SparseArray<ConstraintWidget> mapIdToWidget) {
        super.loadParameters(constraint, child, layoutParams, mapIdToWidget);
        if (child instanceof com.smart.library.support.constraint.solver.widgets.Flow) {
            final com.smart.library.support.constraint.solver.widgets.Flow flow = (com.smart.library.support.constraint.solver.widgets.Flow)child;
            if (layoutParams.orientation != -1) {
                flow.setOrientation(layoutParams.orientation);
            }
        }
    }
    
    @Override
    protected void init(final AttributeSet attrs) {
        super.init(attrs);
        this.mFlow = new com.smart.library.support.constraint.solver.widgets.Flow();
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_android_orientation) {
                    this.mFlow.setOrientation(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_padding) {
                    this.mFlow.setPadding(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_paddingLeft) {
                    this.mFlow.setPaddingLeft(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_paddingTop) {
                    this.mFlow.setPaddingTop(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_paddingRight) {
                    this.mFlow.setPaddingRight(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_android_paddingBottom) {
                    this.mFlow.setPaddingBottom(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_wrapMode) {
                    this.mFlow.setWrapMode(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_horizontalStyle) {
                    this.mFlow.setHorizontalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_verticalStyle) {
                    this.mFlow.setVerticalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_firstHorizontalStyle) {
                    this.mFlow.setFirstHorizontalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_lastHorizontalStyle) {
                    this.mFlow.setLastHorizontalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_firstVerticalStyle) {
                    this.mFlow.setFirstVerticalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_lastVerticalStyle) {
                    this.mFlow.setLastVerticalStyle(a.getInt(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_horizontalBias) {
                    this.mFlow.setHorizontalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_firstHorizontalBias) {
                    this.mFlow.setFirstHorizontalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_lastHorizontalBias) {
                    this.mFlow.setLastHorizontalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_firstVerticalBias) {
                    this.mFlow.setFirstVerticalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_lastVerticalBias) {
                    this.mFlow.setLastVerticalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_verticalBias) {
                    this.mFlow.setVerticalBias(a.getFloat(attr, 0.5f));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_horizontalAlign) {
                    this.mFlow.setHorizontalAlign(a.getInt(attr, 2));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_verticalAlign) {
                    this.mFlow.setVerticalAlign(a.getInt(attr, 2));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_horizontalGap) {
                    this.mFlow.setHorizontalGap(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_verticalGap) {
                    this.mFlow.setVerticalGap(a.getDimensionPixelSize(attr, 0));
                }
                else if (attr == R.styleable.ConstraintLayout_Layout_flow_maxElementsWrap) {
                    this.mFlow.setMaxElementsWrap(a.getInt(attr, -1));
                }
            }
        }
        this.mHelperWidget = (Helper)this.mFlow;
        this.validateParams();
    }
    
    public void setOrientation(final int orientation) {
        this.mFlow.setOrientation(orientation);
        this.requestLayout();
    }
    
    public void setPadding(final int padding) {
        this.mFlow.setPadding(padding);
        this.requestLayout();
    }
    
    public void setPaddingLeft(final int paddingLeft) {
        this.mFlow.setPaddingLeft(paddingLeft);
        this.requestLayout();
    }
    
    public void setPaddingTop(final int paddingTop) {
        this.mFlow.setPaddingTop(paddingTop);
        this.requestLayout();
    }
    
    public void setPaddingRight(final int paddingRight) {
        this.mFlow.setPaddingRight(paddingRight);
        this.requestLayout();
    }
    
    public void setPaddingBottom(final int paddingBottom) {
        this.mFlow.setPaddingBottom(paddingBottom);
        this.requestLayout();
    }
    
    public void setWrapMode(final int mode) {
        this.mFlow.setWrapMode(mode);
        this.requestLayout();
    }
    
    public void setHorizontalStyle(final int style) {
        this.mFlow.setHorizontalStyle(style);
        this.requestLayout();
    }
    
    public void setVerticalStyle(final int style) {
        this.mFlow.setVerticalStyle(style);
        this.requestLayout();
    }
    
    public void setHorizontalBias(final float bias) {
        this.mFlow.setHorizontalBias(bias);
        this.requestLayout();
    }
    
    public void setVerticalBias(final float bias) {
        this.mFlow.setVerticalBias(bias);
        this.requestLayout();
    }
    
    public void setFirstHorizontalStyle(final int style) {
        this.mFlow.setFirstHorizontalStyle(style);
        this.requestLayout();
    }
    
    public void setFirstVerticalStyle(final int style) {
        this.mFlow.setFirstVerticalStyle(style);
        this.requestLayout();
    }
    
    public void setFirstHorizontalBias(final float bias) {
        this.mFlow.setFirstHorizontalBias(bias);
        this.requestLayout();
    }
    
    public void setFirstVerticalBias(final float bias) {
        this.mFlow.setFirstVerticalBias(bias);
        this.requestLayout();
    }
    
    public void setHorizontalAlign(final int align) {
        this.mFlow.setHorizontalAlign(align);
        this.requestLayout();
    }
    
    public void setVerticalAlign(final int align) {
        this.mFlow.setVerticalAlign(align);
        this.requestLayout();
    }
    
    public void setHorizontalGap(final int gap) {
        this.mFlow.setHorizontalGap(gap);
        this.requestLayout();
    }
    
    public void setVerticalGap(final int gap) {
        this.mFlow.setVerticalGap(gap);
        this.requestLayout();
    }
    
    public void setMaxElementsWrap(final int max) {
        this.mFlow.setMaxElementsWrap(max);
        this.requestLayout();
    }
}
