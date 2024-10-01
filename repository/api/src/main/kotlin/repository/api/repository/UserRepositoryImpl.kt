package repository.api.repository

import entities.UserEntity
import entities.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.api.mapper.toModel
import services.api.model.user.CreateUserModel
import services.api.model.user.UserModel
import services.api.repository.UserRepository
import utils.date.api.DateProvider
import java.util.*

internal class UserRepositoryImpl(
    private val dateProvider: DateProvider
) : UserRepository {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return newSuspendedTransaction {
            UserEntity.find { UserTable.email eq email }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return newSuspendedTransaction {
            UserEntity.findById(id)?.toModel()
        }
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return newSuspendedTransaction {
            UserTable.insertAndGetId {
                it[verified] = true
                it[email] = data.email
                it[password] = data.password
                it[updatedAt] = dateProvider.now()
                it[createdAt] = dateProvider.now()
            }.value.toString()
        }
    }

    override suspend fun deleteUser(id: UUID) {
        return newSuspendedTransaction {
            UserTable.deleteWhere { op ->
                UserTable.id.eq(id)
            }
        }
    }

}