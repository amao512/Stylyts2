package kz.eztech.stylyts.ordering.presentation.cart.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartAdapter : BaseAdapter() {

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

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return CartHolder(
            itemView = inflateView(parent, R.layout.item_cart_item),
            adapter = this
        )
    }

//    override fun getViewHolder(view: View): BaseViewHolder {
//        return CartHolder(
//            itemView = view,
//            adapter = this
//        )
//    }
}