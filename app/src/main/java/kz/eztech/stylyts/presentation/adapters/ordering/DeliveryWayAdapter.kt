package kz.eztech.stylyts.presentation.adapters.ordering

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.ordering.holders.DeliveryWayHolder

class DeliveryWayAdapter : BaseAdapter() {

//    override fun getLayoutId(viewType: Int): Int = R.layout.item_ordering_way

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