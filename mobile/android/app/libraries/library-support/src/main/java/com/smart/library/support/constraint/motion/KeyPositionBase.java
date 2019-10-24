package com.smart.library.support.constraint.motion;

import java.util.*;
import android.view.*;
import android.graphics.*;

abstract class KeyPositionBase extends Key
{
    protected static final float SELECTION_SLOPE = 20.0f;
    int mCurveFit;
    
    KeyPositionBase() {
        this.mCurveFit = KeyPositionBase.UNSET;
    }
    
    abstract void calcPosition(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5);
    
    abstract float getPositionX();
    
    abstract float getPositionY();
    
    @Override
    void getAttributeNames(final HashSet<String> attributes) {
    }
    
    abstract void positionAttributes(final View p0, final RectF p1, final RectF p2, final float p3, final float p4, final String[] p5, final float[] p6);
    
    public abstract boolean intersects(final int p0, final int p1, final RectF p2, final RectF p3, final float p4, final float p5);
}
