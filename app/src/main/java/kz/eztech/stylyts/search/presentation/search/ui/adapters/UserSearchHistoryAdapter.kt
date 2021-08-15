package kz.eztech.stylyts.search.presentation.search.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.data.db.search.UserSearchEntity
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.UserSearchHolder

/**
 * Created by Asylzhan Seytbek on 19.03.2021.
 */
class UserSearchHistoryAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as UserSearchEntity).id ==
                        (list[newItemPosition] as UserSearchEntity).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return UserSearchHolder(
            itemView = inflateView(parent, R.layout.item_user_info),
            adapter = this
        )
    }
}