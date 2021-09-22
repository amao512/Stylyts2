package kz.eztech.stylyts.ordering.presentation.order.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.order.ui.adapters.holders.ShopOrderViewHolder

class ShopOrderAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return currentList[oldItemPosition].hashCode() ==
                        list[newItemPosition].hashCode()
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ShopOrderViewHolder(
            itemView = inflateView(parent, R.layout.item_shop_order),
            adapter = this
        )
    }
}