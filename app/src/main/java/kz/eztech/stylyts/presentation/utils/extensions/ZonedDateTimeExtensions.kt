package kz.eztech.stylyts.presentation.utils.extensions

import org.threeten.bp.ZonedDateTime

fun String.getZonedDateTime(): ZonedDateTime = try {
    ZonedDateTime.parse(this) ?: ZonedDateTime.now()
} catch (e: Exception) {
    ZonedDateTime.now()
}

fun ZonedDateTime.getMonthAndDay(): String = "${month.toString().capitalizeWord()} $dayOfMonth"

fun ZonedDateTime.getMonthAndYear(): String = "${month.toString().capitalizeWord()} $year"

fun ZonedDateTime.getDayAndMonth(): String = "$dayOfMonth ${month.toString().capitalizeWord()}"

fun ZonedDateTime.getSlashDate(): String = "${dayOfMonth}/${monthValue}/${year}"