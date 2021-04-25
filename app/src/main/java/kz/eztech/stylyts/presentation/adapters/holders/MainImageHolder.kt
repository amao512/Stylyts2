package kz.eztech.stylyts.presentation.adapters.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter), UniversalViewClickListener, View.OnClickListener {

    private lateinit var additionalAdapter: MainImagesAdditionalAdapter

    override fun bindData(item: Any, position: Int) {
        item as PostModel

        initializeAdapters(postModel = item)
        loadImages(postModel = item)
        processCollectionInfo(
            postModel = item,
            position = position
        )
        loadUserPhoto(postModel = item)
        loadTags(postModel = item)
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.imageViewSlidePhoto -> {
                onShowClothesTags()
                onShowUsersTags()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.item_main_image_clothes_tags_icon -> onShowClothesTags()
            R.id.item_main_image_user_tags_icon -> onShowUsersTags()
        }
    }

    private fun initializeAdapters(postModel: PostModel) {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = adapter.itemClickListener

        with (itemView) {
            recycler_view_item_main_image_additionals_list.adapter = additionalAdapter
            additionalAdapter.updateList(list = postModel.clothes)
        }
    }

    private fun processCollectionInfo(
        postModel: PostModel,
        position: Int
    ) {
        with (itemView) {
            item_main_image_image_card_view.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }

            constraint_layout_fragment_item_main_image_profile_container.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }
            button_item_main_image_change_collection.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }

            postModel.clothes.let { clothes ->
                text_view_item_main_image_comments_cost.text = SPACE_TEXT_FORMAT.format(
                    NumberFormat.getInstance().format(clothes.sumBy { it.cost }), "KZT"
                )
            }

            text_view_item_main_image_comments_count.text = "Показать 99 коммент."

            text_view_item_main_image_comments_count.setOnClickListener { thisView ->
                adapter.itemClickListener?.onViewClicked(thisView, position, postModel)
            }

            imageButton.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, postModel)
            }

//            text_view_item_main_image_date.text =
//                "${DateFormatterHelper.formatISO_8601(postModel.created, FORMAT_DATE_DD_MMMM)}"
        }
    }

    private fun loadImages(postModel: PostModel) {
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

        with (itemView) {
            item_main_image_photos_holder_view_pager.adapter = imageAdapter

            item_main_image_photos_pager_indicator.show()
            item_main_image_photos_pager_indicator.attachToPager(
                item_main_image_photos_holder_view_pager
            )
        }
    }

    private fun loadUserPhoto(postModel: PostModel) {
        with (itemView) {
            postModel.owner?.let {
                text_view_item_main_image_partner_name.text = SPACE_TEXT_FORMAT.format(
                    it.firstName,
                    it.lastName
                )

                if (it.avatar.isBlank()) {
                    shapeable_image_view_item_main_image_profile_avatar.hide()
                    text_view_item_main_image_short_name.show()
                    text_view_item_main_image_short_name.text = getShortName(it.firstName, it.lastName)
                } else {
                    text_view_item_main_image_short_name.hide()

                    Glide.with(shapeable_image_view_item_main_image_profile_avatar.context)
                        .load(it.avatar)
                        .centerCrop()
                        .into(shapeable_image_view_item_main_image_profile_avatar)
                }
            }
        }
    }

    private fun loadTags(postModel: PostModel) {
        checkEmptyTags(postModel)
        loadClothesTags(postModel)
        loadUsersTags(postModel)

        with (itemView) {
            item_main_image_clothes_tags_icon.setOnClickListener(this@MainImageHolder)
            item_main_image_user_tags_icon.setOnClickListener(this@MainImageHolder)
        }
    }

    private fun loadClothesTags(postModel: PostModel) {
        with (itemView) {
            val container = item_main_image_clothes_tags_container
            container.removeAllViews()

            postModel.tags.clothesTags.map {
                val textView = getTagTextView(container)

                itemView.viewTreeObserver
                    .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            setTagPosition(
                                tagModel = it,
                                textView = textView,
                                container = item_main_image_photos_holder_view_pager
                            )

                            textView.text = it.title

                            if (textView.parent != null) {
                                container.removeView(textView)
                            } else {
                                container.addView(textView)
                            }

                            itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
            }
        }
    }

    private fun loadUsersTags(postModel: PostModel) {
        with (itemView) {
            val container = item_main_image_users_tags_container
            container.removeAllViews()

            postModel.tags.usersTags.map {
                val textView = getTagTextView(container)
                textView.backgroundTintList = resources.getColorStateList(R.color.app_dark_blue_gray)

                itemView.viewTreeObserver
                    .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            setTagPosition(
                                tagModel = it,
                                textView = textView,
                                container = item_main_image_photos_holder_view_pager
                            )

                            textView.text = it.title

                            if (textView.parent != null) {
                                container.removeView(textView)
                            } else {
                                container.addView(textView)
                            }

                            itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
            }
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
        with (itemView) {
            item_main_image_clothes_tags_container.hide()

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
        with (itemView) {
            if (item_main_image_clothes_tags_container.visibility == View.GONE) {
                item_main_image_clothes_tags_container.show()
            } else {
                item_main_image_clothes_tags_container.hide()
            }
        }
    }

    private fun onShowUsersTags() {
        with (itemView) {
            if (item_main_image_users_tags_container.visibility == View.GONE) {
                item_main_image_users_tags_container.show()
            } else {
                item_main_image_users_tags_container.hide()
            }
        }
    }

    companion object {
        private const val SPACE_TEXT_FORMAT = "%s %s"
    }
}