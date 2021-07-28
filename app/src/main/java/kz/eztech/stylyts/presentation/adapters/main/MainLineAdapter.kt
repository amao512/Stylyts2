package kz.eztech.stylyts.presentation.adapters.main

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.main.holders.MainLineHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainLineAdapter : BaseAdapter(){

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return currentList[oldItemPosition].hashCode() == list[newItemPosition].hashCode()
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return MainLineHolder(
            itemView = inflateView(parent, R.layout.item_main_line),
            adapter = this
        )
    }

    fun setLikePost(
        isLiked: Boolean,
        id: Int
    ) {
        val post = currentList.find { (it as PostModel).id == id }

        post as PostModel

        if (isLiked) {
            post.likesCount++
        } else {
            post.likesCount--
        }

        post.alreadyLiked = isLiked

        notifyDataSetChanged()
    }
}