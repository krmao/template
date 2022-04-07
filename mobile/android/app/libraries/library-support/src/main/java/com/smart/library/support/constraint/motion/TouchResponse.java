package com.smart.library.support.constraint.motion;

import android.content.*;
import org.xmlpull.v1.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.content.res.*;
import android.util.*;
import android.support.v4.widget.*;
import android.view.*;
import android.graphics.*;

class TouchResponse
{
    private static final String TAG = "TouchResponse";
    private static final boolean DEBUG = false;
    private int mTouchAnchorSide;
    private int mTouchSide;
    private int mOnTouchUp;
    private int mTouchAnchorId;
    private int mTouchRegionId;
    private float mTouchAnchorY;
    private float mTouchAnchorX;
    private float mTouchDirectionX;
    private float mTouchDirectionY;
    private boolean mDragStarted;
    private float[] mAnchorDpDt;
    private float mLastTouchX;
    private float mLastTouchY;
    private final MotionLayout mMotionLayout;
    private static final float[][] TOUCH_SIDES;
    private static final float[][] TOUCH_DIRECTION;
    private float mMaxVelocity;
    private float mMaxAcceleration;
    private boolean mMoveWhenScrollAtTop;
    private float mDragScale;
    
    TouchResponse(final Context context, final MotionLayout layout, final XmlPullParser parser) {
        this.mTouchAnchorSide = 0;
        this.mTouchSide = 0;
        this.mOnTouchUp = 0;
        this.mTouchAnchorId = -1;
        this.mTouchRegionId = -1;
        this.mTouchAnchorY = 0.5f;
        this.mTouchAnchorX = 0.5f;
        this.mTouchDirectionX = 0.0f;
        this.mTouchDirectionY = 1.0f;
        this.mDragStarted = false;
        this.mAnchorDpDt = new float[2];
        this.mMaxVelocity = 4.0f;
        this.mMaxAcceleration = 1.2f;
        this.mMoveWhenScrollAtTop = true;
        this.mDragScale = 1.0f;
        this.mMotionLayout = layout;
        this.fillFromAttributeList(context, Xml.asAttributeSet(parser));
    }
    
    private void fillFromAttributeList(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OnSwipe);
        this.fill(a);
        a.recycle();
    }
    
    private void fill(final TypedArray a) {
        for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.OnSwipe_touchAnchorId) {
                this.mTouchAnchorId = a.getResourceId(attr, this.mTouchAnchorId);
            }
            else if (attr == R.styleable.OnSwipe_touchAnchorSide) {
                this.mTouchAnchorSide = a.getInt(attr, this.mTouchAnchorSide);
                this.mTouchAnchorX = TouchResponse.TOUCH_SIDES[this.mTouchAnchorSide][0];
                this.mTouchAnchorY = TouchResponse.TOUCH_SIDES[this.mTouchAnchorSide][1];
            }
            else if (attr == R.styleable.OnSwipe_dragDirection) {
                this.mTouchSide = a.getInt(attr, this.mTouchSide);
                this.mTouchDirectionX = TouchResponse.TOUCH_DIRECTION[this.mTouchSide][0];
                this.mTouchDirectionY = TouchResponse.TOUCH_DIRECTION[this.mTouchSide][1];
            }
            else if (attr == R.styleable.OnSwipe_maxVelocity) {
                this.mMaxVelocity = a.getFloat(attr, this.mMaxVelocity);
            }
            else if (attr == R.styleable.OnSwipe_maxAcceleration) {
                this.mMaxAcceleration = a.getFloat(attr, this.mMaxAcceleration);
            }
            else if (attr == R.styleable.OnSwipe_moveWhenScrollAtTop) {
                this.mMoveWhenScrollAtTop = a.getBoolean(attr, this.mMoveWhenScrollAtTop);
            }
            else if (attr == R.styleable.OnSwipe_dragScale) {
                this.mDragScale = a.getFloat(attr, this.mDragScale);
            }
            else if (attr == R.styleable.OnSwipe_touchRegionId) {
                this.mTouchRegionId = a.getResourceId(attr, this.mTouchRegionId);
            }
            else if (attr == R.styleable.OnSwipe_onTouchUp) {
                this.mOnTouchUp = a.getInt(attr, this.mOnTouchUp);
            }
        }
    }
    
    void setUpTouchEvent(final float lastTouchX, final float lastTouchY) {
        this.mLastTouchX = lastTouchX;
        this.mLastTouchY = lastTouchY;
        this.mDragStarted = false;
    }
    
    void processTouchEvent(final MotionEvent event, final MotionLayout.MotionTracker velocityTracker, final int currentState, final MotionScene motionScene) {
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case 0: {
                this.mLastTouchX = event.getRawX();
                this.mLastTouchY = event.getRawY();
                this.mDragStarted = false;
                break;
            }
            case 2: {
                final float dy = event.getRawY() - this.mLastTouchY;
                final float dx = event.getRawX() - this.mLastTouchX;
                final float drag = dx * this.mTouchDirectionX + dy * this.mTouchDirectionY;
                if (Math.abs(drag) > 10.0f || this.mDragStarted) {
                    float pos = this.mMotionLayout.getProgress();
                    if (!this.mDragStarted) {
                        this.mDragStarted = true;
                        this.mMotionLayout.setProgress(pos);
                    }
                    if (this.mTouchAnchorId != -1) {
                        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, pos, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                    }
                    else {
                        final float minSize = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                        this.mAnchorDpDt[1] = minSize * this.mTouchDirectionY;
                        this.mAnchorDpDt[0] = minSize * this.mTouchDirectionX;
                    }
                    float movmentInDir = this.mTouchDirectionX * this.mAnchorDpDt[0] + this.mTouchDirectionY * this.mAnchorDpDt[1];
                    movmentInDir *= this.mDragScale;
                    if (Math.abs(movmentInDir) < 0.01) {
                        this.mAnchorDpDt[0] = 0.01f;
                        this.mAnchorDpDt[1] = 0.01f;
                    }
                    float change;
                    if (this.mTouchDirectionX != 0.0f) {
                        change = dx / this.mAnchorDpDt[0];
                    }
                    else {
                        change = dy / this.mAnchorDpDt[1];
                    }
                    pos = Math.max(Math.min(pos + change, 1.0f), 0.0f);
                    if (pos != this.mMotionLayout.getProgress()) {
                        this.mMotionLayout.setProgress(pos);
                        velocityTracker.computeCurrentVelocity(1000);
                        final float tvx = velocityTracker.getXVelocity();
                        final float tvy = velocityTracker.getYVelocity();
                        final float velocity = (this.mTouchDirectionX != 0.0f) ? (tvx / this.mAnchorDpDt[0]) : (tvy / this.mAnchorDpDt[1]);
                        this.mMotionLayout.mLastVelocity = velocity;
                    }
                    else {
                        this.mMotionLayout.mLastVelocity = 0.0f;
                    }
                    this.mLastTouchX = event.getRawX();
                    this.mLastTouchY = event.getRawY();
                    break;
                }
                break;
            }
            case 1: {
                this.mDragStarted = false;
                velocityTracker.computeCurrentVelocity(1000);
                final float tvx2 = velocityTracker.getXVelocity();
                final float tvy2 = velocityTracker.getYVelocity();
                float pos2 = this.mMotionLayout.getProgress();
                if (this.mTouchAnchorId != -1) {
                    this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, pos2, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                }
                else {
                    final float minSize2 = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                    this.mAnchorDpDt[1] = minSize2 * this.mTouchDirectionY;
                    this.mAnchorDpDt[0] = minSize2 * this.mTouchDirectionX;
                }
                final float movmentInDir2 = this.mTouchDirectionX * this.mAnchorDpDt[0] + this.mTouchDirectionY * this.mAnchorDpDt[1];
                float velocity2;
                if (this.mTouchDirectionX != 0.0f) {
                    velocity2 = tvx2 / this.mAnchorDpDt[0];
                }
                else {
                    velocity2 = tvy2 / this.mAnchorDpDt[1];
                }
                if (!Float.isNaN(velocity2)) {
                    pos2 += velocity2 / 3.0f;
                }
                if (pos2 != 0.0f && pos2 != 1.0f && this.mOnTouchUp != 3) {
                    this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, (pos2 < 0.5) ? 0.0f : 1.0f, velocity2);
                    break;
                }
                break;
            }
        }
    }
    
    void setDown(final float lastTouchX, final float lastTouchY) {
        this.mLastTouchX = lastTouchX;
        this.mLastTouchY = lastTouchY;
    }
    
    float getProgressDirection(final float dx, final float dy) {
        final float pos = this.mMotionLayout.getProgress();
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, pos, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float velocity;
        if (this.mTouchDirectionX != 0.0f) {
            if (this.mAnchorDpDt[0] == 0.0f) {
                this.mAnchorDpDt[0] = 1.0E-7f;
            }
            velocity = dx * this.mTouchDirectionX / this.mAnchorDpDt[0];
        }
        else {
            if (this.mAnchorDpDt[1] == 0.0f) {
                this.mAnchorDpDt[1] = 1.0E-7f;
            }
            velocity = dy * this.mTouchDirectionY / this.mAnchorDpDt[1];
        }
        return velocity;
    }
    
    void scrollUp(final float dx, final float dy) {
        this.mDragStarted = false;
        float pos = this.mMotionLayout.getProgress();
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, pos, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        final float movmentInDir = this.mTouchDirectionX * this.mAnchorDpDt[0] + this.mTouchDirectionY * this.mAnchorDpDt[1];
        float velocity;
        if (this.mTouchDirectionX != 0.0f) {
            velocity = dx * this.mTouchDirectionX / this.mAnchorDpDt[0];
        }
        else {
            velocity = dy * this.mTouchDirectionY / this.mAnchorDpDt[1];
        }
        if (!Float.isNaN(velocity)) {
            pos += velocity / 3.0f;
        }
        if (pos != 0.0f && (pos != 1.0f & this.mOnTouchUp != 3)) {
            this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, (pos < 0.5) ? 0.0f : 1.0f, velocity);
        }
    }
    
    void scrollMove(final float dx, final float dy) {
        final float drag = dx * this.mTouchDirectionX + dy * this.mTouchDirectionY;
        float pos = this.mMotionLayout.getProgress();
        if (!this.mDragStarted) {
            this.mDragStarted = true;
            this.mMotionLayout.setProgress(pos);
        }
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, pos, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        final float movmentInDir = this.mTouchDirectionX * this.mAnchorDpDt[0] + this.mTouchDirectionY * this.mAnchorDpDt[1];
        if (Math.abs(movmentInDir) < 0.01) {
            this.mAnchorDpDt[0] = 0.01f;
            this.mAnchorDpDt[1] = 0.01f;
        }
        float change;
        if (this.mTouchDirectionX != 0.0f) {
            change = dx * this.mTouchDirectionX / this.mAnchorDpDt[0];
        }
        else {
            change = dy * this.mTouchDirectionY / this.mAnchorDpDt[1];
        }
        pos = Math.max(Math.min(pos + change, 1.0f), 0.0f);
        if (pos != this.mMotionLayout.getProgress()) {
            this.mMotionLayout.setProgress(pos);
        }
    }
    
    void setupTouch() {
        final View view = this.mMotionLayout.findViewById(this.mTouchAnchorId);
        if (view == null) {
            Log.w("TouchResponse", " cannot find view to handle touch");
        }
        if (view instanceof NestedScrollView) {
            final NestedScrollView sv = (NestedScrollView)view;
            sv.setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    return false;
                }
            });
            sv.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)new NestedScrollView.OnScrollChangeListener() {
                public void onScrollChange(final NestedScrollView v, final int scrollX, final int scrollY, final int oldScrollX, final int oldScrollY) {
                }
            });
        }
    }
    
    public void setAnchorId(final int id) {
        this.mTouchAnchorId = id;
    }
    
    public int getAnchorId() {
        return this.mTouchAnchorId;
    }
    
    public void setTouchAnchorLocation(final float x, final float y) {
        this.mTouchAnchorX = x;
        this.mTouchAnchorY = y;
    }
    
    public void setMaxVelocity(final float velocity) {
        this.mMaxVelocity = velocity;
    }
    
    public void setMaxAcceleration(final float acceleration) {
        this.mMaxAcceleration = acceleration;
    }
    
    float getMaxAcceleration() {
        return this.mMaxAcceleration;
    }
    
    public float getMaxVelocity() {
        return this.mMaxVelocity;
    }
    
    boolean getMoveWhenScrollAtTop() {
        return this.mMoveWhenScrollAtTop;
    }
    
    RectF getTouchRegion(final ViewGroup layout, final RectF rect) {
        if (this.mTouchRegionId == -1) {
            return null;
        }
        final View view = layout.findViewById(this.mTouchRegionId);
        if (view == null) {
            return null;
        }
        rect.set((float)view.getLeft(), (float)view.getTop(), (float)view.getRight(), (float)view.getBottom());
        return rect;
    }
    
    int getTouchRegionId() {
        return this.mTouchRegionId;
    }
    
    float dot(final float dx, final float dy) {
        return dx * this.mTouchDirectionX + dy * this.mTouchDirectionY;
    }
    
    @Override
    public String toString() {
        return this.mTouchDirectionX + " , " + this.mTouchDirectionY;
    }
    
    static {
        TOUCH_SIDES = new float[][] { { 0.5f, 0.0f }, { 0.0f, 0.5f }, { 1.0f, 0.5f }, { 0.5f, 1.0f }, { 0.5f, 0.5f } };
        TOUCH_DIRECTION = new float[][] { { 0.0f, -1.0f }, { 0.0f, 1.0f }, { -1.0f, 0.0f }, { 1.0f, 0.0f } };
    }
}
