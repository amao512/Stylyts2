package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.StyleHolder
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class StyleAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_style

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return StyleHolder(itemView = view, adapter = this)
    }
}