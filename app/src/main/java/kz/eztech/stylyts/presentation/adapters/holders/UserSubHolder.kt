package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_user_subs.view.*
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
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
//            text_view_item_user_sub_short_name.text =
//                "${item.first_name?.toUpperCase()?.get(0)}${item.last_name?.toUpperCase()?.get(0)}"
//            text_view_item_user_subs_name.text = "${item.first_name} ${item.last_name}"

            if (item.isAlreadyFollow) {
                button_item_user_subs_unfollow.show()
                button_item_user_subs_follow.hide()
            } else {
                button_item_user_subs_unfollow.hide()
                button_item_user_subs_follow.show()
            }
        }
    }
}