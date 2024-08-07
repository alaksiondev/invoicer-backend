package utils.authentication.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import utils.authentication.api.AuthTokenGenerator
import utils.authentication.api.AuthTokenManager
import utils.authentication.api.AuthTokenManagerImpl
import utils.authentication.api.jwt.JwtTokenGenerator

val utilsAuthenticationModule = DI.Module("utils-authentication") {

    bindProvider<AuthTokenManager> {
        AuthTokenManagerImpl(
            tokenGenerator = instance(tag = AuthTokenGenerator.Tags.Jwt)
        )
    }

    bindProvider<AuthTokenGenerator>(tag = AuthTokenGenerator.Tags.Jwt) {
        JwtTokenGenerator(
            dateProvider = instance()
        )
    }
}