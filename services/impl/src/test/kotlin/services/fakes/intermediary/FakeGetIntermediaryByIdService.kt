package services.fakes.intermediary

import kotlinx.datetime.Instant
import models.intermediary.IntermediaryModel
import services.api.services.intermediary.GetIntermediaryByIdService
import java.util.*

class FakeGetIntermediaryByIdService : GetIntermediaryByIdService {

    var response: suspend () -> IntermediaryModel = { DEFAULT_RESPONSE }

    override suspend fun get(intermediaryId: UUID, userId: UUID): IntermediaryModel {
        return DEFAULT_RESPONSE
    }

    companion object {
        val DEFAULT_RESPONSE = IntermediaryModel(
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