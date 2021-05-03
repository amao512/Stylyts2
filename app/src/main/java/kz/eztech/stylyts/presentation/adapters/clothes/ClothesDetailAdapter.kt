package kz.eztech.stylyts.presentation.adapters.clothes

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ClothesDetailAdapter: BaseAdapter() {

	override fun getLayoutId(viewType: Int): Int = R.layout.item_clothes_detail
	
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
		return ClothesDetailViewHolder(view,this)
	}
}