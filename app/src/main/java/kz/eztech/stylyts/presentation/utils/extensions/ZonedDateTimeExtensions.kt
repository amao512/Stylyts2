package kz.eztech.stylyts.presentation.utils.extensions

import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.getIncomeDateTime(): String = "${month.toString().capitalizeWord()} $dayOfMonth"