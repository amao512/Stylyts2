package kz.eztech.stylyts.presentation.adapters.test

import androidx.annotation.CheckResult

interface OneToManyFlow<T> {

    @CheckResult
    fun to(vararg delegates: ItemViewDelegate<T, *>): OneToManyEndpoint<T>

    @CheckResult
    fun to(vararg binders: ItemViewBinder<T, *>): OneToManyEndpoint<T>
}