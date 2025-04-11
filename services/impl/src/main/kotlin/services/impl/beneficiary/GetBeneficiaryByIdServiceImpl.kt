package services.impl.beneficiary

import models.beneficiary.BeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetBeneficiaryByIdService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.unauthorizedResourceError
import java.util.*

internal class GetBeneficiaryByIdServiceImpl(
    private val repository: BeneficiaryRepository
) : GetBeneficiaryByIdService {

    override suspend fun get(beneficiaryId: UUID, userId: UUID): BeneficiaryModel {
        val beneficiary = repository.getById(
            beneficiaryId = beneficiaryId,
        )

        if (beneficiary == null) notFoundError("Beneficiary not found")

        if (beneficiary.userId != userId) unauthorizedResourceError()

        return beneficiary
    }

}