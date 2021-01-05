package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.CollectionConstructorShopItemHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemAdapter:BaseAdapter() {
	override fun getLayoutId(): Int {
		return R.layout.item_collection_constructor_category_item
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
		return CollectionConstructorShopItemHolder(view,this)
	}
}