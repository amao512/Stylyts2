package kz.eztech.stylyts.ordering.presentation.incomes.presenters

import kz.eztech.stylyts.ordering.domain.usecases.order.GetOrderByIdUseCase
import kz.eztech.stylyts.ordering.domain.usecases.referrals.GetReferralUseCase
import kz.eztech.stylyts.ordering.presentation.incomes.contracts.IncomeDetailContract
import kz.eztech.stylyts.ordering.presentation.incomes.data.UIIncomeData
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import javax.inject.Inject

class IncomeDetailPresenter @Inject constructor(
    private val getReferralUseCase: GetReferralUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val uiIncomeData: UIIncomeData
) : IncomeDetailContract.Presenter {

    private lateinit var view: IncomeDetailContract.View

    override fun disposeRequests() {
        getReferralUseCase.clear()
        getOrderByIdUseCase.clear()
    }

    override fun attach(view: IncomeDetailContract.View) {
        this.view = view
    }

    override fun getReferralList(incomeListItem: IncomeListItem) {
        view.processReferralList(
            list = uiIncomeData.getReferralItemList(incomeListItem)
        )
    }
}