package com.smart.library.widget.recyclerview.helper

import androidx.annotation.Keep

@Keep
interface STRecyclerViewItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
    fun notifyItemChanged(position: Int)
}
