package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
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
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import kz.eztech.stylyts.presentation.dialogs.EditProfileDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.profile.ProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener {

    @Inject lateinit var presenter: ProfilePresenter

    private lateinit var adapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter

    private var userId: Int = 0
    private var isSettings = false
    private var currentName = EMPTY_STRING
    private var currentNickname = EMPTY_STRING
    private var currentSurname = EMPTY_STRING

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            text_view_toolbar_back.hide()
            text_view_toolbar_title.show()
            image_button_right_corner_action.show()
            image_button_left_corner_action.setImageResource(R.drawable.ic_person_add)
            image_button_left_corner_action.show()

            image_button_right_corner_action.setImageResource(R.drawable.ic_drawer)

            elevation = 0f

            customizeActionToolBar(
                toolbar = this,
                title = currentActivity.getSharedPrefByKey(SharedConstants.USERNAME_KEY)
            )
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(this)

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        val filterList = ArrayList<CollectionFilterModel>()
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_filter)))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_photo_outfits), isChosen = true))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_publishes)))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_wardrobe)))
        filterList.add(CollectionFilterModel(name = getString(R.string.filter_list_my_data)))

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

    override fun initializeListeners() {
        include_toolbar_profile.image_button_right_corner_action.setOnClickListener(this)
        frame_layout_fragment_profile_my_incomes.setOnClickListener(this)
        frame_layout_fragment_profile_my_addresses.setOnClickListener(this)
        text_view_fragment_profile_settings.setOnClickListener(this)
        frame_layout_fragment_profile_cards.setOnClickListener(this)
        linear_layout_fragment_profile_followers_item.setOnClickListener(this)
        linear_layout_fragment_profile_following_item.setOnClickListener(this)
        linear_layout_fragment_profile_photos_item.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getProfile(
            token = currentActivity.getSharedPrefByKey<String>(TOKEN_KEY) ?: EMPTY_STRING
        )
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() = progress_bar_fragment_profile.show()

    override fun hideProgress() = progress_bar_fragment_profile.hide()

    override fun showSettings() {
        linear_layout_fragment_profile_followers_item.hide()
        linear_layout_fragment_profile_change_buttons.hide()
        linear_layout_fragment_profile_photos_item.hide()
        linear_layout_fragment_profile_following_item.hide()
        recycler_view_fragment_profile_filter_list.hide()
        recycler_view_fragment_profile_items_list.hide()

        frame_layout_fragment_profile_settings_container.show()
    }

    override fun hideSettings() {
        frame_layout_fragment_profile_settings_container.hide()

        linear_layout_fragment_profile_followers_item.show()
        linear_layout_fragment_profile_change_buttons.show()
        linear_layout_fragment_profile_photos_item.show()
        linear_layout_fragment_profile_following_item.show()
        recycler_view_fragment_profile_filter_list.show()
        recycler_view_fragment_profile_items_list.show()
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

    override fun processProfile(profileModel: ProfileModel) {
        profileModel.run {
            userId = id
            currentName = name ?: EMPTY_STRING
            currentNickname = EMPTY_STRING
            currentSurname = lastName ?: EMPTY_STRING

            text_view_fragment_profile_user_name.text = name
            fragment_profile_followers_count.text = "${/*followers_count*/ 0}"
            fragment_profile_followings_count.text = "${/*followings_count*/ 0}"
            fragment_profile_photos_count.text = "${0}"

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
            R.id.image_button_right_corner_action -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
            R.id.frame_layout_fragment_profile_my_incomes -> {
                findNavController().navigate(R.id.action_profileFragment_to_profileIncomeFragment)
            }
            R.id.frame_layout_fragment_profile_my_addresses -> {
                findNavController().navigate(R.id.action_profileFragment_to_addressProfileFragment)
            }
            R.id.text_view_fragment_profile_settings -> {
                EditProfileDialog.getNewInstance(
                    token = currentActivity.getSharedPrefByKey(TOKEN_KEY),
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
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_collection_filter -> {
                when (position) {
                    0 -> {}
                    1 -> {
                        val map = HashMap<String, Int>()
                        map["autor"] = userId
                        presenter.getMyCollections(
                            token = currentActivity.getSharedPrefByKey<String>(TOKEN_KEY) ?: EMPTY_STRING,
                            map = map
                        )
                    }
                    2 -> {}
                    3 -> {}
                    4 -> processSettings()
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

    override fun completeEditing(isSuccess: Boolean) {
        if (isSuccess) {
            presenter.getProfile(
                token = currentActivity.getSharedPrefByKey(TOKEN_KEY) ?: EMPTY_STRING
            )
        }
    }
}