package kz.eztech.stylyts.collections.presentation.ui.adapters

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_collection_filter.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionFilterHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var filterItemLinearLayout: LinearLayout
    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as CollectionFilterModel

        initializeViews()
        processFilterItem(collectionFilterModel = item, position = position)
        processDisabledItem(collectionFilterModel = item)
    }

    private fun initializeViews() {
        with(itemView) {
            filterItemLinearLayout = frame_layout_item_collection_filter
            iconImageView = item_collection_filter_icon_image_view
            titleTextView = item_collection_filter_title_text_view
        }
    }

    private fun processFilterItem(
        collectionFilterModel: CollectionFilterModel,
        position: Int
    ) = with(collectionFilterModel) {
        titleTextView.text = name

        if (isChosen) {
            filterItemLinearLayout.background = getDrawable(
                filterItemLinearLayout.context,
                R.drawable.rounded_rectangle_gray
            )
        } else {
            filterItemLinearLayout.background = getDrawable(
                filterItemLinearLayout.context,
                R.drawable.rounded_rectangle_white_gray_corners
            )
        }

        filterItemLinearLayout.setOnClickListener {
            if (!isDisabled) {
                adapter.itemClickListener?.onViewClicked(it, position, collectionFilterModel)
            }
        }

        if (icon != null) {
            iconImageView.setImageResource(icon)
            iconImageView.show()
        } else {
            iconImageView.hide()
        }
    }

    private fun processDisabledItem(collectionFilterModel: CollectionFilterModel) {
        if (collectionFilterModel.isDisabled) {
            filterItemLinearLayout.setBackgroundResource(
                R.drawable.rounded_rectangle_white_gray_corners_disabled
            )
            titleTextView.setTextColor(
                ContextCompat.getColor(titleTextView.context, R.color.app_gray)
            )
            iconImageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(titleTextView.context, R.color.app_gray)
            )
        } else {
            titleTextView.setTextColor(
                ContextCompat.getColor(titleTextView.context, R.color.app_black)
            )
            iconImageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(titleTextView.context, R.color.app_black)
            )
        }
    }
}