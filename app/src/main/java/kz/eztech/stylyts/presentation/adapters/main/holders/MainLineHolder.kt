package kz.eztech.stylyts.presentation.adapters.main.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.item_main_line.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainLineHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val ownId: Int,
    private val imageLoader: DomainImageLoader
) : BaseViewHolder(itemView, adapter), UniversalViewClickListener {

    private lateinit var additionalAdapter: MainImagesAdditionalAdapter

    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var dialogMenuImageButton: ImageButton
    private lateinit var imagesViewPager: ViewPager
    private lateinit var imagesScrollingPagerIndicator: ScrollingPagerIndicator
    private lateinit var clothesTagsContainerFrameLayout: FrameLayout
    private lateinit var usersTagContainerFrameLayout: FrameLayout
    private lateinit var clothesRecyclerView: RecyclerView
    private lateinit var likeImageButton: ImageView
    private lateinit var likesCountTextView: TextView
    private lateinit var totalCostTextView: TextView
    private lateinit var firstCommentTextView: TextView
    private lateinit var commentsImageButton: ImageView
    private lateinit var commentsCountTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as PostModel

        initializeViews()
        initializeAdapters(postModel = item)
        processPost(postModel = item)
        processImages(postModel = item)
        processUserPhoto(postModel = item)
        processTags(postModel = item)
        processLike(isLiked = item.alreadyLiked)
        processLikesCount(likesCount = item.likesCount)
        initializeListeners(postModel = item, position = position)
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.imageViewSlidePhoto -> {
                onShowClothesTags()
                onShowUsersTags()
            }
        }
    }

    private fun initializeAdapters(postModel: PostModel) {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = adapter.itemClickListener

        if (postModel.clothes.isEmpty()) {
            clothesRecyclerView.hide()
        }

        clothesRecyclerView.adapter = additionalAdapter
        additionalAdapter.updateList(list = postModel.clothes)
    }

    private fun initializeViews() {
        with(itemView) {
            avatarShapeableImageView = shapeable_image_view_item_main_image_profile_avatar
            userShortNameTextView = text_view_item_main_image_short_name
            fullNameTextView = text_view_item_main_image_partner_name
            dialogMenuImageButton = item_main_image_dialog_menu_image_button
            imagesViewPager = item_main_image_photos_holder_view_pager
            imagesScrollingPagerIndicator = item_main_image_photos_pager_indicator
            clothesTagsContainerFrameLayout = item_main_image_clothes_tags_container
            usersTagContainerFrameLayout = item_main_image_users_tags_container
            clothesRecyclerView = recycler_view_item_main_image_additionals_list
            likeImageButton = item_main_image_like_image_button
            likesCountTextView = item_main_line_likes_count_text_view
            totalCostTextView = text_view_item_main_image_comments_cost
            firstCommentTextView = item_main_line_first_comment_text_view
            commentsImageButton = item_main_image_comments_image_button
            commentsCountTextView = text_view_item_main_image_comments_count
        }
    }

    private fun processPost(postModel: PostModel) {
        fullNameTextView.text =
            SPACE_TEXT_FORMAT.format(postModel.author.firstName, postModel.author.lastName)
    }

    private fun initializeListeners(
        postModel: PostModel,
        position: Int
    ) {
        with(itemView) {
//            if (ownId != postModel.author.id) {
//                button_item_main_image_change_collection.hide()
//            }

            item_main_image_image_card_view.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }

            constraint_layout_fragment_item_main_image_profile_container.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }
//            button_item_main_image_change_collection.setOnClickListener { thisView ->
//                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
//            }

            if (postModel.clothes.isNotEmpty()) {
                totalCostTextView.text = HtmlCompat.fromHtml(
                    totalCostTextView.context.getString(
                        R.string.total_cost_text_format,
                        NumberFormat.getInstance().format(postModel.clothes.sumBy { it.cost })
                            .toString(),
                        postModel.clothes[0].currency,
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                totalCostTextView.hide()
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

            commentsCountTextView.text = commentsCountTextView.context
                .getString(R.string.comments_count_text_format, postModel.commentsCount.toString())

            commentsCountTextView.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }

            commentsImageButton.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, postModel)
            }

            dialogMenuImageButton.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, postModel)
            }

            likeImageButton.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, postModel)
            }

            firstCommentTextView.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, postModel)
            }

            item_main_image_clothes_tags_icon.setOnClickListener {
                onShowClothesTags()
            }
            item_main_image_user_tags_icon.setOnClickListener {
                onShowUsersTags()
            }

//            text_view_item_main_image_date.text =
//                "${DateFormatterHelper.formatISO_8601(postModel.created, FORMAT_DATE_DD_MMMM)}"
        }
    }

    private fun processLikesCount(likesCount: Int) {
        if (likesCount > 0) {
            likesCountTextView.text = likesCountTextView.context.getString(
                R.string.likes_count_text_format,
                likesCount.toString()
            )
            likesCountTextView.show()
        } else {
            likesCountTextView.hide()
        }
    }

    private fun processImages(postModel: PostModel) {
        val imageArray = ArrayList<String>()

        postModel.images.let {
            it.map { image ->
                imageArray.add(image)
            }
        }

        val imageAdapter = ImagesViewPagerAdapter(
            images = imageArray,
            withAnimation = false
        )
        imageAdapter.mItemImageClickListener = this

        imagesViewPager.adapter = imageAdapter
        imagesScrollingPagerIndicator.show()
        imagesScrollingPagerIndicator.attachToPager(imagesViewPager)
    }

    private fun processUserPhoto(postModel: PostModel) {
        val author = postModel.author

        if (author.avatar.isBlank()) {
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = getShortName(author.firstName, author.lastName)
        } else {
            userShortNameTextView.hide()
            author.avatar.loadImageWithCenterCrop(target = avatarShapeableImageView)
        }
    }

    private fun processTags(postModel: PostModel) {
        checkEmptyTags(postModel)
        loadClothesTags(postModel)
        loadUsersTags(postModel)
    }

    private fun loadClothesTags(postModel: PostModel) {
        clothesTagsContainerFrameLayout.removeAllViews()

        postModel.tags.clothesTags.map {
            val textView = getTagTextView(clothesTagsContainerFrameLayout)

            itemView.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(tagModel = it, textView, container = imagesViewPager)

                        textView.text = it.title

                        if (textView.parent != null) {
                            clothesTagsContainerFrameLayout.removeView(textView)
                        } else {
                            clothesTagsContainerFrameLayout.addView(textView)
                        }

                        textView.setOnClickListener { view ->
                            adapter.itemClickListener?.onViewClicked(view, position = 1, item = it)
                        }

                        itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun loadUsersTags(postModel: PostModel) {
        usersTagContainerFrameLayout.removeAllViews()

        postModel.tags.usersTags.map {
            val textView = getTagTextView(usersTagContainerFrameLayout)
            textView.backgroundTintList =
                itemView.resources.getColorStateList(R.color.app_dark_blue_gray)

            itemView.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setTagPosition(tagModel = it, textView, container = imagesViewPager)

                        textView.text = it.title

                        if (textView.parent != null) {
                            usersTagContainerFrameLayout.removeView(textView)
                        } else {
                            usersTagContainerFrameLayout.addView(textView)
                        }

                        textView.setOnClickListener { view ->
                            adapter.itemClickListener?.onViewClicked(view, position = 2, item = it)
                        }

                        itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
        }
    }

    private fun getTagTextView(container: ViewGroup): TextView {
        val textView = LayoutInflater.from(container.context).inflate(
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
        with(itemView) {
            clothesTagsContainerFrameLayout.hide()

            postModel.tags.let {
                if (it.clothesTags.isEmpty()) {
                    item_main_image_clothes_tags_icon.hide()
                } else {
                    item_main_image_clothes_tags_icon.show()
                }

                if (it.usersTags.isEmpty()) {
                    item_main_image_user_tags_icon.hide()
                } else {
                    item_main_image_user_tags_icon.show()
                }
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
        if (usersTagContainerFrameLayout.visibility == View.GONE) {
            usersTagContainerFrameLayout.show()
        } else {
            usersTagContainerFrameLayout.hide()
        }
    }

    private fun processLike(isLiked: Boolean) {
        likeImageButton.setImageResource(
            when (isLiked) {
                true -> R.drawable.ic_heart_red
                false -> R.drawable.ic_baseline_favorite_border_24
            }
        )
    }

    companion object {
        private const val SPACE_TEXT_FORMAT = "%s %s"
    }
}