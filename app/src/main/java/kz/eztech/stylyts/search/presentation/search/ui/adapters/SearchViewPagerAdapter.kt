package kz.eztech.stylyts.search.presentation.search.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.search.presentation.search.ui.SearchItemFragment

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = SearchItemFragment(position)
}