package kz.eztech.stylyts.presentation.adapters.test

import java.lang.RuntimeException

internal class DelegateNotFoundException(clazz: Class<*>) : RuntimeException(
    "Have you registered the ${clazz.name} type and its delegate or binder?"
)