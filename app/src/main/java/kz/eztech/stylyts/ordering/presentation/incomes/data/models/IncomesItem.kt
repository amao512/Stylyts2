package kz.eztech.stylyts.ordering.presentation.incomes.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel
import org.threeten.bp.Month
import org.threeten.bp.ZonedDateTime

const val INCOME_TYPE = 1
const val INCOME_DATE_TYPE = 2

sealed class IncomesItem(
    val type: Int
)

@Parcelize
data class IncomeListItem(
    val year: Int,
    val month: Month,
    val startDay: Int
) : IncomesItem(INCOME_TYPE), Parcelable {
    private var referralList: MutableList<ReferralModel> = mutableListOf()

    val displayTotalProfit
        get() = "${referralList.sumBy { it.totalProfit }} ₸"

    fun addReferral(item: ReferralModel) {
        referralList.add(item)
    }

    fun getReferralList(): List<ReferralModel> = referralList

    fun getTotalProfit(): Int = getReferralList().sumBy { it.totalProfit }
}

data class IncomeDateItem(
    val data: ZonedDateTime,
    val month: Month,
    val year: Int
) : IncomesItem(INCOME_DATE_TYPE)
