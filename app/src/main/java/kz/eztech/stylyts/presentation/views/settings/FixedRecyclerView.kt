package kz.eztech.stylyts.presentation.views.settings

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class FixedRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun canScrollVertically(direction: Int): Boolean {
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original
        }

        return super.canScrollVertically(direction)
    }
}