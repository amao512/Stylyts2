package kz.eztech.stylyts.profile.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.data.db.entities.CardEntity
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.profile.presentation.adapters.holders.CardHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class CardAdapter : BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_user_card
    }

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