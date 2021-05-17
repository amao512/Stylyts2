package kz.eztech.stylyts.presentation.adapters.main

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.main.holders.MainLineHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainLineAdapter(
    private val ownId: Int,
    private val imageLoader: DomainImageLoader
) : BaseAdapter(){

    override fun getLayoutId(viewType: Int): Int = R.layout.item_main_line

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

    override fun getViewHolder(view: View): MainLineHolder {
        return MainLineHolder(
            itemView = view,
            adapter = this,
            ownId = ownId,
            imageLoader = imageLoader
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