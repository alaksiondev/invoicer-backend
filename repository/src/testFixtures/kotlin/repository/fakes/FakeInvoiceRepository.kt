package repository.fakes

import models.InvoiceModel
import models.createinvoice.CreateInvoiceModel
import models.fixtures.invoiceListItemModelFixture
import models.fixtures.invoiceListModelFixture
import models.fixtures.invoiceModelFixture
import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import repository.InvoiceRepository
import java.util.*

class FakeInvoiceRepository : InvoiceRepository {

    var getInvoicesByBeneficiaryIdResponse: () -> List<InvoiceListItemModel> = { listOf(invoiceListItemModelFixture) }
    var getInvoicesByIntermediaryIdResponse: () -> List<InvoiceListItemModel> = { listOf(invoiceListItemModelFixture) }
    var getInvoiceByExternalIdResponse: () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoiceByIdResponse: () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoicesResponse: () -> InvoiceListModel = { invoiceListModelFixture }
    var createInvoiceResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var deleteResponse: suspend () -> Unit = {}

    val deleteCallStack = mutableListOf<UUID>()
    val createCallStack = mutableListOf<String>()

    override suspend fun createInvoice(data: CreateInvoiceModel, userId: UUID): String {
        val response = createInvoiceResponse()
        createCallStack.add(response)
        return response
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        return getInvoiceByIdResponse()
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return getInvoiceByExternalIdResponse()
    }

    override suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID
    ): InvoiceListModel {
        return getInvoicesResponse()
    }

    override suspend fun delete(id: UUID) {
        deleteCallStack.add(id)
        return deleteResponse()
    }

    override suspend fun getInvoicesByBeneficiaryId(beneficiaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByBeneficiaryIdResponse()
    }

    override suspend fun getInvoicesByIntermediaryId(intermediaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByIntermediaryIdResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"
    }
}