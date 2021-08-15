package kz.eztech.stylyts.ordering.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.ordering.data.api.ReferralApi
import kz.eztech.stylyts.ordering.data.repositories.ReferralsRepository
import kz.eztech.stylyts.ordering.domain.repositories.ReferralsDomainRepository
import kz.eztech.stylyts.ordering.presentation.incomes.data.UIIncomeData
import kz.eztech.stylyts.ordering.presentation.incomes.data.UIIncomeDataDelegate
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

    @Provides
    fun provideUIIncomeData(): UIIncomeData {
        return UIIncomeDataDelegate()
    }
}