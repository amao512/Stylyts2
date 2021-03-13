package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_shop_category.view.*
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryHolder(itemView: View, adapter: BaseAdapter) : BaseViewHolder(itemView, adapter) {
    override fun bindData(item: Any, position: Int) {
        with(item as GenderCategory) {
            with(itemView) {
                Glide.with(this).load(
                    cover_image
                ).into(this.image_view_item_shop_fragment)
                text_view_item_shop_fragment.text = title

                constraint_layout_item_shop_category.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it, position, item)
                }
            }
        }
    }
}