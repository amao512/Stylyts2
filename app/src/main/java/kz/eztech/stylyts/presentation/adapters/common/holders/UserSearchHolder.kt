package kz.eztech.stylyts.presentation.adapters.common.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_info.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 21.02.2021.
 */
class UserSearchHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        when (item) {
            is UserModel -> processUserModel(item, position)
            is UserSearchEntity -> processUserFromHistory(item, position)
        }
    }

    private fun processUserModel(
        user: UserModel,
        position: Int
    ) {
        displayAvatar(
            avatar = user.avatar,
            firstName = user.firstName,
            lastName = user.lastName
        )

        displayUserInfo(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username
        )

        with (itemView) {
            linear_layout_item_user_info_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, user)
            }
        }
    }

    private fun processUserFromHistory(
        user: UserSearchEntity,
        position: Int
    ) {
        displayAvatar(
            avatar = user.avatar,
            firstName = user.name,
            lastName = user.lastName
        )

        displayUserInfo(
            firstName = user.name,
            lastName = user.lastName,
            username = user.username
        )

        with (itemView) {
            item_user_info_remove_image_view.show()

            linear_layout_item_user_info_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, user)
            }

            item_user_info_remove_image_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, user)
            }
        }
    }

    private fun displayAvatar(
        avatar: String?,
        firstName: String?,
        lastName: String?
    ) {
        with(itemView) {
            avatar?.let {
                if (it.isBlank()) {
                    item_user_info_user_avatar_shapeable_image_view.hide()
                    item_user_info_user_short_name_text_view.show()
                    item_user_info_user_short_name_text_view.text = getShortName(firstName, lastName)
                } else {
                    item_user_info_user_avatar_shapeable_image_view.show()
                    item_user_info_user_short_name_text_view.hide()

                    Glide.with(context)
                        .load(it)
                        .centerCrop()
                        .into(item_user_info_user_avatar_shapeable_image_view)
                }
            } ?: run {
                item_user_info_user_avatar_shapeable_image_view.hide()
                item_user_info_user_short_name_text_view.show()
                item_user_info_user_short_name_text_view.text = getShortName(firstName, lastName)
            }
        }
    }

    private fun displayUserInfo(
        firstName: String?,
        lastName: String?,
        username: String?
    ) {
        with(itemView) {
            item_user_info_username_text_view.text = username
            item_user_info_full_name_text_view.text = context.getString(
                R.string.full_name_text_format,
                firstName,
                lastName
            )
        }
    }
}