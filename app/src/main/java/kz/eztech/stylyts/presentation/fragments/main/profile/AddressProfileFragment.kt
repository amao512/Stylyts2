package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_cart.*
import kotlinx.android.synthetic.main.fragment_address_profile.*
import kotlinx.android.synthetic.main.fragment_user_comments.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CartEntity
import kz.eztech.stylyts.data.db.entities.CartMapper
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.AddressAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.AddressProfileContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import java.text.NumberFormat
import javax.inject.Inject

class AddressProfileFragment : BaseFragment<MainActivity>(), AddressProfileContract.View,View.OnClickListener,UniversalViewClickListener{

	@Inject
	lateinit var ds: LocalDataSource
	private var disposables = CompositeDisposable()
	private var addressAdapter : AddressAdapter? = null
	private var currentAddress: AddressEntity? = null
	private var isForm = false
	override fun getLayoutId(): Int {
		return R.layout.fragment_address_profile
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {
		with(include_toolbar_addresses){
			image_button_left_corner_action.visibility = View.VISIBLE
			text_view_toolbar_back.visibility = View.VISIBLE
			text_view_toolbar_title.visibility = View.VISIBLE
			text_view_toolbar_title.text = "Добавить Адрес"
			text_view_toolbar_back.setOnClickListener {
				if(isForm){
					displayContent()
				}else{
					findNavController().navigateUp()
				}
			}
			image_button_right_corner_action.visibility = View.GONE
		}

	}
	
	override fun initializeDependency() {
		(currentActivity.application as StylytsApp).applicationComponent.inject(this)
	}
	
	override fun initializePresenter() {
	
	}
	
	override fun initializeArguments() {
	
	}
	
	override fun initializeViewsData() {
		addressAdapter = AddressAdapter()
	}
	
	override fun initializeViews() {
		recycler_view_fragment_address_profile.layoutManager = LinearLayoutManager(currentActivity)
		recycler_view_fragment_address_profile.adapter = addressAdapter
	}
	
	override fun initializeListeners() {
		linear_layout_fragment_address_profile_add_address.setOnClickListener(this)
		linear_layout_fragment_address_profile_create_address.setOnClickListener(this)
		addressAdapter?.itemClickListener = this
	}
	
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(item){
			is AddressEntity -> {
				currentAddress = item
				displayExistForm(item)
			}
		}
	}
	
	override fun onClick(v: View?) {
		when(v?.id){
			R.id.linear_layout_fragment_address_profile_add_address -> {
				displayForm()
			}
			R.id.linear_layout_fragment_address_profile_create_address -> {
				disposables.clear()
				currentAddress?.let {
					it.apply {
						this.name = edit_Text_fragment_address_profile_name.text.toString()
						this.surname = edit_Text_fragment_address_profile_surname.text.toString()
						this.contact = edit_Text_fragment_address_profile_contact.text.toString()
						this.phone = edit_Text_fragment_address_profile_phone.text.toString()
						this.country = edit_Text_fragment_address_profile_country.text.toString()
						this.address = edit_Text_fragment_address_profile_address.text.toString()
						this.point = edit_Text_fragment_address_profile_point.text.toString()
						this.postIndex = edit_Text_fragment_address_profile_post.text.toString()
					}
					disposables.add(
							ds.updateAddress(it).
							subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
								processPostInitialization()
							}
					)
				} ?: run{
					disposables.add(
							ds.insertAddress(AddressEntity(
									name = edit_Text_fragment_address_profile_name.text.toString(),
									surname = edit_Text_fragment_address_profile_surname.text.toString(),
									contact = edit_Text_fragment_address_profile_contact.text.toString(),
									phone = edit_Text_fragment_address_profile_phone.text.toString(),
									country = edit_Text_fragment_address_profile_country.text.toString(),
									address = edit_Text_fragment_address_profile_address.text.toString(),
									point = edit_Text_fragment_address_profile_point.text.toString(),
									postIndex = edit_Text_fragment_address_profile_post.text.toString()
							)).
							subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
								processPostInitialization()
							}
					)
				}
				
			}
		}
	}

	override fun processPostInitialization() {
		displayContent()
		getList()
	}
	
	override fun disposeRequests() {
	
	}
	
	override fun displayMessage(msg: String) {
	
	}
	
	override fun isFragmentVisible(): Boolean {
		return isVisible
	}
	
	override fun displayProgress() {
		base_progress_address_profile?.visibility = View.VISIBLE
	}
	
	override fun hideProgress() {
		base_progress_address_profile?.visibility = View.GONE
	}

	override fun displayContent() {
		isForm = false
		linear_layout_fragment_address_profile_addresses.visibility = View.VISIBLE
		scroll_view_fragment_address_profile.visibility = View.GONE

	}

	override fun displayForm() {
		isForm = true
		scroll_view_fragment_address_profile.visibility = View.VISIBLE
		linear_layout_fragment_address_profile_addresses.visibility = View.GONE
		edit_Text_fragment_address_profile_phone.setText("")
		edit_Text_fragment_address_profile_country.setText("")
		edit_Text_fragment_address_profile_address.setText("")
		edit_Text_fragment_address_profile_point.setText("")
		edit_Text_fragment_address_profile_post.setText("")
	}
	
	private fun displayExistForm(entity:AddressEntity){
		isForm = true
		scroll_view_fragment_address_profile.visibility = View.VISIBLE
		linear_layout_fragment_address_profile_addresses.visibility = View.GONE
		
		edit_Text_fragment_address_profile_name.setText(entity.name)
		edit_Text_fragment_address_profile_surname.setText(entity.surname)
		edit_Text_fragment_address_profile_contact.setText(entity.contact)
		edit_Text_fragment_address_profile_phone.setText(entity.phone)
		edit_Text_fragment_address_profile_country.setText(entity.country)
		edit_Text_fragment_address_profile_address.setText(entity.address)
		edit_Text_fragment_address_profile_point.setText(entity.point)
		edit_Text_fragment_address_profile_post.setText(entity.postIndex)
	}
	private fun showEmptyPage(){
		text_view_fragment_address_profile_empty?.visibility = View.VISIBLE
	}
	private fun hideEmptyPage(){
		text_view_fragment_address_profile_empty?.visibility = View.GONE
	}

	private fun processList(list:List<AddressEntity>){
		addressAdapter?.updateList(list)
		addressAdapter?.notifyDataSetChanged()
	}


	private fun getList(){
		displayProgress()
		disposables.clear()
		disposables.add(
			ds.allAddresses.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
				hideProgress()
				if(it.isEmpty()){
					showEmptyPage()
				}else{
					hideEmptyPage()
					processList(it)
				}
			}
		)
	}
	override fun onResume() {
		super.onResume()
		currentActivity.hideBottomNavigationView()
	}
}