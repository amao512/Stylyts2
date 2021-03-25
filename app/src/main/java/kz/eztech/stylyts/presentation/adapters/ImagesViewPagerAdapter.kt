package kz.eztech.stylyts.presentation.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.pager_item_vp_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by ZhenisMadiyar on 05,Февраль,2019
 */
class ImagesViewPagerAdapter(val images: ArrayList<String>) : PagerAdapter() {

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    var mItemImageClickListener: UniversalViewClickListener? = null
    fun setOnImageClickListener(click: UniversalViewClickListener) {
        mItemImageClickListener = click
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = container.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pager_item_vp_image, null)

        Glide.with(container.context).load(
           images[position]
        ).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false;
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                view.imageViewSlidePhoto.startAnimation(AnimationUtils.loadAnimation(view.context,R.anim.item_detail_animation))
                return false
            }
        })
            .into(view.imageViewSlidePhoto)

        (container as ViewPager).addView(view, 0)

        view.setOnClickListener {
            mItemImageClickListener?.onViewClicked(view,position, images)
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}