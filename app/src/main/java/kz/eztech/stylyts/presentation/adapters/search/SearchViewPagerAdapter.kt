package kz.eztech.stylyts.presentation.adapters.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.search.SearchItemFragment

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = SearchItemFragment(position)
}