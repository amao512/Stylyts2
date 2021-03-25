package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.item_user_comment.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.UserComment
import kz.eztech.stylyts.presentation.adapters.BaseAdapter

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserCommentHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as UserComment
        itemView.apply {
            text_view_item_user_nickname_and_text.text = HtmlCompat.fromHtml(context.getString(R.string.item_user_comment,item.first_name,item.last_name,item.text),HtmlCompat.FROM_HTML_MODE_LEGACY)
            text_view_item_user_comment_short_name.text =  "${item.first_name?.toUpperCase()?.get(0)}${item.last_name?.toUpperCase()?.get(0)}"
            text_view_item_user_comment_date.text = item.date
        }
    }
}