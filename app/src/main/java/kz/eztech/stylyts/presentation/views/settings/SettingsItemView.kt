package kz.eztech.stylyts.presentation.views.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_settings.view.*
import kz.eztech.stylyts.R

class SettingsItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttributeStyle: Int = 0
) : LinearLayout(context, attrs, defAttributeStyle) {

    init {
        inflate(context, R.layout.item_settings, this)
    }

    fun setItem(
        title: String,
        icon: Int
    ) {
        item_settings_title_text_view.text = title
        item_settings_icon_image_view.setBackgroundResource(icon)
    }

    fun setTitleColor(color: Int) {
        item_settings_title_text_view.setTextColor(ContextCompat.getColor(context, color))
    }
}