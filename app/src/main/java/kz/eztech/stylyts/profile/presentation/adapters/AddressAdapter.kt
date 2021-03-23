package kz.eztech.stylyts.profile.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.profile.domain.models.AddressModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.profile.presentation.interfaces.AddressViewClickListener
import kz.eztech.stylyts.profile.presentation.adapters.holders.AddressHolder

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressAdapter(
    private val addressViewClickListener: AddressViewClickListener
) : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_address_profile

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