package com.smart.housekeeper.module.hybird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.imp.HKBundleManager
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKToastUtil
import kotlinx.android.synthetic.main.hybird_fragment.*

class HybirdFragment : HKBaseFragment() {

    private val indexPath = "file://${HKBundleManager.pathForHybirdDir}index.html"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text.setOnClickListener {
            HKBundleManager.installWithVerify {
                if (it) {
                    HybirdWebFragment.goTo(activity, indexPath)

                    HKLogUtil.d("hybird", "加载成功")
                    HKToastUtil.show("加载成功")
                } else {
                    HKLogUtil.e("hybird", "加载失败,请重新安装程序")
                    HKToastUtil.show("加载失败,请重新安装程序")
                }
            }
        }
    }
}
