package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.include_toolbar_profile
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CategoryTypeDetailAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.shop.CategoryTypeDetailFragmentPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject


class CategoryTypeDetailFragment : BaseFragment<MainActivity>(), CategoryTypeDetailContract.View,
    UniversalViewClickListener {

    @Inject
    lateinit var presenter: CategoryTypeDetailFragmentPresenter
    private lateinit var adapter: CategoryTypeDetailAdapter

    private var gender: String = "M"
    private var typeId: Int? = null
    private var title: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_category_type_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager, "Cart")
            }

            customizeActionToolBar(this, title ?: "Одежда")
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("gender")) {
                gender = it.getString("gender") ?: "M"
            }
            if (it.containsKey("typeId")) {
                typeId = it.getInt("typeId")
            }
            if (it.containsKey("title")) {
                title = it.getString("title")
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.linear_layout_item_category_type_detail -> {
                item as ClothesTypeDataModel

                val bundle = Bundle()
                bundle.putInt("itemId", item.id ?: -1)

                findNavController().navigate(
                    R.id.action_categoryTypeDetailFragment_to_itemDetailFragment,
                    bundle
                )
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        adapter = CategoryTypeDetailAdapter()
        recycler_view_fragment_category_type_detail.layoutManager = GridLayoutManager(context, 2)
        recycler_view_fragment_category_type_detail.adapter = adapter
        adapter.itemClickListener = this
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getShopCategoryTypeDetail(typeId ?: 1, gender)
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processTypeDetail(model: CategoryTypeDetailModel) {
        model.clothes?.data?.let {
            adapter.updateList(it)
        }
    }
}