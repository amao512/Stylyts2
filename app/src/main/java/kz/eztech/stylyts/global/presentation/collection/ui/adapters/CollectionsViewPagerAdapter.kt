package kz.eztech.stylyts.global.presentation.collection.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.collections.presentation.ui.CollectionItemFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionsViewPagerAdapter(
    fa: Fragment,
    private val itemClickListener: UniversalViewClickListener? = null
) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 2

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CollectionItemFragment(currentMode = 0).apply {
                setOnClickListener(itemClickListener)
            }
            else -> CollectionItemFragment(currentMode = 1).apply {
                setOnClickListener(itemClickListener)
            }
        }
    }
}