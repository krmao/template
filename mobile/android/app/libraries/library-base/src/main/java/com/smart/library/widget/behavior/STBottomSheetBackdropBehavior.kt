package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.smart.library.source.STBottomSheetBehavior

/*

-keep class com.smart.library.widget.behavior.STBottomSheetBackdropBehavior{*;}

xml->

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorAccent"
        android:fitsSystemWindows="false"
        app:layout_behavior="com.smart.library.widget.behavior.STBottomSheetBackdropBehavior" />

java/kotlin->

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.final_behavior_bottom_sheet_activity)

        bottomSheetBehavior.enableHalfExpandedState = true
        bottomSheetBehavior.dragEnabled = true
        bottomSheetBehavior.setBottomSheetCallback(onBottomSheetCallback)

        initBackdropBehavior(300.toPxFromDp())
    }

    private fun initBackdropBehavior(bottomSheetHalfExpandTop: Int) {
        val backdropBehavior: STBottomSheetBackdropBehavior<*> = STBottomSheetBackdropBehavior.from(backdropBehaviorViewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetBackdropImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply {
            height = bottomSheetHalfExpandTop
        }
    }

*/
@Suppress("unused", "KDocUnresolvedReference")
@Keep
class STBottomSheetBackdropBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var lastChildY: Float = 0f

    var bottomSheetBehavior: STBottomSheetViewPagerBehavior<*>? = null
    var bottomSheetBehaviorClass: Class<*>? = null

    /**
     * 设置并依赖 bottomSheetBehavior 的滚动
     */
    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (bottomSheetBehaviorClass?.isInstance(dependency) == true) {
            try {
                STBottomSheetBehavior.from(dependency)
                return true
            } catch (e: IllegalArgumentException) {
            }
        }
        return false
    }

    /**
     * 当 bottomSheetBehavior 滚动时, 处理自己的滚动
     * @param parent the parent view of the given child
     * @param child the child view to manipulate
     * @param dependency the dependent view that changed
     * @return true if the Behavior changed the child view's size or position, false otherwise
     */
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