package kz.eztech.stylyts.collection_constructor.presentation.contracts

import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.stick.MotionEntity

/**
 * Created by Ruslan Erdenoff on 07.02.2021.
 */
interface MotionViewContract : BaseView {

    fun deleteSelectedView(motionEntity: MotionEntity)
}