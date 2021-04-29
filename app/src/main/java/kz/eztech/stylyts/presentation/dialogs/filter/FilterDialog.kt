package kz.eztech.stylyts.presentation.dialogs.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_filter.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.ColorModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.filter.FilterItemModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.adapters.filter.FilterAdapter
import kz.eztech.stylyts.presentation.adapters.filter.FilterCheckAdapter
import kz.eztech.stylyts.presentation.adapters.filter.FilterExpandableAdapter
import kz.eztech.stylyts.presentation.contracts.filter.FilterContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.filter.FilterPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.displaySnackBar
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
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
    private lateinit var currentFilter: FilterModel

    private var isOpenedFilter = false
    private var isCheckedItem: Boolean = false

    companion object {
        private const val TOKEN_ARGS = "token_args"

        fun getNewInstance(
            token: String,
            itemClickListener: UniversalViewClickListener,
            gender: String,
            isShowWardrobe: Boolean = false,
            isShowDiscount: Boolean = true,
        ): FilterDialog {
            val filterDialog = FilterDialog(
                itemClickListener = itemClickListener,
                currentGender = gender,
                isShowWardrobe = isShowWardrobe,
                isShowDiscount = isShowDiscount
            )
            val bundle = Bundle()

            bundle.putString(TOKEN_ARGS, token)
            filterDialog.arguments = bundle

            return filterDialog
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
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.filter_list_filter)
            toolbar_title_text_view.show()

            toolbar_title_text_view.show()
            toolbar_right_text_text_view.text = getString(R.string.constructor_filter_reset)
            toolbar_right_text_text_view.isClickable = isCheckedItem
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

        dialog_filter_recycler_view.adapter = filterAdapter

        fragment_category_shop_results_button.text = getString(
            R.string.button_show_results,
            0.toString()
        )
    }

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        fragment_category_shop_results_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getClothesTypes(token = getTokenFromArguments())
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

        checkEmptyFilter()
        setResetTextColor()
    }

    override fun processClothesBrands(list: List<FilterCheckModel>) {
        currentFilter.brandIdList.forEach { brand ->
            list.find { it.id == brand }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processWardrobe(list: List<FilterCheckModel>) {
        currentFilter.categoryIdList.forEach { category ->
            list.find { it.id == category }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processColors(list: List<FilterCheckModel>) {
        currentFilter.colorList.forEach { category ->
            list.find { it.id == category }?.isChecked = true
        }

        filterCheckAdapter.updateList(list)
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        fragment_category_shop_results_button.text = getString(
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

    fun setFilter(filterModel: FilterModel) {
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
        dialog_filter_recycler_view.adapter = filterAdapter
        with (dialog_filter_toolbar) {
            if (isOpenedFilter) {
                toolbar_title_text_view.text = getString(R.string.filter_list_filter)
                toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)

                dialog_filter_title.show()
                dialog_filter_recycler_view.show()
                dialog_filter_range_price_holder.hide()

                isOpenedFilter = false
            } else {
                dismiss()
            }
        }
    }

    private fun onResetButtonClick() {
        filterExpandableAdapter.onResetCheckedItems()
        filterCheckAdapter.onResetCheckedItems()

        currentFilter.typeIdList = emptyList()
        currentFilter.categoryIdList = emptyList()
        currentFilter.brandIdList = emptyList()
        currentFilter.colorList = emptyList()

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
            is ColorModel -> selectColor(position)
        }

        checkEmptyFilter()
        getFilterResults()
        setResetTextColor()
    }

    private fun selectCategoryItem(filterCheck: FilterCheckModel) {
        filterExpandableAdapter.onMultipleCheck(filterCheck)
        currentFilter.categoryIdList = filterExpandableAdapter.getCheckedItemList()
        currentFilter.typeIdList = filterExpandableAdapter.getCheckedFirstItemList()

        checkEmptyFilter()
        getFilterResults()
        setResetTextColor()
    }

    private fun selectWardrobeItem(position: Int) {
        filterCheckAdapter.onMultipleCheckItem(position)
        currentFilter.categoryIdList = filterCheckAdapter.getCheckedItemListByRemoveFirst()
    }

    private fun selectClothesBrand(position: Int) {
        filterCheckAdapter.onMultipleCheckItem(position)
        currentFilter.brandIdList = filterCheckAdapter.getCheckedItemListByRemoveFirst()
    }

    private fun selectColor(position: Int) {
        filterCheckAdapter.onMultipleCheckItem(position)
        currentFilter.colorList = filterCheckAdapter.getCheckedItemList()
    }

    private fun onMyWardrobeClick() {
        currentFilter.isMyWardrobe = true

        presenter.getMyWardrobe(
            token = getTokenFromArguments(),
            filterModel = currentFilter
        )

        processOpenedFilterGroup(title = getString(R.string.filter_my_wardrobe))
        dialog_filter_recycler_view.adapter = filterCheckAdapter
    }

    private fun onCategoriesClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_categories))
        dialog_filter_recycler_view.adapter = filterExpandableAdapter
    }

    private fun onBrandsClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_brands))
        dialog_filter_recycler_view.adapter = filterCheckAdapter

        presenter.getClothesBrands(
            title = getString(R.string.filter_brands),
            token = getTokenFromArguments()
        )
    }

    private fun onColorsClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_colors))
        dialog_filter_recycler_view.adapter = filterCheckAdapter

        presenter.getColors(token = getTokenFromArguments())
    }

    private fun onPriceRangeClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_costs))

        dialog_filter_title.hide()
        dialog_filter_recycler_view.hide()
        dialog_filter_range_price_holder.show()
    }

    private fun processOpenedFilterGroup(title: String) {
        dialog_filter_title.hide()
        toolbar_title_text_view.text = title
        toolbar_left_corner_action_image_button.setBackgroundResource(
            R.drawable.ic_baseline_keyboard_arrow_left_24
        )
        isOpenedFilter = true
    }

    private fun onShowButtonClick() {
        itemClickListener.onViewClicked(
            view = toolbar_left_corner_action_image_button,
            item = currentFilter,
            position = 0
        )
        dismiss()
    }

    private fun getFilterResults() {
        presenter.getClothesResults(
            token = getTokenFromArguments(),
            filterModel = currentFilter
        )
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
            if (isCheckedItem) {
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            } else {
                ContextCompat.getColor(requireContext(), R.color.app_gray_hint)
            }
        )
    }

    private fun checkEmptyFilter() {
        isCheckedItem = currentFilter.brandIdList.isNotEmpty() ||
                currentFilter.categoryIdList.isNotEmpty() ||
                currentFilter.typeIdList.isNotEmpty() ||
                currentFilter.colorList.isNotEmpty()
    }

    private fun getTokenFromArguments(): String {
        return arguments?.getString(TOKEN_ARGS) ?: EMPTY_STRING
    }
}