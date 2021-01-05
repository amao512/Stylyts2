package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_collection_filter.view.*
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionFilterHolder(itemView: View,adapter: BaseAdapter): BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        item as CollectionFilterModel

        with(itemView){
            text_view_item_collection_filter.text = item.name
        }
    }
}