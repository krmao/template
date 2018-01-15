package com.smart.library.bundle.manager

import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.bundle.util.HKHybirdUtil
import com.smart.library.util.HKLogUtil

class HKHybirdModuleManager private constructor(config: HKHybirdModuleConfigModel) {

    companion object {
        @JvmStatic
        fun create(config: HKHybirdModuleConfigModel?): HKHybirdModuleManager? {
            if (config == null || !HKHybirdUtil.isLocalFilesValid(config)) {
                HKLogUtil.e(HKHybird.TAG + ":" + config?.moduleName, ">>>>[错误]<<<< 本地解压文件夹校验失败,不能初始化模块 return null !!!")
                return null
            } else {
                return HKHybirdModuleManager(config)
            }
        }
    }

    var currentConfig: HKHybirdModuleConfigModel = config
        set(value) {
            field = value
            HKHybirdUtil.setIntercept(value)//当设置新的当前生效的配置信息时，更新拦截请求配置
            if (!HKHybird.isModuleOpened(field.moduleName)) {
                HKLogUtil.e(HKHybird.TAG + ":" + field.moduleName, "||||||||=====>>>>>设置完拦截器后由于当前模块尚没有被浏览器加载, 强制设置 onlineModel = false (考虑到检查更新替换为目标版本成功后,此时 应尝试 设置 onlineModel = false), 当前onlineModel=$onlineModel")
                onlineModel = false
            } else {
                HKLogUtil.e(HKHybird.TAG + ":" + field.moduleName, "||||||||=====>>>>>设置完拦截器后由于当前模块已经被浏览器加载, 无法强制设置 onlineModel = false, 当前onlineModel=$onlineModel")
            }
        }

    var onlineModel: Boolean = false

    init {
        this.currentConfig = config//手动重新赋值一下才会调用 其自定义的 set 方法, 从而设置拦截器
    }

}
