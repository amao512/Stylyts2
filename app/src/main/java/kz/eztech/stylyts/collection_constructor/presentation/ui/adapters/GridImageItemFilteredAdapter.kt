package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders.GridImageItemFilteredViewHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageItemFilteredAdapter : BaseAdapter(){

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesModel).id ==
                        (list[newItemPosition] as ClothesModel).id
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return GridImageItemFilteredViewHolder(
            itemView = inflateView(parent, R.layout.item_collection),
            adapter = this
        )
    }
}