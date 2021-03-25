package kz.eztech.stylyts.presentation.adapters.collection

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection.holders.GridImageCollectionItemViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageCollectionItemAdapter : BaseAdapter(){
    override fun getLayoutId(): Int {
        return R.layout.item_collection_image
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as MainResult).id ==
                        (list[newItemPosition] as MainResult).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return GridImageCollectionItemViewHolder(view,this)
    }
}