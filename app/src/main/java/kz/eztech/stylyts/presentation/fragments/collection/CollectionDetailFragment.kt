package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import kz.eztech.stylyts.presentation.dialogs.collection.CollectionContextDialog
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionDetailPresenter
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class CollectionDetailFragment : BaseFragment<MainActivity>(), CollectionDetailContract.View,
    UniversalViewClickListener, View.OnClickListener, DialogChooserListener {

    @Inject lateinit var presenter: CollectionDetailPresenter
    private lateinit var additionalAdapter: MainImagesAdditionalAdapter
    private lateinit var currentOutfitModel: OutfitModel
    private lateinit var currentPostModel: PostModel

    private var currentId: Int = 0
    private var currentMode: Int = OUTFIT_MODE
    private var isOwn: Boolean = false

    companion object {
        const val ID_KEY = "outfit_id"
        const val MODE_KEY = "mode_key"
        const val OUTFIT_MODE = 0
        const val POST_MODE = 1
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_collection_detail) {
            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.collection_detail_fragment_publishes)

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@CollectionDetailFragment)
            toolbar_left_corner_action_image_button.show()

            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(ID_KEY)) {
                currentId = it.getInt(ID_KEY)
            }

            if (it.containsKey(MODE_KEY)) {
                currentMode = it.getInt(MODE_KEY)
            }
        }
    }

    override fun initializeViewsData() {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        recycler_view_fragment_collection_detail_additionals_list.adapter = additionalAdapter
    }

    override fun initializeListeners() {
        text_view_fragment_collection_detail_comments_count.setOnClickListener(this)
        button_fragment_collection_detail_change_collection.setOnClickListener(this)
        constraint_layout_fragment_collection_detail_profile_container.setOnClickListener(this)
        imageButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        when (currentMode) {
            OUTFIT_MODE -> presenter.getOutfitById(
                token = getTokenFromSharedPref(),
                outfitId = currentId.toString()
            )
            POST_MODE -> presenter.getPostById(
                token = getTokenFromSharedPref(),
                postId = currentId
            )
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_main_image_holder_container -> {
                item as ClothesModel

                val bundle = Bundle()
                bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

                findNavController().navigate(
                    R.id.action_collectionDetailFragment_to_itemDetailFragment,
                    bundle
                )
            }
            R.id.imageViewSlidePhoto -> onShowTags()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_view_fragment_collection_detail_comments_count -> {
                findNavController().navigate(R.id.action_collectionDetailFragment_to_userCommentsFragment)
            }
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.button_fragment_collection_detail_change_collection -> onChangeButtonClick()
            R.id.constraint_layout_fragment_collection_detail_profile_container -> onProfileClick()
            R.id.imageButton -> onContextMenuClick()
            R.id.fragment_collection_detail_clothes_tags_icon -> onShowClothesTags()
            R.id.fragment_collection_detail_user_tags_icon -> onShowUsersTags()
        }
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_collection_detail_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_collection_detail_progress_bar.hide()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.dialog_bottom_collection_context_delete_text_view -> onDeleteContextClicked()
            R.id.dialog_bottom_collection_context_change_text_view -> onChangeButtonClick()
        }
    }

    override fun processOutfit(outfitModel: OutfitModel) {
        additionalAdapter.updateList(list = outfitModel.clothes)
        currentOutfitModel = outfitModel
        isOwn = outfitModel.author.id == currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

        processAuthor(userShortModel = outfitModel.author)
        loadImages(images = arrayListOf(outfitModel.coverPhoto))

        text_view_fragment_collection_detail_comments_cost.text = getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(outfitModel.totalPrice)
        )
        text_view_fragment_collection_detail_comments_count.text = "Показать ${0} коммент."
        text_view_fragment_collection_detail_date.text = DateFormatterHelper.formatISO_8601(
            outfitModel.createdAt,
            DateFormatterHelper.FORMAT_DATE_DD_MMMM
        )

        if (!isOwn) {
            button_fragment_collection_detail_change_collection.hide()
        }
    }

    override fun processPost(postModel: PostModel) {
        additionalAdapter.updateList(list = postModel.clothes)
        currentPostModel = postModel
        isOwn = postModel.author.id == currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

        processAuthor(userShortModel = postModel.author)
        loadImages(images = postModel.images)
        loadTags(postModel = postModel)

        if (!isOwn) {
            button_fragment_collection_detail_change_collection.hide()
        }

        if (postModel.clothes.isEmpty()) {
            recycler_view_fragment_collection_detail_additionals_list.hide()
        }
    }

    private fun processAuthor(userShortModel: UserShortModel) {
        text_view_fragment_collection_detail_partner_name.text = getString(
            R.string.full_name_text_format,
            userShortModel.firstName,
            userShortModel.lastName
        )

        if (userShortModel.avatar.isBlank()) {
            shapeable_image_view_fragment_collection_detail_profile_avatar.hide()
            text_view_text_view_fragment_collection_detail_short_name.show()
            text_view_text_view_fragment_collection_detail_short_name.text = getShortName(
                firstName = userShortModel.firstName,
                lastName = userShortModel.lastName
            )
        } else {
            text_view_text_view_fragment_collection_detail_short_name.hide()

            Glide.with(shapeable_image_view_fragment_collection_detail_profile_avatar.context)
                .load(userShortModel.avatar)
                .centerCrop()
                .into(shapeable_image_view_fragment_collection_detail_profile_avatar)
        }
    }

    override fun processSuccessDeleting() {
        findNavController().navigateUp()
    }

    private fun loadImages(images: List<String>) {
        val imageArray = ArrayList<String>()

        images.let {
            it.map { image ->
                imageArray.add(image)
            }
        }

        val imageAdapter = ImagesViewPagerAdapter(
            images = imageArray,
            withAnimation = false
        )
        imageAdapter.mItemImageClickListener = this

        fragment_collection_detail_photos_holder_view_pager.adapter = imageAdapter
        fragment_collection_detail_photos_pager_indicator.show()
        fragment_collection_detail_photos_pager_indicator.attachToPager(
            fragment_collection_detail_photos_holder_view_pager
        )
    }

    private fun loadTags(postModel: PostModel) {
        checkEmptyTags(postModel)
        loadClothesTags(postModel)
        loadUsersTags(postModel)

        fragment_collection_detail_clothes_tags_icon.setOnClickListener(this)
        fragment_collection_detail_user_tags_icon.setOnClickListener(this)
    }

    private fun loadClothesTags(postModel: PostModel) {
        val container = fragment_collection_detail_clothes_tags_container
        container.removeAllViews()

        postModel.tags.clothesTags.map {
            val textView = getTagTextView(container)

            view?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(
                            tagModel = it,
                            textView = textView,
                            container = fragment_collection_detail_photos_holder_view_pager
                        )

                        textView.text = it.title

                        if (textView.parent != null) {
                            container.removeView(textView)
                        } else {
                            container.addView(textView)
                        }

                        view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun loadUsersTags(postModel: PostModel) {
        val container = fragment_collection_detail_users_tags_container
        container.removeAllViews()

        postModel.tags.usersTags.map {
            val textView = getTagTextView(container)
            textView.backgroundTintList = resources.getColorStateList(R.color.app_dark_blue_gray)

            view?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(
                            tagModel = it,
                            textView = textView,
                            container = fragment_collection_detail_photos_holder_view_pager
                        )

                        textView.text = it.title

                        if (textView.parent != null) {
                            container.removeView(textView)
                        } else {
                            container.addView(textView)
                        }

                        view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun getTagTextView(container: ViewGroup): TextView {
        val textView =  LayoutInflater.from(container.context).inflate(
            R.layout.text_view_tag_element,
            container,
            false
        ) as TextView

        textView.setPadding(10, 4, 10, 4)

        return textView
    }

    private fun setTagPosition(
        tagModel: TagModel,
        textView: View,
        container: View
    ) {
        val containerX = container.width / 100
        val containerY = container.height / 100

        textView.x = tagModel.pointX.toFloat() * containerX.toFloat()
        textView.y = tagModel.pointY.toFloat() * containerY.toFloat()
    }

    private fun checkEmptyTags(postModel: PostModel) {
        postModel.tags.let {
            if (it.clothesTags.isEmpty()) {
                fragment_collection_detail_clothes_tags_icon.hide()
            } else {
                fragment_collection_detail_clothes_tags_icon.show()
            }

            if (it.usersTags.isEmpty()) {
                fragment_collection_detail_user_tags_icon.hide()
            } else {
                fragment_collection_detail_user_tags_icon.show()
            }
        }
    }

    private fun onShowClothesTags() {
        if (fragment_collection_detail_clothes_tags_container.visibility == View.GONE) {
            fragment_collection_detail_clothes_tags_container.show()
        } else {
            fragment_collection_detail_clothes_tags_container.hide()
        }
    }

    private fun onShowUsersTags() {
        if (fragment_collection_detail_users_tags_container.visibility == View.GONE) {
            fragment_collection_detail_users_tags_container.show()
        } else {
            fragment_collection_detail_users_tags_container.hide()
        }
    }

    private fun onShowTags() {
        onShowClothesTags()
        onShowUsersTags()
    }

    private fun onProfileClick() {
        val bundle = Bundle()

        when (currentMode) {
            OUTFIT_MODE -> bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, currentOutfitModel.author.id)
            POST_MODE -> bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, currentPostModel.author.id)
        }

        findNavController().navigate(R.id.nav_profile, bundle)
    }

    private fun onChangeButtonClick() {
        val bundle = Bundle()
        val clothes = ArrayList<ClothesModel>()

        when (currentMode) {
            OUTFIT_MODE -> currentOutfitModel.clothes.let {
                clothes.addAll(it)

                bundle.apply {
                    putParcelableArrayList(CollectionConstructorFragment.CLOTHES_ITEMS_KEY, clothes)
                    putInt(CollectionConstructorFragment.MAIN_ID_KEY, currentOutfitModel.id)
                    putBoolean(CollectionConstructorFragment.IS_UPDATING_KEY, true)

                    findNavController().navigate(
                        R.id.action_collectionDetailFragment_to_nav_create_collection,
                        bundle
                    )
                }
            }
            POST_MODE -> currentPostModel.clothes.let {
                val images = ArrayList<String>()

                clothes.addAll(it)
                images.addAll(currentPostModel.images)

                bundle.apply {
                    putInt(CreateCollectionAcceptFragment.MODE_KEY, CreateCollectionAcceptFragment.POST_MODE)
                    putInt(CreateCollectionAcceptFragment.ID_KEY, currentPostModel.id)
                    putBoolean(CreateCollectionAcceptFragment.IS_UPDATING_KEY, true)
                    putBoolean(CreateCollectionAcceptFragment.IS_CHOOSER_KEY, true)
                    putStringArrayList(CreateCollectionAcceptFragment.CHOSEN_PHOTOS_KEY, images)
                    putParcelableArrayList(CreateCollectionAcceptFragment.CLOTHES_KEY, clothes)

                    if (currentPostModel.images.isNotEmpty()) {
                        putParcelable(
                            CreateCollectionAcceptFragment.PHOTO_URI_KEY,
                            FileUtils.getUriFromUrl(currentPostModel.images[0])
                        )
                    }

                    findNavController().navigate(
                        R.id.action_collectionDetailFragment_to_createCollectionAcceptFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun onContextMenuClick() {
        CollectionContextDialog(isOwn = isOwn).apply {
            setChoiceListener(listener = this@CollectionDetailFragment)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun onDeleteContextClicked() {
        when (currentMode) {
            OUTFIT_MODE -> presenter.deleteOutfit(
                token = getTokenFromSharedPref(),
                outfitId = currentId
            )
            POST_MODE -> presenter.deletePost(
                token = getTokenFromSharedPref(),
                postId =  currentId
            )
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}