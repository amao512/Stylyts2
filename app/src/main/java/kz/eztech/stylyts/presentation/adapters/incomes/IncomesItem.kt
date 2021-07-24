package kz.eztech.stylyts.presentation.adapters.incomes

import kz.eztech.stylyts.domain.models.income.IncomeModel

const val INCOME_TYPE = 1
const val INCOME_DATE_TYPE = 2

sealed class IncomesItem(
    val type: Int
)

data class IncomeListItem(
    val data: IncomeModel
) : IncomesItem(INCOME_TYPE)

data class IncomeDateItem(
    val data: String
) : IncomesItem(INCOME_DATE_TYPE)
