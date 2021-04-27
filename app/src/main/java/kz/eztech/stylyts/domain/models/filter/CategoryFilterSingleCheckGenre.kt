package kz.eztech.stylyts.domain.models.filter

import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup

class CategoryFilterSingleCheckGenre : SingleCheckExpandableGroup {

    private var iconResId = 0

    constructor(title: String, filterItems: List<FilterCheckModel>) : super(title, filterItems)

    constructor(parcel: Parcel) : super(parcel) {
        iconResId = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(iconResId)
    }

    override fun describeContents(): Int = 0

    companion object {
        val CREATOR: Parcelable.Creator<CategoryFilterSingleCheckGenre?> =
            object : Parcelable.Creator<CategoryFilterSingleCheckGenre?> {
                override fun createFromParcel(`in`: Parcel): CategoryFilterSingleCheckGenre? {
                    return CategoryFilterSingleCheckGenre(`in`)
                }

                override fun newArray(size: Int): Array<CategoryFilterSingleCheckGenre?> {
                    return arrayOfNulls(size)
                }
            }
    }
}