package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.GetIntermediaryByIdService
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal class GetIntermediaryByIdServiceImpl(
    private val repository: IntermediaryRepository
) : GetIntermediaryByIdService {

    override suspend fun get(intermediaryId: String, userId: String): IntermediaryModel {
        val intermediary = repository.getById(
            intermediaryId = UUID.fromString(intermediaryId),
        )

        if (intermediary == null) notFoundError("Intermediary not found")

        if (intermediary.userId != userId) unauthorizedResourceError()

        return intermediary
    }
}