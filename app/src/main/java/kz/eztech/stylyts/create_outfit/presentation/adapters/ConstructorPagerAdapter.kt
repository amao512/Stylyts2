package kz.eztech.stylyts.create_outfit.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.create_outfit.presentation.fragments.CollectionConstructorHolderFragment
import kz.eztech.stylyts.create_outfit.presentation.fragments.PhotoPostCreatorFragment

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class ConstructorPagerAdapter(
    fa: Fragment,
    val args: Bundle? = null
) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 2

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CollectionConstructorHolderFragment().apply {
                arguments = args
            }
            1 -> PhotoPostCreatorFragment().apply {
                arguments = args
            }
            else -> CollectionConstructorHolderFragment()
        }
    }
}