package kz.eztech.stylyts.presentation.adapters.shop

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.shop.holders.ShopCategoryHolder

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryAdapter(
    private val gender: Int
): BaseAdapter(){

    override fun getLayoutId(viewType: Int): Int = R.layout.item_shop_category

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesTypeModel).id ==
                        (list[newItemPosition] as ClothesTypeModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopCategoryHolder(
            itemView = view,
            adapter = this,
            gender = gender
        )
    }
}