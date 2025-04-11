package services.impl.beneficiary

import utils.exceptions.http.HttpCode
import kotlinx.coroutines.test.runTest
import models.beneficiary.UpdateBeneficiaryModel
import models.fixtures.beneficiaryModelFixture
import models.fixtures.updateBeneficiaryModelFixture
import repository.api.fakes.FakeBeneficiaryRepository
import io.github.alaksion.invoicer.utils.fakes.FakeIbanValidator
import io.github.alaksion.invoicer.utils.fakes.FakeSwiftValidator
import services.fakes.beneficiary.FakeCheckBeneficiarySwiftAvailableService
import services.fakes.beneficiary.FakeGetBeneficiaryByIdService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateBeneficiaryServiceImplTest {

    private lateinit var serviceImpl: UpdateBeneficiaryServiceImpl
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var swiftInUseService: FakeCheckBeneficiarySwiftAvailableService
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var ibanValidator: FakeIbanValidator
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftInUseService = FakeCheckBeneficiarySwiftAvailableService()
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        getUserByIdService = FakeGetUserByIdService()

        serviceImpl = UpdateBeneficiaryServiceImpl(
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            beneficiaryRepository = beneficiaryRepository,
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator,
            checkBeneficiarySwiftAvailableService = swiftInUseService,
            getUserByIdService = getUserByIdService
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
                beneficiaryId = BENEFICIARY_ID,
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
                beneficiaryId = BENEFICIARY_ID,
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
                beneficiaryId = BENEFICIARY_ID,
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
                beneficiaryId = BENEFICIARY_ID,
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
                beneficiaryId = BENEFICIARY_ID,
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
                beneficiaryId = BENEFICIARY_ID,
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

        beneficiaryRepository.updateBeneficiaryResponse = {
            beneficiaryModelFixture
        }

        val result = serviceImpl.execute(
            model = UPDATED_REQUEST,
            beneficiaryId = BENEFICIARY_ID,
            userId = USER_ID
        )

        assertEquals(
            expected = beneficiaryModelFixture,
            actual = result
        )
    }

    @Test
    fun `should ignore swift check if it did not change`() = runTest {
        val beneficiaryId = UUID.fromString("aae887c0-732e-42d8-ac79-0d1953a7d3ec")
        mockIdCodes(
            ibanValid = true,
            swiftValid = true
        )

        beneficiaryRepository.updateBeneficiaryResponse = {
            beneficiaryModelFixture
        }

        getBeneficiaryByIdService.response = {
            beneficiaryModelFixture.copy(
                swift = "1234",
                id = beneficiaryId
            )
        }

        serviceImpl.execute(
            model = updateBeneficiaryModelFixture.copy(
                swift = "1234"
            ),
            beneficiaryId = beneficiaryId,
            userId = USER_ID
        )

        assertEquals(expected = 0, actual = swiftInUseService.calls)
    }

    private fun mockIdCodes(
        ibanValid: Boolean,
        swiftValid: Boolean
    ) {
        ibanValidator.response = ibanValid
        swiftValidator.response = swiftValid
    }

    companion object {
        val UPDATED_REQUEST = UpdateBeneficiaryModel(
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