package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CartEntity
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.AddressHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
class AddressAdapter:BaseAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_address_profile
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as AddressEntity).id ==
                        (list[newItemPosition] as AddressEntity).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return AddressHolder(view,this)
    }
}