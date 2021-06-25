package kz.eztech.stylyts.presentation.presenters.address

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.usecases.address.DeleteAddressUseCase
import kz.eztech.stylyts.domain.usecases.address.GetAddressUseCase
import kz.eztech.stylyts.domain.usecases.address.PostAddressUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.address.AddressContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

class AddressPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<AddressModel>,
    private val getAddressUseCase: GetAddressUseCase,
    private val postAddressUseCase: PostAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : AddressContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: AddressContract.View

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

    override fun attach(view: AddressContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        getAddressUseCase.clear()
        postAddressUseCase.clear()
    }

    override fun loadPage(page: Int) {
        getAddressUseCase.initParams(
            token = view.getToken(),
            page = page
        )
        getAddressUseCase.execute(object : DisposableSingleObserver<ResultsModel<AddressModel>>() {
            override fun onSuccess(t: ResultsModel<AddressModel>) {
                view.processViewAction {
                    t.results.let {
                        if (it.isNotEmpty()) {
                            paginator.proceed(Paginator.Action.NewPage(
                                pageNumber = t.page,
                                items = t.results
                            ))
                            hideEmpty()
                        } else showEmpty()
                    }

                    displayContent()
                }
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getAddresses() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun createAddress(data: HashMap<String, Any>) {
        postAddressUseCase.initParams(token = view.getToken(), data)
        postAddressUseCase.execute(object : DisposableSingleObserver<AddressModel>() {
            override fun onSuccess(t: AddressModel) {
                view.processViewAction {
                    processAddress(addressModel = t)
                    processPostInitialization()
                    displayContent()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }

    override fun deleteAddress(addressId: String) {
        deleteAddressUseCase.initParams(token = view.getToken(), addressId)
        deleteAddressUseCase.execute(object : DisposableSingleObserver<Any>() {
            override fun onSuccess(t: Any) {
                view.processViewAction {
                    displayDeletedAddress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayDeletedAddress()
                }
            }
        })
    }
}