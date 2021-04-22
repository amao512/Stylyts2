package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
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
) : BaseViewHolder(itemView, adapter) {

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
            item_main_image_root_view.setOnClickListener { thisView ->
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

    companion object {
        private const val SPACE_TEXT_FORMAT = "%s %s"
    }
}