package foundation.api

import foundation.env.InvoicerEnvironment

internal class EnvSecretsProvider(
    private val environment: InvoicerEnvironment
) : SecretsProvider {

    override fun getSecret(key: SecretKeys): String {
        val path = when (key) {
            SecretKeys.DB_PASSWORD -> "database.password"
            SecretKeys.DB_USERNAME -> "database.username"
            SecretKeys.DB_URL -> "database.url"
            SecretKeys.JWT_AUDIENCE -> "jwt.audience"
            SecretKeys.JWT_ISSUER -> "jwt.issuer"
            SecretKeys.JWT_SECRET -> "jwt.secret"
            SecretKeys.JWT_REALM -> "jwt.realm"
        }

        return environment.getVariable(path).orEmpty()
    }
}