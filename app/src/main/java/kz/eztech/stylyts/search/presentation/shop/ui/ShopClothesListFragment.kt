package kz.eztech.stylyts.search.presentation.shop.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.search.presentation.shop.ui.adapters.ClothesDetailAdapter
import kz.eztech.stylyts.collections.presentation.ui.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopClothesListContract
import kz.eztech.stylyts.global.presentation.filter.ui.FilterDialog
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.search.presentation.shop.presenters.ShopClothesListPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ShopClothesListFragment : BaseFragment<MainActivity>(), ShopClothesListContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject
    lateinit var presenter: ShopClothesListPresenter
    private lateinit var clothesAdapter: ClothesDetailAdapter
    private lateinit var brandsFilterAdapter: CollectionsFilterAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: ClothesFilterModel

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
            toolbar_left_corner_action_image_button.setOnClickListener(this@ShopClothesListFragment)

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setOnClickListener(this@ShopClothesListFragment)

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
                it.getParcelable<ClothesFilterModel>(FILTER_KEY)?.let { filter ->
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
            R.id.frame_layout_item_collection_filter -> onBrandFilterClick(
                position,
                item as CollectionFilterModel
            )
        }

        when (item) {
            is ClothesFilterModel -> {
                currentFilter = item
                presenter.getClothes()
            }
        }
    }

    override fun initializeViewsData() {
        currentFilter = ClothesFilterModel()

        brandsFilterAdapter = CollectionsFilterAdapter()
        brandsFilterAdapter.setOnClickListener(listener = this)

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.setOnClickListener(listener = this)

        filterDialog = FilterDialog.getNewInstance(
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
        presenter.getClothesBrands()
        presenter.getClothes()

        handleRecyclerView()
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

    override fun getClothesFilter(): ClothesFilterModel = currentFilter

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processList(state.data)
            is Paginator.State.NewPageProgress<*> -> processList(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processList(list: List<Any?>) {
        list.map { it!! }.let {
            when (it[0]) {
                is ClothesModel -> processClothes(it)
                is ClothesBrandModel -> processClothesBrands(it)
            }
        }
    }

    override fun processClothes(list: List<Any>) {
        with(include_toolbar_profile) {
            base_toolbar_small_title_sub_text_view.text = getString(
                if (list.size == 1) {
                    R.string.toolbar_position_text_format
                } else {
                    R.string.toolbar_positions_text_format
                },
                list.size.toString()
            )
        }

        clothesAdapter.updateList(list = list)
    }

    override fun processClothesBrands(list: List<Any>) {
        val filterList = mutableListOf<CollectionFilterModel>()

        filterList.add(
            CollectionFilterModel(
                id = 0,
                name = getString(R.string.filter_list_filter),
                icon = R.drawable.ic_filter
            )
        )

        list.map {
            it as ClothesBrandModel

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
            R.id.toolbar_right_corner_action_image_button -> findNavController().navigate(R.id.action_shopClothesListFragment_to_nav_ordering)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentFilter.brandList = emptyList()
    }

    private fun handleRecyclerView() {
        fragment_category_type_detail_brands_recycler_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!fragment_category_type_detail_brands_recycler_view.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMoreBrands()
                }
            }
        })

        recycler_view_fragment_category_type_detail.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recycler_view_fragment_category_type_detail.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun onClothesItemClick(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(
            R.id.action_shopClothesListFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun onBrandFilterClick(
        position: Int,
        item: CollectionFilterModel
    ) {
        clothesAdapter.clearList()

        if (position == 0) {
            filterDialog.apply {
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, EMPTY_STRING)
        } else {
            brandsFilterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
            currentFilter.brandList = listOf(item.item as ClothesBrandModel)
            presenter.getClothes()
        }
    }
}