package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.constructor.domain.models.ClothesTypes
import kz.eztech.stylyts.common.presentation.adapters.holders.ShopItemListHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopItemListAdapter: BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_shop_item_list
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesTypes).id ==
                        (list[newItemPosition] as ClothesTypes).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopItemListHolder(view,this)
    }
}