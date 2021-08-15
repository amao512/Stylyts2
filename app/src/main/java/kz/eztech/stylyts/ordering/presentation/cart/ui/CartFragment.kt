package kz.eztech.stylyts.ordering.presentation.cart.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCountModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.ordering.presentation.cart.ui.adapters.CartAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.ordering.presentation.cart.contracts.CartContract
import kz.eztech.stylyts.ordering.presentation.cart.ui.dialogs.ClothesCountsBottomDialog
import kz.eztech.stylyts.ordering.presentation.cart.ui.dialogs.ClothesSizesBottomDialog
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.ordering.presentation.cart.presenters.CartPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.extensions.displaySnackBar
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class CartFragment : BaseFragment<MainActivity>(), View.OnClickListener, UniversalViewClickListener,
    DialogChooserListener, CartContract.View {

    @Inject lateinit var presenter: CartPresenter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sizeChooserDialog: ClothesSizesBottomDialog
    private lateinit var countsChooserDialog: ClothesCountsBottomDialog

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_cart

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_cart_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.my_basket))
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
        cartAdapter = CartAdapter()
        cartAdapter.itemClickListener = this
        recycler_view_dialog_cart_list.adapter = cartAdapter

        sizeChooserDialog = ClothesSizesBottomDialog()
        sizeChooserDialog.setChoiceListener(listener = this)

        countsChooserDialog = ClothesCountsBottomDialog()
        countsChooserDialog.setChoiceListener(listener = this)
    }

    override fun initializeViews() {}

    override fun initializeListeners() {
        dialog_cart_ordering_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getCartList()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        view?.let {
            displaySnackBar(context = it.context, view = it, msg = msg)
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_cart_clothes_remove_image_view -> presenter.removeCart(item as CartEntity)
            R.id.frame_layout_item_cart_item_size -> onSizeStockClicked(item)
            R.id.frame_layout_item_cart_item_count -> onSizeStockClicked(item, isSize = false)
            R.id.item_cart_clothes_image_view -> navigateToClothes(item as CartEntity)
            R.id.item_cart_clothes_title_text_view -> navigateToClothes(item as CartEntity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_cart_ordering_button -> findNavController().navigate(R.id.action_cartFragment_to_customerInfoFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        disposeRequests()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is ClothesSizeModel -> presenter.selectSize(clothesSizeModel = item)
            is ClothesCountModel -> presenter.selectCount(clothesCountModel = item)
        }
    }

    override fun processCartList(list: List<CartEntity>) {
        if (list.isEmpty()) {
            showEmptyPage()
        } else {
            processList(list)
        }
    }

    private fun showEmptyPage() {
        recycler_view_dialog_cart_list.hide()
        dialog_cart_price_container_contraint_layout.hide()
        linear_layout_dialog_cart_empty_page.show()
        dialog_cart_ordering_button.hide()
    }

    private fun processList(list: List<CartEntity>) {
        dialog_cart_price_container_contraint_layout.show()
        linear_layout_dialog_cart_empty_page.hide()
        recycler_view_dialog_cart_list.show()
        dialog_cart_ordering_button.show()

        cartAdapter.updateList(list)

        val fullPrice = NumberFormat.getInstance().format(
            list.sumBy {
                if (it.salePrice != 0) {
                    it.salePrice!!
                } else {
                    it.price!!
                }
            }
        )

        text_view_dialog_cart_total_price.text = getString(
            R.string.price_tenge_text_format, fullPrice
        )

        text_view_dialog_cart_pre_total_price.text = "Подытог ${
            getString(R.string.price_tenge_text_format, fullPrice)
        }"
    }

    override fun processSizes(
        clothesModel: ClothesModel,
        cartEntity: CartEntity,
        isSize: Boolean
    ) {
        val currentSize = clothesModel.sizeInStock.find { it.size == cartEntity.size }

        if (isSize) {
            openSizeDialog(clothesModel, cartEntity)
        } else {
            currentSize?.count?.let {
                openCountsDialog(clothesModel, currentSize, cartEntity)
            }
        }
    }

    private fun openSizeDialog(
        clothesModel: ClothesModel,
        cartEntity: CartEntity
    ) {
        val bundle = Bundle()
        val clothesSizes: MutableList<ClothesSizeModel> = mutableListOf()

        if (clothesModel.sizeInStock.isNotEmpty()) {
            clothesModel.sizeInStock.map { size ->
                clothesSizes.add(
                    ClothesSizeModel(
                        clothesId = cartEntity.id ?: 0,
                        size = size.size,
                        count = size.count,
                        price = clothesModel.cost,
                        salePrice = clothesModel.salePrice
                    )
                )
            }

            bundle.putParcelableArrayList(
                ClothesSizesBottomDialog.SIZES_KEY,
                ArrayList(clothesSizes)
            )
            sizeChooserDialog.arguments = bundle
            sizeChooserDialog.show(parentFragmentManager, EMPTY_STRING)
        } else {
            displayMessage(msg = getString(R.string.there_are_not_sizes))
        }
    }

    private fun openCountsDialog(
        clothesModel: ClothesModel,
        clothesSizeModel: ClothesSizeModel,
        cartEntity: CartEntity
    ) {
        val bundle = Bundle()
        val counts = ArrayList<ClothesCountModel>()

        for (i in 1..clothesSizeModel.count) {
            counts.add(
                ClothesCountModel(
                    clothesId = cartEntity.id ?: 0,
                    count = i,
                    price = clothesModel.cost,
                    salePrice = clothesModel.salePrice
                )
            )
        }

        bundle.putParcelableArrayList(ClothesCountsBottomDialog.COUNTS_KEY, counts)

        countsChooserDialog.arguments = bundle
        countsChooserDialog.show(parentFragmentManager, EMPTY_STRING)
    }

    private fun onSizeStockClicked(
        item: Any?,
        isSize: Boolean = true
    ) {
        item as CartEntity

        item.let {
            presenter.getSizes(
                clothesId = it.id ?: 0,
                cartEntity = item,
                isSize = isSize
            )
        }
    }

    private fun navigateToClothes(cartEntity: CartEntity) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, cartEntity.id!!)

        findNavController().navigate(R.id.action_cartFragment_to_clothesDetailFragment, bundle)
    }
}