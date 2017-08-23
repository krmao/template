package com.xixi.library.android.widget.loading

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.xixi.library.android.R
import com.xixi.library.android.util.FSCustomViewUtil
import java.util.*

/**
 * @example:
 *  <p>
 *      <com.xixi.library.android.widget.loading.FSFrameLoadingLayout
 *          android:id="@+id/loading_view"
 *          android:layout_width="match_parent"
 *          android:layout_height="0dp"
 *          android:layout_weight="1"
 *          app:cxAllBackground="#eeeeee"   //三个view的背景
 *          app:cxCenterInParent="true"     //绝对居中，默认是向上一点点(因为加了titlebar居中看起来其实是向下一点点的)
 *          app:cxEmptyView="@layout/fs_widget_frameloading_empty"      //自定义布局 -> 没有数据
 *          app:cxFailureView="@layout/fs_widget_frameloading_failure"  //自定义布局 -> 网络异常
 *          app:cxLoadingView="@layout/fs_widget_frameloading_loading"  //自定义布局 -> 正在加载
 *          app:cxScaleFactor="1.0"                                     //缩放因子
 *          app:cxUseSmallStyle="false">                                //使用小一点的样式，例如放在 imageView 加载时使用
 *
 *          <WebView
 *               android:id="@+id/web_view"
 *               android:layout_width="match_parent"
 *               android:layout_height="match_parent"/>
 *      </com.xixi.library.android.widget.loading.FSFrameLoadingLayout>
 *  </p>
 *
 * 默认要向上一点点不是居中的,因为titlebar占有一定的高度 宽高必须大于等于 120dp
 */
@Suppress("unused")
class FSFrameLoadingLayout : FrameLayout {

    enum class ViewType {
        NODATA, //没有数据
        LOADING, //加载中
        NETWORK_EXCEPTION//网络异常
    }

    companion object {
        var LAYOUT_ID_LOADING = R.layout.fs_widget_frameloading_loading
        var LAYOUT_ID_NO_DATA = R.layout.fs_widget_frameloading_empty
        var LAYOUT_ID_NET_WORK_EXCEPTION = R.layout.fs_widget_frameloading_failure
    }

    private var mViewMaps = HashMap<ViewType, View>()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FSFrameLoadingLayout)
            if (typedArray != null) {
                val loadingLayoutId = typedArray.getResourceId(R.styleable.FSFrameLoadingLayout_fsLoadingView, LAYOUT_ID_LOADING)
                val networkExceptionLayoutId = typedArray.getResourceId(R.styleable.FSFrameLoadingLayout_fsFailureView, LAYOUT_ID_NET_WORK_EXCEPTION)
                val nodataLayoutId = typedArray.getResourceId(R.styleable.FSFrameLoadingLayout_fsEmptyView, LAYOUT_ID_NO_DATA)
                val centerInParent = FSCustomViewUtil.getBoolean(typedArray, R.styleable.FSFrameLoadingLayout_fsCenterInParent, false)
                val useSmallStyle = FSCustomViewUtil.getBoolean(typedArray, R.styleable.FSFrameLoadingLayout_fsUseSmallStyle, false)
                val defaultScaleFactor = 0.6f
                var scaleFactor = FSCustomViewUtil.getFloat(typedArray, R.styleable.FSFrameLoadingLayout_fsScaleFactor, defaultScaleFactor)
                if (scaleFactor <= 0 || scaleFactor > 1)
                    scaleFactor = defaultScaleFactor
                val drawable = FSCustomViewUtil.getDrawable(typedArray, R.styleable.FSFrameLoadingLayout_fsAllBackground, Color.parseColor("#FFFFFFFE"), -1)
                typedArray.recycle()

                resetWithCustomViews(LayoutInflater.from(context), loadingLayoutId, networkExceptionLayoutId, nodataLayoutId)
                enableCenterInParent(centerInParent)
                enableUseSmallStyle(useSmallStyle, scaleFactor)
                setViewsBackground(drawable)
            }
        }
    }

    //在构造函数最后
    fun resetWithCustomViews(inflater: LayoutInflater, loadingLayoutId: Int, networkExceptionLayoutId: Int, nodataLayoutId: Int) {
        //remove oldViews
        removeView(mViewMaps[ViewType.LOADING])
        removeView(mViewMaps[ViewType.NETWORK_EXCEPTION])
        removeView(mViewMaps[ViewType.NODATA])
        //add newViews
        mViewMaps.put(ViewType.LOADING, inflater.inflate(loadingLayoutId, null))
        mViewMaps.put(ViewType.NETWORK_EXCEPTION, inflater.inflate(networkExceptionLayoutId, null))
        mViewMaps.put(ViewType.NODATA, inflater.inflate(nodataLayoutId, null))
        addView(mViewMaps[ViewType.LOADING], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(mViewMaps[ViewType.NETWORK_EXCEPTION], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(mViewMaps[ViewType.NODATA], FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
        hideAll()
    }

    fun showView(viewType: ViewType) {
        processViewToFrontByType(viewType)
    }

    private fun processViewToFrontByType(viewType: ViewType): View? {
        for ((key, view) in mViewMaps) {
            if (key == viewType) {
                view.visibility = View.VISIBLE
                view.bringToFront()
                return view
            } else {
                view.visibility = View.GONE
            }
        }
        return null
    }

    fun showView(viewType: ViewType, text: String, appendToNewLine: Boolean = false, removeOldAppend: Boolean = true) {
        val view = processViewToFrontByType(viewType)
        if (view != null) {
            try {
                var textView: TextView? = null
                when (viewType) {
                    ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                    ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                    ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun updateText(viewType: ViewType, text: String, appendToNewLine: Boolean, removeOldAppend: Boolean) {
        val view = mViewMaps[viewType]
        if (view != null) {
            try {
                var textView: TextView? = null
                when (viewType) {
                    ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                    ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                    ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun hideView(viewType: ViewType) {
        val view = mViewMaps[viewType]
        if (view != null)
            view.visibility = View.GONE
    }

    fun getDefaultText(viewType: ViewType): String {
        var text: String? = null
        when (viewType) {
            ViewType.NODATA -> text = resources.getString(R.string.fs_frameloading_empty)
            ViewType.LOADING -> text = resources.getString(R.string.fs_frameloading_loadingnow)
            ViewType.NETWORK_EXCEPTION -> text = resources.getString(R.string.fs_frameloading_networkerror)
        }
        return text
    }

    fun getText(viewType: ViewType): String? {
        var text: String? = null
        val view = mViewMaps[viewType]
        if (view != null) {
            var textView: TextView? = null
            when (viewType) {
                ViewType.NODATA -> textView = view.findViewById(R.id.text_empty) as TextView
                ViewType.LOADING -> textView = view.findViewById(R.id.text_loading) as TextView
                ViewType.NETWORK_EXCEPTION -> textView = view.findViewById(R.id.text_failure) as TextView
            }
            text = textView.text.toString().trim { it <= ' ' }
        }
        return text
    }

    //默认要向上一点点不是居中的,因为titlebar占有一定的高度  宽高必须大于等于 120dp
    fun enableCenterInParent(enableCenterLayout: Boolean) {
        try {
            for ((key, view) in mViewMaps) {
                when (key) {
                    ViewType.NODATA -> {
                        view.findViewById(R.id.topEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                    ViewType.LOADING -> {
                        view.findViewById(R.id.topEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                    ViewType.NETWORK_EXCEPTION -> {
                        view.findViewById(R.id.topEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                        view.findViewById(R.id.bottomEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Suppress("DEPRECATION")
    fun setViewsBackground(drawable: Drawable?) {
        for ((_, view) in mViewMaps) {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun enableUseSmallStyle(enableUseSmallStyle: Boolean, scaleFactor: Float) {
        try {
            for ((key, view) in mViewMaps) {
                var imageView: View? = null
                var textView: TextView? = null
                when (key) {
                    ViewType.NODATA -> {
                        textView = view.findViewById(R.id.text_empty) as TextView
                        imageView = view.findViewById(R.id.imageView_empty)
                    }
                    ViewType.LOADING -> {
                        textView = view.findViewById(R.id.text_loading) as TextView
                        imageView = view.findViewById(R.id.imageView_loading)
                    }
                    ViewType.NETWORK_EXCEPTION -> {
                        textView = view.findViewById(R.id.text_failure) as TextView
                        imageView = view.findViewById(R.id.imageView_failure)
                    }
                }

                if (imageView != null) {
                    val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
                    layoutParams.height = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                    layoutParams.width = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                    imageView.layoutParams = layoutParams
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (if (enableUseSmallStyle) 10 else 14).toFloat())
                textView.setTypeface(null, if (enableUseSmallStyle) Typeface.NORMAL else Typeface.BOLD)
                textView.paint.isFakeBoldText = !enableUseSmallStyle
                textView.setPadding(
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPxFromDp(value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).toInt()
    }

    fun hideAll() {
        for ((_, view) in mViewMaps) {
            if (true)
                view.visibility = View.GONE
        }
    }

    fun setOnRefreshClickListener(listener: View.OnClickListener) {
        val refreshView = mViewMaps[ViewType.NETWORK_EXCEPTION]
        refreshView?.setOnClickListener { v -> listener.onClick(v) }
    }

    fun setOnClickListener(viewType: ViewType, listener: View.OnClickListener) {
        for ((key, view) in mViewMaps) {
            if (key == viewType)
                view.setOnClickListener(listener)
        }
    }
}
