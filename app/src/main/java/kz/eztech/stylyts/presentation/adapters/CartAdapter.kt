package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.entities.CartEntity
import kz.eztech.stylyts.presentation.adapters.holders.CartHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartAdapter: BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_cart_item
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
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
        return CartHolder(view,this)
    }
}