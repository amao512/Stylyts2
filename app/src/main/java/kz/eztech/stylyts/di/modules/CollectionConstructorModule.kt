package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.repository.posts.PostsRepository
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CollectionConstructorModule {

    @Provides
    @Singleton
    fun provideConstructorApi(retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)
    }

    @Provides
    fun provideConstructorRepository(constructorDomainRepository: PostsRepository): PostsDomainRepository {
        return constructorDomainRepository
    }
}