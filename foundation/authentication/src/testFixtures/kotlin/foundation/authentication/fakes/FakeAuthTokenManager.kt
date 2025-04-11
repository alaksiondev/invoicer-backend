package foundation.authentication.fakes

import foundation.authentication.impl.AuthTokenManager

class FakeAuthTokenManager : AuthTokenManager {

    var token = "1234"
    var refreshToken = "9876"
    var verify: String? = "f47ac10b-58cc-4372-a567-0e02b2c3d479"

    override fun generateToken(userId: String): String {
        return token
    }

    override fun generateRefreshToken(userId: String): String {
        return refreshToken
    }

    override fun verifyToken(token: String): String? {
        return verify
    }
}