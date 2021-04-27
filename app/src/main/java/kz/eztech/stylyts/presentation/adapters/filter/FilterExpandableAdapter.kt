package kz.eztech.stylyts.presentation.adapters.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.filter.holders.FilterExpandableViewHolder
import kz.eztech.stylyts.presentation.adapters.filter.holders.FilterItemExpandableViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

class FilterExpandableAdapter(
    private val categoryFilterSingleGroupList: List<CategoryFilterSingleCheckGenre>,
    private val itemClickListener: UniversalViewClickListener
) : CheckableChildRecyclerViewAdapter<FilterExpandableViewHolder, FilterItemExpandableViewHolder>(categoryFilterSingleGroupList) {

    override fun onCreateGroupViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): FilterExpandableViewHolder {
        return FilterExpandableViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter,
                parent,
                false
            )
        )
    }

    override fun onBindGroupViewHolder(
        holderCheckable: FilterExpandableViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holderCheckable?.onBind(title = group?.title ?: EMPTY_STRING)
    }

    override fun onCreateCheckChildViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): FilterItemExpandableViewHolder {
        return FilterItemExpandableViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter_single_check,
                parent,
                false
            ),
            itemClickListener
        )
    }

    override fun onBindCheckChildViewHolder(
        holderExpandable: FilterItemExpandableViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        holderExpandable?.onBind(group?.items?.get(childIndex) as FilterCheckModel)
    }

    fun getCheckedItemList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()

        categoryFilterSingleGroupList.map {
            val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

            it.items.map { item ->
                copyCurrentList.add(item as FilterCheckModel)
            }

            copyCurrentList.removeAt(0)

            copyCurrentList.map { item ->
                if (item.isChecked) {
                    checkedList.add(item.id)
                }
            }
        }

        return checkedList
    }

    fun getCheckedFirstItemList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()

        categoryFilterSingleGroupList.map {
            val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

            copyCurrentList.add(it.items[0] as FilterCheckModel)

            copyCurrentList.map { item ->
                if (item.isChecked) {
                    checkedList.add(item.id)
                }
            }
        }

        return checkedList
    }

    fun onResetCheckedItems() {
        categoryFilterSingleGroupList.map {
            it.items.map { item ->
                (item as FilterCheckModel).isChecked = false
            }
        }

        notifyDataSetChanged()
    }
}