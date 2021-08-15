package kz.eztech.stylyts.search.presentation.shop.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.search.presentation.shop.ui.adapters.holders.ShopCategoryHolder

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryAdapter(
    private val gender: Int
): BaseAdapter(){

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

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ShopCategoryHolder(
            itemView = inflateView(parent, R.layout.item_shop_category),
            adapter = this,
            gender = gender
        )
    }
}