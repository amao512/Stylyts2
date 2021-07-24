package kz.eztech.stylyts.presentation.adapters.search

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.search.holders.ShopSearchHolder

class ShopsSearchAdapter : BaseAdapter() {

//    override fun getLayoutId(viewType: Int): Int = R.layout.item_shop_search

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as UserModel).id ==
                        (list[newItemPosition] as UserModel).id
            }
        }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ShopSearchHolder(
            itemView = inflateView(parent, R.layout.item_shop_search),
            adapter = this
        )
    }
}