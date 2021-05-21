package kz.eztech.stylyts.presentation.adapters.shop.holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_filter_character.view.*
import kotlinx.android.synthetic.main.item_shop.view.*
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class ShopHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var shopRootViewLinearLayout: LinearLayout
    private lateinit var shopNameTextView: TextView
    private lateinit var favoriteButtonImageView: ImageView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ShopListItem

        when (item.item) {
            is UserModel -> {
                initializeViews()
                processShop(shop = item, position)
            }
            is String -> processCharacter(character = item.item)
        }
    }

    private fun initializeViews() {
        with (itemView) {
            shopRootViewLinearLayout = item_shop_root_view_linear_layout
            shopNameTextView = item_shop_name_text_view
            favoriteButtonImageView = item_shop_favorite_image_view
        }
    }

    private fun processShop(shop: ShopListItem, position: Int) {
        shopNameTextView.text = (shop.item as UserModel).username

        shopRootViewLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, shop)
        }
    }

    private fun processCharacter(character: String) {
        with (itemView) {
            item_filter_character_text_view.text = character
        }
    }
}