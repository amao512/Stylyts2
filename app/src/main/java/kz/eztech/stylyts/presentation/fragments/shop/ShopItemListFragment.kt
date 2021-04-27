package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_item_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.filter.FilterCheckAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemListContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemListPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopItemListFragment : BaseFragment<MainActivity>(), ShopItemListContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: ShopItemListPresenter

    private lateinit var filterCheckAdapter: FilterCheckAdapter
    private lateinit var clothesType: ClothesTypeModel
    private lateinit var selectedCategoryTitle: String
    private lateinit var currentFilter: FilterModel

    private var clothesTypeGender: Int = 0
    private var isCheckedItem: Boolean = false

    companion object {
        const val CLOTHES_TYPE_GENDER = "clothes_type_gender"
        const val CLOTHES_TYPE = "clothes_type_id"
    }

    override fun getLayoutId(): Int = R.layout.fragment_shop_item_list

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_bottom_border_view.hide()

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@ShopItemListFragment)

            toolbar_title_text_view.show()
            toolbar_right_text_text_view.text = getString(R.string.constructor_filter_reset)
            toolbar_right_text_text_view.isClickable = isCheckedItem
            toolbar_right_text_text_view.show()
            setResetTextColor()
            toolbar_right_text_text_view.setOnClickListener(this@ShopItemListFragment)

            customizeActionToolBar(
                toolbar = this,
                title = clothesType.title
            )
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CLOTHES_TYPE)) {
                it.getParcelable<ClothesTypeModel>(CLOTHES_TYPE)?.let { clothesTypeModel ->
                    clothesType = clothesTypeModel
                    selectedCategoryTitle = clothesTypeModel.title
                }
            }

            if (it.containsKey(CLOTHES_TYPE_GENDER)) {
                clothesTypeGender = it.getInt(CLOTHES_TYPE_GENDER)
            }
        }
    }

    override fun initializeViewsData() {
        filterCheckAdapter = FilterCheckAdapter()
        filterCheckAdapter.setOnClickListener(listener = this)

        currentFilter = FilterModel()
    }

    override fun initializeViews() {
        recycler_view_fragment_shop_item_list.adapter = filterCheckAdapter
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {
        fragment_category_shop_results_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        setCurrentGender()

        currentFilter.typeIdList = arrayListOf(clothesType.id)

        presenter.getCategoriesByType(
            token = getTokenFromSharedPref(),
            clothesTypeId = clothesType.id
        )
        presenter.getClothesResultsByType(
            token = getTokenFromSharedPref(),
            filterModel = currentFilter
        )
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_filter_check_title_checked_text_view -> onCheckFilterItem(item, position)
        }
    }

    override fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>) {
        val preparedResults: MutableList<FilterCheckModel> = mutableListOf()
        preparedResults.add(
            FilterCheckModel(
                id = 0,
                item = ClothesCategoryModel(
                    id = 0,
                    clothesType = clothesType,
                    title = clothesType.title,
                    bodyPart = clothesType.id
                )
            )
        )

        resultsModel.results.map {
            preparedResults.add(
                FilterCheckModel(
                    id = it.id,
                    item = it
                )
            )
        }

        filterCheckAdapter.updateList(list = preparedResults)
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        fragment_category_shop_results_button.text = getString(
            R.string.button_show_results,
            resultsModel.totalCount.toString()
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.fragment_category_shop_results_button -> showClothesResults()
            R.id.toolbar_right_text_text_view -> resetCategories()
        }
    }

    private fun onCheckFilterItem(
        item: Any?,
        position: Int
    ) {
        item as FilterCheckModel

        filterCheckAdapter.onMultipleCheckItem(position)
        currentFilter.categoryIdList = filterCheckAdapter.getCheckedItemList()

        with(item.item as ClothesCategoryModel) {
            selectedCategoryTitle = when (currentFilter.categoryIdList.size) {
                1 -> {
                   if (item.isChecked) {
                       title
                   } else item.item.clothesType.title
                }
                else -> item.item.clothesType.title
            }

            checkEmptyFilter()
            setResetTextColor()
            setCurrentGender()

            presenter.getClothesResultsByCategory(
                token = getTokenFromSharedPref(),
                filterModel = currentFilter
            )
        }
    }

    private fun checkEmptyFilter() {
        isCheckedItem = currentFilter.categoryIdList.isNotEmpty()
    }

    private fun showClothesResults() {
        val bundle = Bundle()

        bundle.putParcelable(CategoryTypeDetailFragment.FILTER_KEY, currentFilter)
        bundle.putString(CategoryTypeDetailFragment.CLOTHES_CATEGORY_TITLE, selectedCategoryTitle)

        findNavController().navigate(
            R.id.action_shopItemListFragment_to_categoryTypeDetailFragment,
            bundle
        )
    }

    private fun resetCategories() {
        if (isCheckedItem) {
            filterCheckAdapter.onResetCheckedItems()
            currentFilter.categoryIdList = emptyList()
            selectedCategoryTitle = clothesType.title

            checkEmptyFilter()
            setResetTextColor()
        }
    }

    private fun setCurrentGender() {
        currentFilter.gender = when (clothesTypeGender) {
            0 -> CategoryTypeDetailFragment.GENDER_MALE
            1 -> CategoryTypeDetailFragment.GENDER_FEMALE
            else -> CategoryTypeDetailFragment.GENDER_MALE
        }
    }

    private fun setResetTextColor() {
        include_toolbar_profile.toolbar_right_text_text_view.setTextColor(
            if (isCheckedItem) {
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            } else {
                ContextCompat.getColor(requireContext(), R.color.app_gray_hint)
            }
        )
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}