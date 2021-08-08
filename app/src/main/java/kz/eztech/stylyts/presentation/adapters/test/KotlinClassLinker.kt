package kz.eztech.stylyts.presentation.adapters.test

import kotlin.reflect.KClass

interface KotlinClassLinker<T> {

    fun index(position: Int, item: T): KClass<out ItemViewDelegate<T, *>>
}