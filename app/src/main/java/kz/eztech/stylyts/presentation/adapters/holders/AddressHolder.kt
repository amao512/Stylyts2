package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_address_profile.view.*
import kotlinx.android.synthetic.main.item_cart_item.view.*
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CartEntity
import kz.eztech.stylyts.data.db.entities.CartMapper
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as AddressEntity
        itemView.apply {
            text_view_address_profile_name.text = item.name
            text_view_address_profile_surname.text = item.surname
            text_view_address_profile_city.text = item.address
            text_view_address_profile_post_code.text = item.postIndex
            text_view_address_profile_country.text = item.country
            text_view_address_profile_phone.text = item.phone
            linear_layout_address_profile_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it,position,item)
            }
        }
    }
}