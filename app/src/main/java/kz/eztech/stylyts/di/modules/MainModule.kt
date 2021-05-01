package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.MainApi
import kz.eztech.stylyts.data.repository.MainRepository
import kz.eztech.stylyts.domain.repository.MainDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class MainModule {

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