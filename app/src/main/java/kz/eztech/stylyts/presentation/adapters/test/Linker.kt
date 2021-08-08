package kz.eztech.stylyts.presentation.adapters.test

import androidx.annotation.IntRange

interface Linker<T> {

    @IntRange(from = 0)
    fun index(position: Int, item: T): Int
}