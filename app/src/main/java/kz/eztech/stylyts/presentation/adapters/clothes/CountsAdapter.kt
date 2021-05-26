package kz.eztech.stylyts.presentation.adapters.clothes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesCountModel

class CountsAdapter(
    context: Context,
    styles: List<ClothesCountModel>
) : ArrayAdapter<ClothesCountModel>(context, 0, styles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val clothesCount: ClothesCountModel? = getItem(position)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_style, parent, false)

        val styleTextView: TextView = view.findViewById(R.id.item_style_title_text_view)

        if (clothesCount != null) {
            styleTextView.text = clothesCount.count.toString()
        }

        return view
    }
}