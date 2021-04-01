package kz.eztech.stylyts.di.modules

import kz.eztech.stylyts.presentation.presenters.search.SearchViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        SearchViewModel()
    }
}