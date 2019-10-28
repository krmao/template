package com.smart.library.source;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class STBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int PEEK_HEIGHT_AUTO = -1;
    private static final float HIDE_THRESHOLD = 0.5f;
    private static final float HIDE_FRICTION = 0.1f;
    private boolean fitToContents;
    private float maximumVelocity;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private int lastPeekHeight;
    int fitToContentsOffset;
    int halfExpandedOffset;
    int collapsedOffset;
    boolean hideable;
    private boolean skipCollapsed;
    int state;
    ViewDragHelper viewDragHelper;
    private boolean ignoreEvents;
    private int lastNestedScrollDy;
    private boolean nestedScrolled;
    int parentHeight;
    public WeakReference<V> viewRef;
    public WeakReference<View> nestedScrollingChildRef;
    private BottomSheetCallback callback;
    private VelocityTracker velocityTracker;
    int activePointerId;
    private int initialY;
    boolean touchingScrollingChild;
    private Map<View, Integer> importantForAccessibilityMap;
    private final ViewDragHelper.Callback dragCallback;

    public STBottomSheetBehavior() {
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback() {
            public boolean tryCaptureView(@NonNull final View child, final int pointerId) {
                if (state == 1) {
                    return false;
                }
                if (touchingScrollingChild) {
                    return false;
                }
                if (state == 3 && activePointerId == pointerId) {
                    final View scroll = nestedScrollingChildRef.get();
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false;
                    }
                }
                return viewRef != null && viewRef.get() == child;
            }

            public void onViewPositionChanged(@NonNull final View changedView, final int left, final int top, final int dx, final int dy) {
                dispatchOnSlide(top);
            }

            public void onViewDragStateChanged(final int state) {
                if (state == 1) {
                    setStateInternal(1);
                }
            }

            public void onViewReleased(@NonNull final View releasedChild, final float xvel, final float yvel) {
                int top;
                int targetState;
                if (yvel < 0.0f) {
                    if (fitToContents) {
                        top = fitToContentsOffset;
                        targetState = 3;
                    } else {
                        final int currentTop = releasedChild.getTop();
                        if (currentTop > halfExpandedOffset) {
                            top = halfExpandedOffset;
                            targetState = 6;
                        } else {
                            top = 0;
                            targetState = 3;
                        }
                    }
                } else if (hideable && shouldHide(releasedChild, yvel) && (releasedChild.getTop() > collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                    top = parentHeight;
                    targetState = 5;
                } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                    final int currentTop = releasedChild.getTop();
                    if (fitToContents) {
                        if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                            top = fitToContentsOffset;
                            targetState = 3;
                        } else {
                            top = collapsedOffset;
                            targetState = 4;
                        }
                    } else if (currentTop < halfExpandedOffset) {
                        if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                            top = 0;
                            targetState = 3;
                        } else {
                            top = halfExpandedOffset;
                            targetState = 6;
                        }
                    } else if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset;
                        targetState = 6;
                    } else {
                        top = collapsedOffset;
                        targetState = 4;
                    }
                } else {
                    top = collapsedOffset;
                    targetState = 4;
                }
                if (viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top)) {
                    setStateInternal(2);
                    ViewCompat.postOnAnimation(releasedChild, (Runnable) new SettleRunnable(releasedChild, targetState));
                } else {
                    setStateInternal(targetState);
                }
            }

            public int clampViewPositionVertical(@NonNull final View child, final int top, final int dy) {
                return android.support.v4.math.MathUtils.clamp(top, getExpandedOffset(), hideable ? parentHeight : collapsedOffset);
            }

            public int clampViewPositionHorizontal(@NonNull final View child, final int left, final int dx) {
                return child.getLeft();
            }

            public int getViewVerticalDragRange(@NonNull final View child) {
                if (hideable) {
                    return parentHeight;
                }
                return collapsedOffset;
            }
        };
    }

    public STBottomSheetBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback() {
            public boolean tryCaptureView(@NonNull final View child, final int pointerId) {
                if (state == 1) {
                    return false;
                }
                if (touchingScrollingChild) {
                    return false;
                }
                if (state == 3 && activePointerId == pointerId) {
                    final View scroll = nestedScrollingChildRef.get();
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false;
                    }
                }
                return viewRef != null && viewRef.get() == child;
            }

            public void onViewPositionChanged(@NonNull final View changedView, final int left, final int top, final int dx, final int dy) {
                dispatchOnSlide(top);
            }

            public void onViewDragStateChanged(final int state) {
                if (state == 1) {
                    setStateInternal(1);
                }
            }

            public void onViewReleased(@NonNull final View releasedChild, final float xvel, final float yvel) {
                int top;
                int targetState;
                if (yvel < 0.0f) {
                    if (fitToContents) {
                        top = fitToContentsOffset;
                        targetState = 3;
                    } else {
                        final int currentTop = releasedChild.getTop();
                        if (currentTop > halfExpandedOffset) {
                            top = halfExpandedOffset;
                            targetState = 6;
                        } else {
                            top = 0;
                            targetState = 3;
                        }
                    }
                } else if (hideable && shouldHide(releasedChild, yvel) && (releasedChild.getTop() > collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                    top = parentHeight;
                    targetState = 5;
                } else if (yvel == 0.0f || Math.abs(xvel) > Math.abs(yvel)) {
                    final int currentTop = releasedChild.getTop();
                    if (fitToContents) {
                        if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                            top = fitToContentsOffset;
                            targetState = 3;
                        } else {
                            top = collapsedOffset;
                            targetState = 4;
                        }
                    } else if (currentTop < halfExpandedOffset) {
                        if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                            top = 0;
                            targetState = 3;
                        } else {
                            top = halfExpandedOffset;
                            targetState = 6;
                        }
                    } else if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset;
                        targetState = 6;
                    } else {
                        top = collapsedOffset;
                        targetState = 4;
                    }
                } else {
                    top = collapsedOffset;
                    targetState = 4;
                }
                if (viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top)) {
                    setStateInternal(2);
                    ViewCompat.postOnAnimation(releasedChild, (Runnable) new SettleRunnable(releasedChild, targetState));
                } else {
                    setStateInternal(targetState);
                }
            }

            public int clampViewPositionVertical(@NonNull final View child, final int top, final int dy) {
                return android.support.v4.math.MathUtils.clamp(top, getExpandedOffset(), hideable ? parentHeight : collapsedOffset);
            }

            public int clampViewPositionHorizontal(@NonNull final View child, final int left, final int dx) {
                return child.getLeft();
            }

            public int getViewVerticalDragRange(@NonNull final View child) {
                if (hideable) {
                    return parentHeight;
                }
                return collapsedOffset;
            }
        };
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout);
        final TypedValue value = a.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (value != null && value.data == -1) {
            this.setPeekHeight(value.data);
        } else {
            this.setPeekHeight(a.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setFitToContents(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        this.setSkipCollapsed(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        a.recycle();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        this.maximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public Parcelable onSaveInstanceState(final CoordinatorLayout parent, final V child) {
        return (Parcelable) new SavedState(super.onSaveInstanceState(parent, (V) child), this.state);
    }

    public void onRestoreInstanceState(final CoordinatorLayout parent, final V child, final Parcelable state) {
        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(parent, (V) child, ss.getSuperState());
        if (ss.state == 1 || ss.state == 2) {
            this.state = 4;
        } else {
            this.state = ss.state;
        }
    }

    public boolean onLayoutChild(final CoordinatorLayout parent, final V child, final int layoutDirection) {
        if (ViewCompat.getFitsSystemWindows((View) parent) && !ViewCompat.getFitsSystemWindows((View) child)) {
            child.setFitsSystemWindows(true);
        }
        final int savedTop = child.getTop();
        parent.onLayoutChild((View) child, layoutDirection);
        this.parentHeight = parent.getHeight();
        if (this.peekHeightAuto) {
            if (this.peekHeightMin == 0) {
                this.peekHeightMin = parent.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - parent.getWidth() * 9 / 16);
        } else {
            this.lastPeekHeight = this.peekHeight;
        }
        this.fitToContentsOffset = Math.max(0, this.parentHeight - child.getHeight());
        this.halfExpandedOffset = setHalfExpandedOffset(this.parentHeight / 2);
        this.calculateCollapsedOffset();
        if (this.state == 3) {
            ViewCompat.offsetTopAndBottom((View) child, this.getExpandedOffset());
        } else if (this.state == 6) {
            ViewCompat.offsetTopAndBottom((View) child, this.halfExpandedOffset);
        } else if (this.hideable && this.state == 5) {
            ViewCompat.offsetTopAndBottom((View) child, this.parentHeight);
        } else if (this.state == 4) {
            ViewCompat.offsetTopAndBottom((View) child, this.collapsedOffset);
        } else if (this.state == 1 || this.state == 2) {
            ViewCompat.offsetTopAndBottom((View) child, savedTop - child.getTop());
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create((ViewGroup) parent, this.dragCallback);
        }
        this.viewRef = new WeakReference<V>(child);
        this.nestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild(child));
        return true;
    }

    protected int setHalfExpandedOffset(int halfExpandedOffset) {
        this.halfExpandedOffset = halfExpandedOffset;
        return this.halfExpandedOffset;
    }

    public boolean onInterceptTouchEvent(final CoordinatorLayout parent, final V child, final MotionEvent event) {
        if (!child.isShown()) {
            this.ignoreEvents = true;
            return false;
        }
        final int action = event.getActionMasked();
        if (action == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(event);
        switch (action) {
            case 1:
            case 3: {
                this.touchingScrollingChild = false;
                this.activePointerId = -1;
                if (this.ignoreEvents) {
                    return this.ignoreEvents = false;
                }
                break;
            }
            case 0: {
                final int initialX = (int) event.getX();
                this.initialY = (int) event.getY();
                final View scroll = (this.nestedScrollingChildRef != null) ? this.nestedScrollingChildRef.get() : null;
                if (scroll != null && parent.isPointInChildBounds(scroll, initialX, this.initialY)) {
                    this.activePointerId = event.getPointerId(event.getActionIndex());
                    this.touchingScrollingChild = true;
                }
                this.ignoreEvents = (this.activePointerId == -1 && !parent.isPointInChildBounds((View) child, initialX, this.initialY));
                break;
            }
        }
        if (!this.ignoreEvents && this.viewDragHelper != null && this.viewDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        }
        final View scroll2 = (this.nestedScrollingChildRef != null) ? this.nestedScrollingChildRef.get() : null;
        return action == 2 && scroll2 != null && !this.ignoreEvents && this.state != 1 && !parent.isPointInChildBounds(scroll2, (int) event.getX(), (int) event.getY()) && this.viewDragHelper != null && Math.abs(this.initialY - event.getY()) > this.viewDragHelper.getTouchSlop();
    }

    public boolean onTouchEvent(final CoordinatorLayout parent, final V child, final MotionEvent event) {
        if (!child.isShown()) {
            return false;
        }
        final int action = event.getActionMasked();
        if (this.state == 1 && action == 0) {
            return true;
        }
        if (this.viewDragHelper != null) {
            this.viewDragHelper.processTouchEvent(event);
        }
        if (action == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(event);
        if (action == 2 && !this.ignoreEvents && Math.abs(this.initialY - event.getY()) > this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView((View) child, event.getPointerId(event.getActionIndex()));
        }
        return !this.ignoreEvents;
    }

    public boolean onStartNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final V child, @NonNull final View directTargetChild, @NonNull final View target, final int axes, final int type) {
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        return (axes & 0x2) != 0x0;
    }

    public void onNestedPreScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final V child, @NonNull final View target, final int dx, final int dy, @NonNull final int[] consumed, final int type) {
        if (type == 1) {
            return;
        }
        final View scrollingChild = this.nestedScrollingChildRef.get();
        if (target != scrollingChild) {
            return;
        }
        final int currentTop = child.getTop();
        final int newTop = currentTop - dy;
        if (dy > 0) {
            if (newTop < this.getExpandedOffset()) {
                consumed[1] = currentTop - this.getExpandedOffset();
                ViewCompat.offsetTopAndBottom((View) child, -consumed[1]);
                this.setStateInternal(3);
            } else {
                ViewCompat.offsetTopAndBottom((View) child, -(consumed[1] = dy));
                this.setStateInternal(1);
            }
        } else if (dy < 0 && !target.canScrollVertically(-1)) {
            if (newTop <= this.collapsedOffset || this.hideable) {
                ViewCompat.offsetTopAndBottom((View) child, -(consumed[1] = dy));
                this.setStateInternal(1);
            } else {
                consumed[1] = currentTop - this.collapsedOffset;
                ViewCompat.offsetTopAndBottom((View) child, -consumed[1]);
                this.setStateInternal(4);
            }
        }
        this.dispatchOnSlide(child.getTop());
        this.lastNestedScrollDy = dy;
        this.nestedScrolled = true;
    }

    public void onStopNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final V child, @NonNull final View target, final int type) {
        if (child.getTop() == this.getExpandedOffset()) {
            this.setStateInternal(3);
            return;
        }
        if (target != this.nestedScrollingChildRef.get() || !this.nestedScrolled) {
            return;
        }
        int top;
        int targetState;
        if (this.lastNestedScrollDy > 0) {
            top = this.getExpandedOffset();
            targetState = 3;
        } else if (this.hideable && this.shouldHide(child, this.getYVelocity())) {
            top = this.parentHeight;
            targetState = 5;
        } else if (this.lastNestedScrollDy == 0) {
            final int currentTop = child.getTop();
            if (this.fitToContents) {
                if (Math.abs(currentTop - this.fitToContentsOffset) < Math.abs(currentTop - this.collapsedOffset)) {
                    top = this.fitToContentsOffset;
                    targetState = 3;
                } else {
                    top = this.collapsedOffset;
                    targetState = 4;
                }
            } else if (currentTop < this.halfExpandedOffset) {
                if (currentTop < Math.abs(currentTop - this.collapsedOffset)) {
                    top = 0;
                    targetState = 3;
                } else {
                    top = this.halfExpandedOffset;
                    targetState = 6;
                }
            } else if (Math.abs(currentTop - this.halfExpandedOffset) < Math.abs(currentTop - this.collapsedOffset)) {
                top = this.halfExpandedOffset;
                targetState = 6;
            } else {
                top = this.collapsedOffset;
                targetState = 4;
            }
        } else {
            top = this.collapsedOffset;
            targetState = 4;
        }
        if (this.viewDragHelper.smoothSlideViewTo((View) child, child.getLeft(), top)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation((View) child, (Runnable) new SettleRunnable(child, targetState));
        } else {
            this.setStateInternal(targetState);
        }
        this.nestedScrolled = false;
    }

    public boolean onNestedPreFling(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final V child, @NonNull final View target, final float velocityX, final float velocityY) {
        return target == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, (V) child, target, velocityX, velocityY));
    }

    public boolean isFitToContents() {
        return this.fitToContents;
    }

    public void setFitToContents(final boolean fitToContents) {
        if (this.fitToContents == fitToContents) {
            return;
        }
        this.fitToContents = fitToContents;
        if (this.viewRef != null) {
            this.calculateCollapsedOffset();
        }
        this.setStateInternal((this.fitToContents && this.state == 6) ? 3 : this.state);
    }

    public void setPeekHeight(final int peekHeight) {
        boolean layout = false;
        if (peekHeight == -1) {
            if (!this.peekHeightAuto) {
                this.peekHeightAuto = true;
                layout = true;
            }
        } else if (this.peekHeightAuto || this.peekHeight != peekHeight) {
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, peekHeight);
            this.collapsedOffset = this.parentHeight - peekHeight;
            layout = true;
        }
        if (layout && this.state == 4 && this.viewRef != null) {
            final V view = this.viewRef.get();
            if (view != null) {
                view.requestLayout();
            }
        }
    }

    public final int getPeekHeight() {
        return this.peekHeightAuto ? -1 : this.peekHeight;
    }

    public void setHideable(final boolean hideable) {
        this.hideable = hideable;
    }

    public boolean isHideable() {
        return this.hideable;
    }

    public void setSkipCollapsed(final boolean skipCollapsed) {
        this.skipCollapsed = skipCollapsed;
    }

    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    public void setBottomSheetCallback(final BottomSheetCallback callback) {
        this.callback = callback;
    }

    public final void setState(final int state) {
        if (state == this.state) {
            return;
        }
        if (this.viewRef == null) {
            if (state == 4 || state == 3 || state == 6 || (this.hideable && state == 5)) {
                this.state = state;
            }
            return;
        }
        final V child = this.viewRef.get();
        if (child == null) {
            return;
        }
        final ViewParent parent = child.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow((View) child)) {
            child.post((Runnable) new Runnable() {
                @Override
                public void run() {
                    startSettlingAnimation(child, state);
                }
            });
        } else {
            this.startSettlingAnimation(child, state);
        }
    }

    public final int getState() {
        return this.state;
    }

    void setStateInternal(final int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state == 6 || state == 3) {
            this.updateImportantForAccessibility(true);
        } else if (state == 5 || state == 4) {
            this.updateImportantForAccessibility(false);
        }
        final View bottomSheet = this.viewRef.get();
        if (bottomSheet != null && this.callback != null) {
            this.callback.onStateChanged(bottomSheet, state);
        }
    }

    protected void calculateCollapsedOffset() {
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
        }
    }

    private void reset() {
        this.activePointerId = -1;
        if (this.velocityTracker != null) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    boolean shouldHide(final View child, final float yvel) {
        if (this.skipCollapsed) {
            return true;
        }
        if (child.getTop() < this.collapsedOffset) {
            return false;
        }
        final float newTop = child.getTop() + yvel * 0.1f;
        return Math.abs(newTop - this.collapsedOffset) / this.peekHeight > 0.5f;
    }

    public View findScrollingChild(final View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                final View scrollingChild = this.findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }

    private float getYVelocity() {
        if (this.velocityTracker == null) {
            return 0.0f;
        }
        this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    private int getExpandedOffset() {
        return this.fitToContents ? this.fitToContentsOffset : 0;
    }

    void startSettlingAnimation(final View child, int state) {
        int top;
        if (state == 4) {
            top = this.collapsedOffset;
        } else if (state == 6) {
            top = this.halfExpandedOffset;
            if (this.fitToContents && top <= this.fitToContentsOffset) {
                state = 3;
                top = this.fitToContentsOffset;
            }
        } else if (state == 3) {
            top = this.getExpandedOffset();
        } else {
            if (!this.hideable || state != 5) {
                throw new IllegalArgumentException("Illegal state argument: " + state);
            }
            top = this.parentHeight;
        }
        if (this.viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(child, (Runnable) new SettleRunnable(child, state));
        } else {
            this.setStateInternal(state);
        }
    }

    void dispatchOnSlide(final int top) {
        final View bottomSheet = this.viewRef.get();
        if (bottomSheet != null && this.callback != null) {
            if (top > this.collapsedOffset) {
                this.callback.onSlide(bottomSheet, (this.collapsedOffset - top) / (this.parentHeight - this.collapsedOffset));
            } else {
                this.callback.onSlide(bottomSheet, (this.collapsedOffset - top) / (this.collapsedOffset - this.getExpandedOffset()));
            }
        }
    }

    @VisibleForTesting
    int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    public static <V extends View> STBottomSheetBehavior<V> from(final V view) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof STBottomSheetBehavior)) {
            throw new IllegalArgumentException("The view is not associated with STBottomSheetBehavior");
        }
        return (STBottomSheetBehavior<V>) behavior;
    }

    private void updateImportantForAccessibility(final boolean expanded) {
        if (this.viewRef == null) {
            return;
        }
        final ViewParent viewParent = this.viewRef.get().getParent();
        if (!(viewParent instanceof CoordinatorLayout)) {
            return;
        }
        final CoordinatorLayout parent = (CoordinatorLayout) viewParent;
        final int childCount = parent.getChildCount();
        if (Build.VERSION.SDK_INT >= 16 && expanded) {
            if (this.importantForAccessibilityMap != null) {
                return;
            }
            this.importantForAccessibilityMap = new HashMap<View, Integer>(childCount);
        }
        for (int i = 0; i < childCount; ++i) {
            final View child = parent.getChildAt(i);
            if (child != this.viewRef.get()) {
                if (!expanded) {
                    if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(child)) {
                        ViewCompat.setImportantForAccessibility(child, (int) this.importantForAccessibilityMap.get(child));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 16) {
                        this.importantForAccessibilityMap.put(child, child.getImportantForAccessibility());
                    }
                    ViewCompat.setImportantForAccessibility(child, 4);
                }
            }
        }
        if (!expanded) {
            this.importantForAccessibilityMap = null;
        }
    }

    public abstract static class BottomSheetCallback {
        public abstract void onStateChanged(@NonNull final View p0, final int p1);

        public abstract void onSlide(@NonNull final View p0, final float p1);
    }

    private class SettleRunnable implements Runnable {
        private final View view;
        private final int targetState;

        SettleRunnable(final View view, final int targetState) {
            this.view = view;
            this.targetState = targetState;
        }

        @Override
        public void run() {
            if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, (Runnable) this);
            } else {
                setStateInternal(this.targetState);
            }
        }
    }

    protected static class SavedState extends AbsSavedState {
        final int state;
        public static final Parcelable.Creator<SavedState> CREATOR;

        public SavedState(final Parcel source) {
            this(source, null);
        }

        public SavedState(final Parcel source, final ClassLoader loader) {
            super(source, loader);
            this.state = source.readInt();
        }

        public SavedState(final Parcelable superState, final int state) {
            super(superState);
            this.state = state;
        }

        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.state);
        }

        static {
            CREATOR = (Parcelable.Creator) new Parcelable.ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel in, final ClassLoader loader) {
                    return new SavedState(in, loader);
                }

                public SavedState createFromParcel(final Parcel in) {
                    return new SavedState(in, null);
                }

                public SavedState[] newArray(final int size) {
                    return new SavedState[size];
                }
            };
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface State {
    }
}
