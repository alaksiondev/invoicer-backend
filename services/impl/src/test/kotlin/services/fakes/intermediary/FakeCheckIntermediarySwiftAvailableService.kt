package services.fakes.intermediary

import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import java.util.*

class FakeCheckIntermediarySwiftAvailableService : CheckIntermediarySwiftAvailableService {
    var response: suspend () -> Boolean = { true }


    override suspend fun execute(swift: String, userId: UUID): Boolean {
        return response()
    }
}