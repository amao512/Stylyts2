package kz.eztech.stylyts.profile.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_address_profile.view.*
import kz.eztech.stylyts.profile.domain.models.AddressModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.profile.presentation.interfaces.AddressViewClickListener
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show

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

        itemView.apply {
            text_view_address_profile_street.text = item.street
            text_view_address_profile_city.text = item.city
            text_view_address_profile_post_code.text = item.postalCode
            text_view_address_profile_country.text = item.country
            linear_layout_address_profile_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }

            if (item.isDefaultAddress) {
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