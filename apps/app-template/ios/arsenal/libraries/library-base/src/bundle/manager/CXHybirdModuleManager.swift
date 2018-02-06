import Foundation

class CXHybirdModuleManager {


    init(_ config: CXHybirdModuleConfigModel) {
        //swift 这里不会触发 didSet
        self.currentConfig = config

        defer {
            //swift 这里会触发 didSet
            //self.currentConfig = config//手动重新赋值一下才会调用 其自定义的 set 方法, 从而设置拦截器
        }
    }

    static func create(_ config: CXHybirdModuleConfigModel?) -> CXHybirdModuleManager? {
        if (config == nil || !CXHybirdUtil.isLocalFilesValid(config)) {
            CXLogUtil.e("||||||||=====>>>>>\(config?.moduleName ?? "") [初始化 错误]<<<< 本地解压文件夹校验失败,不能初始化模块 return nil !!!")
            return nil
        } else {
            //CXLogUtil.e("||||||||=====>>>>>\(config?.moduleName ?? "") create success")
            return CXHybirdModuleManager(config!)
        }
    }

    var currentConfig: CXHybirdModuleConfigModel {
        didSet {
            let field = self.currentConfig

            CXLogUtil.e("||||||||=====>>>>>\(field.moduleName) currentConfig didSet")

            CXHybirdUtil.setIntercept(field)//当设置新的当前生效的配置信息时，更新拦截请求配置
            if (!CXHybird.isModuleOpened(field.moduleName)) {
                CXLogUtil.e("||||||||=====>>>>>\(field.moduleName) 设置完拦截器后由于当前模块尚没有被浏览器加载, 强制设置 onlineModel = false (考虑到检查更新替换为目标版本成功后,此时 应尝试 设置 onlineModel = false), 当前onlineModel=\(onlineModel)")
                onlineModel = false
            } else {
                CXLogUtil.e("||||||||=====>>>>>\(field.moduleName) 设置完拦截器后由于当前模块已经被浏览器加载, 无法强制设置 onlineModel = false, 当前onlineModel=\(onlineModel)")
            }
        }
    }

    var onlineModel: Bool = false

}
