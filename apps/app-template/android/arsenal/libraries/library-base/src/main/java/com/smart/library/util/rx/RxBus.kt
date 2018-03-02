package com.smart.library.util.rx

import java.util.concurrent.ConcurrentHashMap

/**
 * 使用RxBus 注意取消订阅, 避免内存泄露
 *
 * 案例:
 *      //任意的 RxJava 的订阅都应该如此操作
 *      val compositeDisposable = CompositeDisposable()
 *
 *      compositeDisposable.add(RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
 *          if (changeEvent.isApplicationVisible)
 *              CXDebugFragment.showDebugNotification(notificationId)
 *          else
 *              CXDebugFragment.cancelDebugNotification(notificationId)
 *      })
 *
 *      //当前页面退出的时候调用 比如 fragment/activity 的 onDestroy 方法里面调用
 *      compositeDisposable.dispose()
 */
@Suppress("unused")
object RxBus {

    //==============================================================================================
    // 标准广播事件 START
    //==============================================================================================

    private val bus: io.reactivex.subjects.PublishSubject<Any> by lazy { io.reactivex.subjects.PublishSubject.create<Any>() }


    fun post(event: Any) {
        bus.onNext(event)
    }

    fun <T> toObservable(eventType: Class<T>): io.reactivex.Observable<T> {
        return bus.ofType(eventType)
    }

    //==============================================================================================
    // 标准广播事件 END and 粘性广播事件 START
    //==============================================================================================

    private val stickyEventMap: ConcurrentHashMap<Class<*>, Any> by lazy { ConcurrentHashMap<Class<*>, Any>() }

    /**
     * 发送粘性广播, A 订阅者收到10条数据后 B 才开始订阅, B也会收到全部10条数据
     * 注意不用的时候 调用 removeStickyEvent 清除历史事件
     * 在程序退出的时候 调用 removeAllStickyEvents 清除所有历史事件
     */
    fun postSticky(event: Any) {
        stickyEventMap[event.javaClass] = event
        post(event)
    }

    fun <T> toObservableSticky(eventType: Class<T>): io.reactivex.Observable<T> {
        val observable = bus.ofType(eventType)
        val event = stickyEventMap[eventType]
        return if (event != null) observable.mergeWith(io.reactivex.Observable.just(eventType.cast(event))) else observable
    }

    fun <T> getStickyEvent(eventType: Class<T>): T {
        return eventType.cast(stickyEventMap[eventType])
    }

    fun <T> removeStickyEvent(eventType: Class<T>): T {
        return eventType.cast(stickyEventMap.remove(eventType))
    }

    fun removeAllStickyEvents() {
        stickyEventMap.clear()
    }

    //==============================================================================================
    // 粘性广播事件 END
    //==============================================================================================

}
