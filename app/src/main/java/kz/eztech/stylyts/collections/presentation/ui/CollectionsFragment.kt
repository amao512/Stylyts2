package kz.eztech.stylyts.collections.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.collections.presentation.ui.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.global.presentation.collection.ui.adapters.CollectionsViewPagerAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.collections.presentation.contracts.CollectionsContract
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.collections.presentation.presenters.CollectionsPresenter
import kz.eztech.stylyts.global.presentation.collection.ui.CollectionDetailFragment
import kz.eztech.stylyts.utils.extensions.show
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
            toolbar_title_text_view.text = getString(R.string.bottom_nav_bar_collection)
            toolbar_title_text_view.show()
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
        pagerAdapter = CollectionsViewPagerAdapter(
            fa = this,
            itemClickListener = this
        )

        filterAdapter = CollectionsFilterAdapter()
        filterAdapter.setOnClickListener(listener = this)
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
                0 -> tab.text = getString(R.string.for_his)
                1 -> tab.text = getString(R.string.for_her)
            }
        }.attach()
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is OutfitModel -> onOutfitClicked(item)
            is PostModel -> onPostClicked(item)
            is CollectionFilterModel -> filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
        }
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getClothesStyles()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processClothesStylesResults(resultsModel: ResultsModel<ClothesStyleModel>) {
        val filterList = mutableListOf<CollectionFilterModel>()

        resultsModel.results.map {
            filterList.add(
                CollectionFilterModel(id = it.id, name = it.title)
            )
        }

        filterAdapter.updateList(list = filterList)
    }

    private fun onOutfitClicked(outfitModel: OutfitModel) {
        val bundle = Bundle()

        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.OUTFIT_MODE)
        bundle.putInt(CollectionDetailFragment.ID_KEY, outfitModel.id)

        findNavController().navigate(
            R.id.action_collectionsFragment_to_collectionDetailFragment,
            bundle
        )
    }

    private fun onPostClicked(postModel: PostModel) {
        val bundle = Bundle()

        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)
        bundle.putInt(CollectionDetailFragment.ID_KEY, postModel.id)

        findNavController().navigate(
            R.id.action_collectionsFragment_to_collectionDetailFragment,
            bundle
        )
    }
}