package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_comment.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class CommentHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val imageLoader: DomainImageLoader
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
    ) {
        commentWithFullNameTextView.text = HtmlCompat.fromHtml(
            commentWithFullNameTextView.context.getString(
                R.string.comment_text_with_user_text_format,
                comment.author.firstName,
                comment.author.lastName,
                comment.text
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        if (comment.author.avatar.isBlank()) {
            userShortNameTextView.text = getShortName(
                firstName = comment.author.firstName,
                lastName = comment.author.lastName
            )
            userAvatarShapeableImageView.hide()
        } else {
            imageLoader.load(
                url = comment.author.avatar,
                target = userAvatarShapeableImageView
            )
            userShortNameTextView.hide()
        }

        commentDateTextView.text = DateFormatterHelper.formatISO_8601(
            comment.createdAt,
            DateFormatterHelper.FORMAT_DATE_DD_MMMM
        )

        userAvatarShapeableImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, comment)
        }

        userShortNameTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, comment)
        }
    }
}