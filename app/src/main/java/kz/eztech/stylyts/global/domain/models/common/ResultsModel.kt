package kz.eztech.stylyts.global.domain.models.common

data class ResultsModel<T>(
    val page: Int,
    val totalPages: Int,
    val pageSize: Int,
    val totalCount: Int,
    val results: List<T>
)