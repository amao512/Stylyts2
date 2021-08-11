package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_character.view.*
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.EMPTY_STRING

class CharacterFilterCheckViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        val title = when (item) {
            is FilterCheckModel -> item.item
            is ShopListItem -> item.item
            else -> EMPTY_STRING
        }

        processCharacter(title as String)
    }

    private fun processCharacter(character: String) {
        with (itemView) {
            item_filter_character_text_view.text = character
        }
    }
}