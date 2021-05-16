package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_cart.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.presentation.adapters.CartAdapter
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class CartDialog : DialogFragment(), View.OnClickListener, UniversalViewClickListener {

    @Inject lateinit var ds: CartDataSource
    private lateinit var cartAdapter: CartAdapter

    private var disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializeViewsData()
        initializeViews()

        initializeListeners()
        getList()
    }

    private fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
    }

    private fun initializeViewsData() {
        cartAdapter = CartAdapter()
        cartAdapter.itemClickListener = this
        recycler_view_dialog_cart_list.adapter = cartAdapter
    }

    private fun initializeViews() {
        include_base_progress_dialog_cart.show()
    }

    private fun initializeListeners() {
        image_view_dialog_cart_close.setOnClickListener(this)
        dialog_cart_ordering_button.setOnClickListener(this)
    }

    private fun getList() {
        disposables.clear()
        disposables.add(
            ds.allCart
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    include_base_progress_dialog_cart.hide()
                    if (it.isEmpty()) {
                        showEmptyPage()
                    } else {
                        processList(it)
                    }
                }
        )
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

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_cart_clothes_remove_image_view -> {
                include_base_progress_dialog_cart.show()
                disposables.clear()
                disposables.add(
                    ds.delete(item as CartEntity).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            getList()
                        }
                )
            }
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
        disposables.clear()
    }
}