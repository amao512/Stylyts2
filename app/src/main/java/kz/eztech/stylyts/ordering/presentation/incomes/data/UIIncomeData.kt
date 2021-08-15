package kz.eztech.stylyts.ordering.presentation.incomes.data

import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.*
import kz.eztech.stylyts.utils.getIncomeDateString
import org.threeten.bp.ZonedDateTime

interface UIIncomeData {

    fun getIncomeList(
        incomesList: List<ReferralModel>
    ): List<IncomesItem>

    fun getReferralItemList(
        incomeListItem: IncomeListItem
    ): List<ReferralItem>
}

class UIIncomeDataDelegate : UIIncomeData {

    override fun getIncomeList(incomesList: List<ReferralModel>): List<IncomesItem> {
        val preparedList: MutableList<IncomesItem> = mutableListOf()
        var lastIncomeCounter = 0

        incomesList.map {
            if (preparedList.isEmpty()) {
                preparedList.add(
                    getIncomeDate(createdAt = it.createdAt)
                )
                preparedList.add(getIncomeItem(it.createdAt).also { income ->
                    income.addReferral(it)
                })

                lastIncomeCounter = 1
            } else {
                val lastIncome = preparedList[lastIncomeCounter] as IncomeListItem

                if (lastIncome.year == it.createdAt.year && lastIncome.month == it.createdAt.month) {
                    if ((lastIncome.startDay + 7) < it.createdAt.dayOfMonth) {
                        preparedList.add(getIncomeItem(it.createdAt).also { income ->
                            income.addReferral(it)
                        })

                        lastIncomeCounter++
                    } else {
                        lastIncome.addReferral(it)
                    }
                } else {
                    preparedList.add(
                        getIncomeDate(createdAt = it.createdAt)
                    )
                    preparedList.add(getIncomeItem(it.createdAt).also { income ->
                        income.addReferral(it)
                    })

                    lastIncomeCounter += 2
                }
            }
        }

        return preparedList
    }

    override fun getReferralItemList(incomeListItem: IncomeListItem): List<ReferralItem> {
        val preparedList: MutableList<ReferralItem> = mutableListOf()

        preparedList.add(
            ReferralProfitItem(
                date = incomeListItem.getReferralList().getIncomeDateString(),
                totalProfit = incomeListItem.displayTotalProfit
            )
        )

        incomeListItem.getReferralList().map {
            it.items.map { item ->
                preparedList.add(
                    ReferralListItem(data = item)
                )
            }
        }

        return preparedList
    }

    private fun getIncomeDate(createdAt: ZonedDateTime): IncomeDateItem {
        return IncomeDateItem(
            data = createdAt,
            month = createdAt.month,
            year = createdAt.year
        )
    }

    private fun getIncomeItem(createdAt: ZonedDateTime): IncomeListItem {
        return IncomeListItem(
            year = createdAt.year,
            month = createdAt.month,
            startDay = createdAt.dayOfMonth
        )
    }
}