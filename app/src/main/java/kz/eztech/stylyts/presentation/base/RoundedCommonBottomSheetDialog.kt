package kz.eztech.stylyts.presentation.base

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kz.eztech.stylyts.R

/**
 * Created by Ruslan Erdenoff on 10.01.2020.
 */
open class RoundedCommonBottomSheetDialog : DialogFragment() {
    override fun getTheme(): Int = R.style.BaseBottomSheetDialogCommon

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
}