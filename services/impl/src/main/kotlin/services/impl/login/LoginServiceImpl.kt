package services.impl.login

import foundation.validator.api.EmailValidator
import models.login.AuthTokenModel
import models.login.LoginModel
import services.api.services.login.LoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.user.GetUserByEmailService
import utils.authentication.api.AuthTokenManager
import utils.exceptions.badRequestError
import utils.exceptions.notFoundError
import utils.password.PasswordEncryption

internal class LoginServiceImpl(
    private val getUserByEmailService: GetUserByEmailService,
    private val authTokenManager: AuthTokenManager,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator,
    private val storeRefreshTokenService: StoreRefreshTokenService
) : LoginService {

    override suspend fun login(model: LoginModel): AuthTokenModel {

        if (emailValidator.validate(model.email).not()) badRequestError(message = "Invalid e-mail format")

        val account = getUserByEmailService.get(model.email)
            ?: notFoundError(message = "Invalid credentials, user not found.")

        if (passwordEncryption.comparePassword(
                encryptedPassword = account.password,
                rawPassword = model.password
            ).not()
        ) {
            notFoundError(
                message = "Invalid credentials, user not found"
            )
        }

        val accessToken = authTokenManager.generateToken(account.id.toString())
        val refreshToken = authTokenManager.generateRefreshToken(account.id.toString())

        storeRefreshTokenService.storeRefreshToken(
            token = refreshToken,
            userId = account.id.toString()
        )

        return AuthTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}