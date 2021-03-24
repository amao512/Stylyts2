package kz.eztech.stylyts.common.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
@Entity(tableName = "cart_table",indices = arrayOf(Index(value = ["id"],unique = true)))
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @SerializedName("item_model")
    @Expose
    @ColumnInfo(name = "item_model")
    var item_model: String? = null,
    /*@SerializedName("item_id")
    @Expose
    @ColumnInfo(name = "item_id")
    var item_id: Int? = null,

    @SerializedName("clothes_type_id")
    @Expose
    @ColumnInfo(name = "clothes_type_id")
    var clothes_type_id:Int? = null,

    @SerializedName("clothes_type_title")
    @Expose
    @ColumnInfo(name = "clothes_type_title")
    var clothes_type_title:String? = null,

    @SerializedName("clothes_type_clothes_category")
    @Expose
    @ColumnInfo(name = "clothes_type_clothes_category")
    var clothes_type_clothes_category:Int? = null,

    @SerializedName("clothes_type_body_part")
    @Expose
    @ColumnInfo(name = "clothes_type_body_part")
    var clothes_type_body_part:Int? = null,

    @SerializedName("clothes_type_constructor_icon")
    @Expose
    @ColumnInfo(name = "clothes_type_constructor_icon")
    var constructor_icon:String? = null,

    @SerializedName("cover_photo")
    @Expose
    @ColumnInfo(name = "cover_photo")
    var cover_photo:String? = null,

    @SerializedName("constructor_photo")
    @Expose
    @ColumnInfo(name = "constructor_photo")
    var constructor_photo:String? = null,


    @SerializedName("brand_id")
    @Expose
    @ColumnInfo(name = "brand_id")
    var brand_id:Int? = null,


    @SerializedName("brand_is_brand")
    @Expose
    @ColumnInfo(name = "brand_is_brand")
    var brand_is_brand:Boolean? = null,

    @SerializedName("brand_role")
    @Expose
    @ColumnInfo(name = "brand_role")
    var brand_role:String? = null,

    @SerializedName("brand_first_name")
    @Expose
    @ColumnInfo(name = "brand_first_name")
    var brand_first_name:String? = null,

    @SerializedName("brand_last_name")
    @Expose
    @ColumnInfo(name = "brand_last_name")
    var brand_last_name:String? = null,

    @SerializedName("brand_avatar")
    @Expose
    @ColumnInfo(name = "brand_avatar")
    var brand_avatar:String? = null,

    @SerializedName("clothes_sizes_id")
    @Expose
    @ColumnInfo(name = "clothes_sizes_id")
    var clothes_sizes_id:Int? = null,

    @SerializedName("clothes_sizes_size")
    @Expose
    @ColumnInfo(name = "clothes_sizes_size")
    var clothes_sizes_size:String? = null,


    @SerializedName("clothes_colors_id")
    @Expose
    @ColumnInfo(name = "clothes_colors_id")
    var clothes_colors_id:Int? = null,

    @SerializedName("clothes_colors_color")
    @Expose
    @ColumnInfo(name = "clothes_colors_color")
    var clothes_colors_color:String? = null,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title:String? = null,

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    var description:String? = null,

    @SerializedName("gender")
    @Expose
    @ColumnInfo(name = "gender")
    var gender:String? = null,

    @SerializedName("cost")
    @Expose
    @ColumnInfo(name = "cost")
    var cost:Int? = null,

    @SerializedName("sale_cost")
    @Expose
    @ColumnInfo(name = "sale_cost")
    var sale_cost:Int? = null,

    @SerializedName("currency")
    @Expose
    @ColumnInfo(name = "currency")
    var currency:String? = null,

    @SerializedName("product_code")
    @Expose
    @ColumnInfo(name = "product_code")
    var product_code:String? = null*/

)