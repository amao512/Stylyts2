package kz.eztech.stylyts.domain.usecases.shop

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class SaveOutfitConstructorUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<OutfitModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<OutfitModel> {
        return outfitsDomainRepository.saveOutfit(token, data)
    }

    fun initParam(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.data = data
    }
}