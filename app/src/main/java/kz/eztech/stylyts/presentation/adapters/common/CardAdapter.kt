package kz.eztech.stylyts.presentation.adapters.common

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.card.CardEntity
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.common.holders.CardHolder

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class CardAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as CardEntity).id ==
                        (list[newItemPosition] as CardEntity).id
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return CardHolder(
            itemView = inflateView(parent, R.layout.item_user_card),
            adapter = this
        )
    }
}