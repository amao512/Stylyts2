package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_user_subs.view.*
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val imageLoader: DomainImageLoader
) : BaseViewHolder(itemView, adapter) {

    private lateinit var followerItemLinearLayout: LinearLayout
    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var userFullNameTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var unFollowTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as FollowerModel

        initializeViews()
        initializeListeners(followerModel = item, position = position)
        processFollower(followerModel = item)

    }

    private fun initializeViews() {
        with(itemView) {
            followerItemLinearLayout = linear_layout_item_user_info_container
            avatarShapeableImageView = shapeable_image_view_item_user_subs_avatar
            userShortNameTextView = text_view_item_user_sub_short_name
            usernameTextView = text_view_item_user_subs_nickname
            userFullNameTextView = text_view_item_user_subs_name
            followTextView = button_item_user_subs_follow
            unFollowTextView = button_item_user_subs_unfollow
        }
    }

    private fun initializeListeners(followerModel: FollowerModel, position: Int) {
        followerItemLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, followerModel)
        }

        unFollowTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, followerModel)
        }

        followTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, followerModel)
        }
    }

    private fun processFollower(followerModel: FollowerModel) {
        usernameTextView.text = followerModel.username
        userFullNameTextView.text = SPACE_TEXT_FORMAT.format(
            followerModel.firstName,
            followerModel.firstName
        )

        if (followerModel.avatar.isBlank()) {
            userShortNameTextView.text = getShortName(
                firstName = followerModel.firstName,
                lastName = followerModel.lastName
            )
        } else {
            imageLoader.load(
                url = followerModel.avatar,
                target = avatarShapeableImageView
            )
        }

        if (followerModel.isAlreadyFollow) {
            unFollowTextView.show()
            followTextView.hide()
        } else {
            unFollowTextView.hide()
            followTextView.show()
        }
    }

    companion object {
        private const val SPACE_TEXT_FORMAT = "%s %s"
    }
}