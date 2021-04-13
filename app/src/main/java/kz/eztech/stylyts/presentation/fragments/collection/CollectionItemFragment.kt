package kz.eztech.stylyts.presentation.fragments.collection

import android.view.View
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection.GridImageCollectionItemAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsItemPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class CollectionItemFragment(var currentMode: Int) : BaseFragment<MainActivity>(),
    CollectionItemContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: CollectionsItemPresenter
    private lateinit var adapter: GridImageCollectionItemAdapter

    private var itemClickListener: UniversalViewClickListener? = null

    fun setOnClickListener(itemClickListener: UniversalViewClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        adapter = GridImageCollectionItemAdapter()
        adapter.setOnClickListener(this)
        recycler_view_fragment_collection_item.adapter = adapter
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

        presenter.getOutfits(
            token = getTokenFromSharedPref(),
            map = map
        )
    }

    override fun processOutfits(resultsModel: ResultsModel<OutfitModel>) {
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