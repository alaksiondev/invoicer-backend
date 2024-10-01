package services.api.services.user

import foundation.validator.api.EmailValidator
import services.api.model.user.CreateUserModel
import services.api.repository.UserRepository
import utils.exceptions.HttpCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import utils.password.PasswordEncryption
import utils.password.PasswordStrength
import utils.password.PasswordValidator

interface CreateUserService {
    suspend fun create(userModel: CreateUserModel): String
}

internal class CreateUserServiceImpl(
    private val getUserByEmailService: GetUserByEmailService,
    private val passwordValidator: PasswordValidator,
    private val repository: UserRepository,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator
) : CreateUserService {

    override suspend fun create(userModel: CreateUserModel): String {

        if (userModel.email != userModel.confirmEmail)
            badRequestError(message = "E-mails must mach")

        if (emailValidator.validate(userModel.email).not())
            badRequestError(message = "invalid email format: ${userModel.email}")

        if (getUserByEmailService.get(userModel.email) != null)
            httpError(message = "E-mail ${userModel.email} already in use", code = HttpCode.Conflict)

        val passwordLevel = passwordValidator.validate(userModel.password)

        if (passwordLevel is PasswordStrength.WEAK) {
            badRequestError(message = passwordLevel.reason)
        }

        val encryptedPassword = passwordEncryption.encryptPassword(userModel.password)

        return repository.createUser(userModel.copy(password = encryptedPassword))
    }

}