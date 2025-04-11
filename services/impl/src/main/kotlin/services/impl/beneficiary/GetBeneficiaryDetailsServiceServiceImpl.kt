package services.impl.beneficiary

import models.beneficiary.BeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetBeneficiaryDetailsService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.unauthorizedResourceError
import java.util.*

internal class GetBeneficiaryDetailsServiceServiceImpl(
    private val repository: BeneficiaryRepository,
    private val getUserService: GetUserByIdService,
) : GetBeneficiaryDetailsService {

    override suspend fun getBeneficiaryDetails(
        userId: UUID,
        beneficiaryId: UUID
    ): BeneficiaryModel {
        val user = getUserService.get(userId)

        val beneficiary = repository.getById(
            beneficiaryId
        )

        if (beneficiary == null) {
            notFoundError("Beneficiary not found")
        }

        if (beneficiary.userId != user.id) {
            unauthorizedResourceError()
        }

        return BeneficiaryModel(
            name = beneficiary.name,
            iban = beneficiary.iban,
            swift = beneficiary.swift,
            bankName = beneficiary.bankName,
            bankAddress = beneficiary.bankAddress,
            userId = beneficiary.userId,
            id = beneficiary.id,
            createdAt = beneficiary.createdAt,
            updatedAt = beneficiary.updatedAt
        )
    }
}