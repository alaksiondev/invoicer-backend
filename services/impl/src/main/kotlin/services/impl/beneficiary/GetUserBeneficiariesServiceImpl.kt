package services.impl.beneficiary

import models.beneficiary.UserBeneficiaries
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetUserBeneficiariesService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.unauthorizedResourceError
import java.util.*

internal class GetUserBeneficiariesServiceImpl(
    private val repository: BeneficiaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
) : GetUserBeneficiariesService {

    override suspend fun execute(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries {
        getUserByIdUseCase.get(userId)

        val beneficiaries = repository.getAll(
            userId = userId,
            page = page,
            limit = limit
        )

        if (beneficiaries.items.any { beneficiary ->
                beneficiary.userId != userId
            }
        ) {
            unauthorizedResourceError()
        }

        return beneficiaries
    }
}