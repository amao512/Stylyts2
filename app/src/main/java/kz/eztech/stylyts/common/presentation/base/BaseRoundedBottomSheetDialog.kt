package kz.eztech.stylyts.common.presentation.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ruslan Erdenoff on 10.01.2020.
 */
open class BaseRoundedBottomSheetDialog (val currentId:Int) : RoundedCommonBottomSheetDialog() {

    internal var listener: DialogChooserListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(currentId, container, false)
    }

    fun setChoiceListener(listener: DialogChooserListener){
        this.listener = listener
    }
}