package kz.eztech.stylyts.profile.presentation.adapters.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacesItemDecoration(
    private val space: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = space

        if (parent.getChildLayoutPosition(view) == 0  || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space
        } else {
            outRect.top = 0
        }

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = space
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
            outRect.right = space
        }
    }
}