package io.github.alaksion.invoicer.server.domain.usecase.login

import io.github.alaksion.invoicer.server.domain.model.login.LoginModel
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByEmailUseCase
import utils.authentication.api.AuthTokenManager
import utils.exceptions.notFoundError
import utils.password.PasswordEncryption

interface LoginUseCase {
    suspend fun login(model: LoginModel): String
}

internal class LoginUseCaseImpl(
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val authTokenManager: AuthTokenManager,
    private val passwordEncryption: PasswordEncryption
) : LoginUseCase {

    override suspend fun login(model: LoginModel): String {
        val account = getUserByEmailUseCase.get(model.email)
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

        return authTokenManager.generateToken(
            account.id.toString()
        )
    }
}