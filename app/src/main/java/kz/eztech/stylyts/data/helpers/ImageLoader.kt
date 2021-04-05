package kz.eztech.stylyts.data.helpers

import android.widget.ImageView
import com.bumptech.glide.Glide
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.helpers.DomainImageLoader

class ImageLoader : DomainImageLoader {

    override fun load(
        url: String,
        target: ImageView
    ) {
        Glide.with(target.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.rounded_rectangle_light_gray_half_transparent)
            .into(target)
    }
}