package kz.eztech.stylyts.presentation.utils.extensions.mappers

import kz.eztech.stylyts.data.api.models.ActionApiModel
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.presentation.enums.LikeEnum

fun ActionApiModel?.map(): ActionModel {
    return ActionModel(action = this?.action ?: LikeEnum.UNLIKE.title)
}