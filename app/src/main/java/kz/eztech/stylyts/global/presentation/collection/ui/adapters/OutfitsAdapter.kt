package kz.eztech.stylyts.global.presentation.collection.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.presentation.collection.ui.adapters.holders.OutfitViewHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class OutfitsAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as OutfitModel).id ==
                        (list[newItemPosition] as OutfitModel).id
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return OutfitViewHolder(
            itemView = inflateView(parent, R.layout.item_collection),
            adapter = this
        )
    }
}