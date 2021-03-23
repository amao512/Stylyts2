package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.profile.domain.models.AddressModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

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

        fun displayDeletedAddress()
    }

    interface Presenter : BasePresenter<View> {

        fun getAllAddress(token: String)

        fun createAddress(
            token: String,
            data: HashMap<String, Any>
        )

        fun deleteAddress(
            token: String,
            addressId: String
        )
    }
}