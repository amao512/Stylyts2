package kz.eztech.stylyts.presentation.fragments.collection

import android.view.View
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsItemPresenter
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemViewModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import org.koin.android.ext.android.inject
import javax.inject.Inject

class CollectionItemFragment(var currentMode: Int) : BaseFragment<MainActivity>(),
    CollectionItemContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: CollectionsItemPresenter
    private lateinit var adapter: GridImageAdapter

    private val shopItemViewModel: ShopItemViewModel by inject()

    private var itemClickListener: UniversalViewClickListener? = null

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
        adapter = GridImageAdapter()
        adapter.setOnClickListener(this)
    }

    override fun initializeViews() {
        recycler_view_fragment_collection_item.adapter = adapter
        recycler_view_fragment_collection_item.addItemDecoration(GridSpacesItemDecoration(space = 16))
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        itemClickListener?.onViewClicked(view, position, item)
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        val map = HashMap<String, Any>()

        when (currentMode) {
            0 -> map["gender"] = "M"
            1 -> map["gender"] = "F"
        }

        shopItemViewModel.isOutfits.observe(viewLifecycleOwner, {
            when (it) {
                true -> presenter.getOutfits(
                    token = getTokenFromSharedPref(),
                    map = map
                )
                false -> {
                    presenter.getPosts(token = getTokenFromSharedPref())
                }
            }
        })
    }

    override fun processOutfits(resultsModel: ResultsModel<OutfitModel>) {
        adapter.updateList(list = resultsModel.results)
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        adapter.updateList(list = resultsModel.results)
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}