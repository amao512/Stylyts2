package kz.eztech.stylyts.presentation.adapters.test

interface JavaClassLinker<T> {

    fun index(position: Int, item: T): Class<out ItemViewDelegate<T, *>>
}