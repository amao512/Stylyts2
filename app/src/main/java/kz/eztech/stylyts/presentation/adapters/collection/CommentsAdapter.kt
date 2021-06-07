package kz.eztech.stylyts.presentation.adapters.collection

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.adapters.collection.holders.CommentHolder
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class CommentsAdapter : BaseAdapter() {

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
            adapter = this
        )
    }
}