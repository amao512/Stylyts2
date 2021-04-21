package kz.eztech.stylyts.presentation.adapters.holders

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

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
        processCollectionInfo(
            postModel = item,
            position = position
        )
        loadUserPhoto(
            publicationModel = item,
            position = position
        )

        Log.d("TAG3", "$item")
    }

    private fun initializeAdapters(postModel: PostModel) {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = adapter.itemClickListener

        with (itemView) {
            recycler_view_item_main_image_additionals_list.adapter = additionalAdapter
            postModel.clothes?.let {
                additionalAdapter.updateList(list = it)
            }
        }
    }

    private fun processCollectionInfo(
        postModel: PostModel,
        position: Int
    ) {
        with (itemView) {
            postModel.owner?.let {
                text_view_item_main_image_partner_name.text = "${it.firstName} ${it.lastName}"

                if (it.avatar.isBlank()) {
                    shapeable_image_view_item_main_image_profile_avatar.hide()
                    text_view_item_main_image_short_name.show()
                    text_view_item_main_image_short_name.text = getShortName(it.firstName, it.lastName)
                } else {
                    text_view_item_main_image_short_name.hide()
                    Glide.with(shapeable_image_view_item_main_image_profile_avatar.context)
                        .load(it.avatar)
                        .into(shapeable_image_view_item_main_image_profile_avatar)
                }
            }

//            constraint_layout_fragment_item_main_image_profile_container.setOnClickListener { thisView ->
//                adapter.itemClickListener?.onViewClicked(thisView, position, publicationModel)
//            }
//                button_item_main_image_change_collection.setOnClickListener { thisView ->
//                    adapter.itemClickListener?.onViewClicked(thisView, position, item)
//                }

            text_view_item_main_image_comments_cost.text = "999 KZT"
//                    "${NumberFormat.getInstance().format(total_price)} $total_price_currency"
            text_view_item_main_image_comments_count.text = "Показать 99 коммент."

//                text_view_item_main_image_comments_count.setOnClickListener { thisView ->
//                    adapter.itemClickListener?.onViewClicked(thisView, position, item)
//                }
//            text_view_item_main_image_date.text =
//                "${DateFormatterHelper.formatISO_8601(publicationModel.created, FORMAT_DATE_DD_MMMM)}"
        }
    }

    private fun loadUserPhoto(
        publicationModel: PostModel,
        position: Int
    ) {
        with (itemView) {
            Glide.with(this)
                .load(publicationModel.images[0])
                .fitCenter()
                .into(this.image_view_item_main_image_imageholder)

            image_view_item_main_image_imageholder.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(
                    image_view_item_main_image_imageholder,
                    position,
                    publicationModel
                )
            }
        }
    }
}