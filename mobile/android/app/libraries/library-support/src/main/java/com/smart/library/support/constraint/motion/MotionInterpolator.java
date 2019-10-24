package com.smart.library.support.constraint.motion;

import android.view.animation.*;

public abstract class MotionInterpolator implements Interpolator
{
    public abstract float getInterpolation(final float p0);
    
    public abstract float getVelocity();
}
