package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.CollectionConstructorShopItemHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewDoubleClickListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemAdapter : BaseAdapter() {

    var itemDoubleClickListener: UniversalViewDoubleClickListener? = null

    override fun getLayoutId(viewType: Int): Int = R.layout.item_collection_constructor_clothes

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
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
        return CollectionConstructorShopItemHolder(
            itemView = view,
            adapter = this
        )
    }

    fun choosePosition(clothesId: Int) {
        currentList.map {
            it as ClothesModel

            if (clothesId == it.id) {
                it.isChosen = true
            }
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun removeChosenPosition(clothesId: Int) {
        currentList.map {
            it as ClothesModel

            if (clothesId == it.id) {
                it.isChosen = false
            }
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }
}