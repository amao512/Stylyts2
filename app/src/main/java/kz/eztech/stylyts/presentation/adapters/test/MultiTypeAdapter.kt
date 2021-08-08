package kz.eztech.stylyts.presentation.adapters.test

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

open class MultiTypeAdapter @JvmOverloads constructor(
    open var items: List<Any> = emptyList(),
    open val initialCapacity: Int = 0,
    open var types: Types = MutableTypes(initialCapacity)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun <T> register(clazz: Class<T>, delegate: ItemViewDelegate<T, *>) {
        unregisterAllTypesIfNeeded(clazz)
        register(Type(clazz, delegate, DefaultLinker()))
    }

    inline fun <reified T : Any> register(delegate: ItemViewDelegate<T, *>) {
        register(T::class.java, delegate)
    }

    fun <T : Any> register(clazz: KClass<T>, delegate: ItemViewDelegate<T, *>) {
        register(clazz.java, delegate)
    }

    fun <T> register(clazz: Class<T>, binder: ItemViewBinder<T, *>) {
        register(clazz, binder as ItemViewDelegate<T, *>)
    }

    inline fun <reified T : Any> register(binder: ItemViewBinder<T, *>) {
        register(binder as ItemViewDelegate<T, *>)
    }

    fun <T : Any> register(clazz: KClass<T>, binder: ItemViewBinder<T, *>) {
        register(clazz, binder as ItemViewDelegate<T, *>)
    }

    internal fun <T> register(type: Type<T>) {
        types.register(type)
        type.delegate._adapter = this
    }

    @CheckResult
    fun <T> register(clazz: Class<T>): OneToManyFlow<T> {
        unregisterAllTypesIfNeeded(clazz)
        return OneToManyBuilder(this, clazz)
    }

    @CheckResult
    fun <T : Any> register(clazz: KClass<T>): OneToManyFlow<T> {
        return register(clazz.java)
    }

    fun registerAll(types: Types) {
        val size = types.size
        for (i in 0 until size) {
            val type = types.getType<Any>(i)
            unregisterAllTypesIfNeeded(type.clazz)
            register(type)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return indexInTypesOf(position, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return types.getType<Any>(viewType).delegate.onCreateViewHolder(parent.context, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        val item = items[position]
        getOutDelegateByViewHolder(holder).onBindViewHolder(holder, item, payloads)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        val item = items[position]
        val itemViewType = getItemViewType(position)

        return types.getType<Any>(itemViewType).delegate.getItemId(item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return getOutDelegateByViewHolder(holder).onFailedRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        getOutDelegateByViewHolder(holder).onViewDetachedFromWindow(holder)
    }

    private fun getOutDelegateByViewHolder(holder: RecyclerView.ViewHolder): ItemViewDelegate<Any, RecyclerView.ViewHolder> {
        @Suppress("UNCHECKED_CAST")
        return types.getType<Any>(holder.itemViewType).delegate as ItemViewDelegate<Any, RecyclerView.ViewHolder>
    }


    @Throws(DelegateNotFoundException::class)
    internal fun indexInTypesOf(position: Int, item: Any): Int {
        val index = types.firstIndexOf(item.javaClass)
        if (index != -1) {
            val linker = types.getType<Any>(index).linker
            return index + linker.index(position, item)
        }
        throw DelegateNotFoundException(item.javaClass)
    }

    private fun <T> unregisterAllTypes(clazz: Class<T>) {
        if (types.unregister(clazz)) {
            Log.w("TAG", "The type ${clazz.simpleName} you originally registered is now overwritten.")
        }
    }

    private fun unregisterAllTypesIfNeeded(clazz: Class<*>) {
        if (types.unregister(clazz)) {
            Log.w("TAG", "The type ${clazz.simpleName} you originally registered is now overwritten.")
        }
    }
}