package com.smart.library.util

@Suppress("unused")
object STEventManager {

    private val eventMap: HashMap<Any, HashSet<Event>> = hashMapOf()

    @JvmStatic
    fun register(eventId: Any, eventKey: String, callbackListener: (eventKey: String, value: Any?) -> Unit) {
        synchronized(eventMap) {
            val eventSet: HashSet<Event> = eventMap.getOrElse(eventId) { hashSetOf() }
            eventSet.add(Event(eventKey, callbackListener))
            eventMap.put(eventId, eventSet)
        }
    }

    @JvmStatic
    fun unregister(eventId: Any, eventKey: String) {
        if (eventKey.isNotBlank()) {
            synchronized(eventMap) {
                if (eventMap.containsKey(eventId)) {
                    val eventSet: HashSet<Event>? = eventMap[eventId]
                    if (eventSet != null) {
                        eventSet.remove(Event(eventKey))
                        eventMap[eventId] = eventSet
                    }
                }
            }
        }
    }

    @JvmStatic
    fun unregisterAll(eventId: Any) {
        synchronized(eventMap) {
            eventMap.remove(eventId)
        }
    }

    @JvmStatic
    fun sendEvent(eventKey: String, value: Any?) {
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

    internal class Event @JvmOverloads constructor(val eventKey: String, val callbackListener: ((eventKey: String, value: Any?) -> Unit)? = null) {
        override fun equals(other: Any?): Boolean {
            return (other is Event) && other.eventKey.isNotBlank() && other.eventKey == eventKey
        }

        override fun hashCode(): Int {
            return if (eventKey.isBlank()) super.hashCode() else eventKey.hashCode()
        }
    }
}