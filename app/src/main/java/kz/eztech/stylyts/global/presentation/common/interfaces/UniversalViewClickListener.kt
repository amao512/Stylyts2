package kz.eztech.stylyts.global.presentation.common.interfaces

import android.view.View

interface UniversalViewClickListener {

    fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    )
}