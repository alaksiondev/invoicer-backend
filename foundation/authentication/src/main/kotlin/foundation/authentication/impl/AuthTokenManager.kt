package foundation.authentication.impl

interface AuthTokenManager {
    fun generateToken(userId: String): String
    fun generateRefreshToken(userId: String): String
    fun verifyToken(token: String): String?

    companion object {
        val NOT_AUTHENTICATED_MESSAGE = "Session is invalid or expired"
    }

    enum class Tags {
        Jwt;
    }
}

internal class AuthTokenManagerImpl(
    private val tokenGenerator: AuthTokenGenerator,
    private val tokenVerifier: AuthTokenVerifier
) : AuthTokenManager {

    override fun generateToken(userId: String): String {
        return tokenGenerator.generateAccessToken(userId)
    }

    override fun generateRefreshToken(userId: String): String {
        return tokenGenerator.generateRefreshToken(userId)
    }

    override fun verifyToken(token: String): String? {
        return tokenVerifier.verify(token)
    }

}