package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.CollectionConstructorShopCategoryHolder
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopCategoryAdapter(
	private val gender: Int
): BaseAdapter() {

	override fun getItemViewType(position: Int): Int {
		return when ((currentList[position] as ClothesTypeModel).isWiden) {
			true -> 1
			false -> 0
		}
	}

	override fun getLayoutId(viewType: Int): Int {
		return when (viewType) {
			1 -> R.layout.item_collection_constructor_category_wide
			else -> R.layout.item_collection_constructor_category
		}
	}
	
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

	fun onExpanded() {
		currentList.map {
			it as ClothesTypeModel

			if (it.isExternal) {
				it.isWiden = false
			}

			notifyDataSetChanged()
		}
	}

	fun onCollapsed() {
		currentList.map {
			it as ClothesTypeModel

			if (it.isExternal) {
				it.isWiden = true
			}

			notifyDataSetChanged()
		}
	}
}