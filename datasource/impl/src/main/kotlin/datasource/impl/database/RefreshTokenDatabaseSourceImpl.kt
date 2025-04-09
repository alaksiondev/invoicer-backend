package datasource.impl.database

import datasource.api.database.RefreshTokenDatabaseSource
import datasource.impl.entities.RefreshTokenEntity
import datasource.impl.entities.RefreshTokensTable
import kotlinx.datetime.Clock
import models.login.RefreshTokenModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

internal class RefreshTokenDatabaseSourceImpl(
    private val dateProvider: Clock
) : RefreshTokenDatabaseSource {

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        return newSuspendedTransaction {
            RefreshTokensTable.insert {
                it[refreshToken] = token
                it[user] = userId
                it[enabled] = true
                it[createdAt] = dateProvider.now()
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun invalidateToken(
        userId: UUID,
        token: String,
    ) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.refreshToken eq token) and
                            (RefreshTokensTable.user eq userId)
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.user eq userId)
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun findUserToken(userId: UUID, token: String): RefreshTokenModel? {
        return newSuspendedTransaction {
            val data = RefreshTokenEntity.find {
                (RefreshTokensTable.refreshToken eq token) and
                        (RefreshTokensTable.user eq userId)
            }.firstOrNull()

            data?.let {
                RefreshTokenModel(
                    token = it.refreshToken,
                    userId = it.user.id.value,
                    enabled = it.enabled,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }
}