package com.smart.library.widget.dotsindicator

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil

/**
 * 参考 https://github.com/tommybuonomo/dotsindicator
 */
//@Keep
@Suppress("unused", "PropertyName")
abstract class STBaseDotsIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    @Suppress("PrivatePropertyName", "unused")
    protected val TAG: String = "[ST_DOTS]"

    var pager: Pager? = null
    var dotsClickable: Boolean = true
    var dotsColor: Int = DEFAULT_POINT_COLOR
        set(value) {
            field = value
            refreshDotsColors()
        }

    protected var dotsSize: Float = 16f.toPxFromDp()
    protected var dotsSpacing: Float = 8f.toPxFromDp()
    protected var dotsCornerRadius: Float = dotsSize / 2f
    protected val dotsImageViewList = ArrayList<ImageView>()

    abstract fun refreshDotColor(index: Int)
    abstract fun addDot(index: Int)
    abstract fun removeDot(index: Int)
    abstract fun buildOnPageChangedListener(): OnPageChangeListenerHelper

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshDots()
    }

    private fun refreshDotsCount() {
        if (dotsImageViewList.size < (pager?.count ?: 0)) {
            addDots((pager?.count ?: 0) - dotsImageViewList.size)
        } else if (dotsImageViewList.size > (pager?.count ?: 0)) {
            removeDots(dotsImageViewList.size - (pager?.count ?: 0))
        }
    }

    private fun refreshDotsColors() {
        for (i in dotsImageViewList.indices) {
            refreshDotColor(i)
        }
    }

    protected fun addDots(count: Int) {
        for (i in 0 until count) {
            addDot(i)
        }
    }

    private fun removeDots(count: Int) {
        for (i in 0 until count) {
            removeDot(i)
        }
    }

    protected fun refreshDots() {
        post {
            // Check if we need to refresh the dots count
            refreshDotsCount()
            refreshDotsColors()
            refreshDotsSize()
            refreshOnPageChangedListener()
        }
    }

    private fun refreshOnPageChangedListener() {
        if (pager?.isNotEmpty == true) {
            pager?.removeOnPageChangeListener()
            val onPageChangeListenerHelper = buildOnPageChangedListener()
            pager?.addOnPageChangeListener(onPageChangeListenerHelper)
            onPageChangeListenerHelper.onPageScrolled(pager?.currentItem ?: 0, 0f)
        }
    }

    private fun refreshDotsSize() {
        for (i in 0 until (pager?.currentItem ?: 0)) {
            setDotImageViewWidth(dotsImageViewList[i], dotsSize.toInt())
        }
    }

    @Deprecated("Use setDotsColors() instead")
    fun setPointsColor(color: Int) {
        dotsColor = color
        refreshDotsColors()
    }

    fun setViewPager(viewPager: ViewPager) {
        if (viewPager.adapter == null) {
            STLogUtil.e(TAG, "You have to set an adapter to the view pager before initializing the dots indicator !")
            return
        }

        viewPager.adapter?.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                super.onChanged()
                refreshDots()
            }
        })

        pager = object : Pager {
            var onPageChangeListener: OnPageChangeListener? = null

            override val isNotEmpty: Boolean
                get() = viewPager.isNotEmpty
            override val currentItem: Int
                get() = viewPager.currentItem
            override val isEmpty: Boolean
                get() = viewPager.isEmpty
            override val count: Int
                get() = viewPager.adapter?.count ?: 0

            override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
                viewPager.setCurrentItem(item, smoothScroll)
            }

            override fun removeOnPageChangeListener() {
                onPageChangeListener?.let { viewPager.removeOnPageChangeListener(it) }
            }

            override fun addOnPageChangeListener(onPageChangeListenerHelper: OnPageChangeListenerHelper) {
                onPageChangeListener = object : OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageSelected(position: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        onPageChangeListenerHelper.onPageScrolled(position, positionOffset)
                    }
                }
                onPageChangeListener?.let { viewPager.addOnPageChangeListener(it) }
            }
        }
        refreshDots()
    }

    @Suppress("unused")
    fun setViewPager2(viewPager2: ViewPager2) {
        if (viewPager2.adapter == null) {
            STLogUtil.e(TAG, "You have to set an adapter to the view pager before initializing the dots indicator !")
            return
        }

        viewPager2.adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                refreshDots()
            }
        })

        pager = object : Pager {
            var onPageChangeCallback: OnPageChangeCallback? = null

            override val isNotEmpty: Boolean
                get() = viewPager2.isNotEmpty
            override val currentItem: Int
                get() = viewPager2.currentItem
            override val isEmpty: Boolean
                get() = viewPager2.isEmpty
            override val count: Int
                get() = viewPager2.adapter?.itemCount ?: 0

            override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
                viewPager2.setCurrentItem(item, smoothScroll)
            }

            override fun removeOnPageChangeListener() {
                onPageChangeCallback?.let { viewPager2.unregisterOnPageChangeCallback(it) }
            }

            override fun addOnPageChangeListener(onPageChangeListenerHelper: OnPageChangeListenerHelper) {
                onPageChangeCallback = object : OnPageChangeCallback() {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        onPageChangeListenerHelper.onPageScrolled(position, positionOffset)
                    }
                }
                onPageChangeCallback?.let { viewPager2.registerOnPageChangeCallback(it) }
            }
        }

        refreshDots()
    }

    fun setDotImageViewWidth(dotImageView: View, dotImageViewWidth: Int) {
        STLogUtil.w(TAG, "setDotImageViewWidth dotImageView=$dotImageView, dotImageViewWidth=$dotImageViewWidth")
        dotImageView.layoutParams = dotImageView.layoutParams?.apply { width = dotImageViewWidth }
        requestLayout()
    }

    fun <T> isNextPositionExists(index: Int, list: ArrayList<T>) = index in 0 until list.size

    @SuppressLint("ObsoleteSdkInt")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            rotation = 180f
            requestLayout()
        }
    }

    abstract class OnPageChangeListenerHelper {
        private var lastLeftPosition: Int = -1
        private var lastRightPosition: Int = -1

        internal abstract val pageCount: Int

        fun onPageScrolled(position: Int, positionOffset: Float) {
            var offset = (position + positionOffset)
            val lastPageIndex = (pageCount - 1).toFloat()
            if (offset == lastPageIndex) {
                offset = lastPageIndex - .0001f
            }
            val leftPosition = offset.toInt()
            val rightPosition = leftPosition + 1

            if (rightPosition > lastPageIndex || leftPosition == -1) {
                return
            }

            onPageScrolled(leftPosition, rightPosition, offset % 1)

            if (lastLeftPosition != -1) {
                if (leftPosition > lastLeftPosition) {
                    (lastLeftPosition until leftPosition).forEach {
                        resetPosition(it)
                    }
                }

                if (rightPosition < lastRightPosition) {
                    resetPosition(lastRightPosition)
                    ((rightPosition + 1)..lastRightPosition).forEach {
                        resetPosition(it)
                    }
                }
            }

            lastLeftPosition = leftPosition
            lastRightPosition = rightPosition
        }

        internal abstract fun onPageScrolled(selectedPosition: Int, nextPosition: Int, positionOffset: Float)
        internal abstract fun resetPosition(position: Int)
    }

    interface Pager {
        val isNotEmpty: Boolean
        val currentItem: Int
        val isEmpty: Boolean
        val count: Int
        fun setCurrentItem(item: Int, smoothScroll: Boolean)
        fun removeOnPageChangeListener()
        fun addOnPageChangeListener(onPageChangeListenerHelper: OnPageChangeListenerHelper)
    }

    protected val ViewPager.isNotEmpty: Boolean get() = (adapter?.count ?: 0) > 0
    protected val ViewPager2.isNotEmpty: Boolean get() = (adapter?.itemCount ?: 0) > 0
    protected val ViewPager?.isEmpty: Boolean get() = this != null && this.adapter != null && adapter?.count == 0
    protected val ViewPager2?.isEmpty: Boolean get() = this != null && this.adapter != null && adapter?.itemCount == 0

    companion object {
        const val DEFAULT_POINT_COLOR = Color.CYAN
    }
}