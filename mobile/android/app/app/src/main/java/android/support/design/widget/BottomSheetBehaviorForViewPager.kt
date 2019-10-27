package android.support.design.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import java.lang.ref.WeakReference

/**
 * -keep class android.support.design.widget.BottomSheetBehaviorForViewPager{*;}
 *
 * Override [.findScrollingChild] to support [ViewPager]'s nested scrolling.
 *
 * By the way, In order to override package level method and field.
 * This class put in the same package path where [BottomSheetBehavior] located.
 */
class BottomSheetBehaviorForViewPager<V : View> @JvmOverloads constructor(context: Context? = null, attrs: AttributeSet? = null) : BottomSheetBehavior<V>(context, attrs) {

    internal override fun findScrollingChild(view: View): View? {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view
        }

        if (view is ViewPager) {
            val currentViewPagerChild = view.getChildAt(view.currentItem)
            val scrollingChild = findScrollingChild(currentViewPagerChild)
            if (scrollingChild != null) {
                return scrollingChild
            }
        } else if (view is ViewGroup) {
            var i = 0
            val count = view.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(view.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    fun updateScrollingChild() {
        (viewRef.get() as? View)?.let {
            val scrollingChild = findScrollingChild(it)
            nestedScrollingChildRef = WeakReference<View>(scrollingChild)
        }
    }

    companion object {

        /**
         * A utility function to get the [BottomSheetBehaviorForViewPager] associated with the `view`.
         *
         * @param view The [View] with [BottomSheetBehaviorForViewPager].
         * @return The [BottomSheetBehaviorForViewPager] associated with the `view`.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): BottomSheetBehaviorForViewPager<V> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is BottomSheetBehaviorForViewPager<*>) { "The view is not associated with BottomSheetBehaviorForViewPager" }
            return behavior as BottomSheetBehaviorForViewPager<V>
        }
    }
}