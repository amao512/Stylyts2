package kz.eztech.stylyts.ordering.domain.models.order

data class CustomerModel(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String
)