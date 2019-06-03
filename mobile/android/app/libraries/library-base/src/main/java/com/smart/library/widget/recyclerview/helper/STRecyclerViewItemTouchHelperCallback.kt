/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smart.library.widget.recyclerview.helper

import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

@Suppress("unused")
/**
 * An implementation of [ItemTouchHelper.Callback] that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br></br>
 *
 * Expects the `RecyclerView.Adapter` to listen for [STRecyclerViewItemTouchHelperAdapter] callbacks

 * @author Paul Burke (ipaulpro)
 */
class STRecyclerViewItemTouchHelperCallback(private val mAdapter: STRecyclerViewItemTouchHelperAdapter, private var onDragListener: OnDragListener?) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =// Set movement flags based on the layout manager
            if (recyclerView.layoutManager is GridLayoutManager) {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
            } else {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
            }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) =// Notify the adapter of the dismissal
            mAdapter.onItemDismiss(viewHolder.adapterPosition)

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) =
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Fade out the view as it is swiped out of the parent's bounds
                val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
                viewHolder.itemView.alpha = alpha
                viewHolder.itemView.translationX = dX
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder != null)
                onDragListener?.onDragBegin(viewHolder, actionState)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (onDragListener != null)
            onDragListener!!.onDragEnd(recyclerView, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
    }

    interface OnDragListener {
        /**
         * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
         * Implementations should update the item view to indicate it's active state.
         */
        fun onDragBegin(viewHolder: RecyclerView.ViewHolder, actionState: Int)

        /**
         * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
         * state should be cleared.
         */
        fun onDragEnd(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
    }

    companion object {

        const val ALPHA_FULL = 1.0f
    }

}
