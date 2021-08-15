package kz.eztech.stylyts.global.di.modules

import kz.eztech.stylyts.global.presentation.common.viewModels.PagerViewModel
import kz.eztech.stylyts.search.presentation.search.viewModels.SearchViewModel
import kz.eztech.stylyts.profile.presentation.subscriptions.viewModels.UserSubsViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        SearchViewModel()
    }

    single {
        UserSubsViewModel()
    }

    single {
        PagerViewModel()
    }
}