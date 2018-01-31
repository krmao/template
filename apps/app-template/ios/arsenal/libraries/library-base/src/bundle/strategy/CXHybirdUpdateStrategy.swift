import Foundation

/**
 * 检查更新的策略
 */
public enum CXHybirdUpdateStrategy:Hashable {
    //检测到更新理解切换在线,在程序第一次启动初始化 以及 前后台切换的时候执行 异步检查,  每次加载模块URL 同步检查
    case ONLINE
    //检测到更新仍然使用本地,在程序第一次启动初始化 以及 前后台切换的时候执行 异步检查,  每次加载模块URL   不检查
    case OFFLINE
}
