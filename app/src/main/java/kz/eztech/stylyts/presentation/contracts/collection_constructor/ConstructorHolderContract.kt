package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
interface ConstructorHolderContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}