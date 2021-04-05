package kz.eztech.stylyts.domain.helpers

import android.widget.ImageView

interface DomainImageLoader {

    fun load(
        url: String,
        target: ImageView
    )
}