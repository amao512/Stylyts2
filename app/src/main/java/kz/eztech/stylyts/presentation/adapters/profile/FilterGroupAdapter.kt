package kz.eztech.stylyts.presentation.adapters.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.profile.FilterSingleCheckedExpandableGroup
import kz.eztech.stylyts.domain.models.profile.FilterItemModel
import kz.eztech.stylyts.presentation.adapters.profile.holders.FilterGroupViewHolder
import kz.eztech.stylyts.presentation.adapters.profile.holders.FilterItemViewHolder
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

class FilterGroupAdapter(
    filterSingleGroupList: List<FilterSingleCheckedExpandableGroup>
) : CheckableChildRecyclerViewAdapter<FilterGroupViewHolder, FilterItemViewHolder>(filterSingleGroupList) {

    override fun onCreateGroupViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): FilterGroupViewHolder {
        return FilterGroupViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter_group,
                parent,
                false
            )
        )
    }

    override fun onBindGroupViewHolder(
        holder: FilterGroupViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holder?.onBind(title = group?.title ?: EMPTY_STRING)
    }

    override fun onCreateCheckChildViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): FilterItemViewHolder {
        return FilterItemViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.item_filter_item,
                parent,
                false
            )
        )
    }

    override fun onBindCheckChildViewHolder(
        holder: FilterItemViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        holder?.onBind(group?.items?.get(childIndex) as FilterItemModel)
    }
}