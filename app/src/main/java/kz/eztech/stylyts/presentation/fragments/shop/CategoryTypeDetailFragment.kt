package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CategoryTypeDetailAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.fragments.collection.ItemDetailFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.CategoryTypeDetailFragmentPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class CategoryTypeDetailFragment : BaseFragment<MainActivity>(), CategoryTypeDetailContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: CategoryTypeDetailFragmentPresenter
    private lateinit var adapter: CategoryTypeDetailAdapter

    private var gender: String = GENDER_MALE
    private var categoryId: Int = 0
    private var typeId: Int = 0
    private var title: String = EMPTY_STRING

    companion object {
        const val CLOTHES_GENDER = "clothes_gender"
        const val CLOTHES_CATEGORY_ID = "clothes_category_id"
        const val CLOTHES_TYPE_ID = "clothes_type_id"
        const val CLOTHES_CATEGORY_TITLE = "clothes_category_title"
        const val GENDER_MALE = "M"
        const val GENDER_FEMALE = "F"
    }

    override fun getLayoutId(): Int = R.layout.fragment_category_type_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_bottom_border_view.hide()

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@CategoryTypeDetailFragment)

            toolbar_title_text_view.show()

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setOnClickListener(this@CategoryTypeDetailFragment)

            customizeActionToolBar(toolbar = this, title = title)
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
            if (it.containsKey(CLOTHES_GENDER)) {
                gender = it.getString(CLOTHES_GENDER) ?: GENDER_MALE
            }
            if (it.containsKey(CLOTHES_CATEGORY_ID)) {
                categoryId = it.getInt(CLOTHES_CATEGORY_ID)
            }
            if (it.containsKey(CLOTHES_TYPE_ID)) {
                typeId = it.getInt(CLOTHES_TYPE_ID)
            }
            if (it.containsKey(CLOTHES_CATEGORY_TITLE)) {
                title = it.getString(CLOTHES_CATEGORY_TITLE) ?: EMPTY_STRING
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.linear_layout_item_category_type_detail -> {
                item as ClothesModel

                val bundle = Bundle()
                bundle.putInt(ItemDetailFragment.CLOTHES_ID, item.id ?: 0)

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
        recycler_view_fragment_category_type_detail.adapter = adapter
        adapter.itemClickListener = this
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        if (categoryId == 0 && typeId != 0) {
            presenter.getClothesByType(
                token = getTokenFromSharedPref(),
                gender = gender,
                typeId = typeId.toString()
            )
        } else {
            presenter.getCategoryTypeDetail(
                token = getTokenFromSharedPref(),
                gender = gender,
                clothesCategoryId = categoryId.toString()
            )
        }
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

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        resultsModel.results?.let {
            adapter.updateList(list = it)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.toolbar_right_corner_action_image_button -> CartDialog().show(
                childFragmentManager, EMPTY_STRING
            )
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}