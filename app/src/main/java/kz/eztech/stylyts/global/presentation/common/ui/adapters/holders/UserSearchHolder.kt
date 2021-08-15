package kz.eztech.stylyts.global.presentation.common.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_user_info.view.*
import kz.eztech.stylyts.global.data.db.search.UserSearchEntity
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show

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
            shortName = user.displayShortName
        )

        displayUserInfo(
            fullName = user.displayFullName,
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
            shortName = user.displayShortName
        )

        displayUserInfo(
            fullName = user.displayFullName,
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
        shortName: String
    ) {
        with(itemView) {
            avatar?.let {
                if (it.isBlank()) {
                    item_user_info_user_avatar_shapeable_image_view.hide()
                    item_user_info_user_short_name_text_view.show()
                    item_user_info_user_short_name_text_view.text = shortName
                } else {
                    item_user_info_user_avatar_shapeable_image_view.show()
                    item_user_info_user_short_name_text_view.hide()

                    it.loadImageWithCenterCrop(target = item_user_info_user_avatar_shapeable_image_view)
                }
            } ?: run {
                item_user_info_user_avatar_shapeable_image_view.hide()
                item_user_info_user_short_name_text_view.show()
                item_user_info_user_short_name_text_view.text = shortName
            }
        }
    }

    private fun displayUserInfo(
        fullName: String?,
        username: String?
    ) {
        with(itemView) {
            item_user_info_username_text_view.text = username
            item_user_info_full_name_text_view.text = fullName
        }
    }
}