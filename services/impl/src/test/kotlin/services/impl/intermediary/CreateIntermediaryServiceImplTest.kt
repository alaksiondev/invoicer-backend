package services.impl.intermediary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import models.intermediary.CreateIntermediaryModel
import repository.test.repository.FakeIntermediaryRepository
import repository.test.repository.FakeUserRepository
import services.test.intermediary.FakeCheckIntermediarySwiftAvailableService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateIntermediaryServiceImplTest {

    private lateinit var service: CreateIntermediaryServiceImpl
    private lateinit var repository: FakeIntermediaryRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var checkSwiftInUseService: FakeCheckIntermediarySwiftAvailableService
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var ibanValidator: FakeIbanValidator

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        repository = FakeIntermediaryRepository()
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        checkSwiftInUseService = FakeCheckIntermediarySwiftAvailableService()
        getUserByIdService = FakeGetUserByIdService()

        service = CreateIntermediaryServiceImpl(
            getUserByIdService = getUserByIdService,
            repository = repository,
            swiftValidator = swiftValidator,
            checkIntermediarySwiftAlreadyUsedService = checkSwiftInUseService,
            ibanValidator = ibanValidator
        )
    }

    @Test
    fun `should throw error when swift is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            swiftValidator.response = false

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when iban is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            ibanValidator.response = false

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(
                    name = ""
                ),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(
                    bankName = ""
                ),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank address is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(bankName = ""),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when swift is already in use`() = runTest {
        val exception = assertFailsWith<HttpError> {
            checkSwiftInUseService.response = { true }

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should return created beneficiary id`() = runTest {
        checkSwiftInUseService.response = { false }
        repository.createResponse = { "1234" }

        val response = service.create(
            model = INPUT,
            userId = userId
        )

        assertEquals(
            expected = "1234",
            actual = response
        )
    }

    companion object {
        val userId = "7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"

        val INPUT = CreateIntermediaryModel(
            name = "Sample name",
            iban = "9999",
            swift = "VWBGLCELXXX",
            bankName = "Bank of America",
            bankAddress = "Bank address"
        )
    }
}