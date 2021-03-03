package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_user_info.view.*
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 21.02.2021.
 */
class UserSearchHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter)  {
	override fun bindData(item: Any, position: Int) {
		item as UserSearchModel
		with(itemView){
			item.avatar?.let {
				shapeable_image_view_item_user_info_avatar.visibility = View.VISIBLE
				text_view_item_user_info_short_name.visibility = View.GONE
				Glide.with(context).load(it).into(shapeable_image_view_item_user_info_avatar)
			} ?: run {
				shapeable_image_view_item_user_info_avatar.visibility  =View.GONE
				text_view_item_user_info_short_name.visibility = View.VISIBLE
				
				text_view_item_user_info_short_name.text = "${item.first_name?.toUpperCase()?.get(0)}${item.last_name?.toUpperCase()?.get(0)}"
			}
			text_view_item_user_nickname.text = item.username
			text_view_item_user_info_full_name.text = "${item.first_name} ${item.last_name}"
			linear_layout_item_user_info_container.setOnClickListener {
				adapter.itemClickListener?.onViewClicked(it,position,item)
			}
		}
	}
}