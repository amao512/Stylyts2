package kz.eztech.stylyts.search.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.SearchAPI
import kz.eztech.stylyts.global.data.db.search.SearchDataSource
import kz.eztech.stylyts.global.data.repositories.SearchRepository
import kz.eztech.stylyts.global.domain.repositories.SearchDomainRepository
import kz.eztech.stylyts.search.presentation.shop.data.UISHopItemDataDelegate
import kz.eztech.stylyts.search.presentation.shop.data.UIShopCategoryData
import kz.eztech.stylyts.search.presentation.shop.data.UIShopCategoryDataDelegate
import kz.eztech.stylyts.search.presentation.shop.data.UIShopItemData
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
@Module
class SearchModule(private val mApplication: Application) {

    @Provides
    fun provideSearchDataSource(): SearchDataSource = SearchDataSource(mApplication)

    @Provides
    @Singleton
    internal fun provideSearchApi(retrofit: Retrofit): SearchAPI {
        return retrofit.create(SearchAPI::class.java)
    }

    @Provides
    fun provideSearchRepository(searchRepository: SearchRepository): SearchDomainRepository {
        return searchRepository
    }

    @Provides
    fun provideUIShopItemData(uiShopItemDelegate: UISHopItemDataDelegate): UIShopItemData {
        return uiShopItemDelegate
    }

    @Provides
    fun provideUIShopCategoryData(): UIShopCategoryData {
        return UIShopCategoryDataDelegate()
    }
}