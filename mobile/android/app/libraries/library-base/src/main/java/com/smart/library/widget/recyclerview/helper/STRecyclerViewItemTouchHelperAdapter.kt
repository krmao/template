package com.smart.library.widget.recyclerview.helper

interface STRecyclerViewItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)

}
