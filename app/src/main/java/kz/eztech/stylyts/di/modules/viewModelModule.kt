package kz.eztech.stylyts.di.modules

import kz.eztech.stylyts.presentation.presenters.search.SearchViewModel
import kz.eztech.stylyts.presentation.presenters.shop.ShopItemViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        SearchViewModel()
    }

    single {
        ShopItemViewModel()
    }
}