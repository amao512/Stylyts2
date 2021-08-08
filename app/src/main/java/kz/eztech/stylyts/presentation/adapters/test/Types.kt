package kz.eztech.stylyts.presentation.adapters.test

interface Types {

    val size: Int

    fun <T> register(type: Type<T>)

    fun unregister(clazz: Class<*>): Boolean

    fun <T> getType(index: Int): Type<T>

    fun firstIndexOf(clazz: Class<*>): Int
}