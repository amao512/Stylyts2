package kz.eztech.stylyts.collection_constructor.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.CollectionFilterModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.common.presentation.adapters.holders.CollectionFilterHolder
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionsFilterAdapter : BaseAdapter() {

    enum class TYPES(
        val type: Int,
        val desc: String
    ) {
        CUSTOM(type = 0, desc = EMPTY_STRING),
        DEFAULT(type = 1, desc = EMPTY_STRING)
    }


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

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            TYPES.CUSTOM.type
        } else {
            TYPES.DEFAULT.type
        }
    }

    override fun getLayoutId(): Int = R.layout.item_collection_filter

    override fun getViewHolder(view: View): BaseViewHolder {
        return CollectionFilterHolder(
            itemView = view,
            adapter = this
        )
    }

    fun removeByPosition(position: Int) {
        currentList.removeAt(position)
        notifyItemRemoved(position)
    }
    /*
    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            TYPES.CUSTOM.type -> {
                CollectionFilterHolder(parent,this)
            }
            TYPES.DEFAULT.type -> {
                createItem(parent,R.layout.item_collection_filter_custom,::CollectionFilterCustomHolder)
            }
            else -> throw IllegalStateException("Wrong class")
        }
    }*/
}