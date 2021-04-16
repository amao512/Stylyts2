package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_item_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
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

    private var clothesTypeGender: Int = 0
    private var selectedCategoryIdList: ArrayList<Int> = arrayListOf()
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

    override fun initializeViewsData() {}

    override fun initializeViews() {
        filterCheckAdapter = FilterCheckAdapter()
        recycler_view_fragment_shop_item_list.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_shop_item_list.adapter = filterCheckAdapter
        filterCheckAdapter.itemClickListener = this
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {
        fragment_category_shop_results_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getCategoriesByType(
            token = getTokenFromSharedPref(),
            clothesTypeId = clothesType.id
        )
        presenter.getClothesResultsByType(
            token = getTokenFromSharedPref(),
            typeIdList = arrayListOf(clothesType.id),
            gender = getCurrentGender()
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
            R.id.item_filter_check_title_checked_text_view -> onCheckCategory(item, position)
        }
    }

    override fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>) {
        val preparedResults: MutableList<ClothesCategoryModel> = mutableListOf()
        preparedResults.add(
            ClothesCategoryModel(
                id = 0,
                clothesType = clothesType,
                title = clothesType.title,
                bodyPart = clothesType.id
            )
        )

        preparedResults.addAll(resultsModel.results)
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

    private fun onCheckCategory(
        item: Any?,
        position: Int
    ) {
        filterCheckAdapter.onCheckPosition(position)

        val list = filterCheckAdapter.getCheckedCategoryList()

        selectedCategoryIdList.clear()
        selectedCategoryIdList.addAll(list)
        isCheckedItem = list.isNotEmpty()

        with(item as ClothesCategoryModel) {
            selectedCategoryTitle = when (selectedCategoryIdList.size) {
                1 -> {
                   if (item.isChecked) {
                       title
                   } else item.clothesType.title
                }
                else -> item.clothesType.title
            }

            setResetTextColor()

            presenter.getClothesResultsByCategory(
                token = getTokenFromSharedPref(),
                gender = getCurrentGender(),
                categoryIdList = selectedCategoryIdList
            )
        }
    }

    private fun showClothesResults() {
        val bundle = Bundle()
        bundle.putString(CategoryTypeDetailFragment.CLOTHES_GENDER, getCurrentGender())
        bundle.putIntegerArrayList(CategoryTypeDetailFragment.CLOTHES_CATEGORY_ID_LIST, selectedCategoryIdList)
        bundle.putInt(CategoryTypeDetailFragment.CLOTHES_TYPE_ID, clothesType.id)
        bundle.putString(CategoryTypeDetailFragment.CLOTHES_CATEGORY_TITLE, selectedCategoryTitle)

        findNavController().navigate(
            R.id.action_shopItemListFragment_to_categoryTypeDetailFragment,
            bundle
        )
    }

    private fun resetCategories() {
        if (isCheckedItem) {
            filterCheckAdapter.onResetChecking()

            selectedCategoryIdList.retainAll(selectedCategoryIdList)
            selectedCategoryTitle = clothesType.title
            isCheckedItem = false

            setResetTextColor()
        }
    }

    private fun getCurrentGender(): String {
        return when (clothesTypeGender) {
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