package kz.eztech.stylyts.profile.presentation.subscriptions.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.profile.presentation.subscriptions.ui.UserSubsItemFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
class UserSubViewPagerAdapter(
    fa: Fragment,
    var itemClickListener: UniversalViewClickListener? = null,
    private val userId: Int
) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 2

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return UserSubsItemFragment.getNewInstance(
            position = position,
            userId = userId
        )
    }
}