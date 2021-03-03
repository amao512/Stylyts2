package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CardEntity
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.AddressHolder
import kz.eztech.stylyts.presentation.adapters.holders.CardHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

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