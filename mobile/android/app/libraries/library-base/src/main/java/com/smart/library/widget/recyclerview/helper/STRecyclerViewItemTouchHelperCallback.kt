package com.smart.library.widget.recyclerview.helper

import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import org.jetbrains.anko.AlertDialogBuilder
import kotlin.math.abs

class STRecyclerViewItemTouchHelperCallback @JvmOverloads constructor(private val mAdapter: STRecyclerViewItemTouchHelperAdapter, private var enableConfirmDialogBeforeSwiped: Boolean = true, private var onDragListener: OnDragListener? = null) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            if (recyclerView.layoutManager is GridLayoutManager) {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                makeMovementFlags(dragFlags, swipeFlags)
            } else {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                makeMovementFlags(dragFlags, swipeFlags)
            }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        if (enableConfirmDialogBeforeSwiped) {
            val alertDialogBuilder = AlertDialogBuilder(viewHolder.itemView.context)
            alertDialogBuilder.title("提示")
            alertDialogBuilder.message("确定删除该数据吗？")
            alertDialogBuilder.positiveButton("确定") {
                // remove this item
                mAdapter.onItemDismiss(viewHolder.adapterPosition)
            }
            alertDialogBuilder.negativeButton("取消") {
                // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                mAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
            alertDialogBuilder.show()
        } else {
            // remove this item
            mAdapter.onItemDismiss(viewHolder.adapterPosition)
        }
    }


    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) =
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                val alpha = 1.0f - abs(dX) / viewHolder.itemView.width.toFloat()
                viewHolder.itemView.alpha = alpha
                viewHolder.itemView.translationX = dX
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder != null)
                onDragListener?.onDragBegin(viewHolder, actionState)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        onDragListener?.onDragEnd(recyclerView, viewHolder)
        viewHolder.itemView.alpha = 1.0f
    }

    interface OnDragListener {

        fun onDragBegin(viewHolder: RecyclerView.ViewHolder, actionState: Int)
        fun onDragEnd(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)

    }

}
