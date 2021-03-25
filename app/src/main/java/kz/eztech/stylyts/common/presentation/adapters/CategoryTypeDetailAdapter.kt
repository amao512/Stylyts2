package kz.eztech.stylyts.common.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.collection_constructor.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.common.presentation.adapters.holders.CategoryTypeDetailHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailAdapter: BaseAdapter() {
	override fun getLayoutId(): Int {
		return R.layout.item_category_type_detail
	}
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		return object : BaseDiffUtilCallBack(currentList, list){
			override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
			): Boolean {
				return (currentList[oldItemPosition] as ClothesTypeDataModel).id ==
						(list[newItemPosition] as ClothesTypeDataModel).id
			}
		}
	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return CategoryTypeDetailHolder(view,this)
	}
}