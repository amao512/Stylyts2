package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.create_outfit.domain.models.GenderCategory
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.ShopCategoryHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryAdapter: BaseAdapter(){

    override fun getLayoutId(): Int {
        return R.layout.item_shop_category
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as GenderCategory).id ==
                        (list[newItemPosition] as GenderCategory).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopCategoryHolder(view,this)
    }
}