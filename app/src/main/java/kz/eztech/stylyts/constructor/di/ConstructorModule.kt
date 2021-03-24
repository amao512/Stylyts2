package kz.eztech.stylyts.constructor.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.constructor.data.api.ConstructorApi
import kz.eztech.stylyts.constructor.data.repository.ConstructorRepository
import kz.eztech.stylyts.constructor.domain.repository.ConstructorDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ConstructorModule {

    @Provides
    @Singleton
    fun provideConstructorApi(retrofit: Retrofit): ConstructorApi {
        return retrofit.create(ConstructorApi::class.java)
    }

    @Provides
    fun provideConstructorRepository(constructorDomainRepository: ConstructorRepository): ConstructorDomainRepository {
        return constructorDomainRepository
    }
}