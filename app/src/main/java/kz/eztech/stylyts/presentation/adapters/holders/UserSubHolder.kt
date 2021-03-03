package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_user_subs.view.*
import kz.eztech.stylyts.domain.models.UserSub
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as UserSub
        with(itemView){
            text_view_item_user_subs_nickname.text = item.username
            text_view_item_user_sub_short_name.text =  "${item.first_name?.toUpperCase()?.get(0)}${item.last_name?.toUpperCase()?.get(0)}"
            text_view_item_user_subs_name.text = "${item.first_name} ${item.last_name}"
        }
    }
}