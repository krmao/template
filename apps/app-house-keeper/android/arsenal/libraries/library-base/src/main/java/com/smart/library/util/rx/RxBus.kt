package com.smart.library.util.rx

import java.util.concurrent.ConcurrentHashMap


object RxBus {
    private val bus: io.reactivex.subjects.PublishSubject<Any> by lazy { io.reactivex.subjects.PublishSubject.create<Any>() }
    private val stickyEventMap: ConcurrentHashMap<Class<*>, Any> by lazy { ConcurrentHashMap<Class<*>, Any>() }

    fun post(event: Any) {
        bus.onNext(event)
    }

    fun postSticky(event: Any) {
        stickyEventMap.put(event.javaClass, event)
        post(event)
    }

    fun <T> toObservable(eventType: Class<T>): io.reactivex.Observable<T> {
        return bus.ofType(eventType)
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

}