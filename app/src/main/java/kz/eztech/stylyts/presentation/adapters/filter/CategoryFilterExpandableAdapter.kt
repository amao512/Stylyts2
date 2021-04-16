package kz.eztech.stylyts.presentation.adapters.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.presentation.adapters.filter.holders.CategoryExpandableViewHolder
import kz.eztech.stylyts.presentation.adapters.filter.holders.CategoryItemExpandableViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

class CategoryFilterExpandableAdapter(
    private val categoryFilterSingleGroupList: List<CategoryFilterSingleCheckGenre>,
    private val itemClickListener: UniversalViewClickListener
) : CheckableChildRecyclerViewAdapter<CategoryExpandableViewHolder, CategoryItemExpandableViewHolder>(categoryFilterSingleGroupList) {

    override fun onCreateGroupViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): CategoryExpandableViewHolder {
        return CategoryExpandableViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter,
                parent,
                false
            )
        )
    }

    override fun onBindGroupViewHolder(
        holderCheckable: CategoryExpandableViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holderCheckable?.onBind(title = group?.title ?: EMPTY_STRING)
    }

    override fun onCreateCheckChildViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): CategoryItemExpandableViewHolder {
        return CategoryItemExpandableViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter_single_check,
                parent,
                false
            ),
            itemClickListener
        )
    }

    override fun onBindCheckChildViewHolder(
        holderExpandable: CategoryItemExpandableViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        holderExpandable?.onBind(group?.items?.get(childIndex) as ClothesCategoryModel)
    }

    fun getCheckedCategoryList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()

        categoryFilterSingleGroupList.map {
            val copyCurrentList: MutableList<ClothesCategoryModel> = mutableListOf()

            it.items.map { item ->
                copyCurrentList.add(item as ClothesCategoryModel)
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
}