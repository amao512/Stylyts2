package kz.eztech.stylyts.ordering.presentation.order_constructor.presenters

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.address.AddressModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.domain.usecases.address.GetAddressUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.ordering.presentation.order_constructor.contracts.PickupPointsContract
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class PickupPointsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<AddressModel>,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : PickupPointsContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: PickupPointsContract.View

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
        getUserByIdUseCase.clear()
        getAddressUseCase.clear()
        cancel()
    }

    override fun attach(view: PickupPointsContract.View) {
        this.view = view
    }

    override fun getShop(id: Int) {
        view.displayProgress()

        getUserByIdUseCase.initParams(id)
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processShop(userModel = t)
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.hideProgress()
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun loadPage(page: Int) {
        getAddressUseCase.initParams(
            isMy = false,
            owner = view.getShopId(),
            page = page
        )
        getAddressUseCase.execute(object : DisposableSingleObserver<ResultsModel<AddressModel>>() {
            override fun onSuccess(t: ResultsModel<AddressModel>) {
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

    override fun getPickupPoints() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }
}