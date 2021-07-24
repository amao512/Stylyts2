package kz.eztech.stylyts.presentation.adapters.common

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.common.holders.GridImageViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageAdapter : BaseAdapter() {

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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return GridImageViewHolder(
            itemView = inflateView(parent, R.layout.item_collection),
            adapter = this
        )
    }
}