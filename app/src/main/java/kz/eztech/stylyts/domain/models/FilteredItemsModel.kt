package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
data class FilteredItemsModel(
	@SerializedName("results")
	@Expose
	var results: List<ClothesMainModel>? = null
)