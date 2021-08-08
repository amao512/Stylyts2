package kz.eztech.stylyts.presentation.adapters.test

class DefaultLinker<T> : Linker<T> {

    override fun index(position: Int, item: T): Int = 0
}