package kz.eztech.stylyts.presentation.adapters.search.holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_shop_search.view.*
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

class ShopSearchHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var shopItemLinearLayout: LinearLayout
    private lateinit var shopNameTextView: TextView
    private lateinit var favoriteImageView: ImageView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as UserModel

        initializeViews()
        processShop(item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            shopItemLinearLayout = item_shop_root_view_linear_layout
            shopNameTextView = item_shop_search_shop_name_text_view
            favoriteImageView = item_shop_search_favorite_image_view
        }
    }

    private fun processShop(
        shop: UserModel,
        position: Int
    ) {
        shopNameTextView.text = shop.username

        shopItemLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, shop)
        }

        favoriteImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, shop)
        }
    }
}