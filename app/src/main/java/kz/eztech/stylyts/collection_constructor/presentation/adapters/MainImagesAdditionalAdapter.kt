package kz.eztech.stylyts.collection_constructor.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.collection_constructor.presentation.adapters.holders.MainImageAdditionalHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImagesAdditionalAdapter : BaseAdapter(){

    override fun getLayoutId(): Int = R.layout.item_main_image_detail

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

    override fun getViewHolder(view: View): MainImageAdditionalHolder {
        return MainImageAdditionalHolder(
            itemView = view,
            adapter = this
        )
    }
}