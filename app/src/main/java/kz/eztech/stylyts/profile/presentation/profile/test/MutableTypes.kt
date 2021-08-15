package kz.eztech.stylyts.profile.presentation.profile.test

open class MutableTypes constructor(
    open val initialCapacity: Int = 0,
    open val types: MutableList<BaseViewHolder> = ArrayList(initialCapacity)
){

    val size get() = types.size

    fun register(type: BaseViewHolder) {
        types.add(type)
    }

    fun getType(index: Int): BaseViewHolder = types[index]
}