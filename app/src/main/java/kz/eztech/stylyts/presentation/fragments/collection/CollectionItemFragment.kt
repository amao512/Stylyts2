package kz.eztech.stylyts.presentation.fragments.collection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsItemPresenter
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemViewModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import org.koin.android.ext.android.inject
import javax.inject.Inject

class CollectionItemFragment(var currentMode: Int) : BaseFragment<MainActivity>(),
    CollectionItemContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: CollectionsItemPresenter
    private lateinit var adapter: GridImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var currentFilter: FilterModel

    private val shopItemViewModel: ShopItemViewModel by inject()

    private var itemClickListener: UniversalViewClickListener? = null
    private var isOutfits: Boolean = true

    fun setOnClickListener(itemClickListener: UniversalViewClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        currentFilter = FilterModel()
        adapter = GridImageAdapter()
        adapter.setOnClickListener(this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_collection_item
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridSpacesItemDecoration(space = 16))
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        itemClickListener?.onViewClicked(view, position, item)
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        getCollections()
        handleListRecyclerView()
    }

    override fun processOutfits(resultsModel: ResultsModel<OutfitModel>) {
        adapter.updateMoreList(list = resultsModel.results)
        resetPages(resultsModel.totalPages)
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        adapter.updateMoreList(list = resultsModel.results)
        resetPages(resultsModel.totalPages)
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_collection_small_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_collection_small_progress_bar.hide()
    }

    private fun handleListRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!currentFilter.isLastPage) {
                        when (isOutfits) {
                            true -> getOutfits()
                            false -> getPosts()
                        }
                    }
                }
            }
        })
    }

    private fun resetPages(totalPages: Int) {
        if (totalPages != currentFilter.page) {
            currentFilter.page++
        } else {
            currentFilter.isLastPage = true
        }
    }

    private fun getCollections() {
        shopItemViewModel.isOutfits.observe(viewLifecycleOwner, {
            isOutfits = it
            currentFilter.page = 1
            currentFilter.isLastPage = false
            adapter.clearList()

            when (it) {
                true -> getOutfits()
                false -> getPosts()
            }
        })
    }

    private fun getOutfits() {
        currentFilter.gender = when (currentMode) {
            0 -> GenderEnum.MALE.gender
            else -> GenderEnum.FEMALE.gender
        }

        presenter.getOutfits(
            token = getTokenFromSharedPref(),
            filterModel = currentFilter
        )
    }

    private fun getPosts() {
        presenter.getPosts(
            token = getTokenFromSharedPref(),
            filterModel = currentFilter
        )
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}