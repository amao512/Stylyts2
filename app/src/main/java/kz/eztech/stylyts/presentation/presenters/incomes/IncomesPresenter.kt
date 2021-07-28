package kz.eztech.stylyts.presentation.presenters.incomes

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.domain.usecases.referrals.GetReferralListUseCase
import kz.eztech.stylyts.presentation.adapters.incomes.INCOME_DATE_TYPE
import kz.eztech.stylyts.presentation.adapters.incomes.IncomeDateItem
import kz.eztech.stylyts.presentation.adapters.incomes.IncomeListItem
import kz.eztech.stylyts.presentation.adapters.incomes.IncomesItem
import kz.eztech.stylyts.presentation.contracts.incomes.IncomeContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

class IncomesPresenter @Inject constructor(
    private val paginator: Paginator.Store<ReferralModel>,
    private val getReferralListUseCase: GetReferralListUseCase
) : IncomeContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: IncomeContract.View

    init {
        launch {
            paginator.render = { view.renderPaginatorState(it) }
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {}
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
                        items = getPreparedIncomesList(list = t.results)
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

    private fun getPreparedIncomesList(list: List<ReferralModel>): List<IncomesItem> {
        val preparedList: MutableList<IncomesItem> = mutableListOf()

        list.map {
            val dateItem = IncomeDateItem(
                data = it.createdAt,
                month = it.createdAt.month,
                year = it.createdAt.year
            )

            if (preparedList.isEmpty()) {
                preparedList.add(dateItem)
            } else {
                preparedList.map { income ->
                    if (income.type == INCOME_DATE_TYPE) {
                        income as IncomeDateItem

                        if (income.month != it.createdAt.month && income.year != it.createdAt.year) {
                            preparedList.add(dateItem)
                        }
                    }
                }
            }

            preparedList.add(IncomeListItem(data = it))
        }

        return preparedList
    }
}