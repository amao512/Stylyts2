package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_subs.view.*
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
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as FollowerModel

        with(itemView) {
            text_view_item_user_subs_nickname.text = item.username
            text_view_item_user_subs_name.text = SPACE_TEXT_FORMAT.format(
                item.firstName,
                item.firstName
            )

            if (item.avatar.isBlank()) {
                text_view_item_user_sub_short_name.text = getShortName(
                    firstName = item.firstName,
                    lastName = item.lastName
                )
            } else {
                Glide.with(shapeable_image_view_item_user_subs_avatar.context)
                    .load(item.avatar)
                    .centerCrop()
                    .into(shapeable_image_view_item_user_subs_avatar)
            }

            if (item.isAlreadyFollow) {
                button_item_user_subs_unfollow.show()
                button_item_user_subs_follow.hide()
            } else {
                button_item_user_subs_unfollow.hide()
                button_item_user_subs_follow.show()
            }

            linear_layout_item_user_info_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }

            button_item_user_subs_unfollow.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }

            button_item_user_subs_follow.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }

    companion object {
        private const val SPACE_TEXT_FORMAT = "%s %s"
    }
}