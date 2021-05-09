package kz.eztech.stylyts.presentation.adapters.collection

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.collection.holders.CollectionFilterHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionsFilterAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_collection_filter

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as CollectionFilterModel).name ==
                        (list[newItemPosition] as CollectionFilterModel).name
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return CollectionFilterHolder(
            itemView = view,
            adapter = this
        )
    }

    fun onChooseItem(position: Int) {
        currentList.forEach {
            it as CollectionFilterModel

            it.isChosen = it.id == (currentList[position] as CollectionFilterModel).id
        }

        (currentList[0] as CollectionFilterModel).isDisabled = position != 3

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun changeItemByPosition(
        position: Int,
        title: String
    ) {
        val item = currentList[position] as CollectionFilterModel
        item.name = title

        notifyDataSetChanged()
    }
}