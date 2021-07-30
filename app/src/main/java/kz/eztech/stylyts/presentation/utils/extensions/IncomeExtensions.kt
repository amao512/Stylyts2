package kz.eztech.stylyts.presentation.utils.extensions

import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import org.threeten.bp.Month

fun List<ReferralModel>.getIncomeDateString(): String {
    val startDate = this.first().createdAt
    val lastDate = this.last().createdAt

    return if (startDate.dayOfMonth != lastDate.dayOfMonth) {
        startDate.getIncomeDateTime() + " - " + lastDate.getIncomeDateTime()
    } else {
        if ((lastDate.dayOfMonth + 7) > 31) {
            val day = lastDate.month.length(false)
            val newDate = getDate(lastDate.year, lastDate.month, day.toString())

            startDate.getIncomeDateTime() + " - " + newDate.getTimeByFormat().getIncomeDateTime()
        } else {
            val newDate = getDate(lastDate.year, lastDate.month, getDay(lastDate.dayOfMonth))

            startDate.getIncomeDateTime() + " - " + newDate.getTimeByFormat().getIncomeDateTime()
        }
    }
}

private fun getDate(
    year: Int,
    month: Month,
    day: String
): String = "$year-${getMonth(month)}-${day}T12:12:03.784536+06:00"

private fun getMonth(month: Month): String {
    return if (month.value < 10) {
        "0${month.value}"
    } else {
        month.value.toString()
    }
}

private fun getDay(day: Int): String {
    return if (day < 10) {
        "0${day + 7}"
    } else {
        (day + 7).toString()
    }
}