package services.impl.beneficiary

import utils.exceptions.http.HttpCode
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.getinvoices.InvoiceListItemModel
import repository.api.fakes.FakeBeneficiaryRepository
import repository.api.fakes.FakeInvoiceRepository
import services.fakes.beneficiary.FakeGetBeneficiaryByIdService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteBeneficiaryImplTest {

    private lateinit var service: DeleteBeneficiaryServiceImpl
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var repository: FakeBeneficiaryRepository
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        repository = FakeBeneficiaryRepository()
        getUserByIdService = FakeGetUserByIdService()
        invoiceRepository = FakeInvoiceRepository()
        service = DeleteBeneficiaryServiceImpl(
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            beneficiaryRepository = repository,
            getUserByIdService = getUserByIdService,
            invoiceRepository = invoiceRepository
        )
    }

    @Test
    fun `given no invoices attached to beneficiary then should delete beneficiary`() = runTest {
        invoiceRepository.getInvoicesByBeneficiaryIdResponse = { listOf() }

        service.execute(
            beneficiaryId = UUID.fromString("988b016b-41a9-487f-b280-283faff1d1d1"),
            userId = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36")
        )

        assertEquals(
            expected = 1,
            actual = repository.deleteCalls
        )
    }

    @Test
    fun `given invoices attached to beneficiary then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            invoiceRepository.getInvoicesByBeneficiaryIdResponse = {
                listOf(
                    InvoiceListItemModel(
                        id = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"),
                        externalId = "123",
                        senderCompany = "Sender company",
                        recipientCompany = "Recipient company",
                        issueDate = Instant.parse("2000-06-19T00:00:00Z"),
                        dueDate = Instant.parse("2000-06-20T00:00:00Z"),
                        createdAt = Instant.parse("2000-06-19T00:00:00Z"),
                        updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
                        totalAmount = 10
                    )
                )
            }

            service.execute(
                beneficiaryId = UUID.fromString("988b016b-41a9-487f-b280-283faff1d1d1"),
                userId = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36")
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }
}