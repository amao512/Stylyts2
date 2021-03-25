package kz.eztech.stylyts.profile.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.profile.data.api.ProfileApi
import kz.eztech.stylyts.profile.data.repository.ProfileRepository
import kz.eztech.stylyts.profile.domain.repository.ProfileDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
@Module
class ProfileModule {

    @Provides
    @Singleton
    internal fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    fun providesProfileRepository(profileRepository: ProfileRepository): ProfileDomainRepository {
        return profileRepository
    }
}