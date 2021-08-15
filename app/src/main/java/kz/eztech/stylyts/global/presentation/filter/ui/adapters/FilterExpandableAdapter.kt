package kz.eztech.stylyts.global.presentation.filter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.global.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders.FilterExpandableViewHolder
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders.FilterItemExpandableViewHolder
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.utils.EMPTY_STRING

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

    fun onMultipleCheck(filterCheck: FilterCheckModel) {
        val group = categoryFilterSingleGroupList.find { it.position == (filterCheck.item as ClothesCategoryModel).clothesType.id }

        if (filterCheck.isCustom) {
            filterCheck.isChecked = !filterCheck.isChecked

            group?.items?.forEach {
                (it as FilterCheckModel).isChecked = filterCheck.isChecked
            }
        } else {
            group?.items?.forEach {
                it as FilterCheckModel

                val category = it.item as ClothesCategoryModel

                if (it.isChecked && it.isCustom) {
                    it.isChecked = false
                }

                if ((filterCheck.item as ClothesCategoryModel).id == category.id && !it.isCustom) {
                    it.isChecked = !it.isChecked
                }
            }
        }

        notifyDataSetChanged()
    }

    fun getCheckedItemList(): List<FilterCheckModel> {
        val checkedList: MutableList<FilterCheckModel> = mutableListOf()

        categoryFilterSingleGroupList.map {
            val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

            it.items.map { item ->
                copyCurrentList.add(item as FilterCheckModel)
            }

            copyCurrentList.removeAt(0)

            copyCurrentList.map { item ->
                if (item.isChecked) {
                    checkedList.add(item)
                }
            }
        }

        return checkedList
    }

    fun getCheckedFirstItemList(): List<FilterCheckModel> {
        val checkedList: MutableList<FilterCheckModel> = mutableListOf()

        categoryFilterSingleGroupList.map {
            val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

            copyCurrentList.add(it.items[0] as FilterCheckModel)

            copyCurrentList.map { item ->
                if (item.isChecked) {
                    checkedList.add(item)
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

    fun onCheckChildById(id: Int) {
        categoryFilterSingleGroupList.map {
            it.items.map { item ->
                item as FilterCheckModel

                if (item.id == id) {
                    item.isChecked = true
                }
            }
        }

        notifyDataSetChanged()
    }
}