package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.animation.ObjectAnimator
import android.view.View
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterExpandableViewHolder(
    itemView: View
) : GroupViewHolder(itemView) {

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    fun onBind(title: String) {
        with(itemView) {
            item_filter_title.text = title
        }
    }

    private fun animateExpand() {
        startObjectRotation(
            view = itemView.item_filter_arrow,
            y = 90f
        )
    }

    private fun animateCollapse() {
        startObjectRotation(
            view = itemView.item_filter_arrow,
            y = 0f
        )
    }

    private fun startObjectRotation(view: View, y: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, view.rotation, y)

        objectAnimator.duration = 300
        objectAnimator.start()
    }
}