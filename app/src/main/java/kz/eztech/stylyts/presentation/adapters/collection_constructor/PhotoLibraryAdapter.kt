package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.common.PhotoLibraryModel
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.PhotoLibraryHolder
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class PhotoLibraryAdapter : BaseAdapter() {

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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return PhotoLibraryHolder(
            itemView = inflateView(parent, R.layout.item_photo_library),
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
            it.number = 0
        }

        notifyDataSetChanged()
    }

    fun setNumber(
        position: Int,
        payload: Int
    ) {
        val item = currentList[position] as PhotoLibraryModel
        item.number = payload
        item.isChosen = true

        notifyItemChanged(position, payload)
    }

    fun removeNumber(position: Int) {
        val item = currentList[position] as PhotoLibraryModel

        currentList.map {
            it as PhotoLibraryModel

            if (it.number > item.number) {
                it.number = 0
                it.isChosen = false
            }
        }

        item.number = 0
        item.isChosen = false

        notifyDataSetChanged()
    }

    fun getChosenPhotos(): List<PhotoLibraryModel> {
        val chosenList: MutableList<PhotoLibraryModel> = mutableListOf()

        currentList.map {
            it as PhotoLibraryModel

            if (it.isChosen) {
                chosenList.add(it)
            }
        }

        return chosenList
    }
}