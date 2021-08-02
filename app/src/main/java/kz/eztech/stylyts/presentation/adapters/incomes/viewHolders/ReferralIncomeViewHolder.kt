package kz.eztech.stylyts.presentation.adapters.incomes.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_shop_order_clothes.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.referrals.ReferralItemModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
import kz.eztech.stylyts.presentation.utils.extensions.show

class ReferralIncomeViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var brandTitleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var clothesImageView: ImageView
    private lateinit var clothesTitleTextView: TextView
    private lateinit var secondPriceTextView: TextView
    private lateinit var countsTextView: TextView
    private lateinit var commissionTextView: TextView
    private lateinit var commissionCostTextView: TextView

    override fun bindData(item: Any, position: Int) {
        item as ReferralItemModel

        initializeViews()
        processClothes(clothes = item, position)
    }

    private fun initializeViews() {
        with(itemView) {
            brandTitleTextView = item_shop_order_clothes_brand_name_text_view
            priceTextView = item_shop_order_clothes_price_text_view
            clothesImageView = item_shop_order_clothes_image_view
            clothesTitleTextView = item_shop_order_clothes_title_text_view
            secondPriceTextView = item_shop_order_clothes_price_second_text_view
            countsTextView = item_shop_order_clothes_count_text_view
            commissionTextView = item_shop_order_clothes_commission_text_view
            commissionCostTextView = item_shop_order_clothes_commission_cost_text_view

            commissionTextView.show()
            commissionCostTextView.show()
        }
    }

    private fun processClothes(
        clothes: ReferralItemModel,
        position: Int
    ) {
        clothesTitleTextView.text = clothes.title

        val price = priceTextView.context.getString(
            R.string.price_tenge_text_format,
            clothes.cost.toString()
        )

        priceTextView.text = price
        secondPriceTextView.text = price
        commissionCostTextView.text = commissionCostTextView.context.getString(
            R.string.price_tenge_text_format,
            clothes.referralProfit.toString()
        )
        countsTextView.text = clothes.count.toString()

        if (clothes.coverImages.isNotEmpty()) {
            clothes.coverImages[0].loadImage(target = clothesImageView)
        }

        clothesImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothes)
        }
    }
}