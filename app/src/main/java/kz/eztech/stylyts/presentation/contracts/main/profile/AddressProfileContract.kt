package kz.eztech.stylyts.presentation.contracts.main.profile

import kz.eztech.stylyts.domain.models.AddressModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
interface AddressProfileContract {

    interface View : BaseView {

        fun displayContent()

        fun displayForm()

        fun processAddressList(addressList: List<AddressModel>)

        fun processAddress(addressModel: AddressModel)

        fun showEmpty()

        fun hideEmpty()
    }

    interface Presenter : BasePresenter<View> {

        fun getAllAddress(token: String)

        fun createAddress(
            token: String,
            data: HashMap<String, Any>
        )
    }
}