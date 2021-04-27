package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_collection_filter.view.*
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionFilterCustomHolder(itemView: View,adapter: BaseAdapter): BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        item as CollectionFilterModel

        with(itemView){
            item_collection_filter_title_text_view.text = item.name
        }
    }
}