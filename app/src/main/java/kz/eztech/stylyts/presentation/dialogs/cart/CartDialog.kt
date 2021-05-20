package kz.eztech.stylyts.presentation.dialogs.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.dialog_cart.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.adapters.ordering.CartAdapter
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.cart.CartContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.cart.CartPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.displayToast
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class CartDialog : DialogFragment(), View.OnClickListener, UniversalViewClickListener,
    DialogChooserListener, CartContract.View {

    @Inject lateinit var presenter: CartPresenter
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sizeChooserDialog: ClothesSizesBottomDialog
    private lateinit var countsChooserDialog: ClothesCountsBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializePresenter()
        initializeViewsData()
        initializeViews()

        initializeListeners()
        processPostInitialization()
    }

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(dialog = this)
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
        image_view_dialog_cart_close.setOnClickListener(this)
        dialog_cart_ordering_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getCartList()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(context = view?.context, msg)
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
            R.id.frame_layout_item_cart_item_count -> onCountsStockClicked(item)
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.image_view_dialog_cart_close -> dismiss()
            R.id.dialog_cart_ordering_button -> {
                findNavController().navigate(R.id.nav_ordering)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposeRequests()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is ClothesSizeModel -> presenter.selectSize(clothesSizeModel = item)
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
        frame_layout_dialog_cart_price_container.hide()
        linear_layout_dialog_cart_empty_page.show()
    }

    private fun processList(list: List<CartEntity>) {
        frame_layout_dialog_cart_price_container.show()
        linear_layout_dialog_cart_empty_page.hide()
        recycler_view_dialog_cart_list.show()

        cartAdapter.updateList(list)

        text_view_dialog_cart_total_price.text = getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(list.sumBy { it.price!! })
        )

        text_view_dialog_cart_pre_total_price.text = "Подытог ${getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(list.sumBy { it.price!! })
        )}"
    }

    private fun onSizeStockClicked(item: Any?) {
        item as CartEntity

        item.sizeList?.let {
            if (it.isNotEmpty()) {
                val bundle = Bundle()
                var counter = 0
                val clothesSizes: MutableList<ClothesSizeModel> = mutableListOf()

                it.map { size ->
                    clothesSizes.add(
                        ClothesSizeModel(
                            clothesId = item.id ?: 0,
                            size = size,
                            count = 1
                        )
                    )
                    counter++
                }

                bundle.putParcelableArrayList(
                    ClothesSizesBottomDialog.SIZES_KEY,
                    ArrayList(clothesSizes)
                )
                sizeChooserDialog.arguments = bundle
                sizeChooserDialog.show(parentFragmentManager, EMPTY_STRING)
            }
        }
    }

    private fun onCountsStockClicked(item: Any?) {
        item as CartEntity

        val bundle = Bundle()
        val counts = ArrayList<Int>()

        counts.add(1)
        counts.add(2)
        counts.add(3)
        counts.add(4)
        counts.add(5)

        bundle.putIntegerArrayList(ClothesCountsBottomDialog.COUNTS_KEY, counts)

        countsChooserDialog.arguments = bundle
        countsChooserDialog.show(parentFragmentManager, EMPTY_STRING)
    }
}