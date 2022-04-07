package com.smart.template.home.test

import android.content.ClipData
import androidx.recyclerview.widget.RecyclerView
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import com.smart.library.util.STLogUtil
import com.smart.template.R

/**
 * 将一个 recyclerView 中的 item 拖拽到 另一个 recyclerView 中
 *
 * @param fromRecyclerView 待拖拽的 RecyclerView
 * @param toRecyclerView   拖拽到的 目标RecyclerView
 * @param onFromRecyclerViewEnding 在 fromRecyclerView 中执行 删除等操作
 * @param onToRecyclerViewDropping 在 toRecyclerView   中执行 插入等操作
 */
@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate", "PrivatePropertyName", "UNUSED_ANONYMOUS_PARAMETER", "unused")
class FinalRecyclerViewTransferManager(val fromRecyclerView: RecyclerView, val toRecyclerView: RecyclerView, val onFromRecyclerViewEnding: (fromRecyclerView: RecyclerView, fromPosition: Int) -> Unit, val onToRecyclerViewDropping: (toRecyclerView: RecyclerView, fromPosition: Int, toPosition: Int) -> Unit) {

    private val TAG = "recyclerView transfer"
    private var backgroundView: View? = null
    private var dataOriginPosition: Int = -1
    private var dataNewPosition: Int = -1 // -1 默认值, -2 目标recyclerView 已添加, 则原始目标recyclerView 需要删除对应项

    init {
        toRecyclerView.setOnDragListener { dragView, dragEvent ->

            // 松手时在 原始 recyclerView 边界之内 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DROP        -> ACTION_DRAG_ENDED
            // 松手时在 原始 recyclerView 边界之外 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DRAG_EXITED -> ACTION_DRAG_ENDED
            when (dragEvent.action) {
                // 第一次长按开始 drag 会触发
                DragEvent.ACTION_DRAG_STARTED -> STLogUtil.w(TAG, "days ACTION_DRAG_STARTED")
                // 进入 原始 recyclerView 边界会触发, 第一次长按开始 drag 也会触发
                DragEvent.ACTION_DRAG_ENTERED -> STLogUtil.w(TAG, "days ACTION_DRAG_ENTERED")
                // 离开 原始 recyclerView 边界会触发
                DragEvent.ACTION_DRAG_EXITED -> {
                    STLogUtil.w(TAG, "days ACTION_DRAG_EXITED")
                    backgroundView?.setBackgroundColor(toRecyclerView.resources.getColor(R.color.orange_700))
                    dataNewPosition = -1
                }

                // 长按后移动会连续触发
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val onTopOfView = toRecyclerView.findChildViewUnder(dragEvent.x, dragEvent.y)
                    onTopOfView?.let {
                        dataNewPosition = toRecyclerView.getChildAdapterPosition(it)
                        backgroundView?.setBackgroundColor(toRecyclerView.resources.getColor(R.color.orange_700))
                        backgroundView = it.findViewById<TextView>(R.id.textViewDays)
                        backgroundView?.setBackgroundColor(toRecyclerView.resources.getColor(R.color.blue_700))
                        Unit
                    }
                    STLogUtil.d(TAG, "days ACTION_DRAG_LOCATION $dataNewPosition")
                }

                // 松手时如果在 原始 recyclerView 边界内会触发, 边界之外不会触发
                DragEvent.ACTION_DROP -> {
                    STLogUtil.w(TAG, "days ACTION_DROP dataNewPosition=$dataNewPosition, dataOriginPosition=$dataOriginPosition")
                    backgroundView?.setBackgroundColor(toRecyclerView.resources.getColor(R.color.orange_700))

                    if (dataOriginPosition != -1 && dataNewPosition != -1) {
                        onToRecyclerViewDropping(toRecyclerView, dataOriginPosition, dataNewPosition)
                        dataNewPosition = -2 // must be remove
                    }
                }

                //松手后最后一个事件, 一定会触发
                DragEvent.ACTION_DRAG_ENDED -> {
                    STLogUtil.w(TAG, "days ACTION_DRAG_ENDED $dataNewPosition")
                    backgroundView = null
                }
                else -> {
                    STLogUtil.w(TAG, "days else ${dragEvent.action}")
                }
            }
            true
        }

        fromRecyclerView.setOnDragListener { dragView, dragEvent ->
            // val destRecyclerView = dragView as RecyclerView
            // STLogUtil.v(TAG, "dayD dragView:${destRecyclerView.tag}")
            val draggingItemView = dragEvent.localState as View
            try {
                dataOriginPosition = fromRecyclerView.getChildAdapterPosition(draggingItemView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 松手时在 原始 recyclerView 边界之内 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DROP        -> ACTION_DRAG_ENDED
            // 松手时在 原始 recyclerView 边界之外 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DRAG_EXITED -> ACTION_DRAG_ENDED
            when (dragEvent.action) {
                // 第一次长按开始 drag 会触发
                DragEvent.ACTION_DRAG_STARTED -> STLogUtil.w(TAG, "dayD ACTION_DRAG_STARTED")
                // 进入 原始 recyclerView 边界会触发, 第一次长按开始 drag 也会触发
                DragEvent.ACTION_DRAG_ENTERED -> STLogUtil.w(TAG, "dayD ACTION_DRAG_ENTERED")
                // 离开 原始 recyclerView 边界会触发
                DragEvent.ACTION_DRAG_EXITED -> STLogUtil.w(TAG, "dayD ACTION_DRAG_EXITED")
                // 长按后移动会连续触发
                DragEvent.ACTION_DRAG_LOCATION -> STLogUtil.d(TAG, "dayD ACTION_DRAG_LOCATION")

                // 松手时如果在 原始 recyclerView 边界内会触发, 边界之外不会触发
                DragEvent.ACTION_DROP -> {
                    dataOriginPosition = -1
                    STLogUtil.w(TAG, "dayD ACTION_DROP dataOriginPosition=$dataOriginPosition")
                }

                //松手后最后一个事件, 一定会触发
                DragEvent.ACTION_DRAG_ENDED -> {
                    STLogUtil.w(TAG, "dayD ACTION_DRAG_ENDED dataNewPosition=$dataNewPosition, dataOriginPosition=$dataOriginPosition")
                    if (dataNewPosition == -2 && dataOriginPosition != -1) {
                        onFromRecyclerViewEnding(fromRecyclerView, dataOriginPosition)
                        dataNewPosition = -1
                    } else {
                        // draggingItemView.visibility = View.VISIBLE
                    }
                    dataOriginPosition = -1
                }
                else -> {
                    STLogUtil.w(TAG, "dayD else ${dragEvent.action}")
                }
            }
            true
        }


    }

    fun startDrag(itemView: View) {
        itemView.startDrag(ClipData.newPlainText("", ""), View.DragShadowBuilder(itemView), itemView, 0)
    }

}