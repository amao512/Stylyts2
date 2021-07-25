package kz.eztech.stylyts.presentation.contracts.address

import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
interface AddressContract {

    interface View : BaseView {

        fun displayContent()

        fun displayForm()

        fun renderPaginatorState(state: Paginator.State)

        fun processAddressList(list: List<Any?>)

        fun processAddress(addressModel: AddressModel)

        fun showEmpty()

        fun hideEmpty()

        fun displayDeletedAddress()
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadMorePage()

        fun getAddresses()

        fun createAddress(data: HashMap<String, Any>)

        fun deleteAddress(addressId: String)
    }
}