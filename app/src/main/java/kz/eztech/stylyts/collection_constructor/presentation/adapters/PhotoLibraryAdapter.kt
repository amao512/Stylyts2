package kz.eztech.stylyts.collection_constructor.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.collection_constructor.presentation.adapters.holders.PhotoLibraryHolder

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class PhotoLibraryAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_photo_library

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {

            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as String) ==
                        (list[newItemPosition] as String)
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return PhotoLibraryHolder(
            itemView = view,
            adapter = this
        )
    }
}