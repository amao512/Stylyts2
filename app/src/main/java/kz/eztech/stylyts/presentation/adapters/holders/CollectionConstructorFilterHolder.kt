package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_constructor_filter_clothe_items.view.*
import kz.eztech.stylyts.domain.models.BrandModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            when(item){
                is BrandModel -> {
                    text_view_item_constructor_filter_clothe_item_name.text = item.first_name
                }
            }
        }

    }
}