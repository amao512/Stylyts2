package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.GridImageViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageAdapter : BaseAdapter(){
    override fun getLayoutId(): Int {
        return R.layout.item_collection_image
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as MainImageModel).name ==
                        (list[newItemPosition] as MainImageModel).name
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return GridImageViewHolder(view,this)
    }
}