package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.ordering.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders.DeliveryWayHolder

class DeliveryWayAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as DeliveryWayModel).id ==
                        (list[newItemPosition] as DeliveryWayModel).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return DeliveryWayHolder(
            itemView = inflateView(parent, R.layout.item_ordering_way),
            adapter = this
        )
    }
}