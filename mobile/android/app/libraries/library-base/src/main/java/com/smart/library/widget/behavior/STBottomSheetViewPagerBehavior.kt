package com.smart.library.widget.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 * -keep class com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior{*;}
 *
 * Override [.findScrollingChild] to support [ViewPager]'s nested scrolling.
 *
 * By the way, In order to override package level method and field.
 * This class put in the same package path where [BottomSheetBehavior] located.
 */
class STBottomSheetViewPagerBehavior<V : View> @JvmOverloads constructor(context: Context? = null, attrs: AttributeSet? = null) : STBottomSheetViewDragHelperBehavior<V>(context, attrs) {

    override fun findScrollingChild(view: View): View? {
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

    private val onPageChangeListener by lazy {
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                updateScrollingChild()
            }
        }
    }

    /**
     * 当 viewPager 页面切换时, 更新 nestedScrollingChildRef
     */
    fun bindViewPager(viewPager: ViewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener)
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        this.viewDragHelper = STBottomSheetViewDragHelper.create(parent, this.dragCallback)
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    companion object {

        /**
         * A utility function to get the [STBottomSheetViewPagerBehavior] associated with the `view`.
         *
         * @param view The [View] with [STBottomSheetViewPagerBehavior].
         * @return The [STBottomSheetViewPagerBehavior] associated with the `view`.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): STBottomSheetViewPagerBehavior<V> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is STBottomSheetViewPagerBehavior<*>) { "The view is not associated with BottomSheetViewPagerBehavior" }
            return behavior as STBottomSheetViewPagerBehavior<V>
        }
    }
}