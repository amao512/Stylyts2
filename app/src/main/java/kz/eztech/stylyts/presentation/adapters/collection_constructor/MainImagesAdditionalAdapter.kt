package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.MainImageAdditionalHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImagesAdditionalAdapter : BaseAdapter(){

    override fun getLayoutId(viewType: Int): Int = R.layout.item_main_image_detail

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

    override fun getViewHolder(view: View): MainImageAdditionalHolder {
        return MainImageAdditionalHolder(
            itemView = view,
            adapter = this
        )
    }
}