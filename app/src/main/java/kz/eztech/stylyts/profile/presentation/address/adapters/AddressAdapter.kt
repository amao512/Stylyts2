package kz.eztech.stylyts.profile.presentation.address.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.address.AddressModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.profile.presentation.address.interfaces.AddressViewClickListener

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressAdapter(
    private val addressViewClickListener: AddressViewClickListener
) : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as AddressModel).id ==
                        (list[newItemPosition] as AddressModel).id
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return AddressHolder(
            itemView = inflateView(parent, R.layout.item_address_profile),
            adapter = this,
            addressViewClickListener = addressViewClickListener
        )
    }

    fun deleteItem(position: Int) {
        addressViewClickListener.onSwipeDelete(addressModel = currentList[position] as AddressModel)

        currentList.removeAt(position)
        notifyItemRemoved(position)
    }
}