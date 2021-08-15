package kz.eztech.stylyts.global.presentation.common.interfaces

import android.view.View

interface UniversalViewDoubleClickListener {
	fun onViewDoubleClicked(view: View, position:Int, item:Any?,isDouble:Boolean? = false)
}