package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_cart.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.include_toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.adapters.CartAdapter
import kz.eztech.stylyts.presentation.contracts.main.profile.EditProfileContract

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog: DialogFragment(), EditProfileContract.View {
	private var currentName:String =""
	private var currentSurname:String =""
	private var currentUserName:String =""
	
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.dialog_edit_profile, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		initializeDependency()
		initializeArguments()
		initializeViewsData()
		initializeViews()
		initializeListeners()
		initializePresenter()
		processPostInitialization()
		customizeActionBar()
	}
	
	override fun customizeActionBar() {
		with(include_toolbar_edit_profile){
			image_button_left_corner_action.visibility = View.GONE
			text_view_toolbar_back.visibility = android.view.View.VISIBLE
			text_view_toolbar_back.text = "Отмена"
			text_view_toolbar_back.setCompoundDrawables(null,null,null,null)
			text_view_toolbar_back.setOnClickListener {
				dismiss()
			}
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.text = "Редактировать профиль"
			image_button_right_corner_action.visibility = android.view.View.GONE
			text_view_toolbar_right_text.visibility = View.VISIBLE
			text_view_toolbar_right_text.text = "Готово"
			text_view_toolbar_right_text.setOnClickListener {
				dismiss()
			}
			elevation = 0f
		}
	}
	
	override fun initializeDependency() {
		(requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
	}
	
	override fun initializePresenter() {
	
	}
	
	override fun initializeArguments() {
		arguments?.let {
			if(it.containsKey("currentName")){
				currentName = it.getString("currentName","")
			}
			if(it.containsKey("currentUserName")){
				currentUserName = it.getString("currentUserName","")
			}
			if(it.containsKey("currentSurname")){
				currentSurname = it.getString("currentSurname","")
			}
		}
	}
	
	override fun initializeViewsData() {
	
	}
	
	override fun initializeViews() {
		if(currentName.isNotEmpty()){
			edit_text_dialog_edit_profile_name.setText(currentName)
		}
		
		if(currentUserName.isNotEmpty()){
			edit_text_dialog_edit_profile_username.setText(currentUserName)
		}
		if(currentSurname.isNotEmpty()){
			edit_text_dialog_edit_profile_username.setText(currentUserName)
		}

		if(currentName.isNotEmpty() && currentSurname.isNotEmpty()){
			text_view_fragment_profile_edit_user_short_name.text =
				"${currentName?.toUpperCase()?.get(0)}${currentSurname?.toUpperCase()?.get(0)}"
		}

	}
	
	override fun initializeListeners() {
	
	}
	
	override fun processPostInitialization() {
	}
	
	override fun disposeRequests() {
	
	}
	
	override fun displayMessage(msg: String) {
	
	}
	
	override fun isFragmentVisible(): Boolean {
		return isVisible
	}
	
	override fun displayProgress() {
	
	}
	
	override fun hideProgress() {
	
	}
	
	override fun getTheme(): Int {
		return R.style.FullScreenDialog
	}
}