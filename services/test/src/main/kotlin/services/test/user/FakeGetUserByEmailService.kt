package services.test.user

import kotlinx.datetime.LocalDate
import models.user.UserModel
import services.api.services.user.GetUserByEmailService
import java.util.*

class FakeGetUserByEmailService : GetUserByEmailService {

    var response: suspend () -> UserModel? = { DEFAULT_RESPONSE }

    override suspend fun get(email: String): UserModel? {
        return response()
    }

    companion object {
        val DEFAULT_RESPONSE = UserModel(
            id = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"),
            password = "1234",
            verified = true,
            email = "luccab.souza@gmail.com",
            createdAt = LocalDate(2000, 6, 19),
            updatedAt = LocalDate(2000, 6, 19)
        )
    }
}