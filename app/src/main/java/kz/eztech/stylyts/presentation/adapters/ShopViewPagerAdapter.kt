package kz.eztech.stylyts.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.shop.ShopItemFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */

class ShopViewPagerAdapter(
    fa: Fragment,
    var itemClickListener: UniversalViewClickListener? = null
) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 2
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment =
        ShopItemFragment(position).apply { setOnClickListener(itemClickListener) }
}