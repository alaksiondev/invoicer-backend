package services.impl.intermediary

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.getinvoices.InvoiceListItemModel
import repository.api.fakes.FakeIntermediaryRepository
import repository.api.fakes.FakeInvoiceRepository
import services.fakes.intermediary.FakeGetIntermediaryByIdService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteIntermediaryImplTest {

    private lateinit var service: DeleteIntermediaryServiceImpl
    private lateinit var getIntermediaryByIdService: FakeGetIntermediaryByIdService
    private lateinit var repository: FakeIntermediaryRepository
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        getIntermediaryByIdService = FakeGetIntermediaryByIdService()
        repository = FakeIntermediaryRepository()
        getUserByIdService = FakeGetUserByIdService()
        invoiceRepository = FakeInvoiceRepository()
        service = DeleteIntermediaryServiceImpl(
            getIntermediaryByIdService = getIntermediaryByIdService,
            intermediaryRepo = repository,
            invoiceRepository = invoiceRepository,
            getUserByIdUseCase = getUserByIdService
        )
    }

    @Test
    fun `given no invoices attached to beneficiary then should delete beneficiary`() = runTest {
        invoiceRepository.getInvoicesByIntermediaryIdResponse = { listOf() }

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
            invoiceRepository.getInvoicesByIntermediaryIdResponse = {
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