package kz.eztech.stylyts.presentation.fragments.main.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants.TOKEN_KEY
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import kz.eztech.stylyts.presentation.presenters.main.profile.ProfilePresenter
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View,View.OnClickListener{
	
	private lateinit var adapter:GridImageAdapter
	private lateinit var adapterFilter:CollectionsFilterAdapter
	private var isSettings = false
	
	@Inject
	lateinit var presenter: ProfilePresenter
	override fun getLayoutId(): Int {
		return R.layout.fragment_profile
	}

	override fun getContractView(): BaseView {
		return this
	}

	override fun customizeActionBar() {
		with(include_toolbar_profile){
			image_button_left_corner_action.visibility = android.view.View.VISIBLE
			image_button_left_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_person_add)
			text_view_toolbar_back.visibility = android.view.View.GONE
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_drawer)
			elevation = 0f
			customizeActionToolBar(this)
		}
	}

	override fun initializeDependency() {
		(currentActivity.application as StylytsApp).applicationComponent.inject(this)
	}

	override fun initializePresenter() {
		presenter.attach(this)
	}

	override fun initializeArguments() {}

	override fun initializeViewsData() {
		val filterList = ArrayList<CollectionFilterModel>()
		filterList.add(CollectionFilterModel("Фильтр"))
		filterList.add(CollectionFilterModel("Фото образов"))
		filterList.add(CollectionFilterModel("Гардеров (355)"))
		filterList.add(CollectionFilterModel("Мои данные"))
		adapterFilter = CollectionsFilterAdapter()
		recycler_view_fragment_profile_filter_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
		recycler_view_fragment_profile_filter_list.adapter = adapterFilter
		adapterFilter.updateList(filterList)
	}

	override fun initializeViews() {
		currentActivity.displayBottomNavigationView()
		val dummyList = ArrayList<MainImageModel>()
		for(i in 0..5){
			val listAdditional = ArrayList<MainImageAdditionalModel>()
			for (i in 0..5){
				listAdditional.add(MainImageAdditionalModel(name = "Hello"))
			}
			dummyList.add(MainImageModel(name = "zara ${dummyList.count()}",additionals = listAdditional))
		}
		adapter = GridImageAdapter()
		recycler_view_fragment_profile_items_list.layoutManager = GridLayoutManager(context,2)
		recycler_view_fragment_profile_items_list.adapter = adapter
		adapter.updateList(dummyList)
	}

	override fun processSettings() {
		if(isSettings){
			isSettings = false
			hideSettings()
		}else{
			isSettings = true
			showSettings()
		}
	}

	override fun showSettings() {
		recycler_view_fragment_profile_filter_list.visibility = View.GONE
		recycler_view_fragment_profile_items_list.visibility = View.GONE
		frame_layout_fragment_profile_settings_container.visibility = View.VISIBLE
	}

	override fun hideSettings() {
		recycler_view_fragment_profile_filter_list.visibility = View.VISIBLE
		recycler_view_fragment_profile_items_list.visibility = View.VISIBLE
		frame_layout_fragment_profile_settings_container.visibility = View.GONE
	}

	override fun initializeListeners() {
		include_toolbar_profile.image_button_right_corner_action.setOnClickListener(this)
		frame_layout_fragment_profile_my_incomes.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.image_button_right_corner_action -> {
				processSettings()
			}
			R.id.frame_layout_fragment_profile_my_incomes->{
				findNavController().navigate(R.id.action_profileFragment_to_profileIncomeFragment)
			}
		}
	}

	override fun processPostInitialization() {
		presenter.getProfile(currentActivity.getSharedPrefByKey<String>(TOKEN_KEY)?:"")
	}

	override fun disposeRequests() {
		presenter.disposeRequests()
	}

	override fun displayMessage(msg: String) {
		displayToast(msg)
	}

	override fun isFragmentVisible(): Boolean {
		return isVisible
	}
	override fun displayProgress() {
		progress_bar_fragment_profile.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progress_bar_fragment_profile.visibility = View.GONE
	}
	
	override fun processProfile(user: UserModel) {
		user.run {
			text_view_fragment_profile_user_name.text = first_name
			text_view_fragment_profile_user_followers_count.text = "${followers_count ?: 0}"
			text_view_fragment_profile_user_followings_count.text = "${followings_count ?: 0}"
			avatar?.let {
				text_view_fragment_profile_user_short_name.visibility = View.GONE
				Glide.with(currentActivity).load(it).into(shapeable_image_view_fragment_profile_avatar)
			} ?: run{
				shapeable_image_view_fragment_profile_avatar.visibility = View.GONE
				text_view_fragment_profile_user_short_name.visibility = View.VISIBLE
				text_view_fragment_profile_user_short_name.text =
						"${first_name?.toUpperCase()?.get(0)}${last_name?.toUpperCase()?.get(0)}"
				
			}
		}
		
	}
}