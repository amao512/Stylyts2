package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_card.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileCardContract


class ProfileCardFragment: BaseFragment<MainActivity>(), ProfileCardContract.View{
	
	override fun getLayoutId(): Int {
		return R.layout.fragment_profile_card
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {
		with(include_toolbar_profile_card){
			text_view_toolbar_back.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.visibility = android.view.View.GONE
			elevation = 0f
			customizeActionToolBar(this,"Добавить способ оплаты")
		}
	}
	
	override fun initializeDependency() {
	
	}
	
	override fun initializePresenter() {
	
	}
	
	override fun initializeArguments() {
		TODO("Not yet implemented")
	}
	
	override fun initializeViewsData() {
		TODO("Not yet implemented")
	}
	
	override fun initializeViews() {
		TODO("Not yet implemented")
	}
	
	override fun initializeListeners() {
		TODO("Not yet implemented")
	}
	
	override fun processPostInitialization() {
		TODO("Not yet implemented")
	}
	
	override fun disposeRequests() {
		TODO("Not yet implemented")
	}
	
	override fun displayMessage(msg: String) {
		TODO("Not yet implemented")
	}
	
	override fun isFragmentVisible(): Boolean {
		TODO("Not yet implemented")
	}
	
	override fun displayProgress() {
		TODO("Not yet implemented")
	}
	
	override fun hideProgress() {
	
	}
}