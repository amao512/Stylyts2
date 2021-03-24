package kz.eztech.stylyts.profile.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.data.models.SharedConstants.TOKEN_KEY
import kz.eztech.stylyts.common.domain.models.CollectionFilterModel
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.common.domain.models.MainResult
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.base.EditorListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.common.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.create_outfit.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.create_outfit.presentation.fragments.CameraFragment
import kz.eztech.stylyts.profile.presentation.contracts.ProfileContract
import kz.eztech.stylyts.profile.presentation.dialogs.EditProfileDialog
import kz.eztech.stylyts.profile.presentation.presenters.ProfilePresenter
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener {

    @Inject lateinit var presenter: ProfilePresenter

    private lateinit var adapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter

    private var isOwnProfile: Boolean = true
    private var userId: Int = 0
    private var isMyData = false
    private var currentName = EMPTY_STRING
    private var currentNickname = EMPTY_STRING
    private var currentSurname = EMPTY_STRING

    companion object {
        const val USER_ID_BUNDLE_KEY = "user_id"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_title_text_view.show()
            toolbar_left_corner_action_image_button.show()

            if (isOwnProfile) {
                toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)
                toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_drawer)
                toolbar_right_corner_action_image_button.show()
            } else {
                toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            }

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(this)

    override fun initializeArguments() {
        if (arguments?.getInt(USER_ID_BUNDLE_KEY) != null) {
            isOwnProfile = false
            userId = arguments?.getInt(USER_ID_BUNDLE_KEY) ?: 0
        }
    }

    override fun initializeViewsData() {
        adapterFilter = CollectionsFilterAdapter()
        adapterFilter.setOnClickListener(this)
        adapterFilter.updateList(getCollectionFilterList())

        recycler_view_fragment_profile_filter_list.adapter = adapterFilter
    }

    override fun initializeViews() {
        adapter = GridImageAdapter()
        adapter.setOnClickListener(this)

        recycler_view_fragment_profile_items_list.layoutManager = GridLayoutManager(context, 2)
        recycler_view_fragment_profile_items_list.adapter = adapter
    }

    override fun initializeListeners() {
        include_toolbar_profile.toolbar_right_corner_action_image_button.setOnClickListener(this)
        frame_layout_fragment_profile_my_incomes.setOnClickListener(this)
        frame_layout_fragment_profile_my_addresses.setOnClickListener(this)
        fragment_profile_edit_text_view.setOnClickListener(this)
        frame_layout_fragment_profile_cards.setOnClickListener(this)
        linear_layout_fragment_profile_followers_item.setOnClickListener(this)
        linear_layout_fragment_profile_following_item.setOnClickListener(this)
        linear_layout_fragment_profile_photos_item.setOnClickListener(this)
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        if (isOwnProfile) {
            presenter.getProfile(token = getTokenFromSharedPref())
        } else {
            presenter.getProfileById(
                token = getTokenFromSharedPref(),
                userId = userId.toString()
            )
        }
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() = progress_bar_fragment_profile.show()

    override fun hideProgress() = progress_bar_fragment_profile.hide()

    override fun showMyData() {
        linear_layout_fragment_profile_followers_item.hide()
        linear_layout_fragment_profile_change_buttons.hide()
        linear_layout_fragment_profile_photos_item.hide()
        linear_layout_fragment_profile_following_item.hide()
        recycler_view_fragment_profile_filter_list.hide()
        recycler_view_fragment_profile_items_list.hide()
        frame_layout_fragment_profile_settings_container.show()
    }

    override fun hideMyData() {
        frame_layout_fragment_profile_settings_container.hide()
        linear_layout_fragment_profile_followers_item.show()
        linear_layout_fragment_profile_change_buttons.show()
        linear_layout_fragment_profile_photos_item.show()
        linear_layout_fragment_profile_following_item.show()
        recycler_view_fragment_profile_filter_list.show()
        recycler_view_fragment_profile_items_list.show()
    }

    override fun processMyData() {
        if (isMyData) {
            isMyData = false
            hideMyData()
        } else {
            isMyData = true
            showMyData()
        }
    }

    override fun processProfile(userModel: UserModel) {
        userModel.run {
            userId = id ?: 0
            currentName = name ?: EMPTY_STRING
            currentNickname = username ?: EMPTY_STRING
            currentSurname = lastName ?: EMPTY_STRING

            include_toolbar_profile.toolbar_title_text_view.text = username
            text_view_fragment_profile_user_name.text = name
            fragment_profile_followers_count.text = "${/*followers_count*/ 0}"
            fragment_profile_followings_count.text = "${/*followings_count*/ 0}"
            fragment_profile_photos_count.text = "${0}"

            if (!isOwnProfile) {
                fragment_profile_subscribe_text_view.show()
                fragment_profile_edit_text_view.hide()

                adapterFilter.removeByPosition(position = 5)
                adapterFilter.removeByPosition(position = 4)
                adapterFilter.removeByPosition(position = 3)
            }

            avatar?.let {
                text_view_fragment_profile_user_short_name.hide()

                Glide.with(currentActivity)
                    .load(it)
                    .into(shapeable_image_view_fragment_profile_avatar)
            } ?: run {
                shapeable_image_view_fragment_profile_avatar.hide()
                text_view_fragment_profile_user_short_name.show()
                text_view_fragment_profile_user_short_name.text = getShortName(name, lastName)
            }
        }
    }

    override fun processMyCollections(models: MainLentaModel) {
        models.results?.let {
            adapter.updateList(it)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
            R.id.frame_layout_fragment_profile_my_incomes -> {
                findNavController().navigate(R.id.action_profileFragment_to_profileIncomeFragment)
            }
            R.id.frame_layout_fragment_profile_my_addresses -> {
                findNavController().navigate(R.id.action_profileFragment_to_addressProfileFragment)
            }
            R.id.fragment_profile_edit_text_view -> {
                EditProfileDialog.getNewInstance(
                    token = getTokenFromSharedPref(),
                    name = currentName,
                    surname = currentSurname,
                    editorListener = this
                ).show(childFragmentManager, "Cart")
            }
            R.id.frame_layout_fragment_profile_cards -> {
                findNavController().navigate(R.id.action_profileFragment_to_cardFragment)
            }
            R.id.linear_layout_fragment_profile_followers_item -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.linear_layout_fragment_profile_following_item -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.linear_layout_fragment_profile_photos_item -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.toolbar_left_corner_action_image_button -> {
                if (!isOwnProfile) {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_collection_filter -> onFilterClick(position)
            R.id.shapeable_image_view_item_collection_image -> onCollectionClick(item)
        }
    }

    override fun completeEditing(isSuccess: Boolean) {
        if (isSuccess) {
            presenter.getProfile(token = getTokenFromSharedPref())
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(TOKEN_KEY) ?: EMPTY_STRING
    }

    private fun getCollectionFilterList(): List<CollectionFilterModel> {
        val filterList = mutableListOf<CollectionFilterModel>()

        filterList.add(CollectionFilterModel(
            name = getString(R.string.filter_list_filter),
            icon = R.drawable.ic_filter
        ))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_photo_outfits), isChosen = true))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_wardrobe)))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_my_data)))
        filterList.add(CollectionFilterModel(
            name = getString(R.string.profile_add_by_barcode),
            icon = R.drawable.ic_baseline_qr_code_2_24
        ))
        filterList.add(CollectionFilterModel(
            name = getString(R.string.profile_add_by_photo),
            icon = R.drawable.ic_camera
        ))

        return filterList
    }

    private fun onFilterClick(position: Int) {
        when (position) {
            0 -> {}
            1 -> {
                val map = HashMap<String, Int>()
                map["autor"] = userId
                presenter.getMyCollections(
                    token = getTokenFromSharedPref(),
                    map = map
                )
            }
            2 -> {}
            3 -> processMyData()
            4 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            5 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
        }
    }

    private fun onCollectionClick(item: Any?) {
        item as MainResult

        val bundle = Bundle()
        bundle.putParcelable("model", item)

        findNavController().navigate(R.id.action_profileFragment_to_collectionDetailFragment, bundle)
    }

    private fun navigateToCameraFragment(mode: Int) {
        val bundle = Bundle()
        bundle.putInt("mode", mode)

        findNavController().navigate(
            R.id.action_profileFragment_to_cameraFragment,
            bundle
        )
    }
}