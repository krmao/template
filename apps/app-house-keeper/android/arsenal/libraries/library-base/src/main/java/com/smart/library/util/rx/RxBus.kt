package com.smart.library.util.rx

/*

RxBus.toObservable(HKTestingFragment.ChangeEvent::class.java).subscribe { changeEvent ->
    try {
        HKURLManager.curEnvironment = HKURLManager.Environments.valueOf(changeEvent.model.name)
    } catch (_: Exception) {
    }
    HKToastUtil.show("检测到环境切换(${changeEvent.model.name})\n已切换到:${HKURLManager.curEnvironment.name}")
}

 */
object RxBus {
    private val bus: io.reactivex.subjects.PublishSubject<Any> by lazy { io.reactivex.subjects.PublishSubject.create<Any>() }

    fun post(event: Any) = bus.onNext(event)

    fun <T> toObservable(eventType: Class<T>): io.reactivex.Observable<T> = bus.ofType(eventType)
}
