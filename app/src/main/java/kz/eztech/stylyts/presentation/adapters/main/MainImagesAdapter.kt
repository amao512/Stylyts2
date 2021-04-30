package kz.eztech.stylyts.presentation.adapters.main

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.MainImageHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImagesAdapter(
    private val ownId: Int
) : BaseAdapter(){

    override fun getLayoutId(): Int = R.layout.item_main_image

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

    override fun getViewHolder(view: View): MainImageHolder {
        return MainImageHolder(
            itemView = view,
            adapter = this,
            ownId = ownId
        )
    }

    fun setLikePost(
        isLiked: Boolean,
        id: Int
    ) {
        val post = currentList.find { (it as PostModel).id == id }

        (post as PostModel).alreadyLiked = isLiked

        notifyDataSetChanged()
    }
}