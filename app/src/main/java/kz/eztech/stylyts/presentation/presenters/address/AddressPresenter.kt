package kz.eztech.stylyts.presentation.presenters.address

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.usecases.address.DeleteAddressUseCase
import kz.eztech.stylyts.domain.usecases.address.GetAddressUseCase
import kz.eztech.stylyts.domain.usecases.address.PostAddressUseCase
import kz.eztech.stylyts.presentation.contracts.address.AddressContract
import kz.eztech.stylyts.presentation.base.processViewAction
import javax.inject.Inject

class AddressPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private val getAddressUseCase: GetAddressUseCase,
    private val postAddressUseCase: PostAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : AddressContract.Presenter {

    private lateinit var view: AddressContract.View

    override fun attach(view: AddressContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        getAddressUseCase.clear()
        postAddressUseCase.clear()
    }

    override fun getAllAddress(token: String){
        getAddressUseCase.initParams(token)
        getAddressUseCase.execute(object : DisposableSingleObserver<List<AddressModel>>() {

            override fun onSuccess(t: List<AddressModel>) {
                view.processViewAction {
                    if (t.isNotEmpty()) {
                        processAddressList(addressList = t)
                        hideEmpty()
                    } else showEmpty()

                    displayContent()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    showEmpty()
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }

    override fun createAddress(
        token: String,
        data: HashMap<String, Any>
    ) {
        postAddressUseCase.initParams(token, data)
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

    override fun deleteAddress(
        token: String,
        addressId: String
    ) {
        deleteAddressUseCase.initParams(token, addressId)
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