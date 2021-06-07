package kz.eztech.stylyts.domain.models.common

data class PageFilterModel(
    var page: Int = 1,
    var isLastPage: Boolean = false
)