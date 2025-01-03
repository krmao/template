package com.smart.template.home.test.behavior;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * This class will link the Backdrop element (that can be anything extending View) with a
 * NestedScrollView (the dependency). Whenever dependecy is moved, the backdrop will be moved too
 * behaving like parallax effect.
 * <p>
 * The backdrop need to be <b>into</b> a CoordinatorLayout and <b>before</b>
 * {@link FinalBottomSheetBehaviorGoogleMapsLike} in the XML file to get same behavior like Google Maps.
 * It doesn't matter where the backdrop element start in XML, it will be moved following
 * Google Maps's parallax behavior.
 *
 * @param <V> instace of Behavior
 */
public class FinalBackdropBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    /**
     * To avoid using multiple "peekheight=" in XML and looking flexibility allowing {@link FinalBottomSheetBehaviorGoogleMapsLike#mPeekHeight}
     * get changed dynamically we get the {@link NestedScrollView} that has
     * "app:layout_behavior=" {@link FinalBottomSheetBehaviorGoogleMapsLike} inside the {@link CoordinatorLayout}
     */
    private WeakReference<FinalBottomSheetBehaviorGoogleMapsLike> mBottomSheetBehaviorRef;
    /**
     * Following {@link #onDependentViewChanged}'s docs mCurrentChildY just save the child Y
     * position.
     */
    private int mCurrentChildY;

    public FinalBackdropBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof NestedScrollView) {
            try {
                FinalBottomSheetBehaviorGoogleMapsLike.from(dependency);
                return true;
            } catch (IllegalArgumentException e) {
            }
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        /**
         * collapsedY and achorPointY are calculated every time looking for
         * flexibility, in case that dependency's height, child's height or {@link FinalBottomSheetBehaviorGoogleMapsLike#getPeekHeight()}'s
         * value changes throught the time, I mean, you can have a {@link android.widget.ImageView}
         * using images with different sizes and you don't want to resize them or so
         */
        if (mBottomSheetBehaviorRef == null || mBottomSheetBehaviorRef.get() == null)
            getBottomSheetBehavior(parent);
        /**
         * mCollapsedY: Y position in where backdrop get hidden behind dependency.
         * {@link FinalBottomSheetBehaviorGoogleMapsLike#getPeekHeight()} and collapsedY are the same point on screen.
         */
        int collapsedY = dependency.getHeight() - mBottomSheetBehaviorRef.get().getPeekHeight();
        /**
         * achorPointY: with top being Y=0, achorPointY defines the point in Y where could
         * happen 2 things:
         * The backdrop should be moved behind dependency view (when {@link #mCurrentChildY} got
         * positive values) or the dependency view overlaps the backdrop (when
         * {@link #mCurrentChildY} got negative values)
         */
        int achorPointY = child.getHeight();
        /**
         * lastCurrentChildY: Just to know if we need to return true or false at the end of this
         * method.
         */
        int lastCurrentChildY = mCurrentChildY;

        if ((mCurrentChildY = (int) ((dependency.getY() - achorPointY) * collapsedY / (collapsedY - achorPointY))) <= 0)
            child.setY(mCurrentChildY = 0);
        else
            child.setY(mCurrentChildY);
        return (lastCurrentChildY == mCurrentChildY);
    }

    /**
     * Look into the CoordiantorLayout for the {@link FinalBottomSheetBehaviorGoogleMapsLike}
     *
     * @param coordinatorLayout with app:layout_behavior= {@link FinalBottomSheetBehaviorGoogleMapsLike}
     */
    private void getBottomSheetBehavior(@NonNull CoordinatorLayout coordinatorLayout) {

        for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
            View child = coordinatorLayout.getChildAt(i);

            if (child instanceof NestedScrollView) {

                try {
                    FinalBottomSheetBehaviorGoogleMapsLike temp = FinalBottomSheetBehaviorGoogleMapsLike.from(child);
                    mBottomSheetBehaviorRef = new WeakReference<>(temp);
                    break;
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }
}