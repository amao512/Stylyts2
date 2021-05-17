package kz.eztech.stylyts.presentation.adapters.ordering

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.ordering.holders.CartHolder
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_cart_item

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as CartEntity).id ==
                        (list[newItemPosition] as CartEntity).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return CartHolder(
            itemView = view,
            adapter = this
        )
    }
}