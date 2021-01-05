package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesTypes
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.CollectionConstructorShopCategoryHolder
import kz.eztech.stylyts.presentation.adapters.holders.CollectionConstructorShopItemHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopCategoryAdapter:BaseAdapter() {

	var isSubCategory = false

	override fun getLayoutId(): Int {
		return R.layout.item_collection_constructor_category_item
	}
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		if(!isSubCategory){
			return object : BaseDiffUtilCallBack(currentList, list){
				override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
				): Boolean {
					return (currentList[oldItemPosition] as GenderCategory).id ==
							(list[newItemPosition] as GenderCategory).id
				}
			}
		}else{
			return object : BaseDiffUtilCallBack(currentList, list){
				override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
				): Boolean {
					return (currentList[oldItemPosition] as ClothesTypes).id ==
							(list[newItemPosition] as ClothesTypes).id
				}
			}
		}

	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return CollectionConstructorShopCategoryHolder(view,this)
	}
}