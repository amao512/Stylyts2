package kz.eztech.stylyts.presentation.adapters.test

import kotlin.reflect.KClass

interface OneToManyEndpoint<T> {

    fun withLinker(linker: Linker<T>)

    fun withLinker(linker: (position: Int, item: T) -> Int) {
        withLinker(object : Linker<T> {
            override fun index(position: Int, item: T): Int {
                return linker(position, item)
            }
        })
    }

    fun withJavaClassLinker(javaClassLinker: JavaClassLinker<T>)

    private fun withJavaClassLinker(classLinker: (position: Int, item: T) -> Class<out ItemViewDelegate<T, *>>) {
        withJavaClassLinker(object : JavaClassLinker<T> {
            override fun index(position: Int, item: T): Class<out ItemViewDelegate<T, *>> {
                return classLinker(position, item)
            }
        })
    }

    fun withKotlinClassLinker(classLinker: KotlinClassLinker<T>) {
        withJavaClassLinker { position, item -> classLinker.index(position, item).java }
    }

    fun withKotlinClassLinker(classLinker: (position: Int, item: T) -> KClass<out ItemViewDelegate<T, *>>) {
        withJavaClassLinker { position, item -> classLinker(position, item).java }
    }
}