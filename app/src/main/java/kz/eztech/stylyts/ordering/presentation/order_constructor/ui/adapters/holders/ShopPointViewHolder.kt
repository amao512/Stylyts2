package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_shop_point.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.ordering.domain.models.order.ShopPointModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show

class ShopPointViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var shortNameTextView: TextView
    private lateinit var avatarImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var checkedImageView: ImageView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ShopPointModel

        initializeViews()
        processShopPoint(item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            shortNameTextView = item_shop_point_user_short_name_text_view
            avatarImageView = item_shop_point_user_avatar_shapeable_image_view
            titleTextView = item_shop_point_shop_name_text_view
            statusTextView = item_shop_point_address_status_text_view
            checkedImageView = item_shop_point_checked_image_view
        }
    }

    private fun processShopPoint(
        item: ShopPointModel,
        position: Int
    ) = with (item) {
        if (avatar.isEmpty()) {
            shortNameTextView.text = displayShortName
            avatarImageView.hide()
            shortNameTextView.show()
        } else {
            avatarImageView.show()
            shortNameTextView.hide()
            avatar.loadImageWithCenterCrop(target = avatarImageView)
        }
        titleTextView.text = title

        if (isSelected && selectedAddress == null) {
            statusTextView.text = statusTextView.context.getString(R.string.address_not_selected)
            statusTextView.setTextColor(
                ContextCompat.getColor(statusTextView.context, R.color.app_light_orange)
            )
            checkedImageView.hide()
        } else {
            statusTextView.text = statusTextView.context.getString(
                R.string.address_short_detail_text_format,
                selectedAddress?.city,
                selectedAddress?.street,
                selectedAddress?.apartment
            )
            statusTextView.setTextColor(
                ContextCompat.getColor(statusTextView.context, R.color.app_green)
            )
            checkedImageView.show()
        }

        itemView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, item)
        }
    }
}