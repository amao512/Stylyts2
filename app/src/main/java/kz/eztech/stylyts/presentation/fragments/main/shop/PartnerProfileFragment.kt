package kz.eztech.stylyts.presentation.fragments.main.shop

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_partner_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CategoryTypeDetailAdapter
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.PartnerProfileContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class PartnerProfileFragment : BaseFragment<MainActivity>(), PartnerProfileContract.View,
    UniversalViewClickListener {

    private lateinit var adapter: CategoryTypeDetailAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter

    override fun getLayoutId(): Int = R.layout.fragment_partner_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_partner_profile) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)

            toolbar_back_text_view.show()
            toolbar_title_text_view.show()

            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager, "Cart")
            }

            customizeActionToolBar(this, "Zara")
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {
        arguments?.let {}
    }

    override fun initializeViewsData() {
        val dummyList = ArrayList<ClothesTypeDataModel>()
        for (i in 0..11) {
            dummyList.add(ClothesTypeDataModel(title = "Кожанная куртка", cost = 12000))
        }
        adapter = CategoryTypeDetailAdapter()
        recycler_view_fragment_partner_profile_items_list.layoutManager =
            GridLayoutManager(context, 2)
        recycler_view_fragment_partner_profile_items_list.adapter = adapter
        adapter.updateList(dummyList)
        adapter.itemClickListener = this

        val filterList = ArrayList<CollectionFilterModel>()
        filterList.add(CollectionFilterModel("Фильтр"))
        filterList.add(CollectionFilterModel("Образы"))
        filterList.add(CollectionFilterModel("Все позиции"))
        filterList.add(CollectionFilterModel("Верх"))
        filterList.add(CollectionFilterModel("Низ"))
        adapterFilter = CollectionsFilterAdapter()
        recycler_view_fragment_partner_profile_filter_list.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        recycler_view_fragment_partner_profile_filter_list.adapter = adapterFilter
        adapterFilter.updateList(filterList)
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        findNavController().navigate(R.id.action_partnerProfileFragment_to_itemDetailFragment)
    }

    override fun initializeViews() {}

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}