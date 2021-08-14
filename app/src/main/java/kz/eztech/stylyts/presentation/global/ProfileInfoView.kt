package kz.eztech.stylyts.presentation.global

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show

class ProfileInfoView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private var avatarImageView: ShapeableImageView
    private var userShortNameTextView: TextView
    private var nameTextView: TextView
    private var followersCountTextView: TextView
    private var followingsCountTextView: TextView
    private var followersHolderLinearLayout: LinearLayout
    private var followingsHolderLinearLayout: LinearLayout
    private var publicationsCountTextView: TextView
    private var changeButtonTextView: TextView
    private var followButtonTextView: TextView
    private var unFollowButtonTextView: TextView

    init {
        val view = inflate(context, R.layout.view_profile_info, this)

        avatarImageView = view.findViewById(R.id.view_profile_info_avatar_shapeable_image_view)
        userShortNameTextView = view.findViewById(R.id.view_profile_info_short_name_text_view)
        nameTextView = view.findViewById(R.id.view_profile_info_name_text_view)
        followersCountTextView = view.findViewById(R.id.view_profile_info_followers_count_text_view)
        followingsCountTextView = view.findViewById(R.id.view_profile_info_followings_count_text_view)
        followersHolderLinearLayout = view.findViewById(R.id.view_profile_info_followers_holder_linear_layout)
        followingsHolderLinearLayout = view.findViewById(R.id.view_profile_info_followings_holder_linear_layout)
        publicationsCountTextView = view.findViewById(R.id.view_profile_info_publications_count_text_view)
        changeButtonTextView = view.findViewById(R.id.view_profile_info_edit_text_view)
        followButtonTextView = view.findViewById(R.id.view_profile_info_follow_text_view)
        unFollowButtonTextView = view.findViewById(R.id.view_profile_info_unfollow_text_view)
    }

    fun setUserModel(
        userModel: UserModel,
        isOwnProfile: Boolean
    ) = with(userModel) {
        if (avatar.isBlank()) {
            avatarImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = displayShortName
        } else {
            userShortNameTextView.hide()
            avatar.loadImageWithCenterCrop(target = avatarImageView)
        }

        nameTextView.text = firstName
        followersCountTextView.text = followersCount.toString()
        followingsCountTextView.text = followingsCount.toString()

        if (isOwnProfile) {
            changeButtonTextView.show()
            followButtonTextView.hide()
            unFollowButtonTextView.hide()
        }
    }

    fun setPublicationsCount(count: Int) {
        publicationsCountTextView.text = count.toString()
    }

    fun setButtonStates(
        isOwnProfile: Boolean,
        isAlreadyFollow: Boolean
    ) {
        if (!isOwnProfile) {
            changeButtonTextView.hide()

            if (isAlreadyFollow) {
                unFollowButtonTextView.show()
                followButtonTextView.hide()
            } else {
                unFollowButtonTextView.hide()
                followButtonTextView.show()
            }
        }
    }

    fun setButtonStatesOnSuccessFollowing() {
        changeButtonTextView.hide()
        followButtonTextView.hide()
        unFollowButtonTextView.show()
    }

    fun setButtonStatesOnSuccessUnfollowing(isOwnProfile: Boolean) {
        if (!isOwnProfile) {
            changeButtonTextView.hide()
            followButtonTextView.show()
            unFollowButtonTextView.hide()
        }
    }

    fun onFollowersClickListener(onClick: () -> Unit) {
        followersHolderLinearLayout.setOnClickListener { onClick() }
    }

    fun onFollowingsClickListener(onClick: () -> Unit) {
        followingsHolderLinearLayout.setOnClickListener { onClick() }
    }

    fun onChangeClickListener(onClick: () -> Unit) {
        changeButtonTextView.setOnClickListener { onClick() }
    }

    fun onFollowClickListener(onClick: () -> Unit) {
        followButtonTextView.setOnClickListener { onClick() }
    }

    fun onUnFollowClickListener(onClick: () -> Unit) {
        unFollowButtonTextView.setOnClickListener { onClick() }
    }
}