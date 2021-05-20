package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import kz.eztech.stylyts.presentation.dialogs.cart.CartDialog
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.CategoryTypeDetailFragmentPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class CategoryTypeDetailFragment : BaseFragment<MainActivity>(), CategoryTypeDetailContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: CategoryTypeDetailFragmentPresenter
    private lateinit var clothesAdapter: ClothesDetailAdapter
    private lateinit var brandsFilterAdapter: CollectionsFilterAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: FilterModel

    private var title: String = EMPTY_STRING

    companion object {
        const val CLOTHES_CATEGORY_TITLE = "clothes_category_title"
        const val FILTER_KEY = "filter_model"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_category_type_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_bottom_border_view.hide()

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@CategoryTypeDetailFragment)

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setOnClickListener(this@CategoryTypeDetailFragment)

            base_toolbar_small_title_text_view.text = title
            base_toolbar_title_with_subtitle.show()
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(FILTER_KEY)) {
                it.getParcelable<FilterModel>(FILTER_KEY)?.let { filter ->
                    currentFilter = filter
                }
            }

            if (it.containsKey(CLOTHES_CATEGORY_TITLE)) {
                title = it.getString(CLOTHES_CATEGORY_TITLE) ?: EMPTY_STRING
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.item_clothes_detail_linear_layout -> onClothesItemClick(item)
            R.id.frame_layout_item_collection_filter -> onBrandFilterClick(position, item as CollectionFilterModel)
        }

        when (item) {
            is FilterModel -> {
                currentFilter = item

                presenter.getClothesByType(
                    token = currentActivity.getTokenFromSharedPref(),
                    filterModel = currentFilter
                )
            }
        }
    }

    override fun initializeViewsData() {
        currentFilter = FilterModel()

        brandsFilterAdapter = CollectionsFilterAdapter()
        brandsFilterAdapter.setOnClickListener(listener = this)

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.setOnClickListener(listener = this)

        filterDialog = FilterDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            itemClickListener = this,
            gender = currentFilter.gender
        ).apply {
            setFilter(filterModel = currentFilter)
        }
    }

    override fun initializeViews() {
        fragment_category_type_detail_brands_recycler_view.adapter = brandsFilterAdapter
        recycler_view_fragment_category_type_detail.adapter = clothesAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getClothesBrands(token = currentActivity.getTokenFromSharedPref())

        if (currentFilter.categoryIdList.isEmpty() && currentFilter.typeIdList.isNotEmpty()) {
            presenter.getClothesByType(
                token = currentActivity.getTokenFromSharedPref(),
                filterModel = currentFilter
            )
        } else {
            presenter.getCategoryTypeDetail(
                token = currentActivity.getTokenFromSharedPref(),
                filterModel = currentFilter
            )
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        with (include_toolbar_profile) {
            base_toolbar_small_title_sub_text_view.text = getString(
                if (resultsModel.totalCount == 1) {
                    R.string.toolbar_position_text_format
                } else {
                    R.string.toolbar_positions_text_format
                },
                resultsModel.totalCount.toString()
            )
        }

        clothesAdapter.updateList(list = resultsModel.results)
    }

    override fun processClothesBrands(resultsModel: ResultsModel<ClothesBrandModel>) {
        val filterList = mutableListOf<CollectionFilterModel>()

        filterList.add(
            CollectionFilterModel(
                id = 0,
                name = getString(R.string.filter_list_filter),
                icon = R.drawable.ic_filter
            )
        )

        resultsModel.results.map {
            filterList.add(
                CollectionFilterModel(
                    id = it.id,
                    name = it.title,
                    item = it
                )
            )
        }

        brandsFilterAdapter.updateList(list = filterList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.toolbar_right_corner_action_image_button -> CartDialog().show(
                childFragmentManager, EMPTY_STRING
            )
        }
    }

    private fun onClothesItemClick(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(
            R.id.action_categoryTypeDetailFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun onBrandFilterClick(
        position: Int,
        item: CollectionFilterModel
    ) {
        if (position == 0) {
            filterDialog.apply {
                currentFilter.page = 1
                currentFilter.isLastPage = false
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, EMPTY_STRING)
        } else {
            brandsFilterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
            currentFilter.brandList = listOf(item.item as ClothesBrandModel)
            presenter.getClothesByBrand(
                token = currentActivity.getTokenFromSharedPref(),
                filterModel = currentFilter
            )
        }
    }
}