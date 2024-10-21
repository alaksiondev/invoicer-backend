package repository.test.repository

import kotlinx.datetime.LocalDate
import models.beneficiary.BeneficiaryModel
import models.beneficiary.CreateBeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import java.util.*

class FakeBeneficiaryRepository : BeneficiaryRepository {

    var createResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var getAllResponse: suspend () -> List<BeneficiaryModel> = { listOf() }
    var getByIdResponse: suspend () -> BeneficiaryModel? = { DEFAULT_BENEFICIARY }
    var getBySwiftResponse: suspend () -> BeneficiaryModel? = { null }
    lateinit var updateBeneficiaryResponse: suspend () -> BeneficiaryModel

    var deleteCalls = 0
        private set

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return createResponse()
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        deleteCalls++
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return getByIdResponse()
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return getBySwiftResponse()
    }

    override suspend fun getAll(userId: UUID, page: Long, limit: Int): List<BeneficiaryModel> {
        return getAllResponse()
    }

    override suspend fun update(userId: UUID, beneficiaryId: UUID, model: UpdateBeneficiaryModel): BeneficiaryModel {
        return updateBeneficiaryResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"

        val DEFAULT_BENEFICIARY = BeneficiaryModel(
            name = "Name",
            iban = "Iban",
            swift = "Swift,",
            bankName = "bank name",
            bankAddress = "bank address",
            userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
            id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
            createdAt = LocalDate(2000, 6, 19),
            updatedAt = LocalDate(2000, 6, 19)
        )
    }
}