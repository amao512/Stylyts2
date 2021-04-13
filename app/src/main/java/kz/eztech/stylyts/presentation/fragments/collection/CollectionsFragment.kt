package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionsContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class CollectionsFragment : BaseFragment<MainActivity>(), CollectionsContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: CollectionsPresenter
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var pagerAdapter: CollectionsViewPagerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_collections

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_camera)
            toolbar_back_text_view.hide()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_create_collection)

            customizeActionToolBar(this, getString(R.string.fragment_registration_appbar_title))
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        pagerAdapter = CollectionsViewPagerAdapter(this, this)

        filterAdapter = CollectionsFilterAdapter()
        filterAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        view_pager_fragment_collections.isSaveEnabled = false
        recycler_view_fragment_collections_filter_list.adapter = filterAdapter
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
        view_pager_fragment_collections.adapter = pagerAdapter

        TabLayoutMediator(
            tab_bar_fragment_collections,
            view_pager_fragment_collections
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Для нее"
                }
                1 -> {
                    tab.text = "Для него"
                }
            }
        }.attach()
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is MainResult -> {
                val bundle = Bundle()
                bundle.putParcelable("model", item)
                findNavController().navigate(
                    R.id.action_collectionsFragment_to_collectionDetailFragment,
                    bundle
                )
            }
            is CollectionFilterModel -> onStyleItemClick(item, position)
        }
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getClothesStyles(token = getTokenFromSharedPref())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processClothesStylesResults(resultsModel: ResultsModel<ClothesStyleModel>) {
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
                CollectionFilterModel(id = it.id, name = it.title)
            )
        }

        filterAdapter.updateList(list = filterList)
    }

    private fun onStyleItemClick(
        item: CollectionFilterModel,
        position: Int
    ) {
        if (position == 0) {
            FilterDialog.getNewInstance(
                token = getTokenFromSharedPref(),
                itemClickListener = this,
                gender = item.gender ?: "M"
            ).show(childFragmentManager, EMPTY_STRING)
        } else filterAdapter.onChooseItem(position)
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}