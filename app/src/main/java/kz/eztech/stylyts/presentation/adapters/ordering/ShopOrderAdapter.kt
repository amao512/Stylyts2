package kz.eztech.stylyts.presentation.adapters.ordering

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.ordering.holders.ShopOrderHolder

class ShopOrderAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_shop_order

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopOrderHolder(
            itemView = view,
            adapter = this
        )
    }
}