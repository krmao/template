package com.smart.library.support.constraint;

import android.view.*;
import android.content.*;
import android.util.*;
import android.content.res.*;
import android.graphics.*;

import com.smart.library.support.R;

public class Placeholder extends View
{
    private int mContentId;
    private View mContent;
    private int mEmptyVisibility;
    
    public Placeholder(final Context context) {
        super(context);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(null);
    }
    
    public Placeholder(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(attrs);
    }
    
    public Placeholder(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(attrs);
    }
    
    public Placeholder(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(attrs);
    }
    
    private void init(final AttributeSet attrs) {
        super.setVisibility(this.mEmptyVisibility);
        this.mContentId = -1;
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_placeholder);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_placeholder_content) {
                    this.mContentId = a.getResourceId(attr, this.mContentId);
                }
                else if (attr == R.styleable.ConstraintLayout_placeholder_placeholder_emptyVisibility) {
                    this.mEmptyVisibility = a.getInt(attr, this.mEmptyVisibility);
                }
            }
        }
    }
    
    public void setEmptyVisibility(final int visibility) {
        this.mEmptyVisibility = visibility;
    }
    
    public int getEmptyVisibility() {
        return this.mEmptyVisibility;
    }
    
    public View getContent() {
        return this.mContent;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            final Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            final Rect r = new Rect();
            canvas.getClipBounds(r);
            paint.setTextSize((float)r.height());
            final int cHeight = r.height();
            final int cWidth = r.width();
            paint.setTextAlign(Paint.Align.LEFT);
            final String text = "?";
            paint.getTextBounds(text, 0, text.length(), r);
            final float x = cWidth / 2.0f - r.width() / 2.0f - r.left;
            final float y = cHeight / 2.0f + r.height() / 2.0f - r.bottom;
            canvas.drawText(text, x, y, paint);
        }
    }
    
    public void updatePreLayout(final ConstraintLayout container) {
        if (this.mContentId == -1 && !this.isInEditMode()) {
            this.setVisibility(this.mEmptyVisibility);
        }
        this.mContent = container.findViewById(this.mContentId);
        if (this.mContent != null) {
            final ConstraintLayout.LayoutParams layoutParamsContent = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
            layoutParamsContent.isInPlaceholder = true;
            this.mContent.setVisibility(View.VISIBLE);
            this.setVisibility(View.VISIBLE);
        }
    }
    
    public void setContentId(final int id) {
        if (this.mContentId == id) {
            return;
        }
        if (this.mContent != null) {
            this.mContent.setVisibility(View.VISIBLE);
            final ConstraintLayout.LayoutParams layoutParamsContent = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
            layoutParamsContent.isInPlaceholder = false;
            this.mContent = null;
        }
        if ((this.mContentId = id) != -1) {
            final View v = ((View)this.getParent()).findViewById(id);
            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }
    }
    
    public void updatePostMeasure(final ConstraintLayout container) {
        if (this.mContent == null) {
            return;
        }
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        final ConstraintLayout.LayoutParams layoutParamsContent = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
        layoutParamsContent.widget.setVisibility(View.VISIBLE);
        layoutParams.widget.setWidth(layoutParamsContent.widget.getWidth());
        layoutParams.widget.setHeight(layoutParamsContent.widget.getHeight());
        layoutParamsContent.widget.setVisibility(View.GONE);
    }
}
