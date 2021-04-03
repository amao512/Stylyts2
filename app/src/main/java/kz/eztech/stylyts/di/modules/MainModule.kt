package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.MainApi
import kz.eztech.stylyts.data.repository.main.MainLentaRepository
import kz.eztech.stylyts.data.repository.main.MainRepository
import kz.eztech.stylyts.domain.repository.main.MainDomainRepository
import kz.eztech.stylyts.domain.repository.main.MainLentaDomainRepository
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

    @Provides
    fun providesMainLentaRepository(mainLentaRepository: MainLentaRepository): MainLentaDomainRepository {
        return mainLentaRepository
    }
}