package kz.eztech.stylyts.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kotlinx.android.synthetic.main.fragment_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.main.MainImageModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.main.MainImagesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.MainLinePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class MainFragment : BaseFragment<MainActivity>(), MainContract.View, View.OnClickListener,
    UniversalViewClickListener {

    @Inject lateinit var presenter: MainLinePresenter

    private lateinit var dummyList: ArrayList<MainImageModel>
    private lateinit var postsAdapter: MainImagesAdapter

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.hide()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_send_message)

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        postsAdapter = MainImagesAdapter()
        postsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recycler_view_fragment_main_images_list.adapter = postsAdapter
    }

    override fun initializeListeners() {
        toolbar_right_corner_action_image_button.setOnClickListener(this)
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.constraint_layout_fragment_item_main_image_profile_container -> onProfileClicked()
            R.id.button_item_main_image_change_collection -> onChangeCollectionClicked(item)
            R.id.frame_layout_item_main_image_holder_container -> onClothesItemClicked(item)
            R.id.image_view_item_main_image_imageholder -> onCollectionImageClicked(item)
            R.id.text_view_item_main_image_comments_count -> findNavController().navigate(R.id.userCommentsFragment)
        }
    }

    override fun processPostInitialization() {
        presenter.getPosts(token = getTokenFromSharedPref())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun getContractView(): BaseView = this

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_mainFragment_to_chatsFragment)
            }
        }
    }

    override fun displayProgress() {
        progress_bar_fragment_main_lenta.show()
    }

    override fun hideProgress() {
        progress_bar_fragment_main_lenta.hide()
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        postsAdapter.updateList(list = resultsModel.results)
    }

    private fun onProfileClicked() {
        val bundle = Bundle()
        bundle.putInt("items", 1)

        findNavController().navigate(
            R.id.action_mainFragment_to_partnerProfileFragment,
            bundle
        )
    }

    private fun onChangeCollectionClicked(item: Any?) {
        item as MainResult

        item.clothes?.let {
            val itemsList = ArrayList<ClothesMainModel>()
            itemsList.addAll(it)
            val bundle = Bundle()
            bundle.putParcelableArrayList("items", itemsList)
            bundle.putInt("mainId", item.id ?: 0)
            findNavController().navigate(
                R.id.action_mainFragment_to_createCollectionFragment,
                bundle
            )
        } ?: run {
            findNavController().navigate(R.id.action_mainFragment_to_createCollectionFragment)
        }
    }

    private fun onClothesItemClicked(item: Any?) {
        item as ClothesMainModel

        val bundle = Bundle()
        bundle.putParcelable("clotheModel", item)
        findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment, bundle)
    }

    private fun onCollectionImageClicked(item: Any?) {
        item as PublicationModel

        val bundle = Bundle()
        bundle.putParcelable("model", item)

        findNavController().navigate(R.id.action_mainFragment_to_collectionDetailFragment, bundle)
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}