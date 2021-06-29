package kz.eztech.stylyts.presentation.presenters.ordering

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.usecases.order.GetOrderListUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.contracts.ordering.OrderListContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

class OrderListPresenter @Inject constructor(
    private val paginator: Paginator.Store<OrderModel>,
    private val getOrderListUseCase: GetOrderListUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : OrderListContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: OrderListContract.View

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
        getOrderListUseCase.clear()
        getProfileUseCase.clear()
        cancel()
    }

    override fun attach(view: OrderListContract.View) {
        this.view = view
    }

    override fun loadPage(page: Int) {
        getOrderListUseCase.initParams(
            token = view.getToken(),
            page = page
        )
        getOrderListUseCase.execute(object : DisposableSingleObserver<ResultsModel<OrderModel>>() {
            override fun onSuccess(t: ResultsModel<OrderModel>) {
                paginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = t.results
                ))
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getOrders() {
        paginator.proceed(Paginator.Action.Refresh)
    }
}