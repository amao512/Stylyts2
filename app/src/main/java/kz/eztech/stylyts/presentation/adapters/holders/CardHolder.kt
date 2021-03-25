package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_user_card.view.*
import kz.eztech.stylyts.data.db.entities.CardEntity
import kz.eztech.stylyts.presentation.adapters.BaseAdapter

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class CardHolder (itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as CardEntity
        itemView.apply {
            text_view_card_profile_card_name.text = "Debit Mastercard(${item.number?.takeLast(4)})"
            text_view_card_profile_holder.text = item.name_holder
            text_view_card_expiring.text =  "Истекает: ${item.exp_date}"
            linear_layout_card_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }
}