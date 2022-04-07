package com.smart.library.support.constraint.utils;

import android.support.v7.widget.*;
import android.graphics.drawable.*;
import android.content.*;
import android.util.*;

import com.smart.library.support.R;
import android.content.res.*;
import android.widget.*;
import android.os.*;
import android.view.*;
import android.graphics.*;

public class ImageFilterButton extends AppCompatImageButton
{
    private ImageFilterView.ImageMatrix mImageMatrix;
    private float mCrossfade;
    private float mRoundPercent;
    private float mRound;
    private Path mPath;
    ViewOutlineProvider mViewOutlineProvider;
    RectF mRect;
    Drawable[] mLayers;
    LayerDrawable mLayer;
    private boolean mOverlay;
    
    public ImageFilterButton(final Context context) {
        super(context);
        this.mImageMatrix = new ImageFilterView.ImageMatrix();
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.mOverlay = true;
        this.init(context, null);
    }
    
    public ImageFilterButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mImageMatrix = new ImageFilterView.ImageMatrix();
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.mOverlay = true;
        this.init(context, attrs);
    }
    
    public ImageFilterButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mImageMatrix = new ImageFilterView.ImageMatrix();
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.mOverlay = true;
        this.init(context, attrs);
    }
    
    private void init(final Context context, final AttributeSet attrs) {
        this.setPadding(0, 0, 0, 0);
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.ImageFilterView);
            final int N = a.getIndexCount();
            final Drawable drawable = a.getDrawable(R.styleable.ImageFilterView_altSrc);
            for (int i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.ImageFilterView_crossfade) {
                    this.mCrossfade = a.getFloat(attr, 0.0f);
                }
                else if (attr == R.styleable.ImageFilterView_warmth) {
                    this.setWarmth(a.getFloat(attr, 0.0f));
                }
                else if (attr == R.styleable.ImageFilterView_saturation) {
                    this.setSaturation(a.getFloat(attr, 0.0f));
                }
                else if (attr == R.styleable.ImageFilterView_contrast) {
                    this.setContrast(a.getFloat(attr, 0.0f));
                }
                else if (attr == R.styleable.ImageFilterView_round) {
                    this.setRound(a.getFloat(attr, 0.0f));
                }
                else if (attr == R.styleable.ImageFilterView_roundPercent) {
                    this.setRoundPercent(a.getFloat(attr, 0.0f));
                }
                else if (attr == R.styleable.ImageFilterView_overlay) {
                    this.setOverlay(a.getBoolean(attr, this.mOverlay));
                }
            }
            a.recycle();
            if (drawable != null) {
                (this.mLayers = new Drawable[2])[0] = this.getDrawable();
                this.mLayers[1] = drawable;
                this.mLayer = new LayerDrawable(this.mLayers);
                this.mLayer.getDrawable(1).setAlpha((int)(255.0f * this.mCrossfade));
                super.setImageDrawable((Drawable)this.mLayer);
            }
        }
    }
    
    private void setOverlay(final boolean overlay) {
        this.mOverlay = overlay;
    }
    
    public void setSaturation(final float saturation) {
        this.mImageMatrix.mSaturation = saturation;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }
    
    public float getSaturation() {
        return this.mImageMatrix.mSaturation;
    }
    
    public void setContrast(final float contrast) {
        this.mImageMatrix.mContrast = contrast;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }
    
    public float getContrast() {
        return this.mImageMatrix.mContrast;
    }
    
    public void setWarmth(final float warmth) {
        this.mImageMatrix.mWarmth = warmth;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }
    
    public float getWarmth() {
        return this.mImageMatrix.mWarmth;
    }
    
    public void setCrossfade(final float crossfade) {
        this.mCrossfade = crossfade;
        if (this.mLayers != null) {
            if (!this.mOverlay) {
                this.mLayer.getDrawable(0).setAlpha((int)(255.0f * (1.0f - this.mCrossfade)));
            }
            this.mLayer.getDrawable(1).setAlpha((int)(255.0f * this.mCrossfade));
            super.setImageDrawable((Drawable)this.mLayer);
        }
    }
    
    public float getCrossfade() {
        return this.mCrossfade;
    }
    
    public void setBrightness(final float brightness) {
        this.mImageMatrix.mBrightness = brightness;
        this.mImageMatrix.updateMatrix((ImageView)this);
    }
    
    public void setRoundPercent(final float round) {
        final boolean change = this.mRoundPercent != round;
        this.mRoundPercent = round;
        if (this.mRoundPercent != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21 && this.mViewOutlineProvider == null) {
                this.setOutlineProvider(this.mViewOutlineProvider = new ViewOutlineProvider() {
                    public void getOutline(final View view, final Outline outline) {
                        final int w = ImageFilterButton.this.getWidth();
                        final int h = ImageFilterButton.this.getHeight();
                        final float r = Math.min(w, h) * ImageFilterButton.this.mRoundPercent / 2.0f;
                        outline.setRoundRect(0, 0, w, h, r);
                    }
                });
                this.setClipToOutline(true);
            }
            final int w = this.getWidth();
            final int h = this.getHeight();
            final float r = Math.min(w, h) * this.mRoundPercent / 2.0f;
            this.mRect.set(0.0f, 0.0f, (float)w, (float)h);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, r, r, Path.Direction.CW);
        }
        else if (Build.VERSION.SDK_INT >= 21) {
            this.setClipToOutline(false);
        }
        if (change && Build.VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }
    
    public void setRound(final float round) {
        if (Float.isNaN(round)) {
            this.mRound = round;
            final float tmp = this.mRoundPercent;
            this.mRoundPercent = -1.0f;
            this.setRoundPercent(tmp);
            return;
        }
        final boolean change = this.mRound != round;
        this.mRound = round;
        if (this.mRound != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21 && this.mViewOutlineProvider == null) {
                this.setOutlineProvider(this.mViewOutlineProvider = new ViewOutlineProvider() {
                    public void getOutline(final View view, final Outline outline) {
                        final int w = ImageFilterButton.this.getWidth();
                        final int h = ImageFilterButton.this.getHeight();
                        outline.setRoundRect(0, 0, w, h, ImageFilterButton.this.mRound);
                    }
                });
                this.setClipToOutline(true);
            }
            final int w = this.getWidth();
            final int h = this.getHeight();
            this.mRect.set(0.0f, 0.0f, (float)w, (float)h);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, this.mRound, this.mRound, Path.Direction.CW);
        }
        else if (Build.VERSION.SDK_INT >= 21) {
            this.setClipToOutline(false);
        }
        if (change && Build.VERSION.SDK_INT >= 21) {
            this.invalidateOutline();
        }
    }
    
    public float getRoundPercent() {
        return this.mRoundPercent;
    }
    
    public float getRound() {
        return this.mRound;
    }
    
    public void draw(final Canvas canvas) {
        boolean clip = false;
        if (Build.VERSION.SDK_INT < 21 && this.mRound != 0.0f) {
            clip = true;
            canvas.save();
            canvas.clipPath(this.mPath);
        }
        super.draw(canvas);
        if (clip) {
            canvas.restore();
        }
    }
}
