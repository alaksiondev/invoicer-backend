package datasource.api.database

import models.fixtures.refreshTokenModelFixture
import models.login.RefreshTokenModel
import java.util.*

class FakeRefreshTokenDatabaseSource : RefreshTokenDatabaseSource {

    var createRefreshTokenResponse: suspend () -> Unit = {}
    var invalidateTokenResponse: suspend () -> Unit = {}
    var invalidateAllUserTokensResponse: suspend () -> Unit = {}
    var findUserTokenResponse: suspend () -> RefreshTokenModel? = { refreshTokenModelFixture }

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        return createRefreshTokenResponse()
    }

    override suspend fun invalidateToken(userId: UUID, token: String) {
        return invalidateTokenResponse()
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) {
        return invalidateAllUserTokensResponse()
    }

    override suspend fun findUserToken(userId: UUID, token: String): RefreshTokenModel? {
        return findUserTokenResponse()
    }
}