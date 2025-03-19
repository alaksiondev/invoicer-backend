package foundation.authentication.impl.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import foundation.authentication.impl.AuthTokenGenerator
import foundation.authentication.impl.AuthTokenManager
import foundation.impl.SecretKeys
import foundation.impl.SecretsProvider
import io.github.alaksion.invoicer.foundation.http.HttpCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import utils.exceptions.httpError
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal class JwtTokenGenerator(
    private val clock: Clock,
    private val secretsProvider: SecretsProvider,
) : AuthTokenGenerator {

    override fun generateAccessToken(userId: String): String {
        return createToken(
            userId = userId,
            expiration = 1.hours
        )
    }

    override fun generateRefreshToken(userId: String): String {
        return createToken(
            userId = userId,
            expiration = 999.days
        )
    }

    private fun createToken(
        userId: String,
        expiration: Duration
    ): String {
        val token = JWT.create()
            .withAudience(secretsProvider.getSecret(SecretKeys.JWT_AUDIENCE))
            .withIssuer(secretsProvider.getSecret(SecretKeys.JWT_ISSUER))
            .withClaim(JwtConfig.USER_ID_CLAIM, userId)
            .withExpiresAt(clock.now().plus(expiration).toJavaInstant())
            .sign(Algorithm.HMAC256(secretsProvider.getSecret(SecretKeys.JWT_SECRET)))

        return token
    }
}

fun AuthenticationConfig.appJwt(
    secretsProvider: SecretsProvider,
) {
    jwt("auth-jwt") {
        realm = secretsProvider.getSecret(SecretKeys.JWT_REALM)
        verifier(
            JWT
                .require(
                    Algorithm.HMAC256(
                        secretsProvider.getSecret(
                            SecretKeys.JWT_SECRET
                        )
                    )
                )
                .withAudience(secretsProvider.getSecret(SecretKeys.JWT_AUDIENCE))
                .withIssuer(secretsProvider.getSecret(SecretKeys.JWT_ISSUER))
                .build()
        )
        validate { token ->
            if (token.payload.getClaim(JwtConfig.USER_ID_CLAIM).asString().isNotBlank()) {
                JWTPrincipal(token.payload)
            } else {
                null
            }
        }
        challenge { _, _ ->
            httpError(
                message = AuthTokenManager.NOT_AUTHENTICATED_MESSAGE,
                code = HttpCode.UnAuthorized
            )
        }
    }
}


fun Route.jwtProtected(
    block: Route.() -> Unit
) {
    authenticate(JwtConfig.AUTH_NAME) {
        block()
    }
}

fun PipelineContext<Unit, ApplicationCall>.jwtUserId(): String {
    val principal = call.principal<JWTPrincipal>()

    val id = principal?.payload?.getClaim(JwtConfig.USER_ID_CLAIM)?.asString()
        ?: httpError(message = AuthTokenManager.NOT_AUTHENTICATED_MESSAGE, code = HttpCode.UnAuthorized)
    return id
}
