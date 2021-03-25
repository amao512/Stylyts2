package kz.eztech.stylyts.collection_constructor.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.search.presentation.adapters.UserSearchHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 21.02.2021.
 */
class UserSearchAdapter: BaseAdapter() {

	override fun getLayoutId(): Int = R.layout.item_user_info
	
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