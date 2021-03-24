package kz.eztech.stylyts.constructor.presentation.contracts

import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import java.io.File

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
interface CleanBackgroundContract {

    interface View : BaseView {

        fun processItemDetail(model: ClothesMainModel)
    }

    interface Presenter : BasePresenter<View> {

        fun saveItem(
            token: String,
            hashMap: HashMap<String, String>,
            data: File
        )

        fun getItemDetail(
            token: String,
            id: Int
        )
    }
}