package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants.TOKEN_KEY
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import kz.eztech.stylyts.presentation.dialogs.EditProfileDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.profile.ProfilePresenter
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener {

    private lateinit var adapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter
    private var userId: Int = 0
    private var isSettings = false
    private var currentName = ""
    private var currentNickname = ""
    private var currentSurname = ""

    @Inject
    lateinit var presenter: ProfilePresenter
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
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
        filterList.add(CollectionFilterModel(name = "Фото образов", isChosen = true))
        filterList.add(CollectionFilterModel("Публикации"))
        filterList.add(CollectionFilterModel("Гардеров"))
        filterList.add(CollectionFilterModel("Мои данные"))
        adapterFilter = CollectionsFilterAdapter()
        adapterFilter.setOnClickListener(this)
        recycler_view_fragment_profile_filter_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_fragment_profile_filter_list.adapter = adapterFilter
        adapterFilter.updateList(filterList)
    }

    override fun initializeViews() {
        adapter = GridImageAdapter()
        adapter.setOnClickListener(this)
        recycler_view_fragment_profile_items_list.layoutManager = GridLayoutManager(context, 2)
        recycler_view_fragment_profile_items_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun processSettings() {
        if (isSettings) {
            isSettings = false
            hideSettings()
        } else {
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
        frame_layout_fragment_profile_my_addresses.setOnClickListener(this)
        text_view_fragment_profile_settings.setOnClickListener(this)
        frame_layout_fragment_profile_cards.setOnClickListener(this)
        linearLayout.setOnClickListener(this)
        subs.setOnClickListener(this)
        sub.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.image_button_right_corner_action -> {
                processSettings()
            }
            R.id.frame_layout_fragment_profile_my_incomes -> {
                findNavController().navigate(R.id.action_profileFragment_to_profileIncomeFragment)
            }
            R.id.frame_layout_fragment_profile_my_addresses -> {
                findNavController().navigate(R.id.action_profileFragment_to_addressProfileFragment)
            }
            R.id.text_view_fragment_profile_settings -> {
                val editProfileDialog = EditProfileDialog()
                val bundle = Bundle()
                bundle.putString("currentName", currentName)
                bundle.putString("currentUserName", currentName)
                bundle.putString("currentSurname", currentSurname)
                editProfileDialog.show(childFragmentManager, "Cart")
            }
            R.id.frame_layout_fragment_profile_cards -> {
                findNavController().navigate(R.id.action_profileFragment_to_cardFragment)
            }
            R.id.linearLayout -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.subs -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.sub -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_collection_filter -> {
                when (position) {
                    0 -> {

                    }
                    1 -> {
                        val map = HashMap<String, Int>()
                        map["autor"] = userId
                        presenter.getMyCollections(
                            currentActivity.getSharedPrefByKey<String>(
                                TOKEN_KEY
                            ) ?: "", map
                        )
                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }
            }
            R.id.shapeable_image_view_item_collection_image -> {
                item as MainResult
                val bundle = Bundle()
                bundle.putParcelable("model", item)
                findNavController().navigate(
                    R.id.action_profileFragment_to_collectionDetailFragment,
                    bundle
                )
            }
        }

    }

    override fun processPostInitialization() {
        presenter.getProfile(currentActivity.getSharedPrefByKey<String>(TOKEN_KEY) ?: "")
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

    override fun processProfile(profileModel: ProfileModel) {
        profileModel.run {
            userId = id ?: 0
            currentName = name ?: ""
            currentNickname = ""
            currentSurname = lastName ?: ""
            text_view_fragment_profile_user_name.text = name
            text_view_fragment_profile_user_followers_count.text = "${/*followers_count*/ 0}"
            text_view_fragment_profile_user_followings_count.text = "${/*followings_count*/ 0}"
            avatar?.let {
                text_view_fragment_profile_user_short_name.visibility = View.GONE
                Glide.with(currentActivity).load(it)
                    .into(shapeable_image_view_fragment_profile_avatar)
            } ?: run {
                shapeable_image_view_fragment_profile_avatar.visibility = View.GONE
                text_view_fragment_profile_user_short_name.visibility = View.VISIBLE
                text_view_fragment_profile_user_short_name.text =
                    "${name?.toUpperCase()?.get(0)}${lastName?.toUpperCase()?.get(0)}"

            }
        }

    }

    override fun processMyCollections(models: MainLentaModel) {
        models.results?.let {
            adapter.updateList(it)
        }
    }
}