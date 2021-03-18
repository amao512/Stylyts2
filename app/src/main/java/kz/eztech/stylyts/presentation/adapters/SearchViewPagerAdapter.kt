package kz.eztech.stylyts.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.main.search.SearchItemFragment
import kz.eztech.stylyts.presentation.interfaces.SearchListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchViewPagerAdapter(
    fragment: Fragment,
    private val itemClickListener: UniversalViewClickListener,
    private val searchListener: SearchListener
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return SearchItemFragment(position).apply {
            setOnClickListener(itemClickListener)
            setSearchListener(searchListener)
        }
    }
}