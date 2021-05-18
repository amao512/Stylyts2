package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.common.PhotoLibraryModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.PhotoLibraryHolder

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class PhotoLibraryAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_photo_library

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {

            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as PhotoLibraryModel).id ==
                        (list[newItemPosition] as PhotoLibraryModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return PhotoLibraryHolder(
            itemView = view,
            adapter = this
        )
    }

    fun enableMultipleChoice() {
        currentList.map {
            (it as PhotoLibraryModel).isMultipleChoice = true
        }

        notifyDataSetChanged()
    }

    fun disableMultipleChoice() {
        currentList.map {
            (it as PhotoLibraryModel).isMultipleChoice = false
        }

        notifyDataSetChanged()
    }

    fun setNumber(
        position: Int,
        payload: Int
    ) {
        notifyItemChanged(position, payload)
    }
}