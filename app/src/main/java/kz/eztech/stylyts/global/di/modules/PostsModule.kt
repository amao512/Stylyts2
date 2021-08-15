package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.PostsApi
import kz.eztech.stylyts.global.data.repositories.PostsRepository
import kz.eztech.stylyts.global.domain.repositories.PostsDomainRepository
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