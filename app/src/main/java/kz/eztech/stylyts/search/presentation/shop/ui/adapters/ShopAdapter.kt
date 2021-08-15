package kz.eztech.stylyts.search.presentation.shop.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.search.presentation.shop.data.models.ShopListItem
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders.CharacterFilterCheckViewHolder
import kz.eztech.stylyts.search.presentation.shop.ui.adapters.holders.ShopHolder

class ShopAdapter : BaseAdapter() {

    companion object {
        private const val CHARACTER_TYPE = 0
        private const val SHOP_TYPE = 1
    }

//    override fun getLayoutId(viewType: Int): Int {
//        return when (viewType) {
//            CHARACTER_TYPE -> R.layout.item_filter_character
//            else -> R.layout.item_shop
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return when ((currentList[position] as ShopListItem).item) {
            is String -> CHARACTER_TYPE
            else -> SHOP_TYPE
        }
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ShopListItem).id ==
                        (list[newItemPosition] as ShopListItem).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            CHARACTER_TYPE -> CharacterFilterCheckViewHolder(
                itemView = inflateView(parent, R.layout.item_filter_character),
                adapter = this
            )
            else -> ShopHolder(
                itemView = inflateView(parent, R.layout.item_shop),
                adapter = this
            )
        }
    }
}