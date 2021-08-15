package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders.CollectionConstructorShopItemHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewDoubleClickListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemAdapter : BaseAdapter() {

    var itemDoubleClickListener: UniversalViewDoubleClickListener? = null

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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return CollectionConstructorShopItemHolder(
            itemView = inflateView(parent, R.layout.item_collection_constructor_clothes),
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