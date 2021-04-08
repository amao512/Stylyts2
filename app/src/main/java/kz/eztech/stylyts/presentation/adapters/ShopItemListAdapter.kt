package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.ShopItemListHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopItemListAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_shop_item_list

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesCategoryModel).id ==
                        (list[newItemPosition] as ClothesCategoryModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopItemListHolder(view, this)
    }
}