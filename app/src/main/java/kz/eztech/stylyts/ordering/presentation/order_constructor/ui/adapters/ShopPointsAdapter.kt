package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.ordering.domain.models.order.ShopPointModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders.ShopPointViewHolder

class ShopPointsAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ShopPointModel).id ==
                        (list[newItemPosition] as ShopPointModel).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ShopPointViewHolder(
            itemView = inflateView(parent, R.layout.item_shop_point),
            adapter = this
        )
    }

    fun onSelect(
        id: Int,
        isSelected: Boolean
    ) {
        currentList.map {
            it as ShopPointModel

            if (it.id == id) {
                it.isSelected = isSelected
            }
        }
        notifyDataSetChanged()
    }

    fun getItemById(id: Int): ShopPointModel {
        val item = currentList.find {
            it as ShopPointModel

            it.id == id
        }

        return item as ShopPointModel
    }
}