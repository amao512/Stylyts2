package kz.eztech.stylyts.profile.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.profile.data.api.ProfileApi
import kz.eztech.stylyts.profile.data.repositories.ProfileRepository
import kz.eztech.stylyts.profile.domain.repositories.ProfileDomainRepository
import kz.eztech.stylyts.profile.presentation.profile.data.UIProfileFilterData
import kz.eztech.stylyts.profile.presentation.profile.data.UIProfileFilterDataDelegate
import kz.eztech.stylyts.profile.presentation.profile.data.UIShopProfileData
import kz.eztech.stylyts.profile.presentation.profile.data.UIShopProfileDataDelegate
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

    @Provides
    fun providesUIProfileFilterData(): UIProfileFilterData {
        return UIProfileFilterDataDelegate()
    }

    @Provides
    fun providesUIShopProfileData(): UIShopProfileData {
        return UIShopProfileDataDelegate()
    }
}