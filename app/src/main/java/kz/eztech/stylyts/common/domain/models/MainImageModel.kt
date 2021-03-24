package kz.eztech.stylyts.common.domain.models

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
data class MainImageModel(
    val name:String? = "zara",
    val cost:Int? = 65500,
    val comments:Int? = 53,
    val date:String? = "23 сентября",
    val additionals:List<MainImageAdditionalModel>? = ArrayList()
)

data class MainImageAdditionalModel(
    val name:String? = "jacket"
)