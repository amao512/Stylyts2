package kz.eztech.stylyts.domain.models.profile

import android.util.Log
import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup

class FilterSingleCheckedExpandableGroup(
    title: String,
    filterItems: List<FilterItemModel>
) : SingleCheckExpandableGroup(title, filterItems) {

    override fun onChildClicked(childIndex: Int, checked: Boolean) {
        super.onChildClicked(childIndex, checked)

        Log.d("TAG", "filter - $childIndex")
    }
}