package services.impl.intermediary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import models.intermediary.UpdateIntermediaryModel
import repository.test.repository.FakeIntermediaryRepository
import services.test.beneficiary.FakeCheckBeneficiarySwiftAvailableService
import services.test.intermediary.FakeCheckIntermediarySwiftAvailableService
import services.test.intermediary.FakeGetIntermediaryByIdService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateIntermediaryServiceImplTest {

    private lateinit var serviceImpl: UpdateIntermediaryServiceImpl
    private lateinit var getIntermediaryByIdService: FakeGetIntermediaryByIdService
    private lateinit var swiftInUseService: FakeCheckIntermediarySwiftAvailableService
    private lateinit var intermediaryRepository: FakeIntermediaryRepository
    private lateinit var ibanValidator: FakeIbanValidator
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        intermediaryRepository = FakeIntermediaryRepository()
        swiftInUseService = FakeCheckIntermediarySwiftAvailableService()
        getIntermediaryByIdService = FakeGetIntermediaryByIdService()
        getUserByIdService = FakeGetUserByIdService()

        serviceImpl = UpdateIntermediaryServiceImpl(
            getUserByIdService = getUserByIdService,
            intermediaryRepository = intermediaryRepository,
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator,
            checkIntermediarySwiftAlreadyUsedService = swiftInUseService,
            getIntermediaryByIdService = getIntermediaryByIdService
        )
    }

    @Test
    fun `given invalid swift code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = false
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST,
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid iban code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = false,
                swiftValid = true
            )


            serviceImpl.execute(
                model = UPDATED_REQUEST,
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    name = ""
                ),
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    bankName = ""
                ),
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank address then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    bankAddress = ""
                ),
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given swift code already in use then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            swiftInUseService.response = { true }

            serviceImpl.execute(
                model = UPDATED_REQUEST,
                intermediaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }

    @Test
    fun `valid payload then should update model`() = runTest {
        mockIdCodes(
            ibanValid = true,
            swiftValid = true
        )

        swiftInUseService.response = { false }

        intermediaryRepository.updateResponse = {
            IntermediaryTestData.intermediary
        }

        val result = serviceImpl.execute(
            model = UPDATED_REQUEST,
            intermediaryId = BENEFICIARY_ID,
            userId = USER_ID
        )

        assertEquals(
            expected = IntermediaryTestData.intermediary,
            actual = result
        )
    }

    private fun mockIdCodes(
        ibanValid: Boolean,
        swiftValid: Boolean
    ) {
        ibanValidator.response = ibanValid
        swiftValidator.response = swiftValid
    }

    companion object {
        val UPDATED_REQUEST = UpdateIntermediaryModel(
            name = "new name",
            iban = "999",
            swift = "1234",
            bankName = "Bank of America",
            bankAddress = "Bank Address"
        )

        val BENEFICIARY_ID = "aae887c0-732e-42d8-ac79-0d1953a7d3ec"
        val USER_ID = "56de1427-e841-47f5-a14d-d0b9503b5a29"
    }

}