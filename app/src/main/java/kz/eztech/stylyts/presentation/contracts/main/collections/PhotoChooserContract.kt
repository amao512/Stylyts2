package kz.eztech.stylyts.presentation.contracts.main.collections

import android.net.Uri
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface PhotoChooserContract {
    interface View:BaseView{
        fun updatePhoto(path: Uri?)
    }
    interface Presenter:BasePresenter<View>
}