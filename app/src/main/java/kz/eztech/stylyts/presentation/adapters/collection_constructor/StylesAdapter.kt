package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel

class StylesAdapter(
    context: Context,
    styles: List<ClothesStyleModel>
) : ArrayAdapter<ClothesStyleModel>(context, 0, styles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val style: ClothesStyleModel? = getItem(position)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_style, parent, false)

        val styleTextView: TextView = view.findViewById(R.id.item_style_title_text_view)

        if (style != null) {
            styleTextView.text = style.title
        }

        return view
    }
}