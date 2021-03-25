package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.ProfileApi
import kz.eztech.stylyts.data.repository.profile.ProfileRepository
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
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