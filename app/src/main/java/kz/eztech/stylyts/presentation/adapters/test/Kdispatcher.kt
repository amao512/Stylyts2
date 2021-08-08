package kz.eztech.stylyts.presentation.adapters.test

typealias Subscriber<T> = (Notification<T>) -> Unit

data class Notification<T : Any?>(var data: T? = null, var eventName: String?)

object KDispatcher : IDispatcher {

    override val subscribers = mutableMapOf<String, MutableList<Subscriber<Any>>>()

    override val priorityListeners = mutableMapOf<Subscriber<Any>, Int?>()
}

interface IDispatcher {

    val subscribers: MutableMap<String, MutableList<Subscriber<Any>>>

    val priorityListeners: MutableMap<Subscriber<Any>, Int?>
}

inline fun <reified T : Any> IDispatcher.subscribe(notificationName: String?, priority: Int? = null, noinline sub: Subscriber<T>) {
    val localSubscribers = subscribers
    val ls = localSubscribers.getOrPut(notificationName.orEmpty()) { mutableListOf() }
    if (ls.indexOf(sub as Subscriber<*>) < 0) {
        ls.add(sub)
        priority?.takeIf { it > 0 }?.let {
            priorityListeners[sub] = priority
            ls.sortBy { subscriber -> priorityListeners.getOrPut(subscriber) { priority } }
        }
    }
}

inline fun <reified T : Any> IDispatcher.unsubscribe(notificationName: String?, noinline sub: Subscriber<T>? = null) {
    (sub as? Subscriber<Any>)?.let { subscriber ->
        val localSubscribers = subscribers
        val localPriorityListener = priorityListeners
        localPriorityListener.remove(subscriber)
        val ls = localSubscribers[notificationName]
        ls?.let { listeners ->
            if (listeners.remove(subscriber)) {
                if (listeners.isEmpty()) {
                    localSubscribers.remove(notificationName)?.clear()
                }
            }
        }
    } ?: unsubscribeAll(notificationName)
}

fun IDispatcher.unsubscribeAll(notif: String?) {
    if (hasSubscribers(notif)) {
        val localSubscribers = subscribers
        localSubscribers.remove(notif)?.clear()
    }
}

fun IDispatcher.call(notif: String?, data: Any? = null) {
    val localSubscribers = subscribers.toMap()
    val ls = localSubscribers[notif]?.toList()
    ls?.let {
        val notification = Notification(data, notif)
        it.forEach {
            it(notification)
        }
        notification.data = null
    } ?: println("NO LISTENERS FOR EVENT '$notif'")
}

fun IDispatcher.hasSubscribers(notif: String?): Boolean {
    val localSubscribers = subscribers.toMap()
    return localSubscribers[notif]?.isNotEmpty() == true
}

interface IKDispatcher

inline fun <reified T : Any> IKDispatcher.subscribe(notif: String?, priority: Int? = null, noinline sub: Subscriber<T>): IKDispatcher {
    KDispatcher.subscribe(notif, priority, sub)
    return this
}

inline fun <reified T : Any> IKDispatcher.subscribeList(notifs: List<String>, priority: Int? = null, noinline sub: Subscriber<T>): IKDispatcher {
    notifs.forEach { notif ->
        if (!hasSubscribers(notif)) KDispatcher.subscribe(notif, priority, sub)
    }
    return this
}

fun IKDispatcher.hasSubscribers(notif: String): Boolean {
    return KDispatcher.hasSubscribers(notif)
}

fun IKDispatcher.unsubscribe(notif: String?, sub: Subscriber<Any>? = null) {
    KDispatcher.unsubscribe(notif, sub)
}

fun IKDispatcher.unsubscribeAll(notif: String) {
    KDispatcher.unsubscribeAll(notif)
}

fun IKDispatcher.unsubscribeList(notifs: List<String>) {
    notifs.forEach { notif ->
        if (!hasSubscribers(notif)) unsubscribe(notif)
    }
}

fun IKDispatcher.call(notif: String?, data: Any? = null) {
    KDispatcher.call(notif, data)
}

infix fun <T : Any> String.callWith(data: T) {
    KDispatcher.call(this, data)
}