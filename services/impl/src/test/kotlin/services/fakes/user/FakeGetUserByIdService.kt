package services.fakes.user

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.user.UserModel
import services.api.services.user.GetUserByIdService
import java.util.*

class FakeGetUserByIdService : GetUserByIdService {

    var response: suspend () -> UserModel = { DEFAULT_RESPONSE }

    override suspend fun get(id: UUID): UserModel {
        return response()
    }

    companion object {
        val DEFAULT_RESPONSE = UserModel(
            id = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"),
            password = "1234",
            verified = true,
            email = "luccab.souza@gmail.com",
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z")
        )
    }
}