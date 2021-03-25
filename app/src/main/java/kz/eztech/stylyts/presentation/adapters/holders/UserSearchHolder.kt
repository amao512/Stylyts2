package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_info.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
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
        with(itemView) {
            when (item) {
                is UserModel -> {
                    displayAvatar(
                        avatar = item.avatar,
                        name = item.name,
                        lastName = item.lastName
                    )

                    displayUserInfo(
                        name = item.name,
                        lastName = item.lastName,
                        username = item.username
                    )

                    linear_layout_item_user_info_container.setOnClickListener {
                        adapter.itemClickListener?.onViewClicked(it, position, item)
                    }
                }
                is UserSearchEntity -> {
                    displayAvatar(
                        avatar = item.avatar,
                        name = item.name,
                        lastName = item.lastName
                    )

                    displayUserInfo(
                        name = item.name,
                        lastName = item.lastName,
                        username = item.username
                    )

                    item_user_info_remove_image_view.show()

                    linear_layout_item_user_info_container.setOnClickListener {
                        adapter.itemClickListener?.onViewClicked(it, position, item)
                    }

                    item_user_info_remove_image_view.setOnClickListener {
                        adapter.itemClickListener?.onViewClicked(it, position, item)
                    }
                }
            }
        }
    }

    private fun displayAvatar(
        avatar: String?,
        name: String?,
        lastName: String?
    ) {
        with(itemView) {
            avatar?.let {
                item_user_info_user_avatar_shapeable_image_view.show()
                item_user_info_user_short_name_text_view.hide()
                Glide.with(context).load(it).into(item_user_info_user_avatar_shapeable_image_view)
            } ?: run {
                item_user_info_user_avatar_shapeable_image_view.hide()
                item_user_info_user_short_name_text_view.show()

                item_user_info_user_short_name_text_view.text = getShortName(name, lastName)
            }
        }
    }

    private fun displayUserInfo(
        name: String?,
        lastName: String?,
        username: String?
    ) {
        with(itemView) {
            item_user_info_username_text_view.text = username
            item_user_info_full_name_text_view.text = context.getString(
                R.string.full_name_text_format,
                name,
                lastName
            )
        }
    }
}