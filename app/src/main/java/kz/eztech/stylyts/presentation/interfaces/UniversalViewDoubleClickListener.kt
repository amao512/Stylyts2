package kz.eztech.stylyts.presentation.interfaces

import android.view.View

interface UniversalViewDoubleClickListener {
	fun onViewDoubleClicked(view: View, position:Int, item:Any?,isDouble:Boolean? = false)
}