package models.fixtures

import kotlinx.datetime.Instant
import models.user.UserModel
import java.util.*

val userModelFixture = UserModel(
    id = UUID.fromString("b23899e4-63b5-42a9-9016-4647d9ec2936"),
    password = "examplePassword",
    verified = true,
    email = "example@example.com",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z")
)