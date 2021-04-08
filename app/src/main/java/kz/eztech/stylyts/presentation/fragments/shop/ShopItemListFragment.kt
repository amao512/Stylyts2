package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_item_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ShopItemListAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemListContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemListPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopItemListFragment : BaseFragment<MainActivity>(), ShopItemListContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: ShopItemListPresenter

    private lateinit var adapter: ShopItemListAdapter
    private lateinit var clothesType: ClothesTypeModel

    private var clothesTypeGender: Int = 0

    companion object {
        const val CLOTHES_TYPE_GENDER = "clothes_type_gender"
        const val CLOTHES_TYPE = "clothes_type_id"
    }

    override fun getLayoutId(): Int = R.layout.fragment_shop_item_list

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

            customizeActionToolBar(
                toolbar = this,
                title = clothesType.title ?: "Одежда"
            )
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CLOTHES_TYPE)) {
                it.getParcelable<ClothesTypeModel>(CLOTHES_TYPE)?.let { clothesTypeModel ->
                    clothesType = clothesTypeModel
                }
            }

            if (it.containsKey(CLOTHES_TYPE_GENDER)) {
                clothesTypeGender = it.getInt(CLOTHES_TYPE_GENDER)
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        adapter = ShopItemListAdapter()
        recycler_view_fragment_shop_item_list.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_shop_item_list.adapter = adapter
        adapter.itemClickListener = this
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getCategoriesByType(
            token = getTokenFromSharedPref(),
            clothesTypeId = clothesType.id ?: 0
        )
    }

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
        with(item as ClothesCategoryModel) {
            val bundle = Bundle()
            val gender = when (clothesTypeGender) {
                0 -> CategoryTypeDetailFragment.GENDER_MALE
                1 -> CategoryTypeDetailFragment.GENDER_FEMALE
                else -> CategoryTypeDetailFragment.GENDER_MALE
            }
            bundle.putString(CategoryTypeDetailFragment.CLOTHES_GENDER, gender)
            bundle.putInt(CategoryTypeDetailFragment.CLOTHES_CATEGORY_ID, id ?: 1)
            bundle.putString(CategoryTypeDetailFragment.CLOTHES_CATEGORY_TITLE, title)

            findNavController().navigate(
                R.id.action_shopItemListFragment_to_categoryTypeDetailFragment,
                bundle
            )
        }
    }

    override fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>) {
        resultsModel.results?.let {
            adapter.updateList(list = it)
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}