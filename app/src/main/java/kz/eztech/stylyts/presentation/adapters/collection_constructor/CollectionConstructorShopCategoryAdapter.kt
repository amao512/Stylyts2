package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.CollectionConstructorShopCategoryHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopCategoryAdapter(
	private val gender: Int
): BaseAdapter() {

	override fun getLayoutId(): Int = R.layout.item_collection_constructor_category_item
	
	override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
		return object : BaseDiffUtilCallBack(currentList, list){
			override fun getAreContentsTheSame(
					oldItemPosition: Int,
					newItemPosition: Int
			): Boolean {
				return (currentList[oldItemPosition] as ClothesTypeModel).id ==
						(list[newItemPosition] as ClothesTypeModel).id
			}
		}
	}
	
	override fun getViewHolder(view: View): BaseViewHolder {
		return CollectionConstructorShopCategoryHolder(
			itemView = view,
			adapter = this,
			gender = gender
		)
	}

	fun removeChosenPosition(typeId: Int) {
		currentList.map {
			it as ClothesTypeModel

			if (typeId == it.id) {
				it.isChoosen = false
			}
		}

		val diffCallback = getDiffUtilCallBack(currentList)
		val diffResult = DiffUtil.calculateDiff(diffCallback)
		diffResult.dispatchUpdatesTo(this)

		notifyDataSetChanged()
	}
}