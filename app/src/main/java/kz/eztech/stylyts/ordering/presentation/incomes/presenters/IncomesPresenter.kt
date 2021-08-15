package kz.eztech.stylyts.ordering.presentation.incomes.presenters

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.ordering.domain.usecases.referrals.GetReferralListUseCase
import kz.eztech.stylyts.ordering.presentation.incomes.contracts.IncomeContract
import kz.eztech.stylyts.ordering.presentation.incomes.data.UIIncomeData
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.INCOME_TYPE
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class IncomesPresenter @Inject constructor(
    private val paginator: Paginator.Store<ReferralModel>,
    private val getReferralListUseCase: GetReferralListUseCase,
    private val uiIncomeData: UIIncomeData
) : IncomeContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: IncomeContract.View

    init {
        launch {
            paginator.render = { view.renderPaginatorState(it) }
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                    }
                }
            }
        }
    }

    override fun disposeRequests() {
        getReferralListUseCase.clear()
    }

    override fun attach(view: IncomeContract.View) {
        this.view = view
    }

    override fun loadPage(page: Int) {
        getReferralListUseCase.initParams(page)
        getReferralListUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ReferralModel>>() {
            override fun onSuccess(t: ResultsModel<ReferralModel>) {
                paginator.proceed(
                    Paginator.Action.NewPage(
                        pageNumber = t.page,
                        items = uiIncomeData.getIncomeList(incomesList = t.results.reversed())
                    )
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun getReferrals() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getTotalProfit(list: List<Any?>) {
        var referralCost = 0

        list.map { referral ->
            referral as IncomesItem

            if (referral.type == INCOME_TYPE) {
                referralCost += (referral as IncomeListItem).getTotalProfit()
            }
        }

        view.processTotalProfit(totalProfit = referralCost)
    }
}