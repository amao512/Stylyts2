package kz.eztech.stylyts.search.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.search.data.SearchAPI
import kz.eztech.stylyts.search.data.db.SearchDataSource
import kz.eztech.stylyts.search.data.repository.SearchRepository
import kz.eztech.stylyts.search.domain.repository.SearchDomainRepository
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
}