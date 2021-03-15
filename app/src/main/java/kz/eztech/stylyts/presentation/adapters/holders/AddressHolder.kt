package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_address_profile.view.*
import kz.eztech.stylyts.domain.models.AddressModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressHolder(
    itemView: View,
    adapter: BaseAdapter
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
        }
    }
}