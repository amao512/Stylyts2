package kz.eztech.stylyts.profile.presentation.address.adapters

import android.view.View
import kotlinx.android.synthetic.main.item_address_profile.view.*
import kz.eztech.stylyts.global.domain.models.address.AddressModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.profile.presentation.address.interfaces.AddressViewClickListener
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val addressViewClickListener: AddressViewClickListener
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as AddressModel

        with(item) {
            itemView.apply {
                text_view_address_profile_street.text = street
                text_view_address_profile_city.text = city
                text_view_address_profile_post_code.text = postalCode
                text_view_address_profile_country.text = country
                linear_layout_address_profile_container.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it, position, item)
                }

                if (isDefaultAddress) {
                    item_address_divider.hide()
                    item_address_profile_set_default_delivery_address_text_view.hide()

                    item_address_profile_default_card_text_view.show()
                    item_address_profile_prefer_text_view.show()
                }

                item_address_profile_set_default_delivery_address_text_view.setOnClickListener {
                    addressViewClickListener.setDefaultAddress(addressModel = item)
                }
            }
        }
    }
}