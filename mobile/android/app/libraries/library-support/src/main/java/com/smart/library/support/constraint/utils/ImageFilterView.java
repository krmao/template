package com.smart.library.support.constraint.utils;

import android.support.v7.widget.*;
import android.graphics.drawable.*;
import android.content.*;
import android.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import android.widget.*;
import android.os.*;
import android.view.*;
import android.graphics.*;

public class ImageFilterView extends AppCompatImageView
{
    private ImageMatrix mImageMatrix;
    private boolean mOverlay;
    private float mCrossfade;
    private float mRoundPercent;
    private float mRound;
    private Path mPath;
    ViewOutlineProvider mViewOutlineProvider;
    RectF mRect;
    Drawable[] mLayers;
    LayerDrawable mLayer;
    
    public ImageFilterView(final Context context) {
        super(context);
        this.mImageMatrix = new ImageMatrix();
        this.mOverlay = true;
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.init(context, null);
    }
    
    public ImageFilterView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mImageMatrix = new ImageMatrix();
        this.mOverlay = true;
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.init(context, attrs);
    }
    
    public ImageFilterView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mImageMatrix = new ImageMatrix();
        this.mOverlay = true;
        this.mCrossfade = 0.0f;
        this.mRoundPercent = 0.0f;
        this.mRound = Float.NaN;
        this.init(context, attrs);
    }
    
    private void init(final Context context, final AttributeSet attrs) {
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
                    this.setRoundPercent(a.getDimension(attr, 0.0f));
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
    
    public float getBrightness() {
        return this.mImageMatrix.mBrightness;
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
                        final int w = ImageFilterView.this.getWidth();
                        final int h = ImageFilterView.this.getHeight();
                        final float r = Math.min(w, h) * ImageFilterView.this.mRoundPercent / 2.0f;
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
                        final int w = ImageFilterView.this.getWidth();
                        final int h = ImageFilterView.this.getHeight();
                        outline.setRoundRect(0, 0, w, h, ImageFilterView.this.mRound);
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
        if (Build.VERSION.SDK_INT < 21 && this.mRoundPercent != 0.0f) {
            clip = true;
            canvas.save();
            canvas.clipPath(this.mPath);
        }
        super.draw(canvas);
        if (clip) {
            canvas.restore();
        }
    }
    
    static class ImageMatrix
    {
        float[] m;
        ColorMatrix mColorMatrix;
        ColorMatrix mTmpColorMatrix;
        float mBrightness;
        float mSaturation;
        float mContrast;
        float mWarmth;
        
        ImageMatrix() {
            this.m = new float[20];
            this.mColorMatrix = new ColorMatrix();
            this.mTmpColorMatrix = new ColorMatrix();
            this.mBrightness = 1.0f;
            this.mSaturation = 1.0f;
            this.mContrast = 1.0f;
            this.mWarmth = 1.0f;
        }
        
        private void saturation(final float saturationStrength) {
            final float Rf = 0.2999f;
            final float Gf = 0.587f;
            final float Bf = 0.114f;
            final float MS = 1.0f - saturationStrength;
            final float Rt = Rf * MS;
            final float Gt = Gf * MS;
            final float Bt = Bf * MS;
            this.m[0] = Rt + saturationStrength;
            this.m[1] = Gt;
            this.m[2] = Bt;
            this.m[3] = 0.0f;
            this.m[4] = 0.0f;
            this.m[5] = Rt;
            this.m[6] = Gt + saturationStrength;
            this.m[7] = Bt;
            this.m[8] = 0.0f;
            this.m[9] = 0.0f;
            this.m[10] = Rt;
            this.m[11] = Gt;
            this.m[12] = Bt + saturationStrength;
            this.m[13] = 0.0f;
            this.m[14] = 0.0f;
            this.m[15] = 0.0f;
            this.m[16] = 0.0f;
            this.m[17] = 0.0f;
            this.m[18] = 1.0f;
            this.m[19] = 0.0f;
        }
        
        private void warmth(float warmth) {
            final float baseTemprature = 5000.0f;
            if (warmth <= 0.0f) {
                warmth = 0.01f;
            }
            float kelvin = baseTemprature / warmth;
            final float centiKelvin = kelvin / 100.0f;
            float colorR;
            float colorG;
            if (centiKelvin > 66.0f) {
                final float tmp = centiKelvin - 60.0f;
                colorR = 329.69873f * (float)Math.pow(tmp, -0.13320475816726685);
                colorG = 288.12216f * (float)Math.pow(tmp, 0.07551484555006027);
            }
            else {
                colorG = 99.4708f * (float)Math.log(centiKelvin) - 161.11957f;
                colorR = 255.0f;
            }
            float colorB;
            if (centiKelvin < 66.0f) {
                if (centiKelvin > 19.0f) {
                    colorB = 138.51773f * (float)Math.log(centiKelvin - 10.0f) - 305.0448f;
                }
                else {
                    colorB = 0.0f;
                }
            }
            else {
                colorB = 255.0f;
            }
            float tmpColor_r = Math.min(255.0f, Math.max(colorR, 0.0f));
            float tmpColor_g = Math.min(255.0f, Math.max(colorG, 0.0f));
            float tmpColor_b = Math.min(255.0f, Math.max(colorB, 0.0f));
            float color_r = tmpColor_r;
            float color_g = tmpColor_g;
            float color_b = tmpColor_b;
            kelvin = baseTemprature;
            final float centiKelvin2 = kelvin / 100.0f;
            float colorR2;
            float colorG2;
            if (centiKelvin2 > 66.0f) {
                final float tmp2 = centiKelvin2 - 60.0f;
                colorR2 = 329.69873f * (float)Math.pow(tmp2, -0.13320475816726685);
                colorG2 = 288.12216f * (float)Math.pow(tmp2, 0.07551484555006027);
            }
            else {
                colorG2 = 99.4708f * (float)Math.log(centiKelvin2) - 161.11957f;
                colorR2 = 255.0f;
            }
            float colorB2;
            if (centiKelvin2 < 66.0f) {
                if (centiKelvin2 > 19.0f) {
                    colorB2 = 138.51773f * (float)Math.log(centiKelvin2 - 10.0f) - 305.0448f;
                }
                else {
                    colorB2 = 0.0f;
                }
            }
            else {
                colorB2 = 255.0f;
            }
            tmpColor_r = Math.min(255.0f, Math.max(colorR2, 0.0f));
            tmpColor_g = Math.min(255.0f, Math.max(colorG2, 0.0f));
            tmpColor_b = Math.min(255.0f, Math.max(colorB2, 0.0f));
            color_r /= tmpColor_r;
            color_g /= tmpColor_g;
            color_b /= tmpColor_b;
            this.m[0] = color_r;
            this.m[1] = 0.0f;
            this.m[2] = 0.0f;
            this.m[3] = 0.0f;
            this.m[4] = 0.0f;
            this.m[5] = 0.0f;
            this.m[6] = color_g;
            this.m[7] = 0.0f;
            this.m[8] = 0.0f;
            this.m[9] = 0.0f;
            this.m[10] = 0.0f;
            this.m[11] = 0.0f;
            this.m[12] = color_b;
            this.m[13] = 0.0f;
            this.m[14] = 0.0f;
            this.m[15] = 0.0f;
            this.m[16] = 0.0f;
            this.m[17] = 0.0f;
            this.m[18] = 1.0f;
            this.m[19] = 0.0f;
        }
        
        private void brightness(final float brightness) {
            this.m[0] = brightness;
            this.m[1] = 0.0f;
            this.m[2] = 0.0f;
            this.m[3] = 0.0f;
            this.m[4] = 0.0f;
            this.m[5] = 0.0f;
            this.m[6] = brightness;
            this.m[7] = 0.0f;
            this.m[8] = 0.0f;
            this.m[9] = 0.0f;
            this.m[10] = 0.0f;
            this.m[11] = 0.0f;
            this.m[12] = brightness;
            this.m[13] = 0.0f;
            this.m[14] = 0.0f;
            this.m[15] = 0.0f;
            this.m[16] = 0.0f;
            this.m[17] = 0.0f;
            this.m[18] = 1.0f;
            this.m[19] = 0.0f;
        }
        
        void updateMatrix(final ImageView view) {
            this.mColorMatrix.reset();
            boolean filter = false;
            if (this.mSaturation != 1.0f) {
                this.saturation(this.mSaturation);
                this.mColorMatrix.set(this.m);
                filter = true;
            }
            if (this.mContrast != 1.0f) {
                this.mTmpColorMatrix.setScale(this.mContrast, this.mContrast, this.mContrast, 1.0f);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            if (this.mWarmth != 1.0f) {
                this.warmth(this.mWarmth);
                this.mTmpColorMatrix.set(this.m);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            if (this.mBrightness != 1.0f) {
                this.brightness(this.mBrightness);
                this.mTmpColorMatrix.set(this.m);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            if (filter) {
                view.setColorFilter((ColorFilter)new ColorMatrixColorFilter(this.mColorMatrix));
            }
            else {
                view.clearColorFilter();
            }
        }
    }
}
