package kz.eztech.stylyts.domain.models.profile

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup

class FilterSingleCheckGenre : SingleCheckExpandableGroup {

    private var iconResId = 0

    constructor(title: String, filterItems: List<FilterItemModel>) : super(title, filterItems)

    constructor(parcel: Parcel) : super(parcel) {
        iconResId = parcel.readInt()
    }

    override fun onChildClicked(childIndex: Int, checked: Boolean) {
        super.onChildClicked(childIndex, checked)

        Log.d("TAG", "filter - $childIndex")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(iconResId)
    }

    override fun describeContents(): Int =  0

    companion object {
        val CREATOR: Parcelable.Creator<FilterSingleCheckGenre?> =
            object : Parcelable.Creator<FilterSingleCheckGenre?> {
                override fun createFromParcel(`in`: Parcel): FilterSingleCheckGenre? {
                    return FilterSingleCheckGenre(`in`)
                }

                override fun newArray(size: Int): Array<FilterSingleCheckGenre?> {
                    return arrayOfNulls(size)
                }
            }
    }
}