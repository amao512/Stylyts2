package kz.eztech.stylyts.presentation.adapters.profile.holders

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_filter_group.view.*

class FilterGroupViewHolder(
    itemView: View
) : GroupViewHolder(itemView) {

    override fun expand() {
        super.expand()
        animateExpand()
    }

    override fun collapse() {
        super.collapse()
        animateCollapse()
    }

    fun onBind(title: String) {
        with(itemView) {
            item_filter_group_title.text = title
        }
    }

    private fun animateExpand() {
        with (itemView) {
            item_filter_group_arrow.startAnimation(getRotateAnimation(
                fromDegrees = 0f,
                toDegrees = 90f
            ))
        }
    }

    private fun animateCollapse() {
        with (itemView) {
            item_filter_group_arrow.startAnimation(getRotateAnimation(
                fromDegrees = 90f,
                toDegrees = 0f
            ))
        }
    }

    private fun getRotateAnimation(
        fromDegrees: Float,
        toDegrees: Float
    ): RotateAnimation {
        val rotate = RotateAnimation(fromDegrees, toDegrees, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true

        return rotate
    }

}