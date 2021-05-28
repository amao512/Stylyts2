package kz.eztech.stylyts.di.modules

import kz.eztech.stylyts.presentation.presenters.search.SearchViewModel
import kz.eztech.stylyts.presentation.presenters.users.UserSubsViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        SearchViewModel()
    }

    single {
        UserSubsViewModel()
    }
}