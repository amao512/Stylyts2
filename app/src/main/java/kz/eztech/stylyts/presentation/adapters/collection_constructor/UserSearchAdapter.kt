package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.UserSearchHolder

/**
 * Created by Ruslan Erdenoff on 21.02.2021.
 */
class UserSearchAdapter: BaseAdapter() {

	override fun getLayoutId(viewType: Int): Int = R.layout.item_user_info
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		return object : BaseDiffUtilCallBack(currentList, list){

			override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
			): Boolean {
				return (currentList[oldItemPosition] as UserModel).id ==
							(list[newItemPosition] as UserModel).id
				}
			}
		}

	override fun getViewHolder(view: View): BaseViewHolder {
		return UserSearchHolder(
			itemView = view,
			adapter = this
		)
	}
}