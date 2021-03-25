package kz.eztech.stylyts.collection_constructor.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.collection_constructor.data.api.CollectionConstructorApi
import kz.eztech.stylyts.collection_constructor.data.repository.CollectionConstructorRepository
import kz.eztech.stylyts.collection_constructor.domain.repository.CollectionConstructorDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CollectionConstructorModule {

    @Provides
    @Singleton
    fun provideConstructorApi(retrofit: Retrofit): CollectionConstructorApi {
        return retrofit.create(CollectionConstructorApi::class.java)
    }

    @Provides
    fun provideConstructorRepository(constructorDomainRepository: CollectionConstructorRepository): CollectionConstructorDomainRepository {
        return constructorDomainRepository
    }
}