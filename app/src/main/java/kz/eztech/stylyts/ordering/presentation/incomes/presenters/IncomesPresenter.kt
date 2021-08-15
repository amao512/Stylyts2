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
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeDateItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.utils.Paginator
import org.threeten.bp.ZonedDateTime
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
                        items = getPreparedIncomesList(list = t.results.reversed())
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
        var lastIncomeCounter = 0

        list.map {
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