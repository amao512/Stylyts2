package kz.eztech.stylyts.presentation.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.presentation.fragments.main.search.SearchItemFragment
import kz.eztech.stylyts.presentation.interfaces.SearchListener

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchViewPagerAdapter(
    fragment: Fragment,
    private val searchListener: SearchListener
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        Log.d("TAG", "position = $position")

        return SearchItemFragment(position).apply {
            setSearchListener(searchListener)
        }
    }
}