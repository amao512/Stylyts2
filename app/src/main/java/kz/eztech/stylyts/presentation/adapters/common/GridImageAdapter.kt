package kz.eztech.stylyts.presentation.adapters.common

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.adapters.common.holders.GridImageViewHolder
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_collection

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
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
        return GridImageViewHolder(view, this)
    }
}