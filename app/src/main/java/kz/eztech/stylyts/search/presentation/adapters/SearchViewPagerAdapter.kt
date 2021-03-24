package kz.eztech.stylyts.search.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.search.presentation.fragments.SearchItemFragment
import kz.eztech.stylyts.search.presentation.interfaces.SearchListener

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchViewPagerAdapter(
    fragment: Fragment,
    private val searchListener: SearchListener
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return SearchItemFragment(position).apply {
            setSearchListener(searchListener)
        }
    }
}