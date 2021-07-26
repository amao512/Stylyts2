package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.ReferralApi
import kz.eztech.stylyts.data.repository.ReferralsRepository
import kz.eztech.stylyts.domain.repository.ReferralsDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ReferralsModule {

    @Provides
    @Singleton
    fun provideReferralApi(retrofit: Retrofit): ReferralApi {
        return retrofit.create(ReferralApi::class.java)
    }

    @Provides
    fun provideReferralsDomainRepository(referralsRepository: ReferralsRepository): ReferralsDomainRepository {
        return referralsRepository
    }
}