package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_style.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

class StyleHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var titleTextView: TextView

    override fun bindData(item: Any, position: Int) {
        item as ClothesStyleModel

        initializeViews()
        processStyle(clothesStyleModel = item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            titleTextView = item_style_title_text_view
        }
    }

    private fun processStyle(
        clothesStyleModel: ClothesStyleModel,
        position: Int
    ) {
        titleTextView.text = clothesStyleModel.title

        titleTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothesStyleModel)
        }
    }
}