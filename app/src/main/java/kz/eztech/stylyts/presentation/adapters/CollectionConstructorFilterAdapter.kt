package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.BrandModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.CollectionConstructorFilterHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder


/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterAdapter: BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_constructor_filter_clothe_items
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        when(list[0]){
            is BrandModel -> {
                return object : BaseDiffUtilCallBack(currentList, list){
                    override fun getAreContentsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                    ): Boolean {
                        return (currentList[oldItemPosition] as BrandModel).isChosen ==
                                (list[newItemPosition] as BrandModel).isChosen
                    }
                }
            }
            is GenderCategory -> {
                return object : BaseDiffUtilCallBack(currentList, list){
                    override fun getAreContentsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                    ): Boolean {
                        return (currentList[oldItemPosition] as GenderCategory).isChoosen ==
                                (list[newItemPosition] as GenderCategory).isChoosen
                    }
                }
            }
            else -> {
                return object : BaseDiffUtilCallBack(currentList, list){
                    override fun getAreContentsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                    ): Boolean {
                        return (currentList[oldItemPosition]).hashCode() ==
                                (list[newItemPosition]).hashCode()
                    }
                }
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return CollectionConstructorFilterHolder(view,this)
    }
}