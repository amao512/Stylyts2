package kz.eztech.stylyts.presentation.adapters.ordering

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.DeliveryConditionModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.ordering.holders.DeliveryConditionViewHolder

class DeliveryConditionAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_delivery_condition

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as DeliveryConditionModel).id ==
                        (list[newItemPosition] as DeliveryConditionModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return DeliveryConditionViewHolder(view, adapter = this)
    }
}