package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders.StyleHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

class StyleAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesStyleModel).id ==
                        (list[newItemPosition] as ClothesStyleModel).id
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return StyleHolder(
            itemView = inflateView(parent, R.layout.item_style),
            adapter = this
        )
    }
}