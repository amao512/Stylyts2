package kz.eztech.stylyts.utils.extensions

fun Boolean?.orTrue() = this ?: true

fun Boolean?.orFalse() = this ?: false