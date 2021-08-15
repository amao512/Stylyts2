package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CollectionConstructorFragment

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
class CollectionConstructorPagerAdapter(
    fa: Fragment,
    val args: Bundle? = null
) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 2

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> getCollectionConstructor(currentType = 0)
            1 -> getCollectionConstructor(currentType = 1)
            else -> CollectionConstructorFragment()
        }
    }

    private fun getCollectionConstructor(currentType: Int): CollectionConstructorFragment {
        return CollectionConstructorFragment().apply {
            args?.putInt(CollectionConstructorFragment.CURRENT_TYPE_KEY, currentType)
            arguments = args
        }
    }
}