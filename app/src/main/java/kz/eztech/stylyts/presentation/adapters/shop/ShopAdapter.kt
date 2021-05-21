package kz.eztech.stylyts.presentation.adapters.shop

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.shop.holders.ShopHolder

class ShopAdapter : BaseAdapter() {

    companion object {
        private const val CHARACTER_TYPE = 0
        private const val SHOP_TYPE = 1
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            CHARACTER_TYPE -> R.layout.item_filter_character
            else -> R.layout.item_shop
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when ((currentList[position] as ShopListItem).item) {
            is String -> CHARACTER_TYPE
            else -> SHOP_TYPE
        }
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ShopListItem).id ==
                        (list[newItemPosition] as ShopListItem).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopHolder(itemView = view, adapter = this)
    }
}