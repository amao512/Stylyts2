package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.CommentsApi
import kz.eztech.stylyts.global.data.repositories.CommentsRepository
import kz.eztech.stylyts.global.domain.repositories.CommentsDomainRepository
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