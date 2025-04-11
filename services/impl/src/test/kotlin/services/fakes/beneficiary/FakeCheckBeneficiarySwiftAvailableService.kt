package services.fakes.beneficiary

import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import java.util.UUID

class FakeCheckBeneficiarySwiftAvailableService : CheckBeneficiarySwiftAvailableService {
    var response: suspend () -> Boolean = { true }

    var calls = 0
        private set

    override suspend fun execute(swift: String, userId: UUID): Boolean {
        calls++
        return response()
    }
}