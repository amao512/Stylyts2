package kz.eztech.stylyts.presentation.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.UserSubHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubAdapter(
    private val imageLoader: DomainImageLoader
): BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_user_subs

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return UserSubHolder(
            itemView = view,
            adapter = this,
            imageLoader = imageLoader
        )
    }

    fun setFollowingUser(followerId: Int) {
        currentList.forEach {
            it as FollowerModel

            if (it.id == followerId) {
                it.isAlreadyFollow = true

                return
            }
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun setUnFollowingUser(followerId: Int) {
        currentList.forEach {
            it as FollowerModel

            if (it.id == followerId) {
                it.isAlreadyFollow = false
            }
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }
}