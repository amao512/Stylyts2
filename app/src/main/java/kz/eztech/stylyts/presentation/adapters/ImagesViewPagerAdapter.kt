package kz.eztech.stylyts.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
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
            container.context.resources.getIdentifier("jacket", "drawable", container.context.packageName)
        ).into(view.imageViewSlidePhoto)

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