package com.smart.library.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STSystemUtil

/*

-keep class com.smart.library.widget.behavior.STBottomSheetBackdropHalfBehavior{*;}

xml->

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/colorAccent"
        android:fitsSystemWindows="false"
        app:layout_behavior="com.smart.library.widget.behavior.STBottomSheetBackdropHalfBehavior" />

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
        val backdropBehavior: STBottomSheetBackdropHalfBehavior<*> = STBottomSheetBackdropHalfBehavior.from(backdropBehaviorViewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        backdropBehaviorViewPager.adapter = STBehaviorBottomSheetBackdropImagesPagerAdapter(this)
        backdropBehaviorViewPager.layoutParams = backdropBehaviorViewPager.layoutParams.apply {
            height = bottomSheetHalfExpandTop
        }
    }

*/
@Keep
@Suppress("KDocUnresolvedReference", "unused")
class STBottomSheetBackdropHalfBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var lastChildY: Float = 0f

    var bottomSheetBehaviorClass: Class<*>? = null
    var bottomSheetContainerLayoutClass: Class<*>? = null

    /**
     * 设置并依赖 bottomSheetBehavior 的滚动
     */
    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (bottomSheetContainerLayoutClass?.isInstance(dependency) == true) {
            val params: CoordinatorLayout.LayoutParams? = dependency.layoutParams as? CoordinatorLayout.LayoutParams
            return params != null && (bottomSheetBehaviorClass?.isInstance(params.behavior) == true)
        }
        return false
    }

    var enableHalfExpandedState = true

    var dependCollapsedOffset: Int = (STSystemUtil.screenHeight() / 0.8f).toInt()
    var dependHalfExpandedOffset: Int = (dependCollapsedOffset * 0.5f).toInt()
    var dependExpandedOffset: Int = (dependCollapsedOffset * 0.2f).toInt()

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
        val onDependentViewChanged: Boolean

        val childMaxShowOffset: Int = child.height // 完整高度
        val childMinShowOffset: Int = 20.toPxFromDp() // 漏出一点的距离
        val childCollapsedOffset = dependCollapsedOffset - childMinShowOffset

        if (enableHalfExpandedState) {
            val childExpandedOffset = dependExpandedOffset - childMinShowOffset
            val childHalfExpandedOffset = dependHalfExpandedOffset - childMaxShowOffset
            if (dependency.y >= dependHalfExpandedOffset) {
                // 在 HalfExpanded - Collapsed 区间
                val dependMoveTotalOffset = dependCollapsedOffset - dependHalfExpandedOffset
                val childMoveTotalOffset = childCollapsedOffset - childHalfExpandedOffset
                child.y = childHalfExpandedOffset + childMoveTotalOffset * (dependency.y - dependHalfExpandedOffset) / dependMoveTotalOffset.toFloat()
                onDependentViewChanged = lastChildY == child.y
                lastChildY = child.y
            } else {
                // 在 HalfExpanded - Expanded 区间
                val dependMoveTotalOffset = dependHalfExpandedOffset - dependExpandedOffset
                val childMoveTotalOffset = childHalfExpandedOffset - childExpandedOffset
                child.y = childHalfExpandedOffset - childMoveTotalOffset * (dependHalfExpandedOffset - dependency.y) / dependMoveTotalOffset.toFloat()
                onDependentViewChanged = lastChildY == child.y
                lastChildY = child.y
            }
        } else {
            // 在 Expanded - Collapsed 区间
            val childExpandedOffset = dependExpandedOffset - childMaxShowOffset
            val dependMoveTotalOffset = dependCollapsedOffset - dependExpandedOffset
            val childMoveTotalOffset = childCollapsedOffset - childExpandedOffset

            child.y = childExpandedOffset + childMoveTotalOffset * (dependency.y - dependExpandedOffset) / dependMoveTotalOffset.toFloat()
            onDependentViewChanged = lastChildY == child.y
            lastChildY = child.y
        }
        return onDependentViewChanged
    }

    companion object {
        @JvmStatic
        fun <V : View> from(view: V): STBottomSheetBackdropHalfBehavior<*> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is STBottomSheetBackdropHalfBehavior) { "The view is not associated with " + "STBottomSheetBackdropBehavior" }
            return behavior
        }
    }
}