package com.smart.library.source;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.core.view.ViewCompat;

import java.util.Arrays;

@Keep
public class STViewDragHelper {
    private static final String TAG = "STViewDragHelper";
    public static final int INVALID_POINTER = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    public static final int EDGE_TOP = 4;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_ALL = 15;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int DIRECTION_ALL = 3;
    private static final int EDGE_SIZE = 20;
    private static final int BASE_SETTLE_DURATION = 256;
    private static final int MAX_SETTLE_DURATION = 600;
    private int mDragState;
    private int mTouchSlop;
    private int mActivePointerId;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private int[] mInitialEdgesTouched;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mPointersDown;
    private VelocityTracker mVelocityTracker;
    private float mMaxVelocity;
    private float mMinVelocity;
    private int mEdgeSize;
    private int mTrackingEdges;
    private OverScroller mScroller;
    private final Callback mCallback;
    private View mCapturedView;
    private boolean mReleaseInProgress;
    private final ViewGroup mParentView;
    private static final Interpolator sInterpolator;
    private final Runnable mSetIdleRunnable;

    public static STViewDragHelper create(@NonNull final ViewGroup forParent, @NonNull final Callback cb) {
        return new STViewDragHelper(forParent.getContext(), forParent, cb);
    }

    public static STViewDragHelper create(@NonNull final ViewGroup forParent, final float sensitivity, @NonNull final Callback cb) {
        final STViewDragHelper helper = create(forParent, cb);
        helper.mTouchSlop *= (int) (1.0f / sensitivity);
        return helper;
    }

    private STViewDragHelper(@NonNull final Context context, @NonNull final ViewGroup forParent, @NonNull final Callback cb) {
        this.mActivePointerId = -1;
        this.mSetIdleRunnable = new Runnable() {
            @Override
            public void run() {
                STViewDragHelper.this.setDragState(0);
            }
        };
        if (forParent == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (cb == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.mParentView = forParent;
        this.mCallback = cb;
        final ViewConfiguration vc = ViewConfiguration.get(context);
        final float density = context.getResources().getDisplayMetrics().density;
        this.mEdgeSize = (int) (20.0f * density + 0.5f);
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        this.mMinVelocity = vc.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(context, STViewDragHelper.sInterpolator);
    }

    public void setMinVelocity(final float minVel) {
        this.mMinVelocity = minVel;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public void setEdgeTrackingEnabled(final int edgeFlags) {
        this.mTrackingEdges = edgeFlags;
    }

    @Px
    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public void captureChildView(@NonNull final View childView, final int activePointerId) {
        if (childView.getParent() != this.mParentView) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the STViewDragHelper's tracked parent view (" + this.mParentView + ")");
        }
        this.mCapturedView = childView;
        this.mActivePointerId = activePointerId;
        this.mCallback.onViewCaptured(childView, activePointerId);
        this.setDragState(1);
    }

    @Nullable
    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    @Px
    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        this.clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void abort() {
        this.cancel();
        if (this.mDragState == 2) {
            final int oldX = this.mScroller.getCurrX();
            final int oldY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            final int newX = this.mScroller.getCurrX();
            final int newY = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, newX, newY, newX - oldX, newY - oldY);
        }
        this.setDragState(0);
    }

    public boolean smoothSlideViewTo(@NonNull final View child, final int finalLeft, final int finalTop) {
        this.mCapturedView = child;
        this.mActivePointerId = -1;
        final boolean continueSliding = this.forceSettleCapturedViewAt(finalLeft, finalTop, 0, 0);
        if (!continueSliding && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return continueSliding;
    }

    public boolean settleCapturedViewAt(final int finalLeft, final int finalTop) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
        }
        return this.forceSettleCapturedViewAt(finalLeft, finalTop, (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId));
    }

    private boolean forceSettleCapturedViewAt(final int finalLeft, final int finalTop, final int xvel, final int yvel) {
        final int startLeft = this.mCapturedView.getLeft();
        final int startTop = this.mCapturedView.getTop();
        final int dx = finalLeft - startLeft;
        final int dy = finalTop - startTop;
        if (dx == 0 && dy == 0) {
            this.mScroller.abortAnimation();
            this.setDragState(0);
            return false;
        }
        final int duration = this.computeSettleDuration(this.mCapturedView, dx, dy, xvel, yvel);
        this.mScroller.startScroll(startLeft, startTop, dx, dy, duration);
        this.setDragState(2);
        return true;
    }

    private int computeSettleDuration(final View child, final int dx, final int dy, int xvel, int yvel) {
        xvel = this.clampMag(xvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        yvel = this.clampMag(yvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        final int absDx = Math.abs(dx);
        final int absDy = Math.abs(dy);
        final int absXVel = Math.abs(xvel);
        final int absYVel = Math.abs(yvel);
        final int addedVel = absXVel + absYVel;
        final int addedDistance = absDx + absDy;
        final float xweight = (xvel != 0) ? (absXVel / addedVel) : (absDx / addedDistance);
        final float yweight = (yvel != 0) ? (absYVel / addedVel) : (absDy / addedDistance);
        final int xduration = this.computeAxisDuration(dx, xvel, this.mCallback.getViewHorizontalDragRange(child));
        final int yduration = this.computeAxisDuration(dy, yvel, this.mCallback.getViewVerticalDragRange(child));
        return (int) (xduration * xweight + yduration * yweight);
    }

    private int computeAxisDuration(final int delta, int velocity, final int motionRange) {
        if (delta == 0) {
            return 0;
        }
        final int width = this.mParentView.getWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1.0f, Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
        velocity = Math.abs(velocity);
        int duration;
        if (velocity > 0) {
            duration = 4 * Math.round(1000.0f * Math.abs(distance / velocity));
        } else {
            final float range = Math.abs(delta) / motionRange;
            duration = (int) ((range + 1.0f) * 256.0f);
        }
        return Math.min(duration, 600);
    }

    private int clampMag(final int value, final int absMin, final int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0;
        }
        if (absValue > absMax) {
            return (value > 0) ? absMax : (-absMax);
        }
        return value;
    }

    private float clampMag(final float value, final float absMin, final float absMax) {
        final float absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0.0f;
        }
        if (absValue > absMax) {
            return (value > 0.0f) ? absMax : (-absMax);
        }
        return value;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f;
        f *= 0.47123894f;
        return (float) Math.sin(f);
    }

    public void flingCapturedView(final int minLeft, final int minTop, final int maxLeft, final int maxTop) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        }
        this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId), minLeft, maxLeft, minTop, maxTop);
        this.setDragState(2);
    }

    public boolean continueSettling(final boolean deferCallbacks) {
        if (this.mDragState == 2) {
            boolean keepGoing = this.mScroller.computeScrollOffset();
            final int x = this.mScroller.getCurrX();
            final int y = this.mScroller.getCurrY();
            final int dx = x - this.mCapturedView.getLeft();
            final int dy = y - this.mCapturedView.getTop();
            if (dx != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, dx);
            }
            if (dy != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, dy);
            }
            if (dx != 0 || dy != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, x, y, dx, dy);
            }
            if (keepGoing && x == this.mScroller.getFinalX() && y == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                keepGoing = false;
            }
            if (!keepGoing) {
                if (deferCallbacks) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    this.setDragState(0);
                }
            }
        }
        return this.mDragState == 2;
    }

    private void dispatchViewReleased(final float xvel, final float yvel) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, xvel, yvel);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            this.setDragState(0);
        }
    }

    private void clearMotionHistory() {
        if (this.mInitialMotionX == null) {
            return;
        }
        Arrays.fill(this.mInitialMotionX, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }

    private void clearMotionHistory(final int pointerId) {
        if (this.mInitialMotionX == null || !this.isPointerDown(pointerId)) {
            return;
        }
        this.mInitialMotionX[pointerId] = 0.0f;
        this.mInitialMotionY[pointerId] = 0.0f;
        this.mLastMotionX[pointerId] = 0.0f;
        this.mLastMotionY[pointerId] = 0.0f;
        this.mInitialEdgesTouched[pointerId] = 0;
        this.mEdgeDragsInProgress[pointerId] = 0;
        this.mEdgeDragsLocked[pointerId] = 0;
        this.mPointersDown &= ~(1 << pointerId);
    }

    private void ensureMotionHistorySizeForId(final int pointerId) {
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= pointerId) {
            final float[] imx = new float[pointerId + 1];
            final float[] imy = new float[pointerId + 1];
            final float[] lmx = new float[pointerId + 1];
            final float[] lmy = new float[pointerId + 1];
            final int[] iit = new int[pointerId + 1];
            final int[] edip = new int[pointerId + 1];
            final int[] edl = new int[pointerId + 1];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, 0, imx, 0, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, 0, imy, 0, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, 0, lmx, 0, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, 0, lmy, 0, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, 0, iit, 0, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, 0, edip, 0, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, 0, edl, 0, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = imx;
            this.mInitialMotionY = imy;
            this.mLastMotionX = lmx;
            this.mLastMotionY = lmy;
            this.mInitialEdgesTouched = iit;
            this.mEdgeDragsInProgress = edip;
            this.mEdgeDragsLocked = edl;
        }
    }

    private void saveInitialMotion(final float x, final float y, final int pointerId) {
        this.ensureMotionHistorySizeForId(pointerId);
        this.mInitialMotionX[pointerId] = (this.mLastMotionX[pointerId] = x);
        this.mInitialMotionY[pointerId] = (this.mLastMotionY[pointerId] = y);
        this.mInitialEdgesTouched[pointerId] = this.getEdgesTouched((int) x, (int) y);
        this.mPointersDown |= 1 << pointerId;
    }

    private void saveLastMotion(final MotionEvent ev) {
        for (int pointerCount = ev.getPointerCount(), i = 0; i < pointerCount; ++i) {
            final int pointerId = ev.getPointerId(i);
            if (this.isValidPointerForActionMove(pointerId)) {
                final float x = ev.getX(i);
                final float y = ev.getY(i);
                this.mLastMotionX[pointerId] = x;
                this.mLastMotionY[pointerId] = y;
            }
        }
    }

    public boolean isPointerDown(final int pointerId) {
        return (this.mPointersDown & 1 << pointerId) != 0x0;
    }

    void setDragState(final int state) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != state) {
            this.mDragState = state;
            this.mCallback.onViewDragStateChanged(state);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    boolean tryCaptureViewForDrag(final View toCapture, final int pointerId) {
        if (toCapture == this.mCapturedView && this.mActivePointerId == pointerId) {
            return true;
        }
        if (toCapture != null && this.mCallback.tryCaptureView(toCapture, pointerId)) {
            this.captureChildView(toCapture, this.mActivePointerId = pointerId);
            return true;
        }
        return false;
    }

    protected boolean canScroll(@NonNull final View v, final boolean checkV, final int dx, final int dy, final int x, final int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            for (int i = count - 1; i >= 0; --i) {
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, dy, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && (v.canScrollHorizontally(-dx) || v.canScrollVertically(-dy));
    }

    public boolean shouldInterceptTouchEvent(@NonNull final MotionEvent ev) {
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();
        if (action == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        switch (action) {
            case 0: {
                final float x = ev.getX();
                final float y = ev.getY();
                final int pointerId = ev.getPointerId(0);
                this.saveInitialMotion(x, y, pointerId);
                final View toCapture = this.findTopChildUnder((int) x, (int) y);
                if (toCapture == this.mCapturedView && this.mDragState == 2) {
                    this.tryCaptureViewForDrag(toCapture, pointerId);
                }
                final int edgesTouched = this.mInitialEdgesTouched[pointerId];
                if ((edgesTouched & this.mTrackingEdges) != 0x0) {
                    this.mCallback.onEdgeTouched(edgesTouched & this.mTrackingEdges, pointerId);
                    break;
                }
                break;
            }
            case 5: {
                final int pointerId2 = ev.getPointerId(actionIndex);
                final float x2 = ev.getX(actionIndex);
                final float y2 = ev.getY(actionIndex);
                this.saveInitialMotion(x2, y2, pointerId2);
                if (this.mDragState == 0) {
                    final int edgesTouched2 = this.mInitialEdgesTouched[pointerId2];
                    if ((edgesTouched2 & this.mTrackingEdges) != 0x0) {
                        this.mCallback.onEdgeTouched(edgesTouched2 & this.mTrackingEdges, pointerId2);
                    }
                    break;
                }
                if (this.mDragState == 2) {
                    final View toCapture = this.findTopChildUnder((int) x2, (int) y2);
                    if (toCapture == this.mCapturedView) {
                        this.tryCaptureViewForDrag(toCapture, pointerId2);
                    }
                    break;
                }
                break;
            }
            case 2: {
                if (this.mInitialMotionX == null) {
                    break;
                }
                if (this.mInitialMotionY == null) {
                    break;
                }
                for (int pointerCount = ev.getPointerCount(), i = 0; i < pointerCount; ++i) {
                    final int pointerId = ev.getPointerId(i);
                    if (this.isValidPointerForActionMove(pointerId)) {
                        final float x3 = ev.getX(i);
                        final float y3 = ev.getY(i);
                        final float dx = x3 - this.mInitialMotionX[pointerId];
                        final float dy = y3 - this.mInitialMotionY[pointerId];
                        final View toCapture2 = this.findTopChildUnder((int) x3, (int) y3);
                        final boolean pastSlop = toCapture2 != null && this.checkTouchSlop(toCapture2, dx, dy);
                        if (pastSlop) {
                            final int oldLeft = toCapture2.getLeft();
                            final int targetLeft = oldLeft + (int) dx;
                            final int newLeft = this.mCallback.clampViewPositionHorizontal(toCapture2, targetLeft, (int) dx);
                            final int oldTop = toCapture2.getTop();
                            final int targetTop = oldTop + (int) dy;
                            final int newTop = this.mCallback.clampViewPositionVertical(toCapture2, targetTop, (int) dy);
                            final int hDragRange = this.mCallback.getViewHorizontalDragRange(toCapture2);
                            final int vDragRange = this.mCallback.getViewVerticalDragRange(toCapture2);
                            if (hDragRange == 0 || (hDragRange > 0 && newLeft == oldLeft)) {
                                if (vDragRange == 0) {
                                    break;
                                }
                                if (vDragRange > 0 && newTop == oldTop) {
                                    break;
                                }
                            }
                        }
                        this.reportNewEdgeDrags(dx, dy, pointerId);
                        if (this.mDragState == 1) {
                            break;
                        }
                        if (pastSlop && this.tryCaptureViewForDrag(toCapture2, pointerId)) {
                            break;
                        }
                    }
                }
                this.saveLastMotion(ev);
                break;
            }
            case 6: {
                final int pointerId2 = ev.getPointerId(actionIndex);
                this.clearMotionHistory(pointerId2);
                break;
            }
            case 1:
            case 3: {
                this.cancel();
                break;
            }
        }
        return this.mDragState == 1;
    }

    public void processTouchEvent(@NonNull final MotionEvent ev) {
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();
        if (action == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        switch (action) {
            case 0: {
                final float x = ev.getX();
                final float y = ev.getY();
                final int pointerId = ev.getPointerId(0);
                final View toCapture = this.findTopChildUnder((int) x, (int) y);
                this.saveInitialMotion(x, y, pointerId);
                this.tryCaptureViewForDrag(toCapture, pointerId);
                final int edgesTouched = this.mInitialEdgesTouched[pointerId];
                if ((edgesTouched & this.mTrackingEdges) != 0x0) {
                    this.mCallback.onEdgeTouched(edgesTouched & this.mTrackingEdges, pointerId);
                    break;
                }
                break;
            }
            case 5: {
                final int pointerId2 = ev.getPointerId(actionIndex);
                final float x2 = ev.getX(actionIndex);
                final float y2 = ev.getY(actionIndex);
                this.saveInitialMotion(x2, y2, pointerId2);
                if (this.mDragState == 0) {
                    final View toCapture = this.findTopChildUnder((int) x2, (int) y2);
                    this.tryCaptureViewForDrag(toCapture, pointerId2);
                    final int edgesTouched = this.mInitialEdgesTouched[pointerId2];
                    if ((edgesTouched & this.mTrackingEdges) != 0x0) {
                        this.mCallback.onEdgeTouched(edgesTouched & this.mTrackingEdges, pointerId2);
                    }
                    break;
                }
                if (this.isCapturedViewUnder((int) x2, (int) y2)) {
                    this.tryCaptureViewForDrag(this.mCapturedView, pointerId2);
                    break;
                }
                break;
            }
            case 2: {
                if (this.mDragState != 1) {
                    for (int pointerCount = ev.getPointerCount(), i = 0; i < pointerCount; ++i) {
                        final int pointerId = ev.getPointerId(i);
                        if (this.isValidPointerForActionMove(pointerId)) {
                            final float x3 = ev.getX(i);
                            final float y3 = ev.getY(i);
                            final float dx = x3 - this.mInitialMotionX[pointerId];
                            final float dy = y3 - this.mInitialMotionY[pointerId];
                            this.reportNewEdgeDrags(dx, dy, pointerId);
                            if (this.mDragState == 1) {
                                break;
                            }
                            final View toCapture2 = this.findTopChildUnder((int) x3, (int) y3);
                            if (this.checkTouchSlop(toCapture2, dx, dy) && this.tryCaptureViewForDrag(toCapture2, pointerId)) {
                                break;
                            }
                        }
                    }
                    this.saveLastMotion(ev);
                    break;
                }
                if (!this.isValidPointerForActionMove(this.mActivePointerId)) {
                    break;
                }
                final int index = ev.findPointerIndex(this.mActivePointerId);
                final float x2 = ev.getX(index);
                final float y2 = ev.getY(index);
                final int idx = (int) (x2 - this.mLastMotionX[this.mActivePointerId]);
                final int idy = (int) (y2 - this.mLastMotionY[this.mActivePointerId]);
                this.dragTo(this.mCapturedView.getLeft() + idx, this.mCapturedView.getTop() + idy, idx, idy);
                this.saveLastMotion(ev);
                break;
            }
            case 6: {
                final int pointerId2 = ev.getPointerId(actionIndex);
                if (this.mDragState == 1 && pointerId2 == this.mActivePointerId) {
                    int newActivePointer = -1;
                    for (int pointerCount2 = ev.getPointerCount(), j = 0; j < pointerCount2; ++j) {
                        final int id = ev.getPointerId(j);
                        if (id != this.mActivePointerId) {
                            final float x4 = ev.getX(j);
                            final float y4 = ev.getY(j);
                            if (this.findTopChildUnder((int) x4, (int) y4) == this.mCapturedView && this.tryCaptureViewForDrag(this.mCapturedView, id)) {
                                newActivePointer = this.mActivePointerId;
                                break;
                            }
                        }
                    }
                    if (newActivePointer == -1) {
                        this.releaseViewForPointerUp();
                    }
                }
                this.clearMotionHistory(pointerId2);
                break;
            }
            case 1: {
                if (this.mDragState == 1) {
                    this.releaseViewForPointerUp();
                }
                this.cancel();
                break;
            }
            case 3: {
                if (this.mDragState == 1) {
                    this.dispatchViewReleased(0.0f, 0.0f);
                }
                this.cancel();
                break;
            }
        }
    }

    private void reportNewEdgeDrags(final float dx, final float dy, final int pointerId) {
        int dragsStarted = 0;
        if (this.checkNewEdgeDrag(dx, dy, pointerId, 1)) {
            dragsStarted |= 0x1;
        }
        if (this.checkNewEdgeDrag(dy, dx, pointerId, 4)) {
            dragsStarted |= 0x4;
        }
        if (this.checkNewEdgeDrag(dx, dy, pointerId, 2)) {
            dragsStarted |= 0x2;
        }
        if (this.checkNewEdgeDrag(dy, dx, pointerId, 8)) {
            dragsStarted |= 0x8;
        }
        if (dragsStarted != 0) {
            final int[] mEdgeDragsInProgress = this.mEdgeDragsInProgress;
            mEdgeDragsInProgress[pointerId] |= dragsStarted;
            this.mCallback.onEdgeDragStarted(dragsStarted, pointerId);
        }
    }

    private boolean checkNewEdgeDrag(final float delta, final float odelta, final int pointerId, final int edge) {
        final float absDelta = Math.abs(delta);
        final float absODelta = Math.abs(odelta);
        if ((this.mInitialEdgesTouched[pointerId] & edge) != edge || (this.mTrackingEdges & edge) == 0x0 || (this.mEdgeDragsLocked[pointerId] & edge) == edge || (this.mEdgeDragsInProgress[pointerId] & edge) == edge || (absDelta <= this.mTouchSlop && absODelta <= this.mTouchSlop)) {
            return false;
        }
        if (absDelta < absODelta * 0.5f && this.mCallback.onEdgeLock(edge)) {
            final int[] mEdgeDragsLocked = this.mEdgeDragsLocked;
            mEdgeDragsLocked[pointerId] |= edge;
            return false;
        }
        return (this.mEdgeDragsInProgress[pointerId] & edge) == 0x0 && absDelta > this.mTouchSlop;
    }

    private boolean checkTouchSlop(final View child, final float dx, final float dy) {
        if (child == null) {
            return false;
        }
        final boolean checkHorizontal = this.mCallback.getViewHorizontalDragRange(child) > 0;
        final boolean checkVertical = this.mCallback.getViewVerticalDragRange(child) > 0;
        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > this.mTouchSlop * this.mTouchSlop;
        }
        if (checkHorizontal) {
            return Math.abs(dx) > this.mTouchSlop;
        }
        return checkVertical && Math.abs(dy) > this.mTouchSlop;
    }

    public boolean checkTouchSlop(final int directions) {
        for (int count = this.mInitialMotionX.length, i = 0; i < count; ++i) {
            if (this.checkTouchSlop(directions, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(final int directions, final int pointerId) {
        if (!this.isPointerDown(pointerId)) {
            return false;
        }
        final boolean checkHorizontal = (directions & 0x1) == 0x1;
        final boolean checkVertical = (directions & 0x2) == 0x2;
        final float dx = this.mLastMotionX[pointerId] - this.mInitialMotionX[pointerId];
        final float dy = this.mLastMotionY[pointerId] - this.mInitialMotionY[pointerId];
        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > this.mTouchSlop * this.mTouchSlop;
        }
        if (checkHorizontal) {
            return Math.abs(dx) > this.mTouchSlop;
        }
        return checkVertical && Math.abs(dy) > this.mTouchSlop;
    }

    public boolean isEdgeTouched(final int edges) {
        for (int count = this.mInitialEdgesTouched.length, i = 0; i < count; ++i) {
            if (this.isEdgeTouched(edges, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeTouched(final int edges, final int pointerId) {
        return this.isPointerDown(pointerId) && (this.mInitialEdgesTouched[pointerId] & edges) != 0x0;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        final float xvel = this.clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity);
        final float yvel = this.clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity);
        this.dispatchViewReleased(xvel, yvel);
    }

    private void dragTo(final int left, final int top, final int dx, final int dy) {
        int clampedX = left;
        int clampedY = top;
        final int oldLeft = this.mCapturedView.getLeft();
        final int oldTop = this.mCapturedView.getTop();
        if (dx != 0) {
            clampedX = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, left, dx);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, clampedX - oldLeft);
        }
        if (dy != 0) {
            clampedY = this.mCallback.clampViewPositionVertical(this.mCapturedView, top, dy);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, clampedY - oldTop);
        }
        if (dx != 0 || dy != 0) {
            final int clampedDx = clampedX - oldLeft;
            final int clampedDy = clampedY - oldTop;
            this.mCallback.onViewPositionChanged(this.mCapturedView, clampedX, clampedY, clampedDx, clampedDy);
        }
    }

    public boolean isCapturedViewUnder(final int x, final int y) {
        return this.isViewUnder(this.mCapturedView, x, y);
    }

    public boolean isViewUnder(@Nullable final View view, final int x, final int y) {
        return view != null && x >= view.getLeft() && x < view.getRight() && y >= view.getTop() && y < view.getBottom();
    }

    @Nullable
    public View findTopChildUnder(final int x, final int y) {
        final int childCount = this.mParentView.getChildCount();
        for (int i = childCount - 1; i >= 0; --i) {
            final View child = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
            if (x >= child.getLeft() && x < child.getRight() && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    private int getEdgesTouched(final int x, final int y) {
        int result = 0;
        if (x < this.mParentView.getLeft() + this.mEdgeSize) {
            result |= 0x1;
        }
        if (y < this.mParentView.getTop() + this.mEdgeSize) {
            result |= 0x4;
        }
        if (x > this.mParentView.getRight() - this.mEdgeSize) {
            result |= 0x2;
        }
        if (y > this.mParentView.getBottom() - this.mEdgeSize) {
            result |= 0x8;
        }
        return result;
    }

    private boolean isValidPointerForActionMove(final int pointerId) {
        if (!this.isPointerDown(pointerId)) {
            Log.e("ViewDragHelper", "Ignoring pointerId=" + pointerId + " because ACTION_DOWN was not received " + "for this pointer before ACTION_MOVE. It likely happened because " + " STViewDragHelper did not receive all the events in the event stream.");
            return false;
        }
        return true;
    }

    static {
        sInterpolator = (Interpolator) new Interpolator() {
            public float getInterpolation(float t) {
                --t;
                return t * t * t * t * t + 1.0f;
            }
        };
    }

    public abstract static class Callback {
        public void onViewDragStateChanged(final int state) {
        }

        public void onViewPositionChanged(@NonNull final View changedView, final int left, final int top, @Px final int dx, @Px final int dy) {
        }

        public void onViewCaptured(@NonNull final View capturedChild, final int activePointerId) {
        }

        public void onViewReleased(@NonNull final View releasedChild, final float xvel, final float yvel) {
        }

        public void onEdgeTouched(final int edgeFlags, final int pointerId) {
        }

        public boolean onEdgeLock(final int edgeFlags) {
            return false;
        }

        public void onEdgeDragStarted(final int edgeFlags, final int pointerId) {
        }

        public int getOrderedChildIndex(final int index) {
            return index;
        }

        public int getViewHorizontalDragRange(@NonNull final View child) {
            return 0;
        }

        public int getViewVerticalDragRange(@NonNull final View child) {
            return 0;
        }

        public abstract boolean tryCaptureView(@NonNull final View p0, final int p1);

        public int clampViewPositionHorizontal(@NonNull final View child, final int left, final int dx) {
            return 0;
        }

        public int clampViewPositionVertical(@NonNull final View child, final int top, final int dy) {
            return 0;
        }
    }
}
