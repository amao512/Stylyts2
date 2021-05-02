package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionsContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsPresenter
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemViewModel
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import org.koin.android.ext.android.inject
import javax.inject.Inject

class CollectionsFragment : BaseFragment<MainActivity>(), CollectionsContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: CollectionsPresenter
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var pagerAdapter: CollectionsViewPagerAdapter

    private val shopItemViewModel: ShopItemViewModel by inject()

    private var isOutfits: Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_collections

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)
            toolbar_left_corner_action_image_button.show()

            setRightToolbarButtons()
            base_toolbar_right_double_buttons_left_button.setOnClickListener(this@CollectionsFragment)
            base_toolbar_right_double_buttons_right_button.setOnClickListener(this@CollectionsFragment)
            base_toolbar_double_right_buttons_linear_layout.show()

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
            is CollectionFilterModel -> filterAdapter.onChooseItem(position)
        }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.base_toolbar_right_double_buttons_left_button -> {
                if (isOutfits) {
                    onRightButtonsClick()
                }
            }
            R.id.base_toolbar_right_double_buttons_right_button -> {
                if (!isOutfits) {
                    onRightButtonsClick()
                }
            }
        }
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getClothesStyles(token = currentActivity.getTokenFromSharedPref())
        shopItemViewModel.setCollectionMode(isOutfits)
    }

    override fun disposeRequests() {}

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

    private fun onRightButtonsClick() {
        isOutfits = !isOutfits
        shopItemViewModel.setCollectionMode(isOutfits)
        setRightToolbarButtons()
        setStylesCondition()
    }

    private fun setRightToolbarButtons() {
        with (include_toolbar) {
            if (isOutfits) {
                base_toolbar_right_double_buttons_left_image_button.setImageResource(R.drawable.ic_camera_disabled)
                base_toolbar_right_double_buttons_right_image_button.setImageResource(R.drawable.ic_outfit_enabled)
            } else {
                base_toolbar_right_double_buttons_left_image_button.setImageResource(R.drawable.ic_camera)
                base_toolbar_right_double_buttons_right_image_button.setImageResource(R.drawable.ic_outfit_disabled)
            }
        }
    }

    private fun setStylesCondition() {
        if (isOutfits) {
            recycler_view_fragment_collections_filter_list.show()
        } else {
            recycler_view_fragment_collections_filter_list.hide()
        }
    }
}