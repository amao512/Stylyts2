package kz.eztech.stylyts.global.presentation.clothes.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesSizeModel

class SizeAdapter(
    context: Context,
    styles: List<ClothesSizeModel>
) : ArrayAdapter<ClothesSizeModel>(context, 0, styles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val clothesSize: ClothesSizeModel? = getItem(position)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_style, parent, false)

        val styleTextView: TextView = view.findViewById(R.id.item_style_title_text_view)

        if (clothesSize != null) {
            styleTextView.text = clothesSize.size
        }

        return view
    }
}