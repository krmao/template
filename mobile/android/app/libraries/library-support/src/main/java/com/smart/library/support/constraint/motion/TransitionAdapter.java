package com.smart.library.support.constraint.motion;

public abstract class TransitionAdapter implements MotionLayout.TransitionListener
{
    @Override
    public void onTransitionStarted(final MotionLayout motionLayout, final int startId, final int endId) {
    }
    
    @Override
    public void onTransitionChange(final MotionLayout motionLayout, final int startId, final int endId, final float progress) {
    }
    
    @Override
    public void onTransitionCompleted(final MotionLayout motionLayout, final int currentId) {
    }
    
    @Override
    public void onTransitionTrigger(final MotionLayout motionLayout, final int triggerId, final boolean positive, final float progress) {
    }
}
