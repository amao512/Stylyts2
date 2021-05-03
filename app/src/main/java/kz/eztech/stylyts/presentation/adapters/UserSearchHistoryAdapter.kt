package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.presentation.adapters.holders.UserSearchHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Asylzhan Seytbek on 19.03.2021.
 */
class UserSearchHistoryAdapter: BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_user_info

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as UserSearchEntity).id ==
                            (list[newItemPosition] as UserSearchEntity).id
                }
            }
        }

    override fun getViewHolder(view: View): BaseViewHolder {
        return UserSearchHolder(view,this)
    }
}