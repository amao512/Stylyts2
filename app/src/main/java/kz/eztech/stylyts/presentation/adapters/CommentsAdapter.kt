package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.CommentHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class CommentsAdapter(
    private val imageLoader: DomainImageLoader
): BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_comment

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as CommentModel).id ==
                        (list[newItemPosition] as CommentModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return CommentHolder(
            itemView = view,
            adapter = this,
            imageLoader = imageLoader
        )
    }
}