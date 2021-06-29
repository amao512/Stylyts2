package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.ClothesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.common.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import kz.eztech.stylyts.presentation.dialogs.collection.CollectionContextDialog
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionDetailPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.*
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.text.NumberFormat
import javax.inject.Inject

class CollectionDetailFragment : BaseFragment<MainActivity>(), CollectionDetailContract.View,
    UniversalViewClickListener, View.OnClickListener, DialogChooserListener {

    @Inject lateinit var presenter: CollectionDetailPresenter

    private lateinit var additionalAdapter: ClothesAdditionalAdapter
    private lateinit var currentOutfitModel: OutfitModel
    private lateinit var currentPostModel: PostModel

    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var userFullNameTextView: TextView
    private lateinit var dialogMenuImageButton: ImageButton
    private lateinit var imagesViewPager: ViewPager
    private lateinit var imageScrollingPagerIndicator: ScrollingPagerIndicator
    private lateinit var clothesTagsContainerFrameLayout: FrameLayout
    private lateinit var userTagsContainerFrameLayout: FrameLayout
    private lateinit var clothesTagIconFrameLayout: FrameLayout
    private lateinit var userTagIconFrameLayout: FrameLayout
    private lateinit var clothesRecyclerView: RecyclerView
    private lateinit var likeImageView: ImageView
    private lateinit var commentsImageView: ImageView
    private lateinit var changeCollectionButton: Button
    private lateinit var likesCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var firstCommentTextView: TextView
    private lateinit var commentsCountTextView: TextView

    companion object {
        const val ID_KEY = "collection_id"
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
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.collection_detail_fragment_publishes))

            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
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
        avatarShapeableImageView = shapeable_image_view_fragment_collection_detail_profile_avatar
        userShortNameTextView = text_view_text_view_fragment_collection_detail_short_name
        userFullNameTextView = text_view_fragment_collection_detail_partner_name
        dialogMenuImageButton = item_main_image_dialog_menu_image_button
        imagesViewPager = fragment_collection_detail_photos_holder_view_pager
        imageScrollingPagerIndicator = fragment_collection_detail_photos_pager_indicator
        clothesTagsContainerFrameLayout = fragment_collection_detail_clothes_tags_container
        userTagsContainerFrameLayout = fragment_collection_detail_users_tags_container
        clothesTagIconFrameLayout = fragment_collection_detail_clothes_tags_icon
        userTagIconFrameLayout = fragment_collection_detail_user_tags_icon
        likeImageView = fragment_collection_detail_like_image_view
        commentsImageView = fragment_collection_detail_comments_image_view
//        changeCollectionButton = button_fragment_collection_detail_change_collection
        likesCountTextView = fragment_collection_detail_likes_count_text_view
        totalPriceTextView = text_view_fragment_collection_detail_comments_cost
        firstCommentTextView = fragment_collection_detail_first_comment_text_view
        commentsCountTextView = text_view_fragment_collection_detail_comments_count

        additionalAdapter = ClothesAdditionalAdapter()
        additionalAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        clothesRecyclerView = recycler_view_fragment_collection_detail_additionals_list
        clothesRecyclerView.adapter = additionalAdapter
    }

    override fun initializeListeners() {
        commentsCountTextView.setOnClickListener(this)
//        changeCollectionButton.setOnClickListener(this)
        likeImageView.setOnClickListener(this)
        commentsImageView.setOnClickListener(this)
        firstCommentTextView.setOnClickListener(this)
        constraint_layout_fragment_collection_detail_profile_container.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        displayProgress()

        when (getModeFromArgs()) {
            OUTFIT_MODE -> presenter.getOutfitById(
                token = currentActivity.getTokenFromSharedPref(),
                outfitId = getCollectionIdFromArgs()
            )
            POST_MODE -> presenter.getPostById(
                token = currentActivity.getTokenFromSharedPref(),
                postId = getCollectionIdFromArgs()
            )
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.frame_layout_item_main_image_holder_container -> navigateToClothes(item)
            R.id.imageViewSlidePhoto -> onShowTags()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_view_fragment_collection_detail_comments_count -> navigateToComments()
            R.id.fragment_collection_detail_comments_image_view -> navigateToComments()
//            R.id.button_fragment_collection_detail_change_collection -> onChangeButtonClick()
            R.id.constraint_layout_fragment_collection_detail_profile_container -> onProfileClick()
            R.id.fragment_collection_detail_clothes_tags_icon -> onShowClothesTags()
            R.id.fragment_collection_detail_user_tags_icon -> onShowUsersTags()
            R.id.fragment_collection_detail_like_image_view -> onLikeClicked()
            R.id.fragment_collection_detail_first_comment_text_view -> onProfileClick()
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

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
        commentsCountTextView.text = getString(
            R.string.comments_count_text_format,
            0.toString()
        )
        firstCommentTextView.text = outfitModel.text
        firstCommentTextView.hide()
        likesCountTextView.hide()
        text_view_fragment_collection_detail_date.text = getFormattedDate(outfitModel.createdAt)

        if (outfitModel.clothes.isNotEmpty() && outfitModel.clothes.sumBy { it.cost } != 0) {
            totalPriceTextView.text = HtmlCompat.fromHtml(
                totalPriceTextView.context.getString(
                    R.string.total_cost_text_format,
                    NumberFormat.getInstance().format(outfitModel.clothes.sumBy { it.cost })
                        .toString(),
                    outfitModel.clothes[0].currency,
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            totalPriceTextView.hide()
        }

        processAuthor(userShortModel = outfitModel.author)
        loadImages(images = arrayListOf(outfitModel.coverPhoto))
        processClothes(list = outfitModel.clothes)
        processOwnViews(authorId = outfitModel.author.id)
    }

    override fun processPost(postModel: PostModel) {
        additionalAdapter.updateList(list = postModel.clothes)
        currentPostModel = postModel

        if (postModel.clothes.isNotEmpty() && postModel.clothes.sumBy { it.cost } != 0) {
            totalPriceTextView.text = HtmlCompat.fromHtml(
                totalPriceTextView.context.getString(
                    R.string.total_cost_text_format,
                    NumberFormat.getInstance().format(postModel.clothes.sumBy { it.cost })
                        .toString(),
                    postModel.clothes[0].currency,
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            totalPriceTextView.hide()
        }

        if (postModel.likesCount > 0) {
            likesCountTextView.show()
            likesCountTextView.text = getString(
                R.string.likes_count_text_format,
                postModel.likesCount.toString()
            )
        } else {
            likesCountTextView.hide()
        }

        if (postModel.firstComment.text.isNotBlank()) {
            firstCommentTextView.text = HtmlCompat.fromHtml(
                firstCommentTextView.context.getString(
                    R.string.comment_text_with_user_text_format,
                    postModel.firstComment.author.username,
                    EMPTY_STRING,
                    postModel.firstComment.text
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            firstCommentTextView.show()
        } else {
            firstCommentTextView.hide()
        }

        commentsCountTextView.text = getString(
            R.string.comments_count_text_format,
            postModel.commentsCount.toString()
        )

        processAuthor(userShortModel = postModel.author)
        loadImages(images = postModel.images)
        loadTags(postModel = postModel)
        processLike(isLiked = postModel.alreadyLiked)
        processClothes(list = postModel.clothes)
        processOwnViews(authorId = postModel.author.id)
    }

    private fun processOwnViews(authorId: Int) {
        val isOwn = authorId == currentActivity.getUserIdFromSharedPref()

//        if (!isOwn) {
//            changeCollectionButton.hide()
//        }

        dialogMenuImageButton.setOnClickListener {
            onContextMenuClick(isOwn)
        }
    }


    private fun processClothes(list: List<ClothesModel>) {
        if (list.isEmpty()) {
            recycler_view_fragment_collection_detail_additionals_list.hide()
        }
    }

    private fun processAuthor(userShortModel: UserShortModel) {
        userFullNameTextView.text = getString(
            R.string.full_name_text_format,
            userShortModel.firstName,
            userShortModel.lastName
        )

        if (userShortModel.avatar.isBlank()) {
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = getShortName(
                firstName = userShortModel.firstName,
                lastName = userShortModel.lastName
            )
        } else {
            userShortNameTextView.hide()
            userShortModel.avatar.loadImageWithCenterCrop(target = avatarShapeableImageView)
        }
    }

    override fun processSuccessDeleting() {
        findNavController().navigateUp()
    }

    override fun processLike(isLiked: Boolean) {
        likeImageView.setImageResource(
            when (isLiked) {
                true -> R.drawable.ic_heart_red
                false -> R.drawable.ic_baseline_favorite_border_24
            }
        )
    }

    override fun navigateToUserProfile(userModel: UserModel) {
        val bundle = Bundle()

        if (userModel.isBrand) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, userModel.id)

            findNavController().navigate(R.id.nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, userModel.id)

            findNavController().navigate(R.id.nav_profile, bundle)
        }
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
            withAnimation = false,
            withCenterCrop = when (getModeFromArgs()) {
                POST_MODE -> true
                else -> false
            }
        )
        imageAdapter.mItemImageClickListener = this

        imagesViewPager.adapter = imageAdapter
        imageScrollingPagerIndicator.show()
        imageScrollingPagerIndicator.attachToPager(
            imagesViewPager
        )

        if (imageArray.size == 1) {
            imageScrollingPagerIndicator.hide()
        }
    }

    private fun loadTags(postModel: PostModel) {
        checkEmptyTags(postModel)
        loadClothesTags(postModel)
        loadUsersTags(postModel)

        clothesTagIconFrameLayout.setOnClickListener(this)
        userTagIconFrameLayout.setOnClickListener(this)
    }

    private fun loadClothesTags(postModel: PostModel) {
        clothesTagsContainerFrameLayout.removeAllViews()

        postModel.tags.clothesTags.map { tag ->
            val textView = getTagTextView(clothesTagsContainerFrameLayout)

            view?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(
                            tagModel = tag,
                            textView = textView,
                            container = imagesViewPager
                        )

                        textView.text = tag.title

                        if (textView.parent != null) {
                            clothesTagsContainerFrameLayout.removeView(textView)
                        } else {
                            clothesTagsContainerFrameLayout.addView(textView)
                        }

                        textView.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(ClothesDetailFragment.CLOTHES_ID, tag.id)

                            findNavController().navigate(
                                R.id.action_collectionDetailFragment_to_clothesDetailFragment,
                                bundle
                            )
                        }

                        view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun loadUsersTags(postModel: PostModel) {
        userTagsContainerFrameLayout.removeAllViews()

        postModel.tags.usersTags.map { tag ->
            val textView = getTagTextView(userTagsContainerFrameLayout)
            textView.backgroundTintList = resources.getColorStateList(R.color.app_dark_blue_gray)

            view?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(
                            tagModel = tag,
                            textView = textView,
                            container = imagesViewPager
                        )

                        textView.text = tag.title

                        if (textView.parent != null) {
                            userTagsContainerFrameLayout.removeView(textView)
                        } else {
                            userTagsContainerFrameLayout.addView(textView)
                        }

                        textView.setOnClickListener {
                            presenter.getUserForNavigate(
                                token = currentActivity.getTokenFromSharedPref(),
                                userId = tag.id
                            )
                        }

                        view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun getTagTextView(container: ViewGroup): TextView {
        return LayoutInflater.from(container.context).inflate(
            R.layout.item_tag,
            container,
            false
        ) as TextView
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
                clothesTagIconFrameLayout.hide()
            } else {
                clothesTagIconFrameLayout.show()
            }

            if (it.usersTags.isEmpty()) {
                userTagIconFrameLayout.hide()
            } else {
                userTagIconFrameLayout.show()
            }
        }
    }

    private fun onShowClothesTags() {
        if (clothesTagsContainerFrameLayout.visibility == View.GONE) {
            clothesTagsContainerFrameLayout.show()
        } else {
            clothesTagsContainerFrameLayout.hide()
        }
    }

    private fun onShowUsersTags() {
        if (userTagsContainerFrameLayout.visibility == View.GONE) {
            userTagsContainerFrameLayout.show()
        } else {
            userTagsContainerFrameLayout.hide()
        }
    }

    private fun onLikeClicked() {
        presenter.onLikeClick(
            token = currentActivity.getTokenFromSharedPref(),
            postId = getCollectionIdFromArgs()
        )
        when (getModeFromArgs()) {
            POST_MODE -> presenter.getPostById(
                token = currentActivity.getTokenFromSharedPref(),
                postId = getCollectionIdFromArgs()
            )
        }
    }

    private fun navigateToClothes(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(
            R.id.action_collectionDetailFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun onShowTags() {
        onShowClothesTags()
        onShowUsersTags()
    }

    private fun onProfileClick() {
        when (getModeFromArgs()) {
            OUTFIT_MODE -> navigateToProfile(currentOutfitModel.author)
            POST_MODE -> navigateToProfile(currentPostModel.author)
        }
    }

    private fun navigateToProfile(author: UserShortModel) {
        val bundle = Bundle()

        if (author.isBrand && author.id != currentActivity.getUserIdFromSharedPref()) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, author.id)

            findNavController().navigate(R.id.nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, author.id)

            findNavController().navigate(R.id.nav_profile, bundle)
        }
    }

    private fun navigateToComments() {
        val bundle = Bundle()
        bundle.putInt(CommentsFragment.COLLECTION_ID_KEY, getCollectionIdFromArgs())
        bundle.putInt(CommentsFragment.MODE_KEY, getModeFromArgs())

        findNavController().navigate(
            R.id.action_collectionDetailFragment_to_userCommentsFragment,
            bundle
        )
    }

    private fun onChangeButtonClick() {
        when (getModeFromArgs()) {
            OUTFIT_MODE -> changeOutfit()
            POST_MODE -> changePost()
        }
    }

    private fun changeOutfit() {
        val bundle = Bundle()
        val clothes = ArrayList<ClothesModel>()

        clothes.addAll(currentOutfitModel.clothes)

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

    private fun changePost() {
        val bundle = Bundle()
        val clothes = ArrayList<ClothesModel>()
        val images = ArrayList<String>()

        clothes.addAll(currentPostModel.clothes)
        images.addAll(currentPostModel.images)

        bundle.apply {
            putInt(
                CreateCollectionAcceptFragment.MODE_KEY,
                CreateCollectionAcceptFragment.POST_MODE
            )
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

    private fun onContextMenuClick(isOwn: Boolean) {
        CollectionContextDialog(
            isOwn = isOwn
        ).apply {
            setChoiceListener(listener = this@CollectionDetailFragment)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun onDeleteContextClicked() {
        when (getModeFromArgs()) {
            OUTFIT_MODE -> presenter.deleteOutfit(
                token = currentActivity.getTokenFromSharedPref(),
                outfitId = getCollectionIdFromArgs()
            )
            POST_MODE -> presenter.deletePost(
                token = currentActivity.getTokenFromSharedPref(),
                postId = getCollectionIdFromArgs()
            )
        }
    }

    private fun getCollectionIdFromArgs(): Int = arguments?.getInt(ID_KEY) ?: 0

    private fun getModeFromArgs(): Int = arguments?.getInt(MODE_KEY) ?: OUTFIT_MODE
}