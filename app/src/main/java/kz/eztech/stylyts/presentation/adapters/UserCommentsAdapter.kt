package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.domain.models.UserComment
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.UserCommentHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserCommentsAdapter:BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_user_comment
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as UserComment).id ==
                        (list[newItemPosition] as UserComment).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return UserCommentHolder(view,this)
    }
}