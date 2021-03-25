package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.CollectionConstructorApi
import kz.eztech.stylyts.data.repository.collection_constructor.CollectionConstructorRepository
import kz.eztech.stylyts.domain.repository.collection_constructor.CollectionConstructorDomainRepository
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