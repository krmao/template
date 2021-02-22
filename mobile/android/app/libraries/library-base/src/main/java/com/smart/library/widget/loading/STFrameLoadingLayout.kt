package com.smart.library.widget.loading

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
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STCustomViewUtil
import java.util.*

/**
 * @example:
 *  <p>
 *      <com.smart.library.widget.loading.STFrameLoadingLayout
 *          android:id="@+id/loading_view"
 *          android:layout_width="match_parent"
 *          android:layout_height="0dp"
 *          android:layout_weight="1"
 *          app:stAllBackground="#eeeeee"   //三个view的背景
 *          app:stCenterInParent="true"     //绝对居中，默认是向上一点点(因为加了titlebar居中看起来其实是向下一点点的)
 *          app:stEmptyView="@layout/st_widget_frameloading_empty"      //自定义布局 -> 没有数据
 *          app:stFailureView="@layout/st_widget_frameloading_failure"  //自定义布局 -> 网络异常
 *          app:stLoadingView="@layout/st_widget_frameloading_loading"  //自定义布局 -> 正在加载
 *          app:stScaleFactor="1.0"                                     //缩放因子
 *          app:stUseSmallStyle="false">                                //使用小一点的样式，例如放在 imageView 加载时使用
 *
 *          <WebView
 *               android:id="@+id/web_view"
 *               android:layout_width="match_parent"
 *               android:layout_height="match_parent"/>
 *      </com.smart.library.widget.loading.STFrameLoadingLayout>
 *  </p>
 *
 * 默认要向上一点点不是居中的,因为titlebar占有一定的高度 宽高必须大于等于 120dp
 */
@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
class STFrameLoadingLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    enum class ViewType {
        NO_DATA, //没有数据
        LOADING, //加载中
        FAILURE//网络异常
    }

    private var viewMaps = HashMap<ViewType, View>()
    private var layoutLoadingID = R.layout.st_widget_frameloading_loading
    private var layoutNoDataID = R.layout.st_widget_frameloading_nodata
    private var layoutFailureID = R.layout.st_widget_frameloading_failure

    //在构造函数最后
    fun resetWithCustomViews(inflater: LayoutInflater, loadingLayoutId: Int, networkExceptionLayoutId: Int, nodataLayoutId: Int) {
        //remove oldViews
        removeView(viewMaps[ViewType.LOADING])
        removeView(viewMaps[ViewType.FAILURE])
        removeView(viewMaps[ViewType.NO_DATA])
        //add newViews
        viewMaps[ViewType.LOADING] = inflater.inflate(loadingLayoutId, null)
        viewMaps[ViewType.FAILURE] = inflater.inflate(networkExceptionLayoutId, null)
        viewMaps[ViewType.NO_DATA] = inflater.inflate(nodataLayoutId, null)
        addView(viewMaps[ViewType.LOADING], LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(viewMaps[ViewType.FAILURE], LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER))
        addView(viewMaps[ViewType.NO_DATA], LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER))
        hideAll()
    }

    fun showView(viewType: ViewType) {
        processViewToFrontByType(viewType)
    }

    private fun processViewToFrontByType(viewType: ViewType): View? {
        for ((key, view) in viewMaps) {
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
                val textView: TextView = when (viewType) {
                    ViewType.NO_DATA -> view.findViewById(R.id.text_empty) as TextView
                    ViewType.LOADING -> view.findViewById(R.id.text_loading) as TextView
                    ViewType.FAILURE -> view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun updateText(viewType: ViewType, text: String, appendToNewLine: Boolean, removeOldAppend: Boolean) {
        val view = viewMaps[viewType]
        if (view != null) {
            try {
                val textView: TextView = when (viewType) {
                    ViewType.NO_DATA -> view.findViewById(R.id.text_empty) as TextView
                    ViewType.LOADING -> view.findViewById(R.id.text_loading) as TextView
                    ViewType.FAILURE -> view.findViewById(R.id.text_failure) as TextView
                }
                textView.text = if (appendToNewLine) (if (removeOldAppend) getDefaultText(viewType) else textView.text).toString() + "\n" + text else text
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun hideView(viewType: ViewType) {
        val view = viewMaps[viewType]
        if (view != null)
            view.visibility = View.GONE
    }

    fun getDefaultText(viewType: ViewType): String {
        return when (viewType) {
            ViewType.NO_DATA -> resources.getString(R.string.st_loading_empty)
            ViewType.LOADING -> resources.getString(R.string.st_loading_now)
            ViewType.FAILURE -> resources.getString(R.string.st_loading_network_error)
        }
    }

    fun getText(viewType: ViewType): String? {
        var text: String? = null
        val view = viewMaps[viewType]
        if (view != null) {
            val textView: TextView = when (viewType) {
                ViewType.NO_DATA -> view.findViewById(R.id.text_empty) as TextView
                ViewType.LOADING -> view.findViewById(R.id.text_loading) as TextView
                ViewType.FAILURE -> view.findViewById(R.id.text_failure) as TextView
            }
            text = textView.text.toString().trim { it <= ' ' }
        }
        return text
    }

    //默认要向上一点点不是居中的,因为titlebar占有一定的高度  宽高必须大于等于 120dp
    fun enableCenterInParent(enableCenterLayout: Boolean) = try {
        for ((key, view) in viewMaps) {
            when (key) {
                ViewType.NO_DATA -> {
                    view.findViewById<View>(R.id.topEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    view.findViewById<View>(R.id.bottomEmptyView_empty).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                }
                ViewType.LOADING -> {
                    view.findViewById<View>(R.id.topEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    view.findViewById<View>(R.id.bottomEmptyView_loading).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                }
                ViewType.FAILURE -> {
                    view.findViewById<View>(R.id.topEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                    view.findViewById<View>(R.id.bottomEmptyView_failure).visibility = if (enableCenterLayout) View.GONE else View.VISIBLE
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    @Suppress("DEPRECATION")
    fun setViewsBackground(drawable: Drawable?) {
        for ((_, view) in viewMaps) {
            view.setBackgroundDrawable(drawable)
        }
    }

    @Deprecated("不适用 pt 适配")
    fun enableUseSmallStyle(enableUseSmallStyle: Boolean, scaleFactor: Float) = try {
        when {
            enableUseSmallStyle -> {
                for ((key, view) in viewMaps) {
                    var imageView: View? = null
                    var textView: TextView? = null
                    when (key) {
                        ViewType.NO_DATA -> {
                            textView = view.findViewById(R.id.text_empty) as? TextView
                            imageView = view.findViewById(R.id.imageView_empty)
                        }
                        ViewType.LOADING -> {
                            textView = view.findViewById(R.id.text_loading) as? TextView
                            imageView = view.findViewById(R.id.imageView_loading)
                        }
                        ViewType.FAILURE -> {
                            textView = view.findViewById(R.id.text_failure) as? TextView
                            imageView = view.findViewById(R.id.imageView_failure)
                        }
                    }

                    if (imageView != null) {
                        val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
                        layoutParams.height = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                        layoutParams.width = getPxFromDp(if (enableUseSmallStyle) 60 * scaleFactor else 60F)
                        imageView.layoutParams = layoutParams
                    }

                    textView?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (if (enableUseSmallStyle) 10 else 14).toFloat())
                    textView?.setTypeface(null, if (enableUseSmallStyle) Typeface.NORMAL else Typeface.BOLD)
                    textView?.paint?.isFakeBoldText = !enableUseSmallStyle
                    textView?.setPadding(
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 10 else 30).toFloat()),
                        getPxFromDp((if (enableUseSmallStyle) 3 else 15).toFloat())
                    )
                }
            }
            else -> {
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun getPxFromDp(value: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).toInt()

    fun hideAll() {
        for ((_, view) in viewMaps) {
            view.visibility = View.GONE
        }
    }

    fun setOnRefreshClickListener(listener: OnClickListener) {
        val refreshView = viewMaps[ViewType.FAILURE]
        refreshView?.setOnClickListener { v -> listener.onClick(v) }
    }

    fun setOnClickListener(viewType: ViewType, listener: OnClickListener) {
        for ((key, view) in viewMaps) {
            if (key == viewType)
                view.setOnClickListener(listener)
        }
    }

    init {
        layoutLoadingID = STInitializer.configLoading?.layoutLoadingID ?: layoutLoadingID
        layoutNoDataID = STInitializer.configLoading?.layoutNoDataID ?: layoutNoDataID
        layoutFailureID = STInitializer.configLoading?.layoutFailureID ?: layoutFailureID

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.STFrameLoadingLayout)
            val loadingLayoutId = typedArray.getResourceId(R.styleable.STFrameLoadingLayout_stLoadingView, layoutLoadingID)
            val networkExceptionLayoutId = typedArray.getResourceId(R.styleable.STFrameLoadingLayout_stFailureView, layoutFailureID)
            val noDataLayoutId = typedArray.getResourceId(R.styleable.STFrameLoadingLayout_stEmptyView, layoutNoDataID)
            val centerInParent = STCustomViewUtil.getBoolean(typedArray, R.styleable.STFrameLoadingLayout_stCenterInParent, false)
            val useSmallStyle = STCustomViewUtil.getBoolean(typedArray, R.styleable.STFrameLoadingLayout_stUseSmallStyle, false)
            val defaultScaleFactor = 0.6f
            var scaleFactor = STCustomViewUtil.getFloat(typedArray, R.styleable.STFrameLoadingLayout_stScaleFactor, defaultScaleFactor)
            if (scaleFactor <= 0 || scaleFactor > 1)
                scaleFactor = defaultScaleFactor
            val drawable = STCustomViewUtil.getDrawable(typedArray, R.styleable.STFrameLoadingLayout_stAllBackground, STInitializer.configLoading?.allBackgroundColor ?: Color.parseColor("#FFFFFFFE"), -1)
            typedArray.recycle()

            resetWithCustomViews(LayoutInflater.from(context), loadingLayoutId, networkExceptionLayoutId, noDataLayoutId)
            enableCenterInParent(centerInParent)
            enableUseSmallStyle(useSmallStyle, scaleFactor)
            setViewsBackground(drawable)
        }
    }

}
