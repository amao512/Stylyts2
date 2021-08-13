package kz.eztech.stylyts.presentation.adapters.collection.holders

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_comment.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.getDayAndMonth
import kz.eztech.stylyts.utils.extensions.getZonedDateTime
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class CommentHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var userAvatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var commentWithFullNameTextView: TextView
    private lateinit var commentDateTextView: TextView

    override fun bindData(item: Any, position: Int) {
        item as CommentModel

        initializeViews()
        processComment(item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            userAvatarShapeableImageView = item_comment_avatar_shapeable_image_view
            userShortNameTextView = item_comment_user_short_name_text_view
            commentWithFullNameTextView = item_comment_text_text_view
            commentDateTextView = item_comment_date_text_view
        }
    }

    private fun processComment(
        comment: CommentModel,
        position: Int
    ) = with (comment) {
        commentWithFullNameTextView.text = HtmlCompat.fromHtml(
            commentWithFullNameTextView.context.getString(
                R.string.comment_text_with_user_text_format,
                author.firstName,
                author.lastName,
                text
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        if (author.avatar.isBlank()) {
            userShortNameTextView.text = author.displayShortName
            userAvatarShapeableImageView.hide()
        } else {
            author.avatar.loadImageWithCenterCrop(target = userAvatarShapeableImageView)
            userShortNameTextView.hide()
        }

        commentDateTextView.text = createdAt.getZonedDateTime().getDayAndMonth()

        userAvatarShapeableImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, comment)
        }

        userShortNameTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, comment)
        }
    }
}