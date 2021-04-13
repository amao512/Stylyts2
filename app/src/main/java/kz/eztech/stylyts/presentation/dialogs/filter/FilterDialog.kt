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

    private var selectedClothesType: Int = 0
    private var selectedClothesCategory: Int = 0
    private var selectedClothesBrand: Int = 0

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
            is FilterModel -> openFilterGroup(position, item)
            is ClothesCategoryModel -> selectClothesCategory(view, position, item)
            is ClothesBrandModel -> selectClothesBrand(view, position, item)
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
        getFilterResults()
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

        dialog_filter_recycler_view.adapter = categoryFilterExpandableAdapter
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

    private fun openFilterGroup(
        position: Int,
        filterModel: FilterModel
    ) {
        when (position) {
            0 -> presenter.getClothesTypes(token = getTokenFromArguments())
            1 -> {
                dialog_filter_recycler_view.adapter = filterCheckAdapter
                presenter.getClothesBrands(
                    title = filterModel.title,
                    token = getTokenFromArguments()
                )
            }
        }

        dialog_filter_title.hide()
        toolbar_title_text_view.text = filterModel.title
        toolbar_left_corner_action_image_button.setBackgroundResource(
            R.drawable.ic_baseline_keyboard_arrow_left_24
        )
        isOpenedFilter = true
    }

    private fun closeFilterGroup() {
        dialog_filter_recycler_view.adapter = filterAdapter
        with (dialog_filter_toolbar) {
            if (isOpenedFilter) {
                dialog_filter_title.show()
                toolbar_title_text_view.text = getString(R.string.filter_list_filter)
                toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)

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
        isCheckedItem = !category.isChecked

        if (!category.isChecked) {
            selectedClothesCategory = when (position) {
                0 -> position
                else -> category.id
            }

            selectedClothesType = category.clothesType.id
        }

        categoryFilterExpandableAdapter.onChildCheckChanged(view, !category.isChecked, position)
        getFilterResults()
        setResetTextColor()
    }

    private fun selectClothesBrand(
        view: View,
        position: Int,
        brand: ClothesBrandModel
    ) {
        isCheckedItem = !brand.isChecked

        if (!brand.isChecked) {
            selectedClothesBrand = when (position) {
                0 -> position
                else -> brand.id
            }
        }

        filterCheckAdapter.onCheckPosition(position)

        getFilterResults()
        setResetTextColor()
    }

    private fun getFilterResults() {
        presenter.getClothesResults(
            token = getTokenFromArguments(),
            gender = currentGender,
            clothesCategoryId = selectedClothesCategory,
            clothesTypeId = selectedClothesType,
            clothesBrandId = selectedClothesBrand
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