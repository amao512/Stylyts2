package kz.eztech.stylyts.domain.models

data class ResultsModel<T>(
    val page: Int,
    val totalPages: Int,
    val pageSize: Int,
    val totalCount: Int,
    val results: List<T>
)