package kz.eztech.stylyts.profile.presentation.subscriptions.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.profile.presentation.subscriptions.ui.adapters.holders.UserSubHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as FollowerModel).id ==
                        (list[newItemPosition] as FollowerModel).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return UserSubHolder(
            itemView = inflateView(parent, R.layout.item_user_subs),
            adapter = this
        )
    }

    fun setFollowingUser(followerId: Int) {
        val item = currentList.find { (it as FollowerModel).id == followerId } as FollowerModel

        item.isAlreadyFollow = true

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun setUnFollowingUser(followerId: Int) {
        val item = currentList.find { (it as FollowerModel).id == followerId } as FollowerModel

        item.isAlreadyFollow = false

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }
}