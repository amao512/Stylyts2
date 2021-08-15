package kz.eztech.stylyts.home.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.MainApi
import kz.eztech.stylyts.global.data.repositories.MainRepository
import kz.eztech.stylyts.global.domain.repositories.MainDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class HomeModule {

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }

    @Provides
    fun provideMainRepository(mainRepository: MainRepository): MainDomainRepository {
        return mainRepository
    }
}