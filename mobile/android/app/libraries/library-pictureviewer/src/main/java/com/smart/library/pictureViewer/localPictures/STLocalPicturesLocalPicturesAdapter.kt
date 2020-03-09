package com.smart.library.pictureviewer.localpictures

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.smart.library.util.STStorageUtil
import com.smart.library.util.STStorageUtil.LocalPictureTimeSlotInfo.Companion.DATA_TYPE_CONTENT
import com.smart.library.util.STStorageUtil.LocalPictureTimeSlotInfo.Companion.DATA_TYPE_TITLE
import com.smart.library.util.image.STImageManager
import com.smart.library.pictureviewer.R

class STLocalPicturesLocalPicturesAdapter(private var context: Activity, private var imagePathList: List<STStorageUtil.LocalPictureTimeSlotInfo>?, private var columnNums: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), View.OnClickListener {
    private var onItemClickListener: OnRecyclerViewItemClickListener? = null
    fun setImagePathList(imagePaths: List<STStorageUtil.LocalPictureTimeSlotInfo>?) {
        this.imagePathList = imagePaths
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view: View?
        when (viewType) {
            DATA_TYPE_TITLE -> {
                view = context.layoutInflater.inflate(R.layout.st_local_pictures_header_layout, null)
                viewHolder = STLocalPicturesLocalPicturesTitleHolder(view)
            }
            DATA_TYPE_CONTENT -> {
                view = context.layoutInflater.inflate(R.layout.st_local_pictures_item_layout, null)
                viewHolder = STLocalPicturesLocalPicturesContentHolder(view)
            }
            else -> {
                view = context.layoutInflater.inflate(R.layout.st_local_pictures_item_layout, null)
                viewHolder = STLocalPicturesLocalPicturesContentHolder(view)
            }
        }
        view?.setOnClickListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder.itemViewType == DATA_TYPE_TITLE) {
            val holder = viewHolder as STLocalPicturesLocalPicturesTitleHolder
            holder.itemView.tag = position
            val pictureDetailInfo = imagePathList!![position]
            val layoutParams: StaggeredGridLayoutManager.LayoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.isFullSpan = true
            holder.itemView.layoutParams = layoutParams
            holder.headerTextView.text = pictureDetailInfo.dataTitle
        } else if (viewHolder.itemViewType == DATA_TYPE_CONTENT) {
            val holder = viewHolder as STLocalPicturesLocalPicturesContentHolder
            holder.itemView.tag = position
            val pictureDetailInfo = imagePathList!![position]
            // get the current position imageView layout params
            var params = holder.imageView.layoutParams
            // get screen size
            val screenSize = getDiaplaySize(context)
            if (params == null) {
                params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            // resize the width and height
            params.width = screenSize[0] / columnNums
            params.height = params.width * (pictureDetailInfo.pictureInfo?.srcHeight ?: 1) / (pictureDetailInfo.pictureInfo?.srcWidth ?: 1)
            holder.imageView.layoutParams = params
            // use glide load the image into ImageView
            STImageManager.show(holder.imageView, STImageManager.getFileUriString(pictureDetailInfo.pictureInfo?.path))
        }
    }

    private fun getDiaplaySize(context: Activity): IntArray {
        val point = Point()
        context.windowManager.defaultDisplay.getSize(point)
        return intArrayOf(point.x, point.y)
    }

    override fun getItemCount(): Int = if (imagePathList == null) 0 else imagePathList!!.size

    override fun getItemViewType(position: Int): Int {
        return imagePathList!![position].dataType
    }

    fun setOnItemClickListener(onItemClickListener: OnRecyclerViewItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun getItemObject(position: Int): STStorageUtil.LocalPictureTimeSlotInfo? {
        return if (imagePathList != null) {
            imagePathList!![position]
        } else null
    }

    override fun onClick(v: View) {
        if (v.tag != null) {
            val position = v.tag as Int
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(v, position)
            }
        }
    }

    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    class STLocalPicturesLocalPicturesTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.headerTextView) }
    }

    class STLocalPicturesLocalPicturesContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: SimpleDraweeView by lazy { itemView.findViewById<SimpleDraweeView>(R.id.imageView) }
    }
}