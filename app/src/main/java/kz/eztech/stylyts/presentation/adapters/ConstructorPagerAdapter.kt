package kz.eztech.stylyts.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.main.constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.main.constructor.PhotoPostCreatorFragment
import kz.eztech.stylyts.presentation.fragments.main.shop.ShopItemFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class ConstructorPagerAdapter (fa: Fragment) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 2
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                CollectionConstructorFragment()
            }
            1 -> {
                PhotoPostCreatorFragment()
            }
            else -> CollectionConstructorFragment()
        }
    }
}