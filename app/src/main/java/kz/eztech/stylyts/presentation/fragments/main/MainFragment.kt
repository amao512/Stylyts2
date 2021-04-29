package kz.eztech.stylyts.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kotlinx.android.synthetic.main.fragment_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.main.MainImagesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.dialogs.collection.CollectionContextDialog
import kz.eztech.stylyts.presentation.fragments.collection.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.MainLinePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class MainFragment : BaseFragment<MainActivity>(), MainContract.View, View.OnClickListener,
    UniversalViewClickListener, DialogChooserListener {

    @Inject lateinit var presenter: MainLinePresenter
    private lateinit var postsAdapter: MainImagesAdapter

    private var currentPage: Int = 1
    private var lastPage: Boolean = false

    private lateinit var recyclerView: RecyclerView

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
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        postsAdapter = MainImagesAdapter(
            ownId = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY) ?: 0
        )
        postsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_main_images_list
        recyclerView.adapter = postsAdapter
    }

    override fun initializeListeners() {
        toolbar_right_corner_action_image_button.setOnClickListener(this)
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.constraint_layout_fragment_item_main_image_profile_container -> onProfileClicked(item)
            R.id.button_item_main_image_change_collection -> onChangeCollectionClicked(item)
            R.id.frame_layout_item_main_image_holder_container -> onClothesItemClicked(item)
            R.id.item_main_image_image_card_view -> onCollectionImageClicked(item)
            R.id.text_view_item_main_image_comments_count -> findNavController().navigate(R.id.action_mainFragment_to_userCommentsFragment)
            R.id.imageButton -> onContextMenuClicked(item)
        }
    }

    override fun processPostInitialization() {
        getPosts()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!lastPage) {
                        getPosts()
                    }
                }
            }
        })
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
        fragment_main_more_small_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_main_more_small_progress_bar.hide()
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        postsAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != currentPage) {
            currentPage++
        } else {
            lastPage = true
        }
    }

    override fun processSuccessDeleting() {
        processPostInitialization()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.dialog_bottom_collection_context_delete_text_view -> onPostDeleteContextClicked(item)
            R.id.dialog_bottom_collection_context_change_text_view -> onChangeCollectionClicked(item)
        }
    }

    private fun getPosts() {
        presenter.getPosts(
            token = getTokenFromSharedPref(),
            page = currentPage
        )
    }

    private fun onProfileClicked(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.author.id)

        findNavController().navigate(R.id.action_mainFragment_to_nav_profile, bundle)
    }

    private fun onChangeCollectionClicked(item: Any?) {
        item as PostModel

        item.clothes.let {
            val bundle = Bundle()
            val itemsList = ArrayList<ClothesModel>()
            val images = ArrayList<String>()

            itemsList.addAll(it)
            images.addAll(item.images)
            images.removeFirst()

            bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, CreateCollectionAcceptFragment.POST_MODE)
            bundle.putStringArrayList(CreateCollectionAcceptFragment.CHOSEN_PHOTOS_KEY, images)
            bundle.putParcelableArrayList(CreateCollectionAcceptFragment.CLOTHES_KEY, itemsList)

            if (item.images.isNotEmpty()) {
                bundle.putString(
                    CreateCollectionAcceptFragment.PHOTO_STRING_KEY,
                    item.images[0]
                )
            }

            bundle.putParcelableArrayList(CollectionConstructorFragment.CLOTHES_ITEMS_KEY, itemsList)
            bundle.putInt(CollectionConstructorFragment.MAIN_ID_KEY, item.id)

            findNavController().navigate(
                R.id.action_mainFragment_to_createCollectionAcceptFragment,
                bundle
            )
        }
    }

    private fun onClothesItemClicked(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(R.id.action_mainFragment_to_clothesDetailFragment, bundle)
    }

    private fun onCollectionImageClicked(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)

        findNavController().navigate(R.id.action_mainFragment_to_collectionDetailFragment, bundle)
    }

    private fun onContextMenuClicked(item: Any?) {
        item as PostModel

        val isOwn = item.author.id == currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

        CollectionContextDialog(isOwn, item).apply {
            setChoiceListener(listener = this@MainFragment)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun onPostDeleteContextClicked(item: Any?) {
        item as PostModel

        presenter.deletePost(
            token = getTokenFromSharedPref(),
            postId = item.id
        )
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}