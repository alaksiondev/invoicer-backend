package utils.authentication.api.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import foundation.api.SecretKeys
import foundation.api.SecretsProvider

interface InvoicerJwtVerifier {
    fun verify(token: String): String?
}

internal class InvoicerJwtVerifierImpl(
    secretsProvider: SecretsProvider
) : InvoicerJwtVerifier {

    private val verifier = JWT
        .require(Algorithm.HMAC256(secretsProvider.getSecret(SecretKeys.JWT_SECRET)))
        .withAudience(secretsProvider.getSecret(SecretKeys.JWT_AUDIENCE))
        .withIssuer(secretsProvider.getSecret(SecretKeys.JWT_ISSUER))
        .build()


    override fun verify(token: String): String? {
        return kotlin.runCatching {
            verifier.verify(token)
        }.fold(
            onSuccess = { it.getClaim(JwtConfig.USER_ID_CLAIM).asString() },
            onFailure = { null }
        )
    }
}