package services.fakes.beneficiary

import kotlinx.datetime.Instant
import models.beneficiary.BeneficiaryModel
import services.api.services.beneficiary.GetBeneficiaryByIdService
import java.util.*

class FakeGetBeneficiaryByIdService : GetBeneficiaryByIdService {

    var response: suspend () -> BeneficiaryModel = { DEFAULT_RESPONSE }

    override suspend fun get(beneficiaryId: UUID, userId: UUID): BeneficiaryModel {
        return response()
    }

    companion object {
        val DEFAULT_RESPONSE = BeneficiaryModel(
            name = "Beneficiary Name",
            iban = "1234",
            swift = "4321",
            bankAddress = "Bank Address St 1",
            bankName = "Bank of America",
            userId = UUID.fromString("36433933-ebaf-42df-83e2-4c684119ccae"),
            id = UUID.fromString("702c32cd-fc9f-4abb-a022-414104646923"),
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z")
        )
    }
}