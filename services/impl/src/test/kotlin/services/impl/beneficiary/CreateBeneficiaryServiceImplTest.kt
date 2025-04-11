package services.impl.beneficiary

import io.github.alaksion.invoicer.utils.fakes.FakeIbanValidator
import io.github.alaksion.invoicer.utils.fakes.FakeSwiftValidator
import utils.exceptions.http.HttpCode
import kotlinx.coroutines.test.runTest
import models.beneficiary.CreateBeneficiaryModel
import repository.api.fakes.FakeBeneficiaryRepository
import repository.api.fakes.FakeUserRepository
import services.fakes.beneficiary.FakeCheckBeneficiarySwiftAvailableService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateBeneficiaryServiceImplTest {

    private lateinit var service: CreateBeneficiaryServiceImpl
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var checkSwiftInUseService: FakeCheckBeneficiarySwiftAvailableService
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var ibanValidator: FakeIbanValidator

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        checkSwiftInUseService = FakeCheckBeneficiarySwiftAvailableService()
        getUserByIdService = FakeGetUserByIdService()

        service = CreateBeneficiaryServiceImpl(
            getUserByIdService = getUserByIdService,
            repository = beneficiaryRepository,
            checkSwiftService = checkSwiftInUseService,
            swiftValidator = swiftValidator,
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
        beneficiaryRepository.createResponse = { "1234" }

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
        val userId = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8")

        val INPUT = CreateBeneficiaryModel(
            name = "Sample name",
            iban = "9999",
            swift = "VWBGLCELXXX",
            bankName = "Bank of America",
            bankAddress = "Bank address"
        )
    }
}