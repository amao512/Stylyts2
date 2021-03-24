package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.BrandModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.CollectionConstructorSubFilterHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 09.02.2021.
 */

class CollectionConstructorSubFilterAdapter: BaseAdapter() {
	override fun getLayoutId(): Int {
		return R.layout.item_constructor_filter_clothe_sub_items
	}
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		when(list[0]){
			is BrandModel -> {
				return object : BaseDiffUtilCallBack(currentList, list){
					override fun getAreContentsTheSame(
							oldItemPosition: Int,
							newItemPosition: Int
					): Boolean {
						return (currentList[oldItemPosition] as BrandModel).id ==
								(list[newItemPosition] as BrandModel).id
					}
				}
			}
			else -> {
				return object : BaseDiffUtilCallBack(currentList, list){
					override fun getAreContentsTheSame(
							oldItemPosition: Int,
							newItemPosition: Int
					): Boolean {
						return (currentList[oldItemPosition]).hashCode() ==
								(list[newItemPosition]).hashCode()
					}
				}
			}
		}
	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return CollectionConstructorSubFilterHolder(view,this)
	}
}