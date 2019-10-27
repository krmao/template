package com.smart.template.home.behavior

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * com.smart.template.home.behavior.STBottomSheetBackdropBehavior
 */
class STBottomSheetBackdropBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var lastChildY: Float = 0f

    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var bottomSheetBehaviorClass: Class<*>? = null

    /**
     * 依赖 bottomSheetBehavior 的滚动
     */
    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (bottomSheetBehaviorClass?.isInstance(dependency) == true) {
            try {
                BottomSheetBehavior.from<View>(dependency)
                return true
            } catch (e: IllegalArgumentException) {
            }
        }
        return false
    }

    /**
     * 当 bottomSheetBehavior 滚动时, 处理自己的滚动
     */
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        val collapsedY = dependency.height - (bottomSheetBehavior?.peekHeight ?: 0)

        var currentChildY = (dependency.y - child.height) * collapsedY / (collapsedY - child.height)
        if (currentChildY <= 0) currentChildY = 0f
        child.y = currentChildY

        val onDependentViewChanged: Boolean = lastChildY == currentChildY
        lastChildY = currentChildY

        return onDependentViewChanged
    }

    companion object {
        @JvmStatic
        fun <V : View> from(view: V): STBottomSheetBackdropBehavior<*> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is STBottomSheetBackdropBehavior) { "The view is not associated with " + "STBottomSheetBackdropBehavior" }
            return behavior
        }
    }
}