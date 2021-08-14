package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.ProfileApi
import kz.eztech.stylyts.data.repository.ProfileRepository
import kz.eztech.stylyts.domain.repository.ProfileDomainRepository
import kz.eztech.stylyts.presentation.fragments.profile.data.UIProfileFilterData
import kz.eztech.stylyts.presentation.fragments.profile.data.UIProfileFilterDataDelegate
import kz.eztech.stylyts.presentation.fragments.shop.data.UIShopProfileData
import kz.eztech.stylyts.presentation.fragments.shop.data.UIShopProfileDataDelegate
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