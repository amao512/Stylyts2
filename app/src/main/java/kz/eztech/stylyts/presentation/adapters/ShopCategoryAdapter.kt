package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.ShopCategoryHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryAdapter: BaseAdapter(){

    override fun getLayoutId(): Int {
        return R.layout.item_shop_catergory
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