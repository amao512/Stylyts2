package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.CommentsApi
import kz.eztech.stylyts.data.repository.CommentsRepository
import kz.eztech.stylyts.domain.repository.CommentsDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CommentsModule {

    @Provides
    @Singleton
    fun providesCommentsApi(retrofit: Retrofit): CommentsApi {
        return retrofit.create(CommentsApi::class.java)
    }

    @Provides
    fun providesCommentsDomainRepository(commentsDomainRepository: CommentsRepository): CommentsDomainRepository {
        return commentsDomainRepository
    }
}