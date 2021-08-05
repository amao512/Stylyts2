package kz.eztech.stylyts.presentation.utils.extensions

fun Boolean?.orTrue() = this ?: true

fun Boolean?.orFalse() = this ?: false