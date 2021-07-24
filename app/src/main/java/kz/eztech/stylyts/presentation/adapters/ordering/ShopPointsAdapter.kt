package kz.eztech.stylyts.presentation.adapters.ordering

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.ShopPointModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.ordering.holders.ShopPointViewHolder

class ShopPointsAdapter : BaseAdapter() {

//    override fun getLayoutId(viewType: Int): Int = R.layout.item_shop_point

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