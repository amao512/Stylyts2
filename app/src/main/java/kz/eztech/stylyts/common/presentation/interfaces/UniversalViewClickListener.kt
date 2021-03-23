package kz.eztech.stylyts.common.presentation.interfaces

import android.view.View

interface UniversalViewClickListener {
	fun onViewClicked(view: View, position:Int, item:Any?)
}