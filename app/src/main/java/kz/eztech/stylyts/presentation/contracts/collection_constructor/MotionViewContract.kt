package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity

/**
 * Created by Ruslan Erdenoff on 07.02.2021.
 */
interface MotionViewContract : BaseView {

    fun deleteSelectedView(motionEntity: MotionEntity)
}