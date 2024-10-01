package services.api.services.beneficiary

import services.api.model.beneficiary.BeneficiaryModel
import services.api.repository.BeneficiaryRepository
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.UUID

interface GetBeneficiaryByIdService {
    suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel
}

internal class GetBeneficiaryByIdServiceImpl(
    private val repository: BeneficiaryRepository
) : GetBeneficiaryByIdService {

    override suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel {
        val beneficiary = repository.getById(
            beneficiaryId = UUID.fromString(beneficiaryId),
        )

        if (beneficiary == null) notFoundError("Beneficiary not found")

        if (beneficiary.userId != userId) unauthorizedResourceError()

        return beneficiary
    }

}