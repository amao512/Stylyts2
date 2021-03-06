package kz.eztech.stylyts.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.main.shop.ShopItemFragment
import kz.eztech.stylyts.presentation.fragments.main.users.UserSubsItemFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubViewPagerAdapter(fa: Fragment, var itemClickListener: UniversalViewClickListener? = null) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 2
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = UserSubsItemFragment()
}