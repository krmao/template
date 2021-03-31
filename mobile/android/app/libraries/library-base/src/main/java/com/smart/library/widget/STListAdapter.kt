package com.smart.library.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.Keep

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate", "PropertyName")
//@Keep
abstract class STListAdapter<ItemEntity, ViewHolder : RecyclerView.ViewHolder> : BaseAdapter {
    protected var TAG: String? = STListAdapter::class.java.simpleName
    protected var dataList: List<ItemEntity> = arrayListOf()
    protected var context: Context? = null

    private constructor()

    constructor(context: Context?, dataList: List<ItemEntity>) {
        this.context = context
        this.dataList = dataList
    }

    override fun getCount(): Int = dataList.size

    override fun getItem(position: Int): ItemEntity = dataList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    abstract fun onBindViewHolder(holder: ViewHolder, position: Int)

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup): View? {
        var convertView = _convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, getItemViewType(position))
            convertView = viewHolder.itemView
            convertView.tag = viewHolder
        } else {
            @Suppress("UNCHECKED_CAST")
            viewHolder = convertView.tag as ViewHolder
        }
        onBindViewHolder(viewHolder, position)
        return convertView
    }
}
