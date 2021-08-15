package kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_character.view.*
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.search.presentation.shop.data.ShopListItem
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
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