package kz.eztech.stylyts.presentation.adapters.address

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.AddressViewClickListener

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressAdapter(
    private val addressViewClickListener: AddressViewClickListener
) : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_address_profile

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return AddressHolder(
            itemView = view,
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