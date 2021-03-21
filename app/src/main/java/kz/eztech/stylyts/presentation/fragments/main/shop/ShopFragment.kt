package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kotlinx.android.synthetic.main.fragment_shop.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ShopViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class ShopFragment : BaseFragment<MainActivity>(), ShopContract.View, UniversalViewClickListener,
    View.OnClickListener{

    private lateinit var shopViewPagerAdapter: ShopViewPagerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_shop

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.hide()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.show()

            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager, "Cart")
            }
            elevation = 0f
            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
            customizeActionToolBar(this, getString(R.string.fragment_registration_appbar_title))
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        shopViewPagerAdapter = ShopViewPagerAdapter(this, this)
        fragment_shop_view_pager.isSaveEnabled = false
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
        fragment_shop_view_pager.adapter = shopViewPagerAdapter
        TabLayoutMediator(fragment_shop_tab_layout, fragment_shop_view_pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Для него"
                1 -> tab.text = "Для нее"
            }
        }.attach()
    }

    override fun initializeListeners() {
        fragment_shop_search_view.setOnClickListener(this)
        fragment_shop_search_view.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.action_shopFragment_to_searchFragment)
            }
        }
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        with(item as HashMap<String, Any>) {
            val currentItem = this["currentItem"] as GenderCategory
            val currentGender = this["currentGender"] as Int

            val bundle = Bundle()
            bundle.putInt("currentGender", currentGender)
            bundle.putParcelable("currentItem", currentItem)
            findNavController().navigate(R.id.action_shopFragment_to_shopItemListFragment, bundle)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_shop_search_view -> findNavController().navigate(R.id.action_shopFragment_to_searchFragment)
        }
    }
}