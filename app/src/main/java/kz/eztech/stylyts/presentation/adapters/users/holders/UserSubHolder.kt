package kz.eztech.stylyts.presentation.adapters.users.holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_user_subs.view.*
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubHolder(
    itemView: View,
    adapter: BaseAdapter
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
        processFollower(followerModel = item)
        initializeListeners(followerModel = item, position = position)

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

    private fun processFollower(followerModel: FollowerModel) = with (followerModel) {
        usernameTextView.text = followerModel.username
        userFullNameTextView.text = displayFullName

        if (avatar.isBlank()) {
            userShortNameTextView.text = displayShortName
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
        } else {
            avatar.loadImageWithCenterCrop(target = avatarShapeableImageView)
            avatarShapeableImageView.show()
            userShortNameTextView.hide()
        }

        if (isAlreadyFollow) {
            unFollowTextView.show()
            followTextView.hide()
        } else {
            unFollowTextView.hide()
            followTextView.show()
        }
    }
}