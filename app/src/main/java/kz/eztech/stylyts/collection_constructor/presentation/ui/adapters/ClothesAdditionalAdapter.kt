package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders.ClothesAdditionalHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.common.interfaces.ItemTouchHelperAdapter
import kz.eztech.stylyts.global.presentation.common.interfaces.OnStartDragListener

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class ClothesAdditionalAdapter(
    private val onStartDragListener: OnStartDragListener? = null
) : BaseAdapter(), ItemTouchHelperAdapter {

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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return ClothesAdditionalHolder(
            itemView = inflateView(parent, R.layout.item_main_image_detail),
            adapter = this,
            onStartDragListener = onStartDragListener
        )
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val moveItem = currentList[fromPosition]
        val item = currentList[toPosition]

        currentList[toPosition] = moveItem
        currentList[fromPosition] = item

        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {}

    fun getClothesList(): List<ClothesModel> {
        val clothesList: MutableList<ClothesModel> = mutableListOf()

        currentList.map {
            clothesList.add(it as ClothesModel)
        }

        return clothesList
    }
}