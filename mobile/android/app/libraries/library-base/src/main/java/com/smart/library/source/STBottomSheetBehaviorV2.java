/*
 * https://github.com/material-components/material-components-android/releases/tag/1.2.1
 */

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smart.library.source;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleableRes;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.smart.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.Suppress;

import static android.os.Build.VERSION.SDK_INT;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;
import static androidx.appcompat.widget.ViewUtils.isLayoutRtl;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * An interaction behavior plugin for a child view of {@link CoordinatorLayout} to make it work as a
 * bottom sheet.
 *
 * <p>To send useful accessibility events, set a title on bottom sheets that are windows or are
 * window-like. For BottomSheetDialog use {@link BottomSheetDialog#setTitle(int)}, and for
 * BottomSheetDialogFragment use {@link ViewCompat#setAccessibilityPaneTitle(View, CharSequence)}.
 */
@SuppressWarnings("ALL")
@SuppressLint({"PrivateResource", "ObsoleteSdkInt", "SwitchIntDef"})
public class STBottomSheetBehaviorV2<V extends View> extends CoordinatorLayout.Behavior<V> {


    /**
     * Callback for monitoring events about bottom sheets.
     */
    public abstract static class BottomSheetCallback {

        /**
         * Called when the bottom sheet changes its state.
         *
         * @param bottomSheet The bottom sheet view.
         * @param newState    The new state. This will be one of {@link #STATE_DRAGGING}, {@link
         *                    #STATE_SETTLING}, {@link #STATE_EXPANDED}, {@link #STATE_COLLAPSED}, {@link
         *                    #STATE_HIDDEN}, or {@link #STATE_HALF_EXPANDED}.
         */
        public abstract void onStateChanged(@NonNull View bottomSheet, @State int newState);

        /**
         * Called when the bottom sheet is being dragged.
         *
         * @param bottomSheet The bottom sheet view.
         * @param slideOffset The new offset of this bottom sheet within [-1,1] range. Offset increases
         *                    as this bottom sheet is moving upward. From 0 to 1 the sheet is between collapsed and
         *                    expanded states and from -1 to 0 it is between hidden and collapsed states.
         */
        public abstract void onSlide(@NonNull View bottomSheet, float slideOffset);
    }

    /**
     * The bottom sheet is dragging.
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * The bottom sheet is settling.
     */
    public static final int STATE_SETTLING = 2;

    /**
     * The bottom sheet is expanded.
     */
    public static final int STATE_EXPANDED = 3;

    /**
     * The bottom sheet is collapsed.
     */
    public static final int STATE_COLLAPSED = 4;

    /**
     * The bottom sheet is hidden.
     */
    public static final int STATE_HIDDEN = 5;

    /**
     * The bottom sheet is half-expanded (used when mFitToContents is false).
     */
    public static final int STATE_HALF_EXPANDED = 6;

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({
            STATE_EXPANDED,
            STATE_COLLAPSED,
            STATE_DRAGGING,
            STATE_SETTLING,
            STATE_HIDDEN,
            STATE_HALF_EXPANDED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    /**
     * Peek at the 16:9 ratio keyline of its parent.
     *
     * <p>This can be used as a parameter for {@link #setPeekHeight(int)}. {@link #getPeekHeight()}
     * will return this when the value is set.
     */
    public static final int PEEK_HEIGHT_AUTO = -1;

    /**
     * This flag will preserve the peekHeight int value on configuration change.
     */
    public static final int SAVE_PEEK_HEIGHT = 0x1;

    /**
     * This flag will preserve the fitToContents boolean value on configuration change.
     */
    public static final int SAVE_FIT_TO_CONTENTS = 1 << 1;

    /**
     * This flag will preserve the hideable boolean value on configuration change.
     */
    public static final int SAVE_HIDEABLE = 1 << 2;

    /**
     * This flag will preserve the skipCollapsed boolean value on configuration change.
     */
    public static final int SAVE_SKIP_COLLAPSED = 1 << 3;

    /**
     * This flag will preserve all aforementioned values on configuration change.
     */
    public static final int SAVE_ALL = -1;

    /**
     * This flag will not preserve the aforementioned values set at runtime if the view is destroyed
     * and recreated. The only value preserved will be the positional state, e.g. collapsed, hidden,
     * expanded, etc. This is the default behavior.
     */
    public static final int SAVE_NONE = 0;

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef(
            flag = true,
            value = {
                    SAVE_PEEK_HEIGHT,
                    SAVE_FIT_TO_CONTENTS,
                    SAVE_HIDEABLE,
                    SAVE_SKIP_COLLAPSED,
                    SAVE_ALL,
                    SAVE_NONE,
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SaveFlast {
    }

    private static final String TAG = "STBottomSheetBehaviorV2";

    @SaveFlast
    private int saveFlast = SAVE_NONE;

    protected static final int SIGNIFICANT_VEL_THRESHOLD = 500;

    private static final float HIDE_THRESHOLD = 0.5f;

    private static final float HIDE_FRICTION = 0.1f;

    private static final int CORNER_ANIMATION_DURATION = 500;

    protected boolean fitToContents = true;

    private boolean updateImportantForAccessibilityOnSiblinst = false;

    private float maximumVelocity;

    /**
     * Peek height set by the user.
     */
    private int peekHeight;

    /**
     * Whether or not to use automatic peek height.
     */
    protected boolean peekHeightAuto;

    /**
     * Minimum peek height permitted.
     */
    protected int peekHeightMin;

    /**
     * Peek height gesture inset buffer to ensure enough swipeable space.
     */
    private int peekHeightGestureInsetBuffer;

    /**
     * True if Behavior has a non-null value for the @shapeAppearance attribute
     */
    protected boolean shapeThemingEnabled;

    protected MaterialShapeDrawable materialShapeDrawable;

    private int gestureInsetBottom;
    private boolean gestureInsetBottomIgnored;

    /**
     * Default Shape Appearance to be used in bottomsheet
     */
    private ShapeAppearanceModel shapeAppearanceModelDefault;

    protected boolean isShapeExpanded;

    protected SettleRunnable settleRunnable = null;

    @Nullable
    private ValueAnimator interpolatorAnimator;

    private static final int DEF_STYLE_RES = R.style.Widget_Design_BottomSheet_Modal;

    protected int expandedOffset;

    protected int fitToContentsOffset;

    protected int halfExpandedOffset;

    float halfExpandedRatio = 0.5f;

    protected int collapsedOffset;

    protected float elevation = -1;

    protected boolean hideable;

    private boolean skipCollapsed;

    protected boolean draggable = true;

    @State
    protected int state = STATE_COLLAPSED;

    @Nullable
    protected ViewDragHelper viewDragHelper;

    private boolean ignoreEvents;

    protected int lastNestedScrollDy;

    protected boolean nestedScrolled;

    protected int childHeight;
    protected int parentWidth;
    protected int parentHeight;

    @Nullable
    protected WeakReference<V> viewRef;

    @Nullable
    protected WeakReference<View> nestedScrollingChildRef;

    @NonNull
    protected final ArrayList<BottomSheetCallback> callbacks = new ArrayList<>();

    @Nullable
    private VelocityTracker velocityTracker;

    protected int activePointerId;

    private int initialY;

    protected boolean touchingScrollingChild;

    @Nullable
    private Map<View, Integer> importantForAccessibilityMap;

    public STBottomSheetBehaviorV2() {
    }

    public STBottomSheetBehaviorV2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // https://github.com/material-components/material-components-android/releases/tag/1.2.1
        // material-components-android-1.2.1/lib/java/com/google/android/material/resources/res/values/dimens.xml
        peekHeightGestureInsetBuffer = context.getResources().getDimensionPixelSize(R.dimen.st_mtrl_min_touch_target_size);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.STBottomSheetBehaviorV2_Layout);
        this.shapeThemingEnabled = a.hasValue(R.styleable.STBottomSheetBehaviorV2_Layout_st_shapeAppearance);
        boolean hasBackgroundTint = a.hasValue(R.styleable.STBottomSheetBehaviorV2_Layout_android_backgroundTint);
        if (hasBackgroundTint) {
            ColorStateList bottomSheetColor = getColorStateList(context, a, R.styleable.STBottomSheetBehaviorV2_Layout_android_backgroundTint);
            createMaterialShapeDrawable(context, attrs, hasBackgroundTint, bottomSheetColor);
        } else {
            createMaterialShapeDrawable(context, attrs, hasBackgroundTint);
        }
        createShapeValueAnimator();

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            this.elevation = a.getDimension(R.styleable.STBottomSheetBehaviorV2_Layout_android_elevation, -1);
        }

        TypedValue value = a.peekValue(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_peekHeight);
        if (value != null && value.data == PEEK_HEIGHT_AUTO) {
            setPeekHeight(value.data);
        } else {
            setPeekHeight(
                    a.getDimensionPixelSize(
                            R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_peekHeight, PEEK_HEIGHT_AUTO));
        }
        setHideable(a.getBoolean(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_hideable, false));
        setGestureInsetBottomIgnored(
                a.getBoolean(R.styleable.STBottomSheetBehaviorV2_Layout_st_gestureInsetBottomIgnored, false));
        setFitToContents(
                a.getBoolean(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_fitToContents, true));
        setSkipCollapsed(
                a.getBoolean(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_skipCollapsed, false));
        setDraggable(a.getBoolean(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_draggable, true));
        setSaveFlast(a.getInt(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_saveFlags, SAVE_NONE));
        setHalfExpandedRatio(
                a.getFloat(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_halfExpandedRatio, 0.5f));

        value = a.peekValue(R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_expandedOffset);
        if (value != null && value.type == TypedValue.TYPE_FIRST_INT) {
            setExpandedOffset(value.data);
        } else {
            setExpandedOffset(
                    a.getDimensionPixelOffset(
                            R.styleable.STBottomSheetBehaviorV2_Layout_st_behavior_expandedOffset, 0));
        }
        a.recycle();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @NonNull
    @Override
    public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout parent, @NonNull V child) {
        return new SavedState(super.onSaveInstanceState(parent, child), this);
    }

    @Override
    public void onRestoreInstanceState(
            @NonNull CoordinatorLayout parent, @NonNull V child, @NonNull Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());
        // Restore Optional State values designated by saveFlast
        restoreOptionalState(ss);
        // Intermediate states are restored as collapsed state
        if (ss.state == STATE_DRAGGING || ss.state == STATE_SETTLING) {
            this.state = STATE_COLLAPSED;
        } else {
            this.state = ss.state;
        }
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull LayoutParams layoutParams) {
        super.onAttachedToLayoutParams(layoutParams);
        // These may already be null, but just be safe, explicitly assign them. This lets us know the
        // first time we layout with this behavior by checking (viewRef == null).
        viewRef = null;
        viewDragHelper = null;
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        // Release references so we don't run unnecessary codepaths while not attached to a view.
        viewRef = null;
        viewDragHelper = null;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.setFitsSystemWindows(true);
        }

        if (viewRef == null) {
            // First layout with this behavior.
            peekHeightMin = parent.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            setSystemGestureInsets(child);
            viewRef = new WeakReference<>(child);
            // Only set MaterialShapeDrawable as background if shapeTheming is enabled, otherwise will
            // default to android:background declared in styles or layout.
            if (shapeThemingEnabled && materialShapeDrawable != null) {
                ViewCompat.setBackground(child, materialShapeDrawable);
            }
            // Set elevation on MaterialShapeDrawable
            if (materialShapeDrawable != null) {
                // Use elevation attr if set on bottomsheet; otherwise, use elevation of child view.
                materialShapeDrawable.setElevation(
                        elevation == -1 ? ViewCompat.getElevation(child) : elevation);
                // Update the material shape based on initial state.
                isShapeExpanded = state == STATE_EXPANDED;
                materialShapeDrawable.setInterpolation(isShapeExpanded ? 0f : 1f);
            }
            updateAccessibilityActions();
            if (ViewCompat.getImportantForAccessibility(child) == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
                ViewCompat.setImportantForAccessibility(child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
            }
        }
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(parent, dragCallback);
        }

        int savedTop = child.getTop();
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);
        // Offset the bottom sheet
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();
        childHeight = child.getHeight();
        fitToContentsOffset = max(0, parentHeight - childHeight);
        calculateHalfExpandedOffset();
        calculateCollapsedOffset();

        if (state == STATE_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, getExpandedOffset());
        } else if (state == STATE_HALF_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, halfExpandedOffset);
        } else if (hideable && state == STATE_HIDDEN) {
            ViewCompat.offsetTopAndBottom(child, parentHeight);
        } else if (state == STATE_COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, collapsedOffset);
        } else if (state == STATE_DRAGGING || state == STATE_SETTLING) {
            ViewCompat.offsetTopAndBottom(child, savedTop - child.getTop());
        }

        nestedScrollingChildRef = new WeakReference<>(findScrollingChild(child));
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(
            @NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!child.isShown() || !draggable) {
            ignoreEvents = true;
            return false;
        }
        int action = event.getActionMasked();
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchingScrollingChild = false;
                activePointerId = MotionEvent.INVALID_POINTER_ID;
                // Reset the ignore flag
                if (ignoreEvents) {
                    ignoreEvents = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                int initialX = (int) event.getX();
                initialY = (int) event.getY();
                // Only intercept nested scrolling events here if the view not being moved by the
                // ViewDragHelper.
                if (state != STATE_SETTLING) {
                    View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
                    if (scroll != null && parent.isPointInChildBounds(scroll, initialX, initialY)) {
                        activePointerId = event.getPointerId(event.getActionIndex());
                        touchingScrollingChild = true;
                    }
                }
                ignoreEvents =
                        activePointerId == MotionEvent.INVALID_POINTER_ID
                                && !parent.isPointInChildBounds(child, initialX, initialY);
                break;
            default: // fall out
        }
        if (!ignoreEvents
                && viewDragHelper != null
                && viewDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        }
        // We have to handle cases that the ViewDragHelper does not capture the bottom sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
        return action == MotionEvent.ACTION_MOVE
                && scroll != null
                && !ignoreEvents
                && state != STATE_DRAGGING
                && !parent.isPointInChildBounds(scroll, (int) event.getX(), (int) event.getY())
                && viewDragHelper != null
                && Math.abs(initialY - event.getY()) > viewDragHelper.getTouchSlop();
    }

    @Override
    public boolean onTouchEvent(
            @NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!child.isShown()) {
            return false;
        }
        int action = event.getActionMasked();
        if (state == STATE_DRAGGING && action == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(event);
        }
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
        // to capture the bottom sheet in case it is not captured and the touch slop is passed.
        if (viewDragHelper != null && action == MotionEvent.ACTION_MOVE && !ignoreEvents) {
            if (Math.abs(initialY - event.getY()) > viewDragHelper.getTouchSlop()) {
                viewDragHelper.captureChildView(child, event.getPointerId(event.getActionIndex()));
            }
        }
        return !ignoreEvents;
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View directTargetChild,
            @NonNull View target,
            int axes,
            int type) {
        lastNestedScrollDy = 0;
        nestedScrolled = false;
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View target,
            int dx,
            int dy,
            @NonNull int[] consumed,
            int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            // Ignore fling here. The ViewDragHelper handles it.
            return;
        }
        View scrollingChild = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
        if (target != scrollingChild) {
            return;
        }
        int currentTop = child.getTop();
        int newTop = currentTop - dy;
        if (dy > 0) { // Upward
            if (newTop < getExpandedOffset()) {
                consumed[1] = currentTop - getExpandedOffset();
                ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                setStateInternal(STATE_EXPANDED);
            } else {
                if (!draggable) {
                    // Prevent dragging
                    return;
                }

                consumed[1] = dy;
                ViewCompat.offsetTopAndBottom(child, -dy);
                setStateInternal(STATE_DRAGGING);
            }
        } else if (dy < 0) { // Downward
            if (!target.canScrollVertically(-1)) {
                if (newTop <= collapsedOffset || hideable) {
                    if (!draggable) {
                        // Prevent dragging
                        return;
                    }

                    consumed[1] = dy;
                    ViewCompat.offsetTopAndBottom(child, -dy);
                    setStateInternal(STATE_DRAGGING);
                } else {
                    consumed[1] = currentTop - collapsedOffset;
                    ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                    setStateInternal(STATE_COLLAPSED);
                }
            }
        }
        dispatchOnSlide(child.getTop());
        lastNestedScrollDy = dy;
        nestedScrolled = true;
    }

    @Override
    public void onStopNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View target,
            int type) {
        if (child.getTop() == getExpandedOffset()) {
            setStateInternal(STATE_EXPANDED);
            return;
        }
        if (nestedScrollingChildRef == null
                || target != nestedScrollingChildRef.get()
                || !nestedScrolled) {
            return;
        }
        int top;
        int targetState;
        if (lastNestedScrollDy > 0) {
            if (fitToContents) {
                top = fitToContentsOffset;
                targetState = STATE_EXPANDED;
            } else {
                int currentTop = child.getTop();
                if (currentTop > halfExpandedOffset) {
                    top = halfExpandedOffset;
                    targetState = STATE_HALF_EXPANDED;
                } else {
                    top = expandedOffset;
                    targetState = STATE_EXPANDED;
                }
            }
        } else if (hideable && shouldHide(child, getYVelocity())) {
            top = parentHeight;
            targetState = STATE_HIDDEN;
        } else if (lastNestedScrollDy == 0) {
            int currentTop = child.getTop();
            if (fitToContents) {
                if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
                    top = fitToContentsOffset;
                    targetState = STATE_EXPANDED;
                } else {
                    top = collapsedOffset;
                    targetState = STATE_COLLAPSED;
                }
            } else {
                if (currentTop < halfExpandedOffset) {
                    if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                        top = expandedOffset;
                        targetState = STATE_EXPANDED;
                    } else {
                        top = halfExpandedOffset;
                        targetState = STATE_HALF_EXPANDED;
                    }
                } else {
                    if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                        top = halfExpandedOffset;
                        targetState = STATE_HALF_EXPANDED;
                    } else {
                        top = collapsedOffset;
                        targetState = STATE_COLLAPSED;
                    }
                }
            }
        } else {
            if (fitToContents) {
                top = collapsedOffset;
                targetState = STATE_COLLAPSED;
            } else {
                // Settle to nearest height.
                int currentTop = child.getTop();
                if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
                    top = halfExpandedOffset;
                    targetState = STATE_HALF_EXPANDED;
                } else {
                    top = collapsedOffset;
                    targetState = STATE_COLLAPSED;
                }
            }
        }
        startSettlingAnimation(child, targetState, top, false);
        nestedScrolled = false;
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed,
            int type,
            @NonNull int[] consumed) {
        // Overridden to prevent the default consumption of the entire scroll distance.
    }

    @Override
    public boolean onNestedPreFling(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View target,
            float velocityX,
            float velocityY) {
        if (nestedScrollingChildRef != null) {
            return target == nestedScrollingChildRef.get()
                    && (state != STATE_EXPANDED
                    || super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY));
        } else {
            return false;
        }
    }

    /**
     * @return whether the height of the expanded sheet is determined by the height of its contents,
     * or if it is expanded in two stages (half the height of the parent container, full height of
     * parent container).
     */
    public boolean isFitToContents() {
        return fitToContents;
    }

    /**
     * Sets whether the height of the expanded sheet is determined by the height of its contents, or
     * if it is expanded in two stages (half the height of the parent container, full height of parent
     * container). Default value is true.
     *
     * @param fitToContents whether or not to fit the expanded sheet to its contents.
     */
    public void setFitToContents(boolean fitToContents) {
        if (this.fitToContents == fitToContents) {
            return;
        }
        this.fitToContents = fitToContents;

        // If sheet is already laid out, recalculate the collapsed offset based on new setting.
        // Otherwise, let onLayoutChild handle this later.
        if (viewRef != null) {
            calculateCollapsedOffset();
        }
        // Fix incorrect expanded settinst depending on whether or not we are fitting sheet to contents.
        setStateInternal((this.fitToContents && state == STATE_HALF_EXPANDED) ? STATE_EXPANDED : state);

        updateAccessibilityActions();
    }

    /**
     * Sets the height of the bottom sheet when it is collapsed.
     *
     * @param peekHeight The height of the collapsed bottom sheet in pixels, or {@link
     *                   #PEEK_HEIGHT_AUTO} to configure the sheet to peek automatically at 16:9 ratio keyline.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_peekHeight
     */
    public void setPeekHeight(int peekHeight) {
        setPeekHeight(peekHeight, false);
    }

    /**
     * Sets the height of the bottom sheet when it is collapsed while optionally animating between the
     * old height and the new height.
     *
     * @param peekHeight The height of the collapsed bottom sheet in pixels, or {@link
     *                   #PEEK_HEIGHT_AUTO} to configure the sheet to peek automatically at 16:9 ratio keyline.
     * @param animate    Whether to animate between the old height and the new height.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_peekHeight
     */
    public final void setPeekHeight(int peekHeight, boolean animate) {
        boolean layout = false;
        if (peekHeight == PEEK_HEIGHT_AUTO) {
            if (!peekHeightAuto) {
                peekHeightAuto = true;
                layout = true;
            }
        } else if (peekHeightAuto || this.peekHeight != peekHeight) {
            peekHeightAuto = false;
            this.peekHeight = max(0, peekHeight);
            layout = true;
        }
        // If sheet is already laid out, recalculate the collapsed offset based on new setting.
        // Otherwise, let onLayoutChild handle this later.
        if (layout) {
            updatePeekHeight(animate);
        }
    }

    private void updatePeekHeight(boolean animate) {
        if (viewRef != null) {
            calculateCollapsedOffset();
            if (state == STATE_COLLAPSED) {
                V view = viewRef.get();
                if (view != null) {
                    if (animate) {
                        settleToStatePendingLayout(state);
                    } else {
                        view.requestLayout();
                    }
                }
            }
        }
    }

    /**
     * Gets the height of the bottom sheet when it is collapsed.
     *
     * @return The height of the collapsed bottom sheet in pixels, or {@link #PEEK_HEIGHT_AUTO} if the
     * sheet is configured to peek automatically at 16:9 ratio keyline
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_peekHeight
     */
    public int getPeekHeight() {
        return peekHeightAuto ? PEEK_HEIGHT_AUTO : peekHeight;
    }

    /**
     * Determines the height of the BottomSheet in the {@link #STATE_HALF_EXPANDED} state. The
     * material guidelines recommended a value of 0.5, which results in the sheet filling half of the
     * parent. The height of the BottomSheet will be smaller as this ratio is decreased and taller as
     * it is increased. The default value is 0.5.
     *
     * @param ratio a float between 0 and 1, representing the {@link #STATE_HALF_EXPANDED} ratio.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_halfExpandedRatio
     */
    public void setHalfExpandedRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio) {

        if ((ratio <= 0) || (ratio >= 1)) {
            throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
        }
        this.halfExpandedRatio = ratio;
        // If sheet is already laid out, recalculate the half expanded offset based on new setting.
        // Otherwise, let onLayoutChild handle this later.
        if (viewRef != null) {
            calculateHalfExpandedOffset();
        }
    }

    /**
     * Gets the ratio for the height of the BottomSheet in the {@link #STATE_HALF_EXPANDED} state.
     *
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_halfExpandedRatio
     */
    @FloatRange(from = 0.0f, to = 1.0f)
    public float getHalfExpandedRatio() {
        return halfExpandedRatio;
    }

    /**
     * Determines the top offset of the BottomSheet in the {@link #STATE_EXPANDED} state when
     * fitsToContent is false. The default value is 0, which results in the sheet matching the
     * parent's top.
     *
     * @param offset an integer value greater than equal to 0, representing the {@link
     *               #STATE_EXPANDED} offset. Value must not exceed the offset in the half expanded state.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_expandedOffset
     */
    public void setExpandedOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to 0");
        }
        this.expandedOffset = offset;
    }

    /**
     * Returns the current expanded offset. If {@code fitToContents} is true, it will automatically
     * pick the offset depending on the height of the content.
     *
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_expandedOffset
     */
    public int getExpandedOffset() {
        return fitToContents ? fitToContentsOffset : expandedOffset;
    }

    /**
     * Sets whether this bottom sheet can hide when it is swiped down.
     *
     * @param hideable {@code true} to make this bottom sheet hideable.
     * @attr ref com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_hideable
     */
    public void setHideable(boolean hideable) {
        if (this.hideable != hideable) {
            this.hideable = hideable;
            if (!hideable && state == STATE_HIDDEN) {
                // Lift up to collapsed state
                setState(STATE_COLLAPSED);
            }
            updateAccessibilityActions();
        }
    }

    /**
     * Gets whether this bottom sheet can hide when it is swiped down.
     *
     * @return {@code true} if this bottom sheet can hide.
     * @attr ref com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_hideable
     */
    public boolean isHideable() {
        return hideable;
    }

    /**
     * Sets whether this bottom sheet should skip the collapsed state when it is being hidden after it
     * is expanded once. Setting this to true has no effect unless the sheet is hideable.
     *
     * @param skipCollapsed True if the bottom sheet should skip the collapsed state.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_skipCollapsed
     */
    public void setSkipCollapsed(boolean skipCollapsed) {
        this.skipCollapsed = skipCollapsed;
    }

    /**
     * Sets whether this bottom sheet should skip the collapsed state when it is being hidden after it
     * is expanded once.
     *
     * @return Whether the bottom sheet should skip the collapsed state.
     * @attr ref
     * com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_skipCollapsed
     */
    public boolean getSkipCollapsed() {
        return skipCollapsed;
    }

    /**
     * Sets whether this bottom sheet is can be collapsed/expanded by dragging. Note: When disabling
     * dragging, an app will require to implement a custom way to expand/collapse the bottom sheet
     *
     * @param draggable {@code false} to prevent dragging the sheet to collapse and expand
     * @attr ref com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Sets save flast to be preserved in bottomsheet on configuration change.
     *
     * @param flast bitwise int of {@link #SAVE_PEEK_HEIGHT}, {@link #SAVE_FIT_TO_CONTENTS}, {@link
     *              #SAVE_HIDEABLE}, {@link #SAVE_SKIP_COLLAPSED}, {@link #SAVE_ALL} and {@link #SAVE_NONE}.
     * @attr ref com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_saveFlast
     * @see #getSaveFlast()
     */
    public void setSaveFlast(@SaveFlast int flast) {
        this.saveFlast = flast;
    }

    /**
     * Returns the save flast.
     *
     * @attr ref com.google.android.material.R.styleable#STBottomSheetBehaviorV2_Layout_behavior_saveFlast
     * @see #setSaveFlast(int)
     */
    @SaveFlast
    public int getSaveFlast() {
        return this.saveFlast;
    }

    /**
     * Sets a callback to be notified of bottom sheet events.
     *
     * @param callback The callback to notify when bottom sheet events occur.
     * @deprecated use {@link #addBottomSheetCallback(BottomSheetCallback)} and {@link
     * #removeBottomSheetCallback(BottomSheetCallback)} instead
     */
    @Deprecated
    public void setBottomSheetCallback(BottomSheetCallback callback) {
        Log.w(
                TAG,
                "STBottomSheetBehaviorV2 now supports multiple callbacks. `setBottomSheetCallback()` removes"
                        + " all existing callbacks, including ones set internally by library authors, which"
                        + " may result in unintended behavior. This may change in the future. Please use"
                        + " `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your"
                        + " own callbacks.");
        callbacks.clear();
        if (callback != null) {
            callbacks.add(callback);
        }
    }

    /**
     * Adds a callback to be notified of bottom sheet events.
     *
     * @param callback The callback to notify when bottom sheet events occur.
     */
    public void addBottomSheetCallback(@NonNull BottomSheetCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    /**
     * Removes a previously added callback.
     *
     * @param callback The callback to remove.
     */
    public void removeBottomSheetCallback(@NonNull BottomSheetCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * Sets the state of the bottom sheet. The bottom sheet will transition to that state with
     * animation.
     *
     * @param state One of {@link #STATE_COLLAPSED}, {@link #STATE_EXPANDED}, {@link #STATE_HIDDEN},
     *              or {@link #STATE_HALF_EXPANDED}.
     */
    public void setState(@State int state) {
        if (state == this.state) {
            return;
        }
        if (viewRef == null) {
            // The view is not laid out yet; modify mState and let onLayoutChild handle it later
            if (state == STATE_COLLAPSED
                    || state == STATE_EXPANDED
                    || state == STATE_HALF_EXPANDED
                    || (hideable && state == STATE_HIDDEN)) {
                this.state = state;
            }
            return;
        }
        settleToStatePendingLayout(state);
    }

    /**
     * Sets whether this bottom sheet should adjust it's position based on the system gesture area on
     * Android Q and above.
     *
     * <p>Note: the bottom sheet will only adjust it's position if it would be unable to be scrolled
     * upwards because the peekHeight is less than the gesture inset margins,(because that would cause
     * a gesture conflict), gesture navigation is enabled, and this {@code ignoreGestureInsetBottom}
     * flag is false.
     */
    public void setGestureInsetBottomIgnored(boolean gestureInsetBottomIgnored) {
        this.gestureInsetBottomIgnored = gestureInsetBottomIgnored;
    }

    /**
     * Returns whether this bottom sheet should adjust it's position based on the system gesture area.
     */
    public boolean isGestureInsetBottomIgnored() {
        return gestureInsetBottomIgnored;
    }

    private void settleToStatePendingLayout(@State int state) {
        final V child = viewRef.get();
        if (child == null) {
            return;
        }
        // Start the animation; wait until a pending layout if there is one.
        ViewParent parent = child.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(child)) {
            final int finalState = state;
            child.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            settleToState(child, finalState);
                        }
                    });
        } else {
            settleToState(child, state);
        }
    }

    /**
     * Gets the current state of the bottom sheet.
     *
     * @return One of {@link #STATE_EXPANDED}, {@link #STATE_HALF_EXPANDED}, {@link #STATE_COLLAPSED},
     * {@link #STATE_DRAGGING}, {@link #STATE_SETTLING}, or {@link #STATE_HALF_EXPANDED}.
     */
    @State
    public int getState() {
        return state;
    }

    protected void setStateInternal(@State int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;

        if (viewRef == null) {
            return;
        }

        View bottomSheet = viewRef.get();
        if (bottomSheet == null) {
            return;
        }

        if (state == STATE_EXPANDED) {
            updateImportantForAccessibility(true);
        } else if (state == STATE_HALF_EXPANDED || state == STATE_HIDDEN || state == STATE_COLLAPSED) {
            updateImportantForAccessibility(false);
        }

        updateDrawableForTargetState(state);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onStateChanged(bottomSheet, state);
        }
        updateAccessibilityActions();
    }

    protected void updateDrawableForTargetState(@State int state) {
        if (state == STATE_SETTLING) {
            // Special case: we want to know which state we're settling to, so wait for another call.
            return;
        }

        boolean expand = state == STATE_EXPANDED;
        if (isShapeExpanded != expand) {
            isShapeExpanded = expand;
            if (materialShapeDrawable != null && interpolatorAnimator != null) {
                if (interpolatorAnimator.isRunning()) {
                    interpolatorAnimator.reverse();
                } else {
                    float to = expand ? 0f : 1f;
                    float from = 1f - to;
                    interpolatorAnimator.setFloatValues(from, to);
                    interpolatorAnimator.start();
                }
            }
        }
    }

    private int calculatePeekHeight() {
        if (peekHeightAuto) {
            int desiredHeight = max(peekHeightMin, parentHeight - parentWidth * 9 / 16);
            return min(desiredHeight, childHeight);
        }
        if (!gestureInsetBottomIgnored && gestureInsetBottom > 0) {
            return max(peekHeight, gestureInsetBottom + peekHeightGestureInsetBuffer);
        }
        return peekHeight;
    }

    protected void calculateCollapsedOffset() {
        int peek = calculatePeekHeight();

        if (fitToContents) {
            collapsedOffset = max(parentHeight - peek, fitToContentsOffset);
        } else {
            collapsedOffset = parentHeight - peek;
        }
    }

    protected void calculateHalfExpandedOffset() {
        this.halfExpandedOffset = (int) (parentHeight * (1 - halfExpandedRatio));
    }

    private void reset() {
        activePointerId = ViewDragHelper.INVALID_POINTER;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void restoreOptionalState(@NonNull SavedState ss) {
        if (this.saveFlast == SAVE_NONE) {
            return;
        }
        if (this.saveFlast == SAVE_ALL || (this.saveFlast & SAVE_PEEK_HEIGHT) == SAVE_PEEK_HEIGHT) {
            this.peekHeight = ss.peekHeight;
        }
        if (this.saveFlast == SAVE_ALL
                || (this.saveFlast & SAVE_FIT_TO_CONTENTS) == SAVE_FIT_TO_CONTENTS) {
            this.fitToContents = ss.fitToContents;
        }
        if (this.saveFlast == SAVE_ALL || (this.saveFlast & SAVE_HIDEABLE) == SAVE_HIDEABLE) {
            this.hideable = ss.hideable;
        }
        if (this.saveFlast == SAVE_ALL
                || (this.saveFlast & SAVE_SKIP_COLLAPSED) == SAVE_SKIP_COLLAPSED) {
            this.skipCollapsed = ss.skipCollapsed;
        }
    }

    protected boolean shouldHide(@NonNull View child, float yvel) {
        if (skipCollapsed) {
            return true;
        }
        if (child.getTop() < collapsedOffset) {
            // It should not hide, but collapse.
            return false;
        }
        int peek = calculatePeekHeight();
        final float newTop = child.getTop() + yvel * HIDE_FRICTION;
        return Math.abs(newTop - collapsedOffset) / (float) peek > HIDE_THRESHOLD;
    }

    @Nullable
    @VisibleForTesting
    protected View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0, count = group.getChildCount(); i < count; i++) {
                View scrollingChild = findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }

    private void createMaterialShapeDrawable(
            @NonNull Context context, AttributeSet attrs, boolean hasBackgroundTint) {
        this.createMaterialShapeDrawable(context, attrs, hasBackgroundTint, null);
    }

    private void createMaterialShapeDrawable(
            @NonNull Context context,
            AttributeSet attrs,
            boolean hasBackgroundTint,
            @Nullable ColorStateList bottomSheetColor) {
        if (this.shapeThemingEnabled) {
            this.shapeAppearanceModelDefault =
                    ShapeAppearanceModel.builder(context, attrs, R.attr.STBottomSheetStyle, DEF_STYLE_RES)
                            .build();

            this.materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModelDefault);
            this.materialShapeDrawable.initializeElevationOverlay(context);

            if (hasBackgroundTint && bottomSheetColor != null) {
                materialShapeDrawable.setFillColor(bottomSheetColor);
            } else {
                // If the tint isn't set, use the theme default background color.
                TypedValue defaultColor = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.colorBackground, defaultColor, true);
                materialShapeDrawable.setTint(defaultColor.data);
            }
        }
    }

    private void createShapeValueAnimator() {
        interpolatorAnimator = ValueAnimator.ofFloat(0f, 1f);
        interpolatorAnimator.setDuration(CORNER_ANIMATION_DURATION);
        interpolatorAnimator.addUpdateListener(
                new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        if (materialShapeDrawable != null) {
                            materialShapeDrawable.setInterpolation(value);
                        }
                    }
                });
    }

    /**
     * Ensure the peek height is at least as large as the bottom gesture inset size so that the sheet
     * can always be dragged, but only when the inset is required by the system.
     */
    protected void setSystemGestureInsets(@NonNull View child) {
        if (VERSION.SDK_INT >= VERSION_CODES.Q && !isGestureInsetBottomIgnored() && !peekHeightAuto) {
            doOnApplyWindowInsets(
                    child,
                    new OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(
                                View view, WindowInsetsCompat insets, RelativePadding initialPadding) {
                            gestureInsetBottom = insets.getMandatorySystemGestureInsets().bottom;
                            updatePeekHeight(/* animate= */ false);
                            return insets;
                        }
                    });
        }
    }

    protected float getYVelocity() {
        if (velocityTracker == null) {
            return 0;
        }
        velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
        return velocityTracker.getYVelocity(activePointerId);
    }

    protected void settleToState(@NonNull View child, int state) {
        int top;
        if (state == STATE_COLLAPSED) {
            top = collapsedOffset;
        } else if (state == STATE_HALF_EXPANDED) {
            top = halfExpandedOffset;
            if (fitToContents && top <= fitToContentsOffset) {
                // Skip to the expanded state if we would scroll past the height of the contents.
                state = STATE_EXPANDED;
                top = fitToContentsOffset;
            }
        } else if (state == STATE_EXPANDED) {
            top = getExpandedOffset();
        } else if (hideable && state == STATE_HIDDEN) {
            top = parentHeight;
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + state);
        }
        startSettlingAnimation(child, state, top, false);
    }

    protected void startSettlingAnimation(View child, int state, int top, boolean settleFromViewDragHelper) {
        boolean startedSettling =
                viewDragHelper != null
                        && (settleFromViewDragHelper
                        ? viewDragHelper.settleCapturedViewAt(child.getLeft(), top)
                        : viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top));
        if (startedSettling) {
            setStateInternal(STATE_SETTLING);
            // STATE_SETTLING won't animate the material shape, so do that here with the target state.
            updateDrawableForTargetState(state);
            if (settleRunnable == null) {
                // If the singleton SettleRunnable instance has not been instantiated, create it.
                settleRunnable = new SettleRunnable(child, state);
            }
            // If the SettleRunnable has not been posted, post it with the correct state.
            if (settleRunnable.isPosted == false) {
                settleRunnable.targetState = state;
                ViewCompat.postOnAnimation(child, settleRunnable);
                settleRunnable.isPosted = true;
            } else {
                // Otherwise, if it has been posted, just update the target state.
                settleRunnable.targetState = state;
            }
        } else {
            setStateInternal(state);
        }
    }

    protected ViewDragHelper.Callback dragCallback =
            new ViewDragHelper.Callback() {

                @Override
                public boolean tryCaptureView(@NonNull View child, int pointerId) {
                    if (state == STATE_DRAGGING) {
                        return false;
                    }
                    if (touchingScrollingChild) {
                        return false;
                    }
                    if (state == STATE_EXPANDED && activePointerId == pointerId) {
                        View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
                        if (scroll != null && scroll.canScrollVertically(-1)) {
                            // Let the content scroll up
                            return false;
                        }
                    }
                    return viewRef != null && viewRef.get() == child;
                }

                @Override
                public void onViewPositionChanged(
                        @NonNull View changedView, int left, int top, int dx, int dy) {
                    dispatchOnSlide(top);
                }

                @Override
                public void onViewDragStateChanged(int state) {
                    if (state == ViewDragHelper.STATE_DRAGGING && draggable) {
                        setStateInternal(STATE_DRAGGING);
                    }
                }

                private boolean releasedLow(@NonNull View child) {
                    // Needs to be at least half way to the bottom.
                    return child.getTop() > (parentHeight + getExpandedOffset()) / 2;
                }

                @Override
                public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                    int top;
                    @State int targetState;
                    if (yvel < 0) { // Moving up
                        if (fitToContents) {
                            top = fitToContentsOffset;
                            targetState = STATE_EXPANDED;
                        } else {
                            int currentTop = releasedChild.getTop();
                            if (currentTop > halfExpandedOffset) {
                                top = halfExpandedOffset;
                                targetState = STATE_HALF_EXPANDED;
                            } else {
                                top = expandedOffset;
                                targetState = STATE_EXPANDED;
                            }
                        }
                    } else if (hideable && shouldHide(releasedChild, yvel)) {
                        // Hide if the view was either released low or it was a significant vertical swipe
                        // otherwise settle to closest expanded state.
                        if ((Math.abs(xvel) < Math.abs(yvel) && yvel > SIGNIFICANT_VEL_THRESHOLD)
                                || releasedLow(releasedChild)) {
                            top = parentHeight;
                            targetState = STATE_HIDDEN;
                        } else if (fitToContents) {
                            top = fitToContentsOffset;
                            targetState = STATE_EXPANDED;
                        } else if (Math.abs(releasedChild.getTop() - expandedOffset)
                                < Math.abs(releasedChild.getTop() - halfExpandedOffset)) {
                            top = expandedOffset;
                            targetState = STATE_EXPANDED;
                        } else {
                            top = halfExpandedOffset;
                            targetState = STATE_HALF_EXPANDED;
                        }
                    } else if (yvel == 0.f || Math.abs(xvel) > Math.abs(yvel)) {
                        // If the Y velocity is 0 or the swipe was mostly horizontal indicated by the X velocity
                        // being greater than the Y velocity, settle to the nearest correct height.
                        int currentTop = releasedChild.getTop();
                        if (fitToContents) {
                            if (Math.abs(currentTop - fitToContentsOffset)
                                    < Math.abs(currentTop - collapsedOffset)) {
                                top = fitToContentsOffset;
                                targetState = STATE_EXPANDED;
                            } else {
                                top = collapsedOffset;
                                targetState = STATE_COLLAPSED;
                            }
                        } else {
                            if (currentTop < halfExpandedOffset) {
                                if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                                    top = expandedOffset;
                                    targetState = STATE_EXPANDED;
                                } else {
                                    top = halfExpandedOffset;
                                    targetState = STATE_HALF_EXPANDED;
                                }
                            } else {
                                if (Math.abs(currentTop - halfExpandedOffset)
                                        < Math.abs(currentTop - collapsedOffset)) {
                                    top = halfExpandedOffset;
                                    targetState = STATE_HALF_EXPANDED;
                                } else {
                                    top = collapsedOffset;
                                    targetState = STATE_COLLAPSED;
                                }
                            }
                        }
                    } else { // Moving Down
                        if (fitToContents) {
                            top = collapsedOffset;
                            targetState = STATE_COLLAPSED;
                        } else {
                            // Settle to the nearest correct height.
                            int currentTop = releasedChild.getTop();
                            if (Math.abs(currentTop - halfExpandedOffset)
                                    < Math.abs(currentTop - collapsedOffset)) {
                                top = halfExpandedOffset;
                                targetState = STATE_HALF_EXPANDED;
                            } else {
                                top = collapsedOffset;
                                targetState = STATE_COLLAPSED;
                            }
                        }
                    }
                    startSettlingAnimation(releasedChild, targetState, top, true);
                }

                @Override
                public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                    return MathUtils.clamp(
                            top, getExpandedOffset(), hideable ? parentHeight : collapsedOffset);
                }

                @Override
                public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                    return child.getLeft();
                }

                @Override
                public int getViewVerticalDragRange(@NonNull View child) {
                    if (hideable) {
                        return parentHeight;
                    } else {
                        return collapsedOffset;
                    }
                }
            };

    protected void dispatchOnSlide(int top) {
        View bottomSheet = viewRef.get();
        if (bottomSheet != null && !callbacks.isEmpty()) {
            float slideOffset =
                    (top > collapsedOffset || collapsedOffset == getExpandedOffset())
                            ? (float) (collapsedOffset - top) / (parentHeight - collapsedOffset)
                            : (float) (collapsedOffset - top) / (collapsedOffset - getExpandedOffset());
            for (int i = 0; i < callbacks.size(); i++) {
                callbacks.get(i).onSlide(bottomSheet, slideOffset);
            }
        }
    }

    @VisibleForTesting
    int getPeekHeightMin() {
        return peekHeightMin;
    }

    /**
     * Disables the shaped corner {@link ShapeAppearanceModel} interpolation transition animations.
     * Will have no effect unless the sheet utilizes a {@link MaterialShapeDrawable} with set shape
     * theming properties. Only For use in UI testing.
     *
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @VisibleForTesting
    public void disableShapeAnimations() {
        // Sets the shape value animator to null, prevents animations from occuring during testing.
        interpolatorAnimator = null;
    }

    protected class SettleRunnable implements Runnable {

        private final View view;

        public boolean isPosted;

        @State
        public int targetState;

        public SettleRunnable(View view, @State int targetState) {
            this.view = view;
            this.targetState = targetState;
        }

        @Override
        public void run() {
            if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(view, this);
            } else {
                setStateInternal(targetState);
            }
            this.isPosted = false;
        }
    }

    /**
     * State persisted across instances
     */
    protected static class SavedState extends AbsSavedState {
        @State
        final int state;
        int peekHeight;
        boolean fitToContents;
        boolean hideable;
        boolean skipCollapsed;

        public SavedState(@NonNull Parcel source) {
            this(source, null);
        }

        public SavedState(@NonNull Parcel source, ClassLoader loader) {
            super(source, loader);
            //noinspection ResourceType
            state = source.readInt();
            peekHeight = source.readInt();
            fitToContents = source.readInt() == 1;
            hideable = source.readInt() == 1;
            skipCollapsed = source.readInt() == 1;
        }

        public SavedState(Parcelable superState, @NonNull STBottomSheetBehaviorV2<?> behavior) {
            super(superState);
            this.state = behavior.state;
            this.peekHeight = behavior.peekHeight;
            this.fitToContents = behavior.fitToContents;
            this.hideable = behavior.hideable;
            this.skipCollapsed = behavior.skipCollapsed;
        }

        /**
         * This constructor does not respect flast: {@link STBottomSheetBehaviorV2#SAVE_PEEK_HEIGHT}, {@link
         * STBottomSheetBehaviorV2#SAVE_FIT_TO_CONTENTS}, {@link STBottomSheetBehaviorV2#SAVE_HIDEABLE}, {@link
         * STBottomSheetBehaviorV2#SAVE_SKIP_COLLAPSED}. It is as if {@link STBottomSheetBehaviorV2#SAVE_NONE}
         * were set.
         *
         * @deprecated Use {@link #SavedState(Parcelable, STBottomSheetBehaviorV2)} instead.
         */
        @Deprecated
        public SavedState(Parcelable superstate, int state) {
            super(superstate);
            this.state = state;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flast) {
            super.writeToParcel(out, flast);
            out.writeInt(state);
            out.writeInt(peekHeight);
            out.writeInt(fitToContents ? 1 : 0);
            out.writeInt(hideable ? 1 : 0);
            out.writeInt(skipCollapsed ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR =
                new ClassLoaderCreator<SavedState>() {
                    @NonNull
                    @Override
                    public SavedState createFromParcel(@NonNull Parcel in, ClassLoader loader) {
                        return new SavedState(in, loader);
                    }

                    @Nullable
                    @Override
                    public SavedState createFromParcel(@NonNull Parcel in) {
                        return new SavedState(in, null);
                    }

                    @NonNull
                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    /**
     * A utility function to get the {@link STBottomSheetBehaviorV2} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link STBottomSheetBehaviorV2}.
     * @return The {@link STBottomSheetBehaviorV2} associated with the {@code view}.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <V extends View> STBottomSheetBehaviorV2<V> from(@NonNull V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior<?> behavior =
                ((LayoutParams) params).getBehavior();
        if (!(behavior instanceof STBottomSheetBehaviorV2)) {
            throw new IllegalArgumentException("The view is not associated with STBottomSheetBehaviorV2");
        }
        return (STBottomSheetBehaviorV2<V>) behavior;
    }

    /**
     * Sets whether the BottomSheet should update the accessibility status of its {@link
     * CoordinatorLayout} siblinst when expanded.
     *
     * <p>Set this to true if the expanded state of the sheet blocks access to siblinst (e.g., when
     * the sheet expands over the full screen).
     */
    public void setUpdateImportantForAccessibilityOnSiblinst(
            boolean updateImportantForAccessibilityOnSiblinst) {
        this.updateImportantForAccessibilityOnSiblinst = updateImportantForAccessibilityOnSiblinst;
    }

    protected void updateImportantForAccessibility(boolean expanded) {
        if (viewRef == null) {
            return;
        }

        ViewParent viewParent = viewRef.get().getParent();
        if (!(viewParent instanceof CoordinatorLayout)) {
            return;
        }

        CoordinatorLayout parent = (CoordinatorLayout) viewParent;
        final int childCount = parent.getChildCount();
        if ((VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) && expanded) {
            if (importantForAccessibilityMap == null) {
                importantForAccessibilityMap = new HashMap<>(childCount);
            } else {
                // The important for accessibility values of the child views have been saved already.
                return;
            }
        }

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == viewRef.get()) {
                continue;
            }

            if (expanded) {
                // Saves the important for accessibility value of the child view.
                if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                    importantForAccessibilityMap.put(child, child.getImportantForAccessibility());
                }
                if (updateImportantForAccessibilityOnSiblinst) {
                    ViewCompat.setImportantForAccessibility(
                            child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
                }
            } else {
                if (updateImportantForAccessibilityOnSiblinst
                        && importantForAccessibilityMap != null
                        && importantForAccessibilityMap.containsKey(child)) {
                    // Restores the original important for accessibility value of the child view.
                    ViewCompat.setImportantForAccessibility(child, importantForAccessibilityMap.get(child));
                }
            }
        }

        if (!expanded) {
            importantForAccessibilityMap = null;
        }
    }

    protected void updateAccessibilityActions() {
        if (viewRef == null) {
            return;
        }
        V child = viewRef.get();
        if (child == null) {
            return;
        }
        ViewCompat.removeAccessibilityAction(child, AccessibilityNodeInfoCompat.ACTION_COLLAPSE);
        ViewCompat.removeAccessibilityAction(child, AccessibilityNodeInfoCompat.ACTION_EXPAND);
        ViewCompat.removeAccessibilityAction(child, AccessibilityNodeInfoCompat.ACTION_DISMISS);

        if (hideable && state != STATE_HIDDEN) {
            addAccessibilityActionForState(child, AccessibilityActionCompat.ACTION_DISMISS, STATE_HIDDEN);
        }

        switch (state) {
            case STATE_EXPANDED: {
                int nextState = fitToContents ? STATE_COLLAPSED : STATE_HALF_EXPANDED;
                addAccessibilityActionForState(
                        child, AccessibilityActionCompat.ACTION_COLLAPSE, nextState);
                break;
            }
            case STATE_HALF_EXPANDED: {
                addAccessibilityActionForState(
                        child, AccessibilityActionCompat.ACTION_COLLAPSE, STATE_COLLAPSED);
                addAccessibilityActionForState(
                        child, AccessibilityActionCompat.ACTION_EXPAND, STATE_EXPANDED);
                break;
            }
            case STATE_COLLAPSED: {
                int nextState = fitToContents ? STATE_EXPANDED : STATE_HALF_EXPANDED;
                addAccessibilityActionForState(child, AccessibilityActionCompat.ACTION_EXPAND, nextState);
                break;
            }
            default: // fall out
        }
    }

    private void addAccessibilityActionForState(
            V child, AccessibilityActionCompat action, final int state) {
        ViewCompat.replaceAccessibilityAction(
                child,
                action,
                null,
                new AccessibilityViewCommand() {
                    @Override
                    public boolean perform(@NonNull View view, @Nullable CommandArguments arguments) {
                        setState(state);
                        return true;
                    }
                });
    }

    //region dependent method

    /**
     * Returns the {@link ColorStateList} from the given {@link TypedArray} attributes. The resource
     * can include themeable attributes, regardless of API level.
     */
    @Nullable
    public static ColorStateList getColorStateList(@NonNull Context context, @NonNull TypedArray attributes, @StyleableRes int index) {
        if (attributes.hasValue(index)) {
            int resourceId = attributes.getResourceId(index, 0);
            if (resourceId != 0) {
                ColorStateList value = AppCompatResources.getColorStateList(context, resourceId);
                if (value != null) {
                    return value;
                }
            }
        }

        // Reading a single color with getColorStateList() on API 15 and below doesn't always correctly
        // read the value. Instead we'll first try to read the color directly here.
        if (VERSION.SDK_INT <= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            int color = attributes.getColor(index, -1);
            if (color != -1) {
                return ColorStateList.valueOf(color);
            }
        }

        return attributes.getColorStateList(index);
    }
    //endregion

    //region dependent class

    /**
     * Simple data object to store the initial padding for a view.
     */
    public static class RelativePadding {
        public int start;
        public int top;
        public int end;
        public int bottom;

        public RelativePadding(int start, int top, int end, int bottom) {
            this.start = start;
            this.top = top;
            this.end = end;
            this.bottom = bottom;
        }

        public RelativePadding(@NonNull RelativePadding other) {
            this.start = other.start;
            this.top = other.top;
            this.end = other.end;
            this.bottom = other.bottom;
        }

        /**
         * Applies this relative padding to the view.
         */
        public void applyToView(View view) {
            ViewCompat.setPaddingRelative(view, start, top, end, bottom);
        }
    }

    /**
     * Listener for applying window insets on a view in a custom way.
     *
     * <p>Apps may choose to implement this interface if they want to apply custom policy
     * to the way that window insets are treated for a view. If an OnApplyWindowInsetsListener
     * is set, its
     * {@link #onApplyWindowInsets(android.view.View, androidx.core.view.WindowInsetsCompat) onApplyWindowInsets}
     * method will be called instead of the View's own {@code onApplyWindowInsets} method.
     * The listener may optionally call the parameter View's <code>onApplyWindowInsets</code>
     * method to apply the View's normal behavior as part of its own.</p>
     */
    public interface OnApplyWindowInsetsListenerV1 {
        /**
         * When {@link ViewCompat#setOnApplyWindowInsetsListener(View, androidx.core.view.OnApplyWindowInsetsListener) set}
         * on a View, this listener method will be called instead of the view's own
         * {@code onApplyWindowInsets} method.
         *
         * @param v      The view applying window insets
         * @param insets The insets to apply
         * @return The insets supplied, minus any insets that were consumed
         */
        WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets);
    }

    /**
     * Wrapper around {@link androidx.core.view.OnApplyWindowInsetsListener} which also passes
     * the initial padding set on the view. Used with {@link #doOnApplyWindowInsets(View,
     * STOnApplyWindowInsetsListener)}.
     */
    public interface OnApplyWindowInsetsListener {

        /**
         * When {@link View#setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener) set} on a
         * View, this listener method will be called instead of the view's own {@link
         * View#onApplyWindowInsets(WindowInsets)} method. The {@code initialPadding} is the view's
         * original padding which can be updated and will be applied to the view automatically. This
         * method should return a new {@link WindowInsetsCompat} with any insets consumed.
         */
        WindowInsetsCompat onApplyWindowInsets(
                View view, WindowInsetsCompat insets, RelativePadding initialPadding);
    }

    /**
     * Wrapper around {@link androidx.core.view.OnApplyWindowInsetsListener} that records the
     * initial padding of the view and requests that insets are applied when attached.
     */
    public static void doOnApplyWindowInsets(
            @NonNull final View view, @NonNull final OnApplyWindowInsetsListener listener) {
        // Create a snapshot of the view's padding state.
        final RelativePadding initialPadding =
                new RelativePadding(
                        ViewCompat.getPaddingStart(view),
                        view.getPaddingTop(),
                        ViewCompat.getPaddingEnd(view),
                        view.getPaddingBottom());
        // Set an actual OnApplyWindowInsetsListener which proxies to the given callback, also passing
        // in the original padding state.
        setOnApplyWindowInsetsListener(
                view,
                new OnApplyWindowInsetsListenerV1() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        return listener.onApplyWindowInsets(view, insets, new RelativePadding(initialPadding));
                    }
                });
        // Request some insets.
        requestApplyInsetsWhenAttached(view);
    }

    /**
     * Set an {@link androidx.core.view.OnApplyWindowInsetsListener} to take over the policy for applying
     * window insets to this view. This will only take effect on devices with API 21 or above.
     */
    public static void setOnApplyWindowInsetsListener(@NonNull View v,
                                                      @Nullable final OnApplyWindowInsetsListenerV1 listener) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (listener == null) {
                v.setOnApplyWindowInsetsListener(null);
                return;
            }

            v.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                    WindowInsetsCompat compatInsets = WindowInsetsCompat
                            .toWindowInsetsCompat(insets);
                    compatInsets = listener.onApplyWindowInsets(view, compatInsets);
                    return compatInsets.toWindowInsets();
                }
            });
        }
    }

    /**
     * Requests that insets should be applied to this view once it is attached.
     */
    public static void requestApplyInsetsWhenAttached(@NonNull View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            // We're already attached, just request as normal.
            ViewCompat.requestApplyInsets(view);
        } else {
            // We're not attached to the hierarchy, add a listener to request when we are.
            view.addOnAttachStateChangeListener(
                    new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(@NonNull View v) {
                            v.removeOnAttachStateChangeListener(this);
                            ViewCompat.requestApplyInsets(v);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                        }
                    });
        }
    }

    /**
     * Wrapper around {@link androidx.core.view.OnApplyWindowInsetsListener} that can
     * automatically apply inset padding based on view attributes.
     */
    public static void doOnApplyWindowInsets(
            @NonNull View view,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes,
            @Nullable final OnApplyWindowInsetsListener listener) {
        TypedArray a =
                view.getContext()
                        .obtainStyledAttributes(attrs, R.styleable.STInsets, defStyleAttr, defStyleRes);

        final boolean paddingBottomSystemWindowInsets =
                a.getBoolean(R.styleable.STInsets_st_paddingBottomSystemWindowInsets, false);
        final boolean paddingLeftSystemWindowInsets =
                a.getBoolean(R.styleable.STInsets_st_paddingLeftSystemWindowInsets, false);
        final boolean paddingRightSystemWindowInsets =
                a.getBoolean(R.styleable.STInsets_st_paddingRightSystemWindowInsets, false);

        a.recycle();

        doOnApplyWindowInsets(
                view,
                new OnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(
                            View view,
                            @NonNull WindowInsetsCompat insets,
                            @NonNull RelativePadding initialPadding) {
                        if (paddingBottomSystemWindowInsets) {
                            initialPadding.bottom += insets.getSystemWindowInsetBottom();
                        }
                        boolean isRtl = isLayoutRtl(view);
                        if (paddingLeftSystemWindowInsets) {
                            if (isRtl) {
                                initialPadding.end += insets.getSystemWindowInsetLeft();
                            } else {
                                initialPadding.start += insets.getSystemWindowInsetLeft();
                            }
                        }
                        if (paddingRightSystemWindowInsets) {
                            if (isRtl) {
                                initialPadding.start += insets.getSystemWindowInsetRight();
                            } else {
                                initialPadding.end += insets.getSystemWindowInsetRight();
                            }
                        }
                        initialPadding.applyToView(view);
                        return listener != null
                                ? listener.onApplyWindowInsets(view, insets, initialPadding)
                                : insets;
                    }
                });
    }

    /**
     * Describes a set of insets for window content.
     *
     * <p>WindowInsetsCompats are immutable and may be expanded to include more inset types in the
     * future. To adjust insets, use one of the supplied clone methods to obtain a new
     * WindowInsetsCompat instance with the adjusted properties.</p>
     */
    public static class WindowInsetsCompat {
        private static final String TAG = "WindowInsetsCompat";

        /**
         * @hide we'll make this public in a future release
         */
        @RestrictTo(LIBRARY_GROUP_PREFIX)
        public static final WindowInsetsCompat CONSUMED = new WindowInsetsCompat.Builder()
                .build()
                .consumeDisplayCutout()
                .consumeStableInsets()
                .consumeSystemWindowInsets();

        private final Impl mImpl;

        @RequiresApi(20)
        private WindowInsetsCompat(@NonNull WindowInsets insets) {
            if (SDK_INT >= 29) {
                mImpl = new Impl29(this, insets);
            } else if (SDK_INT >= 28) {
                mImpl = new Impl28(this, insets);
            } else if (SDK_INT >= 21) {
                mImpl = new Impl21(this, insets);
            } else if (SDK_INT >= 20) {
                mImpl = new Impl20(this, insets);
            } else {
                mImpl = new Impl(this);
            }
        }

        /**
         * Constructs a new WindowInsetsCompat, copying all values from a source WindowInsetsCompat.
         *
         * @param src source from which values are copied
         */
        public WindowInsetsCompat(@Nullable final WindowInsetsCompat src) {
            if (src != null) {
                // We'll copy over from the 'src' instance's impl
                final Impl srcImpl = src.mImpl;
                if (SDK_INT >= 29 && srcImpl instanceof Impl29) {
                    mImpl = new Impl29(this, (Impl29) srcImpl);
                } else if (SDK_INT >= 28 && srcImpl instanceof Impl28) {
                    mImpl = new Impl28(this, (Impl28) srcImpl);
                } else if (SDK_INT >= 21 && srcImpl instanceof Impl21) {
                    mImpl = new Impl21(this, (Impl21) srcImpl);
                } else if (SDK_INT >= 20 && srcImpl instanceof Impl20) {
                    mImpl = new Impl20(this, (Impl20) srcImpl);
                } else {
                    mImpl = new Impl(this);
                }
            } else {
                // Ideally src would be @NonNull, oh well.
                mImpl = new Impl(this);
            }
        }

        /**
         * Wrap an instance of {@link WindowInsets} into a {@link WindowInsetsCompat}.
         *
         * @param insets source insets to wrap
         * @return the wrapped instance
         */
        @NonNull
        @RequiresApi(20)
        public static WindowInsetsCompat toWindowInsetsCompat(@NonNull WindowInsets insets) {
            return new WindowInsetsCompat(Preconditions.checkNotNull(insets));
        }

        /**
         * Returns the left system window inset in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code 0}.
         *
         * @return The left system window inset
         */
        public int getSystemWindowInsetLeft() {
            return getSystemWindowInsets().left;
        }

        /**
         * Returns the top system window inset in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code 0}.
         *
         * @return The top system window inset
         */
        public int getSystemWindowInsetTop() {
            return getSystemWindowInsets().top;
        }

        /**
         * Returns the right system window inset in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code 0}.
         *
         * @return The right system window inset
         */
        public int getSystemWindowInsetRight() {
            return getSystemWindowInsets().right;
        }

        /**
         * Returns the bottom system window inset in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code 0}.
         *
         * @return The bottom system window inset
         */
        public int getSystemWindowInsetBottom() {
            return getSystemWindowInsets().bottom;
        }

        /**
         * Returns true if this WindowInsets has nonzero system window insets.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code false}.
         *
         * @return true if any of the system window inset values are nonzero
         */
        public boolean hasSystemWindowInsets() {
            return !getSystemWindowInsets().equals(Insets.NONE);
        }

        /**
         * Returns true if this WindowInsets has any non-zero insets.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code false}.
         *
         * @return true if any inset values are nonzero
         */
        public boolean hasInsets() {
            return hasSystemWindowInsets()
                    || hasStableInsets()
                    || getDisplayCutout() != null
                    || !getSystemGestureInsets().equals(Insets.NONE)
                    || !getMandatorySystemGestureInsets().equals(Insets.NONE)
                    || !getTappableElementInsets().equals(Insets.NONE);
        }

        /**
         * Check if these insets have been fully consumed.
         *
         * <p>Insets are considered "consumed" if the applicable <code>consume*</code> methods
         * have been called such that all insets have been set to zero. This affects propagation of
         * insets through the view hierarchy; insets that have not been fully consumed will continue
         * to propagate down to child views.</p>
         *
         * <p>The result of this method is equivalent to the return value of
         * {@link android.view.View#fitSystemWindows(android.graphics.Rect)}.</p>
         *
         * @return true if the insets have been fully consumed.
         */
        public boolean isConsumed() {
            return mImpl.isConsumed();
        }

        /**
         * Returns true if the associated window has a round shape.
         *
         * <p>A round window's left, top, right and bottom edges reach all the way to the
         * associated edges of the window but the corners may not be visible. Views responding
         * to round insets should take care to not lay out critical elements within the corners
         * where they may not be accessible.</p>
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code false}.
         *
         * @return true if the window is round
         */
        public boolean isRound() {
            return mImpl.isRound();
        }

        /**
         * Returns a copy of this WindowInsets with the system window insets fully consumed.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code null}.
         *
         * @return A modified copy of this WindowInsets
         */
        @NonNull
        public WindowInsetsCompat consumeSystemWindowInsets() {
            return mImpl.consumeSystemWindowInsets();
        }

        /**
         * Returns a copy of this WindowInsets with selected system window insets replaced
         * with new values.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code null}.
         *
         * @param left   New left inset in pixels
         * @param top    New top inset in pixels
         * @param right  New right inset in pixels
         * @param bottom New bottom inset in pixels
         * @return A modified copy of this WindowInsets
         * @deprecated use {@link WindowInsetsCompat.Builder} with
         * {@link WindowInsetsCompat.Builder#setSystemWindowInsets(Insets)} instead.
         */
        @Deprecated
        @NonNull
        public WindowInsetsCompat replaceSystemWindowInsets(int left, int top, int right, int bottom) {
            return new Builder(this)
                    .setSystemWindowInsets(Insets.of(left, top, right, bottom))
                    .build();
        }

        /**
         * Returns a copy of this WindowInsets with selected system window insets replaced
         * with new values.
         *
         * <p>When running on platforms with API 19 and below, this method always returns {@code null}.
         *
         * @param systemWindowInsets New system window insets. Each field is the inset in pixels
         *                           for that edge
         * @return A modified copy of this WindowInsets
         * @deprecated use {@link WindowInsetsCompat.Builder} with
         * {@link WindowInsetsCompat.Builder#setSystemWindowInsets(Insets)} instead.
         */
        @Deprecated
        @NonNull
        public WindowInsetsCompat replaceSystemWindowInsets(@NonNull Rect systemWindowInsets) {
            return new Builder(this)
                    .setSystemWindowInsets(Insets.of(systemWindowInsets))
                    .build();
        }

        /**
         * Returns the top stable inset in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code 0}.
         *
         * @return The top stable inset
         */
        public int getStableInsetTop() {
            return getStableInsets().top;
        }

        /**
         * Returns the left stable inset in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code 0}.
         *
         * @return The left stable inset
         */
        public int getStableInsetLeft() {
            return getStableInsets().left;
        }

        /**
         * Returns the right stable inset in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code 0}.
         *
         * @return The right stable inset
         */
        public int getStableInsetRight() {
            return getStableInsets().right;
        }

        /**
         * Returns the bottom stable inset in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code 0}.
         *
         * @return The bottom stable inset
         */
        public int getStableInsetBottom() {
            return getStableInsets().bottom;
        }

        /**
         * Returns true if this WindowInsets has nonzero stable insets.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code false}.
         *
         * @return true if any of the stable inset values are nonzero
         */
        public boolean hasStableInsets() {
            return !getStableInsets().equals(Insets.NONE);
        }

        /**
         * Returns a copy of this WindowInsets with the stable insets fully consumed.
         *
         * <p>When running on platforms with API 20 and below, this method always returns {@code null}.
         *
         * @return A modified copy of this WindowInsetsCompat
         */
        @NonNull
        public WindowInsetsCompat consumeStableInsets() {
            return mImpl.consumeStableInsets();
        }

        /**
         * Returns the display cutout if there is one.
         *
         * <p>When running on platforms with API 27 and below, this method always returns {@code null}.
         *
         * @return the display cutout or null if there is none
         * @see DisplayCutoutCompat
         */
        @Nullable
        public DisplayCutoutCompat getDisplayCutout() {
            return mImpl.getDisplayCutout();
        }

        /**
         * Returns a copy of this WindowInsets with the cutout fully consumed.
         *
         * <p>When running on platforms with API 27 and below, this method is a no-op.
         *
         * @return A modified copy of this WindowInsets
         */
        @NonNull
        public WindowInsetsCompat consumeDisplayCutout() {
            return mImpl.consumeDisplayCutout();
        }

        /**
         * Returns the system window insets in pixels.
         *
         * <p>The system window inset represents the area of a full-screen window that is
         * partially or fully obscured by the status bar, navigation bar, IME or other system windows.
         * </p>
         *
         * @return The system window insets
         * @see #getSystemWindowInsetLeft()
         * @see #getSystemWindowInsetTop()
         * @see #getSystemWindowInsetRight()
         * @see #getSystemWindowInsetBottom()
         */
        @NonNull
        public Insets getSystemWindowInsets() {
            return mImpl.getSystemWindowInsets();
        }

        /**
         * Returns the stable insets in pixels.
         *
         * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
         * partially or fully obscured by the system UI elements.  This value does not change
         * based on the visibility state of those elements; for example, if the status bar is
         * normally shown, but temporarily hidden, the stable inset will still provide the inset
         * associated with the status bar being shown.</p>
         *
         * @return The stable insets
         * @see #getStableInsetLeft()
         * @see #getStableInsetTop()
         * @see #getStableInsetRight()
         * @see #getStableInsetBottom()
         */
        @NonNull
        public Insets getStableInsets() {
            return mImpl.getStableInsets();
        }

        /**
         * Returns the mandatory system gesture insets.
         *
         * <p>The mandatory system gesture insets represent the area of a window where mandatory system
         * gestures have priority and may consume some or all touch input, e.g. due to the a system bar
         * occupying it, or it being reserved for touch-only gestures.
         *
         * @see WindowInsets#getMandatorySystemGestureInsets
         */
        @NonNull
        public Insets getMandatorySystemGestureInsets() {
            return mImpl.getMandatorySystemGestureInsets();
        }

        /**
         * Returns the tappable element insets.
         *
         * <p>The tappable element insets represent how much tappable elements <b>must at least</b> be
         * inset to remain both tappable and visually unobstructed by persistent system windows.
         *
         * <p>This may be smaller than {@link #getSystemWindowInsets()} if the system window is
         * largely transparent and lets through simple taps (but not necessarily more complex gestures).
         *
         * @see WindowInsets#getTappableElementInsets
         */
        @NonNull
        public Insets getTappableElementInsets() {
            return mImpl.getTappableElementInsets();
        }

        /**
         * Returns the system gesture insets.
         *
         * <p>The system gesture insets represent the area of a window where system gestures have
         * priority and may consume some or all touch input, e.g. due to the a system bar
         * occupying it, or it being reserved for touch-only gestures.
         *
         * <p>An app can declare priority over system gestures with
         * {@link android.view.View#setSystemGestureExclusionRects} outside of the
         * {@link #getMandatorySystemGestureInsets() mandatory system gesture insets}.
         *
         * @see WindowInsets#getSystemGestureInsets
         */
        @NonNull
        public Insets getSystemGestureInsets() {
            return mImpl.getSystemGestureInsets();
        }

        /**
         * Returns a copy of this instance inset in the given directions.
         * <p>
         * This is intended for dispatching insets to areas of the window that are smaller than the
         * current area.
         *
         * <p>Example:
         * <pre>
         * childView.dispatchApplyWindowInsets(insets.inset(childMargins));
         * </pre>
         *
         * @param insets the amount of insets to remove from all sides.
         * @see #inset(int, int, int, int)
         */
        @NonNull
        public WindowInsetsCompat inset(@NonNull Insets insets) {
            return inset(insets.left, insets.top, insets.right, insets.bottom);
        }

        /**
         * Returns a copy of this instance inset in the given directions.
         * <p>
         * This is intended for dispatching insets to areas of the window that are smaller than the
         * current area.
         *
         * <p>Example:
         * <pre>
         * childView.dispatchApplyWindowInsets(insets.inset(
         *         childMarginLeft, childMarginTop, childMarginBottom, childMarginRight));
         * </pre>
         *
         * @param left   the amount of insets to remove from the left. Must be non-negative.
         * @param top    the amount of insets to remove from the top. Must be non-negative.
         * @param right  the amount of insets to remove from the right. Must be non-negative.
         * @param bottom the amount of insets to remove from the bottom. Must be non-negative.
         * @return the inset insets
         */
        @NonNull
        public WindowInsetsCompat inset(@IntRange(from = 0) int left, @IntRange(from = 0) int top,
                                        @IntRange(from = 0) int right, @IntRange(from = 0) int bottom) {
            return mImpl.inset(left, top, right, bottom);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WindowInsetsCompat)) {
                return false;
            }
            WindowInsetsCompat other = (WindowInsetsCompat) o;
            return ObjectsCompat.equals(mImpl, other.mImpl);
        }

        @Override
        public int hashCode() {
            return mImpl == null ? 0 : mImpl.hashCode();
        }

        /**
         * Return the source {@link WindowInsets} instance used in this {@link WindowInsetsCompat}.
         *
         * @return the wrapped WindowInsets instance
         */
        @Nullable
        @RequiresApi(20)
        public WindowInsets toWindowInsets() {
            return mImpl instanceof Impl20 ? ((Impl20) mImpl).mPlatformInsets : null;
        }

        private static class Impl {
            final WindowInsetsCompat mHost;

            Impl(@NonNull WindowInsetsCompat host) {
                mHost = host;
            }

            boolean isRound() {
                return false;
            }

            boolean isConsumed() {
                return false;
            }

            @NonNull
            WindowInsetsCompat consumeSystemWindowInsets() {
                return mHost;
            }

            @NonNull
            WindowInsetsCompat consumeStableInsets() {
                return mHost;
            }

            @Nullable
            DisplayCutoutCompat getDisplayCutout() {
                return null;
            }

            @NonNull
            WindowInsetsCompat consumeDisplayCutout() {
                return mHost;
            }

            @NonNull
            Insets getSystemWindowInsets() {
                return Insets.NONE;
            }

            @NonNull
            Insets getStableInsets() {
                return Insets.NONE;
            }

            @NonNull
            Insets getSystemGestureInsets() {
                // Pre-Q return the system window insets
                return getSystemWindowInsets();
            }

            @NonNull
            Insets getMandatorySystemGestureInsets() {
                // Pre-Q return the system window insets
                return getSystemWindowInsets();
            }

            @NonNull
            Insets getTappableElementInsets() {
                // Pre-Q return the system window insets
                return getSystemWindowInsets();
            }

            @NonNull
            WindowInsetsCompat inset(int left, int top, int right, int bottom) {
                return CONSUMED;
            }

            @Override
            public boolean equals(Object o) {
                // On API < 28 we can not rely on WindowInsets.equals(), so we handle it manually
                if (this == o) return true;
                if (!(o instanceof Impl)) return false;
                final Impl impl = (Impl) o;
                return isRound() == impl.isRound()
                        && isConsumed() == impl.isConsumed()
                        && ObjectsCompat.equals(getSystemWindowInsets(), impl.getSystemWindowInsets())
                        && ObjectsCompat.equals(getStableInsets(), impl.getStableInsets())
                        && ObjectsCompat.equals(getDisplayCutout(), impl.getDisplayCutout());
            }

            @Override
            public int hashCode() {
                // On API < 28 we can not rely on WindowInsets.hashCode(), so we handle it manually
                return ObjectsCompat.hash(isRound(), isConsumed(), getSystemWindowInsets(),
                        getStableInsets(), getDisplayCutout());
            }
        }

        @RequiresApi(20)
        private static class Impl20 extends Impl {
            @NonNull
            final WindowInsets mPlatformInsets;

            // Used to cache the wrapped value
            private Insets mSystemWindowInsets = null;

            Impl20(@NonNull WindowInsetsCompat host, @NonNull WindowInsets insets) {
                super(host);
                mPlatformInsets = insets;
            }

            Impl20(@NonNull WindowInsetsCompat host, @NonNull Impl20 other) {
                this(host, new WindowInsets(other.mPlatformInsets));
            }

            @Override
            boolean isRound() {
                return mPlatformInsets.isRound();
            }

            @Override
            @NonNull
            final Insets getSystemWindowInsets() {
                if (mSystemWindowInsets == null) {
                    mSystemWindowInsets = Insets.of(
                            mPlatformInsets.getSystemWindowInsetLeft(),
                            mPlatformInsets.getSystemWindowInsetTop(),
                            mPlatformInsets.getSystemWindowInsetRight(),
                            mPlatformInsets.getSystemWindowInsetBottom());
                }
                return mSystemWindowInsets;
            }

            @NonNull
            @Override
            WindowInsetsCompat inset(int left, int top, int right, int bottom) {
                Builder b = new Builder(toWindowInsetsCompat(mPlatformInsets));
                b.setSystemWindowInsets(insetInsets(getSystemWindowInsets(), left, top, right, bottom));
                b.setStableInsets(insetInsets(getStableInsets(), left, top, right, bottom));
                return b.build();
            }
        }

        @RequiresApi(21)
        private static class Impl21 extends Impl20 {
            private Insets mStableInsets = null;

            Impl21(@NonNull WindowInsetsCompat host, @NonNull WindowInsets insets) {
                super(host, insets);
            }

            Impl21(@NonNull WindowInsetsCompat host, @NonNull Impl21 other) {
                super(host, other);
            }

            @Override
            boolean isConsumed() {
                return mPlatformInsets.isConsumed();
            }

            @NonNull
            @Override
            WindowInsetsCompat consumeStableInsets() {
                return toWindowInsetsCompat(mPlatformInsets.consumeStableInsets());
            }

            @NonNull
            @Override
            WindowInsetsCompat consumeSystemWindowInsets() {
                return toWindowInsetsCompat(mPlatformInsets.consumeSystemWindowInsets());
            }

            @Override
            @NonNull
            final Insets getStableInsets() {
                if (mStableInsets == null) {
                    mStableInsets = Insets.of(
                            mPlatformInsets.getStableInsetLeft(),
                            mPlatformInsets.getStableInsetTop(),
                            mPlatformInsets.getStableInsetRight(),
                            mPlatformInsets.getStableInsetBottom());
                }
                return mStableInsets;
            }
        }

        @RequiresApi(28)
        private static class Impl28 extends Impl21 {
            Impl28(@NonNull WindowInsetsCompat host, @NonNull WindowInsets insets) {
                super(host, insets);
            }

            Impl28(@NonNull WindowInsetsCompat host, @NonNull Impl28 other) {
                super(host, other);
            }

            @Nullable
            @Override
            DisplayCutoutCompat getDisplayCutout() {
                return DisplayCutoutCompat.wrap(mPlatformInsets.getDisplayCutout());
            }

            @NonNull
            @Override
            WindowInsetsCompat consumeDisplayCutout() {
                return toWindowInsetsCompat(mPlatformInsets.consumeDisplayCutout());
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Impl28)) return false;
                Impl28 otherImpl28 = (Impl28) o;
                // On API 28+ we can rely on WindowInsets.equals()
                return Objects.equals(mPlatformInsets, otherImpl28.mPlatformInsets);
            }

            @Override
            public int hashCode() {
                return mPlatformInsets.hashCode();
            }
        }

        @RequiresApi(29)
        private static class Impl29 extends Impl28 {
            // Used to cache the wrapped values
            private Insets mSystemGestureInsets = null;
            private Insets mMandatorySystemGestureInsets = null;
            private Insets mTappableElementInsets = null;

            Impl29(@NonNull WindowInsetsCompat host, @NonNull WindowInsets insets) {
                super(host, insets);
            }

            Impl29(@NonNull WindowInsetsCompat host, @NonNull Impl29 other) {
                super(host, other);
            }

            @NonNull
            @Override
            Insets getSystemGestureInsets() {
                if (mSystemGestureInsets == null) {
                    mSystemGestureInsets = Insets.toCompatInsets(mPlatformInsets.getSystemGestureInsets());
                }
                return mSystemGestureInsets;
            }

            @NonNull
            @Override
            Insets getMandatorySystemGestureInsets() {
                if (mMandatorySystemGestureInsets == null) {
                    mMandatorySystemGestureInsets =
                            Insets.toCompatInsets(mPlatformInsets.getMandatorySystemGestureInsets());
                }
                return mMandatorySystemGestureInsets;
            }

            @NonNull
            @Override
            Insets getTappableElementInsets() {
                if (mTappableElementInsets == null) {
                    mTappableElementInsets = Insets.toCompatInsets(mPlatformInsets.getTappableElementInsets());
                }
                return mTappableElementInsets;
            }

            @NonNull
            @Override
            WindowInsetsCompat inset(int left, int top, int right, int bottom) {
                return toWindowInsetsCompat(mPlatformInsets.inset(left, top, right, bottom));
            }
        }

        static Insets insetInsets(Insets insets, int left, int top, int right, int bottom) {
            int newLeft = Math.max(0, insets.left - left);
            int newTop = Math.max(0, insets.top - top);
            int newRight = Math.max(0, insets.right - right);
            int newBottom = Math.max(0, insets.bottom - bottom);
            if (newLeft == left && newTop == top && newRight == right && newBottom == bottom) {
                return insets;
            }
            return Insets.of(newLeft, newTop, newRight, newBottom);
        }

        /**
         * Builder for {@link WindowInsetsCompat}.
         */
        public static final class Builder {
            private final BuilderImpl mImpl;

            /**
             * Creates a builder where all insets are initially consumed.
             */
            public Builder() {
                if (SDK_INT >= 29) {
                    mImpl = new BuilderImpl29();
                } else if (SDK_INT >= 20) {
                    mImpl = new BuilderImpl20();
                } else {
                    mImpl = new BuilderImpl();
                }
            }

            /**
             * Creates a builder where all insets are initialized from {@link WindowInsetsCompat}.
             *
             * @param insets the instance to initialize from.
             */
            public Builder(@NonNull WindowInsetsCompat insets) {
                if (SDK_INT >= 29) {
                    mImpl = new BuilderImpl29(insets);
                } else if (SDK_INT >= 20) {
                    mImpl = new BuilderImpl20(insets);
                } else {
                    mImpl = new BuilderImpl(insets);
                }
            }

            /**
             * Sets system window insets in pixels.
             *
             * <p>The system window inset represents the area of a full-screen window that is
             * partially or fully obscured by the status bar, navigation bar, IME or other system
             * windows.</p>
             *
             * @return itself
             * @see #getSystemWindowInsets()
             */
            @NonNull
            public Builder setSystemWindowInsets(@NonNull Insets insets) {
                mImpl.setSystemWindowInsets(insets);
                return this;
            }

            /**
             * Sets system gesture insets in pixels.
             *
             * <p>The system gesture insets represent the area of a window where system gestures have
             * priority and may consume some or all touch input, e.g. due to the a system bar
             * occupying it, or it being reserved for touch-only gestures.
             *
             * <p>The insets passed will only take effect when running on API 29 and above.
             *
             * @return itself
             * @see #getSystemGestureInsets()
             */
            @NonNull
            public Builder setSystemGestureInsets(@NonNull Insets insets) {
                mImpl.setSystemGestureInsets(insets);
                return this;
            }

            /**
             * Sets mandatory system gesture insets in pixels.
             *
             * <p>The mandatory system gesture insets represent the area of a window where mandatory
             * system gestures have priority and may consume some or all touch input, e.g. due to the a
             * system bar occupying it, or it being reserved for touch-only gestures.
             *
             * <p>In contrast to {@link #setSystemGestureInsets regular system gestures},
             * <b>mandatory</b> system gestures cannot be overridden by
             * {@link ViewCompat#setSystemGestureExclusionRects}.
             *
             * <p>The insets passed will only take effect when running on API 29 and above.
             *
             * @return itself
             * @see #getMandatorySystemGestureInsets()
             */
            @NonNull
            public Builder setMandatorySystemGestureInsets(@NonNull Insets insets) {
                mImpl.setMandatorySystemGestureInsets(insets);
                return this;
            }

            /**
             * Sets tappable element insets in pixels.
             *
             * <p>The tappable element insets represent how much tappable elements <b>must at least</b>
             * be inset to remain both tappable and visually unobstructed by persistent system windows.
             *
             * <p>The insets passed will only take effect when running on API 29 and above.
             *
             * @return itself
             * @see #getTappableElementInsets()
             */
            @NonNull
            public Builder setTappableElementInsets(@NonNull Insets insets) {
                mImpl.setTappableElementInsets(insets);
                return this;
            }

            /**
             * Sets the stable insets in pixels.
             *
             * <p>The stable inset represents the area of a full-screen window that <b>may</b> be
             * partially or fully obscured by the system UI elements.  This value does not change
             * based on the visibility state of those elements; for example, if the status bar is
             * normally shown, but temporarily hidden, the stable inset will still provide the inset
             * associated with the status bar being shown.</p>
             *
             * <p>The insets passed will only take effect when running on API 29 and above.
             *
             * @return itself
             * @see #getStableInsets()
             */
            @NonNull
            public Builder setStableInsets(@NonNull Insets insets) {
                mImpl.setStableInsets(insets);
                return this;
            }

            /**
             * Sets the display cutout.
             *
             * <p>The cutout passed will only take effect when running on API 29 and above.
             *
             * @param displayCutout the display cutout or null if there is none
             * @return itself
             * @see #getDisplayCutout()
             */
            @NonNull
            public Builder setDisplayCutout(@Nullable DisplayCutoutCompat displayCutout) {
                mImpl.setDisplayCutout(displayCutout);
                return this;
            }

            /**
             * Builds a {@link WindowInsetsCompat} instance.
             *
             * @return the {@link WindowInsetsCompat} instance.
             */
            @NonNull
            public WindowInsetsCompat build() {
                return mImpl.build();
            }
        }

        private static class BuilderImpl {
            private final WindowInsetsCompat mInsets;

            BuilderImpl() {
                this(new WindowInsetsCompat((WindowInsetsCompat) null));
            }

            BuilderImpl(@NonNull WindowInsetsCompat insets) {
                mInsets = insets;
            }

            void setSystemWindowInsets(@NonNull Insets insets) {
            }

            void setSystemGestureInsets(@NonNull Insets insets) {
            }

            void setMandatorySystemGestureInsets(@NonNull Insets insets) {
            }

            void setTappableElementInsets(@NonNull Insets insets) {
            }

            void setStableInsets(@NonNull Insets insets) {
            }

            void setDisplayCutout(@Nullable DisplayCutoutCompat displayCutout) {
            }

            @NonNull
            WindowInsetsCompat build() {
                return mInsets;
            }
        }

        @RequiresApi(api = 20)
        private static class BuilderImpl20 extends BuilderImpl {
            private static Field sConsumedField;
            private static boolean sConsumedFieldFetched = false;

            private static Constructor<WindowInsets> sConstructor;
            private static boolean sConstructorFetched = false;

            private WindowInsets mInsets;

            BuilderImpl20() {
                mInsets = createWindowInsetsInstance();
            }

            BuilderImpl20(@NonNull WindowInsetsCompat insets) {
                mInsets = insets.toWindowInsets();
            }

            @Override
            void setSystemWindowInsets(@NonNull Insets insets) {
                if (mInsets != null) {
                    mInsets = mInsets.replaceSystemWindowInsets(insets.left, insets.top, insets.right, insets.bottom);
                }
            }

            @Override
            @NonNull
            WindowInsetsCompat build() {
                return WindowInsetsCompat.toWindowInsetsCompat(mInsets);
            }

            @Nullable
            @SuppressWarnings("JavaReflectionMemberAccess")
            private static WindowInsets createWindowInsetsInstance() {
                // On API 20-28, there is no public way to create an WindowInsets instance, so we
                // need to use reflection.

                // We will first try getting the WindowInsets.CONSUMED static field, and creating a
                // copy of it
                if (!sConsumedFieldFetched) {
                    try {
                        sConsumedField = WindowInsets.class.getDeclaredField("CONSUMED");
                    } catch (ReflectiveOperationException e) {
                        Log.i(TAG, "Could not retrieve WindowInsets.CONSUMED field", e);
                    }
                    sConsumedFieldFetched = true;
                }
                if (sConsumedField != null) {
                    try {
                        WindowInsets consumed = (WindowInsets) sConsumedField.get(null);
                        if (consumed != null) {
                            return new WindowInsets(consumed);
                        }
                    } catch (ReflectiveOperationException e) {
                        Log.i(TAG, "Could not get value from WindowInsets.CONSUMED field", e);
                    }
                }

                // If we reached here, the WindowInsets.CONSUMED field did not exist. We can try
                // the hidden WindowInsets(Rect) constructor instead
                if (!sConstructorFetched) {
                    try {
                        sConstructor = WindowInsets.class.getConstructor(Rect.class);
                    } catch (ReflectiveOperationException e) {
                        Log.i(TAG, "Could not retrieve WindowInsets(Rect) constructor", e);
                    }
                    sConstructorFetched = true;
                }
                if (sConstructor != null) {
                    try {
                        return sConstructor.newInstance(new Rect());
                    } catch (ReflectiveOperationException e) {
                        Log.i(TAG, "Could not invoke WindowInsets(Rect) constructor", e);
                    }
                }

                // If the reflective calls failed, return null
                return null;
            }
        }

        @RequiresApi(api = 29)
        private static class BuilderImpl29 extends BuilderImpl {
            final WindowInsets.Builder mPlatBuilder;

            BuilderImpl29() {
                mPlatBuilder = new WindowInsets.Builder();
            }

            BuilderImpl29(@NonNull WindowInsetsCompat insets) {
                final WindowInsets platInsets = insets.toWindowInsets();
                mPlatBuilder = platInsets != null
                        ? new WindowInsets.Builder(platInsets)
                        : new WindowInsets.Builder();
            }

            @Override
            void setSystemWindowInsets(@NonNull Insets insets) {
                mPlatBuilder.setSystemWindowInsets(insets.toPlatformInsets());
            }

            @Override
            void setSystemGestureInsets(@NonNull Insets insets) {
                mPlatBuilder.setSystemGestureInsets(insets.toPlatformInsets());
            }

            @Override
            void setMandatorySystemGestureInsets(@NonNull Insets insets) {
                mPlatBuilder.setMandatorySystemGestureInsets(insets.toPlatformInsets());
            }

            @Override
            void setTappableElementInsets(@NonNull Insets insets) {
                mPlatBuilder.setTappableElementInsets(insets.toPlatformInsets());
            }

            @Override
            void setStableInsets(@NonNull Insets insets) {
                mPlatBuilder.setStableInsets(insets.toPlatformInsets());
            }

            @Override
            void setDisplayCutout(@Nullable DisplayCutoutCompat displayCutout) {
                mPlatBuilder.setDisplayCutout(displayCutout != null ? displayCutout.unwrap() : null);
            }

            @Override
            @NonNull
            WindowInsetsCompat build() {
                return WindowInsetsCompat.toWindowInsetsCompat(mPlatBuilder.build());
            }
        }
    }

    /**
     * An Insets instance holds four integer offsets which describe changes to the four
     * edges of a Rectangle. By convention, positive values move edges towards the
     * centre of the rectangle.
     * <p>
     * Insets are immutable so may be treated as values.
     */
    public static final class Insets {
        @NonNull
        public static final Insets NONE = new Insets(0, 0, 0, 0);

        public final int left;
        public final int top;
        public final int right;
        public final int bottom;

        private Insets(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        // Factory methods

        /**
         * Return an Insets instance with the appropriate values.
         *
         * @param left   the left inset
         * @param top    the top inset
         * @param right  the right inset
         * @param bottom the bottom inset
         * @return Insets instance with the appropriate values
         */
        @NonNull
        public static Insets of(int left, int top, int right, int bottom) {
            if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                return NONE;
            }
            return new Insets(left, top, right, bottom);
        }

        /**
         * Return an Insets instance with the appropriate values.
         *
         * @param r the rectangle from which to take the values
         * @return an Insets instance with the appropriate values
         */
        @NonNull
        public static Insets of(@NonNull Rect r) {
            return of(r.left, r.top, r.right, r.bottom);
        }

        /**
         * Two Insets instances are equal iff they belong to the same class and their fields are
         * pairwise equal.
         *
         * @param o the object to compare this instance with.
         * @return true iff this object is equal {@code o}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Insets insets = (Insets) o;

            if (bottom != insets.bottom) return false;
            if (left != insets.left) return false;
            if (right != insets.right) return false;
            if (top != insets.top) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = left;
            result = 31 * result + top;
            result = 31 * result + right;
            result = 31 * result + bottom;
            return result;
        }

        @Override
        public String toString() {
            return "Insets{left=" + left + ", top=" + top
                    + ", right=" + right + ", bottom=" + bottom + '}';
        }

        /**
         * @hide
         * @deprecated Use {@link #toCompatInsets(android.graphics.Insets)} instead.
         */
        @RequiresApi(api = 29)
        @NonNull
        @Deprecated
        @RestrictTo(LIBRARY_GROUP_PREFIX)
        public static Insets wrap(@NonNull android.graphics.Insets insets) {
            return toCompatInsets(insets);
        }

        /**
         * Return a copy of the given {@link android.graphics.Insets} instance, converted to be an
         * {@link Insets} instance from AndroidX.
         */
        @RequiresApi(api = 29)
        @NonNull
        public static Insets toCompatInsets(@NonNull android.graphics.Insets insets) {
            return Insets.of(insets.left, insets.top, insets.right, insets.bottom);
        }

        /**
         * Return a copy this instance, converted to be an {@link android.graphics.Insets} instance
         * from the platform.
         */
        @RequiresApi(api = 29)
        @NonNull
        public android.graphics.Insets toPlatformInsets() {
            return android.graphics.Insets.of(left, top, right, bottom);
        }
    }

    /**
     * Represents the area of the display that is not functional for displaying content.
     *
     * <p>{@code DisplayCutoutCompat} instances are immutable.
     */
    public static final class DisplayCutoutCompat {

        private final Object mDisplayCutout;

        /**
         * Creates a DisplayCutout instance.
         *
         * @param safeInsets    the insets from each edge which avoid the display cutout as returned by
         *                      {@link #getSafeInsetTop()} etc.
         * @param boundingRects the bounding rects of the display cutouts as returned by
         *                      {@link #getBoundingRects()} ()}.
         */
        // TODO(b/73953958): @VisibleForTesting(visibility = PRIVATE)
        public DisplayCutoutCompat(Rect safeInsets, List<Rect> boundingRects) {
            this(SDK_INT >= 28 ? new DisplayCutout(safeInsets, boundingRects) : null);
        }

        private DisplayCutoutCompat(Object displayCutout) {
            mDisplayCutout = displayCutout;
        }

        /**
         * Returns the inset from the top which avoids the display cutout in pixels.
         */
        public int getSafeInsetTop() {
            if (SDK_INT >= 28) {
                return ((DisplayCutout) mDisplayCutout).getSafeInsetTop();
            } else {
                return 0;
            }
        }

        /**
         * Returns the inset from the bottom which avoids the display cutout in pixels.
         */
        public int getSafeInsetBottom() {
            if (SDK_INT >= 28) {
                return ((DisplayCutout) mDisplayCutout).getSafeInsetBottom();
            } else {
                return 0;
            }
        }

        /**
         * Returns the inset from the left which avoids the display cutout in pixels.
         */
        public int getSafeInsetLeft() {
            if (SDK_INT >= 28) {
                return ((DisplayCutout) mDisplayCutout).getSafeInsetLeft();
            } else {
                return 0;
            }
        }

        /**
         * Returns the inset from the right which avoids the display cutout in pixels.
         */
        public int getSafeInsetRight() {
            if (SDK_INT >= 28) {
                return ((DisplayCutout) mDisplayCutout).getSafeInsetRight();
            } else {
                return 0;
            }
        }

        /**
         * Returns a list of {@code Rect}s, each of which is the bounding rectangle for a non-functional
         * area on the display.
         * <p>
         * There will be at most one non-functional area per short edge of the device, and none on
         * the long edges.
         *
         * @return a list of bounding {@code Rect}s, one for each display cutout area.
         */
        public List<Rect> getBoundingRects() {
            if (SDK_INT >= 28) {
                return ((DisplayCutout) mDisplayCutout).getBoundingRects();
            } else {
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DisplayCutoutCompat other = (DisplayCutoutCompat) o;
            return mDisplayCutout == null ? other.mDisplayCutout == null
                    : mDisplayCutout.equals(other.mDisplayCutout);
        }

        @Override
        public int hashCode() {
            return mDisplayCutout == null ? 0 : mDisplayCutout.hashCode();
        }

        @Override
        public String toString() {
            return "DisplayCutoutCompat{" + mDisplayCutout + "}";
        }

        static DisplayCutoutCompat wrap(Object displayCutout) {
            return displayCutout == null ? null : new DisplayCutoutCompat(displayCutout);
        }

        @RequiresApi(api = 28)
        DisplayCutout unwrap() {
            return (DisplayCutout) mDisplayCutout;
        }
    }

    //endregion
}
