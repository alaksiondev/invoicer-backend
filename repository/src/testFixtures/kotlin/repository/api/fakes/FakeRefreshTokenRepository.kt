package repository.api.fakes

import models.login.RefreshTokenModel
import repository.api.repository.RefreshTokenRepository
import java.util.*

class FakeRefreshTokenRepository : RefreshTokenRepository {

    var invalidateCallStack = mutableListOf<Pair<UUID, String>>()

    var userToken: suspend () -> RefreshTokenModel? = { null }

    var storeCalls = 0
        private set

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        storeCalls++
    }

    override suspend fun invalidateToken(userId: UUID, token: String) {
        invalidateCallStack.add(userId to token)
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) = Unit

    override suspend fun findUserToken(userId: UUID, token: String): RefreshTokenModel? {
        return userToken()
    }
}