package kz.eztech.stylyts.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.main.constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.main.constructor.PhotoPostCreatorFragment

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
class CollectionConstructorPagerAdapter (fa: Fragment, val args: Bundle? = null) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 2
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CollectionConstructorFragment().apply {
                    args?.putInt("currentType",0)
                    arguments = args
                }
            }
            1 -> {
                CollectionConstructorFragment().apply {
                    args?.putInt("currentType",1)
                    arguments = args
                }
            }
            else -> CollectionConstructorFragment()
        }
    }
}