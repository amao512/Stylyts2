package kz.eztech.stylyts.presentation.adapters.common

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.entities.CardEntity
import kz.eztech.stylyts.presentation.adapters.common.holders.CardHolder
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class CardAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_user_card

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return CardHolder(view,this)
    }
}