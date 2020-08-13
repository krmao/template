package com.smart.template.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.smart.library.util.image.STImageManager
import com.smart.template.R

class STBehaviorBottomSheetImagesPagerAdapter(context: Context) : PagerAdapter() {

    private val layoutInflater: LayoutInflater by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.st_behavior_pager_item, container, false)
        val imageView = itemView.findViewById<View>(R.id.imageView) as SimpleDraweeView
        STImageManager.show(imageView, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565851968832&di=b73c29d745a1454381ea2276e0707d72&imgtype=0&src=http%3A%2F%2Fzz.fangyi.com%2FR_Img%2Fnews%2F8%2F2016_1%2F9%2F20160109173836_4593.jpg")
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as LinearLayout)
    }
}