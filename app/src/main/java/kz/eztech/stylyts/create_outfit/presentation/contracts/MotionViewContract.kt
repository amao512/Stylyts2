package kz.eztech.stylyts.create_outfit.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.utils.stick.MotionEntity

/**
 * Created by Ruslan Erdenoff on 07.02.2021.
 */
interface MotionViewContract : BaseView {

    fun deleteSelectedView(motionEntity: MotionEntity)
}