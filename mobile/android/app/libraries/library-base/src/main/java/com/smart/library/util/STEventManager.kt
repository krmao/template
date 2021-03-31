package com.smart.library.util

import androidx.annotation.Keep

@Keep
@Suppress("unused")
object STEventManager {

    private val eventMap: HashMap<String, HashSet<Event>> = hashMapOf()

    @JvmStatic
    fun register(eventId: String, eventKey: String, callbackListener: (eventKey: String, value: Map<String, Any>?) -> Unit) {
        synchronized(eventMap) {
            val eventSet: HashSet<Event> = eventMap.getOrElse(eventId) { hashSetOf() }
            eventSet.add(Event(eventKey, callbackListener))
            eventMap.put(eventId, eventSet)
        }
    }

    @JvmStatic
    fun unregister(eventId: String, eventKey: String) {
        if (eventKey.isNotBlank()) {
            synchronized(eventMap) {
                if (eventMap.containsKey(eventId)) {
                    val eventSet: HashSet<Event>? = eventMap[eventId]
                    if (eventSet != null) {
                        eventSet.remove(Event(eventKey))
                        if (eventSet.isEmpty()) {
                            eventMap.remove(eventId)
                        } else {
                            eventMap[eventId] = eventSet
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    fun unregisterAll(eventId: String) {
        synchronized(eventMap) {
            eventMap.remove(eventId)
        }
    }

    @JvmStatic
    fun sendEvent(eventKey: String, value: Map<String, Any>?) {
        if (eventKey.isNotBlank()) {
            synchronized(eventMap) {
                eventMap.forEach { entry: Map.Entry<Any, java.util.HashSet<Event>> ->
                    eventMap[entry.key]?.forEach { model: Event ->
                        if (model.eventKey == eventKey) {
                            model.callbackListener?.invoke(eventKey, value)
                        }
                    }
                }
            }
        }
    }

    internal class Event @JvmOverloads constructor(val eventKey: String, val callbackListener: ((eventKey: String, value: Map<String, Any>?) -> Unit)? = null) {
        override fun equals(other: Any?): Boolean {
            return (other is Event) && other.eventKey.isNotBlank() && other.eventKey == eventKey
        }

        override fun hashCode(): Int {
            return if (eventKey.isBlank()) super.hashCode() else eventKey.hashCode()
        }
    }
}