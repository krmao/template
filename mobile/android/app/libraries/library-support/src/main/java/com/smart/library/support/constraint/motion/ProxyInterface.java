package com.smart.library.support.constraint.motion;

interface ProxyInterface
{
    void setToolPosition(final float p0);
    
    long getTransitionTimeMs();
    
    boolean setKeyFramePosition(final Object p0, final int p1, final int p2, final float p3, final float p4);
    
    int designAccess(final int p0, final String p1, final Object p2, final float[] p3, final int p4, final float[] p5, final int p6);
    
    void setAttributes(final int p0, final String p1, final Object p2, final Object p3);
    
    float getKeyFramePosition(final Object p0, final int p1, final float p2, final float p3);
    
    void setKeyFrame(final Object p0, final int p1, final String p2, final Object p3);
    
    Boolean getPositionKeyframe(final Object p0, final Object p1, final float p2, final float p3, final String[] p4, final float[] p5);
    
    Object getKeyframeAtLocation(final Object p0, final float p1, final float p2);
}
