package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.GridImageViewHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

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
                return (currentList[oldItemPosition]).hashCode() ==
                        (list[newItemPosition]).hashCode()
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return GridImageViewHolder(view,this)
    }
}