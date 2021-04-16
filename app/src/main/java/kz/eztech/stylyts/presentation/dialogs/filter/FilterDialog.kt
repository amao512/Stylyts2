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
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.adapters.filter.CategoryFilterExpandableAdapter
import kz.eztech.stylyts.presentation.adapters.filter.FilterAdapter
import kz.eztech.stylyts.presentation.adapters.filter.FilterCheckAdapter
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
    private val currentGender: String
) : DialogFragment(), FilterContract.View, View.OnClickListener, UniversalViewClickListener {

    @Inject lateinit var presenter: FilterPresenter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var categoryFilterExpandableAdapter: CategoryFilterExpandableAdapter
    private lateinit var filterCheckAdapter: FilterCheckAdapter

    private var isOpenedFilter = false
    private var isCheckedItem: Boolean = false

    private var selectedClothesType: MutableList<Int> = mutableListOf()
    private var selectedClothesCategoryList: MutableList<Int> = mutableListOf()
    private var selectedClothesBrandList: MutableList<Int> = mutableListOf()

    companion object {
        private const val TOKEN_ARGS = "token_args"

        fun getNewInstance(
            token: String,
            itemClickListener: UniversalViewClickListener,
            gender: String
        ): FilterDialog {
            val filterDialog = FilterDialog(itemClickListener, gender)
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
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> closeFilterGroup()
            R.id.toolbar_right_text_text_view -> {}
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is FilterModel -> openFilterGroup(position)
            is ClothesCategoryModel -> selectClothesCategory(view, position, item)
            is ClothesBrandModel -> selectClothesBrand(position)
        }
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

    override fun initializeViewsData() {}

    override fun initializeViews() {
        filterAdapter = FilterAdapter()
        filterAdapter.itemClickListener = this
        filterAdapter.updateList(getFilterList())

        filterCheckAdapter = FilterCheckAdapter()
        filterCheckAdapter.itemClickListener = this

        dialog_filter_recycler_view.adapter = filterAdapter
    }

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getClothesTypes(token = getTokenFromArguments())
        presenter.getClothesBrands(
            title = getString(R.string.filter_brands),
            token = getTokenFromArguments()
        )
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
        categoryFilterExpandableAdapter = CategoryFilterExpandableAdapter(
            categoryFilterSingleGroupList = list,
            itemClickListener = this
        )
    }

    override fun processClothesBrands(resultsModel: ResultsModel<ClothesBrandModel>) {
        filterCheckAdapter.updateList(list = resultsModel.results)
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        fragment_category_shop_results_button.text = getString(
            R.string.button_show_results,
            resultsModel.totalCount.toString()
        )
    }

    private fun openFilterGroup(position: Int) {
        when (position) {
            0 -> onCategoriesClick()
            1 -> onBrandsClick()
            3 -> onPriceRangeClick()
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

    private fun selectClothesCategory(
        view: View,
        position: Int,
        category: ClothesCategoryModel
    ) {
        categoryFilterExpandableAdapter.onChildCheckChanged(view, category.isChecked, position)

        val list = categoryFilterExpandableAdapter.getCheckedCategoryList()

        selectedClothesCategoryList.clear()
        selectedClothesCategoryList.addAll(list)
        isCheckedItem = selectedClothesBrandList.isNotEmpty() || selectedClothesCategoryList.isNotEmpty()

        getFilterResults()
        setResetTextColor()
    }

    private fun selectClothesBrand(position: Int) {
        filterCheckAdapter.onCheckPosition(position)

        val list = filterCheckAdapter.getCheckedBrandList()

        selectedClothesBrandList.clear()
        selectedClothesBrandList.addAll(list)
        isCheckedItem = selectedClothesBrandList.isNotEmpty() || selectedClothesCategoryList.isNotEmpty()

        getFilterResults()
        setResetTextColor()
    }

    private fun onCategoriesClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_categories))
        dialog_filter_recycler_view.adapter = categoryFilterExpandableAdapter
    }

    private fun onBrandsClick() {
        processOpenedFilterGroup(title = getString(R.string.filter_brands))
        dialog_filter_recycler_view.adapter = filterCheckAdapter
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

    private fun getFilterResults() {
        presenter.getClothesResults(
            token = getTokenFromArguments(),
            gender = currentGender,
            categoryIdList = selectedClothesCategoryList,
            typeIdList = selectedClothesType,
            brandIdList = selectedClothesBrandList
        )
    }

    private fun getFilterList(): List<FilterModel> {
        val list: MutableList<FilterModel> = mutableListOf()
        list.add(FilterModel(title = getString(R.string.filter_categories)))
        list.add(FilterModel(title = getString(R.string.filter_brands)))
        list.add(FilterModel(title = getString(R.string.filter_colors)))
        list.add(FilterModel(title = getString(R.string.filter_costs)))

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

    private fun getTokenFromArguments(): String {
        return arguments?.getString(TOKEN_ARGS) ?: EMPTY_STRING
    }
}