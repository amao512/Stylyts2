package kz.eztech.stylyts.global.presentation.filter.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_filter.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.domain.models.filter.FilterItemModel
import kz.eztech.stylyts.global.domain.models.clothes.*
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.FilterAdapter
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.FilterCheckAdapter
import kz.eztech.stylyts.global.presentation.filter.ui.adapters.FilterExpandableAdapter
import kz.eztech.stylyts.global.presentation.filter.contracts.FilterContract
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.global.presentation.filter.presenters.FilterPresenter
import kz.eztech.stylyts.utils.extensions.displaySnackBar
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class FilterDialog(
    private val itemClickListener: UniversalViewClickListener,
    private val currentGender: String,
    private val isShowWardrobe: Boolean,
    private val isShowDiscount: Boolean,
) : DialogFragment(), FilterContract.View, View.OnClickListener, UniversalViewClickListener {

    @Inject lateinit var presenter: FilterPresenter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var filterExpandableAdapter: FilterExpandableAdapter
    private lateinit var filterCheckAdapter: FilterCheckAdapter
    private lateinit var currentFilter: ClothesFilterModel

    private lateinit var filterTitleTextView: TextView
    private lateinit var listHolderLinearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var priceRangeHolderLinearLayout: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var rightCharacterListLinearLayout: LinearLayout
    private lateinit var minPriceEditText: EditText
    private lateinit var maxPriceEditText: EditText
    private lateinit var showResultsButton: Button

    private var isOpenedFilter = false

    companion object {
        fun getNewInstance(
            itemClickListener: UniversalViewClickListener,
            gender: String,
            isShowWardrobe: Boolean = false,
            isShowDiscount: Boolean = true,
        ): FilterDialog {
            return FilterDialog(
                itemClickListener = itemClickListener,
                currentGender = gender,
                isShowWardrobe = isShowWardrobe,
                isShowDiscount = isShowDiscount
            )
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializePresenter()
        customizeActionBar()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun customizeActionBar() {
        with (dialog_filter_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.filter_list_filter)
            toolbar_title_text_view.show()

            toolbar_title_text_view.show()
            toolbar_right_text_text_view.text = getString(R.string.constructor_filter_reset)
            toolbar_right_text_text_view.isClickable = false
            toolbar_right_text_text_view.show()
            setResetTextColor()
            toolbar_right_text_text_view.setOnClickListener(this@FilterDialog)

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(dialog = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        currentFilter.gender = currentGender
    }

    override fun initializeViews() {
        filterAdapter = FilterAdapter()
        filterAdapter.itemClickListener = this
        filterAdapter.updateList(getFilterList())

        filterCheckAdapter = FilterCheckAdapter()
        filterCheckAdapter.itemClickListener = this

        filterTitleTextView = dialog_filter_title
        listHolderLinearLayout = dialog_filter_list_holder_linear_layout
        recyclerView = dialog_filter_recycler_view
        recyclerView.adapter = filterAdapter

        priceRangeHolderLinearLayout = dialog_filter_range_price_holder
        showResultsButton = fragment_category_shop_results_button
        showResultsButton.text = getString(
            R.string.button_show_results,
            0.toString()
        )
        searchView = dialog_filter_search_view
        rightCharacterListLinearLayout = dialog_filter_right_character_list_linear_layout
        minPriceEditText = dialog_filter_min_price_edit_text
        maxPriceEditText = dialog_filter_max_price_edit_text
    }

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        showResultsButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getClothesTypes()
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        view?.let {
            displaySnackBar(
                context = requireContext(),
                view = it,
                msg = msg
            )
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processClothesCategories(list: List<CategoryFilterSingleCheckGenre>) {
        filterExpandableAdapter = FilterExpandableAdapter(
            categoryFilterSingleGroupList = list,
            itemClickListener = this
        )

        currentFilter.categoryIdList.map {
            filterExpandableAdapter.onCheckChildById(id = it.id)
        }

        checkEmptyFilter()
        setResetTextColor()
    }

    override fun processClothesBrands(list: List<FilterCheckModel>) {
        currentFilter.brandList.forEach { brand ->
            list.find { it.id == brand.id }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processBrandCharacters(characters: List<String>) {
        rightCharacterListLinearLayout.removeAllViews()

        characters.map {
            val textView = TextView(requireContext())
            textView.text = it
            textView.gravity = Gravity.CENTER

            rightCharacterListLinearLayout.addView(textView)
        }
    }

    override fun processWardrobe(list: List<FilterCheckModel>) {
        currentFilter.categoryIdList.forEach { category ->
            list.find { it.id == category.id }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processColors(list: List<FilterCheckModel>) {
        currentFilter.colorList.forEach { color ->
            list.find { (it.item as ClothesColorModel).color == color.color }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        showResultsButton.text = getString(
            R.string.button_show_results,
            resultsModel.totalCount.toString()
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> closeFilterGroup()
            R.id.toolbar_right_text_text_view -> onResetButtonClick()
            R.id.fragment_category_shop_results_button -> onShowButtonClick()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_filter_single_check_title_checked_text_view -> selectCategoryItem(
                filterCheck = item as FilterCheckModel
            )
            R.id.item_filter_check_title_checked_text_view -> selectFilterItem(position, item)
            R.id.item_filter_root_view_frame_layout -> openFilterGroup(item as FilterItemModel)
        }
    }

    fun setFilter(filterModel: ClothesFilterModel) {
        this.currentFilter = filterModel
    }

    private fun openFilterGroup(filterItemModel: FilterItemModel) {
        when (filterItemModel.id) {
            0 -> onMyWardrobeClick()
            1 -> onCategoriesClick()
            2 -> onBrandsClick()
            3 -> onColorsClick()
            4 -> onPriceRangeClick()
            5 -> {}
        }
    }

    private fun closeFilterGroup() {
        recyclerView.adapter = filterAdapter
        with (dialog_filter_toolbar) {
            if (isOpenedFilter) {
                toolbar_title_text_view.text = getString(R.string.filter_list_filter)

                filterTitleTextView.show()
                listHolderLinearLayout.show()
                recyclerView.show()
                priceRangeHolderLinearLayout.hide()
                searchView.hide()
                rightCharacterListLinearLayout.hide()

                isOpenedFilter = false
            } else {
                dismiss()
            }
        }
    }

    private fun onResetButtonClick() {
        filterExpandableAdapter.onResetCheckedItems()
        filterCheckAdapter.onResetCheckedItems()

        currentFilter.categoryIdList = emptyList()
        currentFilter.brandList = emptyList()
        currentFilter.colorList = emptyList()
        currentFilter.minPrice = 0
        currentFilter.maxPrice = 0
        currentFilter.inMyWardrobe = false

        minPriceEditText.text.clear()
        maxPriceEditText.text.clear()

        checkEmptyFilter()
        setResetTextColor()
    }

    private fun selectFilterItem(
        position: Int,
        item: Any?
    ) {
        item as FilterCheckModel

        when (item.item) {
            is ClothesCategoryModel -> selectWardrobeItem(position)
            is ClothesBrandModel -> selectClothesBrand(position)
            is ClothesColorModel -> selectColor(position)
        }

        checkEmptyFilter()
        getFilterResults()
        setResetTextColor()
    }

    private fun selectCategoryItem(filterCheck: FilterCheckModel) {
        filterExpandableAdapter.onMultipleCheck(filterCheck)
        currentFilter.categoryIdList = filterExpandableAdapter.getCheckedItemList().map { it.item as ClothesCategoryModel }
        currentFilter.typeIdList = filterExpandableAdapter.getCheckedFirstItemList().map {
            val type = it.item as ClothesCategoryModel

            ClothesTypeModel(id = type.id, title = type.title)
        }

        checkEmptyFilter()
        getFilterResults()
        setResetTextColor()
    }

    private fun selectWardrobeItem(position: Int) {
        filterCheckAdapter.onMultipleCheckItem(position)
        currentFilter.categoryIdList = filterCheckAdapter.getCheckedItemListByRemoveFirst().map { it.item as ClothesCategoryModel }
    }

    private fun selectClothesBrand(position: Int) {
        filterCheckAdapter.onSingleCheckItem(position)
        currentFilter.brandList = filterCheckAdapter.getCheckedItemList().map {
            it.item as ClothesBrandModel
        }
    }

    private fun selectColor(position: Int) {
        filterCheckAdapter.onSingleCheckItem(position)
        currentFilter.colorList = filterCheckAdapter.getCheckedItemList().map { it.item as ClothesColorModel }
    }

    private fun onMyWardrobeClick() {
        currentFilter.inMyWardrobe = true

        presenter.getMyWardrobe(filterModel = currentFilter)

        processOpenedFilterGroup(title = getString(R.string.filter_my_wardrobe))
        recyclerView.adapter = filterCheckAdapter
    }

    private fun onCategoriesClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_categories))
        recyclerView.adapter = filterExpandableAdapter
    }

    private fun onBrandsClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_brands))
        recyclerView.adapter = filterCheckAdapter
//        searchView.show()
        rightCharacterListLinearLayout.show()

        presenter.getClothesBrands(title = getString(R.string.filter_brands),)
    }

    private fun onColorsClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_colors))
        recyclerView.adapter = filterCheckAdapter

        presenter.getColors()
    }

    private fun onPriceRangeClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_costs))
        handlePriceEditText()

        filterTitleTextView.hide()
        listHolderLinearLayout.hide()
        priceRangeHolderLinearLayout.show()
    }

    private fun handlePriceEditText() {
        minPriceEditText.setOnKeyListener { _, _, _ ->
            if (minPriceEditText.text.isNotBlank()) {
                currentFilter.minPrice = minPriceEditText.text.toString().toInt()
            } else {
                currentFilter.minPrice = 0
            }

            getFilterResults()
            checkEmptyFilter()

            false
        }

        maxPriceEditText.setOnKeyListener { _, _, _ ->
            if (maxPriceEditText.text.isNotBlank()) {
                currentFilter.maxPrice = maxPriceEditText.text.toString().toInt()
            } else {
                currentFilter.maxPrice = 0
            }

            getFilterResults()
            checkEmptyFilter()

            false
        }
    }

    private fun processOpenedFilterGroup(title: String) {
        toolbar_title_text_view.text = title

        filterTitleTextView.hide()
        isOpenedFilter = true
    }

    private fun onShowButtonClick() {
        itemClickListener.onViewClicked(
            view = fragment_category_shop_results_button,
            item = currentFilter,
            position = 0
        )
        dismiss()
    }

    private fun getFilterResults() {
        presenter.getClothesResults(filterModel = currentFilter)
    }

    private fun getFilterList(): List<FilterItemModel> {
        val list: MutableList<FilterItemModel> = mutableListOf()

        if (isShowWardrobe) {
            list.add(FilterItemModel(id = 0, title = getString(R.string.filter_my_wardrobe)))
        }

        list.add(FilterItemModel(id = 1, title = getString(R.string.filter_categories)))
        list.add(FilterItemModel(id = 2, title = getString(R.string.filter_brands)))
        list.add(FilterItemModel(id = 3, title = getString(R.string.filter_colors)))
        list.add(FilterItemModel(id = 4, title = getString(R.string.filter_costs)))

        if (isShowDiscount) {
            list.add(FilterItemModel(id = 5, title = getString(R.string.filter_discounts)))
        }

        return list
    }

    private fun setResetTextColor() {
        dialog_filter_toolbar.toolbar_right_text_text_view.setTextColor(
            if (toolbar_right_text_text_view.isClickable) {
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            } else {
                ContextCompat.getColor(requireContext(), R.color.app_gray_hint)
            }
        )
    }

    private fun checkEmptyFilter() {
        toolbar_right_text_text_view.isClickable = currentFilter.brandList.isNotEmpty() ||
                currentFilter.categoryIdList.isNotEmpty() ||
                currentFilter.colorList.isNotEmpty() ||
                currentFilter.minPrice != 0 ||
                currentFilter.maxPrice != 0
    }
}