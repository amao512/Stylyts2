package kz.eztech.stylyts.common.presentation.fragments.collections

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.CollectionFilterModel
import kz.eztech.stylyts.common.domain.models.MainResult
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.collection_constructor.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.common.presentation.adapters.CollectionsViewPagerAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.contracts.main.collections.CollectionsContract
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show

class CollectionsFragment : BaseFragment<MainActivity>(), CollectionsContract.View,
    UniversalViewClickListener {

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filterList: ArrayList<CollectionFilterModel>
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

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        filterList = ArrayList()
        filterList.add(CollectionFilterModel(name = "Классика"))
        filterList.add(CollectionFilterModel(name = "Уличный"))
        filterList.add(CollectionFilterModel(name = "Casual"))
        filterList.add(CollectionFilterModel(name = "Спортивный"))
        filterList.add(CollectionFilterModel(name = "Произвольный"))
        filterList.add(CollectionFilterModel(name = "Классика"))
        filterList.add(CollectionFilterModel(name = "Уличный"))
        filterList.add(CollectionFilterModel(name = "Casual"))
        filterList.add(CollectionFilterModel(name = "Спортивный"))
        filterList.add(CollectionFilterModel(name = "Произвольный"))
    }

    override fun initializeViews() {
        pagerAdapter = CollectionsViewPagerAdapter(this, this)
        view_pager_fragment_collections.isSaveEnabled = false
        filterAdapter = CollectionsFilterAdapter()
        recycler_view_fragment_collections_filter_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_fragment_collections_filter_list.adapter = filterAdapter
        filterAdapter.updateList(filterList)
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
        }
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}