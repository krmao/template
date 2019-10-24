package com.smart.library.support.constraint.utils;

import com.smart.library.support.R;
import com.smart.library.support.constraint.motion.*;
import android.content.*;
import android.util.*;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import android.graphics.*;
import android.view.*;

public class MotionTelltales extends MockView
{
    private static final String TAG = "MotionTelltales";
    private Paint mPaintTelltales;
    MotionLayout mMotionLayout;
    float[] velocity;
    Matrix mInvertMatrix;
    int mVelocityMode;
    int mTailColor;
    float mTailScale;
    
    public MotionTelltales(final Context context) {
        super(context);
        this.mPaintTelltales = new Paint();
        this.velocity = new float[2];
        this.mInvertMatrix = new Matrix();
        this.mVelocityMode = 0;
        this.mTailColor = -65281;
        this.mTailScale = 0.25f;
        this.init(context, null);
    }
    
    public MotionTelltales(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mPaintTelltales = new Paint();
        this.velocity = new float[2];
        this.mInvertMatrix = new Matrix();
        this.mVelocityMode = 0;
        this.mTailColor = -65281;
        this.mTailScale = 0.25f;
        this.init(context, attrs);
    }
    
    public MotionTelltales(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaintTelltales = new Paint();
        this.velocity = new float[2];
        this.mInvertMatrix = new Matrix();
        this.mVelocityMode = 0;
        this.mTailColor = -65281;
        this.mTailScale = 0.25f;
        this.init(context, attrs);
    }
    
    private void init(final Context context, final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MotionTelltales);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.MotionTelltales_telltales_tailColor) {
                    this.mTailColor = a.getColor(attr, this.mTailColor);
                }
                else if (attr == R.styleable.MotionTelltales_telltales_velocityMode) {
                    this.mVelocityMode = a.getInt(attr, this.mVelocityMode);
                }
                else if (attr == R.styleable.MotionTelltales_telltales_tailScale) {
                    this.mTailScale = a.getFloat(attr, this.mTailScale);
                }
            }
        }
        this.mPaintTelltales.setColor(this.mTailColor);
        this.mPaintTelltales.setStrokeWidth(5.0f);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    
    public void setText(final CharSequence text) {
        this.mText = text.toString();
        this.requestLayout();
    }
    
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.postInvalidate();
    }
    
    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final Matrix matrix = this.getMatrix();
        matrix.invert(this.mInvertMatrix);
        if (this.mMotionLayout == null) {
            final ViewParent vp = this.getParent();
            if (vp instanceof MotionLayout) {
                this.mMotionLayout = (MotionLayout)vp;
            }
            return;
        }
        final int width = this.getWidth();
        final int height = this.getHeight();
        final float[] f = { 0.1f, 0.25f, 0.5f, 0.75f, 0.9f };
        for (int y = 0; y < f.length; ++y) {
            final float py = f[y];
            for (int x = 0; x < f.length; ++x) {
                final float px = f[x];
                this.mMotionLayout.getViewVelocity(this, px, py, this.velocity, this.mVelocityMode);
                this.mInvertMatrix.mapVectors(this.velocity);
                final float sx = width * px;
                final float sy = height * py;
                final float ex = sx - this.velocity[0] * this.mTailScale;
                final float ey = sy - this.velocity[1] * this.mTailScale;
                this.mInvertMatrix.mapVectors(this.velocity);
                canvas.drawLine(sx, sy, ex, ey, this.mPaintTelltales);
            }
        }
    }
}
