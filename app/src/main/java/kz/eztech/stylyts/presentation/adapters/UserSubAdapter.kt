package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.domain.models.UserSub
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.UserSubHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder


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