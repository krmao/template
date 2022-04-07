package com.smart.library.support.constraint.utils;

import android.view.*;
import android.content.*;
import android.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import android.graphics.*;

public class MockView extends View
{
    private Paint mPaintDiagonals;
    private Paint mPaintText;
    private Paint mPaintTextBackground;
    private boolean mDrawDiagonals;
    private boolean mDrawLabel;
    protected String mText;
    private Rect mTextBounds;
    private int mDiagonalsColor;
    private int mTextColor;
    private int mTextBackgroundColor;
    private int mMargin;
    
    public MockView(final Context context) {
        super(context);
        this.mPaintDiagonals = new Paint();
        this.mPaintText = new Paint();
        this.mPaintTextBackground = new Paint();
        this.mDrawDiagonals = true;
        this.mDrawLabel = true;
        this.mText = null;
        this.mTextBounds = new Rect();
        this.mDiagonalsColor = Color.argb(255, 0, 0, 0);
        this.mTextColor = Color.argb(255, 200, 200, 200);
        this.mTextBackgroundColor = Color.argb(255, 50, 50, 50);
        this.mMargin = 4;
        this.init(context, null);
    }
    
    public MockView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mPaintDiagonals = new Paint();
        this.mPaintText = new Paint();
        this.mPaintTextBackground = new Paint();
        this.mDrawDiagonals = true;
        this.mDrawLabel = true;
        this.mText = null;
        this.mTextBounds = new Rect();
        this.mDiagonalsColor = Color.argb(255, 0, 0, 0);
        this.mTextColor = Color.argb(255, 200, 200, 200);
        this.mTextBackgroundColor = Color.argb(255, 50, 50, 50);
        this.mMargin = 4;
        this.init(context, attrs);
    }
    
    public MockView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaintDiagonals = new Paint();
        this.mPaintText = new Paint();
        this.mPaintTextBackground = new Paint();
        this.mDrawDiagonals = true;
        this.mDrawLabel = true;
        this.mText = null;
        this.mTextBounds = new Rect();
        this.mDiagonalsColor = Color.argb(255, 0, 0, 0);
        this.mTextColor = Color.argb(255, 200, 200, 200);
        this.mTextBackgroundColor = Color.argb(255, 50, 50, 50);
        this.mMargin = 4;
        this.init(context, attrs);
    }
    
    private void init(final Context context, final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MockView);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.MockView_mock_label) {
                    this.mText = a.getString(attr);
                }
                else if (attr == R.styleable.MockView_mock_showDiagonals) {
                    this.mDrawDiagonals = a.getBoolean(attr, this.mDrawDiagonals);
                }
                else if (attr == R.styleable.MockView_mock_diagonalsColor) {
                    this.mDiagonalsColor = a.getColor(attr, this.mDiagonalsColor);
                }
                else if (attr == R.styleable.MockView_mock_labelBackgroundColor) {
                    this.mTextBackgroundColor = a.getColor(attr, this.mTextBackgroundColor);
                }
                else if (attr == R.styleable.MockView_mock_labelColor) {
                    this.mTextColor = a.getColor(attr, this.mTextColor);
                }
                else if (attr == R.styleable.MockView_mock_showLabel) {
                    this.mDrawLabel = a.getBoolean(attr, this.mDrawLabel);
                }
            }
        }
        if (this.mText == null) {
            try {
                this.mText = context.getResources().getResourceEntryName(this.getId());
            }
            catch (Exception ex) {}
        }
        this.mPaintDiagonals.setColor(this.mDiagonalsColor);
        this.mPaintDiagonals.setAntiAlias(true);
        this.mPaintText.setColor(this.mTextColor);
        this.mPaintText.setAntiAlias(true);
        this.mPaintTextBackground.setColor(this.mTextBackgroundColor);
        this.mMargin = Math.round(this.mMargin * (this.getResources().getDisplayMetrics().xdpi / 160.0f));
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        int w = this.getWidth();
        int h = this.getHeight();
        if (this.mDrawDiagonals) {
            --w;
            --h;
            canvas.drawLine(0.0f, 0.0f, (float)w, (float)h, this.mPaintDiagonals);
            canvas.drawLine(0.0f, (float)h, (float)w, 0.0f, this.mPaintDiagonals);
            canvas.drawLine(0.0f, 0.0f, (float)w, 0.0f, this.mPaintDiagonals);
            canvas.drawLine((float)w, 0.0f, (float)w, (float)h, this.mPaintDiagonals);
            canvas.drawLine((float)w, (float)h, 0.0f, (float)h, this.mPaintDiagonals);
            canvas.drawLine(0.0f, (float)h, 0.0f, 0.0f, this.mPaintDiagonals);
        }
        if (this.mText != null && this.mDrawLabel) {
            this.mPaintText.getTextBounds(this.mText, 0, this.mText.length(), this.mTextBounds);
            final float tx = (w - this.mTextBounds.width()) / 2.0f;
            final float ty = (h - this.mTextBounds.height()) / 2.0f + this.mTextBounds.height();
            this.mTextBounds.offset((int)tx, (int)ty);
            this.mTextBounds.set(this.mTextBounds.left - this.mMargin, this.mTextBounds.top - this.mMargin, this.mTextBounds.right + this.mMargin, this.mTextBounds.bottom + this.mMargin);
            canvas.drawRect(this.mTextBounds, this.mPaintTextBackground);
            canvas.drawText(this.mText, tx, ty, this.mPaintText);
        }
    }
}
