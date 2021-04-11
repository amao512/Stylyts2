package kz.eztech.stylyts.presentation.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants.ACCESS_TOKEN_KEY
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.profile.ProfileContract
import kz.eztech.stylyts.presentation.dialogs.profile.CreatorChooserDialog
import kz.eztech.stylyts.presentation.dialogs.profile.EditProfileDialog
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.ProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener, DialogChooserListener {

    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var imageLoader: DomainImageLoader

    private lateinit var gridAdapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter

    private var isOwnProfile: Boolean = true
    private var userId: Int = 0
    private var isMyData = false
    private var currentName = EMPTY_STRING
    private var currentNickname = EMPTY_STRING
    private var currentSurname = EMPTY_STRING
    private var currentGender = EMPTY_STRING
    private var chosenFilterPosition = 1

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
        arguments?.getInt(USER_ID_BUNDLE_KEY)?.let {
            isOwnProfile = false
            userId = it
        }
    }

    override fun initializeViewsData() {
        adapterFilter = CollectionsFilterAdapter()
        gridAdapter = GridImageAdapter()
    }

    override fun initializeViews() {
        recycler_view_fragment_profile_filter_list.adapter = adapterFilter
        recycler_view_fragment_profile_items_list.adapter = gridAdapter
        recycler_view_fragment_profile_items_list.addItemDecoration(GridSpacesItemDecoration(space = 16))
    }

    override fun initializeListeners() {
        adapterFilter.setOnClickListener(this)
        gridAdapter.setOnClickListener(this)

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
        presenter.getFilerList(isOwnProfile)
        presenter.getProfile(
            token = getTokenFromSharedPref(),
            userId = userId.toString(),
            isOwnProfile = isOwnProfile
        )

        when (chosenFilterPosition) {
            1 -> presenter.getPublications(
                token = getTokenFromSharedPref(),
                isOwnProfile = isOwnProfile
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
            currentName = firstName ?: EMPTY_STRING
            currentNickname = username ?: EMPTY_STRING
            currentSurname = lastName ?: EMPTY_STRING
            currentGender = gender ?: "M"
        }

        fillProfileInfo(userModel = userModel)
        loadProfilePhoto(userModel = userModel)
    }

    override fun processFilter(filterList: List<CollectionFilterModel>) {
        adapterFilter.updateList(filterList)
    }

    override fun processMyPublications(resultsModel: ResultsModel<PublicationModel>) {
        resultsModel.results?.let {
            gridAdapter.updateList(it)
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
            presenter.getProfile(
                token = getTokenFromSharedPref(),
                userId = userId.toString(),
                isOwnProfile = isOwnProfile
            )
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.dialog_bottom_creator_chooser_barcode -> {
                navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            }
            R.id.dialog_bottom_creator_chooser_photo -> {
                navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
            }
            R.id.dialog_bottom_creator_chooser_create_publish -> {
                findNavController().navigate(R.id.action_profileFragment_to_photoPostCreatorFragment)
            }
        }
    }

    private fun fillProfileInfo(userModel: UserModel) {
        include_toolbar_profile.toolbar_title_text_view.text = userModel.username
        text_view_fragment_profile_user_name.text = userModel.firstName
        fragment_profile_followers_count.text = "${/*followers_count*/ 0}"
        fragment_profile_followings_count.text = "${/*followings_count*/ 0}"
        fragment_profile_photos_count.text = "${0}"

        if (!isOwnProfile) {
            fragment_profile_edit_text_view.hide()
            fragment_profile_subscribe_text_view.show()
        }
    }

    private fun loadProfilePhoto(userModel: UserModel) {
        userModel.avatar?.let {
            text_view_fragment_profile_user_short_name.hide()

            imageLoader.load(
                url = it,
                target = shapeable_image_view_fragment_profile_avatar
            )
        } ?: run {
            shapeable_image_view_fragment_profile_avatar.hide()
            text_view_fragment_profile_user_short_name.show()
            text_view_fragment_profile_user_short_name.text = getShortName(userModel.firstName, userModel.lastName)
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }

    private fun onFilterClick(position: Int) {
        when (position) {
            0 -> FilterDialog.getNewInstance(
                token = getTokenFromSharedPref(),
                itemClickListener = this,
                gender = currentGender
            ).show(childFragmentManager, EMPTY_STRING)
            1 -> {
                presenter.getPublications(
                    token = getTokenFromSharedPref(),
                    isOwnProfile = isOwnProfile
                )
                adapterFilter.onChooseItem(position)
            }
            2 -> adapterFilter.onChooseItem(position)
            3 -> adapterFilter.onChooseItem(position)
            4 -> processMyData()
            5 ->  CreatorChooserDialog().apply {
                setChoiceListener(listener = this@ProfileFragment)
            }.show(childFragmentManager, EMPTY_STRING)
        }
    }

    private fun onCollectionClick(item: Any?) {
        item as PublicationModel

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