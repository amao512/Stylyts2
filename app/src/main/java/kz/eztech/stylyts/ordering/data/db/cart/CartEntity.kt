package kz.eztech.stylyts.ordering.data.db.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table", indices = [Index(value = ["id"], unique = true)])
data class CartEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "clothes_type_id")
    val typeId: Int? = null,

    @ColumnInfo(name = "clothes_category_id")
    val categoryId: Int? = null,

    @ColumnInfo(name = "clothes_cover_image")
    val coverImage: String? = null,

    @ColumnInfo(name = "clothes_brand_id")
    val brandId: Int? = null,

    @ColumnInfo(name = "clothes_brand_title")
    val brandTitle: String? = null,

    @ColumnInfo(name = "owner_id")
    val ownerId: Int? = null,

    @ColumnInfo(name = "clothes_title")
    val title: String? = null,

    @ColumnInfo(name = "product_code")
    val productCode: String? = null,

    @ColumnInfo(name = "total_count")
    val totalCount: Int? = null,

    @ColumnInfo(name = "count")
    var count: Int? = null,

    @ColumnInfo(name = "clothes_price")
    var price: Int? = null,

    @ColumnInfo(name = "clothes_sale_price")
    var salePrice: Int? = null,

    @ColumnInfo(name = "currency")
    val currency: String? = null,

    @ColumnInfo(name = "size")
    var size: String? = null,

    @ColumnInfo(name = "size_list")
    val sizeList: ArrayList<String>? = null,

    @ColumnInfo(name = "referral_user")
    val referralUser: Int? = null
) {
    val displayPrice
        get() = "$price ₸"

    val displaySalePrice
        get() = "$salePrice ₸"
}