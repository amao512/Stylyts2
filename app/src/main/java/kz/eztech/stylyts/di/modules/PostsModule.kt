package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.repository.PostsRepository
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class PostsModule {

    @Provides
    @Singleton
    internal fun providePostsApi(retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)
    }

    @Provides
    fun providePostsRepository(postsDomainRepository: PostsRepository): PostsDomainRepository {
        return postsDomainRepository
    }
}