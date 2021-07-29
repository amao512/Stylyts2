package kz.eztech.stylyts.presentation.adapters.incomes

import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import org.threeten.bp.Month
import org.threeten.bp.ZonedDateTime

const val INCOME_TYPE = 1
const val INCOME_DATE_TYPE = 2

sealed class IncomesItem(
    val type: Int
)

data class IncomeListItem(
    val year: Int,
    val month: Month,
    val startDay: Int
) : IncomesItem(INCOME_TYPE) {
    private var referralList: MutableList<ReferralModel> = mutableListOf()

    fun addReferral(item: ReferralModel) {
        referralList.add(item)
    }

    fun getReferralList(): List<ReferralModel> = referralList
}

data class IncomeDateItem(
    val data: ZonedDateTime,
    val month: Month,
    val year: Int
) : IncomesItem(INCOME_DATE_TYPE)
