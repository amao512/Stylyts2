package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.UserSub
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.UserSubHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder


/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubAdapter: BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_user_subs
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as UserSub).id ==
                        (list[newItemPosition] as UserSub).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return UserSubHolder(view,this)
    }
}