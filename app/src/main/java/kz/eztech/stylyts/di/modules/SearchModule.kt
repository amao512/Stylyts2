package kz.eztech.stylyts.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.SearchAPI
import kz.eztech.stylyts.data.db.search.SearchDataSource
import kz.eztech.stylyts.data.repository.search.SearchRepository
import kz.eztech.stylyts.domain.repository.search.SearchDomainRepository
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