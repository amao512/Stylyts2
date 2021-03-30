package kz.eztech.stylyts.presentation.dialogs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_filter_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.profile.FilterItemModel
import kz.eztech.stylyts.domain.models.profile.FilterModel
import kz.eztech.stylyts.domain.models.profile.FilterSingleCheckedExpandableGroup
import kz.eztech.stylyts.presentation.adapters.profile.FilterAdapter
import kz.eztech.stylyts.presentation.adapters.profile.FilterGroupAdapter
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class FilterProfileDialog : DialogFragment(), View.OnClickListener, UniversalViewClickListener {

    private lateinit var filterAdapter: FilterAdapter
    private lateinit var filterGroupAdapter: FilterGroupAdapter

    private var isOpenedGroup = false

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_filter_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeViews()
        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> closeFilterGroup()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is FilterModel -> openFilterGroup()
        }
    }

    private fun customizeActionBar() {
        with (dialog_filter_profile_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.filter_list_filter)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    private fun initializeViews() {
        filterAdapter = FilterAdapter()
        filterAdapter.itemClickListener = this
        filterAdapter.updateList(getFilterList())

        filterGroupAdapter = FilterGroupAdapter(getFilterGroupList())

        dialog_filter_profile_recycler_view.adapter = filterAdapter
    }

    private fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    private fun openFilterGroup() {
        dialog_filter_profile_recycler_view.adapter = filterGroupAdapter
        dialog_filter_profile_toolbar.toolbar_left_corner_action_image_button
            .setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
        isOpenedGroup = true
    }

    private fun closeFilterGroup() {
        dialog_filter_profile_recycler_view.adapter = filterAdapter
        with (dialog_filter_profile_toolbar) {
            if (isOpenedGroup) {
                toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)
                isOpenedGroup = false
            } else {
                dismiss()
            }
        }
    }

    private fun getFilterList(): List<FilterModel> {
        val list: MutableList<FilterModel> = mutableListOf()
        list.add(FilterModel("Категории"))
        list.add(FilterModel("Бренды"))
        list.add(FilterModel("Цвета"))
        list.add(FilterModel("Диапазон цен"))

        return list
    }

    private fun getFilterGroupList(): List<FilterSingleCheckedExpandableGroup> {
        // test data
        val list: MutableList<FilterSingleCheckedExpandableGroup> = mutableListOf()
        val topList: MutableList<FilterItemModel> = mutableListOf()
        topList.add(FilterItemModel(title = "Вещь 1"))
        topList.add(FilterItemModel(title = "Вещь 2"))
        topList.add(FilterItemModel(title = "Вещь 3"))
        topList.add(FilterItemModel(title = "Вещь 4"))

        list.add(FilterSingleCheckedExpandableGroup(title = "Верх", topList))
        list.add(FilterSingleCheckedExpandableGroup(title = "Низ", topList))
        list.add(FilterSingleCheckedExpandableGroup(title = "Обувь", topList))
        list.add(FilterSingleCheckedExpandableGroup(title = "Акссесуары", topList))

        return list
    }
}