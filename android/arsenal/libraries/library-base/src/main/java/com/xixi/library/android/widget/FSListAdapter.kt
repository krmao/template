package com.xixi.library.android.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class FSListAdapter<ItemEntity, ViewHolder : RecyclerView.ViewHolder> : BaseAdapter {

    protected var TAG = javaClass.simpleName

    protected lateinit var mDataList: List<ItemEntity>
    protected lateinit var mContext: Context

    private constructor() {}

    constructor(context: Context, dataList: List<ItemEntity>) {
        mContext = context
        mDataList = dataList
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    override fun getItem(position: Int): ItemEntity {
        return mDataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    abstract fun onBindViewHolder(holder: ViewHolder, position: Int)

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup): View {
        var convertView = _convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, getItemViewType(position))
            convertView = viewHolder.itemView
            convertView!!.tag = viewHolder
        } else {
            @Suppress("UNCHECKED_CAST")
            viewHolder = convertView.tag as ViewHolder
        }
        onBindViewHolder(viewHolder, position)
        return convertView
    }
}