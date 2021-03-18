package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.UserSearchHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 21.02.2021.
 */
class UserSearchAdapter: BaseAdapter() {
	override fun getLayoutId(): Int {
		return R.layout.item_user_info
	}
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		return object : BaseDiffUtilCallBack(currentList, list){
			override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
			): Boolean {
				return (currentList[oldItemPosition] as ProfileModel).owner ==
						(list[newItemPosition] as ProfileModel).owner
			}
		}
	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return UserSearchHolder(view,this)
	}
}