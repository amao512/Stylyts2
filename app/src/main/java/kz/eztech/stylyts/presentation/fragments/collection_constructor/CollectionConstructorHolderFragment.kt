package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_constructor_holder.*
import kotlinx.android.synthetic.main.fragment_profile.include_toolbar_profile
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection_constructor.ConstructorHolderContract
import kz.eztech.stylyts.presentation.presenters.common.PagerViewModel
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import org.koin.android.ext.android.inject

class CollectionConstructorHolderFragment : BaseFragment<MainActivity>(),
    ConstructorHolderContract.View, View.OnClickListener {

    private lateinit var pagerAdapter: CollectionConstructorPagerAdapter

    private val pagerViewModel: PagerViewModel by inject()
    private val inputClotheList = ArrayList<ClothesModel>()

    companion object {
        const val CLOTHES_ITEMS_KEY = "items"
        const val MAIN_ID_KEY = "mainId"
        const val IS_UPDATING_KEY = "isUpdating"
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_constructor_holder

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()

            toolbar_back_text_view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            toolbar_back_text_view.setOnClickListener(this@CollectionConstructorHolderFragment)
            toolbar_back_text_view.text = context.getString(R.string.close)
            toolbar_back_text_view.show()

            customizeActionToolBar(
                toolbar = this,
                title = context.getString(R.string.constructor_create_outfit_title)
            )
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CLOTHES_ITEMS_KEY)) {
                it.getParcelableArrayList<ClothesModel>(CLOTHES_ITEMS_KEY)?.let { it1 ->
                    inputClotheList.addAll(it1)
                }
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        val bundle = Bundle()

        bundle.putParcelableArrayList(CollectionConstructorFragment.CLOTHES_ITEMS_KEY, inputClotheList)
        bundle.putInt(CollectionConstructorFragment.MAIN_ID_KEY, getIdFromArgs())
        bundle.putBoolean(CollectionConstructorFragment.IS_UPDATING_KEY, isUpdating())
        pagerAdapter = CollectionConstructorPagerAdapter(this, bundle)

        view_pager_fragment_collection_constructor_holder.isUserInputEnabled = false
        view_pager_fragment_collection_constructor_holder.isSaveEnabled = false
    }

    override fun initializeListeners() {}

    override fun onResume() {
        super.onResume()

        view_pager_fragment_collection_constructor_holder.adapter = pagerAdapter

        TabLayoutMediator(
            tab_bar_fragment_collection_constructor_holder,
            view_pager_fragment_collection_constructor_holder
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.for_his)
                1 -> tab.text = getString(R.string.for_her)
            }
        }.attach()

        view_pager_fragment_collection_constructor_holder.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pagerViewModel.setPosition(position)
            }
        })
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_back_text_view -> findNavController().navigateUp()
        }
    }

    private fun getIdFromArgs(): Int = arguments?.getInt(MAIN_ID_KEY) ?: 0

    private fun isUpdating(): Boolean = arguments?.getBoolean(IS_UPDATING_KEY) ?: false
}