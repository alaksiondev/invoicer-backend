package services.impl.intermediary

import io.github.alaksion.invoicer.utils.fakes.FakeIbanValidator
import io.github.alaksion.invoicer.utils.fakes.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import models.fixtures.intermediaryModelFixture
import models.intermediary.UpdateIntermediaryModel
import repository.api.fakes.FakeIntermediaryRepository
import services.fakes.intermediary.FakeCheckIntermediarySwiftAvailableService
import services.fakes.intermediary.FakeGetIntermediaryByIdService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import java.util.*
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
            intermediaryModelFixture
        }

        val result = serviceImpl.execute(
            model = UPDATED_REQUEST,
            intermediaryId = BENEFICIARY_ID,
            userId = USER_ID
        )

        assertEquals(
            expected = intermediaryModelFixture,
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

        val BENEFICIARY_ID = UUID.fromString("aae887c0-732e-42d8-ac79-0d1953a7d3ec")
        val USER_ID = UUID.fromString("56de1427-e841-47f5-a14d-d0b9503b5a29")
    }

}