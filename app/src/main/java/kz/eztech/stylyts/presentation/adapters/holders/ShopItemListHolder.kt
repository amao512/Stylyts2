package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_shop_item_list.view.*
import kz.eztech.stylyts.domain.models.shop.ClothesTypes
import kz.eztech.stylyts.presentation.adapters.BaseAdapter

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopItemListHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        with(item as ClothesTypes){
            with(itemView){
                text_view_item_shop_item_list.text = title
                linear_layout_item_shop_item_list.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it,position,item)
                }
            }
        }
    }
}