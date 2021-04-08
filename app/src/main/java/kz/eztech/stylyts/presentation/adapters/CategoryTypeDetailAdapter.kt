package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.CategoryTypeDetailHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailAdapter: BaseAdapter() {

	override fun getLayoutId(): Int = R.layout.item_category_type_detail
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		return object : BaseDiffUtilCallBack(currentList, list){
			override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
			): Boolean {
				return (currentList[oldItemPosition] as ClothesModel).id ==
						(list[newItemPosition] as ClothesModel).id
			}
		}
	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return CategoryTypeDetailHolder(view,this)
	}
}