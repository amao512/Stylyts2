package kz.eztech.stylyts.common.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_cart.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.data.db.LocalDataSource
import kz.eztech.stylyts.common.data.db.entities.CartEntity
import kz.eztech.stylyts.common.data.db.entities.CartMapper
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.presentation.adapters.CartAdapter
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class CartDialog: DialogFragment(), View.OnClickListener, UniversalViewClickListener {

	private var disposables = CompositeDisposable()
	private lateinit var cartAdapter: CartAdapter
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.dialog_cart, container, false)
	}

	@Inject lateinit var ds: LocalDataSource


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		include_base_progress_dialog_cart.visibility = View.VISIBLE
		(requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)

		cartAdapter = CartAdapter()
		cartAdapter.itemClickListener = this
		recycler_view_dialog_cart_list.layoutManager = LinearLayoutManager(requireContext())
		recycler_view_dialog_cart_list.adapter = cartAdapter


		initializeListeners()
		getList()
	}

	private fun getList(){
		disposables.clear()
		disposables.add(
			ds.allItems.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
				include_base_progress_dialog_cart.visibility = View.GONE
				if(it.isEmpty()){
					showEmptyPage()
				}else{
					processList(it)
				}
			}
		)
	}

	private fun initializeListeners(){
		image_view_dialog_cart_close.setOnClickListener(this)
	}

	private fun showEmptyPage(){
		recycler_view_dialog_cart_list.visibility = View.GONE
		frame_layout_dialog_cart_price_container.visibility = View.GONE
		linear_layout_dialog_cart_empty_page.visibility = View.VISIBLE
	}

	private fun processList(list:List<CartEntity>){
		frame_layout_dialog_cart_price_container.visibility = View.VISIBLE
		linear_layout_dialog_cart_empty_page.visibility = View.GONE
		recycler_view_dialog_cart_list.visibility = View.VISIBLE

		val models = ArrayList<ClothesMainModel>()
		list.forEach {
			models.add(CartMapper.mapToClotheMainModel(it))
		}
		cartAdapter.updateList(list)
		text_view_dialog_cart_total_price.text = "${NumberFormat.getInstance().format(models.sumBy { it.cost!! })} ${models[0].currency}"
		text_view_dialog_cart_pre_total_price.text = "Подытог ${NumberFormat.getInstance().format(models.sumBy { it.cost!! })} ${models[0].currency}"
	}

	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(view?.id){
			R.id.image_view_item_cart_close -> {
				include_base_progress_dialog_cart.visibility = View.VISIBLE
				disposables.clear()
				disposables.add(
					ds.delete(item as CartEntity).
					subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
						getList()
					}
				)

			}
		}
	}

	override fun getTheme(): Int {
		return R.style.FullScreenDialog
	}
	override fun onClick(v: View?) {
		when(v?.id){
			R.id.image_view_dialog_cart_close -> {
				dismiss()
			}
		}
	}

	override fun onStop() {
		super.onStop()
		disposables.clear()
	}
}