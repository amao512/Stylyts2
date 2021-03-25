package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.GridImageItemFilteredViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageItemFilteredAdapter : BaseAdapter(){

    override fun getLayoutId(): Int = R.layout.item_collection_image

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesMainModel).id ==
                        (list[newItemPosition] as ClothesMainModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return GridImageItemFilteredViewHolder(
            itemView = view,
            adapter = this
        )
    }
}